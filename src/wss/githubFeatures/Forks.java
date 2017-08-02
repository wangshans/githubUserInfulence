package wss.githubFeatures;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import utils.BF;
import utils.Constants;

public class Forks {

	/**
	 * @param args
	 * 根据projects中的项目之间的fork关系形成了项目Fork网络
	 * 可以用来求：
	 * 	1.项目被Fork数量
	 * 	2.用户拥有的项目被Fork总数和平均数
	 */

	
//	File projectsFile = new File(Constants.screenFile+"tables\\projects.csv");
	File projectsFile = new File(Constants.dumpFile+"projectsND.csv");
	File forksFile = new File(projectsFile.getParent()+"\\forks.csv");
	public void getReposForkNet(){//根据被选中的repos对watchers进行筛选
    	System.out.println("read  "+projectsFile.getName()+", waiting......");
		String [] words;
		String line = null;
		int i=0;
		TreeSet<Integer> repoHasFork = new TreeSet<Integer> ();//被Fork过的项目
        try{
        	BufferedReader reader = BF.readFile(projectsFile);
    		File outFile = new File(projectsFile.getParent()+"\\forks.csv");
    		PrintWriter out = new PrintWriter(
    				new BufferedWriter(new OutputStreamWriter(
    						new FileOutputStream(outFile),"utf-8")));
    		while((line=reader.readLine())!=null){
    			i++;
    			if(i%100000==0){
    				System.out.print(i/100000+" ");
    				if(i/100000%25==0){
    					System.out.println();
    				}
    			}
    			words = line.split(",");
    			int rid = Integer.valueOf(words[0]);
    			String forked_from = words[words.length-4];
    			
    			if(forked_from.equals("\\N")){
    				continue;
    			}
    			if(forked_from.contains("-1")){
    				System.out.println(line);
    				break;
    			}
    			Integer forkedfrom = Integer.valueOf(forked_from);
    			
    			out.write(rid+","+words[2]+","+forkedfrom+","+words[words.length-5]+"\n");
    			repoHasFork.add(forkedfrom);
    		}
    		out.close();
    		System.out.println(repoHasFork.size());
        }catch(Exception e){
			System.out.println(i+" "+line);
			e.printStackTrace();
		}
	}
	public void repoForkedNET(){//项目被Fork的次数和列表
		try{
			String line = "";
			int i=0;
			TreeMap<Integer,ArrayList<Integer>> repoForksListMap = new TreeMap<Integer,ArrayList<Integer>>();
//	    	File usersFP = new File(Constants.screenFile+"tables\\projects.csv");
//	    	System.out.println("read  "+usersFP.getName()+", waiting......");
//	    	BufferedReader reader = BF.readFile(usersFP,"utf8");
//			while((line=reader.readLine())!=null){
//				i++;
//				if(i%100000==0){
//					System.out.print(i/100000+" ");
//					if(i/100000%25==0){
//						System.out.println();
//					}
//				}
//				String [] words = line.split(",");
//				int id = Integer.valueOf(words[0]);;
//				repoForksListMap.put(id, null);
//			}
//			System.out.println("\n repos number is "+repoForksListMap.size());
			
			BufferedWriter outN = new BufferedWriter(new FileWriter
					(forksFile.getParent()+"\\features\\repoForkedNum.csv"));
			BufferedWriter outL = new BufferedWriter(new FileWriter
					(forksFile.getParent()+"\\features\\repoForkedList.csv"));
			
		//	TreeMap<Integer,ArrayList<Integer>> repoForksList = new TreeMap<Integer,ArrayList<Integer>>();
			
			System.out.println(forksFile.getName());
			BufferedReader r1 = BF.readFile(forksFile);
			i=0;
			while((line = r1.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if((i/100000)%25==0)
						System.out.println(" ");
					
				}
				String[] words = line.split(",");
				int id = Integer.valueOf(words[0]);
				Integer forkedfrom = Integer.valueOf(words[2]);
				
//				if(!repoForksListMap.containsKey(forkedfrom)){
//					continue;
//				}
				ArrayList<Integer> list = repoForksListMap.get(forkedfrom);
				if(list==null){
					list = new ArrayList<Integer>();
				}
				list.add(id);
				repoForksListMap.put(forkedfrom, list);
			}
			
			for(Map.Entry<Integer, ArrayList<Integer>> m:repoForksListMap.entrySet()){
				int t = m.getKey();//userid
				ArrayList<Integer> list = repoForksListMap.get(t);
				if(list==null){
					outN.write(t+","+0+"\n");
					outL.write(t+",");
					outL.write("\n");
				}else{
					outN.write(t+","+list.size()+"\n");
					outL.write(t+",");
					for(Integer w:list){
						outL.write(w+" ");
					}
					outL.write("\n");
				}
			}
			outN.close();
			outL.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public void repoForkedPRedNum(){//项目被Fork,并被pull_request的数量
		try{
			String line = "";
			int i=0;
			TreeMap<Integer,Integer> repoPull_requestNumMap = new TreeMap<Integer,Integer>();
			TreeMap<Integer,Integer> repoMergedNumMap = new TreeMap<Integer,Integer>();
	    	File RP = new File(forksFile.getParent()+"\\features\\repoPull_requestNumMergedNum.csv");
	    	System.out.println("read  "+RP.getName()+", waiting......");
	    	BufferedReader reader = BF.readFile(RP,"utf8");
			while((line=reader.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%25==0){
						System.out.println();
					}
				}
				String [] words = line.split(",");
				Integer id = Integer.valueOf(words[0]);
				Integer num = Integer.valueOf(words[1]);
				repoPull_requestNumMap.put(id, num);
				repoMergedNumMap.put(id, Integer.valueOf(words[2]));
			}
			System.out.println("\n repos number is "+repoPull_requestNumMap.size());
			
			BufferedWriter outN = new BufferedWriter(new FileWriter
					(forksFile.getParent()+"\\features\\repoForkedNumPRNumMergedNum.csv"));
			outN.write("repoID,forkedNum,Pull_redNum,mergedPRNum\n");
			BufferedWriter outL = new BufferedWriter(new FileWriter
					(forksFile.getParent()+"\\features\\repoPRedNumList.csv"));
			
			File f = new File(forksFile.getParent()+"\\features\\repoForkedList.csv");
			System.out.println(f.getName());
			BufferedReader r1 = BF.readFile(f);
			i=0;
			while((line = r1.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if((i/100000)%25==0)
						System.out.println(" ");
					
				}
				String[] words = line.split(",");
				Integer id = Integer.valueOf(words[0]);
				String[] forksList = words[1].split(" ");
				outL.write(id+",");
				int forkedPRedNum = 0,merged=0;
			//	int num = 0;
				if(forksList.length==0){
					outL.write("\n");
					continue;
				}else{
					for(String s:forksList){
						Integer fork = Integer.valueOf(s);//fork的项目
						Integer pull_request_num = repoPull_requestNumMap.get(fork);
						if(pull_request_num==null||pull_request_num==0){//该fork没有pull_request
							pull_request_num  = 0;
						}else{
						//	forkedPRedNum ++;
						}
						forkedPRedNum += pull_request_num;
						outL.write(pull_request_num+" ");
						Integer mergedNum = repoMergedNumMap.get(fork);
						if(mergedNum==null||mergedNum==0){
							mergedNum = 0;
						}else{
						//	merged++;
						}
						merged += mergedNum;
					}
					outL.write("\n");
				}
				outN.write(id+","+forksList.length+","+forkedPRedNum+","+merged+"\n");//repo,forksNum,forksWithPR_Num
			}
			outN.close();
			outL.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public void repoForksWithPull_requestNum(){//项目被Fork,并被pull_request的数量
		try{
			String line = "";
			int i=0;
			TreeMap<Integer,Integer> repoPull_requestNumMap = new TreeMap<Integer,Integer>();
	    	File RP = new File(Constants.screenFile+"features\\ForkRepoPull_RequestsNum.csv");
	    	System.out.println("read  "+RP.getName()+", waiting......");
	    	BufferedReader reader = BF.readFile(RP,"utf8");
			while((line=reader.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%25==0){
						System.out.println();
					}
				}
				String [] words = line.split(",");
				Integer id = Integer.valueOf(words[0]);
				Integer num = Integer.valueOf(words[1]);
				repoPull_requestNumMap.put(id, num);
			}
			System.out.println("\n repos number is "+repoPull_requestNumMap.size());
			
			BufferedWriter outN = new BufferedWriter(new FileWriter
					(Constants.screenFile+"features\\repoForksWithPull_RequestNum.csv"));
//			BufferedWriter outL = new BufferedWriter(new FileWriter
//					(Constants.screenFile+"features\\repoForks_Pull_Request_List.csv"));
			
		//	TreeMap<Integer,ArrayList<Integer>> repoForksList = new TreeMap<Integer,ArrayList<Integer>>();
			
			File f = new File(Constants.screenFile+"features\\repoForksList.csv");
			System.out.println(f.getName());
			BufferedReader r1 = BF.readFile(f);
			int sum = 0;
			i=0;
			while((line = r1.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if((i/100000)%25==0)
						System.out.println(" ");
					
				}
				String[] words = line.split(",");
				Integer id = Integer.valueOf(words[0]);
				String[] forksList = words[1].split(" ");
		//		outL.write(id+",");
				sum = 0;
				int num = 0;
				if(forksList.length==0){
				//	outL.write("\n");
				//	continue;
				}else{
					for(String s:forksList){
						Integer fork = Integer.valueOf(s);//fork的项目
						Integer pull_request_num = repoPull_requestNumMap.get(fork);
						if(pull_request_num==null||pull_request_num==0){//该fork没有pull_request
						//	pull_request_num  = 0;
						}else{
							num++;
						}
					//	sum += pull_request_num;
				//		outL.write(pull_request_num+" ");
					}
				//	outL.write("\n");
				}
				outN.write(id+","+forksList.length+","+num+"\n");//repo,forksNum,forksWithPR_Num
			}
			outN.close();
		//	outL.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public void userForkedNum(String userType){
		try{
			String line = "";
			int i=0;
			System.out.println("repoForkedNum");
			BufferedReader rRW = BF.readFile(forksFile.getParent()+"\\features\\repoForkedPRedNum.csv");
			String path = forksFile.getParent()+"\\features\\";
			TreeMap<Integer,Integer> rfnumMap = new TreeMap<Integer,Integer>();
			while((line=rRW.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if((i/100000)%25==0)
						System.out.println(" ");
				}
				String[]words = line.split(",");
				if(words[0].contains("ID")){
					continue;
				}
				int rid = Integer.valueOf(words[0]);

			//	System.out.print(uid+" - ");
				int num = Integer.valueOf(words[2]);
				rfnumMap.put(rid, num);
 			}
			System.out.println(i+" ");
			System.out.println("rwnum: "+rfnumMap.size());
//			BufferedWriter outN = new BufferedWriter(
//					new FileWriter(Constants.screenFile+"features\\usersForkedSumAveMaxNum.txt"));
			File urFile = new File(path+userType+"\\userReposList.csv");
			BufferedReader rUR = BF.readFile(urFile);
			BufferedWriter outSum = new BufferedWriter(
					new FileWriter(urFile.getParent()+"\\userForkedPRedSumNum.csv"));
			BufferedWriter outMax = new BufferedWriter(
					new FileWriter(urFile.getParent()+"\\userForkedPRedMaxNum.csv"));
			BufferedWriter outAve = new BufferedWriter(
					new FileWriter(urFile.getParent()+"\\userForkedPRedAveNum.csv"));
			BufferedWriter outList = new BufferedWriter(
					new FileWriter(urFile.getParent()+"\\userForkedPRedNumList.csv"));
			BufferedWriter outH = new BufferedWriter(
					new FileWriter(urFile.getParent()+"\\userForkedPRedHindex.csv"));
			
			i=0;
			System.out.println("userReposList");
		//	TreeMap<Integer,ArrayList<Integer>> uwlist = new TreeMap<Integer,ArrayList<Integer>>();
			while((line=rUR.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if((i/100000)%25==0)
						System.out.println(" ");
				//	outN.flush();
				}
				String[]words = line.split(",");
				int uid = Integer.valueOf(words[0]);
				int sumNum = 0,maxNum = 0;
				ArrayList<Integer> list = new ArrayList<Integer>();
				String[] reposid = words[1].split(" ");
				for(String rid:reposid){
					Integer repoid = Integer.valueOf(rid);
					Integer num = rfnumMap.get(repoid);
					if(num==null)
						num=0;
					list.add(num);
					sumNum += num;
					if(maxNum<num){
						maxNum = num;
					}
				}
				double aveNum = (double)sumNum/reposid.length;
		//		outN.write(uid+","+sumNum+","+aveNum+","+maxNum+"\n");
				outSum.write(uid+","+sumNum+"\n");
				outMax.write(uid+","+maxNum+"\n");
				outAve.write(uid+","+aveNum+"\n");
			//	break;
				int h_index = 0;
		//		Integer uid = Integer.valueOf(words[0]);
				
				//排序
	 			Collections.sort(list,new Comparator<Integer>(){
	 				public int compare(Integer i1,Integer i2){
	 					return i2.compareTo(i1);
	 				}
	 			});
				
				outList.write(words[0]);
				for(int j=1;j<list.size()+1;j++){
					int num = list.get(j-1);
					outList.write(","+num);
				}
				outList.write("\n");
				for(int j=1;j<list.size()+1;j++){
					int num = list.get(j-1);
					if(j>num){
						h_index = j-1;
						break;
					}
				}
				outH.write(words[0]+","+h_index+"\n");
 			}
			System.out.println("users total num is "+i);
			outSum.close();
			outMax.close();
			outAve.close();
			outList.close(); 
			outH.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void forkedCommit(){
		try{
			String line = "";
			int i=0;
			BufferedReader rRW = BF.readFile(Constants.screenFile+"tables\\projectsFork.csv");
			TreeMap<Integer,Integer> rfnumMap = new TreeMap<Integer,Integer>();
			while((line=rRW.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if((i/100000)%25==0)
						System.out.println(" ");
				}
				String[]words = line.split(",");
				int rid = Integer.valueOf(words[0]);
				rfnumMap.put(rid, 0);
 			}
			System.out.println(i+" ");
			System.out.println("rwnum: "+rfnumMap.size());
			BufferedWriter out = new BufferedWriter(
					new FileWriter(Constants.screenFile+"features\\projectsForkedWithCommit.csv"));
			BufferedWriter out2 = new BufferedWriter(
					new FileWriter(Constants.screenFile+"features\\projectsForkedWithoutCommit.csv"));
			BufferedReader rUR = BF.readFile(Constants.screenFile+"features\\repoCommitsNum.csv");
			i=0;
			while((line=rUR.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if((i/100000)%25==0)
						System.out.println(" ");
					out.flush();
				}
				String[]words = line.split(",");
				Integer rid = Integer.valueOf(words[0]);
				Integer num = Integer.valueOf(words[1]);
				if(rfnumMap.get(rid)==null){//当前项目不是fork来的项目
					continue;
				}
				if(num==0){//没有commit行为
					out2.write(rid+"\n");
				}else{
					out.write(rid+"\n");
				}
 			}
			System.out.println("users total num is "+i);
			out.close();
			out2.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void forkedIssue(){
		try{
			String line = "";
			int i=0;
			BufferedReader rRW = BF.readFile(Constants.screenFile+"tables\\projectsFork.csv");
			TreeMap<Integer,Integer> rfnumMap = new TreeMap<Integer,Integer>();
			while((line=rRW.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if((i/100000)%25==0)
						System.out.println(" ");
				}
				String[]words = line.split(",");
				int rid = Integer.valueOf(words[0]);
				rfnumMap.put(rid, 0);
 			}
			System.out.println(i+" ");
			System.out.println("rwnum: "+rfnumMap.size());
			BufferedWriter out = new BufferedWriter(
					new FileWriter(Constants.screenFile+"features\\projectsForkedWithCommit.csv"));
			BufferedWriter out2 = new BufferedWriter(
					new FileWriter(Constants.screenFile+"features\\projectsForkedWithoutCommit.csv"));
			BufferedReader rUR = BF.readFile(Constants.screenFile+"features\\repoCommitsNum.csv");
			i=0;
			while((line=rUR.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if((i/100000)%25==0)
						System.out.println(" ");
					out.flush();
				}
				String[]words = line.split(",");
				Integer rid = Integer.valueOf(words[0]);
				Integer num = Integer.valueOf(words[1]);
				if(rfnumMap.get(rid)==null){//当前项目不是fork来的项目
					continue;
				}
				if(num==0){//没有commit行为
					out2.write(rid+"\n");
				}else{
					out.write(rid+"\n");
				}
 			}
			System.out.println("users total num is "+i);
			out.close();
			out2.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void forkAna(){//分析有多少repo是fork来的，多少fork之后提交了pull_request，其中多少被merged
		/*
		 * 1.用户，forksrepoNum
		 * 2.uid,forkRepoList
		 * 3.uid,forkRepoPull_requestNumList
		 * 4.uid,forkRepoPull_requestMergedNumList
		 * 5.uid,forkRepoNum,hasPull_requestRepoNum,hasMergedPull_requestNum
		 * 
		 */
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Forks f = new Forks();
		long start = System.currentTimeMillis();
	//	f.getReposForkNet();
	//	f.repoForkedNET();
		f.userForkedNum("USR");
	//	f.userForkedNum("ORG");
	//	f.repoForkedPRedNum();
	//	f.forkedCommit();
	//	f.repoForkedNET();
	//	f.repoForks_Pull_requestNum();
	//	f.repoForksWithPull_requestNum();
		long end = System.currentTimeMillis();
		System.out.println("Time : "+(end-start)/1000);
	}

}
