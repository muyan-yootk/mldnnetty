package cn.mldn.mldnnetty.server.handle;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.CharsetUtil;

public class WebSocketServerHandler extends ChannelHandlerAdapter {
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// 在WebSocket之中所有的数据都是通过WebSocketFrame来进行包装的
		TextWebSocketFrame textFrame = (TextWebSocketFrame) msg ; // 获取发送的文本数据
		String echoValue = "【ECHO】" + textFrame.content().toString(CharsetUtil.UTF_8) ;
		ctx.writeAndFlush(new TextWebSocketFrame(echoValue)) ; 
	} 


	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
	}
}
