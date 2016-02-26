package cn.com.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cn.com.cluster.handle.HandleFacotry;

public class FileProcess {

	public static int VERSION_NUM = 16;
	/**
	 * the failed and duplicated files names
	 */
	public static final String DATA_DIR = "../../data/";
	public static final String FAILED_FILE = "./failed.txt";
	public static final String DUPLICATED_FILE = "./duplicated.txt";
	public static final String OUTPUT_DIR = DATA_DIR+"result/";
	
	//cluster 的输出
	public static final String OUTPUT_DIR2 = DATA_DIR+"rr_cluster/";

	/**
	 * the files after preprocessing
	 */
	//cluster 的输入
	public static final String DIR_AFT_PRE = DATA_DIR+"rr_lda_input";
	public static final String DIR_BEFORE_PRE = DATA_DIR+"lda_input";

	/**
	 * file test cases names
	 */
	public List<String> failedTestCases = new ArrayList<String>();
	public List<StringArray> aftPreproTestCases = new ArrayList<StringArray>();
	public List<StringArray> befPreproTestCases = new ArrayList<StringArray>();

	//
	public  static List<TestCase>[] duplicateTestCaseEachVersionOrig = new List[VERSION_NUM];
	public  static List<TestCase>[] duplicateTestCaseEachVersion = new List[VERSION_NUM];
	//public List<StringArray>[] terraceFeatures = new List[VERSION_NUM];

	// summaries
	public List<Summary> duplicateSummaries = new ArrayList<Summary>();

	public static String versions[] = { "V20",
			"V30", "V35", "V36", "V36_complete_web", "V40", "V50", "V60",
			"V70", "V80", "V90", "V10", "V11", "V12", "V13", "V14"};
	
