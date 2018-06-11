package cn.mldn.mldnnetty.client;

import cn.mldn.commons.ServerInfo;
import cn.mldn.mldnnetty.client.handler.UDPClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class UDPClient {
	public void run() throws Exception  {	// 启动客户端程序
		// 1、创建一个进行数据交互的处理线程池
		EventLoopGroup group = new NioEventLoopGroup() ;
		try {
			Bootstrap clientBootstrap = new Bootstrap() ; // 创建客户端处理
			clientBootstrap.group(group) ; // 设置连接池
			clientBootstrap.channel(NioDatagramChannel.class) ; // 设置UDP类型
			clientBootstrap.handler(new UDPClientHandler()) ;
			clientBootstrap.option(ChannelOption.SO_BROADCAST, true) ;
			clientBootstrap.option(ChannelOption.SO_RCVBUF, 2048) ; // 追加缓冲区配置 
			clientBootstrap.bind(ServerInfo.PORT).sync().channel().closeFuture().await() ;
		} catch (Exception e) {
			group.shutdownGracefully() ; // 关闭线程池
		} 
	}
}
