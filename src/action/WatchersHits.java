package action;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import utils.Constants;
import entity.Watcher;

public class WatchersHits {

	/**
	 * @param args
	 */
	double max = 0.00000000001;
	public static Map<Integer,ArrayList<Integer> > NodePOutListMap = new HashMap();//第一种节点以及出度列表
	public static Map<Integer,ArrayList<Integer> > NodePInListMap = new HashMap();//第一种节点以及其入度列表
	public static Map<Integer,ArrayList<Integer> > NodeQOutListMap = new HashMap();//第二种节点以及其出度列表
    public static Map<Integer,ArrayList<Integer> > NodeQInListMap = new HashMap();//第二种节点以及其入度列表
    
    Map<Integer,ArrayList<Integer> > NodeUserListMap = new HashMap();//第二种节点以及其入度列表
	Map<Integer,Integer> NodeQMap = new HashMap();//第二种节点以及其入度列表
	Map<Integer,ArrayList<Integer> > NodePListMap = new HashMap();//第二种节点以及其入度列表
	Map<Integer,Integer> NodePMap = new HashMap();//第二种节点以及其入度列表
	
    
	ArrayList<Watcher> WatchersList = new ArrayList();
    String time = "2015-6";
	//读表
	public ArrayList<Watcher> readWatchersList(){
		try{
			BufferedInputStream fis = new BufferedInputStream(
					new FileInputStream("Data\\spliWatchersByTime\\watchers"+time+".txt"));
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(fis,"utf-8"),5*1024*1024);
			int count=0;
			String line = null;
			String[] words;
			int i=0;
			while((line = reader.readLine())!=null){
				count++;
//				if(count/300000<32){
//					continue;
//				}
//				if(count%300000 == 0){
//					i++;
//					if(i%20==0){
//						System.out.println();
//					}
//					if(i==33){
//						System.out.println("\t");
//						System.out.print(count+" ");
//					}
//						
//					System.out.print(count/300000+"% ");
//					
//				}
				words = line.split(",");
				Watcher w = new Watcher();
				Integer repo_id = Integer.valueOf(words[0]);
				Integer user_id = Integer.valueOf(words[1]);
				//NodeP  --- Repo
				//NodeQ  --- User
				//在这里，只有user指向repo的关系，也就是说，repo只有入度，而user只有出度
				ArrayList<Integer> c = NodePInListMap.get(repo_id);
				if(c ==null){//该repo还没有放入Map中,则将其加入MAp， 同时将该行的user加入其入度list
					ArrayList<Integer> cc = new ArrayList<Integer>();
					cc.add(user_id);
					NodePInListMap.put(repo_id, cc);
				}else
				{
					c.add(user_id);
					NodePInListMap.put(repo_id, c);
				}
				//对user来说只有出度
				ArrayList<Integer> r = NodeQOutListMap.get(user_id);
				if(r == null){
					ArrayList<Integer> rr = new ArrayList<Integer>();
					rr.add(repo_id);
					NodeQOutListMap.put(user_id, rr);
				}else{
					r.add(repo_id);  
					NodeQOutListMap.put(user_id, r);
					}
				
				
				w.setRepo_id(Integer.valueOf(words[0]));
				w.setUser_id(Integer.valueOf(words[1]));//去掉引号
			//	Timestamp times = Timestamp.valueOf(words[2].substring(1, words[2].length()-1)); 
			//	w.setCreated_at(times);
				w.setCreated_at(words[2].substring(1, words[2].length()-1));
				WatchersList.add(w);
//				System.out.println("\t P: "+ repo_id+" "+NodePInListMap.get(repo_id).size());
//				System.out.println("\t Q: "+ user_id+" "+NodeQOutListMap.get(user_id).size());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("\t Q: "+NodeQOutListMap.size()+"\t P: "+NodePInListMap.size());
		return WatchersList;
	}
	//单纯的读表
	public void read(){
		try{
			BufferedInputStream fis = new BufferedInputStream(new FileInputStream(Constants.WatchersFile));
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"utf-8"),5*1024*1024);
			
			int count=0;
			String line = null;
			String[] words;
			int i=0;
			while((line = reader.readLine())!=null){
				words = line.split(",");
				Integer repoid = Integer.valueOf(words[0]);
				Integer userid = Integer.valueOf(words[1]);
				ArrayList<Integer> us = NodePListMap.get(repoid);
				if(us==null){
					ArrayList<Integer> uss = new ArrayList<Integer>();
					uss.add(userid);
					NodePListMap.put(repoid, uss);
				}else{
					us.add(userid);
					NodePListMap.put(repoid, us);
				}
				
				Integer c = NodePMap.get(repoid);
				if(c==null){
					NodePMap.put(repoid, 1);
				}else{
					c++;
					NodePMap.put(repoid, c);
				}
				
				count++;
				if(count%304000 == 0){
					
					if(i%20==0){System.out.println();}
					i++;
					System.out.print(count/304000+"% ");
					
				}
			}
			System.out.println();
			System.out.println("total repos number is "+NodePListMap.size());
			
//			try{
//	    		
//			//	  BufferedWriter outP = new BufferedWriter(new FileWriter(Constants.RootFile+"outs\\wsReposNum.txt"));
//				  BufferedWriter outPU = new BufferedWriter(new FileWriter(Constants.RootFile+"outs\\wsReposUsers.txt"));
//				  
//		    	  List<Map.Entry<Integer,Integer>> mappingList = new ArrayList<Map.Entry<Integer,Integer>>(NodePMap.entrySet()); 
//					  //通过比较器实现比较排序 
//		    	  	Collections.sort(mappingList, new Comparator<Map.Entry<Integer,Integer>>(){ 
//					   public int compare(Map.Entry<Integer,Integer> mapping1,Map.Entry<Integer,Integer> mapping2){ 
//					    return mapping2.getValue().compareTo(mapping1.getValue()); 
//					   } 
//					  }); 
//					 for(Map.Entry<Integer,Integer> mapping:mappingList){ 
//						//   System.out.println(mapping.getKey()+":"+mapping.getValue()); 
//						  Integer repoid = mapping.getKey();
//						  ArrayList<Integer> user = NodePListMap.get(repoid);
//						  outPU.write(repoid+" ");
//						  for(Integer userid:user){
//							  outPU.write(userid+",");
//						  }
//						  outPU.write("\n");
//						  outP.write(mapping.getKey()+","+mapping.getValue()+"\n");
//					 } 
//				 
////	      	   	Iterator<Integer> t1 = NodePMap.keySet().iterator();
////	     		while(t1.hasNext()){
////		  			Integer repoid = t1.next();
////		  			outP.write(repoid+","+NodePMap.get(repoid)+"\n");
////	     		}
//	     		outP.close();
//	     		outPU.close();
//	    	}catch(Exception e){
//	    		e.printStackTrace();
//	    	}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void read2(){
		try{
			BufferedInputStream fis = new BufferedInputStream(new FileInputStream(Constants.WatchersFile));
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"utf-8"),5*1024*1024);
			
