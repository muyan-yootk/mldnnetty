package cn.mldn.mldnnetty.main;

import cn.mldn.mldnnetty.client.EchoClient;

public class EchoClientMain {
	public static void main(String[] args) throws Exception {
		new EchoClient().run() ;	// 客户端运行
	}
}
