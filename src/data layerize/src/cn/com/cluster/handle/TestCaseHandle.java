package cn.com.cluster.handle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import cn.com.cluster.bean.TestCaseBean;
import cn.com.main.FileProcess;
import cn.com.main.TestCase;
public class TestCaseHandle {
	/**
	 *@function:
	 * 		remove test cases of list rm from orig
	 *@return:
	 *		List<TestCase> : the result list that only contain the test cases in the original but not remove 
	 *@Input: 
	 *		List<TestCase> orig : A test case list, which is the base list
	 *		List<TestCase> rm   : A test case list, from which we can get test cases that we wanna remove in the orig 
	 * 
	 */
	public List<TestCase> removeTestCase(List<TestCase> orig, List<TestCase> rm){
		List<TestCase> result = new ArrayList<TestCase>();
		for(int i=0; i<orig.size();i++){
			TestCase current = orig.get(i);
			if(this.isInTestCaseList(rm,current.getTestCaseString())==null){
				result.add(current);
			}
		}
		
		return result;
	}
	
	
	
	/**
	 *@function:
	 * 		remove test cases of list rm from orig
	 *@return:
	 *		List<TestCase> : the result list that only contain the test cases in the original but not remove 
	 *@Input: 
	 *		List<TestCase> orig : A test case list, which is the base list
	 *		List<TestCase> rm   : A test case list, from which we can get test cases that we wanna remove in the orig 
	 * 
	 */
	public List<TestCaseBean> removeTestCaseFromTestCaseBean(List<TestCaseBean> orig, List<TestCase> rm){
		List<TestCaseBean> result = new ArrayList<TestCaseBean>();
		for(int i=0; i<orig.size();i++){
			TestCaseBean current = orig.get(i);
			if(this.isInTestCaseList(rm,current.getTestCaseString())==null){
				result.add(current);
			}
		}
		
		return result;
	}
	
	/**
	 * function:
	 * 		test test case string is in a List
	 * return:
	 * 		null  when no target testCase String
	 *  	TestCase  the test case we found
	 * Input:
	 *  	List<TestCase> testcases: the test case need to be check
	 *  	String testCaseString: target test case string need to be found in the list 
	 */
	public TestCase isInTestCaseList(List<TestCase> testcases, String testCaseString){
		for(int i=0;i<testcases.size();i++){
			TestCase testcase = testcases.get(i);
			if(testcase.getTestCaseString().equals(testCaseString)){
				return testcase;
			}
		}
		return null;
	}
	
	/**
	 * @function:
	 *  	print the string of test case in one list
	 * @return:
	 * 		void
	 * @input:
	 * 		List<TestCase> input: the list we want to print out
	 */
	public void printTestCaseList(List<TestCase> input){
		
		System.out.println("size of list: "+input.size());
		for(int i=0;i<input.size();i++){
			System.out.println(input.get(i).getFileName());
		}
	}
	
	/**
	 * @function:
	 *  	print the string of test case in one list
	 * @return:
	 * 		void
	 * @input:
	 * 		List<TestCase> input: the list we want to print out
	 */
	public void printTestCaseBeanList(List<TestCaseBean> input){
		
		System.out.println("size of list: "+input.size());
		for(int i=0;i<input.size();i++){
			System.out.println(input.get(i).getTestCaseString());
		}
	}

	
	
	/**
	 * @function:
	 *  	judge if the test case is in the duplicated test cases after preprocessing 
	 * @return:
	 * 		TestCase : when the test case name can be found in the duplicated test cases , return the found test case
	 * 		null     : when the target test case cannot be found 
	 * @input:
	 * 		String testCaseName : the test case is going to checked
	 * 		String testCaseName : the processing version
	 * @param testcaseName
	 * @param versionName
	 * @return
	 */
	public TestCase isInDuplicated(String testcaseName, String versionName) {
		int index = HandleFacotry.getVersionClusterHandle().getIndexOfByVersionName(versionName, FileProcess.versions_orig);
		// System.out.println(this.versionName);
		// System.out.println(index);
		for (int i = 0; i < FileProcess.duplicateTestCaseEachVersion[index].size(); i++) {
			TestCase temp = FileProcess.duplicateTestCaseEachVersion[index].get(i);
			if (testcaseName.equals(temp.getTestCaseString())) {
				return temp;
			}
		}

		return null;
	}
	
	
	
