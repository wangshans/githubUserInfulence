package wss.githubFeatures;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.BF;
import utils.Constants;

public class ScreenUSR {

	
/*	 @param args
//	 数据预处理
//
 * 
 * 	  1.对User进行筛选：
 * 		1.1 格式预处理
 * 		1.2 选择有Follower的用户
 * 		1.3 选择有repos的用户
 * 2.对repos进行筛选
 *     2.1 选择拥有者在筛选后的user中项目
 * 3.对Followers筛选
 *     3.1 没有在筛选后的user中出现的uid和Followerid 删除
 *     
 */
	public void userswithFollowers(){//有Followers的用户
		String followers = Constants.dirname + "dump\\followers.csv";
    	System.out.println("read  "+followers+", waiting......");
		String [] words;
		String line = null;
		int i=0;
		Set<Integer> uids = new TreeSet<Integer>();
        try{
        	BufferedReader reader = BF.readFile(followers,"gbk");
    		while((line=reader.readLine())!=null){
    			i++;
    			if(i%100000==0){
    				System.out.print(i/100000+" ");
    				if(i/100000%25==0){
    					System.out.println();
    				}
    			}
    			words = line.split(",");
    			int userid = Integer.valueOf(words[1]);
    			int follower_id = Integer.valueOf(words[0]);
    			uids.add(userid);
    		}
    		reader.close();
    		System.out.println("followers总行数: "+i);
    		System.out.println("有Followers的用户数量： "+uids.size()+"......");
    		
        	File outF = new File(Constants.screenFile+"usersWithFWithoutDF.csv");//
        	outF.createNewFile();
            PrintWriter out = new PrintWriter(new BufferedWriter(
            		new OutputStreamWriter(new FileOutputStream(outF),"utf-8")));
            BufferedWriter outFnU = new BufferedWriter(new FileWriter(
            		Constants.dirname+"\\screen\\usersInFollowersNotInUsers.csv"));
           	File users = new File(Constants.screenFile+ "usersWithoutDF.csv");//
    	//  File users = new File(Constants.dirname+"\\preproed\\users.csv");//+"\\preproed\\"+ "users.csv"
        	BufferedReader reader2 = new BufferedReader(new FileReader(users),5*1024*1024);//如果是读大文件  则    即，设置缓存

    		i=0;
    		int count=0;
    	//	TreeSet<Integer> uidsSet = new TreeSet<Integer>();
    		while((line=reader2.readLine())!=null){
    			i++;
    			if(i%100000==0){
    				System.out.print(i/100000+" ");
    				if(i/100000%25==0){
    					System.out.println();
    				}
    				out.flush();
    			//	break;
    			}
    			words = line.split(",");
    			if(words.length==0)
    				continue;
    			Integer userid = Integer.valueOf(words[0]);
    			if(uids.contains(userid)){
    				out.write(line+"\n");
    				count++;
    				uids.remove(userid);
    			}
    			
    		}
    		System.out.println("\n users with followers number is "+count);
    		System.out.println("\n users in followers but not in users number is"+uids.size());
    		for(Integer id:uids){
    			outFnU.write(id+"\n");
    		}
    		out.close();
    		outFnU.close();
        }catch(Exception e){
			System.out.println(line);
			e.printStackTrace();
		}
	}
	public void userswithRepos(){
		String file = Constants.dirname+"\\screen\\projectsNoD.csv";
    	System.out.println("read  "+file+", waiting......");
		String [] words;
		String line = null;
		int i=0;
		Set<Integer> uidsSet = new TreeSet<Integer>();
        try{
        	BufferedReader reader = BF.readFile(file);
    		
    		while((line=reader.readLine())!=null){
    			i++;
    			if(i%100000==0){
    				System.out.print(i/100000+" ");
    				if(i/100000%25==0){
    					System.out.println();
    				}
    			}
    			words = line.split(",");
    			if(words[0].contains("id")){
    				continue;
    			}
    			if(words.length==0)
    				continue;
    			int userid = Integer.valueOf(words[2]);;
    			uidsSet.add(userid);
    		}
    		System.out.println("\n number is "+uidsSet.size());
    		File usersF = new File(Constants.dirname+"screen\\"+ "users.csv");
            FileWriter fw = new FileWriter(usersF); 
            PrintWriter out = new PrintWriter(
            		new BufferedWriter(new OutputStreamWriter(new FileOutputStream(usersF),"utf-8")));
            
      //     File users = new File(Constants.dirname+"preproed\\"+ "users.csv");
            File users = new File(Constants.dirname+"\\screen\\"+ "usersWithFWithoutDF.csv");
        	System.out.println("read  "+users.getName()+", waiting......");
        	BufferedReader reader2 = BF.readFile(users,"UTF-8");
    		i=0;
    		int count=0;
    		while((line=reader2.readLine())!=null){
    			i++;
    			if(i%10000==0){
    				System.out.print(i/10000+" ");
    				if(i/10000%25==0){
    					System.out.println();
    				}
    				out.flush();
    			}
    			words = line.split(",");
    			Integer userid = Integer.valueOf(words[0]);
    			
    			if(uidsSet.contains(userid)){
    				out.write(line+"\n");
    				count++;
    			}
    			
    		}
    		System.out.println("\n 拥有项目的用户数量： "+count);
    		out.close();
    		
        }catch(Exception e){
			System.out.println(i+" "+line);
			e.printStackTrace();
		}
	}
	
