package cn.mldn.util.serial;

import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import io.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

public class MarshallingCodeFactory {
	/**
	 * 编写了一个专门的编码器
	 * @return 编码器
	 */
	public static MarshallingEncoder builderEncorder() {
		MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial") ; // 获取当前要使用到的序列化管理类型
		MarshallingConfiguration config = new MarshallingConfiguration() ;
		// config.setVersion(6); // 序列化的管理版本
		MarshallerProvider provider = new DefaultMarshallerProvider(marshallerFactory, config) ;
		MarshallingEncoder encoder = new MarshallingEncoder(provider) ;
		return encoder ;
	}
	/**
	 * 构造一个解码器
	 * @return 解码器的对象
	 */
	public static MarshallingDecoder builderDecorder() {
		MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial") ; // 获取当前要使用到的序列化管理类型
		MarshallingConfiguration config = new MarshallingConfiguration() ;
		// config.setVersion(6); // 序列化的管理版本
		UnmarshallerProvider provider = new DefaultUnmarshallerProvider(marshallerFactory, config) ;
		int maxSize = 1024 << 2 ; // 定义单个对象最大允许传输的尺寸
		MarshallingDecoder decoder = new MarshallingDecoder(provider,maxSize) ;
		return decoder ;
	}
}
