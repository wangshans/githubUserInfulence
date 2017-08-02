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

public class projectsTime {

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
	public void splitProjectsByYear() throws Exception{
		
		String line="";
		ArrayList<String> lineList = new ArrayList<String>();
		try{
			ArrayList<BufferedWriter> outs = new ArrayList<BufferedWriter>();
			for(int i=2007;i<2016;i++){
				BufferedWriter out = new BufferedWriter(new FileWriter("projectsByYear"+i+".txt"));
				outs.add(out);
			}
			for(int i=0;i<5;i++){
				String file = "projects"+i+".txt";
			
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
	public void readProjects() throws Exception{
		String line="";
		for(int i=2015;i<2016;i++){
			ArrayList<String> lineList = new ArrayList<String>();
			String file = "projectsByYear"+i+".txt";
			BufferedReader reader = readFile(file);
		
			while((line=reader.readLine())!=null){
				lineList.add(line);
			}
			//排序
			Collections.sort(lineList, new Comparator<String>() {   
			    public int compare(String s1, String s2) {     
			        String[] w1 = s1.split(",");
			        String[] w2 = s2.split(",");
			        Date d1=null,d2=null;
					try{
						d1 = df.parse(w1[2]);
			            d2 = df.parse(w2[2]);
					}catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        long l = d2.getTime()-d1.getTime();
			        if(l>0)
			        	return 1;
			        else
			        	return 0;
			    }
			});
			System.out.println("sorted");
			BufferedWriter out = new BufferedWriter(new FileWriter("projectsByTime"+i+".txt"));
			for(String s:lineList){
				out.write(s+"\n");
			}
			out.close();
		}
		

	}
	public void SpitProjectsByMonth(int y) throws IOException, ParseException{
		String file = "projectsByTime"+y+".txt";
		BufferedReader reader = readFile(file);
		String line="";
		Date baseTime = df.parse("2015-05-04 00:00:00");
		System.out.println(baseTime.getYear());
		int year = baseTime.getYear(),month = baseTime.getMonth();
		System.out.println(year+" "+month+" ");
		int i=0;
		BufferedWriter out = new BufferedWriter(new FileWriter("Data\\spliProjectsByTime\\"+"fol"+year+"-"+month+".txt"));
		while((line=reader.readLine())!=null){
			String[] words = line.split(",");
			Date d = df.parse(words[2].substring(1,words[2].length()-1));
			if(d.getYear()==year&&d.getMonth()==month){
				out.write(line+"\n");
			}else{
				out.close();
				year = d.getYear();
				month = d.getMonth();
				out = new BufferedWriter(new FileWriter("Data\\spliProjectsByTime\\"+"fol"+year+"-"+month+".txt"));
			//	System.out.println(year+" "+month+" ");
				out.write(line+"\n");
			}
		}
		out.close();
	}
	public void SpitProjects() throws Exception{
		String line="";
		for(int i=2015;i<2016;i++){
			ArrayList<String> lineList = new ArrayList<String>();
			String file = "projectsByYear"+i+".txt";
			BufferedReader reader = readFile(file);
			ArrayList<BufferedWriter> outs = new ArrayList<BufferedWriter>();
			for(int month=1;month<13;month++){//1-12月份
				BufferedWriter out = new BufferedWriter(
						new FileWriter("Data\\spliProjectsByTime\\pro"+i+"-"+month+".txt"));
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
		projectsTime p = new projectsTime();
		//	p.readProjects();
		//	p.splitProjectsByYear();
//			p.writeProjects();
		//	p.SpitProjects();
			
		File f = new File("F:\\myeclipse\\workspace\\github\\Data\\spliFollowersByTime\\");
		File[] fs = f.listFiles();
		String first ="";
		String sec = "";
		for(File ff:fs){
			String name = ff.getName();
			String[] names = name.split("-");
			first = names[0].replaceFirst("114", "2014");
			
			for(int i=0;i<9;i++){
				sec = names[1].replace(""+i, (""+(i+1)));
				name = first+"-"+sec;
				ff.renameTo(new File("Data\\spliFollowersByTime\\"+name+".txt"));
			}
			
			
		}
			
	}

}
