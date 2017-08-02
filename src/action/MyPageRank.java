package action;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
import entity.Follower;

public class MyPageRank {
	String filename = "D:\\mysql-2015-09-25\\mydum\\followers.csv";//followers
	static String FolPRResult = "D:\\mysql-2015-09-25\\PRResult\\FolPRResult.csv";
	
	static String logtxt = "D:\\mysql-2015-09-25\\PRResult\\dlog.txt";
	String tablepath = "D:\\mysql-2015-09-25\\mydum\\";
	/**
	 * @param args
	 */
	 /* 阀值 */  
    public static double MAX = 0.00000000001;  
    public static double getMAX() {
		return MAX;
	}

	public static void setMAX(double mAX) {
		MAX = mAX;
	}
	/* 阻尼系数 */  
    public static double alpha = 0.85;  
   
 //   public static Map<String, HtmlEntity> map = new HashMap<String, HtmlEntity>();
    public static List<Follower> list = new ArrayList<Follower>();  
    public static Map<Integer,Double > initMap = new HashMap<Integer,Double>();//初始化的pr
    public static Map<Integer,Double > prMap = new HashMap<Integer,Double>();//计算过程中的pr
    public static TreeMap<Integer,ArrayList<Integer> > MyfollowerMap = new TreeMap<Integer,ArrayList<Integer> >();//入度
    public Map<Integer,ArrayList<Integer> > MyfollowingMap = new HashMap<Integer,ArrayList<Integer> >();//出度
    public static Map<Integer,Integer > MyOutNumMap = new HashMap<Integer,Integer>();//出度
    public static Map<Integer,Integer > MyInNumMap = new HashMap<Integer,Integer>();//出度
    public Map<Integer,Integer> FollowerMap = new HashMap<Integer,Integer>();//Follower中出现的用户
    
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
    
    public void readTable(String table){
    	table = tablepath+table;
    	File file = new File(table);   
		System.out.println("load file from "+table+",waiting......");
		
		BufferedInputStream fis;
		BufferedReader reader=null;
		try {
			fis = new BufferedInputStream(new FileInputStream(file));
			reader = new BufferedReader(new InputStreamReader(fis,"utf-8"),5*1024*1024);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // 用5M的缓冲读取文本文件  
		  
		String line = "";
		String[] splitedWords;
		try {
			 while((line = reader.readLine()) != null){
				 
				 splitedWords = line.split(" ");
				 Integer uid = Integer.valueOf(splitedWords[0]);
				 Integer fid = Integer.valueOf(splitedWords[1]);
				 //
				 ArrayList<Integer> c = MyfollowerMap.get(uid);
				 if(c==null){
					 c = new ArrayList<Integer>();
					 c.add(fid);//将该粉丝加入到我的粉丝列表
					 MyfollowerMap.put(uid, c);
				 }else{
					 c.add(fid);
					 MyfollowerMap.put(uid, c);
				 }
				 //
				 ArrayList<Integer> fing = MyfollowingMap.get(fid);
				 if(fing==null){
					 fing = new ArrayList<Integer>();
					 fing.add(uid);//将该偶像加入到我的偶像列表
					 MyfollowingMap.put(fid, fing);
				 }else{
					 fing.add(uid);
					 MyfollowingMap.put(fid, fing);
				 }
			 }
		}catch(Exception e){
			e.printStackTrace();
		}
			 
    }
	
    public void OutD(File file){
    	//File file = new File(filename);   
    	
    	System.out.println("计算出度 ，入度，以及初始化 "+",waiting......");
		BufferedInputStream fis;
		BufferedReader reader=null;
//		File file = new File("D:\\mysql-2015-09-25\\splited_followers");  //htmldoc为html文件夹
 //     File[] files = file.listFiles();
        try {
 //       	for(int j=0;j<files.length;j++){
        	//	System.out.println("load file from "+files[j].getPath()+",waiting......");
        		
        		fis = new BufferedInputStream(new FileInputStream(file));//files[j]
    			reader = new BufferedReader(new InputStreamReader(fis),5*1024*1024);
    			
    			String line = null;
    			String [] words;
    			while((line=reader.readLine())!=null){
    				words = line.split(",");
    				if(words[0].compareTo("id")==0){
    					continue;
    				}
    				Integer uid = Integer.valueOf(words[0]);
    				Integer fid = Integer.valueOf(words[1]);
    				
    				//在Follower网络中出现的所有节点以及出现次数
    				Integer f = FollowerMap.get(fid);
    				if(f==null){
    					FollowerMap.put(fid, 1);
    				}else{
    					f++;
    					FollowerMap.put(fid, f);
    				}
    				Integer u = FollowerMap.get(uid);
    				if(u==null){
    					FollowerMap.put(uid, 1);
    				}else{
    					u++;
    					FollowerMap.put(uid, u);
    				}
    				//入度数
    				Integer c = MyInNumMap.get(uid);
    				if(c==null){
    					MyInNumMap.put(uid, 1);
    				}else{
    					c++;
    					MyInNumMap.put(uid, c);
    				}
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
    				
    				//节点的出度链
    				ArrayList<Integer> aing = MyfollowingMap.get(fid);
    				if(aing==null){
    					ArrayList<Integer> a = new ArrayList<Integer>();
    					a.add(uid);
    					MyfollowingMap.put(fid,a );
    				}else{
    					aing.add(uid);
    					MyfollowingMap.put(fid,aing );
    				}
    			}
    	
        	//PR的初始化，INITmAP
        	System.out.println("PR的初始化，INITmAP");
			Iterator<Integer> temp_itmap =FollowerMap.keySet().iterator();
			
			while(temp_itmap.hasNext()){
				Integer tkey = temp_itmap.next();
				Integer tnum = MyOutNumMap.get(tkey);
				initMap.put(tkey, 0.0d);//初始化为0
			}	
		}catch(Exception e){
			e.printStackTrace();
		}
    }
    
    //  
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
        }
       
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
    
