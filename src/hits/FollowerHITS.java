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
import utils.Constants;

public class FollowerHITS {
	double max = 0.00000000001;//停止迭代的差值临界点
	int iter = 1000;//默认迭代次数
//	String path = "E:\\mysql-2015-09-25\\screen\\";
	public static Map<Integer,Double > AuthMap = new HashMap<Integer,Double>();//authority
    public static Map<Integer,Double > HubMap = new HashMap<Integer,Double>();//hub
    public TreeMap<Integer,ArrayList<Integer> > userFollowersListMap = new TreeMap<Integer,ArrayList<Integer>>();//hub
    public TreeMap<Integer,ArrayList<Integer> > userFolloweesListMap = new TreeMap<Integer,ArrayList<Integer>>();//hub

    String path = Constants.dumpFile+"features\\USR\\";
    public void getFollowersListMap(){//读取网络关系到Map
    	try{
    		String line = null;
    		int i=0;
    		File usersFP = new File(path+"userFollowersList.csv");
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
					userFollowersListMap.put(uid,null);//初始化为1.0
				}else{
					ArrayList<Integer> list = new ArrayList<Integer>();
					String[] fersS = words[1].split(" ");
					
						
					for(String s:words[1].split(" ")){
						list.add(Integer.valueOf(s));
					}
					userFollowersListMap.put(uid,list );
				}
				
			}
			System.out.println("FollowersMap: "+userFollowersListMap.size());
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void getFolloweesListMap(){//读取网络关系到Map
    	try{
    		String line = null;
    		int i=0;
    		File usersFP = new File(path+"userFolloweesList.csv");
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
					userFollowersListMap.put(uid,null);//初始化为1.0
				}else{
					ArrayList<Integer> list = new ArrayList<Integer>();
					for(String s:words[1].split(" ")){
						list.add(Integer.valueOf(s));
					}
					userFolloweesListMap.put(uid,list );
				}
			}
			System.out.println("FollowingsMap: "+userFolloweesListMap.size());
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void init(){//初始化，为1
    	String line = null;
		int i=0;
        try{
	    	File usersFP = new File(path+"usersInfo.csv");
	    	BufferedReader reader = BF.readFile(usersFP,"utf8");
	    	System.out.println("read  "+usersFP.getName()+", waiting......");
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
				AuthMap.put(uid, 1.0);//初始化为1.0
				HubMap.put(uid, 1.0);//初始化为1.0
			}
			System.out.println("AuthMap: "+AuthMap.size());
		
        }catch(Exception e){
        	e.printStackTrace();
        }
    }
    public void doHits(){
    	getFollowersListMap();
    	Iterator<Integer> t1 = userFollowersListMap.keySet().iterator();
    	while(t1.hasNext()){
			Integer uid = t1.next();
		//	System.out.print(uid+", followers ：  ");
		//	AuthMap.put(uid, 0.0);
			double auth = 0.0;
			double Anorm =0.0;
			
			ArrayList<Integer> followersList = userFollowersListMap.get(uid);
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
    	}
    	userFollowersListMap.clear();
    	getFolloweesListMap();
    	Iterator<Integer> t2 = userFolloweesListMap.keySet().iterator();
    	while(t2.hasNext()){
    		Integer uid = t2.next();
		//	System.out.print(", following:  ");
	   //     HubMap.put(uid, 0.0);
	        double hub = 0.0;
	        double Hnorm = 0.0;
        	ArrayList<Integer> followingsList = userFolloweesListMap.get(uid);
 	     //   System.out.println(uid+" : "+followingList.size());
        	if(followingsList==null){
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
    	userFolloweesListMap.clear();
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
//	   	System.out.println("读取表数据~~~~~~~");
//	    getFollowersListMap();
//	    getFollowingsListMap();
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
   
   public void AHtoTXT(){
	   	//将结果插入数据库
		   System.out.println("将Authority和Hub值写入文本.....");
	       try{
	    	   
	    	   BufferedWriter outA = new BufferedWriter(
						new FileWriter(path+"userAuthority.csv"));
	    	 
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
					new FileWriter(path+"userHub.csv"));
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
    	FollowerHITS h = new FollowerHITS();
    	long start = System.currentTimeMillis();
    	h.reHITS();
    	h.AHtoTXT();
    	long end = System.currentTimeMillis();
    	System.out.println("计算用时（秒）："+(end-start)/1000);
    }
}