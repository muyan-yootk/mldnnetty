package cn.mldn.mldnnetty.main;

import cn.mldn.mldnnetty.server.WebSocketServer;

public class WebSocketServerStart {
	public static void main(String[] args) throws Exception {
		System.out.println("************* 服务器正常启动 *************");
		new WebSocketServer().run(); 
	}
}
