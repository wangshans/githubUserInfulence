package readTable;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ReadWatchers extends ReadTable2 {
    public static Map<Integer,ArrayList<Integer> > WatchersMap = new HashMap();//watch repo的用户，即watcher列表
    public static Map<Integer,ArrayList<Integer> >WatchesMap = new HashMap();//user watch repo的列表
    public static Map<Integer,Integer > RepoNumMap = new HashMap();//repo_id, num，num为出现次数，即该repo的watcher的数量
    public static Map<Integer,Integer > UserNumMap = new HashMap();//user_id， num， num 为user出现次数，即该user watch的repo 的数量
  //  public static Map<Integer,Integer> FollowerMap = new HashMap();//Follower中出现的用户
    public void read(){
    	readewachers();
    	super.UserandListMap = WatchesMap;
    	super.UserNumMap = UserNumMap;
    	super.OtherandListMap = WatchersMap;
    	super.OtherNumMap = RepoNumMap;
    }
	public void readewachers(){
    	//File file = new File(filename);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
    	
    	System.out.println("读文件 wachers "+",waiting......");
		BufferedInputStream fis;
		BufferedReader reader=null;
//		File file = new File("D:\\mysql-2015-09-25\\splited_followers");  //htmldoc为html文件夹
 //     File[] files = file.listFiles();
        try {
 //       	for(int j=0;j<files.length;j++){
        	//	System.out.println("load file from "+files[j].getPath()+",waiting......");
        		
        	//	fis = new BufferedInputStream(new FileInputStream("D:\\mysql-2015-09-25\\mydum\\wachers.csv"));//files[j]
        		fis = new BufferedInputStream(new FileInputStream("D:\\mysql-2015-09-25\\mydum\\watcher.csv"));//files[j]
        	//	File seven = new File("D:\\mysql-2015-09-25\\mydum\\seven.csv");
        	//	fis = new BufferedInputStream(new FileInputStream("D:\\mysql-2015-09-25\\mydum\\four.csv"));//files[j]
    			reader = new BufferedReader(new InputStreamReader(fis,"utf-8"),5*1024*1024);
    			
    			String line = null;
    			String [] words;
    			int count=0;
    			int i=0;
    			while((line=reader.readLine())!=null){ //repo_id, user_id,created_at
    				words = line.split(",");
    				
    				count++;
    				if(count%300000==0){//共3033,4258行
    					i++;
    					System.out.print(i+"%..");
    					if(i%20==0){
    						System.out.println();
    					}
    				}
    				Integer repoid = Integer.valueOf(words[0]);
    				Integer userid = Integer.valueOf(words[1]);
    				
    				//在Repo及其出现次数
    				Integer r = RepoNumMap.get(repoid);
    				if(r==null){
    					RepoNumMap.put(repoid, 1);
    				}else{
    					r++;
    					RepoNumMap.put(repoid, r);
    				}
    				
    				//user以及其出现次数
    				Integer c = UserNumMap.get(userid);
    				if(c==null){
    					UserNumMap.put(userid, 1);
    				}else{
    					c++;
    					UserNumMap.put(userid, c);
    				}
    				//节点的入度链
    				ArrayList<Integer> array = WatchersMap.get(repoid);
    				if(array==null){
    					ArrayList<Integer> a = new ArrayList<Integer>();
    					a.add(userid);
    					WatchersMap.put(repoid,a );
    				}else{
    					array.add(userid);
    					WatchersMap.put(repoid,array);
    				}
    				
    				
    				
    				//节点的出度链
    			//	System.out.println("Following 检测：");
    				
    				ArrayList<Integer> watchList = WatchesMap.get(userid);
    			//	System.out.print("\t"+fid+" : ");
    				if(watchList==null){
    					ArrayList<Integer> a = new ArrayList<Integer>();
    			//		System.out.print(uid+" ");
    					a.add(repoid);
    					WatchesMap.put(userid,a );
    				}else{
    					watchList.add(repoid);
    			//		System.out.print(uid+" ");
    					WatchesMap.put(userid,watchList );
    				}
    			//	System.out.println(",num :"+MyfollowingMap.get(fid));
    			}	
		}catch(Exception e){
			e.printStackTrace();
		}
    }
	
//	public void printNum(){
//		System.out.println("WatchersMap : "+WatchersMap.size());	
//		System.out.println("WatchesMap : "+WatchesMap.size());
//		System.out.println("RepoNumMap : "+RepoNumMap.size());	
//		System.out.println("UserNumMap : "+UserNumMap.size());
//	}
	public static void main(String[] args){
//		ReadWachers rw = new ReadWachers();
//		rw.readewachers();
//		rw.printNum(); 	
		ReadTable2 rt = new ReadWatchers();
		//	ReadTable rt = new ReadTable();
		rt.read();
		rt.printNum();
	}
}
