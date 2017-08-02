package wss.rank;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import utils.BF;

public class TUR {

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
    public static Map<Integer,Integer > userReposNumMap = new HashMap<Integer,Integer>();//用户及其拥有的项目数量
    public static Map<Integer,Integer > ownNetMap = new HashMap<Integer,Integer>();//用户及其拥有的项目数量
    public static Map<Integer,Integer > forkNetMap = new HashMap<Integer,Integer>();//用户及其拥有的项目数量
    public static TreeMap<Integer,ArrayList<Integer> > repoForksListMap = new TreeMap<Integer,ArrayList<Integer> >();//项目以及它的Fork的列表
    //初始化,所有点的得分为1
    public void init(){
        try{
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
    public void initUserScoreToDB(){
		try{
			String line = "";
			
			Connection conn = BF.conn();
		    conn.setAutoCommit(false); // 设置手动提交  
		    int count = 0;  
		  //获取对象
	        Statement stmt = conn.createStatement();  
	        
//	        String sql1 = "drop table github." +tableName;
//		    stmt.executeUpdate(sql1);
//		    System.out.println("删除表！");
		    
		    String sqlstr = "create table github.userTURScore " +
		    		"(id int(255) primary key not null,score double);";
	        stmt.executeUpdate(sqlstr);
	        System.out.println("创建成功！");
		    String insert_sql = "INSERT INTO github.userTURScore VALUES (?,?)"; 
	        System.out.println(insert_sql);
		    PreparedStatement psts = conn.prepareStatement(insert_sql);
		    File table = new File(path+"tables\\users.csv");
			System.out.println(table);
			BufferedReader r1 = BF.readFile(table);
			while((line = r1.readLine())!=null){
				count++;
				String[] words = line.split(",");
				int id = Integer.valueOf(words[0]);
				psts.setInt(1, id);
			    psts.setDouble(2, 1.0);
			    psts.addBatch();          // 加入批量处理  
			    if(count%10000 ==0){
			    	System.out.print(count/10000+" ");
			    	if(count/10000%25==0)
			    		System.out.println();
			       psts.executeBatch(); // 执行批量处理  
			 	   conn.commit();  // 提交  
			    }
			}
			psts.executeBatch(); // 最后不足1万条的数据  
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
    public void initRepoScoreToDB(){
		try{
			String line = "";
			
			Connection conn = BF.conn();
		    conn.setAutoCommit(false); // 设置手动提交  
		    int count = 0;  
		  //获取对象
	        Statement stmt = conn.createStatement();  
	        
	        String sql1 = "drop table github.repoTURScore ";
		    stmt.executeUpdate(sql1);
		    System.out.println("删除表！");
		    
		    String sqlstr = "create table github.repoTURScore " +
		    		"(id int(255) primary key not null,score double);";
	        stmt.executeUpdate(sqlstr);
	        System.out.println("创建成功！");
		    String insert_sql = "INSERT INTO github.repoTURScore VALUES (?,?)"; 
	        System.out.println(insert_sql);
		    PreparedStatement psts = conn.prepareStatement(insert_sql);
		    File table = new File(path+"tables\\projects.csv");
			System.out.println(table);
			BufferedReader r1 = BF.readFile(table);
			while((line = r1.readLine())!=null){
				count++;
				String[] words = line.split(",");
				int id = Integer.valueOf(words[0]);
				psts.setInt(1, id);
			    psts.setDouble(2, 1.0);
			    psts.addBatch();          // 加入批量处理  
			    if(count%100000 ==0){
			    	System.out.print(count/100000+" ");
			    	if(count/100000%25==0)
			    		System.out.println();
			       psts.executeBatch(); // 执行批量处理  
			 	   conn.commit();  // 提交  
			    }
			}
			psts.executeBatch(); // 最后不足1万条的数据  
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
    public void initToDB(){
    	initUserScoreToDB();
    	initRepoScoreToDB();
    }
    public void getUserFollowersListMap(){//读取网络关系到Map
    	try{
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
			System.out.println("\nuserFollowersMap: "+userFollowersListMap.size());
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void getUserFollowingsNumMap(){//读取网络关系到Map
    	try{
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
			System.out.println("\nuserFollowingsNumMap: "+userFollowingsNumMap.size());
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void getUserReposListMap(){//读取网络关系到Map
    	try{
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
					String[] fersS = words[1].split(" ");
					
						
					for(String s:words[1].split(" ")){
						list.add(Integer.valueOf(s));
					}
					userReposListMap.put(uid,list );
				}
				
			}
			System.out.println("\nuserReposListMap: "+userReposListMap.size());
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void getRepoForksListMap(){//读取网络关系到Map
    	try{
    		String line = null;
    		int i=0;
    		File usersFP = new File(path+"features\\allfeatures\\repoForksList.csv");
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
			System.out.println("\nrepoForksListMap: "+repoForksListMap.size());
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void getUserReposNumMap(){//读取网络关系到Map
    	try{
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
			System.out.println("\nuserReposNumMap: "+userReposNumMap.size());
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void getOwnNetMap(){//读取网络关系到Map
    	try{
    		String line = null;
    		int i=0;
    		File usersFP = new File(path+"features\\allfeatures\\ownNet.csv");
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
				int rid = Integer.valueOf(words[0]);
				int ownerid = Integer.valueOf(words[1]);
				ownNetMap.put(rid,ownerid);
				
			}
			System.out.println("\nownNetMap: "+ownNetMap.size());
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void getForkNetMap(){//读取网络关系到Map
    	try{
    		String line = null;
    		int i=0;
    		File usersFP = new File(path+"features\\allfeatures\\forksNet.csv");
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
				int rid = Integer.valueOf(words[0]);
				int ownerid = Integer.valueOf(words[1]);
				forkNetMap.put(rid,ownerid);
				
			}
			System.out.println("\nforkNetMap: "+forkNetMap.size());
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void readDatas(){
    	getUserFollowersListMap();
    	getUserFollowingsNumMap();
    	getUserReposListMap();
    	getUserReposNumMap();
    	getRepoForksListMap();
    	getOwnNetMap();
    }
    public void MapClear(){
    	userFollowersListMap.clear();//用户以及其粉丝列表
        userFollowingsNumMap.clear();//用户及其追随者数量
        userReposListMap.clear();//用户及其拥有的项目列表
        userReposNumMap.clear();//用户及其拥有的项目数量
        ownNetMap.clear();//用户及其拥有的项目数量
        forkNetMap.clear();//用户及其拥有的项目数量
        repoForksListMap.clear();
    }
  
    public String getLine(String tableName,int id){
    	try{
    		String line = null;
    		int i=0;
    		File usersFP = new File(path+"features\\allfeatures\\"+tableName+".csv");
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
				int tempid = Integer.valueOf(words[0]);
				if(tempid==id){
					reader.close();
					return line;
				}
			}
			reader.close();
			System.out.println("\n 没找到 : "+id);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return null;
    }
    public void doTUR2(){//频繁从文件中读取
    	try{
    		//user
    		for(Map.Entry<Integer, Double> m:userScoreMap.entrySet()){
    			int userID = m.getKey();
    			double s=0.0;
    			//follow
    			ArrayList<Integer> fList = userFollowersListMap.get(userID);
    			for(Integer f:fList){
    				int outD = userFollowingsNumMap.get(f);
    				double sF= userScoreMap.get(f);//上一轮f节点的评分
    				s+= pFollow/outD*sF;
    			}
    			//followed
    			//贡献参数设为0，所以忽略
    			//own
    			ArrayList<Integer> rList = userReposListMap.get(userID);
    			for(Integer r:rList){
    			//	int outD = rList.size();
    				double sR = repoScoreMap.get(r);
    				int outD =1;
    				s += pOwn*sR/outD;
    			}
    			userScoreMap.put(userID, s);
    		}
    		//repo
    		for(Map.Entry<Integer, Double> m:repoScoreMap.entrySet()){
    			int repoID = m.getKey();
    			double s = 0.0;
    			//owned
    			{
    				Integer ownerID = ownNetMap.get(repoID);
    				double sOwner = userScoreMap.get(ownerID);
    				int outD = userReposNumMap.get(ownerID);
    				s += pOwned * sOwner/outD;
    			}
    			//forks
    			ArrayList<Integer> forksList = repoForksListMap.get(repoID);
    			for(Integer fork:forksList){
    				int outD =1;
    				double sFork = repoScoreMap.get(fork);
    				s += pFork*sFork/outD;
    			}
    			//forked
    			{
    				Integer forkedFromID = forkNetMap.get(repoID);
    				double sForkedFrom = repoScoreMap.get(forkedFromID);
    				int outD = repoForksListMap.get(forkedFromID).size();
    				s += pForked *sForkedFrom/outD;
    			}
    			repoScoreMap.put(repoID,s);	
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void doTUR(){//需要读的数据太多了，内存溢出
    	try{
    		//user
    		for(Map.Entry<Integer, Double> m:userScoreMap.entrySet()){
    			int userID = m.getKey();
    			double s=0.0;
    			//follow
    			ArrayList<Integer> fList = userFollowersListMap.get(userID);
    			for(Integer f:fList){
    				int outD = userFollowingsNumMap.get(f);
    				double sF= userScoreMap.get(f);//上一轮f节点的评分
    				s+= pFollow/outD*sF;
    			}
    			//followed
    			//贡献参数设为0，所以忽略
    			//own
    			ArrayList<Integer> rList = userReposListMap.get(userID);
    			for(Integer r:rList){
    			//	int outD = rList.size();
    				double sR = repoScoreMap.get(r);
    				int outD =1;
    				s += pOwn*sR/outD;
    			}
    			userScoreMap.put(userID, s);
    		}
    		//repo
    		for(Map.Entry<Integer, Double> m:repoScoreMap.entrySet()){
    			int repoID = m.getKey();
    			double s = 0.0;
    			//owned
    			{
    				Integer ownerID = ownNetMap.get(repoID);
    				double sOwner = userScoreMap.get(ownerID);
    				int outD = userReposNumMap.get(ownerID);
    				s += pOwned * sOwner/outD;
    			}
    			//forks
    			ArrayList<Integer> forksList = repoForksListMap.get(repoID);
    			for(Integer fork:forksList){
    				int outD =1;
    				double sFork = repoScoreMap.get(fork);
    				s += pFork*sFork/outD;
    			}
    			//forked
    			{
    				Integer forkedFromID = forkNetMap.get(repoID);
    				double sForkedFrom = repoScoreMap.get(forkedFromID);
    				int outD = repoForksListMap.get(forkedFromID).size();
    				s += pForked *sForkedFrom/outD;
    			}
    			repoScoreMap.put(repoID,s);	
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void doTURbyDB(){//
    	try{
    		String line="";
    		int i=0;
    		Connection conn = BF.conn();
		//    conn.setAutoCommit(false); // 设置手动提交  
		    Statement stmt = conn.createStatement();  
			getUserFollowersListMap();
    		getUserFollowingsNumMap();
    		getUserReposListMap();
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
				int userID = Integer.valueOf(words[0]);;
    			double s=0.0;
    			//follow
    			ArrayList<Integer> fList = userFollowersListMap.get(userID);
    			if(fList!=null){
	    			for(Integer f:fList){
	    				int outD = userFollowingsNumMap.get(f);
	    				String sql0 = "select score from github.userturscore where id="+f;
	    				ResultSet temprs = stmt.executeQuery(sql0);
	    				double sF=0.0;
	    				if(temprs.next()){  
	    					sF = temprs.getDouble(1);
	    				}
	    				 //上一轮f节点的评分
	    				s+= pFollow*sF/outD;
	    			}
    			}
    			//followed
    			//贡献参数设为0，所以忽略
    			//own
    			ArrayList<Integer> rList = userReposListMap.get(userID);
    			for(Integer r:rList){
    			//	int outD = rList.size();
    				String sql1 = "select score from github.repoturscore where id="+r;
    				ResultSet temprs = stmt.executeQuery(sql1);
    				double sR=0.0;
    				if(temprs.next()){
    					sR= temprs.getDouble(1);//上一轮f节点的评分
    				}
    				int outD =1;
					s += pOwn*sR/outD;
    			}
		    	String sql2 = "UPDATE github.userturscore SET score="+s+" WHERE id="+userID;
		        stmt.executeUpdate(sql2);
    		//	userScoreMap.put(userID, s);
    		//	System.out.println(userID+" "+s);
	    	}
			reader.close();
	        MapClear();
	        
    		//repo
	     
    		getUserReposNumMap();
    		getOwnNetMap();
    		File Projects = new File(path+"tables\\projects.csv");
	    	BufferedReader reader2 = BF.readFile(Projects,"utf-8");
			while((line=reader2.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%25==0){
						System.out.println();
					}
				}
				String [] words = line.split(",");
	    	    int repoID = Integer.valueOf(words[0]);	
    			double s = 0.0;
    			//owned
    			{
    				Integer ownerID = ownNetMap.get(repoID);
    				String sql3 = "select score from github.userturscore where id="+ownerID;
    				ResultSet temprs = stmt.executeQuery(sql3);
    				double sOwner=0.0;
    				if(temprs.next()){  
    					sOwner = temprs.getDouble(1);
    				}
    				int outD = userReposNumMap.get(ownerID);
    				s += pOwned * sOwner/outD;
    			}
    			String sql4 = "UPDATE github.repoturscore SET score = "+s+" WHERE id = "+repoID;
    			stmt.executeUpdate(sql4);
			}
			MapClear();
			getRepoForksListMap();
    		getForkNetMap();
			reader2 = BF.readFile(Projects,"utf-8");
			while((line=reader2.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%25==0){
						System.out.println();
					}
				}
				String [] words = line.split(",");
	    	    int repoID = Integer.valueOf(words[0]);	
	    	    String sql5 = "select score from github.repoturscore where id = "+repoID;
	    	    ResultSet rs5 = stmt.executeQuery(sql5);
				double s=0.0;
				if(rs5.next()){
					s= rs5.getDouble(1);
				}
    			//forks
    			ArrayList<Integer> forksList = repoForksListMap.get(repoID);
    			if(forksList!=null){
    			
	    			for(Integer fork:forksList){
	    				int outD =1;
	    				 String sql6 = "select score from github.repoturscore where id = "+fork;
	    		    	 ResultSet rs6 = stmt.executeQuery(sql6);
	    				 double sFork=0.0;
	    				 if(rs6.next()){
	    					sFork= rs6.getDouble(1);//上一轮f节点的评分
	    				}
	    				s += pFork*sFork/outD;
	    			}
    			}
    			//forked
    			{
    				Integer forkedFromID = forkNetMap.get(repoID);
    				 String sql7 = "select score from github.repoturscore where id = "+forkedFromID;
    		    	 ResultSet rs7 = stmt.executeQuery(sql7);
    				 double sForkedFrom=0.0;
    					if(rs7.next()){
    						sForkedFrom= rs7.getDouble(1);//上一轮f节点的评分
    					}
    				int outD = repoForksListMap.get(forkedFromID).size();
    				s += pForked *sForkedFrom/outD;
    			}
    		//	repoScoreMap.put(repoID,s);	
    			System.out.println(repoID+" "+s);
    			String sql8 = "UPDATE github.repoturscore SET score = "+s
                			  +" WHERE id = "+repoID;
    			stmt.executeUpdate(sql8);
    		}
    		  MapClear();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void doTURByReadTXT(){//不断从文件中读取数据
    	try{
    		//user
    		getUserFollowersListMap();
    		getUserFollowingsNumMap();
    		getUserReposListMap();
    		for(Map.Entry<Integer, Double> m:userScoreMap.entrySet()){
    			int userID = m.getKey();
    			double s=0.0;
    			//follow
    			
    			ArrayList<Integer> fList = userFollowersListMap.get(userID);
    			for(Integer f:fList){
    				int outD = userFollowingsNumMap.get(f);
    				double sF= userScoreMap.get(f);//上一轮f节点的评分
    				s+= pFollow/outD*sF;
    			}
    			//followed
    			//贡献参数设为0，所以忽略
    			//own
    			ArrayList<Integer> rList = userReposListMap.get(userID);
    			for(Integer r:rList){
    			//	int outD = rList.size();
    				double sR = repoScoreMap.get(r);
    				int outD =1;
    				s += pOwn*sR/outD;
    			}
    			userScoreMap.put(userID, s);
    		}
    		MapClear();
    		getUserReposNumMap();
    		getOwnNetMap();
    		getRepoForksListMap();
    		getForkNetMap();
    		//repo
    		for(Map.Entry<Integer, Double> m:repoScoreMap.entrySet()){
    			int repoID = m.getKey();
    			double s = 0.0;
    			//owned
    			{
    				Integer ownerID = ownNetMap.get(repoID);
    				double sOwner = userScoreMap.get(ownerID);
    				int outD = userReposNumMap.get(ownerID);
    				s += pOwned * sOwner/outD;
    			}
    			//forks
    			ArrayList<Integer> forksList = repoForksListMap.get(repoID);
    			for(Integer fork:forksList){
    				int outD =1;
    				double sFork = repoScoreMap.get(fork);
    				s += pFork*sFork/outD;
    			}
    			//forked
    			{
    				Integer forkedFromID = forkNetMap.get(repoID);
    				double sForkedFrom = repoScoreMap.get(forkedFromID);
    				int outD = repoForksListMap.get(forkedFromID).size();
    				s += pForked *sForkedFrom/outD;
    			}
    			repoScoreMap.put(repoID,s);	
    		}
    		MapClear();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    
    //循环迭代
    public void reTUR(){
// 	   	System.out.println("读取表数据~~~~~~~");
// 	    readDatas();
// 	    System.out.println("初始化~~~~~~~");
// 	    init();
 		System.out.println("开始迭代计算~~~~~~~");
 		boolean flag = false;
 		Map<Integer,Double > CheckUserScoreMap = new HashMap<Integer,Double>();//authority
 		Map<Integer,Double > CheckRepoScoreMap = new HashMap<Integer,Double>();//hub
 		int i=0;//记录迭代次数
 		while(!flag){
 			Iterator<Integer> t1 = userScoreMap.keySet().iterator();
 			while(t1.hasNext()){
 				Integer uid = t1.next();
 				CheckUserScoreMap.put(uid, userScoreMap.get(uid));
 		  		}
 		  	Iterator<Integer> t2 = repoScoreMap.keySet().iterator();
 		  	while(t2.hasNext()){
 				Integer uid = t2.next();
 				CheckRepoScoreMap.put(uid, repoScoreMap.get(uid));
 		  	}
 		 // 	doTURByReadTXT();
 		  	doTURbyDB();
 		  	i++;
 		  	System.out.print(i+" ");
 		  	if(i%10==0)
 		  		System.out.println();
 		  	Iterator<Integer> t3 = userScoreMap.keySet().iterator();
 	  		while(t3.hasNext()){
 				Integer uid = t3.next();
 				double da = userScoreMap.get(uid)-CheckUserScoreMap.get(uid);
 				if(Math.abs(da)>max){
 					flag = false;
 					break;
 				}
 				double dh = repoScoreMap.get(uid)-CheckRepoScoreMap.get(uid);
 				if(Math.abs(dh)>max){
 					flag = false;
 					break;
 				}
 				flag = true;
 	  		}
 		}
         System.out.println("总共进行了    "+i+"次迭代。");
         resultToTXT();
    }
    public void resultToTXT(){
	   	//将结果插入数据库
		   System.out.println("将Authority和Hub值写入文本.....");
	       try{
	    	   
	    	   BufferedWriter outA = new BufferedWriter(
						new FileWriter(path+"features\\TURuserScore.csv"));
	    	   int count=0;
	    	   //排序
	    	   List<Map.Entry<Integer,Double>> list = new ArrayList(userScoreMap.entrySet());
	    	   Collections.sort(list, new Comparator<Map.Entry<Integer,Double>>(){
	    		   public int compare(Map.Entry<Integer,Double> m1,Map.Entry<Integer,Double> m2){
	    			   return m2.getValue().compareTo(m1.getValue());
	    		   }
	    	   });
	    	   for(Map.Entry<Integer, Double> m:list){
	    		   count++;
	    		   if(count%10000 ==0){
		        	System.out.print(count/10000+" ");
		        	if((count/10000)%20==0){
		        		System.out.println();
		        	}
		        	outA.flush();
		        }
				Integer uid = m.getKey();
				outA.write(uid+","+m.getValue()+"\n");
		        
	  		}
	  		System.out.println();
	        System.out.println("All down : " + count);  
	        outA.close(); 
	        count=0;
	        BufferedWriter outH = new BufferedWriter(
					new FileWriter(path+"features\\TURrepoScore.csv"));
	        //排序
	        ArrayList<Map.Entry<Integer,Double>> listH = new ArrayList(repoScoreMap.entrySet());
    	   Collections.sort(listH, new Comparator<Map.Entry<Integer,Double>>(){
    		   public int compare(Map.Entry<Integer,Double> m1,Map.Entry<Integer,Double> m2){
    			   return m2.getValue().compareTo(m1.getValue());
    		   }
    	   });
    	   for(Map.Entry<Integer, Double> m:listH){
    		   count++;
    		   if(count%10000 ==0){
	        	System.out.print(count/10000+" ");
	        	if((count/10000)%20==0){
	        		System.out.println();
	        	}
	        	outH.flush();
	        }
			Integer uid = m.getKey();
			outH.write(uid+","+m.getValue()+"\n");
  		}
  		System.out.println();
        System.out.println("All down : " + count);  
        outH.close(); 
	   }catch(Exception e){
	   	e.printStackTrace();
	   }           
   }
    public static void main(String[] args) {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();
		TUR t= new TUR();
	//	t.init();
	//	t.reTUR();
		t.doTURbyDB();
	//	t.initRepoScoreToDB();
	//	t.initToDB();
		long end = System.currentTimeMillis();
    	System.out.println("计算用时（秒）："+(end-start)/1000);
	}

}
