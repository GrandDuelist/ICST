package cn.com.result.extract;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UniqueWords {
	
	public static void main(String args[]) throws IOException
	{
		Map <String,Integer> words =  new HashMap<String,Integer>(); 
		
		String PATH = "/home/ogre/Desktop/web_lscp/data/lda_input_steps_to_perform/";
		
		File dirFile = new File(PATH);
		
		File versionFiles[] = dirFile.listFiles();
		
		//process every version
		for(int i=0;i<versionFiles.length;i++){
			File versionFile = versionFiles[i];
			File files[] = versionFile.listFiles();
			words.clear();
			//process every file
			for(int j=0;j<files.length;j++){
				File file = files[j];
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
				String  line = reader.readLine();
				String[] s = line.split(" ");
				for(int k=0;k<s.length;k++){
					String key = s[k];
				
					if(!words.containsKey(key)){
						words.put(key, 1);
					}else{
						int value = words.get(key).intValue();
						value ++;
						words.put(key,value);
					}
				}
				reader.close();
				
			}
			
//			
//			Iterator iter = words.entrySet().iterator();
//			while (iter.hasNext()) {
//			    Map.Entry entry = (Map.Entry) iter.next();
//			    Object key = entry.getKey();
//			    Object val = entry.getValue();
//			    System.out.println(key+" "+val);
//			} 
			System.out.println(versionFile.getName()+" : "+words.size());
		}
		
	}

}
