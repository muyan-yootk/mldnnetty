package cn.mldn.mldnnetty.client.handler;

import java.util.concurrent.TimeUnit;

import cn.mldn.vo.Member;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ObjectClientHandler extends ChannelHandlerAdapter {
	private static final int REPEAT = 500 ; // 消息重复发送500次
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {// 客户端的连接激活
		
		for (int x = 0 ; x < REPEAT ; x ++) {
			Member member = new Member() ;
			member.setName("强子 - " + x);
			member.setAge(x);
			member.setSalary(x + 0.0);
			ctx.writeAndFlush(member) ; // 发送数据
		} 
	}	
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Member member = (Member) msg ; // 接收数据
		System.out.println("｛客户端｝" + member); // 服务器端的回应信息
		TimeUnit.MILLISECONDS.sleep(10); 
	} 

}
