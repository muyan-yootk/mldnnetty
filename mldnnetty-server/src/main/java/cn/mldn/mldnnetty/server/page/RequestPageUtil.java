package cn.mldn.mldnnetty.server.page;

import java.io.File;
import java.util.Iterator;
import java.util.Set;

import javax.activation.MimetypesFileTypeMap;

import cn.mldn.commons.ServerInfo;
import cn.mldn.commons.http.HttpSession;
import cn.mldn.mldnnetty.server.http.HttpSessionManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelProgressiveFuture;
import io.netty.channel.ChannelProgressiveFutureListener;
import io.netty.handler.codec.http.Cookie;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderUtil;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.ServerCookieDecoder;
import io.netty.handler.codec.http.ServerCookieEncoder;
import io.netty.handler.codec.http.multipart.DiskFileUpload;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

public class RequestPageUtil {
	private HttpResponse response ;
	private HttpRequest request ;
	private ChannelHandlerContext ctx ; 
	private HttpSession session ;
	private HttpContent content ;

	public RequestPageUtil(HttpRequest request , HttpContent content , ChannelHandlerContext ctx) {
		this.request = request ;
		this.content = content ;
		this.ctx = ctx ;
		this.handleUrl(this.request.uri()); // 根据路径选择要处理的方法
	}
	
