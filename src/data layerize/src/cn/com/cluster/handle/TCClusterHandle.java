package cn.com.cluster.handle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.com.cluster.bean.BeanFactory;
import cn.com.cluster.bean.TCBeanCluster;
import cn.com.cluster.bean.TCCluster;
import cn.com.cluster.bean.TestCaseBean;
import cn.com.main.FileProcess;
import cn.com.main.FileProcess.StringArray;
import cn.com.main.TestCase;

public class TCClusterHandle {
	
	/**
	 *@function:
	 * 		generate a test case cluster based on two list 
	 *@return:
	 *		TCCluster the result test case cluster
	 *@Input: 
	 *		List<TestCase> orig : A test case list, which is the base list
	 *		List<TestCase> rm   : A test case list, from which we can get test cases that we wanna remove in the orig 
	 * 
	 */
	public TCCluster getTCClusterBasedOnTwoList(List<TestCase> orig, List<TestCase> rm)
	{
		TCCluster tccluster = new TCCluster();
		List<TestCase> testcases = HandleFacotry.getTestCaseHandle().removeTestCase(orig, rm);
		tccluster.setTestcases(testcases);
		return tccluster;
	}
	
	
	
	
	/**
	 *@function:
	 * 		generate a fail test case cluster based on two StringArray  
	 *@return:
	 *		TCCluster the result test case cluster
	 *@Input: 
	 *		StringArray origSA  : A StringArray, from which the original test case list can be gotten
	 *		StringArray rmSA    : A StringArray, from which the test cases list we want to remove can be gotten 
	 * 
	 */
	public TCCluster getFailTCClusterBasedOnTwoStringArray(StringArray origAS,StringArray rmAS){
		List<TestCase> orig = origAS.getFailedOldTestList();
		List<TestCase> rm   = rmAS.getFailedOldTestList();	
		TCCluster tccluster = this.getTCClusterBasedOnTwoList(orig, rm);
		tccluster.setOldTestFailed(true);
		
		int currentIndex = HandleFacotry.getVersionClusterHandle().getIndexOfByVersionName(origAS.getVersionName(), FileProcess.versions_orig);
		tccluster.setCurrentIndex(currentIndex);
		tccluster.setCurrentVersion(origAS.getVersionName());
		
		int oldIndex = HandleFacotry.getVersionClusterHandle().getIndexOfByVersionName(origAS.getVersionFrom(), FileProcess.versions_orig);
		tccluster.setOldIndex(oldIndex);
		tccluster.setOldVersion(origAS.getVersionFrom());
		
		tccluster.setOrderInClusters(currentIndex-oldIndex);
		return tccluster;
	}
	
	
	/**
	 *@function:
	 * 		generate a test case cluster based on test case list and previous TC cluster list 
	 *@return:
	 *		TCCluster the result test case cluster
	 *@Input: 
	 *		List<TestCase> orig : A test case list, which is the base list
	 *		List<TCCluster> rm   : A test case cluster list, from which we can get test cases that we wanna remove in the orig 
	 * 
	 */
	public TCCluster getTCClusterBasedOnListAndStringArrays(List<TestCase> orig,List<TCCluster> rm)
	{
		TCCluster tccluster = BeanFactory.getTCCluster();
		tccluster.setTestcases(orig);
		//remove rm from original 
		for(int i=0; i<rm.size();i++){
			List<TestCase> rmList = rm.get(i).getTestcases();
			tccluster = this.getTCClusterBasedOnTwoList(tccluster.getTestcases(), rmList);
		}
		return tccluster;
	}
	
	
	/**
	 *@function:
	 * 		generate a pass test case cluster based on two StringArray  
	 *@return:
	 *		TCCluster the result test case cluster
	 *@Input: 
	 *		StringArray origSA  : A StringArray, from which the original test case list can be gotten
	 *		StringArray rmSA    : A StringArray, from which the test cases list we want to remove can be gotten 
	 * 
	 */
	public TCCluster getPassTCClusterBasedOnTwoStringArray(StringArray origAS,StringArray rmAS,List<TCCluster> rmList){

		List<TestCase> origOld = origAS.getOldTestList();
		List<TestCase> rmOld = rmAS.getOldTestList();
		List<TestCase> currentNew = HandleFacotry.getTestCaseHandle().removeTestCase(origOld, rmOld);
		
		TCCluster tccluster = this.getTCClusterBasedOnListAndStringArrays(currentNew,rmList);
		tccluster.setOldTestFailed(false);
		
		int currentIndex = HandleFacotry.getVersionClusterHandle().getIndexOfByVersionName(origAS.getVersionName(), FileProcess.versions_orig);
		tccluster.setCurrentIndex(currentIndex);
		tccluster.setCurrentVersion(origAS.getVersionName());
		
		int oldIndex = HandleFacotry.getVersionClusterHandle().getIndexOfByVersionName(origAS.getVersionFrom(), FileProcess.versions_orig);
		tccluster.setOldIndex(oldIndex);
		tccluster.setOldVersion(origAS.getVersionFrom());
		
		tccluster.setOrderInClusters(rmList.size()+currentIndex-oldIndex);
		return tccluster;
	}
	
	
	/**
	 *@function:
	 * 		generate a fail test case cluster list based on a list StringArray  
	 *@return:
	 *		TCCluster list , the result test case cluster
	 *@Input: 
	 *		List<StringArray> version : A list of StringArray, from which the original test case list can be gotten
	 */
	public List<TCCluster> getFailTCClusterForOneVersion(List<StringArray> version){
		List<TCCluster> failTCClusters = new ArrayList<TCCluster>();
		for(int i=1;i<version.size();i++){
			TCCluster tccluster = HandleFacotry.getTCClusterHandle().getFailTCClusterBasedOnTwoStringArray(version.get(i-1),version.get(i)); 
			failTCClusters.add(tccluster);
		}
		failTCClusters.add(this.changeStringArrayToFailedTCCluster(version.get(version.size()-1)));
		
		return failTCClusters;
	}
	
	
	/**
	 *@function:
	 * 		generate a pass test case cluster list based on a list StringArray  
	 *@return:
	 *		TCCluster list , the result test case cluster
	 *@Input: 
	 *		List<StringArray> version : A list of StringArray, from which the original test case list can be gotten
	 *		List<TCCluster> failClustersOneVersion, from which we remove the overlapped test cases 
	 */
	public List<TCCluster> getPassTCClusterForOneVersion(List<StringArray> version, List<TCCluster> failClustersOneVersion){
		List<TCCluster> passTCClusters = new ArrayList<TCCluster>();
		for(int i=1;i<version.size();i++){
			TCCluster tccluster =HandleFacotry.getTCClusterHandle().getPassTCClusterBasedOnTwoStringArray(version.get(i-1), version.get(i), failClustersOneVersion);
			passTCClusters.add(tccluster);
		}
		passTCClusters.add(this.changeStringArrayToPassTCCluster(version.get(version.size()-1), failClustersOneVersion));
		return passTCClusters;
	}
	
