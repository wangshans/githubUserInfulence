package wss.rank;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import utils.BF;

public class TUR3 {

	/**
	 * @param args
	 */
	/**
	 */
	String path = "E:\\mysql-2015-09-25\\screen\\";
	double max = 0.000000001;
	//参数设置
	double pFollow = 0.2;//Follow关系权重
	double pFollowed = 0;
	double pOwn = 0.8;//own关系权重
	double pOwned = 0.6;
	double pFork = 0.4;
	double pForked = 0.2;
	public static Map<Integer,Double > userScoreMap = new HashMap<Integer,Double>();//用户评分
    public static Map<Integer,Double > repoScoreMap = new HashMap<Integer,Double>();//repo及其评分
    public static TreeMap<Integer,ArrayList<Integer> > userFollowersListMap = new TreeMap<Integer,ArrayList<Integer> >();//用户以及其粉丝列表
    public Map<Integer,Integer > userFollowingsNumMap = new HashMap<Integer,Integer>();//用户及其追随者数量
    public static TreeMap<Integer,ArrayList<Integer> > userReposListMap = new TreeMap<Integer,ArrayList<Integer> >();//用户及其拥有的项目列表
   // public static TreeMap<Integer,ArrayList<Integer> > userReposListMap2 = new TreeMap<Integer,ArrayList<Integer> >();//用户及其拥有的项目列表

    public static Map<Integer,Integer > userReposNumMap = new HashMap<Integer,Integer>();//用户及其拥有的项目数量
    public static Map<Integer,Integer > ownNetMap = new HashMap<Integer,Integer>();//用户及其拥有的项目数量
    public static Map<Integer,Integer > forkNetMap = new HashMap<Integer,Integer>();//用户及其拥有的项目数量
    public static TreeMap<Integer,ArrayList<Integer> > repoForksListMap = new TreeMap<Integer,ArrayList<Integer> >();//项目以及它的Fork的列表
    public static TreeMap<Integer,Integer> repoForksNumMap = new TreeMap<Integer,Integer>();//项目以及它的Fork的列表

