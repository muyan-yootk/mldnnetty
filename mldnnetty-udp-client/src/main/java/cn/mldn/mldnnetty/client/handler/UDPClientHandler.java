package cn.mldn.mldnnetty.client.handler;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

public class UDPClientHandler extends ChannelHandlerAdapter {
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		DatagramPacket packet = (DatagramPacket) msg ; // 接收数据包
		System.out.println("【客户端（监听者）】数据：" + packet.content().toString(CharsetUtil.UTF_8));
		System.out.println("【客户端（监听者）】来源：" + packet.sender());
	}
	@Override 
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close() ;
	}
}
