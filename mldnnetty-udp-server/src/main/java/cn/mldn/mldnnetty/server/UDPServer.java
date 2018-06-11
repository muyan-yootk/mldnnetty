package cn.mldn.mldnnetty.server;

import cn.mldn.mldnnetty.server.handle.UDPServerHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class UDPServer {
	public void run() throws Exception {	// 程序的运行方法，异常全部抛出
		EventLoopGroup group = new NioEventLoopGroup() ; 
		try {
			Bootstrap serverBootstrap = new Bootstrap() ;
			serverBootstrap.group(group) ;
			serverBootstrap.channel(NioDatagramChannel.class) ;
			serverBootstrap.handler(new UDPServerHandler()) ;
			serverBootstrap.option(ChannelOption.SO_BROADCAST, true) ; // 允许进行广播
			ChannelFuture future = serverBootstrap.bind(0).sync() ;	// 异步线程处理 
			future.channel().closeFuture().sync() ; // 处理完成之后进行关闭
		} catch (Exception e) {
			group.shutdownGracefully() ;	// 关闭主线程池
		}
	}
}
