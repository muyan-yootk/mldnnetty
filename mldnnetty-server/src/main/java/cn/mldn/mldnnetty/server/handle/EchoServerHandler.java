package cn.mldn.mldnnetty.server.handle;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;

public class EchoServerHandler extends ChannelHandlerAdapter {
	/**
	 * 当客户端连接到服务器端之后，需要有一个连接激活的方法，此方法可以直接向客户端发送消息
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// 1、对于发送的消息可以发送中文或者是一些标记（ok标记）
		byte data [] = "【服务器端】连接通道已经建立成功，可以开始进行服务器通信处理".getBytes() ;
		// 2、Nio的处理本质是需要进行缓冲区的处理
		ByteBuf message = Unpooled.buffer(data.length) ; // 创建一个缓冲区
		// 3、将数据写入到缓冲区之中
		message.writeBytes(data) ; // 写入字节数据
		// 4、进行信息的发送，发送完成后刷新缓冲区
		ctx.writeAndFlush(message) ; // 消息发送
	}
	/**
	 * 当接收到消息之后会自动调用此方法对消息内容进行处理；
	 * @param msg 接收到的消息，这个消息可能是各种类型的数据，本程序中使用的是ByteBuf
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf in = (ByteBuf) msg ; // 1、接收消息内容
		String inputStr = in.toString(CharsetUtil.UTF_8) ; // 2、得到用户发送的数据
		String echoContent = "【ECHO】" + inputStr ; // 3、回应的消息内容
		if ("exit".equalsIgnoreCase(inputStr)) {	// 表示发送结束
			echoContent = "quit" ; // 结束的字符串信息 
		}
		ByteBuf echoBuf = Unpooled.buffer(echoContent.length()) ;
		echoBuf.writeBytes(echoContent.getBytes()) ;
		ctx.writeAndFlush(echoBuf) ;
	}
}
