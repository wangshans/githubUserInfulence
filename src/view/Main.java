package view;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import utils.Constants;

import mydata.MyLog;
//import mydata.ReadtoDatabase;
import mydata.TxttoDB;
//import analysisTables.RunGetData;

public class Main {
	public static final String text0 = "D:\\mysql-2015-09-25\\my\\text0.txt";

	static int num = 6;//已经导入的：0,1,2,3,4，6
	public static String temp_text ;//当前正在插入文件
	
	//= Constants.dirname + num+".txt";
	public static void main(String[] args){
		try{		
			long start = System.currentTimeMillis();//记录开始时间
			MyLog ml = new MyLog();
		//	System.out.println("The start time is "+ start);
		//	System.out.println(temp_text);
			
			//方法一：插入数据库
		/*	ReadtoDatabase instance = new ReadtoDatabase();
			instance.readBigFile(temp_text);
			instance.outtodb2();
		*/
			
			//方法二：批量插入数据库
		/*	TxttoDB tb = new TxttoDB();
			tb.DbStoreHelper();
			tb.storeToDb(temp_text);
		*/	
			//循环插入
			int usetime2;
			long start2;
			long end2;
			String table = "watchers";
			for(int i = 286;i<287;i++){
					start2 = System.currentTimeMillis();
					
					temp_text = Constants.dirname + "splited_" + table+"\\text" + i +".txt";
				//	System.out.println(temp_text);
					TxttoDB tb = new TxttoDB();
					tb.DbStoreHelper(table);
					tb.doStoredb(temp_text,table);
					end2 = System.currentTimeMillis();
					usetime2 = (int) ((end2-start2)/1000);
					System.out.println("插入文件  "+i+" 计算用时="+usetime2);
					ml.timelog(table,"插入文件  "+temp_text+" 所用时间为：", usetime2);//记录所用时间到txt文件
				
			}
			
		//	
			
			//记录操作所用时间
			long end = System.currentTimeMillis();
			int usetime = (int) ((end-start)/1000);
			System.out.println("计算用时="+usetime);
			ml.timelog(table,"插入文件所用总时间为：", usetime);
		}catch(Exception e ){
			e.printStackTrace();
		}
	}
}
