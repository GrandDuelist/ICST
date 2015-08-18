package cn.com.secondary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/** 
 * 复制单个文件 
 * @param oldPath String 原文件路径 如：c:/fqf.txt 
 * @param newPath String 复制后路径 如：f:/fqf.txt 
 * @return boolean 
 */ 
public class FileCopy{
public void copyFile(String oldPath, String newPath) { 
   try { 
       int bytesum = 0; 
       int byteread = 0; 
       File newFile = new File(newPath);
       
       File oldfile = new File(oldPath); 
       if (oldfile.exists()) { //文件存在时 
           InputStream inStream = new FileInputStream(oldPath); //读入原文件 
           if(!newFile.exists()){
        	   newFile.createNewFile();
           }
           FileOutputStream fs = new FileOutputStream(newFile); 
           byte[] buffer = new byte[1444]; 
           int length; 
           while ( (byteread = inStream.read(buffer)) != -1) { 
               bytesum += byteread; //字节数 文件大小 
               System.out.println(bytesum); 
               fs.write(buffer, 0, byteread); 
           } 
           inStream.close(); 
       }else{
    	   System.out.println("不存在");
       }
   } 
   catch (Exception e) { 
       System.out.println("复制单个文件操作出错"); 
       e.printStackTrace(); 

   } 

} }