	/**
	 * @function:
	 *  	print the string of test case in list of one TCCluster
	 * @return:
	 * 		void
	 * @input:
	 * 		TCCluster input: the TCCluster we want to print out
	 */
	public void printTCCluster(TCCluster input){
		System.out.println("old test version\t"+ input.getOldVersion());
		System.out.println("current version\t"+ input.getCurrentVersion());
		System.out.println("old test version index\t" + input.getOldIndex());
		System.out.println("current version index\t" + input.getCurrentIndex());
		System.out.println("is old failed\t"+input.isOldTestFailed());
		System.out.println("the order in clusters\t"+input.getOrderInClusters());
		
		System.out.println("test case :");
		HandleFacotry.getTestCaseHandle().printTestCaseList(input.getTestcases());
	}
	
	
	/**
	 * @function:
	 *  	print the string of test case in list of one TCCluster
	 * @return:
	 * 		void
	 * @input:
	 * 		TCCluster input: the TCCluster we want to print out
	 */
	public void printTCBeanCluster(TCBeanCluster input){
		
		System.out.println("the order in clusters\t"+input.getOrderInClusters());
		
		System.out.println("test case :");
		HandleFacotry.getTestCaseHandle().printTestCaseBeanList(input.getTestcases());
	}
	
	
	
	/**
	 *@function:
	 * 		generate a fail test case cluster list based on a  
	 *@return:
	 *		TCCluster list , the result test case cluster
	 *@Input: 
	 *		StringArray : the StringArray you want to change to TCCluster, which is always the last one of the version
	 */
	public TCCluster changeStringArrayToFailedTCCluster(StringArray origAS){
		TCCluster tccluster = new TCCluster();
		tccluster.setTestcases(origAS.getFailedOldTestList());
		
		int currentIndex = HandleFacotry.getVersionClusterHandle().getIndexOfByVersionName(origAS.getVersionName(), FileProcess.versions_orig);
		tccluster.setCurrentIndex(currentIndex);
		tccluster.setCurrentVersion(origAS.getVersionName());
		
		int oldIndex = HandleFacotry.getVersionClusterHandle().getIndexOfByVersionName(origAS.getVersionFrom(), FileProcess.versions_orig);
		tccluster.setOldIndex(oldIndex);
		tccluster.setOldVersion(origAS.getVersionFrom());
		
		tccluster.setOrderInClusters(currentIndex-oldIndex);
		return tccluster;
	}
	
