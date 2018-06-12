package cn.mldn.commons.http;

public interface HttpSession {
	public static final String SESSIONID = "MLDNSESSIONID" ;
	public Object getAttribute(String name) ;
	public void setAttribute(String name,Object value) ;
	public void removeAttribute(String name) ;
	public String getId() ;
	public void invalidate() ;
}
