package cn.mldn.util.serial;

import org.msgpack.MessagePack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
// MessgePack编码器
public class MessagePackEncoder extends MessageToByteEncoder<Object> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
		MessagePack pack = new MessagePack() ;
		byte [] raw = pack.write(msg) ; // 对象序列化
		out.writeBytes(raw) ;
	}

}
