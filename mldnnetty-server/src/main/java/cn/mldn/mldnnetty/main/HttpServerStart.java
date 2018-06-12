package cn.mldn.mldnnetty.main;

import cn.mldn.mldnnetty.server.HttpServer;

public class HttpServerStart {
	public static void main(String[] args) throws Exception {
		System.out.println("************* 服务器正常启动 *************");
		new HttpServer().run(); 
	}
}