	/**
	 *@function:
	 * 		generate a pass test case cluster list based on a StringArray and list of TCCluster containing test cases need to be removed
	 *@return:
	 *		TCCluster list , the result test case cluster
	 *@Input: 
	 *		StringArray origAS : the StringArray you want to change to TCCluster, which is always the last one of the version
	 *		List<TCCluster> rmList : list of test case cluster containing the test cases need to be removed from String Array origAS
	 */
	public TCCluster changeStringArrayToPassTCCluster(StringArray origAS, List<TCCluster> rmList){
		TCCluster tccluster = this.getTCClusterBasedOnListAndStringArrays(origAS.getOldTestList(),rmList);
		tccluster.setOldTestFailed(false);
		
		int currentIndex = HandleFacotry.getVersionClusterHandle().getIndexOfByVersionName(origAS.getVersionName(), FileProcess.versions_orig);
		tccluster.setCurrentIndex(currentIndex);
		tccluster.setCurrentVersion(origAS.getVersionName());
		
		int oldIndex = HandleFacotry.getVersionClusterHandle().getIndexOfByVersionName(origAS.getVersionFrom(), FileProcess.versions_orig);
		tccluster.setOldIndex(oldIndex);
		tccluster.setOldVersion(origAS.getVersionFrom());
		
		tccluster.setOrderInClusters(rmList.size()+currentIndex-oldIndex);
		return tccluster;
	}
	
	
	/**
	 * @function:
	 *  	print the contents in list of one List of TCCluster
	 * @return:
	 * 		void
	 * @input:
	 * 		TCCluster input: the TCCluster List we want to print out
	 */
	public void printListOfClusters(List<TCCluster> input){
		for(int i=0;i<input.size();i++){
			TCCluster currentOne = input.get(i);
			System.out.println("*** index in the list ***: "+i);
			this.printTCCluster(currentOne);
		}
	}
	
	/**
	 * @function:
	 *  	remove the test case in TCCluster list from test case bean list 
	 * @return:
	 * 		void
	 * @input:
	 * 		List<TestCaseBean>  orig :  the original list of test case 
	 * 		List<TCCluster> rm  	 :  the TCCluster list that contain the test case we want to remove
	 * @param orig
	 * @param rm
	 */
	public List<TestCaseBean> removeTCClusterListFromTestCaseList(List<TestCaseBean> orig,List<TCCluster> rm){
		List<TestCaseBean> result = HandleFacotry.getTestCaseHandle().copyList(orig);
		for(int i=0;i<rm.size();i++){
			result = HandleFacotry.getTestCaseHandle().removeTestCaseFromTestCaseBean(result, rm.get(i).getTestcases());
		}
		return result;
	}
	
	
	/**
	 * @function:
	 *  	copy the TCCluster
	 * @return:
	 * 		void
	 * @throws IOException 
	 * @input:
	 * 		String inputDir : the directory the original file belongs to
	 * 		String outputDir: the target directory it copy to
	 */
	public void copyTestCaseFileByTCCluster(String inputDir, String outputDir, TCCluster tccluster) throws IOException{
		outputDir = outputDir+tccluster.getOrderInClusters();
		File file = new File(outputDir);
		if(!file.exists()||!file.isDirectory()){
			file.mkdir();
		}
		outputDir = outputDir+"/";
		HandleFacotry.getTestCaseHandle().copyTestCaseFileByList(inputDir, outputDir,tccluster.getTestcases());
	}
	
	
	/**
	 * @function:
	 *  	copy a list of TCCluster
	 * @return:
	 * 		void
	 * @throws IOException 
	 * @input:
	 * 		String inputDir : the directory the original file belongs to
	 * 		String outputDir: the target directory it copy to
	 */
	
	public void copyTestCaseFileByTCClusterList(String inputDir, String outputDir, List<TCCluster> tcclusters) throws IOException{
		for(int i=0;i<tcclusters.size();i++){
			this.copyTestCaseFileByTCCluster(inputDir, outputDir, tcclusters.get(i));
		}
	}
	
	
	
	/**
	 * @function:
	 *  	copy the TCCluster
	 * @return:
	 * 		void
	 * @throws IOException 
	 * @input:
	 * 		String inputDir : the directory the original file belongs to
	 * 		String outputDir: the target directory it copy to
	 */
	public void copyTestCaseBeanFileByTCCluster(String inputDir, String outputDir, TCBeanCluster tccluster) throws IOException{
		outputDir = outputDir+tccluster.getOrderInClusters();
		File file = new File(outputDir);
		if(!file.exists()||!file.isDirectory()){
			file.mkdir();
		}
		outputDir = outputDir+"/";
		HandleFacotry.getTestCaseHandle().copyTestCaseBeanFileByList(inputDir, outputDir,tccluster.getTestcases());
	}
	
	
	/**
	 * @function:
	 *  	copy a list of TCCluster
	 * @return:
	 * 		void
	 * @throws IOException 
	 * @input:
	 * 		String inputDir : the directory the original file belongs to
	 * 		String outputDir: the target directory it copy to
	 */
	
	public void copyTestCaseBeanFileByTCClusterList(String inputDir, String outputDir, List<TCBeanCluster> tcclusters) throws IOException{
		for(int i=0;i<tcclusters.size();i++){
			this.copyTestCaseBeanFileByTCCluster(inputDir, outputDir, tcclusters.get(i));
		}
	}

}
