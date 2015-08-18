package cn.com.cluster.bean;

import java.util.ArrayList;
import java.util.List;


public class VersionCluster {
	
	private List<TCCluster> failedOldTestClusters = new ArrayList<TCCluster>();
	private List<TCCluster> passOldTestClusters = new ArrayList<TCCluster>();
	private TCBeanCluster  newTypeCluster = new TCBeanCluster();
	private VersionBean version;
	
	
	
	public TCBeanCluster getNewTypeCluster() {
		return newTypeCluster;
	}
	public void setNewTypeCluster(TCBeanCluster newTypeCluster) {
		this.newTypeCluster = newTypeCluster;
	}
	public List<TCCluster> getFailedOldTestClusters() {
		return failedOldTestClusters;
	}
	public void setFailedOldTestClusters(List<TCCluster> failedOldTestClusters) {
		this.failedOldTestClusters = failedOldTestClusters;
	}
	public List<TCCluster> getPassOldTestClusters() {
		return passOldTestClusters;
	}
	public void setPassOldTestClusters(List<TCCluster> passOldTestClusters) {
		this.passOldTestClusters = passOldTestClusters;
	}
	public VersionBean getVersion() {
		return version;
	}
	public void setVersion(VersionBean version) {
		this.version = version;
	}
	
	
	
	
	
}
