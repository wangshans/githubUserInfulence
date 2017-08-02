package pagerank;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import utils.BF;
import utils.Constants;
import entity.Follower;

public class FollowersPageRank {
	/**
	 * @param args
	 */
	 /* 阀值 */  
    public static double MAX = 0.00000000001;  
    public static double getMAX() {
		return MAX;
	}

	public void setMAX(double mAX) {
		MAX = mAX;
	}
	/* 阻尼系数 */  
    public static double alpha = 0.85;  
   
 //   public static Map<String, HtmlEntity> map = new HashMap<String, HtmlEntity>();
    public static List<Follower> list = new ArrayList<Follower>();  
    public static Map<Integer,Double > initMap = new HashMap<Integer,Double>();//初始化的pr
    public static Map<Integer,Double > prMap = new HashMap<Integer,Double>();//计算过程中的pr
    public static TreeMap<Integer,ArrayList<Integer> > MyfollowerMap = new TreeMap<Integer,ArrayList<Integer> >();//入度
 //   public Map<Integer,ArrayList<Integer> > MyfollowingMap = new HashMap<Integer,ArrayList<Integer> >();//出度
    public static Map<Integer,Integer > MyOutNumMap = new HashMap<Integer,Integer>();//出度
 //   public static Map<Integer,Integer > MyInNumMap = new HashMap<Integer,Integer>();//出度
  //  public Map<Integer,Integer> FollowerMap = new HashMap<Integer,Integer>();//Follower中出现的用户
    
	/*读文件
	 * 初始化的PR值
	 * 计算点的出度
	 * 计算点的入度
	 * 计算temp
	 * PR公式
	 * 递归
	 * 阀值
	 * 排序
	 */
    
