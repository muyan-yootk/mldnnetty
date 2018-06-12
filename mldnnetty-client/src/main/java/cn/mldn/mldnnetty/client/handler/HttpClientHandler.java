package cn.mldn.mldnnetty.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.ServerCookieDecoder;
import io.netty.util.CharsetUtil;

public class HttpClientHandler extends ChannelHandlerAdapter {
	private static int count = 1 ;
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println(count ++ + "、*********************************** " + msg.getClass());
		if (msg instanceof HttpResponse) {	// 接收回应
			HttpResponse response = (HttpResponse) msg ; 
			System.out.println("【客户端】ContentType = " + response.headers().get(HttpHeaderNames.CONTENT_TYPE));
			System.out.println("【客户端】ContentLength = " + response.headers().get(HttpHeaderNames.CONTENT_LENGTH));
			System.out.println("【客户端】SET-COOKIE = " + ServerCookieDecoder.decode(response.headers().get(HttpHeaderNames.SET_COOKIE).toString()));
		}
		if (msg instanceof HttpContent) {	// 接收内容
			HttpContent content = (HttpContent) msg ; 
			ByteBuf buf = content.content() ; // 获取所有的回应内容
			System.out.println(buf.toString(CharsetUtil.UTF_8));
		}
	} 

}
