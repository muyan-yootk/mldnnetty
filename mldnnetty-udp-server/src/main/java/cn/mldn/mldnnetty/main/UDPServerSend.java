package cn.mldn.mldnnetty.main;

import cn.mldn.mldnnetty.server.UDPServer;

public class UDPServerSend {
	public static void main(String[] args) throws Exception {
		System.out.println("************* 服务器正常启动 *************");
		new UDPServer().run(); 
	}
}
