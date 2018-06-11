package cn.mldn.mldnnetty.test;

import java.util.HashMap;
import java.util.Map;

import org.msgpack.MessagePack;
import org.msgpack.template.Templates;

public class TestMessagePackMap {
	public static void main(String[] args) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", "mldn") ; 
		map.put("url", "www.mldn.cn") ;
		MessagePack pack = new MessagePack() ;
		byte[] data = pack.write(map) ; // 写入数据
		{	// 通过二进制数据读取出需要的内容
			Map<String,String> result = pack.read(data,Templates.tMap(Templates.TString,pack.lookup(String.class))) ;
			System.out.println(result);
		}
	}
}
