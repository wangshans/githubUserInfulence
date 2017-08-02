package prePro;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class watchersTime {

	/**
	 * @param args
	 */
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public String tablePath = "D:\\github\\mysql-2015-09-25\\dump\\";
	public BufferedReader readFile(String filePath){
		System.out.println("read file from "+filePath);
		try{
			
			BufferedInputStream fis = new BufferedInputStream(new FileInputStream(filePath));
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"utf-8"),5*1024*1024);
			return reader;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public BufferedReader readFile(File filePath){
		System.out.println("read file from "+filePath);
		try{
			
			BufferedInputStream fis = new BufferedInputStream(new FileInputStream(filePath));
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"utf-8"),5*1024*1024);
			return reader;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public void writeProjects() throws IOException, ParseException{
	//	String file = "projects.txt";
		String line="";
		ArrayList<String> lineList = new ArrayList<String>();
		ArrayList<BufferedWriter> outs = new ArrayList<BufferedWriter>();
		for(int i=0;i<5;i++){
			BufferedWriter out = new BufferedWriter(new FileWriter("projects"+i+".txt"));
			outs.add(out);
		}
		
	//	Date baseTime = df.parse("");
		for(int i=0;i<500;i++){
			String file = "D:\\github\\mysql-2015-09-25\\splited_projects\\text"+i+".txt";
			int n = i/100;
			
			BufferedReader reader = readFile(file);
			while((line=reader.readLine())!=null){
			//	System.out.println(line);
				String[] words=line.split(",");
				outs.get(n).write(words[0]+","+words[2]+","+words[words.length-3]+"\n");
			}
		
		}
		for(int i=0;i<5;i++){
			outs.get(i).close();
		}

	}
	public void splitWatchersByYear() throws Exception{
		String line="";
		ArrayList<String> lineList = new ArrayList<String>();
		try{
			ArrayList<BufferedWriter> outs = new ArrayList<BufferedWriter>();
			for(int i=2007;i<2016;i++){
				BufferedWriter out = new BufferedWriter(new FileWriter("watchersByYear"+i+".txt"));
				outs.add(out);
			}
			
			String file = tablePath+"watchers.csv";
		
			BufferedReader reader = readFile(file);
		
			while((line=reader.readLine())!=null){
				if(line==null)
					continue;
				String[] words = line.split(",");
				String createdAt = words[2];
				createdAt = createdAt.substring(1,createdAt.length()-1);//去除引号
				String [] cs = createdAt.split("-");
				int year = Integer.valueOf(cs[0]);
				outs.get(year-2007).write(words[0]+","+words[1]+","+createdAt+"\n");
			}
				
			
			for(int i=2007;i<2016;i++){
				outs.get(i-2007).close();
			}
			System.out.println("years splited ");
		}catch(Exception e){
			System.out.println("file"+line);
			e.printStackTrace();
		}
	}
	
	public void SpitWatchers() throws Exception{
		String line="";
		
		for(int i=2015;i<2016;i++){
			ArrayList<String> lineList = new ArrayList<String>();
			String file = "watchersByYear"+i+".txt";
			BufferedReader reader = readFile(file);
			ArrayList<BufferedWriter> outs = new ArrayList<BufferedWriter>();
			for(int month=1;month<13;month++){//1-12月份
				BufferedWriter out = new BufferedWriter(
						new FileWriter("Data\\spliWatchersByTime\\watchers"+i+"-"+month+".txt"));
				outs.add(out);
			}
			while((line=reader.readLine())!=null){
				String[] words = line.split(",");
				String createdAt = words[2];
				createdAt = createdAt.substring(1,createdAt.length()-1);//去除引号
				String [] cs = createdAt.split("-");
				int year = Integer.valueOf(cs[0]);
				int month = Integer.valueOf(cs[1]);
				outs.get(month-1).write(line+"\n");
			}
			for(int month=0;month<12;month++){//1-12月份
				outs.get(month).close();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		watchersTime w = new watchersTime();
		//w.splitWatchersByYear();
		w.SpitWatchers();
	}

}
