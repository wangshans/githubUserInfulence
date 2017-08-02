package wss.githubFeatures;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import utils.BF;
import utils.Constants;

public class Commits {

	/**
	 * @param args
	 */
	public void prepro() throws IOException{
		String line = "";
		int i=0;
		File slited_files = new File("D:\\github\\mysql-2015-09-25\\splited_commits");
		File[] files = slited_files.listFiles();
		for(File f:files){
			System.out.println(f.getName());
			BufferedReader r1 = BF.readFile(f);
			while((line = r1.readLine())!=null){
				System.out.println(line);
				i++;
				String[] words = line.split(",");
				int cid = Integer.valueOf(words[0]);
				int authorId = Integer.valueOf(words[2]);
				int committerId = Integer.valueOf(words[3]);
				if(authorId!=committerId){
					System.out.println(line);
				}
				break;
			}
		}
	}
	public void userCommitsNum() throws IOException{
		
		BufferedWriter out = new BufferedWriter(new FileWriter
				(Constants.screenFile+"features\\userCommitsNum.csv"));
		TreeMap<Integer,Integer> authorCommitsNum = new TreeMap<Integer,Integer>();
		String line = "";
		int i=0;
		File f = new File(Constants.screenFile+"tables\\commits.csv");
		System.out.println(f.getName());
		BufferedReader r1 = BF.readFile(f);
		while((line = r1.readLine())!=null){
			i++;
			String[] words = line.split(",");
			int cid = Integer.valueOf(words[0]);
		//	int authorid = Integer.valueOf(words[2]);
			int committerId = Integer.valueOf(words[3]);
			int userid = committerId;
			Integer c = authorCommitsNum.get(userid);
			if(c==null)
				c=0;
			c++;
			authorCommitsNum.put(userid, c);
			if(i%100000==0){
				System.out.print(i/100000+" ");
				if(i/100000%25==0){
					System.out.println();
				}
			}
		}
		System.out.println(i);
		System.out.println(authorCommitsNum.size());
		for(Map.Entry<Integer, Integer> t:authorCommitsNum.entrySet()){
			int rid =t.getKey();
			out.write(rid+","+t.getValue()+"\n");
		}
		out.close();
	}
	
