package action;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class Hindex {

	/**
	 * @param args
	 */
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
	public void run(String time) throws IOException{
		
		BufferedWriter out = new BufferedWriter(new FileWriter("Data\\result\\"+time+"userHindex.txt"));
		
		TreeMap<Integer,Integer> RepowatchersNum = new TreeMap<Integer,Integer>();
		String line = "";
		int i=0;
		BufferedReader r1 = readFile("Data\\result\\"+time+"RepowatchersNum.txt","gbk");
		while((line = r1.readLine())!=null){
			i++;
			String[] words = line.split(" ");
			int rid = Integer.valueOf(words[0]);
			int num = Integer.valueOf(words[1]);
			RepowatchersNum.put(rid, num);
		}
		System.out.println(RepowatchersNum.size());
		BufferedReader r2 = readFile("testData\\userReposList.txt","gbk");
		TreeMap<Integer,Integer> userHindex = new TreeMap<Integer,Integer>();
		while((line = r2.readLine())!=null){
		//	System.out.println(line);
			i++;
			String[] words = line.split(" ");
			int uid = Integer.valueOf(words[0]);
			String[] rids = words[1].split(",");
			TreeMap<Integer,Integer> tempRW = new TreeMap<Integer,Integer>();
			ArrayList<Integer> tempWNumList = new ArrayList<Integer>();
			for(String rid:rids){
				
				Integer wnum = RepowatchersNum.get(Integer.valueOf(rid));
		//		System.out.println("\t"+rid+" "+wnum);
				if(wnum==null)
				{
				//	System.out.println(line);
				//	break;
					wnum=0;
				}
				tempRW.put(Integer.valueOf(rid), wnum);
				tempWNumList.add(wnum);
			}
			//排序
			Collections.sort(tempWNumList, new Comparator<Integer>() {   
			    public int compare(Integer m1, Integer m2) {     
			        return (m2 - m1);
			        //  return (m2.getKey())-(m1.getKey());
			    }
			});
		//	System.out.println("=============");
//			for(Integer t:tempWNumList){
//				System.out.println("\t"+t);
//			}
			int j=0;
			for(int index:tempWNumList){
				j++;
				if(index<j)
					break;
			}
			userHindex.put(uid, j-1);
		//	System.out.println(j-1);
		//	break;
		}
		ArrayList<Map.Entry<Integer, Integer>> list=
		    new ArrayList<Map.Entry<Integer, Integer>>(userHindex.entrySet());

		//排序
		Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {   
		    public int compare(Map.Entry<Integer, Integer> m1, Map.Entry<Integer, Integer> m2) {     
		        return (m2.getValue() - m1.getValue());
		        //  return (m2.getKey())-(m1.getKey());
		    }
		});

		for(Map.Entry<Integer, Integer> m:list){
			out.write(m.getKey()+" "+m.getValue()+"\n");
			
		}
		out.close();
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