    public void rePR(){
    	System.out.println("开始pageRank的递归计算~~~~~~~");
    	int i =1;
    	prMap = doPageRank();  
    	
       while (!(checkMax())) {  //递归过程
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
   //     输出到文件
        String FolPRResult = "Data\\result\\"+time+"UserPR.txt";
        for(Map.Entry<Integer, Double> m:maplist){
			BF.writeFile(m.getKey()+","+m.getValue(), FolPRResult,true);
        }

    }
    public void prtoDB(){
    	//将结果插入数据库
        try{
			String connectStr = "jdbc:mysql://localhost:3306/github";  
	        connectStr += "?useServerPrepStmts=false&rewriteBatchedStatements=true";  
	        String insert_sql = "INSERT INTO github.userPR(user_id,user_pr) VALUES (?,?)"; 
	        System.out.println(insert_sql);
	        String charset = "gbk";  
	        boolean debug = true;  
	        String username = "root";  
	        String password = "123456";  
			Class.forName("com.mysql.jdbc.Driver");  
        	Connection conn = DriverManager.getConnection(connectStr, username,password);  
 	        conn.setAutoCommit(false); // 设置手动提交  
 	        int count = 0;  
 	        PreparedStatement psts = conn.prepareStatement(insert_sql);
 	        BufferedReader reader = BF.readFile("D:\\mysql-2015-09-25\\PRResult\\UserPR_sort.csv");
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
    String time = "2015-2";
    public void run(String time){
    	this.time = time;
    	File follower = new File("Data\\spliFollowersByTime\\fol"+this.time+".txt");
    	OutD(follower);
    	rePR();
    }
    public static void main(String[] args) {
		// TODO Auto-generated method stub
    	MyPageRank mypr = new MyPageRank();
    	mypr.setMAX(0.000000001);
    	long start = System.currentTimeMillis();
    	File seven = new File("D:\\mysql-2015-09-25\\mydum\\seven.csv");
    	File five = new File("D:\\mysql-2015-09-25\\mydum\\five.csv");
    	File file = new File("D:\\mysql-2015-09-25\\splited_followers");  //htmldoc为html文件夹
    	File four = new File("D:\\mysql-2015-09-25\\mydum\\four2.csv"); 
    //	File follower = new File("testData\\followers.txt");
    	
   // 	File[] files = file.listFiles();
    /*	for(int j=0;j<files.length;j++){
    	   	System.out.println("load file from "+files[j].getPath()+",waiting......");
    		mypr.OutD(files[j]);//Constants.followers;five
    	  }
    	  */
    	mypr.time = "2015-2";
    	File follower = new File("Data\\spliFollowersByTime\\fol"+mypr.time+".txt");
    	mypr.OutD(follower);
    	long end = System.currentTimeMillis();
    	System.out.println("读数据 计算用时="+(end-start)/1000);
    	
    //	PutMaptoTxt(FolPRResult);//
    	mypr.rePR();
    	long end2 = System.currentTimeMillis();
    	System.out.println("PR 计算用时="+(end2-end)/1000);
    //	mypr.prtoDB();
    //	long end3 = System.currentTimeMillis();
    //	BF.Log("计算用时="+(end-start)/1000);
	//	System.out.println("插入数据库计算用时="+(end3-end2)/1000);
	}

}

//       Iterator<Integer> temp_init = prMap.keySet().iterator();
//ArrayList<Follower> flist = new ArrayList<Follower>();
//while(temp_init.hasNext()){
//	Integer uid = temp_init.next();
//	Follower f = new Follower();
//	f.setId(uid);
//	f.setPr(prMap.get(uid));
//	flist.add(f);
//}

//String FolPRResult = "D:\\mysql-2015-09-25\\PRResult\\UserPR.csv";
//BF.writeFile("", FolPRResult, true);
//for(Follower fo:flist){  
//	BF.writeFile(fo.getId()+","+fo.getPr(), FolPRResult, true);
// } 

//public void getAllUserInF(){
//	/*先读follower中的所有用户到Map
//	 * 然后再读User，看是否在Map中
//	 * */
////	File file = new File(filename);
//	System.out.println("loading file from "+Constants.followers.getPath());
//	
//	try{
//		BufferedInputStream fis = new BufferedInputStream(new FileInputStream(Constants.followers));
//		BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"utf-8"),5*1024*1024);
//		
//		String line = null;
//		String [] words;
//		while((line=reader.readLine())!=null){
//			words = line.split(",");
//			if(words[0].compareTo("id")==0){
//				continue;
//			}
//			Integer uid = Integer.valueOf(words[0]);
//			Integer fid = Integer.valueOf(words[1]);
//			
//			Integer f = FollowerMap.get(fid);
//			if(f==null){
//				FollowerMap.put(fid, 1);
//			}else{
//				f++;
//				FollowerMap.put(fid, f);
//			}
//			Integer u = FollowerMap.get(uid);
//			if(u==null){
//				FollowerMap.put(fid, 1);
//			}else{
//				u++;
//				FollowerMap.put(fid, u);
//			}
//		}	
//		
//		Iterator<Integer> temp_itmap =FollowerMap.keySet().iterator();
//		
//		while(temp_itmap.hasNext()){
//			Integer tkey = temp_itmap.next();
//			Integer tnum = MyOutNumMap.get(tkey);
//			initMap.put(tkey, 0.0d);//初始化为0
//		}
//	//	System.out.println("记录初始PR值的堆————initMap.size: "+initMap.size());
//	//	printPRmap(initMap);
//	}catch(Exception e){
//		e.printStackTrace();
//	}
//}


//打印<Integer,Double>Map
//public static void printPRmap(Map m){
//	Iterator<Integer> temppr = m.keySet().iterator();
//	int i=0;
//    while(temppr.hasNext()){
//    if(i<10){
//    	i++;
//    	Integer key = temppr.next();
//		Double value = (Double) m.get(key);
//		System.out.print(key+"-"+value+" ");		
//	}else{
//		break;
//	}
//    }
//}
//public static void printPRmap(Map m,String mname,BufferedWriter out) throws IOException{
//	Iterator<Integer> temppr = m.keySet().iterator();
//	int i=0;
//	out.write(mname);
//	out.write("\n"+"\t");
//    while(temppr.hasNext()){
//    if(i<10){
//    	i++;
//    	Integer key = temppr.next();
//		Double value = (Double) m.get(key);
//		out.write(key+"-"+value+" ");
//		System.out.print(key+"-"+value+" ");		
//	}else{
//		break;
//	}
//    }
//}
//public static void printIIMap(Map m,String mname,BufferedWriter out) throws IOException{
//	Iterator<Integer> temppr = m.keySet().iterator();
//	int i=0;
//	out.write(mname);
//	out.write("\n"+"\t");
//    while(temppr.hasNext()){
//    if(i<10){
//    	i++;
//    	Integer key = temppr.next();
//    	Integer value = (Integer) m.get(key);
//		out.write(key+"-"+value+" ");
//		System.out.print(key+"-"+value+" ");		
//	}else{
//		break;
//	}
//    }
//}
