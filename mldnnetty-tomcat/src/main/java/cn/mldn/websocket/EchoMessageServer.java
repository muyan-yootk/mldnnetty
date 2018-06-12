package cn.mldn.websocket;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/message") // 此为WebSocket程序的访问路径
public class EchoMessageServer {
	@OnOpen
	public void openMethod() {	// 方法名称自己定义
		System.out.println("********* 【EchoMessageServer】 打开连接 ************");
	}
	@OnMessage
	public void messageHandle(String message, Session session) { // 消息处理
		System.out.println("*********** 【EchoMessageServer】接收到发送的消息：" + message);
		try {
			session.getBasicRemote().sendText("ECHO : " + message);
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	@OnClose 
	public void closeMethod() {
		System.out.println("********* 【EchoMessageServer】 关闭连接 ************");
	}
}
