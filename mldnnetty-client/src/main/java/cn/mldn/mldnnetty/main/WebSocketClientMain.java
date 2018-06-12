package cn.mldn.mldnnetty.main;

import cn.mldn.mldnnetty.client.WebSocketClient;

public class WebSocketClientMain {
	public static void main(String[] args) throws Exception {
		new WebSocketClient().run() ;	// 客户端运行
	}
}
