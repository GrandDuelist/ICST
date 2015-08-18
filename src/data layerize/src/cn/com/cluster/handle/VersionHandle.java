package cn.com.cluster.handle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.com.cluster.bean.BeanFactory;
import cn.com.cluster.bean.TestCaseBean;
import cn.com.cluster.bean.VersionBean;
import cn.com.cluster.bean.VersionCluster;
import cn.com.main.FileProcess.StringArray;
import cn.com.main.TestCase;

public class VersionHandle {
	
	
	/**
	 * @function:
	 *  	get the all test case set
	 * @return:
	 * 		List<TestCase> : the test case list containing all  test cases
	 * @throws IOException 
	 * @input:
	 * 		String testcaseDir : the name of file that contain all test cases
	 * 		String failedFileName: the name of file that can be used to get failed test cases
	 */
	public List<VersionBean> getAllTestCaseBeans(String testcaseDir, String failedFileName, List<StringArray>[] terraceFeatures, String[] dirOrder) throws IOException{
		List<VersionBean> allVersions = new ArrayList<VersionBean>();
		File phDir = new File(testcaseDir);
		File subDirs[]=phDir.listFiles();
		
		List<String> failedTestCases = HandleFacotry.getTestCaseHandle().getFailedTestCaseNameList(failedFileName);
		List<File> subDirsList = this.getOrderedFileDirectories(subDirs, dirOrder);
		for(int i=1;i< subDirsList.size();i++){
			File subDir = subDirsList.get(i);
			
			VersionBean current = this.getTestCaseBeansOfOneVersion(subDir, failedTestCases);
			current = this.addVersionClusterToVersionBean(current, terraceFeatures);
			allVersions.add(current);
		}
		
		
		
		return allVersions;
	}
	
	
	/**
	 * @function:
	 * 		reorder the file directory
	 * @param dirs
	 * @param order
	 * @return
	 */
	public List<File> getOrderedFileDirectories(File[] dirs, String[] order){
		
		List<File> result = new ArrayList<File>();
		for(int i=0;i<order.length;i++){
			for(int j=0;j<dirs.length;j++){
				if(dirs[j].getName().equals(order[i])){
					result.add(dirs[j]);
				}
			}
		}
		
		return result;
	}
	
	/**
	 * @function:
	 *  	get the all test case set
	 * @return:
	 * 		VersionBean : the version that contain features of test cases of each version
	 * @input:
	 * 		File dir : dir of each version
	 * 		List<String>failedTestCase : list of failed test cases name
	 */
	public VersionBean getTestCaseBeansOfOneVersion(File dir,List<String> failedTestCases){
		
		String fileNames[] = dir.list();
		String origFileName[] = dir.list();
		VersionBean version = BeanFactory.getVersionBean();		// version
		version.setVersionNameString(dir.getName());            // version name
		
		fileNames = this.unifyNameOfFile(fileNames);			

		for(int i=0;i<fileNames.length;i++){
			TestCaseBean testcase = BeanFactory.getTestCaseBean();
			String currentName = fileNames[i];
			testcase.setTestCaseString(currentName);
			testcase.setFileName(origFileName[i]);
			//fail
			if(HandleFacotry.getTestCaseHandle().isFailedTestCase(currentName,failedTestCases)){
				testcase.setFail(true);
				version.getFailedTestCases().add(testcase);
			}else{
				testcase.setFail(false);
				version.getPassTestCases().add(testcase);
			}
			
			//duplicated
			if(HandleFacotry.getTestCaseHandle().isInDuplicated(currentName,dir.getName())!=null){
				testcase.setDuplicated(true);
				version.getDuplicatedTestCases().add(testcase);
			}else{
				testcase.setDuplicated(false);
				version.getNoDuplicatedTestCases().add(testcase);
			}
			
			
			testcase.setVersion(version);                         //version
			version.getTestCases().add(testcase);
		}
		
		return version;
	}
	

	/**
	 * @function:
	 *  	process one version and add the version cluster for it
	 * @return:
	 * 		VersionBean : the version that contain features of test cases of each version
	 * @input:
	 * 		VersionBean orig : the original VersionBean to be add cluster
	 * 		List<StringArray>[] terraceFeatures : terrace features of all versions
	 */
	public VersionBean addVersionClusterToVersionBean(VersionBean orig, List<StringArray>[] terraceFeatures){
		// fail and pass
		VersionCluster cluster = HandleFacotry.getVersionClusterHandle().getVersionClusterBasedOnListOfStringArray(orig, terraceFeatures);
		
		// new type
		cluster = HandleFacotry.getVersionClusterHandle().getVersionClusterBasedOnOldTestCase(cluster,orig.getTestCases());
		orig.setVersionCluster(cluster);
		orig.setNewTestCases(cluster.getNewTypeCluster().getTestcases());
		return orig;
	}
	
	
	/**
	 * @function:
	 *  	get the all test case set
	 * @return:
	 * 		List<TestCase> : the test case list containing all  test cases
	 * @throws IOException 
	 * @input:
	 * 		String testcaseDir : the name of file that contain all test cases
	 * 		String failedFileName: the name of file that can be used to get failed test cases
	 */
	public String[] unifyNameOfFile(String []fileNames){
		for(int i=0;i<fileNames.length;i++){
			String tempName = fileNames[i];
			tempName = tempName.substring(0,tempName.indexOf(".txt"));
			tempName = tempName.replace("@", "?");
			fileNames[i] = tempName;
		}
		return fileNames;
	}
	
	
	public void printVersion(VersionBean input){
		System.out.println("--------------------------VERSION  "+input.getVersionNameString()+"   ---------------------");
		System.out.println("--------------------------ALL TC   "+input.getTestCases().size()+"------------------------------");
		System.out.println("--------------------------FAIL TC  "+input.getFailedTestCases().size()+"------------------------------");
		System.out.println("--------------------------PASS TC  "+input.getPassTestCases().size()+"------------------------------");
		System.out.println("--------------------------VERSION CLUSTER------------------------------");
		HandleFacotry.getVersionClusterHandle().printVersionCluster(input.getVersionCluster());
	}

	public void printVersionList(List<VersionBean> input){
		for(int i=0;i<input.size();i++){
			this.printVersion(input.get(i));
		}
	}
	
	
	public void copyVersion(VersionBean version, String primaryinputDir, String primaryoutputDir) throws IOException{
		System.out.println("cluster:  "+version.getVersionNameString()+"........");
		HandleFacotry.getVersionClusterHandle().copyTestCaseFileByVersionCluster(primaryinputDir, primaryoutputDir, version.getVersionCluster());
	}
}
