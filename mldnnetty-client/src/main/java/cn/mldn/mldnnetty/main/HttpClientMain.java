package cn.mldn.mldnnetty.main;

import cn.mldn.mldnnetty.client.HttpClient;

public class HttpClientMain {
	public static void main(String[] args) throws Exception {
		new HttpClient().runPost() ;	// 客户端运行
	}
}