    //初始化,所有点的得分为1
    public void init(){
        try{
        	System.out.println("init---------");
        	String line = null;
    		int i=0;
	    	File usersFP = new File(path+"tables\\users.csv");
	    	BufferedReader reader = BF.readFile(usersFP,"utf-8");
			while((line=reader.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%25==0){
						System.out.println();
					}
				}
				String [] words = line.split(",");
				int uid = Integer.valueOf(words[0]);;
				userScoreMap.put(uid, 1.0);//初始化为1.0
			}
			System.out.println("\nuserScoreMap: "+userScoreMap.size());
		//repos
			i=0;
			usersFP = new File(path+"tables\\projects.csv");
	    	reader = BF.readFile(usersFP,"utf-8");
			while((line=reader.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%25==0){
						System.out.println();
					}
				}
				String [] words = line.split(",");
				int id = Integer.valueOf(words[0]);;
				repoScoreMap.put(id, 1.0);//初始化为1.0
			}
			System.out.println("\nrepoScoreMap: "+repoScoreMap.size());
        }catch(Exception e){
        	e.printStackTrace();
        }
    }
    public void init2(){
        try{
        	System.out.println("init2---------");
        	System.out.print("\tuserTURScore ");
        	String line = null;
    		int i=0;
	    	File usersFP = new File(path+"tables\\users.csv");
			BufferedWriter out = new BufferedWriter(new FileWriter(path+"userTURScore.csv"));
	    	BufferedReader reader = BF.readFile(usersFP,"utf-8");
			while((line=reader.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%25==0){
						System.out.println();
					}
				}
				String [] words = line.split(",");
				int uid = Integer.valueOf(words[0]);;
				out.write(uid+","+1+"\n");//初始化为1.0
			}
			System.out.println(i);
			
			out.close();
		//repos
			System.out.print("\trepoTURScore ");
			BufferedWriter out2 = new BufferedWriter(new FileWriter(path+"repoTURScore.csv"));
			i=0;
			usersFP = new File(path+"tables\\projects.csv");
	    	reader = BF.readFile(usersFP,"utf-8");
			while((line=reader.readLine())!=null){
				i++;
				if(i%1000000==0){
					System.out.print(i/1000000+" ");
					if(i/1000000%25==0){
						System.out.println();
					}
				}
				String [] words = line.split(",");
				int id = Integer.valueOf(words[0]);
				out2.write(id+","+1+"\n");
			}
			out2.close();
			System.out.println(i);
        }catch(Exception e){
        	e.printStackTrace();
        }
    }
    public void getUserFollowersListMap(){//读取网络关系到Map
    	try{
    		userFollowersListMap.clear();
    		System.out.print("userFollowersMap: ");
    		String line = null;
    		int i=0;
    		File usersFP = new File(path+"features\\allfeatures\\userFollowersList.csv");
	    	BufferedReader reader = BF.readFile(usersFP,"utf8");
			while((line=reader.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%25==0){
						System.out.println();
					}
				}
				String [] words = line.split(",");
				int uid = Integer.valueOf(words[0]);
				if(words.length==1){
					userFollowersListMap.put(uid,null);
				}else{
					ArrayList<Integer> list = new ArrayList<Integer>();
					String[] fersS = words[1].split(" ");
					
						
					for(String s:words[1].split(" ")){
						list.add(Integer.valueOf(s));
					}
					userFollowersListMap.put(uid,list );
				}
				
			}
			System.out.println(userFollowersListMap.size());
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void getUserFollowingsNumMap(){//读取网络关系到Map
    	try{
    		userFollowingsNumMap.clear();
    		System.out.print("userFollowingsNumMap: ");
    		String line = null;
    		int i=0;
    		File usersFP = new File(path+"features\\allfeatures\\userFollowingsNum.csv");
	    	BufferedReader reader = BF.readFile(usersFP,"utf8");
			while((line=reader.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%25==0){
						System.out.println();
					}
				}
				String [] words = line.split(",");
				int uid = Integer.valueOf(words[0]);
				int num = Integer.valueOf(words[1]);
				userFollowingsNumMap.put(uid,num );
				
			}
			System.out.println(userFollowingsNumMap.size());
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void getUserReposListMap(){//读取网络关系到Map
    	System.out.print("userReposListMap: ");
    	try{
    		userReposListMap.clear();
    		String line = null;
    		int i=0;
    		File usersFP = new File(path+"features\\allfeatures\\userReposList.csv");
	    	BufferedReader reader = BF.readFile(usersFP,"utf8");
			while((line=reader.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%25==0){
						System.out.println();
					}
				}
				String [] words = line.split(",");
				int uid = Integer.valueOf(words[0]);
				if(words.length==1){
					userReposListMap.put(uid,null);
				}else{
					ArrayList<Integer> list = new ArrayList<Integer>();
					for(String s:words[1].split(" ")){
						list.add(Integer.valueOf(s));
					}
					userReposListMap.put(uid,list );
				}
			
			}
			System.out.println(userReposListMap.size());
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void getRepoForksListMap(){//读取网络关系到Map
    	try{
    		repoForksListMap.clear();
    		System.out.print("repoForksListMap: ");
    		String line = null;
    		int i=0;
    		File usersFP = new File(path+"features\\allfeatures\\repoForksList0.csv");
	    	BufferedReader reader = BF.readFile(usersFP,"utf8");
			while((line=reader.readLine())!=null){
				i++;
				if(i%1000000==0){
					System.out.print(i/1000000+" ");
					if(i/1000000%25==0){
						System.out.println();
					}
				}
				String [] words = line.split(",");
				int uid = Integer.valueOf(words[0]);
				if(words.length==1){
					repoForksListMap.put(uid,null);
				}else{
					ArrayList<Integer> list = new ArrayList<Integer>();
					String[] fersS = words[1].split(" ");
					
						
					for(String s:words[1].split(" ")){
						list.add(Integer.valueOf(s));
					}
					repoForksListMap.put(uid,list );
				}
				
			}
			System.out.println(repoForksListMap.size());
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void getRepoForksNumMap(){//读取网络关系到Map
    	try{
    		repoForksNumMap.clear();
    		System.out.print("repoForksNumMap: ");
    		String line = null;
    		int i=0;
    		File usersFP = new File(path+"features\\allfeatures\\repoForksNum0.csv");
	    	BufferedReader reader = BF.readFile(usersFP,"utf8");
			while((line=reader.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%25==0){
						System.out.println();
					}
				}
				String [] words = line.split(",");
				int uid = Integer.valueOf(words[0]);
				repoForksNumMap.put(uid,Integer.valueOf(words[1]));
			}
			System.out.println(repoForksNumMap.size());
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void getUserReposNumMap(){//读取网络关系到Map
    	try{
    		userReposNumMap.clear();
    		System.out.print("userReposNumMap: ");
    		String line = null;
    		int i=0;
    		File usersFP = new File(path+"features\\allfeatures\\userReposNum.csv");
	    	BufferedReader reader = BF.readFile(usersFP,"utf8");
			while((line=reader.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%25==0){
						System.out.println();
					}
				}
				String [] words = line.split(",");
				int uid = Integer.valueOf(words[0]);
				int num = Integer.valueOf(words[1]);
				userReposNumMap.put(uid,num );
				
			}
			System.out.println(userReposNumMap.size());
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void getForkNetMap(){//读取网络关系到Map
    	try{
    		forkNetMap.clear();
    		System.out.print("getForkNetMap ");
    		String line = null;
    		int i=0;
    		File usersFP = new File(path+"features\\allfeatures\\forksNet0.csv");
	    	BufferedReader reader = BF.readFile(usersFP,"utf8");
			while((line=reader.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
//					if(i/100000%25==0){
//						System.out.println();
//					}
				}
				String [] words = line.split(",");
				int rid = Integer.valueOf(words[0]);
				int ownerid = Integer.valueOf(words[1]);
				forkNetMap.put(rid,ownerid);
				
			}
			System.out.println(forkNetMap.size());
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void getOwnNetMap(int k){//读取网络关系到Map
    	try{
    		ownNetMap.clear();
    	//	System.out.print("ownNetMap: ");
    		String line = null;
    		int i=0;
    		File usersFP = new File(path+"features\\ownNet\\ownNet"+k+".csv");
	    	BufferedReader reader = BF.readFile(usersFP,"utf8");
	   // 	long start = System.currentTimeMillis();
			while((line=reader.readLine())!=null){
				i++;
				if(i%1000000==0){
					System.out.print(i/1000000+" ");
					if(i/1000000%25==0){
						System.out.println();
					}
				}
				String [] words = line.split(",");
				int rid = Integer.valueOf(words[0]);
				int ownerid = Integer.valueOf(words[1]);
				ownNetMap.put(rid,ownerid);
			}
		//	System.out.println(ownNetMap.size());
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void getUserScoreMap(){
    	try{
    		userScoreMap.clear();
    		System.out.print("getUserScoreMap: ");
    		String line="";
    		int i=0;
    		File us = new File(path+"userTURScore.csv");
    		BufferedReader r = BF.readFile(us);
    		while((line = r.readLine())!=null){
    			i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%25==0){
						System.out.println();
					}
				}
    			String[] words = line.split(",");
    			userScoreMap.put(Integer.valueOf(words[0]), Double.valueOf(words[1]));
    		}
    		r.close();
    		System.out.println(userScoreMap.size());
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void getRepoScoreMap(){
    	try{
    		repoScoreMap.clear();
    		System.out.print("getRepoScoreMap: ");
    		String line="";
    		File us = new File(path+"repoTURScore.csv");
    		BufferedReader r = BF.readFile(us);
    		int i=0;
    		while((line = r.readLine())!=null){
    			i++;
    			if(i%1000000==0){
    				System.out.print(i/1000000+" ");
    				if(i/1000000%25==0){
    					System.out.println();
    				}
    			}
    			String[] words = line.split(",");
    			repoScoreMap.put(Integer.valueOf(words[0]), Double.valueOf(words[1]));
    		}
    		r.close();
    		System.out.println(repoScoreMap.size());
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void getRepoScoreMap(int n){//分成两部分读
    	try{
    		repoScoreMap.clear();
    		System.out.print("getRepoScoreMap: ");
    		String line="";
    		File us = new File(path+"repoTURScore.csv");
    		BufferedReader r = BF.readFile(us);
    		int start =0;
    		if(n!=0){
    			start=6000000;
    		}
    		int i=0;
    	//	System.out.print("start : "+start +" ");
    		while((line = r.readLine())!=null){
    			i++;
    			if(i<start){
    				continue;
    			}
    				
    			if(i>start+6000000){
    				break;
    			}
    			if(i%1000000==0){
    				System.out.print(i/1000000+" ");
    				if(i/1000000%25==0){
    					System.out.println();
    				}
    			}
    			String[] words = line.split(",");
    			repoScoreMap.put(Integer.valueOf(words[0]), Double.valueOf(words[1]));
    		}
    		r.close();
    		System.out.println(repoScoreMap.size());
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void getInitUserScoreMap(){
    	try{
    		userScoreMap.clear();
    		System.out.print("getUserScoreMap: ");
    		String line="";
    		int i=0;
    		File us = new File(path+"inituserTURScore.csv");
    		BufferedReader r = BF.readFile(us);
    		while((line = r.readLine())!=null){
    			i++;
    			if(i%100000==0){
    				System.out.print(i/100000+" ");
    				if(i/100000%25==0){
    					System.out.println();
    				}
    			}
    			String[] words = line.split(",");
    			userScoreMap.put(Integer.valueOf(words[0]), Double.valueOf(words[1]));
    		}
    		r.close();
    		System.out.println(userScoreMap.size());
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void getInitRepoScoreMap(){
    	try{
    		repoScoreMap.clear();
    		System.out.print("getRepoScoreMap: ");
    		String line="";
    		int i=0;
    		File us = new File(path+"initrepoTURScore.csv");
    		BufferedReader r = BF.readFile(us);
    		while((line = r.readLine())!=null){
    			i++;
    			if(i%1000000==0){
    				System.out.print(i/1000000+" ");
    				if(i/1000000%25==0){
    					System.out.println();
    				}
    			}
    			String[] words = line.split(",");
    			repoScoreMap.put(Integer.valueOf(words[0]), Double.valueOf(words[1]));
    		}
    		r.close();
    		System.out.println(repoScoreMap.size());
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void MapClear(){
    	userFollowersListMap.clear();//用户以及其粉丝列表
        userFollowingsNumMap.clear();//用户及其追随者数量
        userReposListMap.clear();//用户及其拥有的项目列表
        userReposNumMap.clear();//用户及其拥有的项目数量
        ownNetMap.clear();//用户及其拥有的项目数量
        forkNetMap.clear();//用户及其拥有的项目数量
        repoForksListMap.clear();
        userScoreMap.clear();
        repoScoreMap.clear();
    }
    public void doTURbyFile(){//
    	try{
    		System.out.println("doTURbyFile---------");
    	//	String line="";
    		int i=0;
//    		Connection conn = BF.conn();
//    		//    conn.setAutoCommit(false); // 设置手动提交  
//    		    Statement stmt = conn.createStatement();  
//    	
			getUserFollowersListMap();
    		getUserFollowingsNumMap();
    		System.out.println("user follow ---------------");
    		for(Map.Entry<Integer, Double> m:userScoreMap.entrySet()){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%25==0){
						System.out.println();
					}
				}
				int userID = m.getKey();
    			double s=0.0;
    			//follow
    			ArrayList<Integer> fList = userFollowersListMap.get(userID);
    			if(fList!=null){
	    			for(Integer f:fList){
	    				int outD = userFollowingsNumMap.get(f);
	    				double sF= userScoreMap.get(f); //上一轮f节点的评分
	    				s+= pFollow*sF/outD;
	    			}
    			}
    			//followed
    			//贡献参数设为0，所以忽略
    			userScoreMap.put(userID, s);
			}
	        MapClear();
	      //user own
			
			getUserReposListMap();
			System.out.println("\nuser own ---------------");
			i=0;
			for(Map.Entry<Integer, Double> m:userScoreMap.entrySet()){
    			i++;
    			if(i%100000==0){
    				System.out.print(i/100000+" ");
    				if(i/100000%25==0){
    					System.out.println();
    				}
    			}
    			int userID = m.getKey();
    			double s = userScoreMap.get(userID);
    			//own
    			ArrayList<Integer> rList =  new ArrayList<Integer>();
    			
    			rList = userReposListMap.get(userID);
    			
    			if(rList!=null){
	    			for(Integer r:rList){
	    			//	int outD = rList.size();
	    				double sR=repoScoreMap.get(r);
	    				int outD =1;
						s += pOwn*sR/outD;
	    			}
    			}
    			userScoreMap.put(userID, s);
    		//	System.out.println(userID+" "+s);
	    	}
	        MapClear();
			BufferedWriter out = new BufferedWriter(new FileWriter(path+"userTURScore.csv"));
			for(Map.Entry<Integer, Double> m:userScoreMap.entrySet()){
				out.write(m.getKey()+","+m.getValue()+"\n");
			}
			out.close();
    		//repo
	        System.out.println("repo owned---------------");
	        getUserReposNumMap();
	        int count=0;
    		BufferedWriter out2 = new BufferedWriter(new FileWriter(path+"repoTURScore2.csv",true));
	        for(int k =0;k<15;k++){
	        	long start = System.currentTimeMillis();
		        getOwnNetMap(k);
	    		i=0;
//	    		for(Map.Entry<Integer, Double> m:repoScoreMap.entrySet()){
	    		for(Map.Entry<Integer,Integer> m: ownNetMap.entrySet()){
					i++;
//					if(i%100000==0){
//						System.out.print(i/100000+" ");
//						if(i/100000%25==0){
//							System.out.println();
//						}
//					}
		    	    int repoID = m.getKey();	
	    			double s = 0.0;
	    			//owned
	    			
    				Integer ownerID = m.getValue();
//    				if(ownerID==null){
//    				//	System.out.println(ownerID+" "+repoID);
//    					continue;
//    				}
    				Double sOwner=userScoreMap.get(ownerID);
    			//	System.out.println(sOwner);
    				count++;
    				int outD = userReposNumMap.get(ownerID);
    				s += pOwned * sOwner/outD;
    			//	System.out.println(repoID+" "+sOwner+" "+s);
	    			repoScoreMap.remove(repoID);
	    			out2.write(repoID+","+s+"\n");
	    			
				}
				
	    		long end = System.currentTimeMillis();
				System.out.println("readTxt2方法，使用内存="
						+(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())
						+",使用时间毫秒="+(end-start)); 
	    	//	break;
	       }
	        out2.close();
			MapClear();
			System.out.println("\ncount ------"+count);
			System.out.println("\nrepo fork ---------------");
//			getRepoForksListMap();
//    		getForkNetMap();
//    		i=0;
//    		for(Map.Entry<Integer, Double> m:repoScoreMap.entrySet()){
//				i++;
//				if(i%100000==0){
//					System.out.print(i/100000+" ");
//					if(i/100000%25==0){
//						System.out.println();
//					}
//				}
//	    	    int repoID = m.getKey();	
//				double s=repoScoreMap.get(repoID);
//    			//forks
//    			ArrayList<Integer> forksList = repoForksListMap.get(repoID);
//    			if(forksList!=null){
//    			
//	    			for(Integer fork:forksList){
//	    				int outD =1;
//	    				double sFork=repoScoreMap.get(fork);
//	    				s += pFork*sFork/outD;
//	    			}
//    			}
//    			//forked
//    			{
//    				Integer forkedFromID = forkNetMap.get(repoID);
//    				 double sForkedFrom=repoScoreMap.get(forkedFromID);
//    				int outD = repoForksListMap.get(forkedFromID).size();
//    				s += pForked *sForkedFrom/outD;
//    			}
//    		//	repoScoreMap.put(repoID,s);	
//    			System.out.println(repoID+" "+s);
//    			repoScoreMap.put(repoID,s);
//    		}
			MapClear();
		
//			BufferedWriter out2 = new BufferedWriter(new FileWriter(path+"repoTURScore.csv"));
//			for(Map.Entry<Integer, Double> m:repoScoreMap.entrySet()){
//				out2.write(m.getKey()+","+m.getValue()+"\n");
//			}
//			out2.close();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void userFollow(){
    	try{
    		System.out.println("user follow ---------------");
    		String line="";
    		int i=0;
    		System.out.print("\t");
    		getUserFollowersListMap();
    		System.out.print("\t");
    		getUserFollowingsNumMap();
    		System.out.print("\t");
    		getInitUserScoreMap();
    		System.out.print("\t");
			BufferedWriter out = new BufferedWriter(
					new FileWriter(path+"userTURScore.csv"));
    		for(Map.Entry<Integer, Double> m:userScoreMap.entrySet()){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%25==0){
						System.out.println();
					}
					out.flush();
				}
				int userID = m.getKey();
    			double s=0.0;
    			//follow
    			ArrayList<Integer> fList = userFollowersListMap.get(userID);
    			if(fList!=null){
	    			for(Integer f:fList){
	    				int outD = userFollowingsNumMap.get(f);
	    				double sF= userScoreMap.get(f); //上一轮f节点的评分
	    				s+= pFollow*sF/outD;
	    			}
    			}
    			//followed
    			//贡献参数设为0，所以忽略
    		//	userScoreMap.put(userID, s);
    			out.write(userID+","+s+"\n");
			}
    		System.out.println();
    		out.close();
	        MapClear();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void userFollowed(){
    	
    }
   
    public void userOwn0(){
    	try{
    		System.out.println("user own ---------------");
    		System.out.print("\t");
    		getUserReposListMap();
    		System.out.print("\t");
    		getUserScoreMap();
    		System.out.print("\t");
    		getRepoScoreMap();
    		System.out.print("\t");
    		int i=0;
			BufferedWriter out = new BufferedWriter(new FileWriter(path+"userTURScore.csv"));
			for(Map.Entry<Integer, Double> m:userScoreMap.entrySet()){
    			i++;
    			if(i%100000==0){
    				System.out.print(i/100000+" ");
    				if(i/100000%25==0){
    					System.out.println();
    				}
    			}
    			int userID = m.getKey();
    			double s = userScoreMap.get(userID);
    			//own
    			ArrayList<Integer> rList  = userReposListMap.get(userID);
    			
    			if(rList!=null){
	    			for(Integer r:rList){
	    			//	int outD = rList.size();
	    				double sR=repoScoreMap.get(r);
	    				int outD =1;
						s += pOwn*sR/outD;
	    			}
    			}
    			out.write(userID+","+s+"\n");
    		//	userScoreMap.put(userID, s);
    		//	System.out.println(userID+" "+s);
	    	}
			System.out.println();
			out.close();
	        MapClear();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void userOwn(){
    	try{
    		System.out.println("user own ---------------");
    		System.out.print("\t");
    		getUserReposListMap();
    		System.out.print("\t");
    		getUserScoreMap();
			for(int k=0;k<2;k++){
				int i=0;
				BufferedWriter out = new BufferedWriter(new FileWriter(path+"userTURScore.csv"));
				System.out.println("\t"+k+"----------");
				System.out.print("\t");
				getRepoScoreMap(k);
				System.out.print("\t");
				for(Map.Entry<Integer, Double> m:userScoreMap.entrySet()){
	    			i++;
	    			if(i%100000==0){
	    				System.out.print(i/100000+" ");
	    				if(i/100000%25==0){
	    					System.out.println();
	    				}
	    				out.flush();
	    			}
	    			int userID = m.getKey();
	    			double s = userScoreMap.get(userID);
	    			//own
	    			ArrayList<Integer> rList  = userReposListMap.get(userID);
	    			
	    			if(rList!=null){
		    			for(Integer r:rList){
		    			//	int outD = rList.size();
		    				Double sR=repoScoreMap.get(r);
		    				if(sR==null){
		    					continue;
		    				}
		    				int outD =1;
							s += pOwn*sR/outD;
		    			}
	    			}
	    			out.write(userID+","+s+"\n");
	    		//	userScoreMap.put(userID, s);
	    		//	System.out.println(userID+" "+s);
		    	}
				System.out.println();
				out.close();
			}
	        MapClear();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void repoOwned(){
    	try{
    		//repo
	        System.out.println("repo owned---------------");
	        System.out.print("\t");
	        getUserReposNumMap();
	        System.out.print("\t");
	        getInitUserScoreMap();
	    	System.out.print("\t");
    		BufferedWriter out2 = new BufferedWriter(new FileWriter(path+"repoTURScore.csv"));
    		for(int k =0;k<15;k++){
	        	System.out.print(k+" ");
//	        	long start = System.currentTimeMillis();
		        getOwnNetMap(k);
	    		int i=0;
	    		for(Map.Entry<Integer,Integer> m: ownNetMap.entrySet()){
					i++;
		    	    int repoID = m.getKey();	
	    			double s = 0.0;
	    			//owned
    				Integer ownerID = m.getValue();
    				Double sOwner=userScoreMap.get(ownerID);
    				int outD = userReposNumMap.get(ownerID);
    				s += pOwned * sOwner/outD;
    			//	System.out.println(repoID+" "+sOwner+" "+s);
	    		//	repoScoreMap.remove(repoID);
	    			out2.write(repoID+","+s+"\n");
				}
//	    		long end = System.currentTimeMillis();
//				System.out.println("readTxt2方法，使用内存="
//						+(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1000
//						+",使用时间毫秒="+(end-start)); 
	    	//	break;
	    		ownNetMap.clear();
	       }
    		System.out.println();
	        out2.close();
			MapClear();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void repoFork(){
    	try{
    		System.out.println("repo fork ---------------");
    		System.out.print("\t");
    		getRepoScoreMap();
    		System.out.print("\t");
    		getRepoForksListMap();
    		System.out.print("\t");
    		int i=0;
    		BufferedWriter out = new BufferedWriter(new FileWriter(path+"repoTURScore.csv"));
    		for(Map.Entry<Integer, Double> m:repoScoreMap.entrySet()){
				i++;
				if(i%1000000==0){
					System.out.print(i/1000000+" ");
					if(i/1000000%25==0){
						System.out.println();
					}
					out.flush();
				}
	    	    int repoID = m.getKey();	
				double s=repoScoreMap.get(repoID);
    			//forks
    			ArrayList<Integer> forksList = repoForksListMap.get(repoID);
    			if(forksList!=null){
	    			for(Integer fork:forksList){
	    				int outD =1;
	    				double sFork=repoScoreMap.get(fork);
	    				s += pFork*sFork/outD;
	    			}
    			}
    			out.write(repoID+","+s+"\n");
    		}
    		System.out.println();
    		out.close();
    		MapClear();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void repoForked(){
    	try{
    		System.out.println("repo forked ---------------");
    		System.out.print("\t");
    		getRepoScoreMap();
    		System.out.print("\t");
    		getForkNetMap();
    		System.out.print("\t");
    		getRepoForksNumMap();
    		System.out.print("\t");
    		int i=0;
    		BufferedWriter out = new BufferedWriter(new FileWriter(path+"repoTURScore.csv"));
    		for(Map.Entry<Integer, Double> m:repoScoreMap.entrySet()){
    //		for(Map.Entry<Integer, Integer> m:forkNetMap.entrySet()){
				i++;
				if(i%1000000==0){
					System.out.print(i/1000000+" ");
					out.flush();
				}
	    	    int repoID = m.getKey();	
				Double s = repoScoreMap.get(repoID);
				Integer forkedFromID = forkNetMap.get(repoID);
				if(forkedFromID!=null){
					Double sForkedFrom = repoScoreMap.get(forkedFromID);
					//		System.out.println(repoID+" "+s+" "+forkedFromID+" "+sForkedFrom+" ");
							int outD = repoForksNumMap.get(forkedFromID);
							s += pForked *sForkedFrom/outD;
				}
    			out.write(repoID+","+s+"\n");
    		}
    		System.out.println();
    		out.close();
    		MapClear();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void fileCopy0(File s) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try{
            fi = new FileInputStream(s);
            File t = new File(s.getParent()+"\\init"+s.getName());
            fo = new FileOutputStream(t);
            in = fi.getChannel();//得到对应的文件通道
            out = fo.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void fileCopy(File u) {
        try{

        	BufferedReader r = BF.readFile(u);
    		BufferedWriter out = new BufferedWriter(
    				new FileWriter(u.getParent()+"\\init"+u.getName()));
    		String line = "";
    		while((line=r.readLine())!=null){
    			out.write(line+"\n");
    		}
    		out.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    //	public void copyFile(){
	public void FileCopy(){
		System.out.println("FileCopy---------");
		File u = new File(path+"userTURScore.csv");
		fileCopy(u);
		File r = new File(path+"repoTURScore.csv");
		fileCopy(r);
	}
    public void doTUR(){
    	userFollow();
    	userOwn();
    	repoOwned();
    	repoFork();
    	repoForked();
    }
    public boolean checkMax(){
    	try{
    		boolean flag = false;
    	//	System.out.print("getRepoScoreMap: ");
    		String line="";
    		Map<Integer,Double > userSMap = new HashMap<Integer,Double>();//用户评分
    		File initus = new File(path+"inituserTURScore.csv");
    		BufferedReader r0 = BF.readFile(initus);
    		while((line = r0.readLine())!=null){
    			String[] words = line.split(",");
    			userSMap.put(Integer.valueOf(words[0]), Double.valueOf(words[1]));
    		}
    		r0.close();
    		File us = new File(path+"userTURScore.csv");
    		BufferedReader r1 = BF.readFile(us);
    		while((line = r1.readLine())!=null){
    			String[] words = line.split(",");
    			Integer id = Integer.valueOf(words[0]);
    			Double s = Double.valueOf(words[1]);
    			double inits = userSMap.get(id);
    			double d = Math.abs(s-inits);
    			if(d>max){
    				return false;
    			}
    		}
    		r1.close();
    		userSMap.clear();
    		
    		File initF = new File(path+"initrepoTURScore.csv");
    		r0 = BF.readFile(initus);
    		while((line = r0.readLine())!=null){
    			String[] words = line.split(",");
    			userSMap.put(Integer.valueOf(words[0]), Double.valueOf(words[1]));
    		}
    		r0.close();
    		File f = new File(path+"repoTURScore.csv");
    		r1 = BF.readFile(us);
    		while((line = r1.readLine())!=null){
    			String[] words = line.split(",");
    			Integer id = Integer.valueOf(words[0]);
    			Double s = Double.valueOf(words[1]);
    			double inits = userSMap.get(id);
    			double d = Math.abs(s-inits);
    			if(d>max){
    				return false;
    			}
    		}
    		r1.close();
    		userSMap.clear();
    		System.out.println("all checked!");
    		return true;
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return false;
    }
    public void reTUR(){
    	try{
    		long start0 = System.currentTimeMillis();
    		init2();
    		long end = System.currentTimeMillis();
	    	System.out.println("init 计算用时（秒）："+(end-start0)/1000);
	    	long start = System.currentTimeMillis();
    		int i=0;
    		boolean flag = false;
    		while(!flag){
    			i++;
    			System.out.println(i+"=========================");
    			FileCopy();
    			end = System.currentTimeMillis();
    	    	System.out.println("FileCopy计算用时（秒）："+(end-start)/1000);
    	    	start = end;
        		doTUR();
        		end = System.currentTimeMillis();
    	    	System.out.println("第"+i+"次迭代 计算用时（秒）："+(end-start)/1000);
    	    	start = end;
    	    	flag = checkMax();
    	    	end = System.currentTimeMillis();
    	    	System.out.println("checkMax 计算用时（秒）："+(end-start)/1000);
    	    	start = end;
    	    	MapClear();
    		}
    	//	System.out.println("计算用时（秒）："+(end-start0)/1000);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void splitOwnNetTO3(){
		try{
    		System.out.print("ownNetMap: ");
    		String line = null;
    		int i=0;
    		File usersFP = new File(path+"features\\allfeatures\\ownNet.csv");
	    	BufferedReader reader = BF.readFile(usersFP,"utf-8");
	    	BufferedWriter out1 = new BufferedWriter(new FileWriter(path+"features\\ownNet1.csv"));
	    	BufferedWriter out2 = new BufferedWriter(new FileWriter(path+"features\\ownNet2.csv"));
	    	BufferedWriter out3 = new BufferedWriter(new FileWriter(path+"features\\ownNet3.csv"));
	    	long start = System.currentTimeMillis();
			while((line=reader.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%25==0){
						System.out.println();
					}
					long end = System.currentTimeMillis();
					System.out.println("readTxt2方法，使用内存="
							+(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())
							+",使用时间毫秒="+(end-start)); 
				}
				if(i<3500000){
					out1.write(line+"\n");
				}else if(i<7000000){
					out2.write(line+"\n");
				}else{
					out3.write(line+"\n");
				}
			}
			out1.close();
			out2.close();
			out3.close();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
	}
	public void splitOwnNetTO15(){
		try{
    		System.out.print("ownNetMap: ");
    		String line = null;
    		
    		File usersFP = new File(path+"features\\allfeatures\\ownNet.csv");
	    	BufferedReader reader = BF.readFile(usersFP,"utf-8");
	    	ArrayList<BufferedWriter> blist = new ArrayList<BufferedWriter>();
	    	for(int j=0;j<15;j++){
	    		BufferedWriter out = new BufferedWriter(new FileWriter(path+"features\\ownNet\\ownNet"+j+".csv"));
	    		blist.add(out);
	    	}
	    	int i=0;
	    	int num=1;
			while((line=reader.readLine())!=null){
				i++;
//				if(i%100000==0){
//					System.out.print(i/100000+" ");
//					if(i/100000%25==0){
//						System.out.println();
//					}
//				}
				if(i<num*680000+1){
					blist.get(num-1).write(line+"\n");
				}else{
					System.out.println(i+" "+num);
					num++;
					blist.get(num-1).write(line+"\n");
				}
			}
			System.out.println(i+" "+num);
			for(BufferedWriter o:blist){
				o.close();
			}
			
    	}catch(Exception e){
    		e.printStackTrace();
    	}
	}
	public static void main(String[] args) {
			// TODO Auto-generated method stub
			long start = System.currentTimeMillis();
			TUR2 t= new TUR2();
	//		t.splitOwnNetTO15();
	//		t.init2();
	//		t.doTURbyFile();
	//		t.repoOwned();
	//		t.repoFork();
	//		t.repoForked();
	//		t.doTUR();
	    	t.reTUR();
	//		t.FileCopy();
	//		t.getRepoScoreMap(0);
			long end = System.currentTimeMillis();
			System.out.println("计算用时（秒）："+(end-start)/1000);
		}
}