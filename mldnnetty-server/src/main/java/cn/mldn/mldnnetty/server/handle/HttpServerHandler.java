package cn.mldn.mldnnetty.server.handle;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

public class HttpServerHandler extends ChannelHandlerAdapter {
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof HttpRequest) {	// 现在只是发送了一个HTTP请求
			HttpRequest request = (HttpRequest) msg ; // 进行请求的接收
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
		HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
		// 设置响应的MIME类型
		response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/html;charset=UTF-8") ;
		// 设置响应的数据长度
		response.headers().set(HttpHeaderNames.CONTENT_LENGTH, String.valueOf(buf.readableBytes()));
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE) ;
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
	}
}
