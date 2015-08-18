package cn.com.main;

import java.io.IOException;

public class ExcractDuplicated {
	
	public static void main(String args[])
	{
		try {
			//new FileProcess();
			new TerraceProcess().run();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
