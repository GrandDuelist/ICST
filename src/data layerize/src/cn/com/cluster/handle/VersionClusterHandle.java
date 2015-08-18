package cn.com.cluster.handle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.com.cluster.bean.BeanFactory;
import cn.com.cluster.bean.TCCluster;
import cn.com.cluster.bean.TestCaseBean;
import cn.com.cluster.bean.VersionBean;
import cn.com.cluster.bean.VersionCluster;
import cn.com.main.FileProcess;
import cn.com.main.FileProcess.StringArray;

public class VersionClusterHandle {
	
	
	/**
	 *@function:
	 * 		get index of version name in the versions array
	 *@return:
	 *		index of the version
	 *@Input: 
	 *		String versionName  : the name of version that we want to find index in the array
	 *		String versions     : an array contains all version
	 * 
	 */
	public int getIndexOfByVersionName(String versionName,String[] versions){
		for(int i=0;i<versions.length;i++){
			if(versions[i].equals(versionName)){
				return i;
			}
		}
		return -1;
	}
	
	

	/**
	 *@function:
	 * 		add the version cluster  failed and pass old cluster 
	 *@return:
	 *		version cluster with fail, pass cluster finished
	 *@Input: 
	 *		VersionBean :  we can get version name from
	 *		List<terraceFeatures> : all terrace features information of all version
	 */
	public VersionCluster getVersionClusterBasedOnListOfStringArray(VersionBean version, List<StringArray>[] terraceFeatures){
		int index = this.getIndexOfByVersionName(version.getVersionNameString(), FileProcess.versions_orig);
		List<StringArray> terraceFeature =  terraceFeatures[index];
		List<TCCluster> tcclusters = HandleFacotry.getTCClusterHandle().getFailTCClusterForOneVersion(terraceFeature);
		List<TCCluster> passes =  HandleFacotry.getTCClusterHandle().getPassTCClusterForOneVersion(terraceFeature, tcclusters);
		
		VersionCluster result = BeanFactory.getVersionCluster();
		result.setFailedOldTestClusters(tcclusters);
		result.setPassOldTestClusters(passes);
		result.setVersion(version);
		
		return result;
	}
	
	
	
	/**
	 *@function:
	 * 		add the version cluster the new type cluster based on existed failed and pass old cluster 
	 *@return:
	 *		version cluster with fail, pass and new type cluster finished
	 *@Input: 
	 *		VersionCluster version : the version cluster with the failed and pass old one finished
	 *		List<TestCaseBean> all test case bean in one version
	 * 
	 */
	public VersionCluster getVersionClusterBasedOnOldTestCase(VersionCluster version,List<TestCaseBean> allTestCase){
		List<TestCaseBean> result = HandleFacotry.getTCClusterHandle().removeTCClusterListFromTestCaseList(allTestCase,version.getFailedOldTestClusters());
		result = HandleFacotry.getTCClusterHandle().removeTCClusterListFromTestCaseList(result, version.getPassOldTestClusters());
		version.getNewTypeCluster().setTestcases(result);
		version.getNewTypeCluster().setOrderInClusters(version.getFailedOldTestClusters().size()+version.getPassOldTestClusters().size()+1);
		return version;
	}
	
	
	public void printVersionCluster(VersionCluster versionCluster)
	{
		TCClusterHandle tc = HandleFacotry.getTCClusterHandle();
		
		System.out.println("--------FAIL tc cluster--------------");
		tc.printListOfClusters(versionCluster.getFailedOldTestClusters());
		System.out.println("--------PASS tc cluster--------------");
		tc.printListOfClusters(versionCluster.getPassOldTestClusters());
		System.out.println("--------NT tc cluster--------------");
		tc.printTCBeanCluster(versionCluster.getNewTypeCluster());
	}
	

	/**
	 * @function:
	 *  	copy the Version
	 * @return:
	 * 		void
	 * @throws IOException 
	 * @input:
	 * 		String inputDir : the directory the original file belongs to
	 * 		String outputDir: the target directory it copy to
	 */
	public void copyTestCaseFileByVersionCluster(String inputDir, String outputDir, VersionCluster version) throws IOException{
		inputDir = inputDir+version.getVersion().getVersionNameString()+"/";
		outputDir = outputDir+version.getVersion().getVersionNameString()+"/";
		File file = new File(outputDir);
		if(!file.exists()||file.isDirectory()){
			file.mkdir();
		}
		
		HandleFacotry.getTCClusterHandle().copyTestCaseFileByTCClusterList(inputDir, outputDir, version.getFailedOldTestClusters());
		HandleFacotry.getTCClusterHandle().copyTestCaseFileByTCClusterList(inputDir, outputDir, version.getPassOldTestClusters());
		HandleFacotry.getTCClusterHandle().copyTestCaseBeanFileByTCCluster(inputDir, outputDir, version.getNewTypeCluster());
		
	}
	

}
