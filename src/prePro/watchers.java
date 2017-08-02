package prePro;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class watchers {

	/**
	 * @param args
	 */
	//watcher: repo--watcher
	//projects: repo--owner
	//从而得到 owner--watchersNum关系
	public BufferedReader readFile(String filePath,String format){
		System.out.println("read file from "+filePath);
		try{
			if(format==null){
				format = "utf-8";
			}
			BufferedInputStream fis = new BufferedInputStream(new FileInputStream(filePath));
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis,format),5*1024*1024);
			return reader;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public void writeWatchersNET() throws IOException{
		
		BufferedWriter out = new BufferedWriter(new FileWriter("testData\\RepowatchersNum.txt"));
		BufferedWriter out2 = new BufferedWriter(new FileWriter("testData\\RepowatchersList.txt"));
		
		TreeMap<Integer,Integer> RepowatchersNum = new TreeMap<Integer,Integer>();
		TreeMap<Integer,ArrayList<Integer>> RepowatchersList = new TreeMap<Integer,ArrayList<Integer>>();
		String line = "";
		int i=0;
		BufferedReader r1 = readFile("testData\\watchers.txt","gbk");
		Set<Integer> points = new TreeSet<Integer>();
	//	Set<Integer> points = new TreeSet<Integer>();
		while((line = r1.readLine())!=null){
			i++;
			String[] words = line.split(",");
			int rid = Integer.valueOf(words[0]);
			int wid = Integer.valueOf(words[1]);
			points.add(rid);
			Integer c = RepowatchersNum.get(rid);
			if(c==null)
				c=0;
			c++;
			RepowatchersNum.put(rid, c);
			ArrayList<Integer> list = RepowatchersList.get(rid);
			if(list==null){
				list = new ArrayList<Integer>();
			}
			list.add(wid);
			RepowatchersList.put(rid, list);
		}
		for(Integer t:points){
			out.write(t+" "+RepowatchersNum.get(t)+"\n");
			ArrayList<Integer> list = RepowatchersList.get(t);
			out2.write(t+" ");
			for(Integer w:list){
				out2.write(w+",");
			}
			out2.write("\n");
		}
		out.close();
		out2.close();
	}
	public void userReposNET() throws IOException{
		
		BufferedWriter out = new BufferedWriter(new FileWriter("testData\\userReposNum.txt"));
		BufferedWriter out2 = new BufferedWriter(new FileWriter("testData\\userReposList.txt"));
		
		TreeMap<Integer,Integer> RepowatchersNum = new TreeMap<Integer,Integer>();
		TreeMap<Integer,ArrayList<Integer>> RepowatchersList = new TreeMap<Integer,ArrayList<Integer>>();
		String line = "";
		int i=0;
		BufferedReader r1 = readFile("testData\\projects.txt","gbk");
		Set<Integer> points = new TreeSet<Integer>();
	//	Set<Integer> points = new TreeSet<Integer>();
		while((line = r1.readLine())!=null){
			i++;
			String[] words = line.split(",");
			int rid = Integer.valueOf(words[0]);
			int uid = Integer.valueOf(words[2]);
			points.add(uid);
			Integer c = RepowatchersNum.get(uid);
			if(c==null)
				c=0;
			c++;
			RepowatchersNum.put(uid, c);
			ArrayList<Integer> list = RepowatchersList.get(uid);
			if(list==null){
				list = new ArrayList<Integer>();
			}
			list.add(rid);
			RepowatchersList.put(uid, list);
		}
		for(Integer t:points){
			out.write(t+" "+RepowatchersNum.get(t)+"\n");
			ArrayList<Integer> list = RepowatchersList.get(t);
			out2.write(t+" ");
			for(Integer w:list){
				out2.write(w+",");
			}
			out2.write("\n");
		}
		out.close();
		out2.close();
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
