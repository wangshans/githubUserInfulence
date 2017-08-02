package readTable;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import utils.Constants;
import entity.Project;
import entity.Watcher;

public class ReadTable2 {
	
	public static Map<Integer,ArrayList<Integer> > UserandListMap = new HashMap();//入度
    public static Map<Integer,ArrayList<Integer> > OtherandListMap = new HashMap();//出度
    public static Map<Integer,Integer > UserNumMap = new HashMap();//出度
    public static Map<Integer,Integer > OtherNumMap = new HashMap();//出度
    
	public void read(){
		
	}
	public void printNum(){
		System.out.println("UserandListMap : "+UserandListMap.size());	
		System.out.println("OtherandListMap : "+OtherandListMap.size());
		System.out.println("UserNumMap : "+UserNumMap.size());	
		System.out.println("OtherNumMap : "+OtherNumMap.size());
	}
	public void printDetail(){
		System.out.println("UserandListMap : "+UserandListMap.size());
		Iterator<Integer> t3 = UserandListMap.keySet().iterator();
		while(t3.hasNext()){
    		Integer repoid = t3.next();
    		ArrayList<Integer> a = UserandListMap.get(repoid);
    		System.out.println("\t"+repoid+" "+a.size());
    	
    	}
		
		System.out.println("UserNumMap : "+UserNumMap.size());
		Iterator<Integer> t2 = UserNumMap.keySet().iterator();
    	while(t2.hasNext()){
    		Integer repoid = t2.next();
    		Integer num = UserNumMap.get(repoid);
    	//	System.out.println("\t"+repoid+" "+num);
    	
    	}
    	System.out.println("OtherandListMap : "+OtherandListMap.size());
		Iterator<Integer> t4 = OtherandListMap.keySet().iterator();
		while(t4.hasNext()){
    		Integer userid = t4.next();
    		ArrayList<Integer> a = OtherandListMap.get(userid);
    		System.out.println("\t"+userid+" "+a.size());
    	
    	}
		System.out.println("OtherNumMap : "+OtherNumMap.size());
		
    	Iterator<Integer> t = OtherNumMap.keySet().iterator();
    	while(t.hasNext()){
    		Integer userid = t.next();
    		Integer num = OtherNumMap.get(userid);
    		if(num!=1){
    		//	System.out.println("\t"+userid+" "+num);
    		}
    	}
	}
	 public static void PutMaptoTxt(String table){
	    	try{
	    	System.out.println("put the four maps to txt...please wait...");
	    		String f1 = "D:\\mysql-2015-09-25\\PRResult\\MAP\\"+table+"_UserandList.txt";
				String f2 = "D:\\mysql-2015-09-25\\PRResult\\MAP\\"+table+"_OtherandList.txt";
				String f3 = "D:\\mysql-2015-09-25\\PRResult\\MAP\\"+table+"_UserNum.txt";
				String f4 = "D:\\mysql-2015-09-25\\PRResult\\MAP\\"+table+"_OtherNum.txt";
				
				BufferedWriter out1 =  new BufferedWriter(new FileWriter(f1));
				BufferedWriter out2 =  new BufferedWriter(new FileWriter(f2));
				BufferedWriter out3 =  new BufferedWriter(new FileWriter(f3));
				BufferedWriter out4 =  new BufferedWriter(new FileWriter(f4));
				
				
				Iterator<Integer> t1 = UserandListMap.keySet().iterator();
				while(t1.hasNext()){
					Integer uid = t1.next();
					out1.write(uid+" : ");		
					for(Integer t:UserandListMap.get(uid)){
						out1.write(t+" ");
					}
					out1.write("\n");
				}
				
				Iterator<Integer> t2 = OtherandListMap.keySet().iterator();
				while(t2.hasNext()){
					Integer fid = t2.next();
					out2.write(fid+" : ");
					for(Integer t:OtherandListMap.get(fid)){
						out2.write(t+" ");
					}
					out2.write("\n");
				}
				
				Iterator<Integer> t3 = UserNumMap.keySet().iterator();
				while(t3.hasNext()){
					Integer uid = t3.next();
					Integer tnum = UserNumMap.get(uid);
					out3.write(uid+" "+tnum+"\n");
				}
		
				Iterator<Integer> t4 = OtherNumMap.keySet().iterator();
					while(t4.hasNext()){
						Integer uid = t4.next();
						Integer tnum = OtherNumMap.get(uid);
						out4.write(uid+" "+tnum+"\n");
					}
				
				out1.close();
				out2.close();
				out3.close();
				out4.close();
				
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
				
			
	    }
}
/*
 * newread table
 * 
	
	public static Map<Integer,ArrayList<Integer> > NodePOutListMap = new HashMap();//第一种节点以及出度列表
	public static Map<Integer,ArrayList<Integer> > NodePInListMap = new HashMap();//第一种节点以及其入度列表
	public static Map<Integer,ArrayList<Integer> > NodeQOutListMap = new HashMap();//第二种节点以及其出度列表
    public static Map<Integer,ArrayList<Integer> > NodeQInListMap = new HashMap();//第二种节点以及其入度列表
    
	ArrayList<Project> ProjectsList = new ArrayList();
	ArrayList<Watcher> WatchersList = new ArrayList();
	
	public ArrayList<Project> readProjects(){
		try{
			BufferedInputStream fis = new BufferedInputStream(new FileInputStream(Constants.ProjectsFile));
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"utf-8"),5*1024*1024);
			String line = null;
			String[] words;
			while((line = reader.readLine())!=null){
				words = line.split(",");
				Project project = new Project();
				project.setId(Integer.valueOf(words[0]));
				project.setUrl(words[1].substring(1, words[1].length()-1));
				project.setOwer_id(Integer.valueOf(words[2]));
				project.setName(words[3].substring(1, words[3].length()-1));
				project.setDescription(words[4].substring(1, words[4].length()-1));
				project.setLanguage(words[5].substring(1, words[5].length()-1));
				String splitedTime = words[6].substring(1, words[6].length()-1);//去掉引号
				Timestamp times = Timestamp.valueOf(splitedTime);  
				project.setCreated_at(times);
				if(words[7].equals("\\N")){
				//	System.out.println(words[0]+" forked from is \\n");
					words[7] = "0";
				}
				project.setForked_from(Integer.valueOf(words[7]));
				project.setDeleted(Short.valueOf(words[8]));
				ProjectsList.add(project);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println(ProjectsList.size());
		return ProjectsList;
	}
	
	public ArrayList<Watcher> readWatchersList(){
		try{
			BufferedInputStream fis = new BufferedInputStream(new FileInputStream(Constants.WatchersFile));
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"utf-8"),5*1024*1024);
			String line = null;
			String[] words;
			while((line = reader.readLine())!=null){
				words = line.split(",");
				Watcher w = new Watcher();
				Integer repo_id = Integer.valueOf(words[0]);
				Integer user_id = Integer.valueOf(words[1]);
				//NodeP  --- Repo
				//NodeQ  --- User
				//在这里，只有user指向repo的关系，也就是说，repo只有入度，而user只有出度
				ArrayList c = NodePInListMap.get(repo_id);
				if(c ==null){//该repo还没有放入Map中,则将其加入MAp， 同时将该行的user加入其入度list
					ArrayList cc = new ArrayList();
					cc.add(user_id);
					NodePInListMap.put(repo_id, cc);
				}else
				{
					c.add(user_id);
					NodePInListMap.put(repo_id, c);
				}
				//对user来说只有出度
				ArrayList r = NodeQOutListMap.get(user_id);
				if(r == null){
					ArrayList rr = new ArrayList();
					rr.add(repo_id);
					NodeQOutListMap.put(user_id, rr);
				}else{
					r.add(repo_id);  
					NodeQOutListMap.put(user_id, r);
					}
				
				
				w.setRepo_id(Integer.valueOf(words[0]));
				w.setUser_id(Integer.valueOf(words[1]));//去掉引号
				Timestamp times = Timestamp.valueOf(words[2].substring(1, words[2].length()-1)); 
				w.setCreated_at(times);
				WatchersList.add(w);
				System.out.println("\t P: "+ repo_id+" "+NodePInListMap.get(repo_id).size());
				System.out.println("\t Q: "+ user_id+" "+NodeQOutListMap.get(user_id).size());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("\t Q: "+NodeQOutListMap.size()+"\t P: "+NodePInListMap.size());
		return WatchersList;
	}
	public void readWatchers(){
		try{
			BufferedInputStream fis = new BufferedInputStream(new FileInputStream(Constants.WatchersFile));
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"utf-8"),5*1024*1024);
			String line = null;
			String[] words;
			while((line = reader.readLine())!=null){//watchers: repo_id, user_id, created_at
				words = line.split(",");
				
			}
		}catch(Exception e){
			
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NewReadTable rt = new NewReadTable();
//		rt.readProjects();
//		
//		for(Project p:rt.ProjectsList){
//			p.printProject();
//		}
	//	rt.readWatchers();
		rt.readWatchersList();
		System.out.println(rt.WatchersList.size());
//		for(Watcher w:rt.WatchersList){
//			w.printWatcher();
//		}
		
//		String s = "\"\"";
//		System.out.print(s+"," +s.substring(1, s.length()-1)+", ");
	}
 * */