package cn.mldn.mldnnetty.server.handle;

import java.util.Iterator;
import java.util.Set;

import cn.mldn.commons.http.HttpSession;
import cn.mldn.mldnnetty.server.http.HttpSessionManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.Cookie;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.ServerCookieDecoder;
import io.netty.handler.codec.http.ServerCookieEncoder;
import io.netty.util.CharsetUtil;

public class HttpServerHandler extends ChannelHandlerAdapter {
	private HttpRequest request ;
	private HttpResponse response ;
	private HttpSession session ;
	
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
		return false ;
	}
	
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof HttpRequest) {	// 现在只是发送了一个HTTP请求
			this.request = (HttpRequest) msg ; // 进行请求的接收
			System.out.println("【HTTP接收请求】uri = " + request.uri() + "、method = " + request.method());
			String content = "<html><head><title>MLDN-NETTY开发框架</title></head>" + "<body><h1>www.mldn.cn</h1>"
					+ "<h1>好好学习，天天向上！</h1></body></html>";
			this.responseWrite(ctx, content);  
		}
	} 

	private void responseWrite(ChannelHandlerContext ctx, String content) {
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
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
	}
}
