package cn.mldn.mldnnetty.main;

import cn.mldn.mldnnetty.client.UDPClient;

public class UDPClientMain {
	public static void main(String[] args) throws Exception {
		new UDPClient().run() ;	// 客户端运行
	}
}
