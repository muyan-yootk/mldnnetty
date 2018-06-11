package cn.mldn.mldnnetty.server;

import cn.mldn.commons.ServerInfo;
import cn.mldn.mldnnetty.server.handle.ObjectServerHandler;
import cn.mldn.util.serial.MessagePackDecoder;
import cn.mldn.util.serial.MessagePackEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class ObjectServer {
	public void run() throws Exception {	// 程序的运行方法，异常全部抛出
		// 1、在Netty里面服务器端的程序需要准备出两个线程池
		// 1-1、第一个线程池为接收用户请求连接的线程池；
		EventLoopGroup boosGroup = new NioEventLoopGroup() ; 
		// 1-2、第二个线程池为进行数据请求处理的线程池
		EventLoopGroup workGroup = new NioEventLoopGroup() ;
		try {
			// 2、所有的服务端的程序需要通过ServerBootstrap类进行启动
			ServerBootstrap serverBootstrap = new ServerBootstrap() ;
			// 3、为此服务器端配置有线程池对象（配置连接线程池、工作线程池）
			serverBootstrap.group(boosGroup, workGroup) ;
			// 4、指明当前服务器的运行形式，基于NIO的ServerSocket实现
			serverBootstrap.channel(NioServerSocketChannel.class) ;
	// 5、进行Netty数据处理的过滤器配置（责任链设计模式）
	serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(65536, 0, 3, 0, 3));
			ch.pipeline().addLast(new MessagePackDecoder()) ;
			ch.pipeline().addLast(new LengthFieldPrepender(3));	// 与类中的属性个数相同
			ch.pipeline().addLast(new MessagePackEncoder()) ;
			ch.pipeline().addLast(new ObjectServerHandler()) ; // 自定义程序处理逻辑
		} 
	}) ; 
			// 6、由于当前的服务器主要实现的是一个TCP的回应处理程序，那么在这样的情况下就必须进行一些TCP属性配置
			serverBootstrap.option(ChannelOption.SO_BACKLOG, 64) ; // 当处理线程全满时的最大等待队列长度
			// 7、绑定服务器端口并且进行服务的启动
			ChannelFuture future = serverBootstrap.bind(ServerInfo.PORT).sync() ;	// 异步线程处理
			future.channel().closeFuture().sync() ; // 处理完成之后进行关闭
		} catch (Exception e) {
			boosGroup.shutdownGracefully() ;	// 关闭主线程池
			boosGroup.shutdownGracefully() ;	// 关闭子线程池
		}
	}
}
