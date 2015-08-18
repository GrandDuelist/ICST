package cn.com.main;

import java.util.ArrayList;
import java.util.List;

public class Summary {
	
	private List<TestCase> testCases = new ArrayList();
	private String summaryString = null;
	
	
	public List<TestCase> getTestCases() {
		return testCases;
	}
	public void setTestCases(List<TestCase> testCases) {
		this.testCases = testCases;
	}
	public String getSummaryString() {
		return summaryString;
	}
	public void setSummaryString(String summaryString) {
		this.summaryString = summaryString;
	}
	
	
	

}
