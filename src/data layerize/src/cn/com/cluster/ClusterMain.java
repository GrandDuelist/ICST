package cn.com.cluster;

import java.io.IOException;

public class ClusterMain {
	public static void main(String []args){
		
		
		ClusterProcess clusterProcess = new ClusterProcess();
		
		
		try {
			clusterProcess.run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
