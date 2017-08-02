package wss.githubFeatures;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import utils.BF;
import utils.Constants;

public class Issues {

	/**
	 * @param args
	 */
	public void userIssuesNum(){
		String line = null;
		int i=0;
        try{	
        	TreeMap<Integer,Integer> authorIssuesNum = new TreeMap<Integer,Integer>();
        	File usersFP = new File(Constants.screenFile+"tables\\users.csv");
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
    			int userid = Integer.valueOf(words[0]);;
    			authorIssuesNum.put(userid, 0);
    		}
    		System.out.println("\n USERs number is "+authorIssuesNum.size());
    		
    		i=0;
    		int count=0;
    		BufferedReader r1 = BF.readFile(Constants.screenFile+"tables\\issues.csv");
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
    			if(words[2].equals("\\N")){
    				count++;
    				continue;
    			}
    			int reporterId = Integer.valueOf(words[2]);
    			Integer c = authorIssuesNum.get(reporterId);
    			if(c==null){
    				continue;
    			}else{
    				c++;
    				authorIssuesNum.put(reporterId, c);
    			}
    		}
    		System.out.println("\n issues.csv all lines number : "+i);
    		System.out.println(" reporterId 为空的 数量 : "+count);
    		System.out.println(" users 数量 : "+authorIssuesNum.size());
    		
    		BufferedWriter out = new BufferedWriter(new FileWriter(Constants.screenFile+"features\\userIssuesNum.csv"));
    		for(Map.Entry<Integer, Integer> t:authorIssuesNum.entrySet()){
    			int rid =t.getKey();
    			out.write(rid+","+t.getValue()+"\n");
    		}
    		out.close();
        }catch(Exception e){
        	e.printStackTrace();
        }
		
	}
	File usrFile = new File(Constants.dumpFile+"features\\USR\\usersInfo.csv");
	File orgFile = new File(Constants.dumpFile+"features\\ORG\\usersInfo.csv");
	File issuesFile = new File(Constants.dumpFile+"issues.csv");
	public void userIssuesNumMonth(){
		try{
			DecimalFormat doubleDF = new DecimalFormat("######0.00");   
			Date currentTime =  Constants.df.parse( Constants.currentTime);
			Date startTime =  Constants.df.parse(Constants.startTime);
			int m = currentTime.getMonth();
			int y = currentTime.getYear();
			
			int maxMonth = 106;//最多有多少个月
			BufferedWriter out = new BufferedWriter(new FileWriter(issuesFile.getParent()+"\\features\\Monthly\\userIssuesNumMonth.csv"));
			TreeMap<Integer,TreeMap<Integer,Integer>> authorIssuesNum = new TreeMap<Integer,TreeMap<Integer,Integer>>();
			String line = "";
			int i=0;
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
				authorIssuesNum.put(userid, new TreeMap<Integer,Integer> ());
			}
			System.out.println("\n USERs number is "+authorIssuesNum.size());
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
				int userid = Integer.valueOf(words[0]);;
				authorIssuesNum.put(userid, new TreeMap<Integer,Integer> ());
			}
			System.out.println("\n USERs number is "+authorIssuesNum.size());
			System.out.println(issuesFile.getName());
			BufferedReader r1 = BF.readFile(issuesFile);
			i=0;
			while((line = r1.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%25==0){
						System.out.println();
					}
				}
				String[] words = line.split(",");
			//	System.out.println(line);
				int cid = Integer.valueOf(words[0]);
				if(words[2].contains("\\N")){
					continue;
				}
				int authorid = Integer.valueOf(words[2]);
				Date d = Constants.df.parse(words[words.length-2].substring(1,words[words.length-2].length()-1));
				if(d.compareTo(startTime)<0||d.compareTo(currentTime)>0){//
					continue;
				}
				int month = d.getMonth();
				int year = d.getYear();
				int index = (y-year)*12+(m-month);
				
				TreeMap<Integer,Integer> c = authorIssuesNum.get(authorid);
				if(c==null){
//					c = new TreeMap<Integer,Integer>();
//					c.put(index, 1);
					continue;
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
//			for(Map.Entry<Integer, TreeMap<Integer,Integer>> t:authorIssuesNum.entrySet()){
//				TreeMap<Integer,Integer> c= t.getValue();
//				if(maxMonth<c.size()){
//					maxMonth = c.size();
//				}
//			}
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
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public void getMouthData(int n){//获取近N个月的数据
		String line = "";
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter
					(issuesFile.getParent()+"\\features\\"+n+"Month\\user"+n+"MonthIssuesNum.csv"));
			int i=0;
			File f = new File(issuesFile.getParent()+"\\features\\Monthly\\userIssuesNumMonth.csv");
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
	public void repoIssuesNum(){
		String line = null;
		int i=0;
        try{	
        	TreeMap<Integer,Integer> repoIssuesNum = new TreeMap<Integer,Integer>();
        	File usersFP = new File(Constants.screenFile+"tables\\projects.csv");
        	System.out.println("read  "+usersFP.getName()+", waiting......");
        	BufferedReader reader = BF.readFile(usersFP,"utf8");
    		while((line=reader.readLine())!=null){
    			i++;
    			if(i%10000==0){
    				System.out.print(i/10000+" ");
    				if(i/10000%25==0){
    					System.out.println();
    				}
    			}
    			String [] words = line.split(",");
    			int rid = Integer.valueOf(words[0]);;
    			repoIssuesNum.put(rid, 0);
    		}
    		System.out.println("\n repos number is "+repoIssuesNum.size());
    		
    		i=0;
    		int count=0;//统计project_id为空的数量
    		File f = new File(Constants.screenFile+"tables\\issues.csv");
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
    			int cid = Integer.valueOf(words[0]);
    			if(words[1].equals("\\N")){
    				count++;
    				continue;
    			}
    			int rid = Integer.valueOf(words[1]);
    			Integer c = repoIssuesNum.get(rid);
    			if(c==null){
    				continue;
    			}else{
    				c++;
    				repoIssuesNum.put(rid, c);
    			}
    		}
    		System.out.println("\n"+f.getName()+" all lines number : "+i);
    		System.out.println(" repo_id 为空的 数量 : "+count);
    		System.out.println("repos numbers : "+repoIssuesNum.size());
    		
    		BufferedWriter out = new BufferedWriter(new FileWriter(Constants.screenFile+"features\\repoIssuesNum.csv"));
    		for(Map.Entry<Integer, Integer> t:repoIssuesNum.entrySet()){
    			int rid =t.getKey();
    			out.write(rid+","+t.getValue()+"\n");
    		}
    		out.close();
        }catch(Exception e){
        	e.printStackTrace();
        }
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Issues issue = new Issues();
		long start = System.currentTimeMillis();

	//	issue.userIssuesNum();
		issue.userIssuesNumMonth();
		issue.getMouthData(3);
		issue.getMouthData(12);
//		issue.repoIssuesNum();
		long end = System.currentTimeMillis();
		System.out.println("计算用时="+(end-start)/1000);
	}

}
