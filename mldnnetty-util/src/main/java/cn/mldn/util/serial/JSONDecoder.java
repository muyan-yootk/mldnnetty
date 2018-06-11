package cn.mldn.util.serial;

import java.util.List;

import com.alibaba.fastjson.JSON;

import cn.mldn.vo.Member;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

public class JSONDecoder extends MessageToMessageDecoder<ByteBuf> {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		int len = msg.readableBytes() ; // 获取数据长度
		byte data [] = new byte [len] ; // 开启数组准备接收数据 
		msg.getBytes(msg.readerIndex(), data, 0, len);	// 读取数据
		out.add(JSON.parseObject(new String(data)).toJavaObject(Member.class)) ;	// 进行解码处理
	}

}
