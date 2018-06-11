package cn.mldn.mldnnetty.main;

import cn.mldn.mldnnetty.server.EchoServer;

public class EchoServerStart {
	public static void main(String[] args) throws Exception {
		System.out.println("************* 服务器正常启动 *************");
		new EchoServer().run(); 
	}
}