			int count=0;
			String line = null;
			String[] words;
			int i=0;
			while((line = reader.readLine())!=null){
				words = line.split(",");
				Integer repoid = Integer.valueOf(words[0]);
				Integer userid = Integer.valueOf(words[1]);
				
				//add to Users' Repolist---
				ArrayList<Integer> rs = NodeUserListMap.get(userid);
				if(rs==null){
					ArrayList<Integer> rss = new ArrayList<Integer>();
					rss.add(repoid);
					NodeUserListMap.put(userid, rss);
				}else{
					rs.add(repoid);
					NodeUserListMap.put(userid, rs);
				}
				
				Integer c = NodeQMap.get(userid);
				if(c==null){
					NodeQMap.put(userid, 1);
				}else{
					c++;
					NodeQMap.put(userid, c);
				}
				count++;
				if(count%304000 == 0){
					
					if(i%20==0){System.out.println();}
					i++;
					System.out.print(count/304000+"% ");
					
				}
			}
			System.out.println();
			System.out.println("total repos number is "+NodeUserListMap.size());
//			try{
//	    		
//				  BufferedWriter outQ = new BufferedWriter(new FileWriter(Constants.RootFile+"outs\\wsUsersRepoNum.txt"));
//				  BufferedWriter outPU = new BufferedWriter(new FileWriter(Constants.RootFile+"outs\\wsUsersRepos.txt"));
//				  
//		    	  List<Map.Entry<Integer,Integer>> mappingList = new ArrayList<Map.Entry<Integer,Integer>>(NodeQMap.entrySet()); 
//					  //通过比较器实现比较排序 
//		    	  	Collections.sort(mappingList, new Comparator<Map.Entry<Integer,Integer>>(){ 
//					   public int compare(Map.Entry<Integer,Integer> mapping1,Map.Entry<Integer,Integer> mapping2){ 
//					    return mapping2.getValue().compareTo(mapping1.getValue()); 
//					   } 
//					  }); 
//					 for(Map.Entry<Integer,Integer> mapping:mappingList){ 
//						//   System.out.println(mapping.getKey()+":"+mapping.getValue()); 
//						  Integer userid = mapping.getKey();
//						  ArrayList<Integer> user = NodeUserListMap.get(userid);
//						  outPU.write(userid+" ");
//						  for(Integer repoid:user){
//							  outPU.write(repoid+",");
//						  }
//						  outPU.write("\n");
//						  outQ.write(mapping.getKey()+","+mapping.getValue()+"\n");
//					 } 
//				 
////	      	   	Iterator<Integer> t1 = NodePMap.keySet().iterator();
////	     		while(t1.hasNext()){
////		  			Integer repoid = t1.next();
////		  			outP.write(repoid+","+NodePMap.get(repoid)+"\n");
////	     		}
//	     		outQ.close();
//	     		outPU.close();
//	    	}catch(Exception e){
//	    		e.printStackTrace();
//	    	}
////			
//			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	//HITS计算,计算结果Repo只有A值，User只有H值
	//结果
//    public static Map<Integer,double[] > NodePAHMap = new HashMap();//P节点以及{Authority,Hub}
//    public static Map<Integer,double[] > NodeQAHMap = new HashMap();//P节点以及{Authority,Hub}
    