	public void userCommitsNumtoDB(){
    	//将结果插入数据库
		String line = "";
		try{
			int i=0;
			Connection conn = BF.conn();
		    conn.setAutoCommit(false); // 设置手动提交  
		    int count = 0;  
		    String sqlstr = "create table userCommitsNum(user_id int(255),commitsNum int(255));";
		    System.out.println(sqlstr);
	        Statement stmt = conn.createStatement();   //获取对象
	        stmt.executeUpdate(sqlstr);
	        System.out.println("创建成功！");
		    String insert_sql = "INSERT INTO github.userCommitsNum VALUES (?,?)"; 
	        System.out.println(insert_sql);
		    PreparedStatement psts = conn.prepareStatement(insert_sql);
			String file = "Data\\Feature\\userCommitsNum.txt";
			System.out.println(file);
			BufferedReader r1 = BF.readFile(file);
	        while ((line=r1.readLine())!=null){
	           // 读一整行
	        	count++;
	        	String[] words = line.split(",");
				psts.setInt(1, Integer.valueOf(words[0]));
			    psts.setInt(2, Integer.valueOf(words[1]));
			    psts.addBatch();          // 加入批量处理  
			    if(count%10000 ==0){
			    	System.out.println(count);
			       psts.executeBatch(); // 执行批量处理  
			 	   conn.commit();  // 提交  
			    }
	        	//   continue;
           }  
			psts.executeBatch(); // 最后不足1万条的数据  
			conn.commit();  // 提交  
		    System.out.println("All down : " + count);  
	        conn.close(); 
	        System.out.println(i);
	    }catch(Exception e){
	    	System.out.println(line);
	    	e.printStackTrace();
	    }    
	 
	}
	public void userCommitsList() throws IOException{
//		BufferedReader br2 = BF.readFile("Data\\Feature\\userCommitsList.txt");
		String line = "";
//		ArrayList<Integer> authorsID = new ArrayList<Integer>();
//		while((line = br2.readLine())!=null){
//			String[] words = line.split(",");
//			int uid = Integer.valueOf(words[0]);
//			authorsID.add(uid);
//		}
//		System.out.println("Readed 1! "+authorsID.size());
		BufferedReader br = BF.readFile("Data\\Feature\\userCommitsNum.txt");
		TreeMap<Integer,Integer> authorCommitsNum = new TreeMap<Integer,Integer>();
		int i=0;
		while((line = br.readLine())!=null){
			i++;
			if(i%100000==0){
				System.out.print(i/100000+" ");
				if(i/100000%25==0){
					System.out.println();
				}
			}
			String[] words = line.split(",");
			int uid = Integer.valueOf(words[0]);
//			if(authorsID.contains(uid))
//				continue;
			int rnum = Integer.valueOf(words[1]);
			authorCommitsNum.put(uid, rnum);
		}
		System.out.println("Readed 2!"+authorCommitsNum.size());
		BufferedWriter out2 = new BufferedWriter(new FileWriter("Data\\Feature\\userCommitsList2.txt"));
		TreeMap<Integer,ArrayList<Integer>> authorCommitsList = new TreeMap<Integer,ArrayList<Integer>>();
		i=0;
		File f = new File("E:\\mysql-2015-09-25\\dump\\commits.csv");
		
		BufferedReader r1 = BF.readFile(f);
		while((line = r1.readLine())!=null){
			i++;
			if(i%10000==0){
				System.out.println(i);
			}
			String[] words = line.split(",");
			int cid = Integer.valueOf(words[0]);
			System.out.println(cid);
			int userid = Integer.valueOf(words[3]);//committerID
//			if(authorsID.contains(authorid))
//				continue;
			ArrayList<Integer> list = authorCommitsList.get(userid);
			if(list==null){
				list = new ArrayList<Integer>();
			}
			list.add(cid);
			authorCommitsList.put(userid, list);
			int num = authorCommitsNum.get(userid);
			if(list.size()==num){//说明该user的commit数完了
				out2.write(userid+",");
				for(int j=0;j<list.size();j++){
					if(j==list.size()-1)
						out2.write(list.get(j)+"\n");
					else
						out2.write(list.get(j)+" ");
				}
				out2.flush();
			//	authorCommitsList.put(authorid, null);//已经写完了，则置为空节省空间
				authorCommitsList.remove(userid);//已经写完了，移除该用户
			}
		}
		
		r1.close();
		out2.close();
	}
	public void repoCommitsNum(){
		String line = "";
		int j = 0;
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter
					(Constants.screenFile+"features\\repoCommitsNum.txt"));
			TreeMap<Integer,Integer> repoCommitsNum = new TreeMap<Integer,Integer>();
			int i=0;
			File f = new File(Constants.screenFile+"tables\\commits.csv");
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
				int cid = Integer.valueOf(words[0]);
				if(words[4].equals("\\N")){
					j++;
					continue;
				}
					