	public void projectsUserSelected(){
		
		File usersFP = new File(Constants.dirname+"\\screen\\tables\\users.csv");
    	System.out.println("read  "+usersFP.getName()+", waiting......");
		String [] words;
		String line = null;
		int i=0;
		Set<Integer> uidsSet = new TreeSet<Integer>();
        try{
        	BufferedReader reader = BF.readFile(usersFP,"utf8");
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
    			uidsSet.add(userid);
    		}
    		System.out.println("\n USERs number is "+uidsSet.size());
    		
    		File reposUsersFP = new File(Constants.screenFile+"tables\\projects.csv");
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter
            		(new FileOutputStream(reposUsersFP),"utf-8")));
            
            String file = Constants.dirname+"\\screen\\projectsND.csv";
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
    			
    		//	System.out.println(line);
    			Integer userid = Integer.valueOf(words[2]);
    			if(uidsSet.contains(userid)){
    				out.write(line+"\n");
    				count++;
    			}
    			
    		}
    		System.out.println("\n所有项目总数：  "+i);
    		System.out.println("\n 筛选后项目数： "+count);
    		out.close();
        }catch(Exception e){
			System.out.println(i+" "+line);
			e.printStackTrace();
		}
	}
	
	
	
	public void FollowersUserSelected(){
		
		File usersFP = new File(Constants.dirname+"\\screen\\tables\\users.csv");
    	System.out.println("read  "+usersFP.getName()+", waiting......");
		String [] words;
		String line = null;
		int i=0;
		Set<Integer> uids = new TreeSet<Integer>();
        try{
        	BufferedReader reader = BF.readFile(usersFP);
    		while((line=reader.readLine())!=null){
    			i++;
    			if(i%10000==0){
    				System.out.print(i/10000+" ");
    				if(i/10000%25==0){
    					System.out.println();
    				}
    			}
    			words = line.split(",");
    			int userid = Integer.valueOf(words[0]);;
    			uids.add(userid);
    		}
    		System.out.println("\n USERFP number is "+uids.size());
    		
    		File followersUsersFP = new File(Constants.dirname+"\\screen\\followers.csv");
    		followersUsersFP.createNewFile();
        //    FileWriter fw = new FileWriter(reposUsersFP); 
            PrintWriter out = new PrintWriter(
            		new BufferedWriter(new OutputStreamWriter(new FileOutputStream(followersUsersFP),"utf-8")));
            
            String file = Constants.dirname+"\\dump\\followers.csv";
            File repos = new File(file);
        	System.out.println("read  "+repos.getName()+", waiting......");
        	BufferedReader reader2 = BF.readFile(repos.getPath());
    		i=0;
    		int count=0;
    		while((line=reader2.readLine())!=null){
    			i++;
    			if(i%10000==0){
    				System.out.print(i/10000+" ");
    				if(i/10000%25==0){
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
	}
	public void watchersUserSelected(){
		
		File usersFP = new File(Constants.dirname+"\\screen\\tables\\users.csv");
    	System.out.println("read  "+usersFP.getName()+", waiting......");
		String [] words;
		String line = null;
		int i=0;
		Set<Integer> uids = new TreeSet<Integer>();
        try{
        	BufferedReader reader = BF.readFile(usersFP);
    		while((line=reader.readLine())!=null){
    			i++;
    			if(i%10000==0){
    				System.out.print(i/10000+" ");
    				if(i/10000%25==0){
    					System.out.println();
    				}
    			}
    			words = line.split(",");
    			if(words[0].contains("id")){
    				continue;
    			}
    			int userid = Integer.valueOf(words[0]);
    			uids.add(userid);
    		}
    		System.out.println("\n USERs number is "+uids.size());
    		
    		File outFile = new File(Constants.dirname+"\\screen\\watchersU.csv");
    		outFile.createNewFile();
            PrintWriter out = new PrintWriter(
            		new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),"utf-8")));
            
            String file = Constants.dirname+"\\dump\\watchers.csv";
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
    			int repoid = Integer.valueOf(words[0]);
    			int userid = Integer.valueOf(words[1]);
    			if(uids.contains(userid)){
    				out.write(line+"\n");
    				count++;
    			}
    		}
    		System.out.println("\n 所有的watcher关系数量：  "+i);
    		System.out.println("\n 筛选后的watcher关系数量 ： "+count);
    	
    		out.close();
    		
    		
        }catch(Exception e){
			System.out.println(i+" "+line);
			e.printStackTrace();
		}
	}
	public void watchersRepoSelected(){//根据被选中的repos对watchers进行筛选
		
		File usersFP = new File(Constants.dirname+"\\screen\\projects.csv");
    	System.out.println("read  "+usersFP.getName()+", waiting......");
		String [] words;
		String line = null;
		int i=0;
		Set<Integer> rids = new TreeSet<Integer>();
        try{
        	BufferedReader reader = BF.readFile(usersFP);
    		while((line=reader.readLine())!=null){
    			i++;
    			if(i%1000000==0){
    				System.out.print(i/1000000+" ");
    				if(i/1000000%25==0){
    					System.out.println();
    				}
    			}
    			words = line.split(",");
    			int rid = Integer.valueOf(words[0]);
    			
    			rids.add(rid);
    		}
    		System.out.println("\n repos number is "+rids.size());
    		
    		File outFile = new File(Constants.dirname+"\\screen\\watchers.csv");
    		outFile.createNewFile();
    		PrintWriter out = new PrintWriter(
    		new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),"utf-8")));
            
       //     String file = Constants.dirname+"\\dump\\watchers.csv";
            String file = Constants.dirname+"\\screen\\watchersU.csv";
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
    			int repoid = Integer.valueOf(words[0]);
    			int userid = Integer.valueOf(words[1]);
    			if(rids.contains(repoid)){
    				out.write(line+"\n");
    				count++;
    			}
    		}
    		System.out.println("\n 筛选前（以User筛选过的）watcher关系数量： "+i);
    		System.out.println("\n 筛选后（以User、Repo筛选过的）watcher关系数量："+count);
    	
    		out.close();
    		
    		
        }catch(Exception e){
			System.out.println(i+" "+line);
			e.printStackTrace();
		}
	}
	public void pull_RequestsHistoryUserSelected(){
		
		File usersFP = new File(Constants.screenFile+"tables\\users.csv");
    	System.out.println("read  "+usersFP.getName()+", waiting......");
		String [] words;
		String line = null;
		int i=0;
		Set<Integer> idsSet = new TreeSet<Integer>();
        try{
        	BufferedReader reader = BF.readFile(usersFP);
    		while((line=reader.readLine())!=null){
    			i++;
    			if(i%100000==0){
    				System.out.print(i/100000+" ");
    				if(i/100000%25==0){
    					System.out.println();
    				}
    			}
    			words = line.split(",");
    			if(words[0].contains("id")){
    				continue;
    			}
    			int rid = Integer.valueOf(words[0]);
    			idsSet.add(rid);
    		}
    		System.out.println("\n Users number is "+idsSet.size());
    		
    		File outFile = new File(Constants.screenFile+"tables\\pull_request_history.csv");
    		outFile.createNewFile();
            PrintWriter out = new PrintWriter(
            		new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),"utf-8")));
            
            String file = Constants.dirname+"\\dump\\pull_request_history.csv";
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
    			int id = Integer.valueOf(words[0]);
    			if(words[words.length-1].equals("\\N")){
    				continue;
    			}
    			Integer actorID = Integer.valueOf(words[words.length-1]);
    			
    			if(idsSet.contains(actorID)){
					out.write(line+"\n");
					count++;
    			}
    		}
    		System.out.println("\n 所有的数量：  "+i);
    		System.out.println("\n 筛选后的数量 ： "+count);
    	
    		out.close();
    		
    		
        }catch(Exception e){
			System.out.println(i+" "+line);
			e.printStackTrace();
		}
	}
	public void pull_RequestsRepoSelected(){
		File usersFP = new File(Constants.screenFile+"tables\\projects.csv");
		System.out.println("read  "+usersFP.getName()+", waiting......");
		String [] words;
		String line = null;
		int i=0;
		Set<Integer> idsSet = new TreeSet<Integer>();
	    try{
	    	BufferedReader reader = BF.readFile(usersFP);
			while((line=reader.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%25==0){
						System.out.println();
					}
				}
				words = line.split(",");
				if(words[0].contains("id")){
					continue;
				}
				int rid = Integer.valueOf(words[0]);
				idsSet.add(rid);
			}
			System.out.println("\n Users number is "+idsSet.size());
			
			File outFile = new File(Constants.screenFile+"tables\\pull_request.csv");
			outFile.createNewFile();
	        PrintWriter out = new PrintWriter(
	        		new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),"utf-8")));
	        
	        String file = Constants.dirname+"\\dump\\pull_requests.csv";
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
				int id = Integer.valueOf(words[0]);
				if(words[1].equals("\\N")||words[2].equals("\\N")){
					continue;
				}
				Integer head_repo_id = Integer.valueOf(words[1]);
				Integer base_repo_id = Integer.valueOf(words[2]);
				
				if(idsSet.contains(head_repo_id)){
					if(idsSet.contains(base_repo_id)){
						out.write(line+"\n");
						count++;
					}
				}
			}
			System.out.println("\n 所有的数量：  "+i);
			System.out.println("\n 筛选后的数量 ： "+count);
		
			out.close();
			
			
	    }catch(Exception e){
			System.out.println(i+" "+line);
			e.printStackTrace();
		}
	}
	
	public void commitsUserScreen(){
		
		File usersFP = new File(Constants.dirname+"\\screen\\tables\\users.csv");
    	System.out.println("read  "+usersFP.getName()+", waiting......");
		String [] words;
		String line = null;
		int i=0;
		Set<Integer> uids = new TreeSet<Integer>();
        try{
        	BufferedReader reader = BF.readFile(usersFP);
    		while((line=reader.readLine())!=null){
    			i++;
    			if(i%10000==0){
    				System.out.print(i/10000+" ");
    				if(i/10000%25==0){
    					System.out.println();
    				}
    			}
    			words = line.split(",");
    			if(words[0].contains("id")){
    				continue;
    			}
    			int userid = Integer.valueOf(words[0]);
    			uids.add(userid);
    		}
    		System.out.println("\n USERs number is "+uids.size());
    		
    		File outFile = new File(Constants.dirname+"\\screen\\commitsU.csv");
    		outFile.createNewFile();
            PrintWriter out = new PrintWriter(
            		new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),"utf-8")));
            
            String file = Constants.dirname+"\\dump\\commits.csv";
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
    			int id = Integer.valueOf(words[0]);
    			int authorid = Integer.valueOf(words[2]);
    			if(uids.contains(authorid)){
    				out.write(line+"\n");
    				count++;
    			}
    		}
    		System.out.println("\n 所有的数量：  "+i);
    		System.out.println("\n (user)筛选后的数量 ： "+count);
    	
    		out.close();
    		
    		
        }catch(Exception e){
			System.out.println(i+" "+line);
			e.printStackTrace();
		}
	}
	public void commitsRepoScreen(){//根据被选中的repos对watchers进行筛选
		
		File usersFP = new File(Constants.dirname+"\\screen\\tables\\projects.csv");
    	System.out.println("read  "+usersFP.getName()+", waiting......");
		String [] words;
		String line = null;
		int i=0;
		Set<Integer> rids = new TreeSet<Integer>();
        try{
        	BufferedReader reader = BF.readFile(usersFP);
    		while((line=reader.readLine())!=null){
    			i++;
    			if(i%1000000==0){
    				System.out.print(i/1000000+" ");
    				if(i/1000000%25==0){
    					System.out.println();
    				}
    			}
    			words = line.split(",");
    			int rid = Integer.valueOf(words[0]);
    			rids.add(rid);
    		}
    		System.out.println("\n repos number is "+rids.size());
    		
    		File outFile = new File(Constants.dirname+"\\screen\\commits.csv");
    		outFile.createNewFile();
    		PrintWriter out = new PrintWriter(
    		new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),"utf-8")));
            
       //     String file = Constants.dirname+"\\dump\\watchers.csv";
            File commitsU = new File(Constants.dirname+"\\screen\\commitsU.csv");
        	System.out.println("read  "+commitsU.getName()+", waiting......");
        	BufferedReader reader2 = BF.readFile(commitsU);
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
    			int id = Integer.valueOf(words[0]);
    			if(words[words.length-2].equals("\\N")){
    				continue;
    			}
    			int repoid = Integer.valueOf(words[words.length-2]);
    			if(rids.contains(repoid)){
    				out.write(line+"\n");
    				count++;
    			}
    		}
    		System.out.println("\n 筛选前（以User筛选过的）Commits数量： "+i);
    		System.out.println("\n 筛选后（以User、Repo筛选过的）Commits数量："+count);
    	
    		out.close();
    		
    		
        }catch(Exception e){
			System.out.println(i+" "+line);
			e.printStackTrace();
		}
	}
	
	public void commitsScreen(){
		commitsUserScreen();
		commitsRepoScreen();
	}
	public void issuesUserScreen(){
		File usersFP = new File(Constants.dirname+"\\screen\\tables\\users.csv");
    	System.out.println("read  "+usersFP.getName()+", waiting......");
		String [] words;
		String line = null;
		int i=0;
		Set<Integer> uids = new TreeSet<Integer>();
        try{
        	BufferedReader reader = BF.readFile(usersFP);
    		while((line=reader.readLine())!=null){
    			i++;
    			if(i%10000==0){
    				System.out.print(i/10000+" ");
    				if(i/10000%25==0){
    					System.out.println();
    				}
    			}
    			words = line.split(",");
    			if(words[0].contains("id")){
    				continue;
    			}
    			int userid = Integer.valueOf(words[0]);
    			uids.add(userid);
    		}
    		System.out.println("\n USERs number is "+uids.size());
    		
    		File outFile = new File(Constants.dirname+"\\screen\\issuesU.csv");
    		outFile.createNewFile();
            PrintWriter out = new PrintWriter(
            		new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),"utf-8")));
            
            String file = Constants.dirname+"\\dump\\issues.csv";
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
    			int id = Integer.valueOf(words[0]);
    			if(words[2].equals("\\N")){
    				continue;
    			}
    			int reporterid = Integer.valueOf(words[2]);
    			if(uids.contains(reporterid)){
    				out.write(line+"\n");
    				count++;
    			}
    		}
    		System.out.println("\n 所有的数量：  "+i);
    		System.out.println("\n (user)筛选后的数量 ： "+count);
    	
    		out.close();
    		uids.clear();
    		
        }catch(Exception e){
			System.out.println(i+" "+line);
			e.printStackTrace();
		}
	}
	
	public void issuesRepoScreen(){//根据被选中的repos对watchers进行筛选
		
		File usersFP = new File(Constants.dirname+"screen\\tables\\projects.csv");
    	System.out.println("read  "+usersFP.getName()+", waiting......");
		String [] words;
		String line = null;
		int i=0;
		Set<Integer> rids = new TreeSet<Integer>();
        try{
        	BufferedReader reader = BF.readFile(usersFP);
    		while((line=reader.readLine())!=null){
    			i++;
    			if(i%10000==0){
    				System.out.print(i/10000+" ");
    				if(i/10000%25==0){
    					System.out.println();
    				}
    			}
    			words = line.split(",");
    			int rid = Integer.valueOf(words[0]);
    			rids.add(rid);
    		}
    		System.out.println("\n repos number is "+rids.size());
    		
    		File outFile = new File(Constants.dirname+"\\screen\\tables\\issues.csv");
    		outFile.createNewFile();
    		PrintWriter out = new PrintWriter(
    		new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),"utf-8")));
            
       //     String file = Constants.dirname+"\\dump\\watchers.csv";
            String file = Constants.dirname+"\\screen\\issuesU.csv";
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
    			int id = Integer.valueOf(words[0]);
    			if(words[1].equals("\\N")){
    				continue;
    			}
    			int repoid = Integer.valueOf(words[1]);
    			if(rids.contains(repoid)){
    				out.write(line+"\n");
    				count++;
    			}
    		}
    		System.out.println("\n 筛选前（以User筛选过的）项目数量： "+i);
    		System.out.println("\n 筛选后（以User、Repo筛选过的）项目数量："+count);
    	
    		out.close();
    		rids.clear();
    		
        }catch(Exception e){
			System.out.println(i+" "+line);
			e.printStackTrace();
		}
	}
	public void issuesScreen(){
		issuesUserScreen();
		issuesRepoScreen();
	}
	public void ForsNetScreen(){//根据被选中的repos对watchers进行筛选
		
		File projects = new File(Constants.screenFile+"tables\\projects.csv");
    	System.out.println("read  "+projects.getName()+", waiting......");
		String [] words;
		String line = null;
		int i=0;
		Set<Integer> rids = new TreeSet<Integer>();
        try{
        	BufferedReader reader = BF.readFile(projects);
    		while((line=reader.readLine())!=null){
    			i++;
    			if(i%100000==0){
    				System.out.print(i/100000+" ");
    				if(i/100000%25==0){
    					System.out.println();
    				}
    			}
    			words = line.split(",");
    			int rid = Integer.valueOf(words[0]);
    			rids.add(rid);
    		}
    		System.out.println("\n repos number is "+rids.size());
    		File outFile = new File(Constants.screenFile+"features\\ProjectsForksNet.csv");
    		PrintWriter out = new PrintWriter(
    				new BufferedWriter(new OutputStreamWriter(
    						new FileOutputStream(outFile),"utf-8")));
            i=0;
    		reader = BF.readFile(projects);
    		while((line=reader.readLine())!=null){
    			i++;
    			if(i%100000==0){
    				System.out.print(i/100000+" ");
    				if(i/100000%25==0){
    					System.out.println();
    				}
    			}
    			words = line.split(",");
    			int rid = Integer.valueOf(words[0]);
    			String forked_from = words[words.length-4];
    			
    			if(forked_from.equals("\\N")){
    				continue;
    			}
    			Integer forkedfrom = Integer.valueOf(forked_from);
    			if(rids.contains(forkedfrom)){
    				out.write(rid+","+forkedfrom+"\n");
    				
    			}
    		}
    		out.close();
    		rids.clear();
    		System.out.println(i);
        }catch(Exception e){
			System.out.println(i+" "+line);
			e.printStackTrace();
		}
	}
	public int countZi(String str,String k){
		int count=0;
		int n = str.indexOf(k);
		while(n!=-1){
			count++;
			str = str.substring(n+k.length());
			n = str.indexOf(k);
		}
	    return count; 
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ScreenUSR u = new ScreenUSR();
		long s = System.currentTimeMillis();
	//	u.preproUsers();
	
//		u.userwithoutFakeDeleted();
//		u.userswithFollowers();
	//	u.userswithRepos();
//		
//		u.projectsUserSelected();
//		u.FollowersUserSelected();
//		u.watchersUserSelected();
//		u.watchersRepoSelected();
//		u.pull_RequestsUserSelected();
//		u.commitsUserScreen();
//		u.commitsRepoScreen();
//		u.issuesUserScreen();
//		u.issuesRepoScreen();
		
		u.pull_RequestsRepoSelected();
		long end = System.currentTimeMillis();
		System.out.println("\nUsed Time : "+(end-s)/1000);
	}
}



