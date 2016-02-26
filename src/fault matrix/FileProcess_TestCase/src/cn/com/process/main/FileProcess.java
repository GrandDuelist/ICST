package cn.com.process.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;




public class FileProcess {
	
	public static String steps=  "steps_to_perform";
	public static String results="expected_results";
	//public static final String PATH = "/home/ogre/Downloads/litmus70/expected_result/";
	//public static final String PATH = "/home/ogre/Downloads/litmus70/steps_to_perform/";
	public static final String PATH = "../../../data/lda_input_steps_to_perform/";
//	public static  String DATA_DIR="../../../data/lda_input_steps_to_perform/";
//	public static  String OUTPUT_PATH="../../../data/fault_matrix/"+steps+"/";
	public static  String DATA_DIR="../../../data/rr_lda_input";
	public static  String OUTPUT_PATH="../../../data/rr_fault_matrix/"+steps+"/";
	public static void main(String args[])
	{
		String filePath="./test_case.txt";
		
		Process p = new Process();
		p.readFile(filePath);
		
	/*	try {
			p.getBinaryMatrix(versionName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		try {
			p.getBinaryMatrixWithoutDash(DATA_DIR);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		
	}
	
	
	
	
}