				int repoid = Integer.valueOf(words[4]);
				Integer c = repoCommitsNum.get(repoid);
				if(c==null)
					c=0;
				c++;
				repoCommitsNum.put(repoid, c);
			}
			for(Map.Entry<Integer, Integer> t:repoCommitsNum.entrySet()){
				int rid =t.getKey();
				out.write(rid+","+t.getValue()+"\n");
			}
			out.close();
			System.out.println(j);
		}catch(Exception e){
			System.out.println(line);
			e.printStackTrace();
		}
		System.out.println(j);
	}
	
	public void commitsToSomeone(){//预处理Commit文件，将提交的project换成对应的OwnerID
		String line = "";
		int i = 0;
		try{
			BufferedWriter outSelf = new BufferedWriter(new FileWriter
					(Constants.screenFile+"features\\commitsToSelf.csv"));
			BufferedWriter outOther = new BufferedWriter(new FileWriter
					(Constants.screenFile+"features\\commitsToOther.csv"));
			BufferedWriter outElse = new BufferedWriter(new FileWriter
					(Constants.screenFile+"features\\commitsElse.csv"));
			File projects = new File(Constants.screenFile + "tables\\projects.csv");
			TreeMap<Integer,Integer> repoOwnerMap = new TreeMap<Integer,Integer>();
			BufferedReader r1 = BF.readFile(projects);
			while((line = r1.readLine())!=null){
				i++;
				if(i==1){
					System.out.println(line);
				}
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%25==0){
						System.out.println();
					}
					
				}
				if(i>16000000){
					System.out.println(line);
					break;
				}
				String[] words = line.split(",");
				int rid = Integer.valueOf(words[0]);
				int ownerid = Integer.valueOf(words[2]);
				repoOwnerMap.put(rid, ownerid);
			}
			System.out.println(i);
			System.out.println(repoOwnerMap.size());
			r1.close();
			i=0;
			File f = new File(Constants.screenFile+"tables\\commits.csv");
			System.out.println(f.getName());
			BufferedReader r2 = BF.readFile(f);
			while((line = r2.readLine())!=null){
				i++;
				if(i%100000==0){
    				System.out.print(i/100000+" ");
    				if(i/100000%25==0){
    					System.out.println();
    				}
    				outSelf.flush();
    				outOther.flush();
    				outElse.flush();
    			}
				String[] words = line.split(",");
				Integer cid = Integer.valueOf(words[0]);
				Integer author_id = Integer.valueOf(words[2]);
				Integer committer_id = Integer.valueOf(words[2]);
				Integer project_id = Integer.valueOf(words[4]);
				if(words[4].equals("\\N")){
					continue;
				}
				Integer owner = repoOwnerMap.get(project_id);
				if(owner==null){
				//	outElse.write(line+"\n");
					continue;
				}else{
					if(owner==author_id||owner==committer_id){
						outSelf.write(line+"\n");
						System.out.println(line+"\n");
						break;
					}else{
					//	outOther.write(line+"\n");
						continue;
					}
				}
				
			}
			outSelf.close();
			outOther.close();
			outElse.close();
		}catch(Exception e){
			System.out.println(line);
			e.printStackTrace();
		}
	}
	public void commitsToSelfNum(){
		String line = "";
		int j = 0;
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter
					(Constants.screenFile+"features\\commitsToSelf.csv"));
			TreeMap<Integer,Integer> repoCommitsNum = new TreeMap<Integer,Integer>();
			int i=0;
			File f = new File(Constants.screenFile+"tables\\commits.csv");
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
				int cid = Integer.valueOf(words[0]);
				if(words[4].equals("\\N")){
					j++;
					continue;
				}
					
				int repoid = Integer.valueOf(words[4]);
				Integer c = repoCommitsNum.get(repoid);
				if(c==null)
					c=0;
				c++;
				repoCommitsNum.put(repoid, c);
			}
			for(Map.Entry<Integer, Integer> t:repoCommitsNum.entrySet()){
				int rid =t.getKey();
				out.write(rid+","+t.getValue()+"\n");
			}
			out.close();
			System.out.println(j);
		}catch(Exception e){
			System.out.println(line);
			e.printStackTrace();
		}
		System.out.println(j);
	}
	public void repoCommitsNumtoDB(){
    	//将结果插入数据库
		String line = "";
		try{
			int i=0;
			Connection conn = BF.conn();
		    conn.setAutoCommit(false); // 设置手动提交  
		    int count = 0;  
		    String sqlstr = "create table repoCommitsNum(repo_id int(255),commitsNum int(255));";
		    System.out.println(sqlstr);
	        Statement stmt = conn.createStatement();   //获取对象
	        stmt.executeUpdate(sqlstr);
	        System.out.println("创建成功！");
		    String insert_sql = "INSERT INTO github.repoCommitsNum VALUES (?,?)"; 
	        System.out.println(insert_sql);
		    PreparedStatement psts = conn.prepareStatement(insert_sql);
			String file = "Data\\Feature\\repoCommitsNum.txt";
			System.out.println(file);
			BufferedReader r1 = BF.readFile(file);
	        while ((line=r1.readLine())!=null){
	           // 读一整行
	        	count++;
	        	String[] words = line.split(",");
				psts.setInt(1, Integer.valueOf(words[0]));
			    psts.setInt(2, Integer.valueOf(words[1]));
			    psts.addBatch();          // 加入批量处理  
			    if(count%1000 ==0){
			    	System.out.println(count);
			       psts.executeBatch(); // 执行批量处理  
			 	   conn.commit();  // 提交  
			    }
	        	//   continue;
           }  
			psts.executeBatch(); // 最后不足1万条的数据  
			conn.commit();  // 提交  
		    System.out.println("All down : " + count);  
	        conn.close(); 
	        System.out.println(i);
	    }catch(Exception e){
	    	System.out.println(line);
	    	e.printStackTrace();
	    }    
	 
	}
	
	//2017.06.24 用户Commit按时间统计，从当前月（2017年1月）一月一统计
	File usrFile = new File(Constants.dumpFile+"features\\USR\\usersInfo.csv");
	File orgFile = new File(Constants.dumpFile+"features\\ORG\\usersInfo.csv");
	File commitsFile = new File(Constants.dumpFile+"commits.csv");
	public void userCommitReposNumMonth() throws Exception{//用户提交到的项目的数量
		System.out.println("start ... ");
		Date currentTime =  Constants.df.parse( Constants.currentTime);
		Date startTime =  Constants.df.parse(Constants.startTime);
		int m = currentTime.getMonth();
		int y = currentTime.getYear();
		String line = "";
		int i=0;
		int maxMonth = 106;//最多有多少个月
	
		TreeMap<Integer,TreeMap<Integer,Integer>> authorCommitsNum = new TreeMap<Integer,TreeMap<Integer,Integer>>();
		
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
			authorCommitsNum.put(userid, new TreeMap<Integer,Integer> ());
		}
		System.out.println("\n USERs number is "+authorCommitsNum.size());
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
			int userid = Integer.valueOf(words[0]);
			TreeMap<Integer,Integer> t = new TreeMap<Integer,Integer> ();
