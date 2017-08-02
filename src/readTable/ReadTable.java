package readTable;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import prePro.UserPrePro;

import utils.Constants;
import entity.Follower;
import entity.Project;
import entity.User;
import entity.Watcher;

public class ReadTable {
	public String tablePath = "D:\\github\\mysql-2015-09-25\\dump\\";
		//	public String tablePath = "D:\\github\\mysql-2015-09-25\\mydum\\";
	public ArrayList<String> readFile(String filePath){
		ArrayList<String> list = new ArrayList<String>();
		try{
			BufferedInputStream fis = new BufferedInputStream(new FileInputStream(filePath));
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"utf-8"),5*1024*1024);
			String line = null;
			while((line = reader.readLine())!=null){
				list.add(line);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	public ArrayList<User> UsersList = new ArrayList<User>();
	//id,login,name,company,location,email,created_at,type,fake,deleted
    public ArrayList<User> readUsers(){ 
    	String file = tablePath + "users.csv";
    	System.out.println("read  "+file+", waiting......");
		String [] words;
		String line = null;
        try{
        	ArrayList<String> usersLine = readFile(file);
        	int gap = (int) (usersLine.size()/100);
    		int count=0;
    		int i=0;
    		for(int j=0;j<usersLine.size();j++){
    			line = usersLine.get(j);
    			User u = new User();
    			words = line.split(",");
    			if(words[0].compareTo("id")==0){
    				continue;
    			}
    			if(words.length<10){
    				continue;
    			}
    		//	System.out.println(line);
    			count++;
    			
    			if(count%gap==0){
    				i++;
    				System.out.print(i+"%..");
    				if(i%20==0){
    					System.out.println();
    				}
    			}
    		//	System.out.println();
    			//id,login,name,company,location,email,created_at,type,fake,deleted
    			//0, 1      2      3       4       5     6         7     8    9
    			if(words[7].equals("ORG")){
    				continue;
    			}
    			u.setId(Integer.valueOf(words[0]));
//    			u.setLogin(words[1]);
//    			u.setName(words[2]);
//    			u.setCompany(words[3]);
//    			u.setLocation(words[4]);
//    			u.setEmail(words[5]);
//    			u.setCreated_at(words[6]);
//    			
//    			u.setType(words[7]);
//    			u.setFake(Boolean.valueOf(words[8]));
//    			u.setDeleted(Boolean.valueOf(words[9]));
    			UsersList.add(u);
    			
    		}	
		}catch(Exception e){
			System.out.println(line);
			e.printStackTrace();
		}
		return UsersList;
    }

    ArrayList<Project> projectsList = new ArrayList<Project>();
    public ArrayList<Project> readProject(){ 
    	//File file = new File(filename);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
    	System.out.println("读文件 projects "+",waiting......");
    	
        try {
    			ArrayList<String> projectsLine = readFile(tablePath+"projects.csv");
    			String line = null;
    			String [] words;
    			int count=0;
    			int i=0;
    			int gap = (int) (projectsList.size()/100);
    			for(int j=0;j<projectsList.size();j++){//id,url ,owner_id
    				line = projectsLine.get(j);
    				Project p = new Project();
    				words = line.split(",");
    				count++;
    				if(count%gap==0){//共3033,4258行
    					i++;
    					System.out.print(i+"%..");
    					if(i%20==0){
    						System.out.println();
    					}
    				}
    				//id,url,owner_id,name,description,language,created_at,fored_from,deleted
    				p.setId(Integer.valueOf(words[0]));
    				p.setUrl(words[1]);
    				p.setOwer_id(Integer.valueOf(words[2]));
    				p.setName(words[3]);
    				p.setDescription(words[4]);
    				p.setLanguage(words[5]);
    				p.setCreated_at(words[6]);
    				p.setForked_from(Integer.valueOf(words[7]));
    				p.setDeleted(Short.valueOf(words[8]));
    				projectsList.add(p);
    			}	
		}catch(Exception e){
			e.printStackTrace();
		}
		return projectsList;
    }

    public ArrayList<Follower> FollowersList = new ArrayList<Follower>();
    public ArrayList<Follower> readFollowers(){  	
    	String FollowersFile = tablePath + "followers.csv";
    	System.out.println("读文件 "+",waiting......");
        try{
        	ArrayList<String> followersLine = readFile(FollowersFile);
        	String line = null;
    		String [] words;
    		int count=0;
    		int i=0;
			int gap = (int) (followersLine.size()/100);
    		for(int j=0;j<followersLine.size();j++){
    			//user_id,follower_id,created_At
    			line = followersLine.get(j);
    		//	System.out.println("--"+line);
    			Follower f = new Follower();
    			words = line.split(",");
    			if(words[0].compareTo("id")==0){
    				continue;
    			}
    			count++;

    			if(count%gap==0){
    				i++;
    				System.out.print(i+"%..");
    				if(i%20==0){
    					System.out.println();
    				}
    			}
    			Integer uid = Integer.valueOf(words[0]);
    			Integer fid = Integer.valueOf(words[1]);
    			String created_at = words[2];
    			f.setFollower_id(fid);
    			f.setUser_id(uid);
    			f.setCrated_at(created_at);
    			FollowersList.add(f);
    		}	
		}catch(Exception e){
			e.printStackTrace();
		}
		return FollowersList;
    }
    public ArrayList<Watcher> WatchersList = new ArrayList<Watcher>();
    public ArrayList readWatchers(){  //repo_id,user_id,created_at
    	String FollowersFile = tablePath + "watchers.csv";
    	System.out.println("读文件 "+",waiting......");
        try{
        	ArrayList<String> followersLine = readFile(FollowersFile);
        	String line = null;
    		String [] words;
    		int count=0;
    		int i=0;
//			int gap = (int) (followersLine.size()/100);
    		for(int j=0;j<followersLine.size();j++){
    			//user_id,follower_id,created_At
    			line = followersLine.get(j);
    		//	System.out.println("--"+line);
    			Watcher w = new Watcher();
    			words = line.split(",");
    			if(words[0].compareTo("id")==0){
    				continue;
    			}
    			count++;

//    			if(count%gap==0){
//    				i++;
//    				System.out.print(i+"%..");
//    				if(i%20==0){
//    					System.out.println();
//    				}
//    			}
    			Integer repo_id = Integer.valueOf(words[0]);
    			Integer user_id = Integer.valueOf(words[1]);
    			String created_at = words[2];
    			w.setRepo_id(repo_id);
    			w.setUser_id(user_id);
    			w.setCreated_at(created_at);
    			WatchersList.add(w);
    		}	
		}catch(Exception e){
			e.printStackTrace();
		}
    	return WatchersList;
    }
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ReadTable rt = new ReadTable();
		rt.readFollowers();
		System.out.println(rt.FollowersList.size());
	}
}
