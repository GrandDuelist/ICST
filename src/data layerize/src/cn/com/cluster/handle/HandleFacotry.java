package cn.com.cluster.handle;

import cn.com.secondary.FileCopy;

public class HandleFacotry {
	
	public static TestCaseHandle getTestCaseHandle(){
		return new TestCaseHandle();
	}
	public static TCClusterHandle getTCClusterHandle(){
		return new TCClusterHandle();
	}
	public static VersionClusterHandle getVersionClusterHandle(){
		return new VersionClusterHandle();
	}	
	public static VersionHandle getVersionHandle(){
		return new VersionHandle();
	}
	
	public static FileCopy  getFileCopy(){
		return new FileCopy();
	}

}
