package mydata;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import utils.Constants;

public class MyLog {
	
	//将所用时间记录到文件中
	public void timelog(String s,int usetime){
	//	FileWriter fw=null;
		BufferedWriter out=null;
		try{
		//	fw = new FileWriter(Constants.dirname+"intime.txt",true);
			out=new BufferedWriter(new FileWriter(Constants.dirname+"intime.txt",true));
			out.write(s+usetime+"\r\n");//记录每次导入花费的时间
		}catch (IOException e) {
			e.printStackTrace();
		} catch(Exception e ){
			e.printStackTrace();
		}
		//System.out.println("Succeed!");
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void timelog(String table,String s,int usetime){
	//	FileWriter fw=null;
		BufferedWriter out=null;
		try{
		//	fw = new FileWriter(Constants.dirname+"intime.txt",true);
			out=new BufferedWriter(new FileWriter(Constants.dirname+"splited_"+table+"//"+table+"_log.txt",true));
			out.write(s+usetime+"\r\n");//记录每次导入花费的时间
		}catch (IOException e) {
			e.printStackTrace();
		} catch(Exception e ){
			e.printStackTrace();
		}
		//System.out.println("Succeed!");
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
/*	public static void main(String[] args){
		int i = 70231;
		String s = "nihao";
			try{		
				MyLog ml = new MyLog();
				ml.timelog(s, i);
			}catch(Exception e ){
				e.printStackTrace();
			}
	}
	*/

}
