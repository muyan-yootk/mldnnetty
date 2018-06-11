package cn.mldn.mldnnetty.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cn.mldn.vo.Member;

public class JavaAPIDemo {
	private static final File SAVE_FILE = new File("D:" + File.separator + "mldn.member") ;
	public static void main(String[] args) throws Exception {
		Member member = new Member() ;
		member.setName("强子 - ");
		member.setAge(20);
		member.setSalary(200.0);
		saveObject(member) ;
		System.out.println(loadObject());
	}
	public static void saveObject(Object obj) throws Exception {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE)) ;
		oos.writeObject(obj); // 序列化
		oos.close(); 
	}
	public static Object loadObject() throws Exception {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE)) ;
		Object obj = ois.readObject() ; // 反序列化
		ois.close();
		return obj ;
	}
}
