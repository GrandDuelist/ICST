package cn.com.cluster.bean;

public class TestCaseBean {
	private String testCaseString;
	private VersionBean version;
	private boolean isFail;
	private boolean isDuplicated;
	private boolean isFromPrevious;
	private String fileName;
	
	
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getTestCaseString() {
		return testCaseString;
	}
	public void setTestCaseString(String testCaseString) {
		this.testCaseString = testCaseString;
	}
	public VersionBean getVersion() {
		return version;
	}
	public void setVersion(VersionBean version) {
		this.version = version;
	}
	public boolean isFail() {
		return isFail;
	}
	public void setFail(boolean isFail) {
		this.isFail = isFail;
	}
	public boolean isDuplicated() {
		return isDuplicated;
	}
	public void setDuplicated(boolean isDuplicated) {
		this.isDuplicated = isDuplicated;
	}
	public boolean isFromPrevious() {
		return isFromPrevious;
	}
	public void setFromPrevious(boolean isFromPrevious) {
		this.isFromPrevious = isFromPrevious;
	}
	
	
}