//			for(int j=0;j<106;j++){
//				t.put(j, 0);
//			}
			authorCommitsNum.put(userid,t);
		}
		System.out.println("\n USERs number is "+authorCommitsNum.size());
		System.out.println(commitsFile.getName());
		BufferedReader r1 = BF.readFile(commitsFile);
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
			int cid = Integer.valueOf(words[0]);
			int authorid = Integer.valueOf(words[2]);
			Date d = Constants.df.parse(words[5].substring(1,words[5].length()-1));
//			if(d.compareTo(startTime)<0||d.compareTo(currentTime)>0){//
//				continue;
//			}
			int month = d.getMonth();
			int year = d.getYear();
		//	int date = d.getDate();
			time = (year+1900)+"-"+(month+1);
			int index = (y-year)*12+(m-month);
//			indexSet.add(index);
//			timeSet.add(time);
//			if(index<0)
//			{
//				System.out.println();
//				System.out.println(line);
//				break;
//			}
	//		System.out.println(words[5]);
	//		System.out.println(time);
	//		break;
			Integer repo_id = Integer.valueOf(words[4]);
			
			TreeMap<Integer,Integer> c = authorCommitsNum.get(authorid);
			if(c==null){
//				c = new TreeMap<Integer,Integer>();
//				c.put(index, 1);
				continue;
			}else{
				Integer cc = c.get(index);
				if(cc==null){
				//	cc=0;
					continue;
				}
				cc++;
				c.put(index, cc);
			}
			authorCommitsNum.put(authorid, c);
		}
		System.out.println(i);
		System.out.println(authorCommitsNum.size());
	//	System.out.println(indexSet.size());
