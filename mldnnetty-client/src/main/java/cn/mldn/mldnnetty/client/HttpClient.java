package cn.mldn.mldnnetty.client;

import java.net.URI;

import cn.mldn.commons.ServerInfo;
import cn.mldn.mldnnetty.client.handler.HttpClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder;

public class HttpClient {
	
	public void runPost() throws Exception  {	// 启动客户端程序
		// 1、创建一个进行数据交互的处理线程池
		EventLoopGroup group = new NioEventLoopGroup() ;
		try {
			Bootstrap clientBootstrap = new Bootstrap() ; // 创建客户端处理
			clientBootstrap.group(group) ; // 设置连接池
			clientBootstrap.channel(NioSocketChannel.class) ; // 设置通道类型
			clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new HttpResponseDecoder()) ; 
					ch.pipeline().addLast(new HttpRequestEncoder()) ;
					ch.pipeline().addLast(new HttpClientHandler()) ; // 自定义程序处理逻辑
				} 
			}) ; 
			// 连接远程服务器端
			ChannelFuture future = clientBootstrap.connect(ServerInfo.HOSTNAME, ServerInfo.PORT).sync() ;
			// 向服务器端进行请求的发送处理
			String url = "http://" + ServerInfo.HOSTNAME + ":" + ServerInfo.PORT + "/param?info=www.mldn.cn" ;	// HTTP连接地址
			URI uri = new URI(url) ; // 需要通过URI类进行请求地址的封装 
			// 创建一个HTTP请求对象，并且设置了请求方法与请求路径
			DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST,
					uri.getRawPath());	// 利用POST请求模式创建请求
			// 所有通过POST请求发送的请求参数都需要进行POST编码（是由Netty来直接提供的），设置为false表示表单没有包装
			HttpPostRequestEncoder encoder = new HttpPostRequestEncoder(request, false) ;
			encoder.addBodyAttribute("msg", "www.mldn.cn");
			encoder.addBodyAttribute("inst", "唱歌");
			encoder.addBodyAttribute("inst", "跳舞");
			HttpRequest finalizeRequest = encoder.finalizeRequest() ; // 创建POST请求
			 
			finalizeRequest.headers().set(HttpHeaderNames.HOST, "127.0.0.1");
			finalizeRequest.headers().set(HttpHeaderNames.CONNECTION,HttpHeaderValues.KEEP_ALIVE) ;
			finalizeRequest.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8") ;
			finalizeRequest.headers().set(HttpHeaderNames.CONTENT_LENGTH,String.valueOf(encoder.length())) ;
			finalizeRequest.headers().set(HttpHeaderNames.COOKIE,"nothing") ;
			future.channel().writeAndFlush(finalizeRequest) ; // 发送请求 
			future.channel().closeFuture().sync() ; // 等待关闭，Handler里面关闭处理 
		} catch (Exception e) {
			group.shutdownGracefully() ; // 关闭线程池
		}
	}
	
	public void runGet() throws Exception  {	// 启动客户端程序
		// 1、创建一个进行数据交互的处理线程池
		EventLoopGroup group = new NioEventLoopGroup() ;
		try {
			Bootstrap clientBootstrap = new Bootstrap() ; // 创建客户端处理
			clientBootstrap.group(group) ; // 设置连接池
			clientBootstrap.channel(NioSocketChannel.class) ; // 设置通道类型
			clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new HttpResponseDecoder()) ; 
					ch.pipeline().addLast(new HttpRequestEncoder()) ;
					ch.pipeline().addLast(new HttpClientHandler()) ; // 自定义程序处理逻辑
				} 
			}) ; 
			// 连接远程服务器端
			ChannelFuture future = clientBootstrap.connect(ServerInfo.HOSTNAME, ServerInfo.PORT).sync() ;
			// 向服务器端进行请求的发送处理
			String url = "http://" + ServerInfo.HOSTNAME + ":" + ServerInfo.PORT + "/param?info=www.mldn.cn" ;	// HTTP连接地址
			// 创建一个HTTP请求对象，并且设置了请求方法与请求路径
			DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, url);
			request.headers().set(HttpHeaderNames.HOST, "127.0.0.1");
			request.headers().set(HttpHeaderNames.CONNECTION,HttpHeaderValues.KEEP_ALIVE) ;
			request.headers().set(HttpHeaderNames.CONTENT_LENGTH,String.valueOf(request.content().readableBytes())) ;
			request.headers().set(HttpHeaderNames.COOKIE,"nothing") ;
			future.channel().writeAndFlush(request) ; // 发送请求
			future.channel().closeFuture().sync() ; // 等待关闭，Handler里面关闭处理 
		} catch (Exception e) {
			group.shutdownGracefully() ; // 关闭线程池
		}
	}
	public void runConnect() throws Exception  {	// 启动客户端程序
		// 1、创建一个进行数据交互的处理线程池
		EventLoopGroup group = new NioEventLoopGroup() ;
		try {
			Bootstrap clientBootstrap = new Bootstrap() ; // 创建客户端处理
			clientBootstrap.group(group) ; // 设置连接池
			clientBootstrap.channel(NioSocketChannel.class) ; // 设置通道类型
			clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new HttpResponseDecoder()) ; 
					ch.pipeline().addLast(new HttpRequestEncoder()) ;
					ch.pipeline().addLast(new HttpClientHandler()) ; // 自定义程序处理逻辑
				} 
			}) ; 
			// 连接远程服务器端
			ChannelFuture future = clientBootstrap.connect(ServerInfo.HOSTNAME, ServerInfo.PORT).sync() ;
			// 向服务器端进行请求的发送处理
			String url = "http://" + ServerInfo.HOSTNAME + ":" + ServerInfo.PORT ;	// HTTP连接地址
			// 创建一个HTTP请求对象，并且设置了请求方法与请求路径
			DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, url);
			request.headers().set(HttpHeaderNames.HOST, "127.0.0.1");
			request.headers().set(HttpHeaderNames.CONNECTION,HttpHeaderValues.KEEP_ALIVE) ;
			request.headers().set(HttpHeaderNames.CONTENT_LENGTH,String.valueOf(request.content().readableBytes())) ;
			request.headers().set(HttpHeaderNames.COOKIE,"nothing") ;
			future.channel().writeAndFlush(request) ; // 发送请求
			future.channel().closeFuture().sync() ; // 等待关闭，Handler里面关闭处理 
		} catch (Exception e) {
			group.shutdownGracefully() ; // 关闭线程池
		}
	}
}
