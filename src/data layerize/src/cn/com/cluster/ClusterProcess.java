package cn.com.cluster;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.com.cluster.bean.TCCluster;
import cn.com.cluster.bean.VersionBean;
import cn.com.cluster.bean.VersionCluster;
import cn.com.cluster.handle.HandleFacotry;
import cn.com.main.FileProcess;
import cn.com.main.TerraceProcess;
import cn.com.main.TestCase;

public class ClusterProcess extends TerraceProcess {
	
	List<VersionBean> versionBeans;
	String copyInputDir = FileProcess.DIR_AFT_PRE+"/";
	String copyClusterOutputDir = FileProcess.OUTPUT_DIR2;
	public void run() throws IOException{
		this.getTerraceFeatures();
		this.generateVersionClusters();
	}
	
	
	/**
	 * get the terrace features and then cluster them based on the features
	 * @throws IOException
	 */
	public void getTerraceFeatures() throws IOException{
		this.readFile();
		this.statisis();
		this.setEveryVersion();
		this.filterPrepro();
		this.setEveryVersionAfterPrepro();
		this.processTerraceFeatures();
	}
	
	
	
	public void generateVersionClusters() throws IOException
	{
		this.versionBeans=HandleFacotry.getVersionHandle().getAllTestCaseBeans(FileProcess.DIR_AFT_PRE, FileProcess.FAILED_FILE, this.terraceFeatures,FileProcess.versions_orig);
		//HandleFacotry.getVersionHandle().printVersion(versionBeans.get(5));
		this.copyAllVersions(this.copyInputDir,this.copyClusterOutputDir, versionBeans);
	}
	
	public void copyAllVersions(String inputDir,String outputDir, List<VersionBean> versions) throws IOException{
		for(int i=0;i<versions.size();i++){
			HandleFacotry.getVersionHandle().copyVersion(versions.get(i),inputDir,outputDir);
		}
	}
	
	
	
	
	
	
	
	//for(int i=0;i<this.terraceFeatures.length;i++){
	//List<StringArray> terraceFeature = this.terraceFeatures[8];	

		    
/*	List<TCCluster> tcclusters = HandleFacotry.getTCClusterHandle().getFailTCClusterForOneVersion(terraceFeature);
	List<TCCluster> passes =  HandleFacotry.getTCClusterHandle().getPassTCClusterForOneVersion(terraceFeature, tcclusters);
	//HandleFacotry.getTCClusterHandle().printListOfClusters(tcclusters);    
	HandleFacotry.getTCClusterHandle().printListOfClusters(passes);    */
		    
			/*StringArray tmp = terraceFeature.get(7);
			StringArray tmp2 = terraceFeature.get(6);
			System.out.println("version "+tmp.getVersionName());
			System.out.println("testcase ");*/
			
			//TCCluster result = HandleFacotry.getTCClusterHandle().getPassTCClusterBasedOnTwoStringArray(tmp2, tmp);
			//HandleFacotry.getTCClusterHandle().printTCCluster(result);
			
			
		//	List<TestCase> result = HandleFacotry.getTestCaseHandle().removeTestCase(tmp2.getOldTestList(), tmp.getOldTestList());
		//	HandleFacotry.getTestCaseHandle().printTestCaseList(result);
		
	//}
	
	
}
