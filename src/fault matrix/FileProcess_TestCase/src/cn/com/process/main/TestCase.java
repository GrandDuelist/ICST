package cn.com.process.main;

import java.util.ArrayList;
import java.util.List;

public class TestCase {
	
	private String textCaseName;
	private List<String> bugs=new ArrayList<String>();
	private List<Boolean> bugsBinary=new ArrayList<Boolean>();
	private String status ="";

	
	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public List<Boolean> getBugsBinary() {
		return bugsBinary;
	}


	public void setBugsBinary(List<Boolean> bugsBinary) {
		this.bugsBinary = bugsBinary;
	}


	public String getTextCaseName() {
		return textCaseName;
	}


	public void setTextCaseName(String textCaseName) {
		this.textCaseName = textCaseName;
	}


	public List<String> getBugs() {
		return bugs;
	}


	public void setBugs(List<String> bugs) {
		this.bugs = bugs;
	}


	boolean isBugContained(String bugName)
	{
		for(int i=0;i<bugs.size();i++)
		{
			String bug=bugs.get(i);
			if(bug.equals(bugName))
			{
				return true;
			}
		}
		
		return false;
	}

}
