package cn.mldn.mldnnetty.client.handler;

import cn.mldn.util.InputUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public class WebSocketClientHandler extends ChannelHandlerAdapter {
	private WebSocketClientHandshaker handshaker ;
	private ChannelPromise handshakerFuture ; 
	private boolean closeFlag = false ; // 关闭的标志
	public WebSocketClientHandler(WebSocketClientHandshaker handshaker) {
		this.handshaker = handshaker ; // 保存外部的握手对象
	}
	public ChannelPromise handshakerFuture() {
		return this.handshakerFuture ;
	}
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {	// 消息读取完毕
		if (this.closeFlag == false) {	// 此时通道未关闭，表示可以进行消息的发送
			String inputStr = InputUtil.getString("请输入要发送的数据：") ;
			if ("exit".equalsIgnoreCase(inputStr)) {	// 不发送了，要关闭
				ctx.writeAndFlush(new CloseWebSocketFrame()) ;// 发送给服务器端关闭指令
			} else if ("ping".equalsIgnoreCase(inputStr)) {
				ctx.writeAndFlush(new PingWebSocketFrame()) ;	// 连接测试
			} else {
				TextWebSocketFrame text = new TextWebSocketFrame(inputStr) ;
				ctx.writeAndFlush(text) ;// 消息发送
			}
				
		}
	}
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Channel ch = ctx.channel() ; // 获取当前的通道
		if (!this.handshaker.isHandshakeComplete()) {	// 是否已经握手成功了
			try {
				this.handshaker.finishHandshake(ch, (FullHttpResponse) msg);	// 完成握手
				System.out.println("****** 【WebSocket客户端】成功连接到服务器端...");
				this.handshakerFuture.setSuccess() ; // 已经创建成功了
			} catch (Exception e) {
				System.err.println("****** 【WebSocket客户端】无法连接到服务器端...");
				this.handshakerFuture.setFailure(e) ;
			}
			return ; // 后续不再执行，再次执行的时候表示已经握手成功
		}
		if (!(msg instanceof FullHttpResponse)) {	// 表示要进行内容的处理
			WebSocketFrame frame = (WebSocketFrame) msg ; // 获取发送的内容
			if (frame instanceof TextWebSocketFrame) {	// 返回的是文本数据信息
				TextWebSocketFrame text = (TextWebSocketFrame) frame ;
				System.out.println("### " + text.text());
			} else if (frame instanceof PongWebSocketFrame) {
				System.out.println("### pong ...");
			} else if (frame instanceof CloseWebSocketFrame) {
				System.out.println("### close ...");
				this.closeFlag = false ; // 通道关闭
				ch.close() ; // 关闭通道
			}
		}
	}
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {	// 通道激活
		this.handshaker.handshake(ctx.channel()) ; // 进行握手操作
	}
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {	// 追加了处理链之后
		this.handshakerFuture = ctx.newPromise() ; // 创建一个异步通道
	}


}