	/**
	 * @function:
	 *  	judge if the test case is a failed one
	 * @return:
	 * 		true 	 : when the target test name is a failed one
	 * 		false    : when the target test case is not a failed one
	 * @input:
	 * 		String testcase 			 : the name of test case  that is going to checked
	 * 		List<String> failedTestCases : the processing version
	 * 
	 */
	public boolean isFailedTestCase(String testcase,List<String> failedTestCases) {
		for (int i = 0; i < failedTestCases.size(); i++) {
			String temp = failedTestCases.get(i);
			if (temp.equals(testcase)) {
				return true;
			}
		}
		return false;
	}
	
	
	
	/**
	 * @function:
	 *  	get the failed test case set
	 * @return:
	 * 		List<String> : the String list containing all failed test cases
	 * @input:
	 * 		String wholeFileName : the name of file that contain all these information
	 */
	public List<String> getFailedTestCaseNameList(String wholeFileName) throws IOException{
		List<String> failedTestCases = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(wholeFileName))));
		String line;
		while((line=reader.readLine())!=null){
			failedTestCases.add(line);
		}
		reader.close();
		return failedTestCases;
	}
	
	
	public List copyList(List copy){
		List result  = new ArrayList();
		for(int i=0;i<copy.size();i++){
			result.add(copy.get(i));
		}
		
		return result;
	}

	
	/**
	 * @function:
	 *  	copy the test case file
	 * @return:
	 * 		void
	 * @throws IOException 
	 * @input:
	 * 		String inputDir : the directory the original file belongs to
	 * 		String outputDir: the target directory it copy to
	 */
	public void  copyTestCaseFileByName(String inputDir, String outputDir, TestCase testcase) throws IOException{
		String  tmp = testcase.getFileName();
		//tmp= tmp.replace("=", "\\=");
		String commands  ="cp "+inputDir+tmp+" "+outputDir/*+testcase.getFileName()*/;
		System.out.println(commands);
		Runtime.getRuntime().exec(commands);
		//System.out.println(inputDir+tmp+"   " +outputDir+tmp);
		//HandleFacotry.getFileCopy().copyFile(inputDir+tmp, outputDir+tmp);
		
	}
	
	/**
	 * @function:
	 *  	copy the test case file by list
	 * @return:
	 * 		void
	 * @throws IOException 
	 * @input:
	 * 		String inputDir : the directory the original file belongs to
	 * 		String outputDir: the target directory it copy to
	 */
	public void copyTestCaseFileByList(String inputDir, String outputDir, List<TestCase> testcases) throws IOException{
		for(int i=0;i<testcases.size();i++){
			this.copyTestCaseFileByName(inputDir, outputDir, testcases.get(i));
		}
	}
	
	
	
	/**
	 * @function:
	 *  	copy the test case file
	 * @return:
	 * 		void
	 * @throws IOException 
	 * @input:
	 * 		String inputDir : the directory the original file belongs to
	 * 		String outputDir: the target directory it copy to
	 */
	public void  copyTestCaseBeanFileByName(String inputDir, String outputDir, TestCaseBean testcaseBean) throws IOException{
		String  tmp = testcaseBean.getFileName();
		//tmp= tmp.replace("=", "\\=");
		//System.out.println(tmp);
		String commands  ="cp "+inputDir+tmp+" "+outputDir/*+testcaseBean.getFileName()*/;
		Process process = Runtime.getRuntime().exec(commands);
		
		//HandleFacotry.getFileCopy().copyFile(inputDir+tmp, outputDir+tmp);
		
	}
	
	/**
	 * @function:
	 *  	copy the test case  bean file by list
	 * @return:
	 * 		void
	 * @throws IOException 
	 * @input:
	 * 		String inputDir : the directory the original file belongs to
	 * 		String outputDir: the target directory it copy to
	 */
	public void copyTestCaseBeanFileByList(String inputDir, String outputDir, List<TestCaseBean> testcases) throws IOException{
		for(int i=0;i<testcases.size();i++){
			this.copyTestCaseBeanFileByName(inputDir, outputDir, testcases.get(i));
		}
	}
	

}
