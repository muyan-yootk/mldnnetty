package cn.mldn.mldnnetty.client;

import java.net.URI;

import cn.mldn.commons.ServerInfo;
import cn.mldn.mldnnetty.client.handler.WebSocketClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;

public class WebSocketClient {
	
	public void run() throws Exception  {	// 启动客户端程序
		// 1、创建一个进行数据交互的处理线程池
		EventLoopGroup group = new NioEventLoopGroup() ;
		try { 
			Bootstrap clientBootstrap = new Bootstrap() ; // 创建客户端处理
			
			String url = "ws://localhost/message" ; // 定义WebSocket的连接地址
			URI uri = new URI(url) ; // 对访问地址进行包装
			// 进行WebSocket客户端与WebSocket服务端的握手处理
			WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(uri,
					WebSocketVersion.V13, null, true, new DefaultHttpHeaders());
			
			WebSocketClientHandler handler = new WebSocketClientHandler(handshaker) ;
			
			clientBootstrap.group(group) ; // 设置连接池
			clientBootstrap.channel(NioSocketChannel.class) ; // 设置通道类型
			clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new HttpClientCodec()) ; 
					ch.pipeline().addLast(new HttpObjectAggregator(1024 * 1024 * 100)) ; // 设置最大上传容量为10M
					ch.pipeline().addLast(handler) ; // 自定义程序处理逻辑 
				}  
			}) ; 
			// 连接远程服务器端
			Channel channel = clientBootstrap.connect(ServerInfo.HOSTNAME, ServerInfo.PORT).sync().channel() ;
			handler.handshakerFuture().sync() ;	// 进行异步等待的创建
			channel.closeFuture().sync() ; // 等待关闭，Handler里面关闭处理 
		} catch (Exception e) {
			group.shutdownGracefully() ; // 关闭线程池
		}
	} 
}
