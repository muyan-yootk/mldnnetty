package cn.mldn.mldnnetty.server.handle;

import cn.mldn.mldnnetty.server.page.RequestPageUtil;
import cn.mldn.mldnnetty.server.page.RequestParameterUtil;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;

public class HttpServerHandler extends ChannelHandlerAdapter {
	private HttpRequest request ;
	private HttpContent content ; 
	
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof HttpRequest) {	// 现在只是发送了一个HTTP请求
			this.request = (HttpRequest) msg ; // 进行请求的接收
			RequestParameterUtil paramUtil = new RequestParameterUtil(this.request, this.content) ;
			System.out.println("【1 - GET请求参数】" + paramUtil.getParameter("info"));
			RequestPageUtil rpu = new RequestPageUtil(this.request,this.content, ctx) ;
		}
		if (msg instanceof HttpContent) {	// 现在只是发送了一个HTTP请求
			this.content = (HttpContent) msg ; // 进行请求的接收
			RequestPageUtil rpu = new RequestPageUtil(this.request,this.content, ctx) ;
		}
	} 


	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
	}
}