	public static String versions_orig[] = {  "litmus_20", "litmus_30", "litmus_35",
			"litmus_36", "litmus_36_complete_WEB", "litmus_40", "litmus_50",
			"litmus_60", "litmus_70", "litmus_80", "litmus_90", "litmus_10", "litmus_11", "litmus_12",
			"litmus_13", "litmus_14"};
	
	
/*	public static String versions[] = {  "V20",
			"V30", "V35", "V36",  "V40", "V50"};
//			, "V60",
//			"V70", "V80", "V90","V10", "V11", "V12", "V13", "V14","V36_complete_web"};
	public static String versions_orig[] = { "litmus_20", "litmus_30", "litmus_35",
			"litmus_36",  "litmus_40", "litmus_50"};
//			"litmus_60", "litmus_70", "litmus_80", "litmus_90","litmus_10", "litmus_11", "litmus_12",
//			"litmus_13", "litmus_14", "litmus_36_complete_WEB"};
*/
	public int versionsAll[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	public int versionsFail[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0 };

	public int fileNumBefPrepro[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0 };
	public int fileNumAfPrepro[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0 };
/*	public int versionsAll[] = { 0, 0, 0, 0, 0, 0};
	public int versionsFail[] = { 0, 0, 0, 0, 0, 0};
	public int fileNumBefPrepro[] = { 0, 0, 0, 0, 0, 0};
	public int fileNumAfPrepro[] = { 0, 0, 0, 0, 0, 0};*/

	public FileProcess() {

	}
	
	public void run() throws IOException
	{
		this.readFile();
		this.statisis();
		this.setEveryVersion();
		// System.out.println(this.duplicateTestCaseEachVersion[10].size());
		// this.showResult();
		// this.showOriginalResult();
		this.filterPrepro();
		
		String outputDirname  = DATA_DIR;
		this.showAllFetureResult(this.befPreproTestCases, "original_data.txt",outputDirname);
		this.showAllFetureResult(this.aftPreproTestCases, "after_preprocessing_data.txt", outputDirname);
	}

//	public void processTerraceFeatures(){
//		for(int i=0;i<this.terraceFeatures.length;i++){
//			this.terraceFeatures[i] = new ArrayList<StringArray>();
//			
//		}
//		
//	}
	/**
	 * output all features into text file
	 * @throws IOException 
	 */
	public void showAllFetureResult(List stringArrays, String fileName,String dirName) throws IOException
	{
		String wholeFileName = dirName+fileName;
		File outputFile = new File(wholeFileName);
		if(!outputFile.exists()){
			outputFile.createNewFile();
		}
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
		
		new StringArray().outputTitle(writer);
		
		for(int i=0;i<stringArrays.size();i++){
			StringArray stringArray = (StringArray)stringArrays.get(i);
		    stringArray.outputFetures(writer);
		}
		
		writer.close();
	}
	
	public class StringArray {
		private String versionName = null;
		public String filesName[];

		private int fileNum = 0; // total file number
		private int oldTest = 0; // files that inherits from previous version
		private List<TestCase> oldTestList = new ArrayList<TestCase>();
		private int failedOldTest = 0; // files that has failed old versions
		private List<TestCase> failedOldTestList = new ArrayList<TestCase>();
		private int failedTest = 0;
		private List<String> failedTestList = new ArrayList<String>();
		private int failedAndOldTest = 0;
		private List<TestCase> failedAndOldTestList = new ArrayList<TestCase>();
		private int failedAndFailedOldTest = 0;
		private List<TestCase> failedAndFailedOldTestList = new ArrayList<TestCase>();
		private List<TestCase> failedOldButNotFailedOldTestList = new ArrayList<TestCase>();
		private String versionFrom = "";
		private String filesNameOrig[]; 
		
		
		
		public String[] getFilesNameOrig() {
			return filesNameOrig;
		}

		public void setFilesNameOrig(String[] filesNameOrig) {
			this.filesNameOrig = filesNameOrig;
		}

		public String getVersionFrom() {
			return versionFrom;
		}

		public void setVersionFrom(String versionFrom) {
			this.versionFrom = versionFrom;
		}

		public List<TestCase> getOldTestList() {
			return oldTestList;
		}

		public void setOldTestList(List<TestCase> oldTestList) {
			this.oldTestList = oldTestList;
		}

		public List<TestCase> getFailedOldTestList() {
			return failedOldTestList;
		}

		public void setFailedOldTestList(List<TestCase> failedOldTestList) {
			this.failedOldTestList = failedOldTestList;
		}

		public List<String> getFailedTestList() {
			return failedTestList;
		}

		public void setFailedTestList(List<String> failedTestList) {
			this.failedTestList = failedTestList;
		}

		public List<TestCase> getFailedAndOldTestList() {
			return failedAndOldTestList;
		}

		public void setFailedAndOldTestList(List<TestCase> failedAndOldTestList) {
			this.failedAndOldTestList = failedAndOldTestList;
		}

		public List<TestCase> getFailedAndFailedOldTestList() {
			return failedAndFailedOldTestList;
		}

		public void setFailedAndFailedOldTestList(
				List<TestCase> failedAndFailedOldTestList) {
			this.failedAndFailedOldTestList = failedAndFailedOldTestList;
		}

		public List<TestCase> getFailedOldButNotFailedOldTestList() {
			return failedOldButNotFailedOldTestList;
		}

		public void setFailedOldButNotFailedOldTestList(
				List<TestCase> failedOldButNotFailedOldTestList) {
			this.failedOldButNotFailedOldTestList = failedOldButNotFailedOldTestList;
		}

		public int getOldTest() {
			return oldTest;
		}

		public void setOldTest(int oldTest) {
			this.oldTest = oldTest;
		}

		public int getFailedOldTest() {
			return failedOldTest;
		}

		public void setFailedOldTest(int failedOldTest) {
			this.failedOldTest = failedOldTest;
		}

		public int getFailedTest() {
			return failedTest;
		}

		public void setFailedTest(int failedTest) {
			this.failedTest = failedTest;
		}

		public int getFailedAndOldTest() {
			return failedAndOldTest;
		}

		public void setFailedAndOldTest(int failedAndOldTest) {
			this.failedAndOldTest = failedAndOldTest;
		}

		public int getFailedAndFailedOldTest() {
			return failedAndFailedOldTest;
		}

		public void setFailedAndFailedOldTest(int failedAndFailedOldTest) {
			this.failedAndFailedOldTest = failedAndFailedOldTest;
		}

		public int getFileNum() {
			return fileNum;
		}

		public void setFileNum(int fileNum) {
			this.fileNum = fileNum;
		}

		public String getVersionName() {
			return versionName;
		}

		public void setVersionName(String versionName) {
			this.versionName = versionName;
		}

		public String[] getFilesName() {
			return filesName;
		}

		public void setFilesName(String[] filesName) {
			this.filesName = filesName;
		}

		public void setFetures() {
			/*this.filesNameOrig=new String[this.filesName.length];
			for(int i=0;i<this.filesNameOrig.length;i++){
				this.filesNameOrig[i]=new String(this.filesName[i]);
			}*/
			// change the file names
			// set the old test numbers
			for (int i = 0; i < this.filesName.length; i++) {
				String tempName = this.filesName[i];
				// preprocess the file name
				if (tempName.contains(".txt")) {
					tempName = tempName.substring(0, tempName.indexOf(".txt"));
				}
				tempName = tempName.replace('@', '?');
				this.filesName[i] = tempName;

				boolean isFromOld = false;
				boolean isFailed = false;

				// set the old test numbers
				TestCase isDup = this.isInDuplicatedOrig(tempName);
				if (isDup != null && !isDup.isFirstTime()) {
					this.oldTest++;
					isFromOld = true;
					this.oldTestList.add(isDup);
				}

				// set the failed test numbers
				if (isInFailedTestCase(tempName)) {
					isFailed = true;
					this.failedTest++;
					this.failedTestList.add(tempName);
				}

				// set the failed and old ones
				if (isFailed && isFromOld) {
					this.failedAndOldTest++;
					this.failedAndOldTestList.add(isDup);
				}

				// set the failed old test
				if (isDup != null && isDup.isHasPreviousFailed()) {
					this.failedOldTest++;
					this.failedOldTestList.add(isDup);
				}

				// set the failed and failed old test
				if (isFromOld && isFailed && isDup.isHasPreviousFailed()) {
					this.failedAndFailedOldTest++;
					this.failedAndFailedOldTestList.add(isDup);
				}

				// set the failed from old but not failed in old version
				if (isFromOld && isFailed && !isDup.isHasPreviousFailed()) {
					this.failedOldButNotFailedOldTestList.add(isDup);
				}
			}
			// System.out.println(this.versionName+"whole test "+this.fileNum+" old test number: "+this.oldTest);
		}

		/**
		 * judge if it is in the
		 * 
		 * @param testcaseName
		 * @return
		 */
		public TestCase isInDuplicated(String testcaseName) {
			int index = getIndexOfOrigVersion(this.versionName);
			// System.out.println(this.versionName);
			// System.out.println(index);
			for (int i = 0; i < duplicateTestCaseEachVersion[index].size(); i++) {
				TestCase temp = duplicateTestCaseEachVersion[index].get(i);
				if (testcaseName.equals(temp.getTestCaseString())) {
					return temp;
				}
			}

			return null;
		}
		
		
		public TestCase isInDuplicatedOrig(String testcaseName) {
			int index = getIndexOfOrigVersion(this.versionName);
			// System.out.println(this.versionName);
			// System.out.println(index);
			for (int i = 0; i < duplicateTestCaseEachVersionOrig[index].size(); i++) {
				TestCase temp = duplicateTestCaseEachVersionOrig[index].get(i);
				if (testcaseName.equals(temp.getTestCaseString())) {
					return temp;
				}
			}

			return null;
		}
		
		/**
		 * calculate the feature of pervious versions
		 * @param previousVersionNum
		 */
		public void caculateTerraceStringArrayFeature(int previousVersionNum){
			//System.out.println("current "+this.getVersionName()+" number " +  previousVersionNum);
			this.versionFrom = FileProcess.versions_orig[HandleFacotry.getVersionClusterHandle().getIndexOfByVersionName(this.versionName,FileProcess.versions_orig)-previousVersionNum];
			for (int i = 0; i < this.filesName.length; i++) {
				String origFileName = new String(this.filesNameOrig[i]);
			//	System.out.println("orig:       "+origFileName);
				String tempName = this.filesName[i];
				// preprocess the file name
			
				if (tempName.contains(".txt")) {
					tempName = tempName.substring(0, tempName.indexOf(".txt"));
				}
				tempName = tempName.replace('@', '?');
				this.filesName[i] = tempName;

				boolean isFromOld = false;
				boolean isFailed = false;

				// set the old test numbers
				TestCase isDup = this.isInDuplicated(tempName);
				if (isDup != null && this.isFromPrevious(isDup,previousVersionNum)) {
					isDup.setFileName(origFileName);
					this.oldTest++;
					isFromOld = true;
					this.oldTestList.add(isDup);
				}

				// set the failed test numbers
				if (isInFailedTestCase(tempName)) {
					isFailed = true;
					this.failedTest++;
					this.failedTestList.add(tempName);
				}

				// set the failed and old ones
				if (isFailed && isFromOld) {
					this.failedAndOldTest++;
					this.failedAndOldTestList.add(isDup);
				}

				
				boolean isHasPreviousFailed =this.hasPreviousVersionFailed(isDup, previousVersionNum);
				// set the failed old test
				if (isDup != null && isHasPreviousFailed) {
					this.failedOldTest++;
					this.failedOldTestList.add(isDup);
				}

				// set the failed and failed old test
				if (/*isFromOld &&*/ isFailed && isHasPreviousFailed) {
					this.failedAndFailedOldTest++;
					this.failedAndFailedOldTestList.add(isDup);
				}

				// set the failed from old but not failed in old version
				if (isFromOld && isFailed && !isHasPreviousFailed) {
					this.failedOldButNotFailedOldTestList.add(isDup);
				}
			}
			
		}

		private boolean hasPreviousVersionFailed(TestCase isDup, int previousVersionNum){
			if(isDup==null){
				return false;
			}
		//	System.out.println("begins");
			int index = getIndexOfVersion(isDup.getVersion());
			for(int i = 1;i <= previousVersionNum;i++ ){
				List<TestCase> previousVesion = duplicateTestCaseEachVersion[index-i];
			//	System.out.println(previousVesion.get(0).getVersion());
				for(int j=0;j<previousVesion.size();j++){
					TestCase tmp = previousVesion.get(j);
					if(tmp.mySummary==isDup.mySummary&&tmp.getStatus().equals("fail")){
						return true;
					}
				}
			}
			return false;
		}
		
		/**
		 * judge if it isDup is from previous version
		 * @param isDup
		 * @param previousVersionNum
		 * @return
		 */
		private boolean isFromPrevious(TestCase isDup, int previousVersionNum) {
			// TODO Auto-generated method stub
			int index = getIndexOfVersion(isDup.getVersion());
			
			for(int i = 1;i <= previousVersionNum;i++ ){
				List<TestCase> previousVesion = duplicateTestCaseEachVersion[index-i];
				for(int j=0;j<previousVesion.size();j++){
					TestCase tmp = previousVesion.get(j);
					if(tmp.mySummary==isDup.mySummary){
						
						return true;
					}
				}
			}
			return false;
		}

		public void outputTitle(BufferedWriter writer) throws IOException {
			writer.append("version\t" 
					+ "#total test cases\t"
					+ "#has old tests\t" 
					+ "#has failed old tests\t"
					+ "#is failed tests\t" 
					+ "#is failed and has old tests\t"
					+ "#is failed and has old failed tests\t"
					+ "#is failed and has old but not failed tests\n");
		}

		// output the result into a file
		public void outputFetures(BufferedWriter writer) throws IOException {
			writer.append(this.versionName + "\t" 
					+ this.fileNum + "\t"
					+ this.oldTestList.size() + "\t" 
					+ this.failedOldTest+ "\t" 
					+ this.failedTest + "\t" 
					+ this.failedAndOldTest+ "\t" 
					+ this.failedAndFailedOldTest + "\t"
					+ this.failedOldButNotFailedOldTestList.size() + "\n");
		}

	}

	/**
	 * put each test case of duplicated into every version
	 */
	public void setEveryVersion() {
		for (int i = 0; i < this.duplicateTestCaseEachVersionOrig.length; i++) {
			this.duplicateTestCaseEachVersionOrig[i] = new ArrayList<TestCase>();
		}

		
		for (int i = 0; i < this.duplicateSummaries.size(); i++) {
			Summary summary = this.duplicateSummaries.get(i);
			boolean firstTestCase = true;
			for (int j = 0; j < summary.getTestCases().size(); j++) {
				TestCase testcase = summary.getTestCases().get(j);
				testcase.setFirstTime(firstTestCase);
				int index = this.getIndexOfVersion(testcase.getVersion());
				this.duplicateTestCaseEachVersionOrig[index].add(testcase);

				firstTestCase = false;
			}
		}
		
		
	
	}

	/**
	 * filter the files after pre-processing
	 */
	public void filterPrepro() {
		File dirBefPrepro = new File(DIR_BEFORE_PRE);
		File dirAftPrepro = new File(DIR_AFT_PRE);

		// get the versions
		File subDirsBefPrepro[] = dirBefPrepro.listFiles();
		File subDirsAftPrepro[] = dirAftPrepro.listFiles();

		// set list of version
		this.setFileNameList(subDirsBefPrepro, subDirsAftPrepro);

	}

	/**
	 * set list of original and preprocessing data
	 * 
	 * @param subDirsBefPrepro
	 * @param subDirsAftPrepro
	 */
	public void setFileNameList(File subDirsBefPrepro[],
			File subDirsAftPrepro[]) {
		// process each version orginal data
		for (int i = 0; i < subDirsBefPrepro.length; i++) {
			String versionName = subDirsBefPrepro[i].getName();
			int index = this.indexOfDirName(versionName);

			StringArray current = new StringArray();
			current.setVersionName(versionName);
			current.setFileNum(subDirsBefPrepro[i].list().length);
			current.setFilesName(subDirsBefPrepro[i].list());
			current.setFilesNameOrig(subDirsBefPrepro[i].list());
			this.fileNumBefPrepro[index] = current.getFileNum();
			current.setFetures();
			this.befPreproTestCases.add(current);
		}

		// process each version preprocessing data
		for (int i = 0; i < subDirsAftPrepro.length; i++) {
			String versionName = subDirsAftPrepro[i].getName();
			int index = this.indexOfDirName(versionName);

			StringArray current = new StringArray();
			current.setVersionName(versionName);
			current.setFileNum(subDirsAftPrepro[i].list().length);
			current.setFilesName(subDirsAftPrepro[i].list());
			current.setFilesNameOrig(subDirsAftPrepro[i].list());
			current.setFetures();
			this.fileNumAfPrepro[index] = current.getFileNum();

			this.aftPreproTestCases.add(current);
		}
	}

	/**
	 * get the index of the original version names
	 * 
	 * @param dirName
	 * @return
	 */
	public int indexOfDirName(String dirName) {
		for (int i = 0; i < this.versions_orig.length; i++) {
			if (this.versions_orig[i].equals(dirName)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * read data from disk file
	 * 
	 * @throws IOException
	 */
	public void readFile() throws IOException {
		File failedFile = new File(FAILED_FILE);
		File duplicatedFile = new File(DUPLICATED_FILE);
		this.buildMatrix(duplicatedFile, failedFile);

	}

	/**
	 * get the number of all and failed of each version
	 */
	public void statisis() {
		for (int i = 0; i < this.duplicateSummaries.size(); i++) {
			Summary summary = this.duplicateSummaries.get(i);
			boolean previousFail = false;
			for (int j = 0; j < summary.getTestCases().size(); j++) {
				TestCase testcase = summary.getTestCases().get(j);
				// System.out.println(summary.getSummaryString()+"\t"+testcase.getTestCaseString()+"\t"+testcase.getVersion()+"\t"+testcase.getStatus());
				this.versionsAll[this.getIndexOfVersion(testcase.getVersion())]++;
				if (previousFail) {
					testcase.setHasPreviousFailed(true);
					this.versionsFail[this.getIndexOfVersion(testcase
							.getVersion())]++;
				}
				if (testcase.getStatus().equals("fail")) {
					previousFail = true;
				}
			}

		}
	}

	public void showResult() throws IOException {

		String sumFileName = OUTPUT_DIR + "duplicated_statistics.txt";
		File file = new File(sumFileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		BufferedWriter sumWriter = new BufferedWriter(new FileWriter(file));
		for (int i = 0; i < this.versions.length; i++) {
			// System.out.print(this.versions[i]+"\t");
			sumWriter.append(this.versions[i] + "\t");
		}
		// System.out.println();
		sumWriter.append("\n");
		for (int i = 0; i < this.versions.length; i++) {
			// System.out.print(this.versionsAll[i]+"\t");
			sumWriter.append(this.versionsAll[i] + "\t");
		}
		// System.out.println();
		sumWriter.append("\n");
		for (int i = 0; i < this.versions.length; i++) {
			sumWriter.append(this.versionsFail[i] + "\t");
			// System.out.print(this.versionsFail[i]+"\t");
		}
		sumWriter.append("\n");
		// System.out.println();
		sumWriter.close();
	}

	public void showOriginalResult() throws IOException {
		String fileName = OUTPUT_DIR + "duplicated_raw_data.txt";
		File file = new File(fileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));

		for (int i = 0; i < this.duplicateSummaries.size(); i++) {
			Summary summary = this.duplicateSummaries.get(i);
			for (int j = 0; j < summary.getTestCases().size(); j++) {
				TestCase testcase = summary.getTestCases().get(j);
				writer.append(summary.getSummaryString() + "\t"
						+ testcase.getTestCaseString() + "\t"
						+ testcase.getVersion() + "\t" + testcase.getStatus()
						+ "\n");
			}
		}
	}

	/**
	 * build the matrix of each test case by list
	 * 
	 * @param duplicatedFile
	 * @param failedFile
	 * @throws IOException
	 */
	public void buildMatrix(File duplicatedFile, File failedFile)
			throws IOException {
		BufferedReader duplicatedReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(duplicatedFile)));
		BufferedReader failedReader = new BufferedReader(new InputStreamReader(
				new FileInputStream(failedFile)));

		// process the failed file and construct a list that contain the failed
		// test cases
		String line = null;
		while ((line = failedReader.readLine()) != null) {
			failedTestCases.add(line);
		}

		while ((line = duplicatedReader.readLine()) != null) {
			this.processPefLine(line);
		}

	}

	/**
	 * process per line of duplicated test case file
	 * 
	 * @param line
	 */
	public void processPefLine(String line) {
		// TODO Auto-generated method stub
		String lines[] = line.split("\t");
		String summary = lines[0];
		String testCaseString = lines[1];
		String version = lines[2];

		Summary currentSummary = this.getExistSummary(summary);
		if (currentSummary == null) {
			currentSummary = new Summary();
			currentSummary.setSummaryString(summary);
			this.duplicateSummaries.add(currentSummary);
		}

		TestCase testcase = new TestCase();
		testcase.setTestCaseString(testCaseString);
		testcase.setVersion(version);

		if (this.isInFailedTestCase(testCaseString)) {
			testcase.setStatus("fail");
		} else {
			testcase.setStatus("pass");
		}

		int index = this.getIndexOfTestCase(version, currentSummary);
		testcase.mySummary=currentSummary;
		currentSummary.getTestCases().add(index, testcase);

	}

	public int getIndexOfTestCase(String version, Summary summary) {
		for (int i = 0; i < summary.getTestCases().size(); i++) {
			TestCase tmp = summary.getTestCases().get(i);
			if (this.getIndexOfVersion(version) < this.getIndexOfVersion(tmp
					.getVersion())) {
				return i;
			}
		}
		return summary.getTestCases().size();
	}

	public int getIndexOfVersion(String version) {
		for (int i = 0; i < versions.length; i++) {
			if (version.equals(versions[i])) {
				return i;
			}
		}

		return -1;
	}

	public int getIndexOfOrigVersion(String version) {
		for (int i = 0; i < versions_orig.length; i++) {
			if (version.equals(versions_orig[i])) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * check and get the summary if it exist in the list
	 * 
	 * @param summaryString
	 * @return
	 */
	public Summary getExistSummary(String summaryString) {
		for (int i = 0; i < this.duplicateSummaries.size(); i++) {
			Summary temp = this.duplicateSummaries.get(i);
			if (summaryString.equals(temp.getSummaryString())) {
				return temp;
			}
		}
		return null;
	}

	/**
	 * remove the space of the end of the string
	 * 
	 * @param s
	 * @return
	 */
	public String removeEndSpace(String s) {
		int i = 0;
		for (i = s.length() - 1; i > 0; i--) {
			char temp = s.charAt(i);
			if (temp != ' ' && temp != '\t') {
				break;
			}
		}

		s = s.substring(0, i + 1);
		return s;
	}

	/**
	 * judge if the test case is a failed one
	 * 
	 * @param testcase
	 * @return
	 */
	public boolean isInFailedTestCase(String testcase) {
		for (int i = 0; i < this.failedTestCases.size(); i++) {
			String temp = this.failedTestCases.get(i);
			if (temp.equals(testcase)) {
				return true;
			}
		}
		return false;
	}
}
