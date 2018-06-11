package cn.mldn.mldnnetty.client;

import cn.mldn.commons.ServerInfo;
import cn.mldn.mldnnetty.client.handler.ObjectClientHandler;
import cn.mldn.util.serial.JSONDecoder;
import cn.mldn.util.serial.JSONEncoder;
import cn.mldn.util.serial.MarshallingCodeFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class ObjectClient {
	public void run() throws Exception  {	// 启动客户端程序
		// 1、创建一个进行数据交互的处理线程池
		EventLoopGroup group = new NioEventLoopGroup() ;
		try {
			Bootstrap clientBootstrap = new Bootstrap() ; // 创建客户端处理
			clientBootstrap.group(group) ; // 设置连接池
			clientBootstrap.channel(NioSocketChannel.class) ; // 设置通道类型
	clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			// 设置每行数据读取的最大行数
			ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(65536, 0, 3, 0, 3));
			ch.pipeline().addLast(new JSONDecoder()) ;
			ch.pipeline().addLast(new LengthFieldPrepender(3));	// 与类中的属性个数相同
			ch.pipeline().addLast(new JSONEncoder()) ; 
			ch.pipeline().addLast(new ObjectClientHandler()) ; // 自定义程序处理逻辑
		} 
	}) ; 
			// 连接远程服务器端
			ChannelFuture future = clientBootstrap.connect(ServerInfo.HOSTNAME, ServerInfo.PORT).sync() ;
			future.channel().closeFuture().sync() ; // 等待关闭，Handler里面关闭处理 
		} catch (Exception e) {
			group.shutdownGracefully() ; // 关闭线程池
		}
	}
}
