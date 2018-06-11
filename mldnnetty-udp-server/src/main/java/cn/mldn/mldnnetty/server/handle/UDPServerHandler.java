package cn.mldn.mldnnetty.server.handle;

import java.net.InetSocketAddress;

import cn.mldn.commons.ServerInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

public class UDPServerHandler extends ChannelHandlerAdapter {
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// 本次的设计是在UDP服务端启动之后就进行消息内容的发送处理，既然要在启动时发送则应该在通道连接时处理
		String str = "【MLDN】www.mldn.cn" ;
		ByteBuf data = Unpooled.copiedBuffer(str, CharsetUtil.UTF_8) ;
		// 255.255.255.255表示广播地址（向全网段的主机进行xia）
		InetSocketAddress address = new InetSocketAddress("255.255.255.255",ServerInfo.PORT) ;
		// 创建一个UDP程序对应的数据包信息
		DatagramPacket packet = new DatagramPacket(data, address) ;
		ctx.writeAndFlush(packet).sync() ; // 全部进行发送
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.err.println("〖服务器端-生命周期〗服务器出现异常。");
		// ctx.close() ;
	}
}
