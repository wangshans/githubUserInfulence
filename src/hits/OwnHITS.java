package hits;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import utils.BF;

public class OwnHITS {

	/**
	 * @param args
	 */
	double max = 0.00000000001;//停止迭代的差值临界点
	int iter = 1000;//默认迭代次数
	String path = "E:\\mysql-2015-09-25\\screen\\";
	public static Map<Integer,Double > AuthMap = new HashMap<Integer,Double>();//authority
    public static Map<Integer,Double > HubMap = new HashMap<Integer,Double>();//hub
    public TreeMap<Integer,ArrayList<Integer> > FollowersListMap = new TreeMap<Integer,ArrayList<Integer>>();//hub
    public TreeMap<Integer,ArrayList<Integer> > FollowingsListMap = new TreeMap<Integer,ArrayList<Integer>>();//hub

    public void getFollowersListMap(){//读取网络关系到Map
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
					FollowersListMap.put(uid,null);//初始化为1.0
				}else{
					ArrayList<Integer> list = new ArrayList<Integer>();
					String[] fersS = words[1].split(" ");
					
						
					for(String s:words[1].split(" ")){
						list.add(Integer.valueOf(s));
					}
					FollowersListMap.put(uid,list );
				}
				
			}
			System.out.println("FollowersMap: "+FollowersListMap.size());
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void getFollowingsListMap(){//读取网络关系到Map
    	try{
    		String line = null;
    		int i=0;
    		File usersFP = new File(path+"features\\allfeatures\\userFollowingsList.csv");
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
					FollowersListMap.put(uid,null);//初始化为1.0
				}else{
					ArrayList<Integer> list = new ArrayList<Integer>();
					for(String s:words[1].split(" ")){
						list.add(Integer.valueOf(s));
					}
					FollowingsListMap.put(uid,list );
				}
			}
			System.out.println("FollowingsMap: "+FollowingsListMap.size());
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void init(){//初始化，为1
    	String line = null;
		int i=0;
        try{
	    	File usersFP = new File(path+"tables\\users.csv");
	    	BufferedReader reader = BF.readFile(usersFP,"utf8");
	    	System.out.println("read  "+usersFP.getName()+", waiting......");
			while((line=reader.readLine())!=null){
				i++;
				if(i%10000==0){
					System.out.print(i/10000+" ");
					if(i/10000%25==0){
						System.out.println();
					}
				}
				String [] words = line.split(",");
				int uid = Integer.valueOf(words[0]);;
				AuthMap.put(uid, 1.0);//初始化为1.0
				HubMap.put(uid, 1.0);//初始化为1.0
			}
			System.out.println("AuthMap: "+AuthMap.size());
		
        }catch(Exception e){
        	e.printStackTrace();
        }
    }
    public void doHits(){
    	
    	Iterator<Integer> t1 = FollowersListMap.keySet().iterator();
    	while(t1.hasNext()){
			Integer uid = t1.next();
		//	System.out.print(uid+", followers ：  ");
		//	AuthMap.put(uid, 0.0);
			double auth = 0.0;
			double Anorm =0.0;
			
			ArrayList<Integer> followersList = FollowersListMap.get(uid);
			//System.out.println(uid+" : "+followerList.size());
			if(followersList==null){
			//	AuthMap.put(uid, 0.0);
			}else{
				for(Integer ferid:followersList){
		        	auth += HubMap.get(ferid);
		        	Anorm += Math.pow(HubMap.get(ferid), 2); // calculate the sum of the squared auth values to normalise	      
		    //        System.out.print(fid+" ");
		        }
				Anorm = Math.sqrt(Anorm);//平方和的开方，用于归一化
				if(Anorm!=0){
					AuthMap.put(uid, auth/Anorm);//
				}else{
					AuthMap.put(uid, 0.0);
				}
			}
			
		//	System.out.print(", following:  ");
	   //     HubMap.put(uid, 0.0);
	        double hub = 0.0;
	        double Hnorm = 0.0;
        	ArrayList<Integer> followingsList = FollowingsListMap.get(uid);
 	     //   System.out.println(uid+" : "+followingList.size());
        	if(followersList==null){
        	//	HubMap.put(uid, 0.0);
			}else{
	 			for(Integer fingid:followingsList){
	 	        	hub += AuthMap.get(fingid);
	 	        	Hnorm += Math.pow(AuthMap.get(fingid), 2);
	 	    //        System.out.print(fingid+" ");
	 	        }
	 			Hnorm = Math.sqrt(Hnorm);//平方和的开方
	 			if(Hnorm!=0){
	 				HubMap.put(uid, hub/Hnorm);//归一化
	 			}else{
	 				HubMap.put(uid, 0.0);
	 			}
		  //     System.out.println(" ");
			}
		} 
    }
   public void print(){//结果输出
	   	Iterator<Integer> t1 = AuthMap.keySet().iterator();
   		while(t1.hasNext()){
			Integer uid = t1.next();
			System.out.println(uid+"： Auth is  "+AuthMap.get(uid)+" ,Hub is "+HubMap.get(uid));
   		}
   }
   
   //循环迭代
   public void reHITS(){
	   	System.out.println("读取表数据~~~~~~~");
	    getFollowersListMap();
	    getFollowingsListMap();
	    System.out.println("初始化~~~~~~~");
	    init();
		System.out.println("开始HITS的迭代计算~~~~~~~");
		boolean flag = false;
		Map<Integer,Double > CheckAuthMap = new HashMap<Integer,Double>();//authority
		Map<Integer,Double > CheckHubMap = new HashMap<Integer,Double>();//hub
		int i=0;//记录迭代次数
		while(!flag){
			Iterator<Integer> t1 = AuthMap.keySet().iterator();
			while(t1.hasNext()){
				Integer uid = t1.next();
				CheckAuthMap.put(uid, AuthMap.get(uid));
		  		}
		  	Iterator<Integer> t2 = HubMap.keySet().iterator();
		  	while(t2.hasNext()){
				Integer uid = t2.next();
				CheckHubMap.put(uid, HubMap.get(uid));
		  	}
		  	doHits();
		  	i++;
		  	if(i%10==0)
		  		System.out.print(i+" ");
		  	Iterator<Integer> t3 = AuthMap.keySet().iterator();
	  		while(t3.hasNext()){
				Integer uid = t3.next();
				double da = AuthMap.get(uid)-CheckAuthMap.get(uid);
				if(Math.abs(da)>max){
					flag = false;
					break;
				}
				double dh = HubMap.get(uid)-CheckHubMap.get(uid);
				if(Math.abs(dh)>max){
					flag = false;
					break;
				}
				flag = true;
	  		}
		}
        System.out.println("总共进行了    "+i+"次迭代。");
   }
   public void reHITS(int iter){
	   
		System.out.println("开始HITS的迭代计算~~~~~~~");
		boolean flag = false;
		Map<Integer,Double > CheckAuthMap = new HashMap<Integer,Double>();//authority
		Map<Integer,Double > CheckHubMap = new HashMap<Integer,Double>();//hub
		int i=0;//记录迭代次数
		while(iter>0){
		  	doHits();
		  	iter--;
		}
     //  System.out.println("总共进行了    "+i+"次迭代。");
  }
   
   public void AHtoDB(){
   	//将结果插入数据库
	   System.out.println("将Authority和Hub值插入到数据库.....");
       try{
			String connectStr = "jdbc:mysql://localhost:3306/github";  
	        connectStr += "?useServerPrepStmts=false&rewriteBatchedStatements=true";  
	        String insert_sql = "INSERT INTO github.followers_hits(user_id,authority,hub) VALUES (?,?,?)"; 
	      //  System.out.println(insert_sql);
	        String charset = "gbk";  
	        boolean debug = true;  
	        String username = "root";  
	        String password = "123456";  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection conn = DriverManager.getConnection(connectStr, username,password);  
	        conn.setAutoCommit(false); // 设置手动提交  
	        int count = 0;  
	        PreparedStatement psts = conn.prepareStatement(insert_sql);
	       
			Iterator<Integer> t3 = AuthMap.keySet().iterator();
	  		while(t3.hasNext()){
				Integer uid = t3.next();
				psts.setInt(1, uid);
		        psts.setDouble(2, AuthMap.get(uid));
		        psts.setDouble(3, HubMap.get(uid));
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
   public void AHtoTXT(){
	   	//将结果插入数据库
		   System.out.println("将Authority和Hub值写入文本.....");
	       try{
	    	   
	    	   BufferedWriter outA = new BufferedWriter(
						new FileWriter(path+"features\\userAuthority.csv"));
	    	 
	    	   int count=0;
	    	   //排序
	    	   List<Map.Entry<Integer,Double>> list = new ArrayList(AuthMap.entrySet());
	    	   
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
			//	outH.write(uid+","+HubMap.get(uid)+"\n");
		        
	  		}
	  		System.out.println();
	        System.out.println("All down : " + count);  
	        outA.close(); 
	        count=0;
	        BufferedWriter outH = new BufferedWriter(
					new FileWriter(path+"features\\userHub.csv"));
	        //排序
    	   List<Map.Entry<Integer,Double>> listH = new ArrayList(HubMap.entrySet());
    	   
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
   public static void main(String[] args){
    	OwnHITS h = new OwnHITS();
    	long start = System.currentTimeMillis();
    	h.reHITS();
    	h.AHtoTXT();
    	long end = System.currentTimeMillis();
    	System.out.println("计算用时（秒）："+(end-start)/1000);
    }

}