	//读入文件,并计算出度入度
   // String path = ;
    File usrFile = new File(Constants.dumpFile+"\\features\\USR\\usersInfo.csv");
    File newFollowersFile = new File(usrFile.getParent()+"\\followers.csv");
    public File FollowersUSR(){//已选USR之间的Follow关系
    	File outFile = new File(usrFile.getParent()+"\\followers.csv");
	//	File usersFP = new File(Constants.dirname+"\\screen\\tables\\users.csv");
    	System.out.println("read  "+usrFile.getName()+", waiting......");
		String [] words;
		String line = null;
		int i=0;
		Set<Integer> uids = new TreeSet<Integer>();
        try{
        	BufferedReader reader = BF.readFile(usrFile);
    		while((line=reader.readLine())!=null){
    			i++;
    			if(i%100000==0){
    				System.out.print(i/100000+" ");
    				if(i/100000%25==0){
    					System.out.println();
    				}
    			}
    			words = line.split(",");
    			int userid = Integer.valueOf(words[0]);;
    			uids.add(userid);
    		}
    		System.out.println("\n USERFP number is "+uids.size());
    		
    		
    	//	followersUsersFP.createNewFile();
        //    FileWriter fw = new FileWriter(reposUsersFP); 
            PrintWriter out = new PrintWriter(
            		new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),"utf-8")));
            
            String file = Constants.dirname+"\\dump\\followers.csv";
            File repos = new File(file);
        	System.out.println("read  "+repos.getName()+", waiting......");
        	BufferedReader reader2 = BF.readFile(repos.getPath());
    		i=0;
    		int count=0;
    		while((line=reader2.readLine())!=null){
    			i++;
    			if(i%100000==0){
    				System.out.print(i/100000+" ");
    				if(i/100000%25==0){
    					System.out.println();
    				}
    				out.flush();
    			}
    			words = line.split(",");
    			Integer userid = Integer.valueOf(words[0]);
    			Integer fid = Integer.valueOf(words[1]);
    			if(uids.contains(userid)){//该Follower关系的uid在选中列表中
    				if(uids.contains(fid)){//该Follower关系的fid在选中列表中
    					out.write(line+"\n");
    					count++;
    				}
    			}
    			
    		}
    		System.out.println("\n所有Follower关系数量： "+i);
    		System.out.println("\n筛选后Follower关系数量："+count);
    		out.close();
        }catch(Exception e){
			System.out.println(i+" "+line);
			e.printStackTrace();
		}
        return outFile;
	}
    public void OutInDgree(){
    	//File file = new File(filename);   
    	
    	System.out.println("计算出度 ，入度，以及初始化 "+",waiting......");
		BufferedInputStream fis;
		BufferedReader reader=null;
	//	File file = new File(Constants.screenFile+"tables\\followers.csv");  //htmldoc为html文件夹
		File file = newFollowersFile;
	//		File file = new File("E:\\mysql-2015-09-25\\testPR.csv");  //htmldoc为html文件夹
        try{
    		fis = new BufferedInputStream(new FileInputStream(file));//files[j]
			reader = new BufferedReader(new InputStreamReader(fis),5*1024*1024);
			int i=0;
			String line = null;
			String [] words;
			while((line=reader.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%25==0){
						System.out.println();
					}
				}
				words = line.split(",");
				if(words[0].compareTo("id")==0){
					continue;
				}
				Integer fid = Integer.valueOf(words[0]);
				Integer uid = Integer.valueOf(words[1]);

				//节点的入度链
				ArrayList<Integer> array = MyfollowerMap.get(uid);
				if(array==null){
					ArrayList<Integer> a = new ArrayList<Integer>();
					a.add(fid);
					MyfollowerMap.put(uid,a );
				}else{
					array.add(fid);
					MyfollowerMap.put(uid,array );
				}
				
				
				//节点的出度数
				Integer o = MyOutNumMap.get(fid);
				if(o==null){
					MyOutNumMap.put(fid, 1);
				}else{
					o++;
					MyOutNumMap.put(fid, o);
				}
				
		
			}
		}catch(Exception e){
			e.printStackTrace();
		}
    }
    
    public void init(){
    	System.out.println("\nPR的初始化");
    	String line = "";
    	int i=0;
    	try{
    	//	File usersFP = new File(Constants.screenFile+"\\tables\\users.csv");
        	BufferedReader reader = BF.readFile(usrFile,"utf8");
        	System.out.println("read  "+usrFile.getName()+", waiting......");
    		while((line=reader.readLine())!=null){
    			i++;
    			if(i%100000==0){
    				System.out.print(i/100000+" ");
    				if(i/100000%25==0){
    					System.out.println();
    				}
    			}
    			String [] words = line.split(",");
    			int userid = Integer.valueOf(words[0]);;
    			initMap.put(userid, 0.0);
    		}
    		System.out.println("\n USERs number is "+initMap.size());
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    //Pagerank 过程
    private static HashMap doPageRank() {
        try{
			Iterator<Integer> temp_init = initMap.keySet().iterator();
			while(temp_init.hasNext()){
				Integer uid = temp_init.next();
				double temp = 0;
				//计算与本节点相关的总值
				if(MyfollowerMap.get(uid)!=null){
					
					for(Integer fid:MyfollowerMap.get(uid)){
						int outdgree = MyOutNumMap.get(fid);
						if(outdgree!=0 && fid!=uid){
							temp = temp + initMap.get(fid)/outdgree;//当前PR值除以出度；
						}
					}	
				}
				double pr = (1-alpha)+alpha*temp;
				prMap.put(uid, pr);
			}

        }catch(Exception e){
        	e.printStackTrace();
        }//
       
        return (HashMap) prMap;  
    }
    private static boolean checkMax() {  
        boolean flag = true; 
        Iterator<Integer> temp_init = initMap.keySet().iterator();
		
		while(temp_init.hasNext()){
			Integer uid = temp_init.next();
//			if(Math.abs(prMap.get(uid) - initMap.get(uid))<= MAX){
//				 BF.writeFile(uid+","+prMap.get(uid), FolPRResult, true);
//			}else 
			if(Math.abs(prMap.get(uid) - initMap.get(uid))> MAX) {  
				double d = prMap.get(uid) - initMap.get(uid);
            	flag = false;    
            	break;  
			}  
        }  	
        return flag;  
    }
    
    public Map<Integer,Double> rePR() throws IOException{
    
    	int i =1;
    	long start = System.currentTimeMillis();
        
    	//Constants.followers;five
    	init();
    	OutInDgree();
    	long end = System.currentTimeMillis();
    	System.out.println("读数据 计算用时="+(end-start)/1000);
    	System.out.println("开始pageRank的递归计算~~~~~~~");
    	prMap = doPageRank();  
    	
       while (!(checkMax())) {  //递归过程
    	   System.out.print("迭代"+i+" ");
    	   if(i%10==0){
    		   System.out.println();
    	   }
        	i++;
        	//将pr复制给init
        	Iterator<Integer> tt = initMap.keySet().iterator();
    		while(tt.hasNext()){
    			Integer uid = tt.next();
    			initMap.put(uid, prMap.get(uid));
    		}
            prMap = doPageRank();  
        } 
        BF.Log("总共进行了    "+i+"次迭代。");
        System.out.println("总共进行了    "+i+"次迭代。");
  	 
        //排序
        List<Map.Entry<Integer, Double>> maplist = new ArrayList<Map.Entry<Integer, Double>>(prMap.entrySet());
        Collections.sort(maplist,new Comparator<Map.Entry<Integer, Double>>(){  
			public int compare(Map.Entry<Integer, Double> e1, Map.Entry<Integer,Double> e2) {  //重载Comparator的compare函数
               return e2.getValue().compareTo(e1.getValue());  
            }  
			
        });  
 //       输出到文件
        String result = usrFile.getParent()+"\\usrPageRankScore.csv";
        BufferedWriter out = new BufferedWriter(new FileWriter(result));
        for(Map.Entry<Integer, Double> m:maplist){
        	out.write(m.getKey()+","+m.getValue()+"\n");
        }
        out.close();
//        for(Map.Entry<Integer, Double> m:maplist){
//			System.out.println(m.getKey()+","+m.getValue());
//        }
        return prMap;
        
      

    }
    public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
    	FollowersPageRank pr = new FollowersPageRank();
    	pr.setMAX(0.0000000001);
    	long start = System.currentTimeMillis();
    //	pr.FollowersUSR(); 
    	pr.rePR();
    
    	long end = System.currentTimeMillis();
    	System.out.println("PR 计算用时="+(end-start)/1000);
	}

}
