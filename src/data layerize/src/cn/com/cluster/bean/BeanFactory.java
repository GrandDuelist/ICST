package cn.com.cluster.bean;

import cn.com.main.TestCase;

public class BeanFactory {
	public static TCCluster getTCCluster(){
		return new TCCluster();
	}
	
	public static VersionCluster getVersionCluster(){
		return new VersionCluster();
	}
	
	public static TestCase getTestCase(){
		return new TestCase();
	}
	public static TestCaseBean getTestCaseBean(){
		return new TestCaseBean();
	}
	public static VersionBean  getVersionBean(){
		return new VersionBean();
	}

}
