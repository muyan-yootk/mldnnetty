package cn.mldn.mldnnetty.server.http;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.mldn.commons.http.HttpSession;

public class HttpSessionManager {	// HttpSession管理
	// 用户所有的session都在此集合之中进行保存，使用并发访问处理的Map集合。
	private static final Map<String, HttpSession> SESSION_MAP = new ConcurrentHashMap<String, HttpSession>();
	/**
	 * 一个新的用户，需要通过其创建一个SessionID，创建后的SessionID在集合中柏岙村
	 * @return 直接返回新的sessionID内容
	 */
	public static String createSession() {
		HttpSession session = new DefaultHttpSession() ; 	// 生成了SessionID
		String sessionId = session.getId() ; // 获取SessionID数据
		SESSION_MAP.put(sessionId, session) ; // 创建SessionID
		return sessionId ;	// 需要将数据保存在Cookie之中
	} 
	/**
	 * 获取指定的Session对象信息
	 * @param sessionId 要查询的sessionId内容
	 * @return 存在的HttpSession对象
	 */
	public static HttpSession getSession(String sessionId) {
		return SESSION_MAP.get(sessionId) ;
	}  
	/**
	 * 当用户发送请求过来之后需要判断该用户是否已经连接过了
	 * @param sessionId 用户通过Cookie传来的sessionId
	 * @return 如果用户存在则返回true，否则返回false
	 */
	public static boolean isExists(String sessionId) {
		if (SESSION_MAP.containsKey(sessionId)) {	// 如果内容存在
			HttpSession session = SESSION_MAP.get(sessionId) ;
			if (session.getId() == null) {
				SESSION_MAP.remove(sessionId) ; // 删除session数据
				return false ;
			}
			return true ;
		}
		return false ;
	} 
	/**
	 * 删除指定的session数据
	 * @param sessionId 要删除的sessionId
	 */
	public static void invalidate(String sessionId) {
		SESSION_MAP.remove(sessionId) ; // 删除session数据
	}
}
