package cn.mldn.mldnnetty.main;

import cn.mldn.mldnnetty.client.HttpClient;

public class ObjectClientMain {
	public static void main(String[] args) throws Exception {
		new HttpClient().run() ;	// 客户端运行
	}
}
