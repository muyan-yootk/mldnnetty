package cn.mldn.mldnnetty.server.handle;

import cn.mldn.commons.DefaultNettyInfo;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class EchoServerHandler extends ChannelHandlerAdapter {
	/**
	 * 当接收到消息之后会自动调用此方法对消息内容进行处理；
	 * @param msg 接收到的消息，这个消息可能是各种类型的数据，本程序中使用的是ByteBuf
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String inputStr = (String) msg ; // 2、得到用户发送的数据
		System.err.println("｛服务器｝" + inputStr);
		String echoContent = "【ECHO】" + inputStr + DefaultNettyInfo.SEPARATOR ;
		ctx.writeAndFlush(echoContent) ;
	} 
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.err.println("〖服务器端-生命周期〗服务器出现异常。");
		// ctx.close() ;
	}
}
