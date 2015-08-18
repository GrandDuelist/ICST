package cn.com.cluster.bean;

import java.util.ArrayList;
import java.util.List;

public class VersionBean {
	
	private List<TestCaseBean> testCases  = new ArrayList<TestCaseBean>();
	private String versionNameString;
	private List<TestCaseBean> failedTestCases=new ArrayList<TestCaseBean>();
	private List<TestCaseBean> passTestCases=new ArrayList<TestCaseBean>();
	private List<TestCaseBean> duplicatedTestCases=new ArrayList<TestCaseBean>();
	private List<TestCaseBean> noDuplicatedTestCases = new ArrayList<TestCaseBean>();
	private List<TestCaseBean> newTestCases=new ArrayList<TestCaseBean>();
	private List<TestCaseBean> oldTestCases=new ArrayList<TestCaseBean>();
	private VersionCluster versionCluster;
	
	
	
	public VersionCluster getVersionCluster() {
		return versionCluster;
	}
	public void setVersionCluster(VersionCluster versionCluster) {
		this.versionCluster = versionCluster;
	}
	public List<TestCaseBean> getNoDuplicatedTestCases() {
		return noDuplicatedTestCases;
	}
	public void setNoDuplicatedTestCases(List<TestCaseBean> noDuplicatedTestCases) {
		this.noDuplicatedTestCases = noDuplicatedTestCases;
	}
	public List<TestCaseBean> getNewTestCases() {
		return newTestCases;
	}
	public void setNewTestCases(List<TestCaseBean> newTestCases) {
		this.newTestCases = newTestCases;
	}
	public List<TestCaseBean> getDuplicatedTestCases() {
		return duplicatedTestCases;
	}
	public void setDuplicatedTestCases(List<TestCaseBean> duplicatedTestCases) {
		this.duplicatedTestCases = duplicatedTestCases;
	}
	public List<TestCaseBean> getOldTestCases() {
		return oldTestCases;
	}
	public void setOldTestCases(List<TestCaseBean> oldTestCases) {
		this.oldTestCases = oldTestCases;
	}
	public List<TestCaseBean> getTestCases() {
		return testCases;
	}
	public void setTestCases(List<TestCaseBean> testCases) {
		this.testCases = testCases;
	}
	public String getVersionNameString() {
		return versionNameString;
	}
	public void setVersionNameString(String versionNameString) {
		this.versionNameString = versionNameString;
	}
	public List<TestCaseBean> getFailedTestCases() {
		return failedTestCases;
	}
	public void setFailedTestCases(List<TestCaseBean> failedTestCases) {
		this.failedTestCases = failedTestCases;
	}
	public List<TestCaseBean> getPassTestCases() {
		return passTestCases;
	}
	public void setPassTestCases(List<TestCaseBean> passTestCases) {
		this.passTestCases = passTestCases;
	}
	
	

}