    public static Map<Integer,Double> NodePAMap = new HashMap();//P节点以及{Authority,Hub}
    public static Map<Integer,Double> NodeQHMap = new HashMap();//P节点以及{Authority,Hub}
    
    //初始化，将所有节点的A和H都设为1
    public void init(){     
    	//初始化第一种节点，节点P，的A和H
       Iterator<Integer> t1 = NodePInListMap.keySet().iterator();
    		while(t1.hasNext()){
    			Integer pid = t1.next();
    		//	double ah = NodePAMap.get(pid);
    		//	double ah = 1.0;
    			NodePAMap.put(pid, 1.0);//初始化为1.0
    		//	System.out.print(uid+" ");
    		} 	
    	//	System.out.println(" ");
    		Iterator<Integer> t2 = NodeQOutListMap.keySet().iterator();
    		while(t2.hasNext()){
    			Integer qid = t2.next();
    		//	double ah = NodeQHMap.get(qid);
    		//	ah = 1.0;
    			NodeQHMap.put(qid,1.0);//初始化为1.0
  
    		//	System.out.print(oid+" ");
    		} 
    	//	System.out.println(" ");
        }
    public void doHits(){
    	
        //	System.out.println("authority JISUAN");
        	Iterator<Integer> t1 = NodePInListMap.keySet().iterator();
        	while(t1.hasNext()){
    			Integer uid = t1.next();
    		//	System.out.print(uid+", related other points ：  ");
    			NodePAMap.put(uid, 0.0);
    			double auth = 0.0;
    			double Anorm =0.0;//用于归一化
    			if(NodePInListMap.get(uid)==null){
    				//System.out.println(" 没有followers ");
    			}else{
    		//		System.out.print(uid+", related other points ：  ");
    				ArrayList<Integer> InList = NodePInListMap.get(uid);//与该user 有关联的另一类节点的列表
    			//	System.out.println("\t rt.UserandListMap.get(uid).size:  "+rt.UserandListMap.get(uid).size());
    		//		System.out.print(uid+":"+RaOtherList.size()+"----");
    				
    				Iterator it1 = InList.iterator();
    				while(it1.hasNext()){
    		        	Integer oid = (Integer) it1.next();
    		        	auth += NodeQHMap.get(oid);
    		        	Anorm += Math.pow(NodeQHMap.get(oid), 2); // calculate the sum of the squared auth values to normalise	      
    		        	
    		        //	System.out.print(oid+","+OtherHubMap.get(oid)+" ");
    		        }
    				Anorm = Math.sqrt(Anorm);//平方和的开方，用于归一化
    				if(Anorm!=0){
    			//		System.out.print("\"a: "+auth+"\" ");
    					NodePAMap.put(uid, auth/Anorm);//
    				}else{
    					NodePAMap.put(uid, 0.0);
    				}
    			}
    		//	System.out.println("");
        	}
        
        	Iterator<Integer> t2 = NodeQOutListMap.keySet().iterator();
        	while(t2.hasNext()){
        		Integer oid = t2.next();
        		NodeQHMap.put(oid, 0.0);
    	        double hub = 0.0;
    	        double Hnorm = 0.0;
    	     //   System.out.print(oid+":"+RaUserList.size()+"----");
    	        if(NodeQOutListMap.get(oid)==null){
    	        	//System.out.println(" 没有followering ");
    	        }else{
    	        	 ArrayList<Integer> RaUserList = NodeQOutListMap.get(oid);//与其相关的用户的列表
    	 	        
    	 	    //    System.out.print(oid+":"+RaUserList.size()+"----");
    	 			Iterator it2 = RaUserList.iterator();
    	 			while(it2.hasNext()){
    	 	        	Integer uid = (Integer) it2.next();
    	 	        	
    	 	      //  	System.out.println("\t"+uid+" ");
    	 	        	hub += NodePAMap.get(uid);
    	 	        	Hnorm += Math.pow(NodePAMap.get(uid), 2);
    	 	          //  System.out.print(uid+","+UserAuthMap.get(uid)+" ");
    	 	        }
    	 			Hnorm = Math.sqrt(Hnorm);
    	 			if(Hnorm!=0){
    	 			//	System.out.print("\"h: "+hub+"\" ");
    	 				NodeQHMap.put(oid, hub/Hnorm);//归一化
    	 			}else{
    	 				NodeQHMap.put(oid, 0.0);
    	 			}
    	        }
    	   //   System.out.println(" ");
    		} 
        //	System.out.println(" ");
        }
    //循环迭代
    public void reHITS(){
    	System.out.println("read table~~~~~~~");
    	
    	readWatchersList();
    	System.out.println("P: "+NodePInListMap.size());
    	System.out.println("Q: "+NodeQOutListMap.size());
    	
    	//初始化
    	init();
    	
 		System.out.println("开始HITS的迭代计算~~~~~~~");
 		boolean flag = false;
 		Map<Integer,Double > CheckAuthMap = new HashMap<Integer,Double>();//authority
 		Map<Integer,Double > CheckHubMap = new HashMap<Integer,Double>();//hub
 		int i=0;//记录迭代次数
 		while(!flag){
 			Iterator<Integer> t1 = NodePAMap.keySet().iterator();
 			while(t1.hasNext()){
 				Integer uid = t1.next();
 				CheckAuthMap.put(uid, NodePAMap.get(uid));
 		  		}
 		  	Iterator<Integer> t2 = NodeQHMap.keySet().iterator();
 		  	while(t2.hasNext()){
 				Integer uid = t2.next();
 				CheckHubMap.put(uid, NodeQHMap.get(uid));
 		  	}
 		  	doHits();
 		  	i++;
 		  	Iterator<Integer> t3 = NodePAMap.keySet().iterator();
 	  		while(t3.hasNext()){
 				Integer repoid = t3.next();
 				double da = NodePAMap.get(repoid)-CheckAuthMap.get(repoid);
 				if(Math.abs(da)>max){
 					flag = false;
 					break;
 				}
 	  		}
			Iterator<Integer> t4 = NodeQHMap.keySet().iterator();
 	  		while(t4.hasNext()){
 	  			Integer userid = t4.next();
 				double dh = NodeQHMap.get(userid)-CheckHubMap.get(userid);
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
    //迭代iter次
    public void reHITS(int iter){
    	System.out.println("read table~~~~~~~");
    	
    	readWatchersList();
    	System.out.println("P: "+NodePInListMap.size());
    	System.out.println("Q: "+NodeQOutListMap.size());
    	
    	//初始化
    	init();
    	
		System.out.println("开始HITS的迭代计算~~~~~~~");
		
		boolean flag = false;
		int i=0;//记录迭代次数
		while(iter>0){
		//	print();
		  	doHits();
		  	iter--;
		}
		store();
  }
    public void print(){//结果输出
 	   System.out.println("UserAuth is : ");
 	   	Iterator<Integer> t1 = NodePAMap.keySet().iterator();
    		while(t1.hasNext()){
 			Integer uid = t1.next();
 			System.out.println("\t"+uid+","+NodePAMap.get(uid));
    		}
    		
    		System.out.println("OtherHub is : ");
    		Iterator<Integer> t2 = NodeQHMap.keySet().iterator();
    		while(t2.hasNext()){
 			Integer oid = t2.next();
 			System.out.print("\t"+oid+","+NodeQHMap.get(oid)+" ");
    		}
    }
	//结果存储
    public void store(){
    	try{
    		BufferedWriter outPA = new BufferedWriter(
    				new FileWriter("Data\\result\\"+time+"watchersRepoA"+".txt"));
    		BufferedWriter outQH = new BufferedWriter(
    				new FileWriter("Data\\result\\"+time+"watchersUserH"+".txt"));
    		
    		List<Map.Entry<Integer, Double>> list=
    		    new ArrayList<Map.Entry<Integer, Double>>(NodePAMap.entrySet());

    		//排序
    		Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>(){   
    		    public int compare(Map.Entry<Integer, Double> m1, Map.Entry<Integer, Double> m2){     
    		       
    		        double d = m2.getValue()-m1.getValue();
    		        if(d>0)
    		        	return 1;
    		        else
    		        	return 0;
    		    }
    		});
    		
    		List<Map.Entry<Integer, Double>> list2=
    		    new ArrayList<Map.Entry<Integer, Double>>(NodeQHMap.entrySet());

    		//排序
    		Collections.sort(list2, new Comparator<Map.Entry<Integer, Double>>(){   
    		    public int compare(Map.Entry<Integer, Double> m1, Map.Entry<Integer, Double> m2){     
    		        double d = m2.getValue()-m1.getValue();
    		        if(d>0)
    		        	return 1;
    		        else
    		        	return 0;
    		    }
    		});
    		
     		for(Map.Entry<Integer, Double> m:list){
     			outPA.write(m.getKey()+","+m.getValue()+"\n");
     		}
     		for(Map.Entry<Integer, Double> m:list2){
     			outQH.write(m.getKey()+","+m.getValue()+"\n");
     		}
     		outPA.close();
     		outQH.close();
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
    public void GetUsersList(){
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
    public void subCompute(){
    	
    	read2();//得到了repo_userslistMap
    	try{
    		BufferedInputStream fis = new BufferedInputStream(new FileInputStream(Constants.RootFile+"outs\\wsUsersRepoNum.txt"));
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"utf-8"),5*1024*1024);
			String line = null;
			String[] words;
			int userCount=0, repoCount = 0;
			List<Map.Entry<Integer,Integer>> mappingList = new ArrayList<Map.Entry<Integer,Integer>>(NodeQMap.entrySet()); 
		
			 for(Map.Entry<Integer,Integer> mapping:mappingList){ 
				//   System.out.println(mapping.getKey()+":"+mapping.getValue()); 
				  Integer userid = mapping.getKey();
				  //以这个userid为起点，
				  
			 }
			
			while((line = reader.readLine())!=null){
				words = line.split(",");
				Integer userid = Integer.valueOf(words[0]);
				userCount++;
			//	Integer repoids = Integer.valueOf(words[1]);
				String[] repoids = words[1].split(" ");
				for(String rid:repoids){
					Integer repoid = Integer.valueOf(rid);
					repoCount++;
					
				}
			}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    
    /*0622晚删除那些入度很小的repo,和出度很小的user
     * 从重新写的RepoUsers和UserRepos文件中
     * 
     * */
    public void remove(){
    	try{
    		File file = new File(Constants.RootFile+"outs\\wsUsersRepos.txt");
    		BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file));
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"utf-8"),5*1024*1024);
			
			BufferedWriter out = new BufferedWriter(new FileWriter(Constants.RootFile+"outs\\newWsUserRepos.txt"));
			int count=0,c1=0,c2=0,c10=0,c3 = 0;
			String line = null;
			String[] words;
			int i=0;
			while((line = reader.readLine())!=null){
				words = line.split(" ");
				int len = words[1].split(",").length;
				if(len==1){
					c1++;
				}
				if(len==2){
					c2++;
				}
				if(len==3){
					c3++;
				}
				if(len>3){
					count++;
					out.write(line+"\n");
				}
//					else{
//					break;
//				}
			}
			System.out.println(c1+" c2: "+c2+" c3: "+c3+" count: "+count);
			out.close();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void run(String time){
    	this.time = time;
    	reHITS();
    }
    public static void main(String[] args) {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();
		WatchersHits wh = new WatchersHits();
		wh.reHITS();
		long end = System.currentTimeMillis();
		System.out.println("计算用时： "+(end - start)/1000);
	}
	
}

