package cn.com.cluster.bean;

import java.util.ArrayList;
import java.util.List;


public class TCBeanCluster {
	private List<TestCaseBean> testcases = new ArrayList<TestCaseBean>();
	private int orderInClusters;
	
	
	public List<TestCaseBean> getTestcases() {
		return testcases;
	}
	public void setTestcases(List<TestCaseBean> testcases) {
		this.testcases = testcases;
	}
	public int getOrderInClusters() {
		return orderInClusters;
	}
	public void setOrderInClusters(int orderInClusters) {
		this.orderInClusters = orderInClusters;
	}
	
	
}
