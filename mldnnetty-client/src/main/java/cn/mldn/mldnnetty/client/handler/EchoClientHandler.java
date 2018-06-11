package cn.mldn.mldnnetty.client.handler;

import cn.mldn.util.InputUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;

public class EchoClientHandler extends ChannelHandlerAdapter {
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {// 客户端的连接激活
		String data = "userid:mldnjava" ; // 当前建立连接时用户的身份信息
		ByteBuf buf = Unpooled.buffer(data.length()) ;
		buf.writeBytes(data.getBytes()) ;
		ctx.writeAndFlush(buf) ;
	}	
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = (ByteBuf) msg ; // 接收远程发回的数据信息
		String content = buf.toString(CharsetUtil.UTF_8) ; // 接收数据
		if ("quit".equalsIgnoreCase(content)) {	// 服务器端要结束处理
			System.out.println("##### 本次操作结束，已退出 #####");
			ctx.close() ; // 关闭连接通道，和服务器端端口
		} else {	// 需要进行进一步的处理操作
			System.out.println("｛客户端｝" + content); // 服务器端的回应信息
			String inputStr = InputUtil.getString("请输入要发送的信息：") ;
			ByteBuf newBuf = Unpooled.buffer(inputStr.length()) ;
			newBuf.writeBytes(inputStr.getBytes()) ; // 写入数据
		ChannelFuture future = ctx.writeAndFlush(newBuf) ; // 发送数据
		future.addListener(new ChannelFutureListener() {	// 进行监听的回调处理
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if (future.isSuccess()) {	// 操作成功
					System.out.println("********** 客户端回信息发送成功。");
				}
			}}) ;

		}
	}
}
