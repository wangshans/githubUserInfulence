package wss.githubFeatures;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import utils.BF;
import utils.Constants;


public class Watchers {

	/**
	 * @param args
	 */
	File watchersFile = new File(Constants.dirname+"dump\\watchers.csv");
	File usersFile = new File(Constants.dirname+"dump\\features\\USR\\userInfo.csv");
	
	public void repoWatchersNum()throws Exception{
		
		File filename = new File(watchersFile.getParent()+"\\features\\repoWatchersNum.csv");
        filename.createNewFile();
        FileWriter fw = new FileWriter(filename); 
        
        PrintWriter out = new PrintWriter(new BufferedWriter(
        		new OutputStreamWriter(new FileOutputStream(filename),"utf-8")));
		
		TreeMap<Integer,Integer> RepowatchersNum = new TreeMap<Integer,Integer>();
		String line = "";
		int i=0;
	
		BufferedReader r1 = BF.readFile(watchersFile);
		while((line = r1.readLine())!=null){
			i++;
			if(i%100000==0){
				System.out.print(i/100000+" ");
				if(i/100000%25==0){
					System.out.println();
				}
			}
			String[] words = line.split(",");
			int rid = Integer.valueOf(words[0]);
			int uid = Integer.valueOf(words[1]);
			Integer c = RepowatchersNum.get(rid);
			if(c==null)
				c=0;
			c++;
			RepowatchersNum.put(rid, c);
		}
		System.out.println(i);
		System.out.println(RepowatchersNum.size());
	
	    int count = 0; 
		for(Map.Entry<Integer, Integer> t:RepowatchersNum.entrySet()){
			count++;
			int rid =t.getKey();
			out.write(rid+","+t.getValue()+"\n");
		
		}
	    System.out.println("All down : " + count);  
		System.out.println(i);
		out.close();
	}
	public void userWatchReposNum()throws Exception{
		String line = "";
		int i=0;
		TreeMap<Integer,Integer> userWatchsNum = new TreeMap<Integer,Integer>();
//		BufferedReader r1 = BF.readFile(usersFile);
//		while((line = r1.readLine())!=null){
//			i++;
//			if(i%100000==0){
//				System.out.print(i/100000+" ");
//				if(i/100000%25==0){
//					System.out.println();
//				}
//			}
//			String[] words = line.split(",");
//			int uid = Integer.valueOf(words[0]);
//			userWatchsNum.put(uid, 0);
//		}
//		System.out.println(i);
//		System.out.println(userWatchsNum.size());
		File filename = new File(watchersFile.getParent()+"\\features\\userWatchReposNum.csv");
        PrintWriter out = new PrintWriter(new BufferedWriter(
        		new OutputStreamWriter(new FileOutputStream(filename),"utf-8")));
	//	File watchers = new File(Constants.screenFile+"tables\\watchers.csv");
		BufferedReader r2 = BF.readFile(watchersFile);
		while((line = r2.readLine())!=null){
			i++;
			if(i%100000==0){
				System.out.print(i/100000+" ");
				if(i/100000%25==0){
					System.out.println();
				}
			}
			String[] words = line.split(",");
			int rid = Integer.valueOf(words[0]);
			int uid = Integer.valueOf(words[1]);
			Integer c = userWatchsNum.get(uid);
			if(c==null)
				c=0;
			c++;
			userWatchsNum.put(uid, c);
		}
		System.out.println(i);
		System.out.println(userWatchsNum.size());
	
	    int count = 0; 
		for(Map.Entry<Integer, Integer> t:userWatchsNum.entrySet()){
			count++;
			int uid =t.getKey();
			out.write(uid+","+t.getValue()+"\n");
		
		}
	    System.out.println("All down : " + count);  
		System.out.println(i);
		out.close();
	}
//	public void repoWatchersList() throws IOException{
//		
//		BufferedReader br = BF.readFile("Data\\Feature\\RepowatchersNum.txt");
//		String line = "";
//		TreeMap<Integer,Integer> RepowatchersNum = new TreeMap<Integer,Integer>();
//		while((line = br.readLine())!=null){
//			String[] words = line.split(",");
//			int uid = Integer.valueOf(words[0]);
//			int rnum = Integer.valueOf(words[1]);
//			RepowatchersNum.put(uid, rnum);
//		}
//		System.out.println("Readed!");
//		BufferedWriter out2 = new BufferedWriter(new FileWriter("Data\\Feature\\RepowatchersList.txt"));
//		TreeMap<Integer,ArrayList<Integer>> RepowatchersList = new TreeMap<Integer,ArrayList<Integer>>();
//		int i=0;
//		File slited_projects = new File("D:\\github\\mysql-2015-09-25\\splited_watchers");
//		File[] files = slited_projects.listFiles();
//		for(File f:files){
//			System.out.println(f.getName());
//			BufferedReader r1 = BF.readFile(f);
//			while((line = r1.readLine())!=null){
//				i++;
//				if(i%100000==0){
//    				System.out.print(i/100000+" ");
//    				if(i/100000%25==0){
//    					System.out.println();
//    				}
//    			}
//				String[] words = line.split(",");
//				int rid = Integer.valueOf(words[0]);
//				int wid = Integer.valueOf(words[1]);
//			
//				ArrayList<Integer> list = RepowatchersList.get(rid);
//				if(list==null){
//					list = new ArrayList<Integer>();
//				}
//				list.add(wid);
//				RepowatchersList.put(rid, list);
//				int num = RepowatchersNum.get(rid);
//				if(list.size()==num){
//					out2.write(rid+",");
//					for(int j=0;j<list.size();j++){
//						if(j==list.size()-1)
//							out2.write(list.get(j)+"\n");
//						else
//							out2.write(list.get(j)+" ");
//					}
//					RepowatchersList.put(rid, null);//已经写完了，则置为空节省空间
//					}
//				}
//				r1.close();
//			}
//			out2.close();
//		}
//	
	public void userWatchersNum(){
		String line = "";
		int i=0;
		try{
			File rwFile = new File(watchersFile.getParent()+"\\features\\repoWatchersNum.csv");
			BufferedReader rRW = BF.readFile(rwFile);
			System.out.println("read "+rwFile.getName());
			TreeMap<Integer,Integer> rwnumMap = new TreeMap<Integer,Integer>();
			while((line=rRW.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if((i/100000)%25==0)
						System.out.println(" ");
				}
				String[]words = line.split(",");
				int rid = Integer.valueOf(words[0]);

			//	System.out.print(uid+" - ");
				int watchersNum = Integer.valueOf(words[1]);
				rwnumMap.put(rid, watchersNum);
 			}
			System.out.println(i+" ");
			System.out.println("rwnum: "+rwnumMap.size());
//			BufferedWriter out = new BufferedWriter(new FileWriter
//					(Constants.screenFile+"features\\usersWatchersSumAveMaxNum.csv"));
//			BufferedWriter outList = new BufferedWriter(new FileWriter
//					(Constants.screenFile+"features\\usersWatchersNumList.csv"));
			BufferedWriter outMax = new BufferedWriter(new FileWriter
					(usersFile.getParent()+"\\usersWatchersMaxNum.csv"));
			BufferedWriter outSum = new BufferedWriter(new FileWriter
					(usersFile.getParent()+"\\usersWatchersSumNum.csv"));
			BufferedWriter outAve = new BufferedWriter(new FileWriter
					(usersFile.getParent()+"\\usersWatchersAveNum.csv"));
			BufferedWriter outList = new BufferedWriter(new FileWriter
					(usersFile.getParent()+"\\usersWatcherNumList.csv"));
			BufferedWriter outH = new BufferedWriter(new FileWriter
					(usersFile.getParent()+"\\usersWatcherHindex.csv"));
			BufferedReader rUR = BF.readFile(usersFile.getParent()+"\\userReposList.csv");
			i=0;
		//	TreeMap<Integer,ArrayList<Integer>> uwlist = new TreeMap<Integer,ArrayList<Integer>>();
			while((line=rUR.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if((i/100000)%25==0)
						System.out.println(" ");
					outMax.flush();
					outSum.flush();
					outAve.flush();
				}
				String[]words = line.split(",");
				int uid = Integer.valueOf(words[0]);
				if(words.length==1){
					outSum.write(uid+","+0+"\n");
					outAve.write(uid+","+0+"\n");
					outMax.write(uid+","+0+"\n");
				}else{

					int sumNum = 0,maxNum=0;
					String[] reposid = words[1].split(" ");
					for(String rid:reposid){
						Integer repoid = Integer.valueOf(rid);
						Integer Num = rwnumMap.get(repoid);
						if(Num==null)
							Num=0;
						sumNum += Num;
						if(maxNum<Num){
							maxNum = Num;
						}
					}
					double aveNum = (double)sumNum/reposid.length;
					outSum.write(uid+","+sumNum+"\n");
					outAve.write(uid+","+aveNum+"\n");
					outMax.write(uid+","+maxNum+"\n");
				}
				ArrayList<Integer> watchersNumList = new ArrayList<Integer>();
				int h_index = 0;
		//		Integer uid = Integer.valueOf(words[0]);
				String[] reposid = words[1].split(" ");
				for(String rid:reposid){
					Integer repoid = Integer.valueOf(rid);
					Integer num = rwnumMap.get(repoid);
					if(num==null)
						num=0;
					watchersNumList.add(num);
				}
				//排序
	 			Collections.sort(watchersNumList,new Comparator<Integer>(){
	 				public int compare(Integer i1,Integer i2){
	 					return i2.compareTo(i1);
	 				}
	 			});
				
				outList.write(words[0]);
				for(int j=1;j<watchersNumList.size()+1;j++){
					int num = watchersNumList.get(j-1);
					outList.write(","+num);
				}
				outList.write("\n");
				for(int j=1;j<watchersNumList.size()+1;j++){
					int num = watchersNumList.get(j-1);
					if(j>num){
						h_index = j-1;
						break;
					}
					
				}
				outH.write(words[0]+","+h_index+"\n");
				
			//	break;
 			}
			System.out.println("users total num is "+i);
			
			outSum.close(); 
			outMax.close(); 
			outAve.close();
		}catch(Exception e){
			System.out.println(line);
			e.printStackTrace();
		}
	}
	
