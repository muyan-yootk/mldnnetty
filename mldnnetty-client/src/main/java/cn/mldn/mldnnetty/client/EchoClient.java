package cn.mldn.mldnnetty.client;

import cn.mldn.commons.ServerInfo;
import cn.mldn.mldnnetty.client.handler.EchoClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class EchoClient {
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
					ch.pipeline().addLast(new LineBasedFrameDecoder(1024)) ;
					ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8)) ;
					ch.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8)) ;
					ch.pipeline().addLast(new EchoClientHandler()) ; // 自定义程序处理逻辑
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
