package mydata;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utils.Constants;


import entity.Follower;

class t{
	
}
public class Test extends t {

//	/**
//	* 判断一个字符是Ascill字符还是其它字符（如汉，日，韩文字符）
//* 
//	* @param c
//	* @return
//	*/
//	public static boolean isLetter(char c) {
//	int k = 0x80;
//	return (c / k) == 0 ? true : false;
//	}
//    
//	public void douhaofenli(String s){
//		String[] infos = s.split(","); 
//		System.out.println("原始的按逗号分离的结果： ");
//		for(int i = 0;i<infos.length;i++){
//			System.out.println(i+" is : "+infos[i]);
//			for(int j=0;j<infos[i].length();j++){
//				if(isLetter(infos[i].charAt(j))== false){
//					infos[i]+="\"";
//					break;
//				}
//			}
//		}
//		System.out.println();
//     	String[] new_infos = new String[10];//用来存储正确分离的字段
//     	
//     	int length = infos.length;
//     	int temp = 0;
//     	int flag = 0;
//     	System.out.println("length is ："+length);
//     	try{
//     		if(length >3){
//     			
//         		for(int i=0;i<length;i++){//J是真正字段的下标索引
//         			System.out.print("infos["+i+"] is :"+infos[i]+" 。");
//         			System.out.print("此时temp 是 "+temp+"; flag is "+flag+" ; i is "+i+" ; ");
//					System.out.println("new_infos["+temp+"] 是 "+new_infos[temp]);
//         			int lo = infos[i].length();
//         			
//         			
//         			//包含引号
//         			if(infos[i].contains("\"")){//包含引号
//         				//长度为1，即只有一个引号（可能是前引号，也可能是后引号）
//         				if(infos[i].length()<2){
//         					if(flag == 0){//flag = 0,表示为前引号
//         						flag = 1;
//         						infos[temp] = null;//去掉引号
//         					}
//         					if(flag == 1){
//         						flag = 0;
//         						temp++;
//         					}
//         				}
//         				//长度大于等于2
//         				else{
//         					String first = infos[i].substring(0, 1);
//         				//	System.out.println("the first is ： "+first); 
//         					//System.out.println(lo +" "+lo-1 + lo-2);
//         					System.out.print("lo is "+lo+"; ");
//         					String last = infos[i].substring(lo-1, lo);
//         					//第一个元素为引号
//         					if(first.compareTo("\"") == 0){
//         					//	System.out.println("第一个元素为引号");
//         						flag = 1;
//         						//temp ;
//         						//第一个元素，最后一个元素都引号
//         				
//         						if(last.compareTo("\"") == 0){
//         							System.out.print("the first is ： "+first+"; ");
//         							System.out.print(" ， the last is ： "+last+"; "); 
//         							System.out.println(" ， 第一个元素为引号，且最后一个元素为引号"); 
//         							new_infos[temp] = infos[i].substring(1,lo-1);
//         							System.out.println("new_infos[temp] 是 "+new_infos[temp]);
//         						//	infos[temp] = infos[i].substring(1,lo-1);
//         							temp++;
//         							flag = 0;
//         						}
//         						//仅第一个元素为引号
//         						else{
//         							System.out.print("the first is ： "+first+"; ");
//         							System.out.print(" ， the last is ： "+last+"; "); 
//         							System.out.println(" ，第一个元素为引号,且最后一个元素不是引号");
//         							flag = 1;
//         							//infos[temp] = infos[i].substring(1);
//         							new_infos[temp] = infos[i].substring(1)+",";
//         							
//         							
//         						}
//         					}
//         					//第一个元素不是引号，最后一个元素是引号
//         					else if(last.compareTo("\"") == 0){
//         						System.out.print("the first is ： "+first+"; ");
//         						System.out.print(" ， the last is ： "+last+"; "); 
//     							System.out.println(" ， 第一个元素不为引号，且最后一个元素为引号,flag is"+flag); 
//         						if(flag == 1){
//         							
//         							//infos[temp] += infos[i].substring(0,lo-1);
//         							new_infos[temp] += infos[i].substring(0,lo-1);
//         							
//         						}else if(flag == 0){
//         							System.out.println("Something is wrong!");
//         							//continue;
//         						}
//         						flag = 0;
//         						System.out.println("new_infos[temp] 是 "+new_infos[temp]);
//         						temp++;
//         					}
//         				//其他地方元素为引号，不讨论
//         				}
//         			}
//         			//不包含引号
//     					//可能是引号中间的元素
//         			else if(flag == 1){
//         				//有前引号
//         			//	infos[temp] += infos[i];
//         				new_infos[temp] += infos[i];
//         				new_infos[temp] += ",";
//         			}
//         			else if(flag == 0){
//         			//	infos[temp] = infos[i];
//         				System.out.println("可能是数字");
//         				new_infos[temp] = infos[i];
//         				temp++;
//         			}
//     					//可能是数字
//         			
//         		}
//     		}
//     	for(int i = 0;i<10;i++){
//     		System.out.println("new_infos[i] 是 "+new_infos[i]);
//     	}
//     	}catch(Exception e){
//     		e.printStackTrace();
//     	}
//     	
//     	
//	}
//	public String[] fenli(String s){
//		String[] infos = s.split(",");
//		for(int i=0;i<infos.length;i++){
//			System.out.println(i+" : "+infos[i]);
//		}
//		String[] new_infos = new String[9];//用来存储正确分离的字段
//		int length = infos.length;
//		System.out.println("length : "+length);
//		if(length == 9){
//     		//去掉引号
//     		
//     		infos[1] = infos[1].substring(1,infos[1].length()-1);
//     		infos[3] = infos[3].substring(1,infos[3].length()-1);
//     		
//     		if(infos[4].charAt(infos[4].length()-1)=='"'){
//     			infos[4] = infos[4].substring(1,infos[4].length()-1);
//     		}
//     		else{
//     			infos[4] = infos[4].substring(1);
//     		}
//     		infos[5] = infos[5].substring(1,infos[5].length()-1);
//     		infos[6] = infos[6].substring(1,infos[6].length()-1);
//     		for(int i = 0;i<infos.length;i++){
//	     		System.out.print("infos["+i+"] 是 "+infos[i]+" ; ");
//	     	}
//     		return infos;
//     	}
//		else{
//			for(int i=0;i<4;i++){
//				System.out.println(i+" : "+infos[i]);
//				new_infos[i] = infos[i];
//				System.out.println((8-i)+" : "+infos[length-1-i]);
//				new_infos[8-i] = infos[length-1-i];
//			}
//			for(int i=4;i<length-4;i++){
//				if(i==4)
//					new_infos[4] = infos[i];
//				else{
//					new_infos[4] += infos[i];
//				}
//				
//				if(i<length-1-4){
//					new_infos[4] += ",";
//				}
//				System.out.println( 4 +" : "+new_infos[4]);
//			}
//			new_infos[1] = new_infos[1].substring(1,infos[1].length()-1);
//			new_infos[3] = new_infos[3].substring(1,infos[3].length()-1);
//     		
//     		if(new_infos[4].charAt(new_infos[4].length()-1)=='"'){
//     			new_infos[4] = new_infos[4].substring(1,new_infos[4].length()-1);
//     		}
//     		else{
//     			new_infos[4] = new_infos[4].substring(1);
//     		}
//     		new_infos[5] = new_infos[5].substring(1,new_infos[5].length()-1);
//     		new_infos[6] = new_infos[6].substring(1,new_infos[6].length()-1);
//			return new_infos;
//		}
//		
//	}
	public static void main(String[] args) {
		t tt = new Test();
		
		String test="ABC34cccddee";
	    System.out.println(test.toUpperCase());//小写转大写
	 
	  // String test="ABC34cccddee";
	    System.out.println(test.toLowerCase());//小写转大写
	}

}