	private void handleUrl(String uri) { 
		// System.out.println("uri = " + uri + "、method = " + this.request.method());
		if ("/form".equals(uri)) {	// 现在做的是一个表单路径 
			if (HttpMethod.GET.equals(this.request.method())) {
				this.form();
			} 
		} else if (uri.startsWith("/param")) {	// 处理param的请求操作
			if (HttpMethod.POST.equals(this.request.method())) {
				if (this.content != null) {
					this.param();  // 进行所有请求参数的接收
				}
			}
		} else if (uri.startsWith("/images")) {	// 此时是一个图片的访问路径
			if (HttpMethod.GET.equals(this.request.method())) {
				this.images() ;
			}
		} else if ("/favicon.ico".equals(uri)) {
			this.favicon() ;
		} else if (uri.startsWith("http://" + ServerInfo.HOSTNAME + ":" + ServerInfo.PORT + "/images/")) { // 通过netty客户端要下载数据
			this.images(); 
		}
	} 
	private void favicon() {
		try {
			this.sendImage("mldn.ico");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void images() {
		if (this.content != null) { // 进行内容处理
			String fileName = this.request.uri().substring(this.request.uri().lastIndexOf("/") + 1) ;
			// System.out.println("图片名称 = " + fileName + "、content = " + this.content);
			try {
				this.sendImage(fileName); // 发送图片
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void sendImage(String fileName) throws Exception { // 根据图片名称进行图片发送
		String filePath = DiskFileUpload.baseDirectory + fileName ; // 图片路径
		File sendFile = new File(filePath) ; // 定义要发送的图片位置
		// 创建一个HTTP回应对象，设置HTTP协议以及状态码
		HttpResponse imageResponse = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK) ;
		HttpHeaderUtil.setContentLength(imageResponse, sendFile.length());	// 设置回应数据长度
		// 对于响应类型的处理应该可以根据文件动态获取
		MimetypesFileTypeMap mimeMap = new MimetypesFileTypeMap() ;
		imageResponse.headers().set(HttpHeaderNames.CONTENT_TYPE,mimeMap.getContentType(sendFile)) ;
		if (HttpHeaderUtil.isKeepAlive(this.request)) {	// 判断该请求是否还活着
			imageResponse.headers().set(HttpHeaderNames.CONNECTION,HttpHeaderValues.KEEP_ALIVE) ; // 设置存活的状态
		}
		this.ctx.write(imageResponse) ;// 回应请求
		this.ctx.write(new ChunkedFile(sendFile),ctx.newProgressivePromise().addListener(new ChannelProgressiveFutureListener() {
			@Override
			public void operationComplete(ChannelProgressiveFuture future) throws Exception {
				System.out.println("************** 图片传输完成。");
			}
			@Override
			public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) throws Exception {
				if (total < 0) {
					System.out.println("### 传输速度：" + progress);
				} else {
					System.out.println("### 传输速度：" + progress + " / " + total);
				}
			}
		})) ;
		// 这个时候对于发送的图片客户端是无法收取的，因为在HTTP协议里面还需要传入一个空消息内容才表示传输完成
		ChannelFuture lastFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT) ; // 发送空消息
		if (HttpHeaderUtil.isKeepAlive(this.request)) {	// 依然还在连接
			lastFuture.addListener(ChannelFutureListener.CLOSE) ; // 关闭发送通道
		}
	} 
	
	private void param() {
		RequestParameterUtil paramUtil = new RequestParameterUtil(this.request, this.content);
		String fileName = paramUtil.saveFile("photo") ;
		String content = "<html><head><title>MLDN-NETTY开发框架</title></head>" + 
				"<body><h1>www.mldn.cn</h1>"
				+ "<h1>【请求参数】msg = " + paramUtil.getParameter("msg") + "</h1>"
				+ "<h1>【请求参数】inst = " + paramUtil.getParameterValues("inst") + "</h1>"
				+ "<h1>【请求参数】photo = " + fileName + "</h1>"
				+ "<img src='/images/" + fileName + "'>"
				+ "</body></html>";
		this.responseWrite(content); 
	}
	 
	private void form() {
		String content = "<html><head><title>MLDN-NETTY开发框架</title></head>" + 
				"<body><h1>www.mldn.cn</h1>"
				+ "<form method='post' action='/param' enctype='multipart/form-data'>"
				+ "	信息：<input type='text' name='msg' id='msg' value='魔乐科技（MLDN）'><br>"
				+ "	兴趣：<input type='checkbox' name='inst' id='inst' value='唱歌' checked>唱歌"
				+ "		<input type='checkbox' name='inst' id='inst' value='看书' checked>看书"
				+ "		<input type='checkbox' name='inst' id='inst' value='学习' checked>学习"
				+ "		<input type='checkbox' name='inst' id='inst' value='旅游' checked>旅游<br>"
				+ "照片：	<input type='file' name='photo'><br>"
				+ "<input type='submit' value='提交'>"
				+ "<input type='reset' value='重置'>"
				+ "</form>"
				+ "</body></html>";
		this.responseWrite(content); 
	} 
	
	/**
	 * 进行Session数据的创建
	 * @param exists 请求cookie是否存在有session的判断，如果有session传递是true，否则传递的是false
	 */
	private void setSessionId(boolean exists) {	// 设置sessionId
		if (!exists) {	// 此时发送的request请求之中没有指定的Cookie内容
			// 根据自定义的session管理器创建有一个新的sessionid的内容，并且利用定义的常量作为Cookie名称
			String encode = ServerCookieEncoder.encode(HttpSession.SESSIONID,HttpSessionManager.createSession()) ;
			// HttpSessionManager类里面需要保留有一个Map集合进行全部Session数据的存储
			this.response.headers().set(HttpHeaderNames.SET_COOKIE,encode) ; // 保存Cookie数据
		}
	}
	/**
	 * 通过请求的Cookie之中分析是否存在有指定名称的内容存在，该内容为SessionID
	 * @return 如果存在返回true，不存在返回false
	 */
	private boolean isHasSessionId() {
		String cookieStr = (String) this.request.headers().get("Cookie") ; // 获取全部的请求Cookie数据
		if (cookieStr == null || "".equals(cookieStr)) {
			return false ; // 没有指定内容存在
		}
		try {
			Set<Cookie> set = ServerCookieDecoder.decode(cookieStr) ; // 通过字符串解析出全部的Cookie数据
			Iterator<Cookie> iter = set.iterator() ; 
			while (iter.hasNext()) {
				Cookie cookie = iter.next() ; // 获取每一个Cookie的内容
				if (HttpSession.SESSIONID.equals(cookie.name())) {	// 存在有指定名称的Cookie
					if (HttpSessionManager.isExists(cookie.value())) {	// 该session存在
						this.session = HttpSessionManager.getSession(cookie.value()) ; 
						return true ;
					} 
				} 
			}
		} catch (Exception e) {}
		return false ;
	}
	public void responseWrite(String content) {
		// 在Netty里面如果要进行传输处理则需要依靠ByteBuf来完成
		ByteBuf buf = Unpooled.copiedBuffer(content,CharsetUtil.UTF_8) ;
		// 由于实现的是一个HTTP协议，那么在进行响应的时候除了数据显示之外还需要考虑HTTP头信息内容
		this.response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
		this.setSessionId(this.isHasSessionId()); // 判断sessionid是否存在，不存在创建新的
		// 设置响应的MIME类型
		this.response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/html;charset=UTF-8") ;
		// 设置响应的数据长度
		this.response.headers().set(HttpHeaderNames.CONTENT_LENGTH, String.valueOf(buf.readableBytes()));
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE) ;
	}
}
