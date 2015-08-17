package cn.com.result.extract;

import java.io.File;

import cn.com.process.main.Process;

public class ExtractResult {
	
	public static final String PATH = "/home/ogre/Desktop/web_lscp/data/lda_input_steps_to_perform/";
	
	public static void main(String args[])
	{
		String filePath="/home/ogre/Downloads/testCase.txt";		
		Process p = new Process();
		p.readFile(filePath);
		p.keepFaildTestCase(PATH);
	}
	
	
	
	
	public void readFile()
	{
		File file = new File(PATH);
		
		if(file.isDirectory()){
			File files[] = file.listFiles();
			for(int i=0; i<files.length;i++)
			{
				File temp = files[i];
				
			}
			
		}
		
	}

}
