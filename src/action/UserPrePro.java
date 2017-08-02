package action;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import utils.BF;

public class UserPrePro {

	
//	 @param args
//	 数据预处理
//	  1.去掉ORG
//	2.去掉没有follower的
//	  3.去掉没有项目的
	
	 
	public String tablePath = "D:\\github\\mysql-2015-09-25\\dump\\";
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
	public void prepro(){
		String file = tablePath + "users.csv";
    	System.out.println("read  "+file+", waiting......");
		String [] words;
		String line = null;
		Set<Integer> userIds = new TreeSet<Integer>();
		Set<Integer> fuids = readFolUser();
		Set<Integer> puids = readRepoUser();
	//	Set<Integer> wuids = readWatchersUser();
        try{
        	
        	BufferedReader reader = readFile(file,"utf-16");
    		int i=0;
    		while((line=reader.readLine())!=null){
    			i++;
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
    			words = line.split(",");
    			int typeIndex = words.length-3;
    			if(words[0].compareTo("id")==0){
    				continue;
    			}
    			
    			else if(words[typeIndex].contains("ORG")){
    				continue;
    			}
    			int userid = Integer.valueOf(words[0]);
    			if(fuids.contains(userid)&&puids.contains(userid)){//&&wuids.contains(userid)
    				userIds.add(userid);
    			}
    		}
    		System.out.println("\n noOrg has followers users' number is "+userIds.size());
    		BufferedWriter out = new BufferedWriter(new FileWriter("UserswithFolRepo.txt"));
    		for(int s:userIds){
    			out.write(s+"\n");
    		}
    		out.close();
        }catch(Exception e){
			System.out.println(line);
			e.printStackTrace();
		}
	}
	public Set<Integer> readFolUser(){
		String file = tablePath + "followers.csv";
    	System.out.println("read  "+file+", waiting......");
		String [] words;
		String line = null;
		Set<Integer> uids = new TreeSet();
        try{
        	BufferedReader reader = readFile(file,"gbk");
    		int i=0;
    		while((line=reader.readLine())!=null){
    			i++;
    			if(i%100000==0){
    				System.out.print(i/10000+" ");
    				if(i/100000%50==0){
    					System.out.println();
    				}
    			}
    			words = line.split(",");
    			if(line.contains("id")){
    				continue;
    			}
    		//	System.out.println(line);
    			int userid = Integer.valueOf(words[0]);
    			int fid= Integer.valueOf(words[1]);
    			if(!uids.contains(userid))
    				uids.add(userid);
    			if(!uids.contains(fid))
    				uids.add(fid);
    		}
    		System.out.println("\n number is "+uids.size());
    		BufferedWriter out = new BufferedWriter(new FileWriter("FollowersUsers.txt"));
    		for(int s:uids){
    			out.write(s+"\n");
    		}
    		out.close();
    		return uids;
    		
        }catch(Exception e){
			System.out.println(line);
			e.printStackTrace();
		}
        return null;
	}
	public Set<Integer> readRepoUser(){
		String file = tablePath + "projects.csv";
    	System.out.println("read  "+file+", waiting......");
		String [] words;
		String line = null,lastLine=null;;
		int i=0;
		Set<Integer> wuids = new TreeSet();
        try{
        	BufferedReader reader = readFile(file,"utf-16");
    		
    		while((line=reader.readLine())!=null){
    			i++;
//    			if(i<4222)
//    				continue;
//    			if(i>4230)
//    				break;
    			if(i%100000==0){
    				System.out.print(i/10000+" ");
    				if(i/100000%50==0){
    					System.out.println();
    				}
    			}
    			String next_row;
				while(Character.isDigit(line.charAt(0))==false ||
						Character.isDigit(line.charAt(line.length()-1))==false ||
						line.charAt(line.length()-2)!=',')
				{//第一位不是数字，最后一位不是数字（0,1），倒数第二位不是   ， 逗号 ————则该行不是一个完整的行
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
    			
    			words = line.split(",");
    			if(line.contains("id")){
    				continue;
    			}
    		//	System.out.println(line);
    			if(words.length<9){
    				System.out.println(line);
    			}
    
    		//	System.out.println(line);
    			int userid = Integer.valueOf(words[2]);;
    			
    			wuids.add(userid);
    			lastLine = line;
    		}
    		System.out.println("\n number is "+wuids.size());
    		BufferedWriter out = new BufferedWriter(new FileWriter("ProjectUsers.txt"));
    		for(int s:wuids){
    			out.write(s+"\n");
    		}
    		out.close();
    		return wuids;
    		
        }catch(Exception e){
			System.out.println(i+" "+line);
			e.printStackTrace();
		}
        return null;
	}
	public Set<Integer> readWatchersUser(){
		String file = tablePath + "watchers.csv";
    	System.out.println("read  "+file+", waiting......");
		String [] words;
		String line = null;
		int i=0;
		Set<Integer> wuids = new TreeSet<Integer>();
        try{
        	BufferedReader reader = readFile(file,"gbk");
    		
    		while((line=reader.readLine())!=null){
    			i++;
    			if(i%100000==0){
    				System.out.print(i/10000+" ");
    				if(i/100000%50==0){
    					System.out.println();
    				}
    			}
    			words = line.split(",");
    			if(line.contains("id")){
    				continue;
    			}
    		//	System.out.println(line);
    			if(words.length<3){
    				System.out.println(line);
    			}
    
    		//	System.out.println(line);
    			int userid = Integer.valueOf(words[1]);;
    			wuids.add(userid);
    		}
    		System.out.println("\n number is "+wuids.size());
    		BufferedWriter out = new BufferedWriter(new FileWriter("WatcherUsers.txt"));
    		for(int s:wuids){
    			out.write(s+"\n");
    		}
    		out.close();
    		return wuids;
    		
        }catch(Exception e){
			System.out.println(i+" "+line);
			e.printStackTrace();
		}
        return null;
	}
	public void readUsers() throws IOException{
		String file = tablePath + "users.csv";
    //	System.out.println("read  "+file+", waiting......");
		String [] words;
		String line = null;
		int i=0;
		Set<Integer> selUsersSet = readIds("UserswithFolRepo.txt");
		BufferedWriter out = new BufferedWriter(new FileWriter("testData\\users.txt"));
        try{
        	BufferedReader reader = readFile(file,"utf-16");
    		
    		while((line=reader.readLine())!=null){
    			i++;
    			if(i%100000==0){
    				System.out.print(i/100000+" ");
    				if(i/100000%50==0){
    					System.out.println();
    				}
    			}
    			String next_row = "";
    			while(Character.isDigit(line.charAt(0))==false ||
						Character.isDigit(line.charAt(line.length()-1))==false ||
						line.charAt(line.length()-2)!=',')
				{//第一位不是数字，最后一位不是数字（0,1），倒数第二位不是   ， 逗号 ————则该行不是一个完整的行
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
    			words = line.split(",");
    			if(words[0].compareTo("id")==0){
    				continue;
    			}
//    			else if(words.length<10){
//    				continue;
//    			}
    			int userid = Integer.valueOf(words[0]);
    			if(selUsersSet.contains(userid)){
    				out.write(line+"\n");
    			}else{
    				continue;
    			}
    		}
        }catch(Exception e){
			System.out.println(i+" "+line);
			e.printStackTrace();
		}
        out.close();
	}
	public Set<Integer> readFollowers(){
		String file = tablePath + "followers.csv";
    	System.out.println("read  "+file+", waiting......");
		String [] words;
		String line = null;
		Set<Integer> uids = new TreeSet();
        try{
    		Set<Integer> selUsersSet = readIds("UserswithFolRepo.txt");
    		BufferedWriter out = new BufferedWriter(new FileWriter("testData\\followers.txt"));
        	BufferedReader reader = readFile(file,"gbk");
    		int i=0;
    		while((line=reader.readLine())!=null){
    			i++;
    			if(i%100000==0){
    				System.out.print(i/100000+" ");
    				if(i/100000%50==0){
    					System.out.println();
    				}
    			}
    			words = line.split(",");
    			if(line.contains("id")){
    				continue;
    			}
    		//	System.out.println(line);
    			int userid = Integer.valueOf(words[0]);
    			int folid = Integer.valueOf(words[1]);
    			if(selUsersSet.contains(userid)&&selUsersSet.contains(folid)){
    				out.write(line+"\n");
    			}else{
    				continue;
    			}
    		}
    		System.out.println("\n number is "+uids.size());
    		
    		out.close();
    		
        }catch(Exception e){
			System.out.println(line);
			e.printStackTrace();
		}
        return null;
	}
	public Set<Integer> readProjects(){
		String file = tablePath + "projects.csv";
    //	System.out.println("read  "+file+", waiting......");
		String [] words;
		String line = null;
		int i=0;
        try{
        //	Set<Integer> selUsersSet = readIds("UserswithFolRepo.txt");
    	//	BufferedWriter out1 = new BufferedWriter(new FileWriter("projects1.csv"));
    	//	BufferedWriter out2 = new BufferedWriter(new FileWriter("projects2.csv"));
    	//	BufferedWriter out3 = new BufferedWriter(new FileWriter("projects3.csv"));
    	//	BufferedWriter out4 = new BufferedWriter(new FileWriter("projects4.csv"));
    		BufferedWriter out5 = new BufferedWriter(new FileWriter("projects5.csv"));
        	BufferedReader reader = readFile(file,"utf-16");
    		
    		while((line=reader.readLine())!=null){
    			

    			if(i%100000==0){
    				System.out.print(i/100000+" ");
    				if(i/100000%50==0){
    					System.out.println();
    				}
    			}
    			String next_row;
				while(Character.isDigit(line.charAt(0))==false ||
						Character.isDigit(line.charAt(line.length()-1))==false ||
						line.charAt(line.length()-2)!=',')
				{//第一位不是数字，最后一位不是数字（0,1），倒数第二位不是   ， 逗号 ————则该行不是一个完整的行
					if(line.charAt(line.length()-1)=='\\'){
						line = line.substring(0, line.length()-1);
					}
					next_row = reader.readLine();
					line += next_row;
					
				}
				i++;
//				if(i<6500000){
//					
//				}else{
//					out5.write(line+"\n");
//				}
    			
    			words = line.split(",");
    			if(line.contains("id")){
    				continue;
    			}
 //   			int userid = Integer.valueOf(words[2]);
     			int projectid = Integer.valueOf(words[0]);
     			if(projectid<26036317)
     				continue;
     			else{
     				out5.write(line+"\n");
     				if(i%1000==0)
     					out5.flush();
     			}
    	//		if(selUsersSet.contains(userid)){
    			//	out.write(projectid+"\n");
//    				out.write(line+"\n");
//    			}else{
//    				continue;
//    		 	}
    		}
    	//	out1.close();
    	//	out2.close();
    	//	out3.close();
    	//	out4.close();
    		out5.close();
        }catch(Exception e){
			System.out.println(i+" "+line);
			e.printStackTrace();
		}
        return null;
	}
	public Set<Integer> readWatchers(){
		String file = tablePath + "watchers.csv";
    	System.out.println("read  "+file+", waiting......");
		String [] words;
		String line = null;
		int i=0;
        try{
        	Set<Integer> selUsersSet = readIds("UserswithFolRepo.txt");
        	Set<Integer> selProjectsSet = readIds("testData\\projectsId.txt");
    		BufferedWriter out = new BufferedWriter(new FileWriter("testData\\watchers.txt"));
        	BufferedReader reader = readFile(file,"gbk");
    		
    		while((line=reader.readLine())!=null){
    			i++;
//    			if(i<4222)
//    				continue;
//    			if(i>4230)
//    				break;
    			if(i%100000==0){
    				System.out.print(i/100000+" ");
    				if(i/100000%50==0){
    					System.out.println();
    				}
    			}
    			
    			words = line.split(",");
    			if(line.contains("id")){
    				continue;
    			}
    			
    			int projectid = Integer.valueOf(words[0]);
    			int userid = Integer.valueOf(words[1]);
    			if(selUsersSet.contains(userid)&&selProjectsSet.contains(projectid)){//
    				out.write(line+"\n");
    			}else{
    				continue;
    			}
    		}
    		out.close();
    		
        }catch(Exception e){
			System.out.println(i+" "+line);
			e.printStackTrace();
		}
        return null;
	}
	public Set<Integer> readIds(String file) throws IOException{
	//	String file = "UserswithFolRepo.txt";//读取用到的UserId
		System.out.println("read file : "+file);
		BufferedReader reader = readFile(file,"gbk");
		Set<Integer> UserIds = new TreeSet();
		String line = "";
		while((line=reader.readLine())!=null){
			UserIds.add(Integer.valueOf(line));
		}
		return UserIds;
	}
	public void writeNET() throws IOException{
		
		BufferedWriter out = new BufferedWriter(new FileWriter("testData\\followersId_uid.csv"));
		String file = "UserswithFolRepo.txt";
		
		TreeMap<Integer,Integer> userID = new TreeMap<Integer,Integer>();
		String line = "";
		int i=0;
		BufferedReader r1 = readFile(file,"gbk");
//		while((line = r1.readLine())!=null){
//			i++;
//		}
//		out.write("*Vertices "+i+" \n");
//		i=0;
//		r1 = readFile(file,"gbk");
		while((line = r1.readLine())!=null){
			i++;
			out.write(i+","+line+"\n");
			userID.put(Integer.valueOf(line),i);
		}
		System.out.println(userID.size());
		
	//	BufferedReader r2 = readFile("testData\\followers.txt","gbk");
	//	out.write("*Arcs\n");
		i=0;
//		while((line = r2.readLine())!=null){
//			i++;
////			if(i>10000)
////				break;
//			String[] words = line.split(",");
//			int uid = Integer.valueOf(words[0]);
//			int fid = Integer.valueOf(words[1]);
//		//	out.write(userID.get(fid)+" "+userID.get(uid)+"\n");
//		}
		out.close();
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
	public void userFollowersNET() throws IOException{
		
		BufferedWriter out = new BufferedWriter(new FileWriter("testData\\userFollowersNum.txt"));
		BufferedWriter out2 = new BufferedWriter(new FileWriter("testData\\userFollowersList.txt"));
		
		TreeMap<Integer,Integer> userFollowersNum = new TreeMap<Integer,Integer>();
		TreeMap<Integer,ArrayList<Integer>> userFollowersList = new TreeMap<Integer,ArrayList<Integer>>();
		String line = "";
		int i=0;
		BufferedReader r1 = readFile("testData\\followers.txt","gbk");
		Set<Integer> points = new TreeSet<Integer>();
	//	Set<Integer> points = new TreeSet<Integer>();
		while((line = r1.readLine())!=null){
			i++;
			String[] words = line.split(",");
			int uid = Integer.valueOf(words[0]);
			int fid = Integer.valueOf(words[1]);
			points.add(uid);
			Integer c = userFollowersNum.get(uid);
			if(c==null)
				c=0;
			c++;
			userFollowersNum.put(uid, c);
			ArrayList<Integer> list = userFollowersList.get(uid);
			if(list==null){
				list = new ArrayList<Integer>();
			}
			list.add(fid);
			userFollowersList.put(uid, list);
		}
		for(Integer t:points){
			out.write(t+" "+userFollowersNum.get(t)+"\n");
			ArrayList<Integer> list = userFollowersList.get(t);
			out2.write(t+" ");
			for(Integer w:list){
				out2.write(w+",");
			}
			out2.write("\n");
		}
		out.close();
		out2.close();
	}
	public void Hindex() throws IOException{
		
		BufferedWriter out = new BufferedWriter(new FileWriter("testData\\userHindex.txt"));
		
		TreeMap<Integer,Integer> RepowatchersNum = new TreeMap<Integer,Integer>();
		String line = "";
		int i=0;
		BufferedReader r1 = readFile("testData\\RepowatchersNum.txt","gbk");
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
	public void writeInput() throws IOException{
	//	BufferedWriter out = new BufferedWriter(new FileWriter("testData\\userFollowersNum.txt"));
		
		TreeMap<Integer,Integer> userFollowersNum = new TreeMap<Integer,Integer>();
		TreeMap<Integer,Integer> NumNumberMap = new TreeMap<Integer,Integer>();
		String line1 = "",line2="";
		int i=0;
		BufferedReader r1 = readFile("testData\\follower.clu","gbk");
		BufferedReader r2 = readFile("testData\\followersId_uid.txt","gbk");
		while((line2 = r2.readLine())!=null){
			i++;
			line1 = r1.readLine();
			int num = Integer.valueOf(line1);
			Integer c = NumNumberMap.get(num);
			if(c==null)
				c=0;
			c++;
			NumNumberMap.put(num, c);
			String[] ws = line2.split(" ");
			if(Integer.valueOf(ws[0])==i){
				userFollowersNum.put(Integer.valueOf(ws[1]), Integer.valueOf(line1));
			}
			
		}
		System.out.println(NumNumberMap.size());
//		ArrayList<Map.Entry<Integer, Integer>> list =
//		    new ArrayList<Map.Entry<Integer, Integer>>(userFollowersNum.entrySet());
//
//		//排序
//		Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {   
//		    public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {     
//		        //return (o2.getValue() - o1.getValue());
//		        return o2.getValue()-o1.getValue();
//		    }
//		});
//		for(Map.Entry<Integer, Integer> m:list){
//			out.write(m.getKey()+" "+m.getValue()+"\n");
//		}
//		out.close();
		BufferedWriter out2 = new BufferedWriter(new FileWriter("testData\\NumNumbers.csv"));
		for(Map.Entry<Integer, Integer> m:NumNumberMap.entrySet()){
			out2.write(m.getKey()+","+m.getValue()+"\n");
		}
		out2.close();
	}
	public void findUser(int uid) throws NumberFormatException, IOException{
		System.out.println("repo id "+uid);
		String line="";
		BufferedReader r = readFile("testData\\users.txt","gbk");
		while((line = r.readLine())!=null){
			String[] words=line.split(",");
			int id = Integer.valueOf(words[0]);
			if(id==uid){
				break;
			}else{
				continue;
			}
		}
		System.out.println("\tline: "+line);
		
		BufferedReader r2 = readFile("testData\\userReposList.txt","gbk");
		System.out.print("\tRepos: ");
		while((line = r2.readLine())!=null){
			String[] words=line.split(" ");
			int id = Integer.valueOf(words[0]);
			if(id==uid){
				String[] rids = words[1].split(",");
				for(String rid:rids){
					System.out.print(rid+" ");
				}
				System.out.println();
				System.out.println("\trepos number : "+rids.length);
				break;
			}else{
				continue;
			}
		}
		
		BufferedReader r3 = readFile("testData\\userFollowersList.txt","gbk");
		System.out.print("\tFollowers: ");
		while((line = r3.readLine())!=null){
			String[] words=line.split(" ");
			int id = Integer.valueOf(words[0]);
			if(id==uid){
				String[] fids = words[1].split(",");
				int i=0;
				for(String fid:fids){
					if(i<10)
						System.out.print(fid+" ");
					i++;
				}
				System.out.println();
				System.out.println("\tfollowers number : "+fids.length);
				break;
			}else{
				continue;
			}
		}
	}
	public void findRepo(int rid) throws NumberFormatException, IOException{
		System.out.println("repo id "+rid);
		String line="";
		BufferedReader r = readFile("testData\\projects.txt","gbk");
		while((line = r.readLine())!=null){
			String[] words=line.split(",");
			int id = Integer.valueOf(words[0]);
			if(id==rid){
				break;
			}else{
				continue;
			}
		}
		System.out.println("\tline: "+line);
		
		BufferedReader r2 = readFile("testData\\RepowatchersList.txt","gbk");
		System.out.print("\twatchers: ");
		while((line = r2.readLine())!=null){
			String[] words=line.split(" ");
			int id = Integer.valueOf(words[0]);
			if(id==rid){
				String[] wids = words[1].split(",");
				int i=0;
				for(String wid:wids){
					if(i<10)
						System.out.print(wid+" ");
					i++;
					
				}
				System.out.println();
				System.out.println("\twathcers number : "+wids.length);
				break;
			}else{
				continue;
			}
		}
	}
	public Connection conn() throws ClassNotFoundException, SQLException{
		String connectStr = "jdbc:mysql://localhost:3306/github";  
        connectStr += "?useServerPrepStmts=false&rewriteBatchedStatements=true";  
        String username = "root";  
        String password = "123456";  
		Class.forName("com.mysql.jdbc.Driver");  
    	Connection conn = DriverManager.getConnection(connectStr, username,password); 
    	return conn;
	}
	public void PRtoDB(){
    	//将结果插入数据库
        try{
//			String connectStr = "jdbc:mysql://localhost:3306/github";  
//	        connectStr += "?useServerPrepStmts=false&rewriteBatchedStatements=true";  
//	       
//	        String charset = "gbk";  
//	        boolean debug = true;  
//	        String username = "root";  
//	        String password = "123456";  
//			Class.forName("com.mysql.jdbc.Driver");  
//        	Connection conn = 
//        		DriverManager.getConnection(connectStr, username,password);  
        	Connection conn = conn();
 	        conn.setAutoCommit(false); // 设置手动提交  
 	        int count = 0;  
 	       String insert_sql = "INSERT INTO github.userPR(user_id,user_pr) VALUES (?,?)"; 
	        System.out.println(insert_sql);
 	        PreparedStatement psts = conn.prepareStatement(insert_sql);
 	        BufferedReader reader = BF.readFile("testData\\Result\\UserPR.csv");
			String line =null;
			String[] words;
			while((line = reader.readLine())!=null){
				count++;
				words = line.split(",");
				psts.setInt(1, Integer.valueOf(words[0]));
 		        psts.setDouble(2, Double.valueOf(words[1]));
 		        psts.addBatch();          // 加入批量处理  
 		        if(count%1000 ==0){
 		        	psts.executeBatch(); // 执行批量处理  
 		 	        conn.commit();  // 提交  
 		        }
			}
			psts.executeBatch(); // 执行批量处理  
 	        conn.commit();  // 提交  
 	        System.out.println("All down : " + count);  
	        conn.close(); 
        }catch(ClassNotFoundException e){
        	e.printStackTrace();
        }catch(SQLException e){
        	e.printStackTrace();
        }catch(Exception e){
        	e.printStackTrace();
        }           
    }
	public void toDB(){
    	//将结果插入数据库
        try{
        	Connection conn = conn();
 	        conn.setAutoCommit(false); // 设置手动提交  
 	        int count = 0;  
 	       String insert_sql = "INSERT INTO github.RepoWatchersNum(rid,watchersNum) VALUES (?,?)"; 
	        System.out.println(insert_sql);
 	        PreparedStatement psts = conn.prepareStatement(insert_sql);
 	        BufferedReader reader = BF.readFile("testData\\RepoWatchersNum.txt");
			String line =null;
			String[] words;
			while((line = reader.readLine())!=null){
				count++;
				words = line.split(" ");
				psts.setInt(1, Integer.valueOf(words[0]));
 		        psts.setInt(2, Integer.valueOf(words[1]));
 		        psts.addBatch();          // 加入批量处理  
 		        if(count%1000 ==0){
 		        	psts.executeBatch(); // 执行批量处理  
 		 	        conn.commit();  // 提交  
 		        }
			}
			psts.executeBatch(); // 执行批量处理  
 	        conn.commit();  // 提交  
 	        System.out.println("All down : " + count);  
	        conn.close(); 
        }catch(ClassNotFoundException e){
        	e.printStackTrace();
        }catch(SQLException e){
        	e.printStackTrace();
        }catch(Exception e){
        	e.printStackTrace();
        }           
    }
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		UserPrePro up = new UserPrePro();
		long s = System.currentTimeMillis();
	//	up.prepro();
	//	up.readUsers();
	//	up.readFollowers();
		up.readProjects();
	//	up.readWatchers();
	//	up.writeNET();
	//	up.writeInput();
	//	up.writeWatchersNET();
	//	up.userReposNET();
	//	up.userFollowersNET();
	//	up.Hindex();
	//	up.findUser(1199);
	//	up.findRepo(6179325);
	//	up.PRtoDB();
	//	up.toDB();
		long end = System.currentTimeMillis();
		System.out.println("\nUsed Time : "+(end-s)/1000);
	}
}