	public void userWatchUser(){//转化为用户watch用户的关系
		try{
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void splitWatchers(){
		try{
			String line = "";
			int i=0;
			BufferedReader read = BF.readFile(Constants.screenFile+"tables\\projects.csv");
			TreeMap<Integer,Integer> repoOwnerMap = new TreeMap<Integer,Integer>();
			while((line=read.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if((i/100000)%25==0)
						System.out.println(" ");
				}
				String[]words = line.split(",");
				int rid = Integer.valueOf(words[0]);
				int ownerid = Integer.valueOf(words[1]);
				repoOwnerMap.put(rid, ownerid);
			}
			System.out.println("repos : "+repoOwnerMap.size());
			BufferedWriter out = new BufferedWriter(new FileWriter
					(Constants.screenFile+"features\\userWatchUser.csv"));
			BufferedReader readW = BF.readFile(Constants.screenFile+"tables\\watchers.csv");
			i=0;
			while((line=readW.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if((i/100000)%25==0)
						System.out.println(" ");
				}
				String[]words = line.split(",");
				Integer rid = Integer.valueOf(words[0]);
				Integer ownerId = repoOwnerMap.get(rid);
				out.write(ownerId+","+words[1]+"\n");
 			}
			System.out.println(i+" ");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static int x = 100;
	public void selectuserID(int userid){
		try{
			String line = "";
			int i=0;
	  //  	File usersFP = new File(Constants.screenFile+"tables\\users.csv");
	    	System.out.println("read  "+watchersFile.getName()+", waiting......");
	    	BufferedReader reader = BF.readFile(watchersFile,"utf8");
			while((line=reader.readLine())!=null){
				i++;
//				if(i%100000==0){
//					System.out.print(i/100000+" ");
//					if(i/100000%25==0){
//						System.out.println();
//					}
//				}
				String [] words = line.split(",");
				int id = Integer.valueOf(words[1]);
				
				if(userid == id){
					System.out.println(line);
				//	break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Watchers w = new Watchers();
		long start = System.currentTimeMillis();
	//	w.watchersToDB();
	//	w.repoWatchersNum();
	//	w.userWatchReposNum();
	//	w.userWatchersNum();
	//	w.userWatchersNum();
	//	w.repoWatchersList();
		w.selectuserID(435202);
		long end = System.currentTimeMillis();
		System.out.println("计算用时="+(end-start)/1000);
	}

}
