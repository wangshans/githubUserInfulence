package mydata;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import utils.Constants;

public class SplitFile {

//	public static final String filename="D:\\mysql-2015-09-25\\dump\\followers.txt";
	public static final String dirname="E:\\mysql-2015-09-25\\";
	static TreeMap<String,Long> tableLines = new TreeMap<String,Long>();
//	public SplitFile() throws Exception{
//		BufferedReader read = BF.readFile(dirname+"dump\\Analysis\\tableLines.txt");
//		String line = "";
//		while((line=read.readLine())!=null){
//			String [] words=line.split(",");
//			String table = words[0];
//			long lines = Long.valueOf(words[1]);
//			this.tableLines.put(table,lines);
//		}
//	}
	 private static long getTotalLines(String fileName){
//	        FileReader in = new FileReader(fileName);
//	        LineNumberReader reader = new LineNumberReader(in);
		 String strLine = "";
	        long totalLines = 0;
	        int other=0;
		 try{
	        
	        BufferedInputStream fis = new BufferedInputStream(new FileInputStream(fileName));
			BufferedReader br = new BufferedReader(new InputStreamReader(fis,"utf-16"),5*1024*1024);
			File filename = new File(dirname+"users3.txt");
            filename.createNewFile();
            FileWriter fw = new FileWriter(filename); 
            
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename),"utf-8")));
	        while((strLine=br.readLine()) != null){
	       // 	System.out.println(strLine);
	        	char c = strLine.charAt(0);
	        	while(!Character.isDigit(strLine.charAt(0))){
	        		strLine += strLine;
	        		System.out.println(strLine);
	        		other++;
	        		strLine=br.readLine();
	        		continue;
	        	}
	        	totalLines++;
	        	out.write(strLine+"\n");
	        	//	            if(totalLines%1000000==0)
//	            	System.out.println(totalLines/1000000);
	        }
	        br.close();
//	        reader.close();
//	        in.close();
	        out.flush();
	        out.close();
	        System.out.println(other);
		 }catch(Exception e){
			 System.out.println(strLine);
			 e.printStackTrace();
		 }
		  return totalLines;
	    }
	 //预读取各表的行数，用于分离
	 public void getAllLines() throws Exception{
		 File dirFile = new File(dirname+"\\dump\\");
		 File[] files = dirFile.listFiles();
		 BufferedWriter out = new BufferedWriter(new FileWriter(dirname+"\\dump\\tableLines.txt",true));
		
		 out.write("==========="+"\n");
		 for(File f:files){
			 if(f.getName().endsWith(".csv")){
				 System.out.print(f.getName()+",");
				 long n = getTotalLines(f.getPath());
			     System.out.println(n);
				 out.write(f.getName()+","+n+"\n");
				 out.flush();
			 }
		 }
		 out.close();
	 }
