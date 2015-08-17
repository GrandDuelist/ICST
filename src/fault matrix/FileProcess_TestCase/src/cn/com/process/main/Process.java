package cn.com.process.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Process {

	public List<TestCase> testCases = new ArrayList<TestCase>(); // all test
																	// case in
																	// order
																	// name
	private TestCase currentTextCase;
	private List<TestCase> testCasesForOneVersion = new ArrayList<TestCase>();
	private List<String> bugsForOneVersion = new ArrayList<String>();
	private List<TestCase> passTestCases = new ArrayList<TestCase>();
	private List<TestCase> brokenTestCases = new ArrayList<TestCase>();
	// number of all - test cases
	private int numTestCaseWithDashOnly = 0;
	// number of passed test cases except dash only
	private int numTestCasePass = 0;
	// number of failed test cases except dash only
	private int numTestCaseFail = 0;
	// number of test case used except dash only
	private int numTestCaseValid = 0;
	private int numBrokenTestCase=0;
	// failed is divided by valid ones
	private float faultRate = 0.000000f;
	// maximum bug per test case
	private int numMaxBugPerTestCase = 0;
	// maximum test cases per bugs
	private int numMaxTestCasePerBug = 0;
	// minimum test cases per bugs
	private int numMinTestCasePerBug = 0;
	// minimum bugs per test cases
	private int numMinBugPerTestCase = 0;
	// name of maximum bug per test case
	private String nameMaxBugPerTestCase = null;
	// name of minimum bug per test case
	private String nameMinBugPerTestCase = null;
	// name of maximun test case per bug
	private String nameMaxTestCasePerBug = null;
	// name of minimum test case per bug
	private String nameMinTestCasePerBug = null;

	// read file
	public void readFile(String filePath) {
		File file = new File(filePath);
		if (file.isFile() && file.exists()) {
			try {
				InputStreamReader reader = new InputStreamReader(
						new FileInputStream(file));
				BufferedReader bufferReader = new BufferedReader(reader);
				String line = null;

				while ((line = bufferReader.readLine()) != null) {
					/* processPerLine(line); */
					// System.out.println(line);
					this.processPerLine(line);
				}

				/*
				 * for(int i=0;i<this.testCases.size();i++) { TextCase t =
				 * this.testCases.get(i);
				 * System.out.println(t.getTextCaseName()); for(int
				 * j=0;j<t.getBugs().size();j++) { String b =
				 * t.getBugs().get(j); System.out.println(b); } }
				 */
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Put testcase into referented bug
	 * 
	 * @param perLine
	 */
	public void processPerLine(String perLine) {
		if (perLine.contains("show")) {
			// test Case
			currentTextCase = isFileCaseContained(perLine);

			if (currentTextCase == null) {
				this.currentTextCase = new TestCase();
				this.currentTextCase.setTextCaseName(perLine);
				
			}
				//status
		}else if(perLine.contains("Test unclear/broken")){
			currentTextCase.setStatus("Test unclear/broken");
			this.brokenTestCases.add(this.currentTextCase);
		}else if(perLine.contains("Pass")){
			currentTextCase.setStatus("Pass");
			this.passTestCases.add(this.currentTextCase);
		}
		else {
			// bugs
			if(currentTextCase.getStatus()==null||currentTextCase.getStatus().equals("")){
				currentTextCase.setStatus("Fail");
				this.testCases.add(this.currentTextCase);
			}
			if (perLine != null && currentTextCase != null) {
				if (!currentTextCase.isBugContained(perLine)) {
					currentTextCase.getBugs().add(perLine);
				}
			}

		}

	}

	public TestCase isFileCaseContained(String name) {
		for (int i = 0; i < this.testCases.size(); i++) {
			TestCase t = this.testCases.get(i);
			if (t.getTextCaseName().equals(name)) {
				return t;
			}
		}

		return null;
	}

	/**
	 * process the files by dir
	 * 
	 * @param dataDir
	 * @throws IOException
	 */
	public void getBinaryMatrixWithoutDash(String dataDir) throws IOException {
		
		File dir = new File(dataDir);
		File[] subDirs = dir.listFiles();

		boolean isDir = true;
		// get each version
		for (int i = 0; i < subDirs.length; i++) {

			this.initFeatureNum();
			File subDir = subDirs[i];

			// get the matrix head
			if (subDir.isDirectory()) {
				System.out.println("Vesion: " + subDir.getName());
				File[] files = subDir.listFiles();

				MyString mySs[] = new MyString[files.length];// 创建自定义排序的数组
				for (int n = 0; n < files.length; n++) {
					mySs[n] = new MyString(files[n].getName());
				}

				Arrays.sort(mySs);// 排序

				for (int j = 0; j < files.length; j++) {
					File file = new File(subDir.getPath() + "/" + mySs[j].s);
					// System.out.println(file.getName());
					processPerFile(file);
				}
				
			} else {
				processPerFile(subDir);
				isDir = false;
			}

			// set the matrix
			if (isDir) {
				String filePath = FileProcess.OUTPUT_PATH + subDir.getName();
				File file = new File(filePath);
				if (!file.exists()) {
					file.mkdirs();
				}

				String matrixFilePath = FileProcess.OUTPUT_PATH
						+ subDir.getName() + "/" + "fault_matrix.txt";
				String featureFilePath = FileProcess.OUTPUT_PATH
						+ subDir.getName() + "/" + "feature.txt";
				this.outputMatrix(matrixFilePath);
				this.outputFeatureNum(featureFilePath);
			}

			// open the file

		}

		// just files in the dir, one version
		if (!isDir) {
			String filePath = FileProcess.OUTPUT_PATH + "litmus";
			File file = new File(filePath);
			if (!file.exists()) {
				file.mkdir();
			}

			String matrixFilePath = FileProcess.OUTPUT_PATH + "litmus" + "/"
					+ "fault_matrix.txt";
			String featureFilePath = FileProcess.OUTPUT_PATH + "litmus" + "/"
					+ "feature.txt";
			this.outputMatrix(matrixFilePath);
			this.outputFeatureNum(featureFilePath);
		}
	}

	/**
	 * init the numbers for statistic
	 */
	public void initFeatureNum() {
		// number of all - test cases
		numTestCaseWithDashOnly = 0;
		// number of passed test cases except dash only
		numTestCasePass = 0;
		// number of failed test cases except dash only
		numTestCaseFail = 0;
		// number of test case used except dash only
		numTestCaseValid = 0;
		// failed is divided by valid ones
		faultRate = 0;
		// maximum bug per test case
		numMaxBugPerTestCase = 0;
		// maximum test cases per bugs
		numMaxTestCasePerBug = 0;
		// minimum test cases per bugs
		numMinTestCasePerBug = 0;
		// minimum bugs per test cases
		numMinBugPerTestCase = 0;
		numBrokenTestCase = 0;
		testCasesForOneVersion = new ArrayList<TestCase>();
		bugsForOneVersion = new ArrayList<String>();
		this.currentTextCase = null;
	}

	/**
	 * output the feature numbers like failed number passed number and so on
	 * 
	 * @param filePath
	 * @throws IOException
	 */
	public void outputFeatureNum(String filePath) throws IOException {
		// get the maximum test case of bug
		int testCasePerBug[] = new int[1000];
		for (int i = 0; i < this.bugsForOneVersion.size(); i++) {
			testCasePerBug[i] = 0;
			String bug = this.bugsForOneVersion.get(i);
			for (int j = 0; j < this.testCasesForOneVersion.size(); j++) {
				TestCase tempTestCase = this.testCasesForOneVersion.get(j);
				if (tempTestCase.isBugContained(bug)) {
					testCasePerBug[i]++;
				}
			}
		}

		this.numMaxTestCasePerBug = 0;
		this.numMinTestCasePerBug = 10000;

		for (int i = 0; i < this.bugsForOneVersion.size(); i++) {
			if (this.numMaxTestCasePerBug < testCasePerBug[i]) {
				this.numMaxTestCasePerBug = testCasePerBug[i];
				this.nameMaxTestCasePerBug = this.bugsForOneVersion.get(i);
			}
			if (this.numMinTestCasePerBug > testCasePerBug[i]) {
				this.numMinTestCasePerBug = testCasePerBug[i];
				this.nameMinTestCasePerBug = this.bugsForOneVersion.get(i);
			}
		}

		File output = new File(filePath);
		if (!output.exists()) {

			output.createNewFile();

		}
		BufferedWriter outputBuffer = new BufferedWriter(new FileWriter(output));
		this.output2(outputBuffer);
		outputBuffer.close();

	}
	
	
	public void output(BufferedWriter outputBuffer) throws IOException
	{
		outputBuffer.append("Failed  Number: " + this.numTestCaseFail + "\n");
		outputBuffer.append("Passed  Number: " + this.numTestCasePass + "\n");
		outputBuffer.append("dash only Number: " + this.numTestCaseWithDashOnly
				+ "\n");
		outputBuffer.append("Valid   Number: " + this.numTestCaseValid + "\n");
		this.faultRate = ((float) this.numTestCaseFail / (float) this.numTestCaseValid) * 100;
		outputBuffer.append("Fault   Rate  : " + this.faultRate + "%\n");
		outputBuffer.append("Bug     Number: " + this.bugsForOneVersion.size()
				+ "\n");
		outputBuffer.append("Bug Pointed By Most  TestCase: "
				+ this.nameMaxTestCasePerBug + "  Pointed Test Case Number:  "
				+ this.numMaxTestCasePerBug + "\n");
		outputBuffer.append("Bug Pointed By Least TestCase: "
				+ this.nameMinTestCasePerBug + "  Pointed Test Case Number:  "
				+ this.numMinTestCasePerBug + "\n");
		outputBuffer.append("Test Case Has Least Bugs: "
				+ this.nameMinBugPerTestCase + "  Bugs Number:  "
				+ this.numMinBugPerTestCase + "\n");
		outputBuffer.append("Test Case Has Most  Bugs: "
				+ this.nameMaxBugPerTestCase + "  Bugs Number:  "
				+ this.numMaxBugPerTestCase + "\n");
		outputBuffer.append("Broken TestCase: "+this.numBrokenTestCase);
	}
	
	
	public void output2(BufferedWriter outputBuffer) throws IOException
	{
		outputBuffer.append(this.numTestCaseFail + "\n");
		outputBuffer.append(this.numTestCasePass + "\n");
		outputBuffer.append(this.numTestCaseWithDashOnly
				+ "\n");
		outputBuffer.append(this.numTestCaseValid + "\n");
		this.faultRate = ((float) this.numTestCaseFail / (float) this.numTestCaseValid) * 100;
		outputBuffer.append(this.faultRate + "%\n");
		outputBuffer.append(this.bugsForOneVersion.size()
				+ "\n");
		outputBuffer.append(this.nameMaxTestCasePerBug + " \n"
				+ this.numMaxTestCasePerBug + "\n");
		outputBuffer.append(this.nameMinTestCasePerBug + " \n"
				+ this.numMinTestCasePerBug + "\n");
		outputBuffer.append(this.nameMinBugPerTestCase + "\n"
				+ this.numMinBugPerTestCase + "\n");
		outputBuffer.append(this.nameMaxBugPerTestCase + "\n"
				+ this.numMaxBugPerTestCase + "\n");
		outputBuffer.append(this.numBrokenTestCase+"");
	}
	
	/**
	 * remove the broken ones
	 */
	public void removeBrokenOnes(String dirPath)
	{
		
	}

	/**
	 * output the fault matrix
	 * 
	 * @param filePath
	 * @throws IOException
	 */
	public void outputMatrix(String filePath) throws IOException {

		// remove dash from row
		for (int i = 0; i < this.testCases.size(); i++) {
			TestCase testCase = this.testCases.get(i);
			for (int j = 0; j < testCase.getBugs().size(); j++) {
				if (testCase.getBugs().get(j).equals("-")) {
					testCase.getBugs().remove(testCase.getBugs().get(j));
				}
			}
		}
		File output = new File(filePath);
		if (!output.exists()) {

			output.createNewFile();

		}
		BufferedWriter outputBuffer = new BufferedWriter(new FileWriter(output));

		this.numMaxBugPerTestCase = 0;
		this.numMinBugPerTestCase = 100000;
		// init matrix as zero
		for (int k = 0; k < this.testCasesForOneVersion.size(); k++) {
			TestCase tempTestCase = this.testCasesForOneVersion.get(k);

			for (int j = 0; j < this.bugsForOneVersion.size(); j++) {
				String tempBug = this.bugsForOneVersion.get(j);
				if (tempTestCase.isBugContained(tempBug)) {

					tempTestCase.getBugsBinary().add(true);
				} else {
					tempTestCase.getBugsBinary().add(false);
				}
			}

			// get max bugs per test case
			if (tempTestCase.getBugs().size() > 0
					&& tempTestCase.getBugs().size() > this.numMaxBugPerTestCase) {
				this.numMaxBugPerTestCase = tempTestCase.getBugs().size();
				this.nameMaxBugPerTestCase = tempTestCase.getTextCaseName();
			}

			// get min bugs per test case
			if (tempTestCase.getBugs().size() > 0
					&& tempTestCase.getBugs().size() < this.numMinBugPerTestCase) {
				this.numMinBugPerTestCase = tempTestCase.getBugs().size();
				this.nameMinBugPerTestCase = tempTestCase.getTextCaseName();
			}

		}

		/*
		 * for (int j = 0; j < this.bugsForOneVersion.size(); j++) {
		 * outputBuffer.append(this.bugsForOneVersion.get(j) + " "); }
		 * outputBuffer.append("\n");
		 */
		// print the matrix
		System.out.println(this.testCasesForOneVersion.size());
		for (int k = 0; k < this.testCasesForOneVersion.size(); k++) {
			TestCase tempTextCase = this.testCasesForOneVersion.get(k);
			
			// outputBuffer.append(tempTextCase.getTextCaseName()+" ");
			// outputBuffer.append(tempTextCase.getTextCaseName() + " ");
			System.out.println(tempTextCase.getBugsBinary().size());
			for (int j = 0; j < tempTextCase.getBugsBinary().size(); j++) {
				boolean tempBin = tempTextCase.getBugsBinary().get(j);
				if (tempBin) {
					
					outputBuffer.append(1 + " ");

				} else {
					outputBuffer.append(0 + " ");
				}
			}

			outputBuffer.append("\n");

		}

		outputBuffer.close();

	}

	/**
	 * process every file in the directory of every version
	 * 
	 * @param file
	 */
	public void processPerFile(File file) {

		String fileName = file.getName().substring(0,
				file.getName().indexOf(".txt"));
		fileName = fileName.replace('@', '?');
		
		
		
		boolean isContained = false;
		boolean isDash = false;

		for (int i = 0; i < testCases.size(); i++) {
			TestCase testCase = testCases.get(i);
			if (fileName.equals(testCase.getTextCaseName())) {
				if (isTestCaseHasDashBugOnly(testCase)) {
					// remove the file
					isDash = true;
					this.numTestCaseWithDashOnly++;
					file.delete();
				} else {
					// put it into the list
					this.testCasesForOneVersion.add(testCase);
					isContained = true;
					this.numTestCaseFail++;
				}

				if (isContained) {
					// add bugs into row
					for (int j = 0; j < testCase.getBugs().size(); j++) {
						if (!testCase.getBugs().get(j).equals("-")) {
							if (!this.isInBugsOneVersion(testCase.getBugs()
									.get(j))) {
								this.bugsForOneVersion.add(testCase.getBugs()
										.get(j));
							}
						}
					}
				}
				break;
			}
		}

		// set passed one as new test case
		if (!isDash && !isContained) {
			
			if(!this.isPassTestCase(fileName)&&this.isBrokenTestCase(fileName)){
				this.numBrokenTestCase++;
				file.delete();
			}else{
			TestCase tmp = new TestCase();
			tmp.setTextCaseName(fileName);
			this.testCasesForOneVersion.add(tmp);
			this.numTestCasePass++;
			}
		}

		if (!isDash) {
			this.numTestCaseValid++;
		}
		}
	

	/**
	 * if t just has a bug -, the function will return true;
	 * 
	 * @param t
	 * @return
	 */
	public boolean isTestCaseHasDashBugOnly(TestCase t) {
		for (int i = 0; i < t.getBugs().size(); i++) {
			if (!t.getBugs().get(i).equals("-")) {
				return false;
			}
		}
		return true;
	}

	/**
	 * get matrix through order_name file
	 * 
	 * @param versionName
	 * @throws IOException
	 */
	public void getBinaryMatrix(String versionName) throws IOException {
		// TODO Auto-generated method stub
		File file = new File(versionName);
		File output = new File(FileProcess.PATH + "test_matrix.txt");
		if (!output.exists()) {

			output.createNewFile();

		}

		BufferedWriter outputBuffer = new BufferedWriter(new FileWriter(output));

		if (file.isFile() && file.exists()) {

			InputStreamReader reader = new InputStreamReader(
					new FileInputStream(file));
			BufferedReader bufferReader = new BufferedReader(reader);
			String line = null;

			while ((line = bufferReader.readLine()) != null) {
				/* processPerLine(line); */
				// System.out.println(line);
				boolean isInTextCase = false;
				if (line.contains("txt")) {
					line = line.replace('@', '?');

					line = line
							.substring(line.indexOf("\"") + 1, line
									.indexOf(".txt") > 1 ? line.indexOf(".txt")
									: 1);

					// this.processPerLine(line);
					for (int i = 0; i < this.testCases.size(); i++) {
						TestCase t = testCases.get(i);
						if (t.getTextCaseName().equals(line)) {

							this.testCasesForOneVersion.add(t);
							isInTextCase = true;

							// get all bugs in one version
							for (int j = 0; j < t.getBugs().size(); j++) {
								String tempBug = t.getBugs().get(j);
								if (!this.isInBugsOneVersion(tempBug)) {
									this.bugsForOneVersion.add(tempBug);
								}
							}

						}
					}

					if (!isInTextCase) {

						TestCase tmp = new TestCase();
						tmp.setTextCaseName(line);
						this.testCasesForOneVersion.add(tmp);

					}
				}
			}

			System.out.println(this.bugsForOneVersion.size());

			// set the matrix
			// init matrix as zero
			for (int i = 0; i < this.testCasesForOneVersion.size(); i++) {
				TestCase tempTextCase = this.testCasesForOneVersion.get(i);

				for (int j = 0; j < this.bugsForOneVersion.size(); j++) {
					String tempBug = this.bugsForOneVersion.get(j);
					if (tempTextCase.isBugContained(tempBug)) {

						tempTextCase.getBugsBinary().add(true);
					} else {
						tempTextCase.getBugsBinary().add(false);
					}
				}
			}

			// print the matrix
			for (int i = 0; i < this.testCasesForOneVersion.size(); i++) {
				TestCase tempTextCase = this.testCasesForOneVersion.get(i);
				// outputBuffer.append(tempTextCase.getTextCaseName()+" ");
				for (int j = 0; j < tempTextCase.getBugsBinary().size(); j++) {
					boolean tempBin = tempTextCase.getBugsBinary().get(j);
					if (tempBin) {
						// System.out.print(1+"\t");
						outputBuffer.append(1 + " ");

					} else {
						outputBuffer.append(0 + " ");
					}
				}

				outputBuffer.append("\n");

			}

			reader.close();
			outputBuffer.close();

		}

	}

	/**
	 * check if the bug has been found in the version, no duplicated
	 * 
	 * @param bugName
	 * @return
	 */
	public boolean isInBugsOneVersion(String bugName) {
		for (int i = 0; i < this.bugsForOneVersion.size(); i++) {
			String temp = this.bugsForOneVersion.get(i);
			if (temp.equals(bugName)) {
				return true;
			}
		}
		return false;
	}

	
	public boolean isBrokenTestCase(String TestCaseName) {
		for (int i = 0; i < this.brokenTestCases.size(); i++) {
			TestCase temp = this.brokenTestCases.get(i);
			if (temp.getTextCaseName().equals(TestCaseName)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isPassTestCase(String TestCaseName) {
		for (int i = 0; i < this.passTestCases.size(); i++) {
			TestCase temp = this.passTestCases.get(i);
			if (temp.getTextCaseName().equals(TestCaseName)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * remove the passed test cases and just keep the failed ones
	 * 
	 * @param dir
	 * @return the number of failed test cases
	 */

	public int keepFaildTestCase(String dir) {

		File dirFile = new File(dir);
		int totalNum = 0;

		if (dirFile.exists()&&dirFile.isDirectory()) {

			File subDirFile[] = dirFile.listFiles();

			for (int j = 0; j < subDirFile.length; j++) {
				System.out.println(subDirFile[j].getName());
				int failedNum = 0;
				File files[] = subDirFile[j].listFiles();

				for (int i = 0; i < files.length; i++) {
					String fileName = files[i].getName().substring(0,
							files[i].getName().indexOf(".txt"));
					fileName = fileName.replace('@', '?');
					if (this.isFileCaseContained(fileName) == null) {
						 files[i].delete();
						
					} else {
						
						failedNum++;
						totalNum++;
					}
				}
				System.out.println(failedNum);
			}
		}
		return totalNum;
	}
}
