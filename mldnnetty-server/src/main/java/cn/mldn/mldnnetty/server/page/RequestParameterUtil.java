package cn.mldn.mldnnetty.server.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;

public class RequestParameterUtil {
	private HttpRequest request ; // 所有的请求内容
	private HttpContent content ; // 请求内容
	// 不管使用何种模式进行了参数的处理，全部的操作都是通过Map集合保存
	private Map<String,List<String>> params = new HashMap<String,List<String>>() ;
	public RequestParameterUtil(HttpRequest request,HttpContent content) {
		this.request = request ;
		this.content = content ;
		this.parse(); // 进行参数的解析，解析后的内容保存在params集合之中
	}
	public String getParameter(String paramName) {
		try {
			return this.params.get(paramName).get(0) ;
		} catch (Exception e) {
			return null ;
		}
	}
	public List<String> getParameterValues(String paramName) {
		return this.params.get(paramName) ;
	}
	private void parse() {	// 进行参数的解析处理
		if (HttpMethod.GET.equals(this.request.method())) {	// GET请求
			QueryStringDecoder decoder = new QueryStringDecoder(this.request.uri()) ; // 处理GET请求参数
			this.params.putAll(decoder.parameters()); // 保存所有的GET请求参数
		} else if (HttpMethod.POST.equals(this.request.method())) {	// POST请求
			if (this.content != null) {
				HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(this.request) ; // POST解码
				decoder.offer(this.content) ;	// 修改解码的开始位置
				List<InterfaceHttpData> datas = decoder.getBodyHttpDatas() ; // 获取所有的HTTP请求参数
				datas.forEach((param) -> {	// 循环处理所有的参数
					Attribute attribute = (Attribute) param ;
					try {
						List<String> paramValue = null ;
						if (this.params.containsKey(attribute.getName())) {	// 该内容已经保存过了
							paramValue = this.params.get(attribute.getName()) ;
						} else {
							paramValue = new ArrayList<String>() ; // 
						}
						paramValue.add(attribute.getValue()) ; // 获取一个数据进行保存
						this.params.put(attribute.getName(), paramValue) ;
					} catch (Exception e) {}
				});
			}
		}
	}
}
