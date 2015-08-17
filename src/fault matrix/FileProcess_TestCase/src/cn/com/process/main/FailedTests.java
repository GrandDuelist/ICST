package cn.com.process.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FailedTests {
	public static String steps=  "steps_to_perform";
	public static String results="expected_results";
	//public static final String PATH = "/home/ogre/Downloads/litmus70/expected_result/";
	public static final String PATH = "/home/ogre/Downloads/litmus70/steps_to_perform/";
	public static final String DATA_DIR="/home/ogre/Desktop/web_lscp/data/lda_input_"+steps+"/";
	public static final String OUTPUT_PATH="/home/ogre/Desktop/web_lscp/data/fault_matrix/"+steps+"/";
			;
	public static void main(String args[])
	{
		String filePath="/home/ogre/Downloads/test_case.txt";
		String versionName=PATH+"order_name.txt";
		
		Process p = new Process();
		p.readFile(filePath);
		try {
			outputFailed(p);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public static void outputFailed(Process p) throws IOException{
		File file = new File("failed.txt");
		if(!file.exists()){
			file.createNewFile();
		}
		BufferedWriter outputBuffer = new BufferedWriter(new FileWriter(file));
		for(int i=0;i<p.testCases.size();i++){
			outputBuffer.write(p.testCases.get(i).getTextCaseName()+"\n");
		}
		outputBuffer.close();
	}

	
}
