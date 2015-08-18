package cn.com.cluster.bean;

import java.util.ArrayList;
import java.util.List;

import cn.com.main.TestCase;

public class TCCluster {
	
	private List<TestCase> testcases = new ArrayList<TestCase>();
	private int orderInClusters;
	private String OldVersion;
	private String CurrentVersion;
	private int currentIndex;
	private int oldIndex; 
	private boolean isOldTestFailed;
	
	
	
	public int getCurrentIndex() {
		return currentIndex;
	}
	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}
	public int getOldIndex() {
		return oldIndex;
	}
	public void setOldIndex(int oldIndex) {
		this.oldIndex = oldIndex;
	}
	public boolean isOldTestFailed() {
		return isOldTestFailed;
	}
	public void setOldTestFailed(boolean isOldTestFailed) {
		this.isOldTestFailed = isOldTestFailed;
	}
	public List<TestCase> getTestcases() {
		return testcases;
	}
	public void setTestcases(List<TestCase> testcases) {
		this.testcases = testcases;
	}
	public int getOrderInClusters() {
		return orderInClusters;
	}
	public void setOrderInClusters(int orderInClusters) {
		this.orderInClusters = orderInClusters;
	}
	public String getOldVersion() {
		return OldVersion;
	}
	public void setOldVersion(String oldVersion) {
		OldVersion = oldVersion;
	}
	public String getCurrentVersion() {
		return CurrentVersion;
	}
	public void setCurrentVersion(String currentVersion) {
		CurrentVersion = currentVersion;
	}
	
	

}
