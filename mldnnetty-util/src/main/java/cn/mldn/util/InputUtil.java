package cn.mldn.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class InputUtil {
	private static final BufferedReader KEYBOARD_INPUT  = new BufferedReader(new InputStreamReader(System.in)) ;
	private InputUtil() {}
	/**
	 * 获取一个键盘输入的字符串数据，如果数据为空则重新输入
	 * @param prompt 提示文字
	 * @return 输入内容
	 */
	public static String getString(String prompt) {
		String content = null ;
		boolean flag = true ;
		while (flag) {
			System.out.print(prompt);
			try {
				content = KEYBOARD_INPUT.readLine() ;
				if (content == null || "".equals(content)) {
					System.out.println("输入的内容为空，请重新输入！");
				} else {
					flag = false ; // 结束循环
				}
			} catch (Exception e) {
				System.out.println("输入数据错误，请重新输入！");
			}
		}
		return content ;
	}
}
 