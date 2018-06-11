package cn.mldn.util.serial;

import java.util.List;

import org.msgpack.MessagePack;

import cn.mldn.vo.Member;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

public class MessagePackDecoder extends MessageToMessageDecoder<ByteBuf> {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		// 所有的数据都在msg这个ByteBuf类的对象里面
		int len = msg.readableBytes() ;	// 获取缓冲区中的可用数据长度
		byte data [] = new byte [len] ; // 开辟数组接收数据
		msg.getBytes(msg.readerIndex(), data, 0, len) ; // 读取数据到字节数组
		MessagePack pack = new MessagePack() ;
		// 在out里面注册所有要使用的读取处理操作，该操作如果直接编写，则意味着Handler类里面需要接收的MessagePack对象
//		out.add(pack.read(data)) ; // 设置一系列的数据读取处理
		out.add(pack.read(data,pack.lookup(Member.class))) ;
	}

}
