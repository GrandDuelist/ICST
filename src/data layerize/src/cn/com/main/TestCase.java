package cn.com.main;

public class TestCase {
	private String testCaseString=null;
	private String version = null;
	private boolean firstTime = false;
	private String status = null;
	private boolean hasPreviousFailed = false;
	private String fileName;
	public Summary mySummary=null;
	
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Summary getMySummary() {
		return mySummary;
	}
	public void setMySummary(Summary mySummary) {
		this.mySummary = mySummary;
	}
	public boolean isHasPreviousFailed() {
		return hasPreviousFailed;
	}
	public void setHasPreviousFailed(boolean hasPreviousFailed) {
		this.hasPreviousFailed = hasPreviousFailed;
	}
	public boolean isFirstTime() {
		return firstTime;
	}
	public void setFirstTime(boolean firstTime) {
		this.firstTime = firstTime;
	}
	public String getTestCaseString() {
		return testCaseString;
	}
	public void setTestCaseString(String testCaseString) {
		this.testCaseString = testCaseString;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	

}