//		BufferedWriter out = new BufferedWriter(new FileWriter
//				(commitsFile.getParent()+"\\features\\commitsTime.csv"));
//		for(String s:timeSet){
//			out.write(s+"\n");
//		}
//		out.close();
//		BufferedWriter out2 = new BufferedWriter(new FileWriter
//				(commitsFile.getParent()+"\\features\\commitsIndex.csv"));
//		for(Integer s:indexSet){
//			out2.write(s+"\n");
//		}
//		out2.close();
		
		BufferedWriter out = new BufferedWriter(new FileWriter
				(commitsFile.getParent()+"\\features\\Monthly\\userCommitsNumMonth.csv"));
//		for(Map.Entry<Integer, TreeMap<Integer,Integer>> t:authorCommitsNum.entrySet()){
//			TreeMap<Integer,Integer> c= t.getValue();
//			if(maxMonth<c.size()){
//				maxMonth = c.size();
//			}
//		}
		for(Map.Entry<Integer, TreeMap<Integer,Integer>> t:authorCommitsNum.entrySet()){
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
			BufferedWriter out = new BufferedWriter(new FileWriter
					(commitsFile.getParent()+"\\features\\"+n+"Month\\user"+n+"MonthCommitsNum.csv"));
			int i=0;
			File f = new File(commitsFile.getParent()+"\\features\\Monthly\\userCommitsNumMonth.csv");
			System.out.println(f.getName());
			BufferedReader r1 = BF.readFile(f);
			while((line = r1.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%10==0){
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
	public void selectByUserID(int userid,int repoid){
		try{
			String line = "";
			int i=0,k=0;
			BufferedWriter out = new BufferedWriter(new FileWriter
					(commitsFile.getParent()+"\\features\\"+userid+"Commits.csv"));
	    	System.out.println("read  "+commitsFile.getName()+", waiting......");
	    	BufferedReader reader = BF.readFile(commitsFile,"utf8");
	    	ArrayList<String> linesList = new ArrayList<String>();
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
				int authorid = Integer.valueOf(words[2]);
				int repoId = Integer.valueOf(words[4]);
				if(userid == authorid&&repoId==repoid){
					k++;
					linesList.add(line);
				//	out.write(line+"\n");
				//	break;
				}
				
				
			}
			System.out.println(k);
			//排序
			Collections.sort(linesList, new Comparator<String>() {   
			    public int compare(String line1, String line2) {     
			        //return (m2.getValue() - m1.getValue());
					Date d1=null,d2=null;
					try {
						d1 = Constants.df.parse(line1.split(",")[5].substring(1,line1.split(",")[5].length()-1));
						d2 = Constants.df.parse(line1.split(",")[5].substring(1,line1.split(",")[5].length()-1));

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

			        return (d2.compareTo(d1));
			//m1-m2     递增顺序
			//m2-m1     递减顺序
			    }
			});
			for(String s:linesList){
				out.write(s+"\n");
			}
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Commits c = new Commits();
		long start = System.currentTimeMillis();
	//	c.userCommitsNum();
	//	c.repoCommitsNum();
	//	c.userCommitReposNumMonth();
	//	c.getMouthData(3);
	//	c.getMouthData(12);
	//	c.commitsToSomeone();
		c.selectByUserID(20603,39289358);
		long end = System.currentTimeMillis();
		System.out.println("计算用时="+(end-start)/1000);
	}

}
