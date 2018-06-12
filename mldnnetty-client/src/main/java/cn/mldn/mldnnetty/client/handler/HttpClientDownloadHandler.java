package cn.mldn.mldnnetty.client.handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.LastHttpContent;

public class HttpClientDownloadHandler extends ChannelHandlerAdapter {
	private int successCode = 0 ; // 成功编码
	private boolean readingFlag = false ; // 是否需要继续读取
	private OutputStream output = null ; // 数据输出
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// 由于在整个图片传输的过程之中图片内容较大，所以对于回应的处理一定会进行多次的写入操作
		if (msg instanceof HttpResponse) {	// 接收回应
			HttpResponse response = (HttpResponse) msg ; // 获取回应对象
			this.successCode = response.status().code() ;
			if (this.successCode == 200) {	// 请求正常，可以开始进行回应处理
				this.readingFlag = true ; // 可以进行继续读取
				this.output = new FileOutputStream("D:" + File.separator + "download" + File.separator + "tengteng.jpg") ;
			}
		}
		if (msg instanceof HttpContent) {	// 接收内容，内容需要多次写入
			HttpContent content = (HttpContent) msg ; // 获取HttpContent对象，所有内容都在此对象之中
			if (content instanceof LastHttpContent) {	// 已经结束了
				this.readingFlag = false ; // 停止数据的读取
			}
			// 根据每一次回应的数据大小创建一个缓冲区（一般都不会很大）
			ByteBuf buf = content.content() ;
			byte data [] = new byte [buf.readableBytes()] ;
			if (this.successCode == 200) {	// 开始开始进行写入
				while(buf.isReadable()) {	// 如果可以读取到数据
					buf.readBytes(data) ; // 从缓冲区中读取数据
					this.output.write(data); // 通过输出流输出
				}
				if (this.output != null) {
					this.output.flush();  // 清空缓冲区
				}
			}
		}
		if (this.readingFlag == false) {	// 不能够读取了
			if (this.output != null) {
				this.output.flush();
				this.output.close() ;
				this.output = null ; 
			}
			ctx.channel().close() ; // 关闭访问通道
		}
	} 

}
