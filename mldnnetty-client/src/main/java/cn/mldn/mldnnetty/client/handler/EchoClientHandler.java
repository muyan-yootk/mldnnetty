package cn.mldn.mldnnetty.client.handler;

import java.util.concurrent.TimeUnit;

import cn.mldn.util.InputUtil;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class EchoClientHandler extends ChannelHandlerAdapter {
	private static final int REPEAT = 500 ; // 消息重复发送500次
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {// 客户端的连接激活
		String inputStr = InputUtil.getString("请输入要发送的信息：") ;
		for (int x = 0 ; x < REPEAT ; x ++) {
			ctx.writeAndFlush(inputStr + " - " + x + System.getProperty("line.separator")) ; // 发送数据
		}
	}	
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String content = (String) msg ; // 接收数据
		System.out.println("｛客户端｝" + content); // 服务器端的回应信息
		TimeUnit.MILLISECONDS.sleep(200);
	}

}