/**
 * 把一个txt分成几等分
 * 
 * @param count需要分成几等分
 */
	 public static void splitTxt_followers(int count) {
		 try {
			 FileReader read = new FileReader("D:\\mysql-2015-09-25\\dump\\followers.csv");
			 BufferedReader br = new BufferedReader(read);
			String row;
			List<FileWriter> flist = new ArrayList<FileWriter>();
			System.out.println("Followers is being splited into "+ count+",waiting...");
			//创建count个txt文件,将其filewriter放在列表flist
			for (int i = 0; i < count; i++) {
				flist.add(new FileWriter(dirname+"\\splited_followers\\followers" + i + ".txt"));
			}
			//int rownum = 1;// 计数器
			long rownum = getTotalLines(dirname+"followers.csv");//总行数
		//	System.out.println("总行数："+rownum);
		//	System.out.println("平均每个文件行数rownum/count："+rownum/count);
			int filenum=0;
			int j=0;
			String newrow = null;
			while ((row = br.readLine()) != null) {
				if(j<rownum/count+1){
				//	flist.get(filenum).append(row + "\r\n");
					//去掉时间的双引号
					String[] infos = row.split(",");
					infos[2] = infos[2].substring(1, infos[2].length()-1);
					newrow = infos[0]+","+infos[1]+","+infos[2];
					flist.get(filenum).append(newrow + "\r\n");
					
					j++;
				}
				else{
					flist.get(filenum++).append(row + "\r\n");
				//	filenum++;
					j=0;
				}
			}
			for (int i = 0; i < flist.size(); i++) {
				flist.get(i).close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Splited!");
	}
	 
	 public static String remove_quote(String s){
		 String news;
		 if(s.length()<3){
			 news = null;
		 }
		 else{
			 news = s.substring(1, s.length()-1);
			 }
		 return news;
			
	 }
	
	 public static void splitTxt(String table,int count) {
		 try{
			 String filename = Constants.dirname + "dump\\"+table+".txt";
			 FileReader read = new FileReader(filename);
			 BufferedReader br = new BufferedReader(read);
			String row;
			List<FileWriter> flist = new ArrayList<FileWriter>();
			System.out.println(filename+" is being splited into "+ count+",waiting...");
			//创建count个txt文件,将其filewriter放在列表flist
			for (int i = 0; i < count; i++) {
				flist.add(new FileWriter(Constants.dirname+"splited_"+table+"\\text" + i + ".txt"));
			}
			long rownum = getTotalLines(filename);//总行数
			System.out.println("rownums : "+rownum);
			int filenum=0;
			int j=0;		
			if(table == "projects"){
				int w = 0;
				while ((row = br.readLine()) != null) {
					w++;
				//	System.out.println(row);
					String next_row;
					while(Character.isDigit(row.charAt(0))==false ||Character.isDigit(row.charAt(row.length()-1))==false || row.charAt(row.length()-2)!=',')
					{//第一位不是数字，最后一位不是数字（0,1），倒数第二位不是   ， 逗号 ————则该行不是一个完整的行
						if(row.charAt(row.length()-1)=='\\'){
							row = row.substring(0, row.length()-1);
						}
						next_row = br.readLine();
						if(next_row.length()==1 || next_row.length()==0){
						}
						else{
							row += next_row;
						}
					}
					if(j<rownum/count){
						flist.get(filenum).append(row + "\r\n");
						j++;
					}
					else{
						flist.get(filenum).append(row + "\r\n");
						System.out.println("the "+filenum+" is finished!");
						filenum++;
						j=0;
						
					}
					
				}
				for (int i = 0; i < flist.size(); i++) {
					flist.get(i).close();
				}
				
			}
		
			
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Splited!");
	}
	 public static void splitCV(String table,int count) {//table表名，count为每个分割文件包含行数，默认为50000
		 try{
			// String filename = Constants.dirname + "dump\\"+table+".txt";
			String filename = dirname+"dump\\"+table+".csv";
			 
			FileReader read = new FileReader(filename);
			BufferedReader br = new BufferedReader(read);
			String row;
			List<FileWriter> flist = new ArrayList<FileWriter>();
			System.out.println(filename+" is being splited into "+ count+",waiting...");
			//创建count个txt文件,将其filewriter放在列表flist
			for (int i = 0; i < count; i++) {
				flist.add(new FileWriter(dirname+"splited_"+table+"\\"+table+ i + ".txt"));
			}
			long rownum = getTotalLines(filename);//总行数
			System.out.println("rownums : "+rownum);
		//	int rownum = 21845280; //projects的总行数，单独读取rownum,耗时911s
		
			int filenum=0;
			int j=0;
			//int q=0;
		//	String newrow = null;
			
			//分离project文件
			
			if(table == "projects"){
				int w = 0;
				while ((row = br.readLine()) != null) {
					w++;
				//	System.out.println(row);
					String next_row;
					while(Character.isDigit(row.charAt(0))==false ||Character.isDigit(row.charAt(row.length()-1))==false || row.charAt(row.length()-2)!=',')
					{//第一位不是数字，最后一位不是数字（0,1），倒数第二位不是   ， 逗号 ————则该行不是一个完整的行
						if(row.charAt(row.length()-1)=='\\'){
							row = row.substring(0, row.length()-1);
						}
						next_row = br.readLine();
						if(next_row.length()==1 || next_row.length()==0){
						}
						else{
							row += next_row;
						}
					}
					if(j<rownum/count){
						flist.get(filenum).append(row + "\r\n");
						j++;
					}
					else{
						flist.get(filenum).append(row + "\r\n");
						System.out.println("the "+filenum+" is finished!");
						filenum++;
						j=0;
						
					}
					
				}
				for (int i = 0; i < flist.size(); i++) {
					flist.get(i).close();
				}
				
			}
			
			//分离followers文件
			if(table == "followers" || table == "watchers"){
				while ((row = br.readLine()) != null) {
					
					String newrow = null;
					String[] infos = row.split(",");
					newrow = infos[0] + ","+infos[1]+","+remove_quote(infos[2]);
					
					if(j<rownum/count+1){
					//	flist.get(filenum).append(row + "\r\n");
						//去掉时间的双引号
						flist.get(filenum).append(newrow + "\r\n");
						j++;
					}
					else{
						flist.get(filenum++).append(newrow + "\r\n");
						j=0;
		 			}
				}
				for (int i = 0; i < flist.size(); i++) {
					flist.get(i).close();
				}
			}
			
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Splited!");
	}
	 public static void splitSmpile(String table,int count,String code){//简单的分离
		 try{
			String filename = dirname+"dump\\"+code+"\\"+table+".csv";
			System.out.println(filename+" is being splited into "+ count+",waiting...");
			
//			FileReader read = new FileReader(filename,"utf-16");
//			BufferedReader br = new BufferedReader(read);
			BufferedInputStream fis = new BufferedInputStream(new FileInputStream(new File(filename)));
			BufferedReader br = new BufferedReader(new InputStreamReader(fis,code),5*1024*1024);
			String row="";
			
			//创建count个txt文件,将其filewriter放在列表flist
		//	File file =new File(dirname+"splited_"+table);  
			File file =new File("F:\\splited_"+table);  
			//如果文件夹不存在则创建    
			if(!file.exists()&&!file.isDirectory())      {       
			    file.mkdir();    
			}
//			List<FileWriter> flist = new ArrayList<FileWriter>();
//			
//			for (int i = 0; i < count; i++) {
//				flist.add(new FileWriter(dirname+"splited_"+table+"\\"+table+ i + ".txt"));
//			}
			ArrayList<BufferedWriter> blist = new ArrayList<BufferedWriter>();
			for (int i = 0; i < count; i++){
				blist.add(new BufferedWriter(new FileWriter(file.getPath()+"\\"+table+ i + ".txt")));
			}
			long rownum = tableLines.get(table);//总行数
			//	int rownum = getTotalLines(filename);
			System.out.println("rownums : "+rownum);
			int filenum=0;
			int j=0;
			long avg = rownum/count;
			long yushu=rownum%count;
			long w = 0;
			while ((row = br.readLine()) != null) {
				w++;
				
			//	System.out.println(row);
//				String next_row;
//				while(Character.isDigit(row.charAt(0))==false ||Character.isDigit(row.charAt(row.length()-1))==false || row.charAt(row.length()-2)!=',')
//				{//第一位不是数字，最后一位不是数字（0,1），倒数第二位不是   ， 逗号 ————则该行不是一个完整的行
//					if(row.charAt(row.length()-1)=='\\'){
//						row = row.substring(0, row.length()-1);
//					}
//					next_row = br.readLine();
//					if(next_row.length()==1 || next_row.length()==0){
//					}
//					else{
//						row += next_row;
//					}
//				}
				if(filenum<=yushu){
					avg = rownum/count+1;
				}else{
					avg = rownum/count;
				}
				
					if(j<avg){
					//	System.out.println(filenum);
						
						blist.get(filenum).write(row + "\r\n");
					//	flist.get(filenum).append(row + "\r\n");
						j++;
					}
					else{
						blist.get(filenum).write(row + "\r\n");
						blist.get(filenum).flush();
						//flist.get(filenum).append(row + "\r\n");
					//	System.out.println("the "+filenum+" is finished!");
					//	System.out.println(filenum+"==== "+w);
						filenum++;
						j=0;
					
					}
				
			}
			for (int i = 0; i < blist.size(); i++) {
				blist.get(i).close();
				//	flist.get(i).close();
			}
			System.out.println(w);
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Splited!");
	}
	 //检验文件分离是否丢数据
	public void jianyan(String table) throws IOException{
		long total = tableLines.get(table);
		System.out.println(total+ " total");
		File fdir = new File("f://splited_"+table);
		long sum = 0;
		for(File f:fdir.listFiles()){
			long a = getTotalLines(f.getPath());
			sum += a;
			System.out.println(f.getName()+ " - "+a+" ="+sum);
		}
		if(total==sum)
			System.out.println(table+ " 正确分离");
		else
			System.out.println(total-sum);
	}
	
	public static void main(String[] args) throws Exception{
		SplitFile s = new SplitFile();
		
		long start = System.currentTimeMillis();
	//	s.getAllLines();
		System.out.println(s.getTotalLines(s.dirname+"\\dump\\users.csv"));
	//	s.jianyan("users");
	//	s.splitSmpile("project_members",300,"gbk");
//		String[] codes = {"gbk"};
//		for(String code:codes){
//			File dirFile = new File(dirname+"\\dump\\"+code);
//			File[] files = dirFile.listFiles();
//			for(File f:files){
//			//	System.out.print(f.getName()+",");
//////				if(f.getName().contains("."))
//////				{
//////					System.out.println(f.getName().split(".csv").length+"======");
//////					break;
//////				}
//				String tableName = f.getName().split(".csv")[0];
//				System.out.println(tableName+",");
//				long n = s.tableLines.get(tableName);
//				if(n>300){
//					s.splitSmpile(tableName, 300, code);
//				}
//				System.out.println("completed!");
//			}
//		}
//		
	//	s.getAllLines();
	//	s.jianyan("project_commits");
	//	System.out.println(s.getTotalLines(s.dirname+"\\dump\\gbk\\project_commits.csv"));
		long end = System.currentTimeMillis();
		System.out.println("计算用时="+(end-start)/1000);
	}
}