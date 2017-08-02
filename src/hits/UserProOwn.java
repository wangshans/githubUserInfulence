//package hits;
//
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.FileWriter;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//
//import readTable.ReadFollowers;
//import utils.BF;
//public class UserProOwn {
//
//		/**
//		 * @param args
//		 */
//		double max = 0.00000001;//停止迭代的差值临界点
//		int iter = 1000;//默认迭代次数
//		public static Map<Integer,Double > AuthMap = new HashMap<Integer,Double>();//authority
//	    public static Map<Integer,Double > HubMap = new HashMap<Integer,Double>();//hub
//	    
//	    ReadFollowers rf = new ReadFollowers();
//	    
//	    public void init(){//初始化，为1
//	    	rf.readFollowers();
//	    	System.out.println("FollowerMap: "+rf.FollowerMap.size());
//	    	System.out.println("MyfollowerMap: "+rf.MyfollowerMap.size());
//	    	System.out.println("MyfollowingMap: "+rf.MyfollowingMap.size());
//	    	Iterator<Integer> t1 = rf.FollowerMap.keySet().iterator();
//			while(t1.hasNext()){
//				Integer uid = t1.next();
//				AuthMap.put(uid, 1.0);//初始化为1.0
//				HubMap.put(uid, 1.0);//初始化为1.0
//			//	Integer num = rf.FollowerMap.get(uid);
//			} 	
//	    }
//	    public void doHits(){
//	    	
//	    	Iterator<Integer> t1 = rf.FollowerMap.keySet().iterator();
//	    	while(t1.hasNext()){
//				Integer uid = t1.next();
//			//	System.out.print(uid+", followers ：  ");
//				AuthMap.put(uid, 0.0);
//				double auth = 0.0;
//				double Anorm =0.0;
//				if(rf.MyfollowerMap.get(uid)==null){
//					//System.out.println(" 没有followers ");
//				}else{
//					ArrayList<Integer> followerList = rf.MyfollowerMap.get(uid);
//					//System.out.println(uid+" : "+followerList.size());
//					
//					Iterator it1 = followerList.iterator();
//					while(it1.hasNext()){
//			        	Integer fid = (Integer) it1.next();
//			        	auth += HubMap.get(fid);
//			        	Anorm += Math.pow(HubMap.get(fid), 2); // calculate the sum of the squared auth values to normalise	      
//			        	
//			    //        System.out.print(fid+" ");
//			        }
//					Anorm = Math.sqrt(Anorm);//平方和的开方，用于归一化
//					if(Anorm!=0){
//						AuthMap.put(uid, auth/Anorm);//
//					}else{
//						AuthMap.put(uid, 0.0);
//					}
//					
//				}
//			//	System.out.print(", following:  ");
//		        HubMap.put(uid, 0.0);
//		        double hub = 0.0;
//		        double Hnorm = 0.0;
//		        
//		        if(rf.MyfollowingMap.get(uid)==null){
//		        //	System.out.println(" 没有followering ");
//		        }else{
//		        	 ArrayList<Integer> followingList = rf.MyfollowingMap.get(uid);
//		 	        
//		 	     //   System.out.println(uid+" : "+followingList.size());
//		 			Iterator it2 = followingList.iterator();
//		 			while(it2.hasNext()){
//		 	        	Integer fingid = (Integer) it2.next();
//		 	        	hub += AuthMap.get(fingid);
//		 	        	Hnorm += Math.pow(AuthMap.get(fingid), 2);
//		 	    //        System.out.print(fingid+" ");
//		 	        }
//		 			Hnorm = Math.sqrt(Hnorm);
//		 			if(Hnorm!=0){
//		 				HubMap.put(uid, hub/Hnorm);//归一化
//		 			}else{
//		 				HubMap.put(uid, 0.0);
//		 			}
//		        }
//		  //     System.out.println(" ");
//			} 
//	    }
//	   public void print(){//结果输出
//		   	Iterator<Integer> t1 = AuthMap.keySet().iterator();
//	   		while(t1.hasNext()){
//				Integer uid = t1.next();
//				System.out.println(uid+"： Auth is  "+AuthMap.get(uid)+" ,Hub is "+HubMap.get(uid));
//	   		}
//	   }
//	   
//	   //循环迭代
//	   public void reHITS(){
//		   
//			System.out.println("开始HITS的迭代计算~~~~~~~");
//			boolean flag = false;
//			Map<Integer,Double > CheckAuthMap = new HashMap<Integer,Double>();//authority
//			Map<Integer,Double > CheckHubMap = new HashMap<Integer,Double>();//hub
//			int i=0;//记录迭代次数
//			while(!flag){
//				Iterator<Integer> t1 = AuthMap.keySet().iterator();
//				while(t1.hasNext()){
//					Integer uid = t1.next();
//					CheckAuthMap.put(uid, AuthMap.get(uid));
//			  		}
//			  	Iterator<Integer> t2 = HubMap.keySet().iterator();
//			  	while(t2.hasNext()){
//					Integer uid = t2.next();
//					CheckHubMap.put(uid, HubMap.get(uid));
//			  	}
//			  	doHits();
//			  	i++;
//			  	Iterator<Integer> t3 = AuthMap.keySet().iterator();
//		  		while(t3.hasNext()){
//					Integer uid = t3.next();
//					double da = AuthMap.get(uid)-CheckAuthMap.get(uid);
//					if(Math.abs(da)>max){
//						flag = false;
//						break;
//					}
//					double dh = HubMap.get(uid)-CheckHubMap.get(uid);
//					if(Math.abs(dh)>max){
//						flag = false;
//						break;
//					}
//					flag = true;
//		  		}
//			}
//				
//	    //    BF.Log("总共进行了    "+i+"次迭代。");
//	        System.out.println("总共进行了    "+i+"次迭代。");
//	   }
//	   public void reHITS(int iter){
//		   
//			System.out.println("开始HITS的迭代计算~~~~~~~");
//			boolean flag = false;
//			Map<Integer,Double > CheckAuthMap = new HashMap<Integer,Double>();//authority
//			Map<Integer,Double > CheckHubMap = new HashMap<Integer,Double>();//hub
//			int i=0;//记录迭代次数
//			while(iter>0){
//			  	doHits();
//			  	iter--;
//			}
//	     //  System.out.println("总共进行了    "+i+"次迭代。");
//	  }
//	   
//	   public void AHtoDB(){
//	   	//将结果插入数据库
//		   System.out.println("将Authority和Hub值插入到数据库.....");
//	       try{
//				String connectStr = "jdbc:mysql://localhost:3306/github";  
//		        connectStr += "?useServerPrepStmts=false&rewriteBatchedStatements=true";  
//		        String insert_sql = "INSERT INTO github.followers_hits(user_id,authority,hub) VALUES (?,?,?)"; 
//		      //  System.out.println(insert_sql);
//		        String charset = "gbk";  
//		        boolean debug = true;  
//		        String username = "root";  
//		        String password = "123456";  
//				Class.forName("com.mysql.jdbc.Driver");  
//				Connection conn = DriverManager.getConnection(connectStr, username,password);  
//		        conn.setAutoCommit(false); // 设置手动提交  
//		        int count = 0;  
//		        PreparedStatement psts = conn.prepareStatement(insert_sql);
//		       
//				Iterator<Integer> t3 = AuthMap.keySet().iterator();
//		  		while(t3.hasNext()){
//					Integer uid = t3.next();
//					psts.setInt(1, uid);
//			        psts.setDouble(2, AuthMap.get(uid));
//			        psts.setDouble(3, HubMap.get(uid));
//			        psts.addBatch();          // 加入批量处理  
//			        if(count%1000 ==0){
//			        	psts.executeBatch(); // 执行批量处理  
//			 	        conn.commit();  // 提交  
//			        }
//		  		}
//				
//				psts.executeBatch(); // 执行批量处理  
//		        conn.commit();  // 提交  
//		        System.out.println("All down : " + count);  
//		        conn.close(); 
//	       }catch(ClassNotFoundException e){
//	       	e.printStackTrace();
//	       }catch(SQLException e){
//	       	e.printStackTrace();
//	       }catch(Exception e){
//	       	e.printStackTrace();
//	       }           
//	       
//	   }
//	   public void AHtoTXT(){
//		   	//将结果插入数据库
//			   System.out.println("将Authority和Hub值写入文本.....");
//		       try{
//					BufferedWriter out = new BufferedWriter(new FileWriter("D:\\github\\AH_Followers.txt"));
//					int count=0;
//					Iterator<Integer> t3 = AuthMap.keySet().iterator();
//			  		while(t3.hasNext()){
//			  			count++;
//						Integer uid = t3.next();
//						out.write(uid+", ");
//						out.write(AuthMap.get(uid)+", ");
//				        out.write(HubMap.get(uid)+"\n");
//				         // 加入批量处理  
//				        if(count%15000 ==0){
//				        	System.out.print(count/15000+"%..");
//				        	if((count/15500)%20==0){
//				        		System.out.println();
//				        	}
//				        }
//			  		}
//			  		System.out.println();
//			        System.out.println("All down : " + count);  
//			        out.close(); 
//		       }catch(Exception e){
//		       	e.printStackTrace();
//		       }           
//		       
//		   }
//	   public static void main(String[] args){
//	    	FollowerHITS h = new FollowerHITS();
//	    	long start = System.currentTimeMillis();
//	    	h.init();
//	    	long end0 = System.currentTimeMillis();
//	    	System.out.println("初始化计算用时（秒）："+(end0-start)/1000);
//	    //	System.out.println(h.AuthMap.size());
//	    //	h.doHits();
//	    	h.reHITS(1000);
//	    	long end1 = System.currentTimeMillis();
//	    	System.out.println("迭代计算用时（秒）："+(end1-end0)/1000);
//	    //	h.AHtoDB();
//	    	h.AHtoTXT();
//	    //	h.print();
//	    	long end2 = System.currentTimeMillis();
//	    	System.out.println("存储计算用时（秒）："+(end2-end1)/1000);
//	    	System.out.println("Total 计算用时（秒）："+(end2-start)/1000);
//	    }
//	}
//
////		 G := set of pages
//	//2 for each page p in G do
//	//3   p.auth = 1 // p.auth is the authority score of the page p
//	//4   p.hub = 1 // p.hub is the hub score of the page p
//	//5 function HubsAndAuthorities(G)
//	//6   for step from 1 to k do // run the algorithm for k steps
//	//7     norm = 0
//	//8     for each page p in G do  // update all authority values first
//	//9       p.auth = 0
//	//10       for each page q in p.incomingNeighbors do // p.incomingNeighbors is the set of pages that link to p
//	//11          p.auth += q.hub
//	//12       norm += square(p.auth) // calculate the sum of the squared auth values to normalise
//	//13     norm = sqrt(norm)
//	//14     for each page p in G do  // update the auth scores 
//	//15       p.auth = p.auth / norm  // normalise the auth values
//	//16     norm = 0
//	//17     for each page p in G do  // then update all hub values
//	//18       p.hub = 0
//	//19       for each page r in p.outgoingNeighbors do // p.outgoingNeighbors is the set of pages that p links to
//	//20         p.hub += r.auth
//	//21       norm += square(p.hub) // calculate the sum of the squared hub values to normalise
//	//22     norm = sqrt(norm)
//	//23     for each page p in G do  // then update all hub values
//	//24       p.hub = p.hub / norm   // normalise the hub values
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
//	}
//
//}
