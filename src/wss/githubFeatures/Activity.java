package wss.githubFeatures;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import utils.BF;
import utils.Constants;

public class Activity {

	/**
	 * @param args
	 */
	String path = Constants.dumpFile;
	File usrFile = new File(path+"features\\USR\\usersInfo.csv");
	File orgFile = new File(path+"features\\ORG\\usersInfo.csv");
	public void preproAct(File f,int repoIndex,int userIndex,int timeIndex){
		String line = null;
		int i=0;
        try{	
    		BufferedWriter out = new BufferedWriter(new FileWriter
    				(f.getParent()+"\\features\\Activity\\"+f.getName()));
    		BufferedReader reader = BF.readFile(f,"utf8");
	    	System.out.println("read  "+f.getName()+", waiting......");
			while((line=reader.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%25==0){
						System.out.println();
					}
				}
				String [] words = line.split(",");
				Integer id = Integer.valueOf(words[0]);
				if(words[repoIndex].equals("\\N")||words[userIndex].equals("\\N")){
    				continue;
    			}
				Integer repoId = Integer.valueOf(words[repoIndex]);
    			Integer userId = Integer.valueOf(words[userIndex]);
    			String time = words[timeIndex];
    			out.write(id+","+repoId+","+userId+","+time+"\n");
				
			}
    		System.out.println(f.getName()+" completed!");
    		out.close();
        }catch(Exception e){
        	e.printStackTrace();
        }
	}
	public void userActReposNumMonth(File f) throws Exception{//用户行为作用到的项目的数量
		System.out.println("start ... ");
		Date currentTime =  Constants.df.parse( Constants.currentTime);
		Date startTime =  Constants.df.parse(Constants.startTime);
		int m = currentTime.getMonth();
		int y = currentTime.getYear();
		String line = "";
		int i=0;
		int maxMonth = 106;//最多有多少个月
	
		TreeMap<Integer,TreeMap<Integer,TreeSet<Integer>>> userActReposSetMap = new TreeMap<Integer,TreeMap<Integer,TreeSet<Integer>>>();
		
    	BufferedReader reader = BF.readFile(usrFile,"utf8");
    	System.out.println("read  "+usrFile.getName()+", waiting......");
		while((line=reader.readLine())!=null){
			i++;
			if(i%100000==0){
				System.out.print(i/100000+" ");
				if(i/100000%25==0){
					System.out.println();
				}
		//		break;
			}
			String [] words = line.split(",");
			int userid = Integer.valueOf(words[0]);;
			userActReposSetMap.put(userid, new TreeMap<Integer,TreeSet<Integer>> ());
		}
		System.out.println("\n USERs number is "+userActReposSetMap.size());
		reader = BF.readFile(orgFile,"utf8");
    	System.out.println("read  "+orgFile.getName()+", waiting......");
    	i=0;
		while((line=reader.readLine())!=null){
			i++;
			if(i%100000==0){
				System.out.print(i/100000+" ");
				if(i/100000%25==0){
					System.out.println();
				}
			}
			String [] words = line.split(",");
			Integer userid = Integer.valueOf(words[0]);
			
			TreeMap<Integer,TreeSet<Integer>> t = new TreeMap<Integer,TreeSet<Integer>>();
//			for(int j=0;j<106;j++){
//				t.put(j, 0);
//			}
			userActReposSetMap.put(userid,t);
		}
		System.out.println("\n USERs number is "+userActReposSetMap.size());
		System.out.println(f.getName());
		BufferedReader r1 = BF.readFile(f);
		i=0;
		
//		TreeSet<Integer> indexSet = new TreeSet<Integer>();
//		TreeSet<String> timeSet = new TreeSet<String>();
		String time = "";
		while((line = r1.readLine())!=null){
			i++;
			if(i%100000==0){
				System.out.print(i/100000+" ");
				if(i/100000%25==0){
					System.out.println();
				}
			//	break;
			}
			String[] words = line.split(",");
		//	System.out.println(line);
			Integer actId = Integer.valueOf(words[0]);
			Integer repoId = Integer.valueOf(words[1]);
			Integer userId = Integer.valueOf(words[2]);
			Date d = Constants.df.parse(words[3].substring(1,words[3].length()-1));
			int month = d.getMonth();
			int year = d.getYear();
		//	int date = d.getDate();
			time = (year+1900)+"-"+(month+1);
			int index = (y-year)*12+(m-month);

			
			TreeMap<Integer,TreeSet<Integer>> c = userActReposSetMap.get(userId);
			if(c==null){
//				c = new TreeMap<Integer,Integer>();
//				c.put(index, 1);
				continue;
			}else{
				TreeSet<Integer> cc = c.get(index);
				if(cc==null){
					cc= new TreeSet<Integer> () ;
				}
				cc.add(repoId);
				c.put(index, cc);
			//	System.out.println(line);
			}
			userActReposSetMap.put(userId, c);
		//	break;
		}
		System.out.println(i);
		System.out.println(userActReposSetMap.size());
		
		BufferedWriter out = new BufferedWriter(new FileWriter
				(f.getParent()+"\\userMonth"+f.getName().replaceAll(".csv","")+"ReposNum.csv"));
//		for(Map.Entry<Integer, TreeMap<Integer,Integer>> t:authorCommitsNum.entrySet()){
//			TreeMap<Integer,Integer> c= t.getValue();
//			if(maxMonth<c.size()){
//				maxMonth = c.size();
//			}
//		}
		for(Map.Entry<Integer, TreeMap<Integer,TreeSet<Integer>>> t:userActReposSetMap.entrySet()){
			int id =t.getKey();
			out.write(id+",");
			TreeMap<Integer,TreeSet<Integer>> c= t.getValue();
			for(int j=0;j<maxMonth;j++){
				TreeSet<Integer> cNum = c.get(j);
				if(cNum==null){
					out.write(0+" ");;
				}else{
					out.write(cNum.size()+" ");
				}	
			}
			out.write("\n");
		}
		out.close();
	}
	public void getMouthData(int n){//获取近N个月的数据
		String line = "";
		try{
			File monthlyFile = new File(path+"features\\Activity\\Monthly\\");
			File[] files = monthlyFile.listFiles();
			TreeMap<Integer,Integer> userActivityMap = new TreeMap<Integer,Integer>();
			for(File f:files){
				System.out.println(f.getName());
				BufferedReader r1 = BF.readFile(f);
				int i=0;
				while((line = r1.readLine())!=null){
					i++;
					if(i%100000==0){
						System.out.print(i/100000+" ");
//						if(i/100000%10==0){
//							System.out.println();
//						}
					}
					String[] words = line.split(",");
					Integer id = Integer.valueOf(words[0]);
					String [] months = words[1].split(" ");
					int sum = 0;
					for(int j=0;j<n;j++){
						sum += Integer.valueOf(months[j]);
					}
					Integer c = userActivityMap.get(id);
					if(c==null){
						c=0;
					}
					c += sum;
					userActivityMap.put(id, c);
				}
				System.out.println();
			}
			BufferedWriter out = new BufferedWriter(new FileWriter
					(monthlyFile.getParent()+"\\user"+n+"MonthActivityNum.csv"));
			int i=0;
			for(Map.Entry<Integer, Integer> m:userActivityMap.entrySet()){
				out.write(m.getKey()+","+m.getValue()+"\n");
			}
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void usersActivity(){
		String line = null;
		try{
			TreeMap<Integer,String> userActivityMap = new TreeMap<Integer,String>();
       
        	int i=0;
        	ArrayList<Integer> usersIdList = new ArrayList<Integer>();
        	BufferedReader r2 = BF.readFile(path+"features\\USR\\userFollowersNum2.csv");
			System.out.print("userFollowersNum ... ");
			while((line = r2.readLine())!=null){
    			i++;
    			if(i%1000==0){
    				System.out.print(i/1000+" ");
    				if(i/1000%25==0){
    					System.out.println();
    				}
    				break;
    			}
    			String[] words = line.split(",");
    			if(words[0].contains("id")){
    				continue;
    			}
    			Integer id = Integer.valueOf(words[0]);
    			usersIdList.add(id);
			}
			BufferedReader r1 = BF.readFile(path+"features\\Activity_Monthly0.csv");
			System.out.print("Activity_Monthly ... ");
			while((line = r1.readLine())!=null){
    			i++;
    			if(i%100000==0){
    				System.out.print(i/100000+" ");
    				if(i/100000%25==0){
    					System.out.println();
    				}
    			}
    			String[] words = line.split(",");
    			if(words[0].contains("id")){
    				continue;
    			}
    			Integer id = Integer.valueOf(words[0]);
    			if(!usersIdList.contains(id)){
    				continue;
    			}
    			String activity = words[1];
    			userActivityMap.put(id, activity);
			}
			System.out.println();
			r1.close();
    		System.out.println("\n users number : "+userActivityMap.size());
    		File outFile = new File(path+"features\\USR\\userActivityNum.csv");
    		BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
    		r2 = BF.readFile(path+"features\\USR\\userFollowersNum2.csv");
			System.out.print("userFollowersNum ... ");
			while((line = r2.readLine())!=null){
    			i++;
    			if(i%10000==0){
    				System.out.print(i/10000+" ");
    				if(i/10000%25==0){
    					System.out.println();
    				}
    				break;
    			}
    			String[] words = line.split(",");
    			if(words[0].contains("id")){
    				continue;
    			}
    			Integer id = Integer.valueOf(words[0]);
    		//	String login = words[1];
    		//	System.out.println(line);
    			out.write(id+","+words[1]+","+userActivityMap.get(id)+"\n");
			}
    		out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void SumNum(File sumPath){//参数为需要求和的特征名称列表
		String line = null;
		try{
			TreeMap<Integer,Double> userNumMap = new TreeMap<Integer,Double>();
            File[] features = sumPath.listFiles();
            String title = "id";
            for(File f:features){
           	  title = title +","+f.getName();
            }
            for(File f:features){
            	int i=0;
				BufferedReader r1 = BF.readFile(f);
				System.out.print(f.getName()+" ... ");
				while((line = r1.readLine())!=null){
	    			i++;
	    			if(i%100000==0){
	    				System.out.print(i/100000+" ");
	    				if(i/100000%25==0){
	    					System.out.println();
	    				}
	    			}
	    			String[] words = line.split(",");
	    			
	    			int id = Integer.valueOf(words[0]);
	    			Double num = Double.valueOf(words[1]);
	    			Double c = userNumMap.get(id);
	    			if(c==null){
	    				c=0.0;
	    			}
	    			c += num;
	    			userNumMap.put(id, c);
	    			
				}
				System.out.println();
				r1.close();
        	}
    		System.out.println("\n users number : "+userNumMap.size());
    		File outFile = new File(path+"features\\Activity_"+sumPath.getName()+".csv");
    		BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
    		for(Map.Entry<Integer, Double> t:userNumMap.entrySet()){
    			int rid =t.getKey();
    			out.write(rid+","+t.getValue()+"\n");
    		}
    		out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void SumNum2(File sumPath){//参数为需要求和的特征名称列表
		String line = null;
		try{
			TreeMap<Integer,ArrayList<Integer>> userNumMap = new TreeMap<Integer,ArrayList<Integer>>();
            File[] features = sumPath.listFiles();
            String title = "id";
            for(File f:features){
           	  title = title +","+f.getName();
            }
            for(File f:features){
            	int i=0;
				BufferedReader r1 = BF.readFile(f);
				System.out.print(f.getName()+" ... ");
				while((line = r1.readLine())!=null){
	    			i++;
	    			if(i%100000==0){
	    				System.out.print(i/100000+" ");
	    				if(i/100000%25==0){
	    					System.out.println();
	    				}
	    			}
	    			String[] words = line.split(",");
	    			
	    			int id = Integer.valueOf(words[0]);
	    			Integer num = Integer.valueOf(words[1]);
	    			ArrayList<Integer> c = userNumMap.get(id);
	    			if(c==null){
	    				c=new ArrayList<Integer>();
	    			}
	    			c .add(num);
	    			userNumMap.put(id, c);
	    			
				}
				System.out.println();
				r1.close();
        	}
    		System.out.println("\n users number : "+userNumMap.size());
    		File outFile = new File(path+"features\\Activity_"+sumPath.getName()+".csv");
    		BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
    		for(Map.Entry<Integer, ArrayList<Integer>> t:userNumMap.entrySet()){
    			int id =t.getKey();
    			out.write(id+",");
    			int sum = 0;
    			for(Integer tt:t.getValue()){
    				sum += tt;
    			}
    			out.write(sum+",");
    			for(Integer tt:t.getValue()){
    				out.write(tt+" ");
    			}
    			out.write("\n");
    		}
    		out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void SumNumMonthly(File sumPath){//参数为需要求和的特征名称列表
		String line = null;
		try{
			System.out.println("start ... ");
			TreeMap<Integer,TreeMap<Integer,Integer>> userNumMap = new TreeMap<Integer,TreeMap<Integer,Integer>>();
            File[] features = sumPath.listFiles();
            String title = "id";
            for(File f:features){
           	  title = title +","+f.getName();
            }
        	int maxSize = 106;
            String [] Nums =null;
            String[] words  = null;
            for(int k=1;k<25;k++){
            	System.out.println(k+" ... ");
            	userNumMap.clear();
            	for(File f:features){
                	int i=0;
    				BufferedReader r1 = BF.readFile(f);
    				System.out.print(f.getName()+" ... ");
    				while((line = r1.readLine())!=null){
    	    			i++;
    	    			if(i%100000==0){
    	    				System.out.print(i/100000+" ");
    	    				if(i/100000%25==0){
    	    					System.out.println();
    	    				}
//    	    				System.out.println(line+" ");
//    	    				break;
    	    			}
    	    			if(i<(k-1)*100000){
    	    				continue;
    	    			}
    	    			if(i>=k*100000){
    	    				break;
    	    			}
    	    			words = line.split(",");
    	    			
    	    			int id = Integer.valueOf(words[0]);
    	    			
    	    			Nums = words[1].split(" ");
    	    			TreeMap<Integer,Integer> c = userNumMap.get(id);
    	    			if(c==null){
    	    				c=new TreeMap<Integer,Integer> ();
    	    				for(int j=0;j<Nums.length;j++){
    	    					c.put(j, Integer.valueOf(Nums[j]));
    	    				}
    	    			}else{
    	    				for(int j=0;j<Nums.length;j++){
    	    					Integer cc = c.get(j);
    	    					if(cc==null){
    	    						cc=0;
    	    					}
    	    					cc += Integer.valueOf(Nums[j]);
    	    					c.put(j, cc);
    	    				}
    	    			}
    	    			userNumMap.put(id, c);
    	    		//	break;
    				}
    				System.out.println(Nums.length);
//    				r1.close();
            	}
//        		System.out.println("\n users number : "+userNumMap.size());
//        	
//        		for(Map.Entry<Integer, TreeMap<Integer,Integer>> m:userNumMap.entrySet()){
//        			int rid =m.getKey();
//        			if(maxSize<m.getValue().size()){
//        				maxSize = m.getValue().size();
//        			}
//        		}
        		File outFile = new File(path+"features\\Activity_"+sumPath.getName()+".csv");
        		BufferedWriter out = new BufferedWriter(new FileWriter(outFile,true));
        		for(Map.Entry<Integer, TreeMap<Integer,Integer>> m:userNumMap.entrySet()){
        			int rid =m.getKey();
        			out.write(rid+",");
        			TreeMap<Integer,Integer> mm = m.getValue();
        			for(int j=0;j<maxSize;j++){
        				Integer c = mm.get(j);
        				if(c==null){
        					c = 0;
        				}
        				out.write(c+" ");
        			}
        			out.write("\n");
        		}
        		out.close();
            }
            
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Activity a = new Activity();
//		Issues i = new Issues();
//		i.userIssuesNumMonth();
//		i.getMouthData(3);
//		i.getMouthData(12);
//		Commits c = new Commits();
//		c.userCommitReposNumMonth();
//		c.getMouthData(3);
//		c.getMouthData(12);
//		Projects p = new Projects();
//		p.userProjectNumMonth();
//		p.getMouthData(3);
//		p.getMouthData(12);
//		Pull_request_history pr = new Pull_request_history();
//		pr.userOpenPull_request_historyNumMonth();
//		pr.getMouthData(3);
//		pr.getMouthData(12);
//		File f3 = new File(Constants.dumpFile+"features\\3Month");
//		a.SumNum2(f3);
//		File f12 = new File(Constants.dumpFile+"features\\12Month");
//		a.SumNum(f12);
//		File fly = new File(Constants.dumpFile+"features\\Monthly");
//		a.SumNumMonthly(fly);
	//	a.issue("commits",2,4);
	//	a.usersActivity();
//		File issuesFile = new File(a.path+"commits.csv");
//		a.preproAct(issuesFile, 2, 4, 5);
//		File file = new File(a.path+"pull_requests_open.csv");
//		a.preproAct(file, 1, 5, 3);
//		File file2 = new File(a.path+"projects.csv");
//		a.preproAct(file2, 2, 4, 5);
//		File issuesFile = new File(a.path+"features\\Activity\\pull_requests_open.csv");
//		a.userActReposNumMonth(issuesFile);
		a.getMouthData(3);
	}

}
