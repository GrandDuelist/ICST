package cn.com.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TerraceProcess extends FileProcess {

	public List<StringArray>[] terraceFeatures = new List[VERSION_NUM];  
	public static final int FEATURE_NUM = 5;
	public TerraceProcess() {
		
	}
	
	public void run() throws IOException{
		this.readFile();
		this.statisis();
		this.setEveryVersion();
		this.filterPrepro();
		
		
		this.setEveryVersionAfterPrepro();
		this.processTerraceFeatures();
		
		String outputDirname  = FileProcess.DATA_DIR;
		this.showTerraceResult("terrace.txt", outputDirname);
		
		this.check();
	
	}
	
	public StringArray getStringArrayByIndex(int index){
		String version = versions_orig[index];
		for(int i=0;i<this.aftPreproTestCases.size();i++){
			if(version.equals(this.aftPreproTestCases.get(i).getVersionName()))
			{
				return this.aftPreproTestCases.get(i);
			}
		}
		
		return null;
	}
	
	
	public StringArray getOrigStringArrayByIndex(int index){
		String version = versions_orig[index];
		for(int i=0;i<this.befPreproTestCases.size();i++){
			if(version.equals(this.befPreproTestCases.get(i).getVersionName()))
			{
				return this.befPreproTestCases.get(i);
			}
		}
		
		return null;
	}
	
	
	public void setEveryVersionAfterPrepro(){
		for (int i = 0; i < this.duplicateTestCaseEachVersion.length; i++) {
			this.duplicateTestCaseEachVersion[i] = new ArrayList<TestCase>();
		}
		
		
		
		for (int i=0;i<this.duplicateTestCaseEachVersionOrig.length;i++){
			List<TestCase> tmp = this.duplicateTestCaseEachVersionOrig[i];
			for(int j=0;j<tmp.size();j++){
				if(this.isInFileName(tmp.get(j).getTestCaseString())){
					this.duplicateTestCaseEachVersion[i].add(tmp.get(j));
				}
			}
			
		}
	}
	
	public boolean isInFileName(String testCaseName)
	{
		for(int i=0; i<this.aftPreproTestCases.size();i++){
			StringArray temp = this.aftPreproTestCases.get(i);
			for(int j=0;j<temp.filesName.length;j++){
				if(temp.filesName[j].equals(testCaseName)){
					return true;
				}
			}
		}
		
		return false;
	}

	
	
	public void processTerraceFeatures(){
		for(int i=0;i<this.terraceFeatures.length;i++){
			this.terraceFeatures[i] = new ArrayList<StringArray>();
			for(int j=0;j<i;j++){
				StringArray stringArray=new StringArray();
				stringArray.setVersionName(this.versions_orig[i]);
				System.out.println(this.versions_orig[i]);
			 	stringArray.filesName=this.getStringArrayByIndex(i).filesName;
			 	stringArray.setFilesNameOrig(this.getStringArrayByIndex(i).getFilesNameOrig());
				stringArray.caculateTerraceStringArrayFeature(j+1);
				this.terraceFeatures[i].add(0,stringArray);
				
			}
		}
		
	}
	
	
	public void showTerraceResult(String fileName,String dirname) throws IOException{
		File file = new File(dirname+fileName);
		if(!file.exists()){
			file.createNewFile();
		}
		
		BufferedWriter writer  = new BufferedWriter(new FileWriter(file));
		
		//header
		writer.append("version\t");
		for(int i=0;i<this.versions_orig.length;i++){
			writer.append(this.versions_orig[i]+"\t");
		}
		writer.append("\n");
		
		
		//contents
		writer.append("#total orig\t");
		for(int i=0;i<VERSION_NUM;i++){
			StringArray tmp= this.getOrigStringArrayByIndex(i);
			if(tmp!=null){
			writer.append(tmp.getFileNum()+"\t");
			}
		}
		writer.append("\n");
				
		writer.append("#total aft prpro\t");
		for(int i=0;i<VERSION_NUM;i++){
			StringArray tmp= this.getStringArrayByIndex(i);
			writer.append(tmp.getFileNum()+"\t");
		}
		writer.append("\n");
		
		writer.append("#failing\t");
		for(int i=0;i<VERSION_NUM;i++){
			StringArray tmp= this.getStringArrayByIndex(i);
			writer.append(tmp.getFailedTest()+"\t");
		}
		writer.append("\n");
		
		
		
		for(int i=0;i<VERSION_NUM;i++){
			
			writer.append("#old tests\t");
			int j=0;
			for(;j<i+1;j++){
				writer.append(" \t");
			}
			for(;j<VERSION_NUM;j++)
			{
				writer.append(this.terraceFeatures[j].get(i).getOldTest()+"\t");
				
			}
			writer.append("\n");
			
			writer.append("#failed old tests\t");
			j=0;
			for(;j<i+1;j++){
				writer.append(" \t");
			}
			for(;j<VERSION_NUM;j++)
			{
				writer.append(this.terraceFeatures[j].get(i).getFailedOldTest()+"\t");
				
			}
			writer.append("\n");
			
			
			writer.append("#failed and from old tests\t");
			j=0;
			for(;j<i+1;j++){
				writer.append(" \t");
			}
			for(;j<VERSION_NUM;j++)
			{
				writer.append(this.terraceFeatures[j].get(i).getFailedAndOldTest()+"\t");
				
			}
			writer.append("\n");
			
			
			writer.append("#failed and from failed old tests\t");
			j=0;
			for(;j<i+1;j++){
				writer.append(" \t");
			}
			for(;j<VERSION_NUM;j++)
			{
				writer.append(this.terraceFeatures[j].get(i).getFailedAndFailedOldTest()+"\t");
				
			}
			writer.append("\n");
			
			
			writer.append("#failed and from not failed old tests\t");
			j=0;
			for(;j<i+1;j++){
				writer.append(" \t");
			}
			for(;j<VERSION_NUM;j++)
			{
				writer.append(this.terraceFeatures[j].get(i).getFailedOldButNotFailedOldTestList().size()+"\t");
				
			}
			writer.append("\n");
			
		}
	/*	for(int i=0;i<this.terraceFeatures.length;i++){
			List<StringArray> temp = this.terraceFeatures[i];
			for(int j=0;j<temp.size();j++){
				System.out.println();
			}
		}*/
		
		writer.close();
	}
	
	
	public void check()
	{
		System.out.println(this.terraceFeatures[7].get(6).getVersionName());
		for(int i=0;i<this.terraceFeatures[7].get(6).getFailedAndFailedOldTestList().size();i++){
			System.out.println(this.terraceFeatures[7].get(6).getFailedAndFailedOldTestList().get(i).getTestCaseString());
			
		}
		System.out.println(this.getStringArrayByIndex(6).getVersionName());
		for(int i=0;i<this.getStringArrayByIndex(6).getFailedTestList().size();i++){
			System.out.println(this.getStringArrayByIndex(6).getFailedTestList().get(i));
			//System.out.println(this.getStringArrayByIndex(6).getFailedAndFailedOldTest())
			
		}
	}
	

}
