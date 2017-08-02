package wss.githubFeatures;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import utils.BF;
import utils.Constants;

public class Pull_requests {

	/**
	 * @param args
	 */
	public void baseRepoPull_requestsNum(){//项目收到的pull_request数量，base_repo_id
		try{
			String line = "";
			int i=0;
			TreeMap<Integer,Integer> repoNumMap = new TreeMap<Integer,Integer>();
	    	File usersFP = new File(Constants.screenFile+"tables\\projects.csv");
	    	System.out.println("read  "+usersFP.getName()+", waiting......");
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
				int userid = Integer.valueOf(words[0]);;
				repoNumMap.put(userid, 0);
			}
			System.out.println("\n repos number is "+repoNumMap.size());
			
			BufferedWriter out = new BufferedWriter(
					new FileWriter(Constants.screenFile+"features\\repoPull_requestsNum.csv"));
			
			i=0;
			int count=0;
			File f = new File("E:\\mysql-2015-09-25\\screen\\pull_requests.csv");
			System.out.println(f.getName());
			BufferedReader r1 = BF.readFile(f);
			while((line = r1.readLine())!=null){
				i++;
				if(i%10000==0){
					System.out.print(i/10000+" ");
					if(i/10000%25==0){
						System.out.println();
					}
				}
				String[] words = line.split(",");
				int pull_requestId = Integer.valueOf(words[0]);
				if(words[2].equals("\\N")){
					count++;
					continue;
				}
				int base_repoId = Integer.valueOf(words[2]);

				Integer c = repoNumMap.get(base_repoId);
				if(c==null){
					continue;
				}
				c++;
				repoNumMap.put(base_repoId, c);
			}
			System.out.println("\n所有pull_request数量 ： "+i);
			System.out.println("base repo id 为空的pull_request数量 ： "+count);
			System.out.println("涉及repo数量 ： "+repoNumMap.size());
			for(Map.Entry<Integer, Integer> t:repoNumMap.entrySet()){
				int uid =t.getKey();
				out.write(uid+","+t.getValue()+"\n");
			}
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void headrepoPull_requestsNum(){//项目收到的pull_request数量，base_repo_id
		try{
			String line = "";
			int i=0;
			TreeMap<Integer,Integer> repoNumMap = new TreeMap<Integer,Integer>();
	    	File usersFP = new File(Constants.screenFile+"tables\\projectsFork.csv");
	    	System.out.println("read  "+usersFP.getName()+", waiting......");
	    	BufferedReader reader = BF.readFile(usersFP,"utf-8");
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
				repoNumMap.put(userid, 0);
			}
			System.out.println("\n repos number is "+repoNumMap.size());
			
			BufferedWriter out = new BufferedWriter(
					new FileWriter(Constants.screenFile+"features\\ForkedRepoPull_requestsNum.csv"));
//			BufferedWriter out0 = new BufferedWriter(
//					new FileWriter(Constants.screenFile+"features\\ForkedRepoPull_requestsNum0.csv"));
			
			i=0;
			int count=0;
		//	File f = new File(Constants.screenFile+"tables\\pull_requests.csv");
			File f = new File(Constants.screenFile+"tables\\pull_requests.csv");
			System.out.println(f.getName());
			BufferedReader r1 = BF.readFile(f);
			while((line = r1.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%25==0){
						System.out.println();
					}
				}
				String[] words = line.split(",");
				int pull_requestId = Integer.valueOf(words[0]);
				if(words[1].equals("\\N")){
					count++;
					continue;
				}
				int head_repoId = Integer.valueOf(words[1]);

				Integer c = repoNumMap.get(head_repoId);
				if(c==null){
					continue;
				}
				c++;
				repoNumMap.put(head_repoId, c);
			}
			System.out.println("\n所有pull_request数量 ： "+i);
			System.out.println("head_repoId  为空的pull_request数量 ： "+count);
			System.out.println("涉及repo数量 ： "+repoNumMap.size());
			int k=0;
			for(Map.Entry<Integer, Integer> t:repoNumMap.entrySet()){
				int uid =t.getKey();
				Integer n = t.getValue();
				if(n!=0){
					k++;
				}
				out.write(uid+","+t.getValue()+"\n");
			}
			out.close();
			System.out.println("Forked 有 pull_request ： "+k);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void Pull_requestsTo(){//项目收到的pull_request数量，base_repo_id
		try{
			String line = "";
			int i=0;
			File RepoForkedFile = new File(Constants.screenFile+"tables\\forksNet2.csv");
			TreeMap<Integer,Integer> RepoForkedMap = new TreeMap<Integer,Integer>();
	    	System.out.println("read  "+RepoForkedFile.getName()+", waiting......");
	    	BufferedReader reader = BF.readFile(RepoForkedFile,"utf-8");
			while((line=reader.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%25==0){
						System.out.println();
					}
				}
				if(i<=10000000)
					continue;
				String [] words = line.split(",");
				Integer userid = Integer.valueOf(words[0]);
				Integer forkedFrom = Integer.valueOf(words[1]);
				RepoForkedMap.put(userid, forkedFrom);
			}
			System.out.println("\n repos number is "+RepoForkedMap.size());
			
			BufferedWriter outForked = new BufferedWriter(
					new FileWriter(Constants.screenFile+"features\\pull_requestActionToForkedFrom.csv",true));
			BufferedWriter outSelf = new BufferedWriter(
					new FileWriter(Constants.screenFile+"features\\pull_requestActionToSelf.csv",true));
			BufferedWriter outOthers = new BufferedWriter(
					new FileWriter(Constants.screenFile+"features\\pull_requestActionToOthers.csv",true));
			
			i=0;
			int count=0;
			File f = new File(Constants.screenFile+"tables\\pull_request_action.csv");
			System.out.println(f.getName());
			BufferedReader r1 = BF.readFile(f);
			while((line = r1.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%25==0){
						System.out.println();
					}
				}
				String[] words = line.split(",");
				int pull_requestId = Integer.valueOf(words[0]);
				if(words[1].equals("\\N")){
					continue;
				}
				Integer head_repoId = Integer.valueOf(words[1]);
				if(words[2].equals("\\N")){
					continue;
				}
				Integer base_repoId = Integer.valueOf(words[2]);
				if(base_repoId.compareTo(head_repoId)==0){
					outSelf.write(line+"\n");
					continue;
				}
				Integer c = RepoForkedMap.get(head_repoId);//发起pull_request的项目forkedFrom
				if(c==null)
					continue;
				if(c.compareTo(base_repoId)==0){
				//	System.out.println("xiangdeng");
					count++;
					outForked.write(line+"\n");
				}else{
					outOthers.write(line+"\n");
				}
			}
			System.out.println("\n所有pull_request数量 ： "+i);
			System.out.println("To others pull_request数量 ： "+count);
			outForked.close();
			outSelf.close();
			outOthers.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void pull_rHeadRNum(){//按action为其分类
		try{
			String line = "";
			int i=0;
			TreeMap<Integer,Integer> repoPRNumMap = new TreeMap<Integer,Integer>();
			TreeMap<Integer,Integer> repoPRMergedNumMap = new TreeMap<Integer,Integer>();
			BufferedWriter out = new BufferedWriter(
					new FileWriter(Constants.screenFile+"features\\RepoP_RtsToForkedNumMergedNum.csv"));
//			BufferedWriter out0 = new BufferedWriter(
//					new FileWriter(Constants.screenFile+"features\\ForkedRepoPull_requestsNum0.csv"));
			
			i=0;
			int count=0;
		//	File f = new File(Constants.screenFile+"tables\\pull_requests.csv");
			File f = new File(Constants.screenFile+"features\\pull_requestActionToForkedFrom.csv");
			System.out.println(f.getName());
			BufferedReader r1 = BF.readFile(f);
			while((line = r1.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%25==0){
						System.out.println();
					}
				}
				String[] words = line.split(",");
				int pull_requestId = Integer.valueOf(words[0]);
				if(words[1].equals("\\N")){
					count++;
					continue;
				}
				int head_repoId = Integer.valueOf(words[1]);
				String action = words[words.length-1];
				if(action.contains("merged")){
					Integer c = repoPRMergedNumMap.get(head_repoId);
					if(c==null){
						c= 0;
					}
					c++;
					repoPRMergedNumMap.put(head_repoId, c);
				}
				Integer c = repoPRNumMap.get(head_repoId);
				if(c==null){
					c= 0;
				}
				c++;
				repoPRNumMap.put(head_repoId, c);
			}
			System.out.println("\n所有pull_request数量 ： "+i);
			System.out.println("head_repoId  为空的pull_request数量 ： "+count);
			System.out.println("涉及repo数量 ： "+repoPRNumMap.size());
			int k=0;
			for(Map.Entry<Integer, Integer> t:repoPRNumMap.entrySet()){
				int uid =t.getKey();
				Integer n = t.getValue();
				if(n!=0){
					k++;
				}
				Integer m = repoPRMergedNumMap.get(uid);
				if(m==null){
					m=0;
				}
				out.write(uid+","+t.getValue()+","+m+"\n");
			}
			out.close();
			System.out.println("Forked 有 pull_request ： "+k);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void headrepoPull_requestsNum(String type,String action){//项目发起的pull_request数量，head_repo_id
		try{
			String line = "";
			int i=0;
			TreeMap<Integer,Integer> repoNumMap = new TreeMap<Integer,Integer>();
	    	File usersFP = new File(Constants.screenFile+"tables\\projects"+type+".csv");
	    	System.out.println("read  "+usersFP.getName()+", waiting......");
	    	BufferedReader reader = BF.readFile(usersFP,"utf-8");
			while((line=reader.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%25==0){
						System.out.println();
					}
				}
				String [] words = line.split(",");
				int userid = Integer.valueOf(words[0]);
				
				repoNumMap.put(userid, 0);
			}
			System.out.println("\n repos number is "+repoNumMap.size());
			
			BufferedWriter out = new BufferedWriter(
					new FileWriter(Constants.screenFile+"features\\"+type+"Repo_"+action+"Pull_RequestsNum.csv"));
//			BufferedWriter out0 = new BufferedWriter(
//					new FileWriter(Constants.screenFile+"features\\"+type+"RepoPull_requestsNum0.csv"));
			
			i=0;
			int count=0;
	//		File f = new File(Constants.dirname+"dump\\pull_requests.csv");
			File f = new File(Constants.screenFile+"tables\\pull_request_"+action+".csv");
			System.out.println(f.getName());
			BufferedReader r1 = BF.readFile(f);
			while((line = r1.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%25==0){
						System.out.println();
					}
				}
				String[] words = line.split(",");
				int pull_requestId = Integer.valueOf(words[0]);
				if(words[1].equals("\\N")){
					count++;
					continue;
				}
				int head_repoId = Integer.valueOf(words[1]);
				String ac = words[words.length-1];
				//if(ac.equals(action)){
					Integer c = repoNumMap.get(head_repoId);
					if(c==null){
						continue;
					}
					c++;
					repoNumMap.put(head_repoId, c);
			//	}
			}
			System.out.println("\n所有pull_request数量 ： "+i);
			System.out.println("head_repoId  为空的pull_request数量 ： "+count);
			System.out.println("涉及repo数量 ： "+repoNumMap.size());
			int k=0;
			for(Map.Entry<Integer, Integer> t:repoNumMap.entrySet()){
				int uid =t.getKey();
				Integer n = t.getValue();
				if(n!=0){
					k++;
				}
				out.write(uid+","+t.getValue()+"\n");
			}
			out.close();
		//	out0.close();
			System.out.println("Forked 有Merged_pull_request ： "+k);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void headrepoPull_requestsNum(String type){//项目发起的pull_request数量，head_repo_id
		try{
			String line = "";
			int i=0;
			TreeMap<Integer,Integer> repoNumMap = new TreeMap<Integer,Integer>();
	    	File usersFP = new File(Constants.screenFile+"tables\\projects"+type+".csv");
	    	System.out.println("read  "+usersFP.getName()+", waiting......");
	    	BufferedReader reader = BF.readFile(usersFP,"utf-8");
			while((line=reader.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%25==0){
						System.out.println();
					}
				}
				String [] words = line.split(",");
				int userid = Integer.valueOf(words[0]);
				
				repoNumMap.put(userid, 0);
			}
			System.out.println("\n repos number is "+repoNumMap.size());
			
			BufferedWriter out = new BufferedWriter(
					new FileWriter(Constants.screenFile+"features\\"+type+"Repo_Pull_RequestsNum.csv"));
//			BufferedWriter out0 = new BufferedWriter(
//					new FileWriter(Constants.screenFile+"features\\"+type+"RepoPull_requestsNum0.csv"));
			
			i=0;
			int count=0;
	//		File f = new File(Constants.dirname+"dump\\pull_requests.csv");
			File f = new File(Constants.screenFile+"tables\\pull_request.csv");
			System.out.println(f.getName());
			BufferedReader r1 = BF.readFile(f);
			while((line = r1.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%25==0){
						System.out.println();
					}
				}
				String[] words = line.split(",");
				int pull_requestId = Integer.valueOf(words[0]);
				if(words[1].equals("\\N")){
					count++;
					continue;
				}
				int head_repoId = Integer.valueOf(words[1]);
				String ac = words[words.length-1];
				//if(ac.equals(action)){
					Integer c = repoNumMap.get(head_repoId);
					if(c==null){
						continue;
					}
					c++;
					repoNumMap.put(head_repoId, c);
			//	}
			}
			System.out.println("\n所有pull_request数量 ： "+i);
			System.out.println("head_repoId  为空的pull_request数量 ： "+count);
			System.out.println("涉及repo数量 ： "+repoNumMap.size());
			int k=0;
			for(Map.Entry<Integer, Integer> t:repoNumMap.entrySet()){
				int uid =t.getKey();
				Integer n = t.getValue();
				if(n!=0){
					k++;
				}
				out.write(uid+","+t.getValue()+"\n");
			}
			out.close();
		//	out0.close();
			System.out.println("Forked 有Merged_pull_request ： "+k);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void headrepoMergedPull_requestsNum(){//项目收到的pull_request数量，base_repo_id
		try{
			String line = "";
			int i=0;
			TreeMap<Integer,Integer> repoNumMap = new TreeMap<Integer,Integer>();
	    	File usersFP = new File(Constants.screenFile+"tables\\projects.csv");
	    	System.out.println("read  "+usersFP.getName()+", waiting......");
	    	BufferedReader reader = BF.readFile(usersFP,"utf-8");
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
				repoNumMap.put(userid, 0);
			}
			System.out.println("\n repos number is "+repoNumMap.size());
			
			BufferedWriter out = new BufferedWriter(
					new FileWriter(Constants.screenFile+"features\\headRepoMergedPull_requestsNum.csv"));
			
			i=0;
			int count=0;
			File f = new File("E:\\mysql-2015-09-25\\screen\\pull_requests.csv");
			System.out.println(f.getName());
			BufferedReader r1 = BF.readFile(f);
			while((line = r1.readLine())!=null){
				i++;
				if(i%10000==0){
					System.out.print(i/10000+" ");
					if(i/10000%25==0){
						System.out.println();
					}
				}
				String[] words = line.split(",");
				int pull_requestId = Integer.valueOf(words[0]);
				if(words[1].equals("\\N")){
					count++;
					continue;
				}
				int head_repoId = Integer.valueOf(words[1]);
				int merge = Integer.valueOf(words[words.length-1]);
				if(merge!=1){
					continue;
				}
				Integer c = repoNumMap.get(head_repoId);
				if(c==null){
					continue;
				}
				c++;
				repoNumMap.put(head_repoId, c);
			}
			System.out.println("\n所有pull_request数量 ： "+i);
			System.out.println("head_repoId  为空的pull_request数量 ： "+count);
			System.out.println("涉及repo数量 ： "+repoNumMap.size());
			for(Map.Entry<Integer, Integer> t:repoNumMap.entrySet()){
				int uid =t.getKey();
				out.write(uid+","+t.getValue()+"\n");
			}
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void userPull_RequestsNum(String type){
		try{
			String line = "";
			int i=0;
			BufferedReader rRW = BF.readFile(Constants.screenFile+"features\\headRepo"+type+"Pull_requestsNum.csv");
			TreeMap<Integer,Integer> headRepoPull_requestsNumMap = new TreeMap<Integer,Integer>();
			while((line=rRW.readLine())!=null){
				i++;
				if(i%10000==0){
					System.out.print(i/10000+" ");
					if((i/10000)%25==0)
						System.out.println(" ");
				}
				String[]words = line.split(",");
				int rid = Integer.valueOf(words[0]);

				int Num = Integer.valueOf(words[1]);
				headRepoPull_requestsNumMap.put(rid,Num);
 			}
			System.out.println(" \n"+i+" ");
			System.out.println("repoWatchersNumMap: "+headRepoPull_requestsNumMap.size());
			BufferedWriter outN = new BufferedWriter(
					new FileWriter(Constants.screenFile+"features\\user"+type+"Pull_RequestsNum.csv"));//用户提交的pull_request数量
			BufferedWriter outL = new BufferedWriter(
					new FileWriter(Constants.screenFile+"features\\user"+type+"Pull_RequestsNumsList.csv"));
			BufferedReader readUserReposList = BF.readFile(Constants.screenFile+"features\\userReposList.csv");
			i=0;
			while((line=readUserReposList.readLine())!=null){
				i++;
				if(i%10000==0){
					System.out.print(i/10000+" ");
					if((i/10000)%25==0)
						System.out.println(" ");
					outN.flush();
					outL.flush();
				}
				String[]words = line.split(",");
				int uid = Integer.valueOf(words[0]);
				outL.write(uid+",");
				int SumNum = 0;
				String[] reposid = words[1].split(" ");
				for(String rid:reposid){
					Integer repoid = Integer.valueOf(rid);
					Integer Num = headRepoPull_requestsNumMap.get(repoid);
					if(Num==null)
						Num=0;
					SumNum += Num;
					outL.write(Num+" ");
				}
				outN.write(uid+","+SumNum+"\n");
				outL.write("\n");
			//	break;
 			}
			System.out.println("\nusers total num is "+i);
			
			outN.close(); 
			outL.close(); 
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void Pull_Requestsuser(){
		try{
			String line = "";
			int i=0;
	//	
			BufferedReader readUserReposList = BF.readFile(Constants.screenFile+"tables\\projects.csv");
			TreeMap<Integer,Integer> repoUserMap = new TreeMap<Integer,Integer>();
			while((line=readUserReposList.readLine())!=null){
				i++;
				if(i%10000==0){
					System.out.print(i/10000+" ");
					if((i/10000)%25==0)
						System.out.println(" ");
				}
				String[]words = line.split(",");
				Integer rid = Integer.valueOf(words[0]);
				Integer uid = Integer.valueOf(words[2]);
				repoUserMap.put(rid, uid);
 			}
			System.out.println(" \n"+i+" ");
			System.out.println("userReposListMap: "+repoUserMap.size());
			BufferedWriter out = new BufferedWriter(
					new FileWriter(Constants.screenFile+"features\\Pull_RequestUser.csv"));//用户提交的pull_request数量
			
			BufferedReader r = BF.readFile(Constants.screenFile+"screen\\pull_requests.csv");
			i=0;
			while((line=r.readLine())!=null){
				i++;
				if(i%10000==0){
					System.out.print(i/10000+" ");
					if((i/10000)%25==0)
						System.out.println(" ");
					out.flush();
				}
				String[] words = line.split(",");
				int pull_requestId = Integer.valueOf(words[0]);
				if(words[1].equals("\\N")){
				//	count++;
					continue;
				}
				Integer head_repoId = Integer.valueOf(words[1]);
				Integer userId = repoUserMap.get(head_repoId);
				out.write(pull_requestId+","+userId+"\n");
			}
			
			System.out.println("\nusers total num is "+i);
			
			out.close(); 
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void userIssuesNumMonth() throws Exception{
		DecimalFormat doubleDF = new DecimalFormat("######0.00");   
		Date thisTime = Constants.df.parse(Constants.currentTime);
		int m = thisTime.getMonth();
		int y = thisTime.getYear();
		
		int maxMonth = 0;//最多有多少个月
		BufferedWriter out = new BufferedWriter(new FileWriter("userIssuesNumMonth.csv"));
		TreeMap<Integer,TreeMap<Integer,Integer>> authorIssuesNum = new TreeMap<Integer,TreeMap<Integer,Integer>>();
		String line = "";
		int i=0;
		File usersFP = new File(Constants.screenFile+"\\tables\\users.csv");
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
			int userid = Integer.valueOf(words[0]);;
			authorIssuesNum.put(userid, new TreeMap<Integer,Integer> ());
		}
		System.out.println("\n USERs number is "+authorIssuesNum.size());
		File f = new File("E:\\mysql-2015-09-25\\screen\\tables\\issues.csv");
		System.out.println(f.getName());
		BufferedReader r1 = BF.readFile(f);
		while((line = r1.readLine())!=null){
			i++;
			if(i%10000==0){
				System.out.print(i/10000+" ");
				if(i/10000%25==0){
					System.out.println();
				}
			}
			String[] words = line.split(",");
		//	System.out.println(line);
			int cid = Integer.valueOf(words[0]);
			int authorid = Integer.valueOf(words[2]);
			Date d = Constants.df.parse(words[words.length-2].substring(1,words[words.length-2].length()-1));
			int month = d.getMonth();
			int year = d.getYear();
			int index = (y-year)*12+(m-month);
			
			TreeMap<Integer,Integer> c = authorIssuesNum.get(authorid);
			if(c==null){
				c = new TreeMap<Integer,Integer>();
				c.put(index, 1);
			}else{
				Integer cc = c.get(index);
				if(cc==null){
					cc=0;
				}
				cc++;
				c.put(index, cc);
			}
			authorIssuesNum.put(authorid, c);
		}
		System.out.println(i);
		System.out.println(authorIssuesNum.size());
		for(Map.Entry<Integer, TreeMap<Integer,Integer>> t:authorIssuesNum.entrySet()){
			TreeMap<Integer,Integer> c= t.getValue();
			if(maxMonth<c.size()){
				maxMonth = c.size();
			}
		}
		for(Map.Entry<Integer, TreeMap<Integer,Integer>> t:authorIssuesNum.entrySet()){
			int rid =t.getKey();
			out.write(rid+",");
			TreeMap<Integer,Integer> c= t.getValue();
			for(int j=0;j<maxMonth;j++){
				
				Integer cNum = c.get(j);
				if(cNum==null){
					cNum=0;
				}
				out.write(cNum+" ");
			}
			out.write("\n");
		}
		out.close();
	}
	public void getMouthData(int n){//获取近N个月的数据
		String line = "";
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter("user"+n+"MonthIssuesNum.txt"));
			TreeMap<Integer,Integer> repoCommitsNum = new TreeMap<Integer,Integer>();
			int i=0;
			File f = new File("userIssuesNumMonth.csv");
			System.out.println(f.getName());
			BufferedReader r1 = BF.readFile(f);
			while((line = r1.readLine())!=null){
				i++;
				if(i%10000==0){
					System.out.print(i/10000+" ");
					if(i/10000%10==0){
						System.out.println();
					}
				}
				String[] words = line.split(",");
				String id = words[0];
				String [] months = words[1].split(" ");
				int sum = 0;
				for(int j=0;j<n;j++){
					sum += Integer.valueOf(months[j]);
				}
				out.write(id+","+sum+"\n");
			}
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void userPull_RequestsMergedFreq(){//用户提交的pull_request被merged的比例
		try{
			String line = "";
			int i=0;
			BufferedReader rRW = BF.readFile(Constants.screenFile+"features\\userMergedPull_RequestsNum.csv");
			TreeMap<Integer,Integer> userMergedNumMap = new TreeMap<Integer,Integer>();
			while((line=rRW.readLine())!=null){
				i++;
				if(i%10000==0){
					System.out.print(i/10000+" ");
					if((i/10000)%25==0)
						System.out.println(" ");
				}
				String[]words = line.split(",");
				int rid = Integer.valueOf(words[0]);

				int Num = Integer.valueOf(words[1]);
				userMergedNumMap.put(rid,Num);
 			}
			System.out.println(" \n"+i+" ");
			System.out.println("repoWatchersNumMap: "+userMergedNumMap.size());
			BufferedWriter outF = new BufferedWriter(
					new FileWriter(Constants.screenFile+"features\\userPull_RequestsMergedFreq.csv"));//用户提交的pull_request数量
			BufferedReader readUserReposList = BF.readFile(Constants.screenFile+"features\\userPull_RequestsNum.csv");
			i=0;
			while((line=readUserReposList.readLine())!=null){
				i++;
				if(i%10000==0){
					System.out.print(i/10000+" ");
					if((i/10000)%25==0)
						System.out.println(" ");
					outF.flush();
				}
				String[]words = line.split(",");
				int uid = Integer.valueOf(words[0]);
				int num = Integer.valueOf(words[1]);;
				double f = 0.00;
				if(num==0){
					f=0.00;
				}else{
					double mergedNum = userMergedNumMap.get(uid);
					f =mergedNum/num;
				}
				outF.write(uid+","+f+"\n");
 			}
			System.out.println("\nusers total num is "+i);
			outF.close(); 
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void Pull_requestsAction(String action){//结合pull_request_history表，确定pull_request的状态
		try{
			String line = "";
			int i=0;
			String tempaction = "";
		//	TreeMap<Integer,String> PR_ActionMap = new TreeMap<Integer,String>();//pull_request,state
			TreeSet<Integer> MergedSet = new TreeSet<Integer>();//pull_request,state

			File usersFP = new File(Constants.screenFile+"tables\\pull_request_history.csv");
	    	System.out.println("read  "+usersFP.getName()+", waiting......");
	    	BufferedReader reader = BF.readFile(usersFP,"utf-8");
			while((line=reader.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%25==0){
						System.out.println();
					}
				}
				String [] words = line.split(",");
    			int id = Integer.valueOf(words[0]);
    			Integer pull_Request_id = Integer.valueOf(words[1]);
    			tempaction = words[3].substring(1, words[3].length()-1);
    			if(tempaction.contains(action)){
    				MergedSet.add(pull_Request_id);
    			}
    		//	PR_ActionMap.put(pull_Request_id,tempaction);
			}
			System.out.println("\n repos number is "+MergedSet.size());
//			BufferedWriter out2 = new BufferedWriter(
//					new FileWriter(Constants.screenFile+"tables\\pull_requestsID_Action.csv"));
//			for(Map.Entry<Integer, String> m:PR_ActionMap.entrySet()){
//				out2.write(m.getKey()+","+m.getValue()+"\n");
//			}
//			out2.close();
			BufferedWriter out = new BufferedWriter(
					new FileWriter(Constants.screenFile+"tables\\pull_requests_"+action+".csv"));
			i=0;
			int count=0;
			File f = new File(Constants.dirname+"dump\\pull_requests.csv");
			System.out.println(f.getName());
			BufferedReader r1 = BF.readFile(f);
			while((line = r1.readLine())!=null){
				i++;
				if(i%1000000==0){
					System.out.print(i/1000000+" ");
					if(i/1000000%25==0){
						System.out.println();
					}
				}
				String[] words = line.split(",");
				Integer id = Integer.valueOf(words[0]);
				if(MergedSet.contains(id)){
					out.write(line+"\n");
					count++;
				}
			}
			System.out.println("\n所有pull_request数量 ： "+i);
			System.out.println("merged pull_request数量 ： "+count);
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void Pull_requestsAction(){//结合pull_request_history表，确定pull_request的状态
		try{
			String line = "";
			int i=0;
			String tempaction = "";
			TreeMap<Integer,String> PR_ActionMap = new TreeMap<Integer,String>();//pull_request,state
		//	TreeSet<Integer> MergedSet = new TreeSet<Integer>();//pull_request,state

			File usersFP = new File(Constants.screenFile+"tables\\pull_request_history.csv");
	    	System.out.println("read  "+usersFP.getName()+", waiting......");
	    	BufferedReader reader = BF.readFile(usersFP,"utf-8");
			while((line=reader.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%25==0){
						System.out.println();
					}
				}
				if(i<=15000000){
					continue;
				}
				if(i>20000000){
					break;
				}
				String [] words = line.split(",");
    			int id = Integer.valueOf(words[0]);
    			Integer pull_Request_id = Integer.valueOf(words[1]);
    			tempaction = words[3].substring(1, words[3].length()-1);
//    			if(tempaction.contains("closed")){
//    				MergedSet.add(pull_Request_id);
//    			}
    			PR_ActionMap.put(pull_Request_id,tempaction);
			}
			System.out.println("\n repos number is "+PR_ActionMap.size());
//			BufferedWriter out2 = new BufferedWriter(
//					new FileWriter(Constants.screenFile+"tables\\pull_requestsID_Action.csv"));
//			for(Map.Entry<Integer, String> m:PR_ActionMap.entrySet()){
//				out2.write(m.getKey()+","+m.getValue()+"\n");
//			}
//			out2.close();
			BufferedWriter out = new BufferedWriter(
					new FileWriter(Constants.screenFile+"tables\\pull_request_action.csv",true));
			i=0;
			int count=0;
			File f = new File(Constants.dirname+"dump\\pull_requests.csv");
			System.out.println(f.getName());
			BufferedReader r1 = BF.readFile(f);
			while((line = r1.readLine())!=null){
				i++;
				if(i%1000000==0){
					System.out.print(i/1000000+" ");
					if(i/1000000%25==0){
						System.out.println();
					}
				}
				String[] words = line.split(",");
				Integer id = Integer.valueOf(words[0]);
				String action = PR_ActionMap.get(id);
				if(action==null){
					continue;
				}else{
					out.write(line+","+action+"\n");
					count++;
				}
//				if(MergedSet.contains(id)){
//					out.write(line+"\n");
//					count++;
//				}
			}
			System.out.println("\n所有pull_request数量 ： "+i);
			System.out.println("merged pull_request数量 ： "+count);
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	File pull_RequestHistoryFile = new File(Constants.dumpFile+"FinalPull_requestsHistory.csv");
	File pull_RequestFile = new File(Constants.dumpFile+"pull_requests.csv");
	public void getNewPull_request(){
		/*
		 * 组合pull_request和pull_request_history表，
		 * 输出：id，head_repo_id,base_repo_id,created_At,action,actor
		 * 
		 */
		try{
			String line = "";
			int i=0,per = 5000000;
			int count=0,cnull = 0;
			for(int j=0;j<4;j++){
				System.out.println(j*per+"....."+per*(j+1));
				TreeMap<Integer,String> PR_ActionMap = new TreeMap<Integer,String>();//pull_request,state
				//	TreeSet<Integer> MergedSet = new TreeSet<Integer>();//pull_request,state
			    	System.out.println("read  "+pull_RequestHistoryFile.getName()+", waiting......");
			    	BufferedReader reader = BF.readFile(pull_RequestHistoryFile,"utf-8");
			    	i=0;
					while((line=reader.readLine())!=null){
						i++;
						if(i%100000==0){
							System.out.print(i/100000+" ");
							if(i/100000%25==0){
								System.out.println();
							}
						}
						if(i<=j*per)
							continue;
					//	if(i>j*per&&i<=per*(j+1)){
							String [] words = line.split(",");
							Integer pull_Request_id = Integer.valueOf(words[1]);
							String tempLine = words[2]+","+words[3]+","+words[4];
							PR_ActionMap.put(pull_Request_id,tempLine);
				//		}
						if(i>per*(j+1)){
							break;
						}
			
					}
					System.out.println("\n PR_ActionMap number is "+PR_ActionMap.size());
					BufferedWriter out = new BufferedWriter(
							new FileWriter(pull_RequestHistoryFile.getParent()+"\\new_pull_request.csv",true));
					i=0;
					
					System.out.println(pull_RequestFile.getName());
					BufferedReader r1 = BF.readFile(pull_RequestFile);
					while((line = r1.readLine())!=null){
						i++;
						if(i%1000000==0){
							System.out.print(i/1000000+" ");
							if(i/1000000%25==0){
								System.out.println();
							}
						}
						String[] words = line.split(",");
						Integer id = Integer.valueOf(words[0]);
						String tempLine = PR_ActionMap.get(id);
						if(tempLine==null){
							cnull++;
							continue;
						}else{
							out.write(id+","+words[1]+","+words[2]+","+tempLine+"\n");
							count++;
						}
					}
					System.out.println("\n所有pull_request数量 ： "+i);
					
					out.close();
			}
			System.out.println("new pull_request数量 ： "+count);
			System.out.println("null pull_request数量 ： "+cnull);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Pull_requests p = new Pull_requests();
		long start = System.currentTimeMillis();
	//	p.baseRepoPull_requestsNum();
	//	p.headrepoPull_requestsNum("Fork","merged");
	//	p.headrepoMergedPull_requestsNum();
//		p.userPull_RequestsNum("");
//		p.userPull_RequestsNum("Merged");
//		p.userPull_RequestsMergedFreq()
		//p.Pull_requestsAction("synchronize");
//		p.Pull_requestsToOthers();
//		p.Pull_requestsTo();
	//	p.pull_rHeadRNum();
		p.getNewPull_request();
		long end = System.currentTimeMillis();
		System.out.println("计算用时="+(end-start)/1000);
	}

}
