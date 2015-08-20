package cn.com.main;

import java.io.File;

public class EmptyClean {
		
	public static String FILE_PATH = "../../data/cluster/";
	
		public EmptyClean(){
			
		}
		
		public void run(){
			this.removeEmptyFoulder();
			
			
		}
		
		public void removeEmptyFoulder(){
			File file  = new File(FILE_PATH);
			File versions[] = file.listFiles();
			for(int i=0;i<versions.length;i++){
				File current = versions[i];
				System.out.println(current.getName());
				if(current.listFiles().length<1){
					current.delete();
				}else
				{
					File clusters[] = current.listFiles();
					for(int j=0;j<clusters.length;j++){
						File currentCluster = clusters[j];
						
						if(currentCluster.listFiles().length<1){
							currentCluster.delete();
						}
					}
				}
			}
		}
		
		
		
}
