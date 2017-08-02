package hits;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import utils.BF;
import utils.Constants;

public class WatchersHits{

	/**
	 * @param args
	 */
	File watchersFile = new File(Constants.screenFile+"tables\\watchers.csv");

	double max = 0.000000001;
	//项目--watch项目的用户列表，即项目节点的入度
	public static Map<Integer,ArrayList<Integer> > RepoUsersListMap = new HashMap();
	//项目--
	public static Map<Integer,ArrayList<Integer> > UserReposListMap = new HashMap();
//	public static Map<Integer,ArrayList<Integer> > UserOwnRepoListMap = new HashMap();
    public static Map<Integer,Double> userAuthMap = new HashMap();//P节点以及{Authority,Hub}
    public static Map<Integer,Double> repoHubMap = new HashMap();//P节点以及{Authority,Hub}
	//读表
/*	public void readWatchers(){
		try{
			BufferedInputStream fis = new BufferedInputStream(
					new FileInputStream(Constants.screenFile+"tables\\watchers.csv"));
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(fis,"utf-8"),5*1024*1024);
			int count=0;
			String line = null;
			String[] words;
			int i=0;
			while((line = reader.readLine())!=null){
				count++;
				if(count%100000 == 0){
					i++;
					System.out.print(count/100000+" ");
					if(i%20==0){
						System.out.println();
					}				
				}
				words = line.split(",");
				Integer repo_id = Integer.valueOf(words[0]);
				Integer user_id = Integer.valueOf(words[1]);
				//在这里，只有user指向repo的关系，也就是说，repo只有入度，而user只有出度
				ArrayList<Integer> c = RepoWatchersListMap.get(repo_id);
				if(c ==null){//该repo还没有放入Map中,则将其加入MAp， 同时将该行的user加入其入度list
					c = new ArrayList<Integer>();
					
				}
				c.add(user_id);
				RepoWatchersListMap.put(repo_id, c);
				//对user来说只有出度
				ArrayList<Integer> r = UserWatchedreposListMap.get(user_id);
				if(r == null){
					r = new ArrayList<Integer>();
				}
				r.add(repo_id);  
				UserWatchedreposListMap.put(user_id, r);
//				System.out.println("\t P: "+ repo_id+" "+NodePInListMap.get(repo_id).size());
//				System.out.println("\t Q: "+ user_id+" "+NodeQOutListMap.get(user_id).size());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("\t repo: "+RepoWatchersListMap.size()+
				"\t user: "+UserWatchedreposListMap.size());
	}
	*/
    public void splitWatchersTO3(){
		try{
    		System.out.print("ownNetMap: ");
    		String line = null;
    		int i=0;
  //  		File watchersFile = new File(Constants.screenFile+"features\\watchers.csv");
	    	BufferedReader reader = BF.readFile(watchersFile);
	    	BufferedWriter out1 = new BufferedWriter(new FileWriter
	    			(Constants.screenFile+"tables\\watchers1.csv"));
	    	BufferedWriter out2 = new BufferedWriter(new FileWriter
	    			(Constants.screenFile+"tables\\watchers2.csv"));
	    	BufferedWriter out3 = new BufferedWriter(new FileWriter
	    			(Constants.screenFile+"tables\\watchers3.csv"));
	    	long start = System.currentTimeMillis();
			while((line=reader.readLine())!=null){
				i++;
				if(i%10000000==0){
					System.out.print(i/10000000+" ");
					if(i/10000000%25==0){
						System.out.println();
					}
					long end = System.currentTimeMillis();
					System.out.println("readTxt2方法，使用内存="
							+(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())
							+",使用时间毫秒="+(end-start)); 
				}
				if(i<10000000){
					out1.write(line+"\n");
				}else if(i<20000000){
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
	//读表
	public void readRepoUsers(String fileName){
		try{
			BufferedInputStream fis = new BufferedInputStream(new FileInputStream(fileName));
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(fis,"utf-8"),5*1024*1024);
			int count=0;
			String line = null;
			String[] words;
			int i=0;
			while((line = reader.readLine())!=null){
				count++;
				if(count%1000000 == 0){
					i++;
					System.out.print(count/1000000+" ");
					if(i%20==0){
						System.out.println();
					}				
				}
				words = line.split(",");
				Integer repo_id = Integer.valueOf(words[0]);
				Integer user_id = Integer.valueOf(words[1]);
				//在这里，只有user指向repo的关系，也就是说，repo只有入度，而user只有出度
				ArrayList<Integer> c = RepoUsersListMap.get(repo_id);
				if(c ==null){//该repo还没有放入Map中,则将其加入MAp， 同时将该行的user加入其入度list
					c = new ArrayList<Integer>();
				}
				c.add(user_id);
				RepoUsersListMap.put(repo_id, c);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("\t repo: "+RepoUsersListMap.size()+
				", user: "+UserReposListMap.size());
	}
	//读表
	public void readUserRepos(String fileName){
		try{
			System.out.println("readUserRepos");
			BufferedInputStream fis = new BufferedInputStream(
					new FileInputStream(fileName));
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(fis,"utf-8"),5*1024*1024);
			int count=0;
			String line = null;
			String[] words;
			int i=0;
			while((line = reader.readLine())!=null){
				count++;
				if(count%1000000 == 0){
					i++;
					System.out.print(count/1000000+" ");
					if(i%20==0){
						System.out.println();
					}				
				}
				words = line.split(",");
				Integer repo_id = Integer.valueOf(words[0]);
				Integer user_id = Integer.valueOf(words[1]);
				//在这里，只有user指向repo的关系，也就是说，repo只有入度，而user只有出度
				
				//对user来说只有出度
				ArrayList<Integer> r = UserReposListMap.get(user_id);
				if(r == null){
					r = new ArrayList<Integer>();
				}
				r.add(repo_id);  
				UserReposListMap.put(user_id, r);
//				System.out.println("\t P: "+ repo_id+" "+NodePInListMap.get(repo_id).size());
//				System.out.println("\t Q: "+ user_id+" "+NodeQOutListMap.get(user_id).size());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("\t repo: "+RepoUsersListMap.size()+
				"\t user: "+UserReposListMap.size());
	}
    //初始化，将所有节点的A和H都设为1
    public void init(){     
    	String line = null;
		int i=0;
        try{
	    	File usersFile = new File(Constants.screenFile+"tables\\watchers.csv");
	    	BufferedReader reader = BF.readFile(usersFile,"utf8");
	    	System.out.println("read  "+usersFile.getName()+", waiting......");
			while((line=reader.readLine())!=null){
				i++;
				if(i%1000000==0){
					System.out.print(i/1000000+" ");
					if(i/1000000%25==0){
						System.out.println();
					}
				}
				String [] words = line.split(",");
				int uid = Integer.valueOf(words[1]);;
				userAuthMap.put(uid, 1.0);//初始化为1.0
				repoHubMap.put(Integer.valueOf(words[0]), 1.0);//初始化为1.0
				
			}
			System.out.println("userAuthMap: "+userAuthMap.size());
			System.out.println("repoHubMap: "+repoHubMap.size());
//			File projectsFile = new File(Constants.screenFile+"tables\\watchers.csv");
//	    	BufferedReader reader2 = BF.readFile(projectsFile,"utf8");
//	    	System.out.println("read  "+projectsFile.getName()+", waiting......");
//	    	i=0;
//			while((line=reader2.readLine())!=null){
//				i++;
//				if(i%1000000==0){
//					System.out.print(i/1000000+" ");
//					if(i/1000000%25==0){
//						System.out.println();
//					}
//				}
//				String [] words = line.split(",");
//				repoHubMap.put(Integer.valueOf(words[0]), 1.0);//初始化为1.0
//			}
//			System.out.println("repoHubMap: "+repoHubMap.size());
//		
        }catch(Exception e){
        	e.printStackTrace();
        }
    }
    public void doHits(){
    	
        //	System.out.println("authority JISUAN");
    	
    	for(int k=1;k<4;k++){
    		RepoUsersListMap.clear();
    		String tempWatcher = Constants.screenFile+"tables\\watchers"+k+".csv";
    		readRepoUsers(tempWatcher);
    		for(Map.Entry<Integer, ArrayList<Integer>> m:RepoUsersListMap.entrySet()){
    			Integer rid = m.getKey();
    			double hub = 0.0;
    			if(k!=1){
    				hub = repoHubMap.get(rid);
    			}
    			ArrayList<Integer> usersList = RepoUsersListMap.get(rid);
    			if(usersList!=null){
    				for(Integer uid:usersList){
    					double uAuth = userAuthMap.get(uid);
    					hub += uAuth;
    				}
    			}
    			repoHubMap.put(rid, hub);
    		}
    	}
    	RepoUsersListMap.clear();
    	//找最大值
    	double Max = 0;
    	for(Map.Entry<Integer, Double> m:repoHubMap.entrySet()){
    		if(Max<m.getValue()){
    			Max = m.getValue();
    		}
    	}
    	//标准化
    	for(Map.Entry<Integer, Double> m:repoHubMap.entrySet()){
    		double hub = m.getValue();
    		repoHubMap.put(m.getKey(), hub/Max);
    	}
    	
    	//user -Auth
    	for(int k=1;k<4;k++){
    		UserReposListMap.clear();
    		String tempWatcher = Constants.screenFile+"tables\\watchers"+k+".csv";
    		readUserRepos(tempWatcher);
    		for(Map.Entry<Integer, ArrayList<Integer>> m:UserReposListMap.entrySet()){
    			Integer uid = m.getKey();
    			double auth = 0.0;
    			if(k!=1){
    				auth = userAuthMap.get(uid);
    			}
    			ArrayList<Integer> reposList = UserReposListMap.get(uid);
    			if(reposList!=null){
    				for(Integer rid:reposList){
    					double rHub = repoHubMap.get(rid);
    					auth += rHub;
    				}
    			}
    			userAuthMap.put(uid, auth);
    		}
    	}
    	UserReposListMap.clear();
    	//找最大值
    	double Max2 = 0;
    	for(Map.Entry<Integer, Double> m:userAuthMap.entrySet()){
    		if(Max2<m.getValue()){
    			Max2 = m.getValue();
    		}
    	}
    	//标准化
    	for(Map.Entry<Integer, Double> m:userAuthMap.entrySet()){
    		double auth = m.getValue();
    		userAuthMap.put(m.getKey(), auth/Max2);
    	}
    }
    //循环迭代
    public void reHITS(){
    	System.out.println("read table~~~~~~~");
    	
    //	readWatchers();
    	//初始化
    	init();
    	
 		System.out.println("开始HITS的迭代计算~~~~~~~");
 		boolean flag = false;
 		Map<Integer,Double > CheckAuthMap = new HashMap<Integer,Double>();//authority
 		Map<Integer,Double > CheckHubMap = new HashMap<Integer,Double>();//hub
 		int i=0;//记录迭代次数
 		while(!flag){
 			Iterator<Integer> t1 = userAuthMap.keySet().iterator();
 			while(t1.hasNext()){
 				Integer uid = t1.next();
 				CheckAuthMap.put(uid, userAuthMap.get(uid));
 		  	}
 		  	Iterator<Integer> t2 = repoHubMap.keySet().iterator();
 		  	while(t2.hasNext()){
 				Integer rid = t2.next();
 				CheckHubMap.put(rid, repoHubMap.get(rid));
 		  	}
 			i++;
 		  	System.out.println(i+"~~~~~~~");
 		  	doHits();
 		  
 		  	Iterator<Integer> t3 = userAuthMap.keySet().iterator();
 	  		while(t3.hasNext()){
 				Integer repoid = t3.next();
 				double da = userAuthMap.get(repoid)-CheckAuthMap.get(repoid);
 				if(Math.abs(da)>max){
 					flag = false;
 					break;
 				}
 	  		}
			Iterator<Integer> t4 = repoHubMap.keySet().iterator();
 	  		while(t4.hasNext()){
 	  			Integer userid = t4.next();
 				double dh = repoHubMap.get(userid)-CheckHubMap.get(userid);
 				if(Math.abs(dh)>max){
 					flag = false;
 					break;
 				}
 				flag = true;
 	  		}
 		}
 		
     //    BF.Log("总共进行了    "+i+"次迭代。");
         System.out.println("总共进行了    "+i+"次迭代。");
         store();
    }
    public void print(){//结果输出
 	   System.out.println("UserAuth is : ");
 	   	Iterator<Integer> t1 = userAuthMap.keySet().iterator();
		while(t1.hasNext()){
		Integer uid = t1.next();
		System.out.println("\t"+uid+","+userAuthMap.get(uid));
		}
		
		System.out.println("OtherHub is : ");
		Iterator<Integer> t2 = repoHubMap.keySet().iterator();
		while(t2.hasNext()){
		Integer oid = t2.next();
		System.out.print("\t"+oid+","+repoHubMap.get(oid)+" ");
		}
    }
	//结果存储
    public void store(){
    	String line = "";
    	int i = 0;
    	try{
//    		File userAuth = new File(Constants.screenFile+"\\features\\hits\\watchersUserAuth.csv");
//        	BufferedReader reader = BF.readFile(userAuth,"utf8");
//        	System.out.println("read  "+userAuth.getName()+", waiting......");
//    		while((line=reader.readLine())!=null){
//    			i++;
//    			if(i%100000==0){
//    				System.out.print(i/100000+" ");
//    				if(i/100000%25==0){
//    					System.out.println();
//    				}
//    			}
//    			String [] words = line.split(",");
//    			Integer userid = Integer.valueOf(words[0]);
//    			userAuthMap.put(userid, Double.valueOf(words[1]));
//    		}
//    		System.out.println("\n userAuthMap number is "+userAuthMap.size());
//    		File users = new File(Constants.screenFile+"\\tables\\users.csv");
//        	reader = BF.readFile(users,"utf8");
//        	System.out.println("read  "+users.getName()+", waiting......");
//        	i=0;
//    		while((line=reader.readLine())!=null){
//    			i++;
//    			if(i%100000==0){
//    				System.out.print(i/100000+" ");
//    				if(i/10000%25==0){
//    					System.out.println();
//    				}
//    			}
//    			String [] words = line.split(",");
//    			Integer userid = Integer.valueOf(words[0]);
//    			if(userAuthMap.get(userid)==null){
//    				userAuthMap.put(userid, 0.0);
//    			}
//    		}
//    		System.out.println("\n userAuthMap number is "+userAuthMap.size());
//    		
//    		BufferedWriter outUA = new BufferedWriter(
//    				new FileWriter(Constants.screenFile+"features\\hits\\2watchersUserAuth.csv"));
//    		
//    		List<Map.Entry<Integer, Double>> list=
//    		    new ArrayList<Map.Entry<Integer, Double>>(userAuthMap.entrySet());
//
//    		//排序
//    		Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>(){   
//    		    public int compare(Map.Entry<Integer, Double> m1, Map.Entry<Integer, Double> m2){     
//    		       
//    		        double d = m2.getValue()-m1.getValue();
//    		        if(d>0)
//    		        	return 1;
//    		        else
//    		        	return 0;
//    		    }
//    		});
//    		for(Map.Entry<Integer, Double> m:list){
//     			outUA.write(m.getKey()+","+m.getValue()+"\n");
//     		}
//    		outUA.close();
//    		userAuthMap.clear();
    		i=0;
    		BufferedWriter outRH = new BufferedWriter(
    				new FileWriter(Constants.screenFile+"features\\hits\\2watchersRepoHub.csv"));
    	
    		File projects = new File(Constants.screenFile+"\\tables\\projects.csv");
    		BufferedReader reader2 = BF.readFile(projects,"utf8");
        	System.out.println("read  "+projects.getName()+", waiting......");
        	i=0;
        	ArrayList<Integer> ridsList = new ArrayList<Integer>();
    		while((line=reader2.readLine())!=null){
    			i++;
    			if(i%1000000==0){
    				System.out.print(i/1000000+" ");
    				if(i/1000000%25==0){
    					System.out.println();
    				}
    			}
    			String [] words = line.split(",");
    			Integer rid = Integer.valueOf(words[0]);
    			ridsList.add(rid);
    		}
    		System.out.println("\n ridsList number is "+ridsList.size());
    		
    		File repoHub = new File(Constants.screenFile+"\\features\\hits\\watchersRepoHub.csv");
        	reader2 = BF.readFile(repoHub,"utf8");
        	System.out.println("read  "+repoHub.getName()+", waiting......");
        	i=0;
    		while((line=reader2.readLine())!=null){
    			i++;
    			if(i%10000==0){
    				System.out.print(i/10000+" ");
    				if(i/10000%25==0){
    					System.out.println();
    				}
    			}
    			String [] words = line.split(",");
    			Integer rid = Integer.valueOf(words[0]);
    			outRH.write(line+"\n");
    			ridsList.remove(rid);
    		}
    		System.out.println("\n ridsList number is "+ridsList.size());
    	
     		for(Integer m:ridsList){
     			i++;
     			outRH.write(m+",0\n");
     		}
     		outRH.close();
     		System.out.println("\n all RepoHUb number is "+i);
        	
    	}catch(Exception e){
    		e.printStackTrace();
    	}
   }
	
    /*已经把REPO以及指向repo 的user 列表写在了文档 里
     * 已经把user 以及user指向的repo 列表写在了文档里
     * 然后如果全读到MAP中计算的话，MAP 装不下，所以想这样，因为并不一定是多有的点都彼此相互连接形成一个网，
     * 所以打算先算一部分，将repo--userList读到MAP中，然后遍历userRepos文件
     * 
     * */
/*    public void GetUsersList(){
    	try{
    		BufferedInputStream fis = new BufferedInputStream(new FileInputStream(Constants.WatchersFile));
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"utf-8"),5*1024*1024);
			String line = null;
			String[] words;
			int userCount=0, repoCount = 0,count=0;
			BufferedWriter out  = new BufferedWriter(new FileWriter(Constants.RootFile+"outs\\sub1.txt"));
			ArrayList<Integer> userList = new ArrayList<Integer>();
			ArrayList<Integer> repoList = new ArrayList<Integer>();
			
			while((line = reader.readLine())!=null){
				words = line.split(",");
				count++;
				if(count%304000==0){
					System.out.println(count/304000);
				}
				Integer rid = Integer.valueOf(words[0]);
				Integer uid = Integer.valueOf(words[1]);
				if(repoList.size()==0){
					//此时表示刚开始，repoList为空，从从REpo开始
					repoList.add(rid);
				}
				if(repoList.contains(rid)){
					userList.add(uid);
					out.write(rid+","+uid+"\n");
				}else if(userList.contains(uid)){
					repoList.add(rid);
					out.write(rid+","+uid+"\n");
				}
				
				
			}
			System.out.println("repos  size is "+repoList.size());
			System.out.println("users  size is "+userList.size());
			out.close();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
   */
    public static void main(String[] args){
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();
		WatchersHits wh = new WatchersHits();
	//	wh.init();
	//	wh.reHITS();
		wh.store();
	//	wh.readRepoWatchers();
	//	wh.splitWatchersTO3();
		long end = System.currentTimeMillis();
		System.out.println("计算用时： "+(end - start)/1000);
	//	wh.print();
	}
	
}

