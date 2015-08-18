package cn.com.cluster;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import cn.com.cluster.bean.BeanFactory;
import cn.com.cluster.bean.TestCaseBean;

public class Test {
	
	public static void main(String []args) throws IOException{
		/*TestCaseBean testcase=BeanFactory.getTestCaseBean();
		List<TestCaseBean> nihaos = new ArrayList<TestCaseBean>();
		testcase.setTestCaseString("nihao");
		nihaos.add(testcase);
		testcase.setTestCaseString("hello");
		System.out.println(nihaos.get(0).getTestCaseString());*/
	   
	     Process process = Runtime.getRuntime().exec ("ls /home/ogre/");   
	      
	     InputStreamReader ir=new InputStreamReader(process.getInputStream());  
	     LineNumberReader input = new LineNumberReader (ir);  
	      
	     String line;  
	     while ((line = input.readLine ()) != null){  
	     System.out.println(line);  
	     }
	   
	   
		
	}
	
	

}
