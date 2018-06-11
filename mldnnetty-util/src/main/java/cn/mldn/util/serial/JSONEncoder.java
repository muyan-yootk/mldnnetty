package cn.mldn.util.serial;

import com.alibaba.fastjson.JSONObject;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class JSONEncoder extends MessageToByteEncoder<Object> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
		byte data [] = JSONObject.toJSONString(msg).getBytes() ; // 按照字节数组传递JSON内容
		out.writeBytes(data) ;
	}

}
