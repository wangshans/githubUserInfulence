package features;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Feature {

	/**
	 * @param args
	 */
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String tablePath = "D:\\github\\mysql-2015-09-25\\dump\\";
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
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis),5*1024*1024);
			return reader;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	//1.用户创建时长（以年为单位，向上取整，即不足一年按一年计算）
	public void preproUser(){
		String file = tablePath + "projects0.csv";
    //	System.out.println("read  "+file+", waiting......");
		String [] words;
		String line = null;
        try{
    		BufferedWriter out = new BufferedWriter(new FileWriter(tablePath+"projects.csv"));
        	BufferedReader reader = readFile(file);
    		int i=0;
    		while((line=reader.readLine())!=null){
    			
    			i++;
    			if(i==1)//第一行为标题行
    				continue;
    			System.out.println(i);
    			if(i%100000==0){
    				System.out.print(i/10000+" ");
    				if(i/100000%50==0){
    					System.out.println();
    				}
    			}
    			String next_row = "";
    			while(Character.isDigit(line.charAt(0))==false ||
						Character.isDigit(line.charAt(line.length()-1))==false ||
						line.charAt(line.length()-2)!=',')
				{//第一位不是数字，最后一位不是数字（0,1），倒数第二位不是   ， 逗号 ————则该行不是一个完整的行
    			//	System.out.println("\t"+line);
					if(line.charAt(line.length()-1)=='\\'){
						line = line.substring(0, line.length()-1);
					}
					next_row = reader.readLine();
					if(next_row.length()==1 || next_row.length()==0){
					}
					else{
						line += next_row;
					}
				}
    			out.write(line+"\n");
    		}
    		out.close();
        }catch(Exception e){
			System.out.println(line);
			e.printStackTrace();
		}
	}
	public void preproRepo() throws ParseException{
	//	String file = tablePath + "projects0.csv";
    //	System.out.println("read  "+file+", waiting......");
		String line = null;
		DecimalFormat doubleDF   = new DecimalFormat("######0.00"); 
		Date thisTime = df.parse("2016-12-01 00:00:00");
		long t1 = thisTime.getTime();
        try{
    	//	BufferedWriter out = new BufferedWriter(new FileWriter(tablePath+"projects.csv"));
    		BufferedWriter out = new BufferedWriter(
    				new FileWriter("Data\\Feature\\repoYear.txt",true));
    		File f = new File("D:\\github\\mysql-2015-09-25\\splited_projects");
    		File[] files = f.listFiles();
    		for(File file:files){
	        	BufferedReader reader = readFile(file);
	    		int i=0;
	    		while((line=reader.readLine())!=null){
	    			i++;
	    			if(line.split(",")[0].contains("id"))
	    				continue;
	    			if(i%100000==0){
	    				System.out.print(i/100000+" ");
	    				if(i/100000%50==0){
	    					System.out.println();
	    				}
	    				out.flush();
	    			}
	    			
	    			String next_row = "";
	    			while(Character.isDigit(line.charAt(0))==false ||
							Character.isDigit(line.charAt(line.length()-1))==false ||
							line.charAt(line.length()-2)!=',')
					{//第一位不是数字，最后一位不是数字（0,1），倒数第二位不是   ， 逗号 ————则该行不是一个完整的行
	    			//	System.out.println("\t"+line);
						if(line.charAt(line.length()-1)=='\\'){
							line = line.substring(0, line.length()-1);
						}
						next_row = reader.readLine();
						if(next_row.length()==1 || next_row.length()==0){
						}
						else{
							line += next_row;
						}
					}
	    			//通过去掉url字段，简化project表
	    			String [] words = line.split(",");
	//    			int id = Integer.valueOf(ws[0]);
	//    			if(id<8066210)
//	//    				continue;
//	    			for(int j=0;j<ws.length;j++){
//	    				if(j!=1&&j!=ws.length-1){
//	    					out.write(ws[j]+",");
//	    				}else if(j==ws.length-1){
//	    					out.write(ws[j]+"\n");
//	    				}
//	    			}
	    			Integer id = Integer.valueOf(words[0]);

	    			//	System.out.println(id);
	    				Date d = df.parse(words[words.length-3].substring(1,words[words.length-3].length()-1));
	    				long t2 = d.getTime();
	    				long time = (t1-t2)/1000;
	    				double Dyear = (double)time/(60*60*24*365);
//	    				int year = d.getYear()+1900;
//	    				int month = d.getMonth()+1;
	    				//	System.out.println(year+" "+month+" ");
	    				out.write(id+","+(doubleDF.format(Dyear))+"\n");
	    		//	out.write(ws[0]+"\n");
	    		}
    		}
    		out.close();
        }catch(Exception e){
			System.out.println(line);
			e.printStackTrace();
		}
	}
	public void userYear() throws Exception{
		String file = "D:\\github\\mysql-2015-09-25\\dump\\users.csv";
		BufferedReader reader = readFile(file);
		String line="";
		int thisYear = 2016;
		DecimalFormat doubleDF   = new DecimalFormat("######0.00");   
		Date thisTime = df.parse("2016-12-01 00:00:00");
		long t1 = thisTime.getTime();
		BufferedWriter out = new BufferedWriter(
				new FileWriter("Data\\userYear1.txt"));
		int i=0;
		while((line=reader.readLine())!=null){
			i++;
			if(i%10000==0)
				System.out.println(i/10000);
			
			String[] words = line.split(",");
			String id = words[0];
		//	System.out.println(id);
			Date d = df.parse(words[words.length-4].substring(1,words[words.length-4].length()-1));
			long t2 = d.getTime();
			long time = (t1-t2)/1000;
			double Dyear = (double)time/(60*60*24*365);
//			int year = d.getYear()+1900;
//			int month = d.getMonth()+1;
			//	System.out.println(year+" "+month+" ");
			out.write(id+","+(doubleDF.format(Dyear))+"\n");
			}
		out.close();
	}
	public void repoYear() throws Exception{
		String file = "D:\\github\\mysql-2015-09-25\\dump\\projects.csv";
		BufferedReader reader = readFile(file);
		String line="";
		int thisYear = 2016;
		DecimalFormat doubleDF   = new DecimalFormat("######0.00");   
		Date thisTime = df.parse("2016-12-01 00:00:00");
		long t1 = thisTime.getTime();
		BufferedWriter out = new BufferedWriter(
				new FileWriter("Data\\Feature\\repoYear.txt",true));
		int i=0;
		while((line=reader.readLine())!=null){
			i++;
			if(i%10000==0)
				System.out.println(i/10000);
			
			String[] words = line.split(",");
			Integer id = Integer.valueOf(words[0]);

		//	System.out.println(id);
			Date d = df.parse(words[words.length-3].substring(1,words[words.length-3].length()-1));
			long t2 = d.getTime();
			long time = (t1-t2)/1000;
			double Dyear = (double)time/(60*60*24*365);
//			int year = d.getYear()+1900;
//			int month = d.getMonth()+1;
			//	System.out.println(year+" "+month+" ");
			out.write(id+","+(doubleDF.format(Dyear))+"\n");
			}
		System.out.println();
		out.close();
	}
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Feature f = new Feature();
		long start = System.currentTimeMillis();
		//		f.repoYear();
		f.preproRepo();
		long end = System.currentTimeMillis();
		System.out.println((end-start)/1000);
	}

}
