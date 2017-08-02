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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import utils.BF;
import utils.Constants;

public class Projects {

	/**
	 * @param args
	 */
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	File projectsFile = new File(Constants.dirname+"dump\\projectsND.csv");
	String path = projectsFile.getParent();
	File usrFile = new File(Constants.dumpFile+"features\\USR\\usersInfo.csv");
	File orgFile = new File(Constants.dumpFile+"features\\ORG\\usersInfo.csv");
	public void preproProjects(){
		String line = "";
		int i=0,count=0;
		try{
			File f = new File(Constants.dirname+"dump\\projects.csv");
			BufferedReader br =new BufferedReader(new FileReader(f),5*1024*1024);;
			System.out.println(f.getPath());
			BufferedWriter out = new BufferedWriter(new FileWriter(
					new File(Constants.dirname+"preproed\\"+f.getName())));
			while((line = br.readLine())!=null){
				i++;
//				if(i<5565705){
//					continue;
//				}
//				System.out.println(i+" : "+line);
//				if(i>5565715){
//				//	System.out.println("\n"+i+" : "+line);
//
//					break;
//				}
				String[] words = line.split(",");
				String next_row = "";
				while(words.length<10){
					next_row = br.readLine();
					i++;
					line += next_row;
					words = line.split(",");
				}
			//	int id = Integer.valueOf(words[0]);
				
				while(line.endsWith("\\")){
					next_row = br.readLine();
					i++;
					line += next_row;
				}
				words = line.split(",");
				if(!line.endsWith("\\N")){
					while(Character.isDigit(line.charAt(line.length()-1))==false)//倒数第yi位不是数字
					{ 
						next_row = br.readLine();
						i++;
						line += next_row;
				//		System.out.println(i+" : "+line);
						if(line.endsWith("\\N")){
							break;
						}
						words = line.split(",");
					}
				}
				
				count++;
				out.write(line+"\n");

				if(i%10000==0){
					System.out.print(i/10000+" ");
					if(i/10000%25==0){
						System.out.println();
					}
					out.flush();
//					if(i==1650000){
//						System.out.println("\n"+i+" : "+line);
//						break;
//					}
					
				//	break;
				}
			
			//	System.out.println(line);
				
			}
			System.out.println("\n项目总数："+count);
			out.close();
		}catch(Exception e){
			System.out.println("\n"+i+" : "+line);
			e.printStackTrace();
		}
		
		
	}
	public void RepoDeleted(){//根据被选中的repos对watchers进行筛选
		
		File usersFP = new File(Constants.dirname+"\\preproed\\projects.csv");
    	System.out.println("read  "+usersFP.getName()+", waiting......");
		String [] words;
		String line = null;
		int i=0,count=0;
        try{
        	File outFile = new File(Constants.dirname+"\\preproed\\projectsND.csv");//NoDeleted
    		PrintWriter out = new PrintWriter(
    		new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),"utf-8")));
          
        	BufferedReader reader = BF.readFile(usersFP);
    		while((line=reader.readLine())!=null){
    			i++;
    			if(i%1000000==0){
    				System.out.print(i/1000000+" ");
    				if(i/1000000%25==0){
    					System.out.println();
    				}
    				out.flush();
    			}
    			words = line.split(",");
    			int rid = Integer.valueOf(words[0]);
    			int deleted = Integer.valueOf(words[words.length-3]);
    			if(deleted==1){
    				continue;
    			}
    			out.write(line+"\n");
    			count++;
    		}
    	
    		System.out.println("\n 筛选前（以User筛选过的）项目数量： "+i);
    		System.out.println("\n 筛选后（No Deleted）项目数量："+count);
    	
    		out.close();
    		
    		
        }catch(Exception e){
			System.out.println(i+" "+line);
			e.printStackTrace();
		}
	}
	public void reposYear() throws Exception{
		String line="";
		DecimalFormat doubleDF = new DecimalFormat("######0.00");   
		Date thisTime = df.parse(Constants.currentTime);
		long t1 = thisTime.getTime();
		BufferedWriter out = new BufferedWriter(
				new FileWriter(Constants.screenFile+"features\\reposYear.txt"));
		int i=0;
		File file = new File(Constants.screenFile+"tables\\projects.csv");
		BufferedReader reader = BF.readFile(file);
		while((line=reader.readLine())!=null){
			i++;
			if(i%1000000==0){
				System.out.print(i/1000000+" ");
				if(i/1000000%25==0){
					System.out.println();
				}
				out.flush();
			}
	//		System.out.println(line);
//			break;
			String[] words = line.split(",");
			String id = words[0];
			Date d = df.parse(words[words.length-5].substring(1,words[words.length-5].length()-1));
			long t2 = d.getTime();
			long time = (t1-t2)/1000;
			double Dyear = (double)time/(60*60*24*365);
			out.write(id+","+(doubleDF.format(Dyear))+"\n");
			
		}
		System.out.println(i);
		out.close();
	}
	public void projectsYearToDB() throws IOException{
		try{
			TreeMap<Integer,Integer> RepowatchersNum = new TreeMap<Integer,Integer>();
			String line = "";
			File file = new File("Data\\Feature\\projectsYear.txt");
			Connection conn = BF.conn();
		    conn.setAutoCommit(false); // 设置手动提交  
		    int count = 0;  
		    String sqlstr = "create table reposYear(repo_id int(255),year double);";
		    //获取对象
	        Statement stmt = conn.createStatement();  
	        stmt.executeUpdate(sqlstr);
	        System.out.println("创建成功！");
		    String insert_sql = "INSERT INTO github.reposYear(repo_id,year) VALUES (?,?)"; 
	        System.out.println(insert_sql);
		    PreparedStatement psts = conn.prepareStatement(insert_sql);
			System.out.println(file.getName());
			BufferedReader r1 = BF.readFile(file);
			while((line = r1.readLine())!=null){
				count++;
				String[] words = line.split(",");
				int rid = Integer.valueOf(words[0]);
				double uid = Double.valueOf(words[1]);
				
				psts.setInt(1, Integer.valueOf(words[0]));
			    psts.setDouble(2, Double.valueOf(words[1]));
			    psts.addBatch();          // 加入批量处理  
			    if(count%10000 ==0){
			    	
			       psts.executeBatch(); // 执行批量处理  
			 	   conn.commit();  // 提交  
			 	  System.out.println(count);
			    }
			}
			psts.executeBatch(); // 最后不足1万条的数据  
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
	
	public void projectsToOriFork(){
		String line="";
		try{
			int o=0,f=0;
			int i=0;
	//		File file = new File(Constants.screenFile+"tables\\projects.csv");
		
			File fileO = new File(projectsFile.getParent()+"\\ProjectsOriginal.csv");  
		    BufferedWriter outO = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileO),"utf-8"));  
			File fileF = new File(projectsFile.getParent()+"\\ProjectsFork.csv");  
		    BufferedWriter outF = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileF),"utf-8"));  
			File fileForkNet = new File(projectsFile.getParent()+"\\ProjectsForkNet.csv");  
		    BufferedWriter outForkNet = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileForkNet),"utf-8"));  
			BufferedReader reader = BF.readFile(projectsFile);
			while((line=reader.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if((i/100000)%25==0)
						System.out.println(" ");
					outO.flush();
					outF.flush();
					outForkNet.flush();
				}
				String[] words = line.split(",");
				String id = words[0];
				String forked_fromS = words[words.length-4];
				if(forked_fromS.equals("\\N")){
					outO.write(line+"\n");
					o++;
				}else{
					Integer forked_from = Integer.valueOf(forked_fromS);
					if(forked_from!=null){
						outF.write(line+"\n");
						f++;
						outForkNet.write(id+","+forked_from+"\n");
					}else{
						o++;
						outO.write(line+"\n");
					}
				}
			}
			System.out.println("All down ! "+i);
			System.out.println("OriginalNum is: "+o+" , ForkedNum is "+f);
			outF.close();
			outO.close();
			outForkNet.close();
		}catch(Exception e){
			System.out.println(line);
			e.printStackTrace();
		}
		
	}
	public void projectsIDToOriFork(){
		String line="";
		try{
			int o=0,f=0;
			int i=0;
			File file = new File(Constants.screenFile+"tables\\projects.csv");
			File fileO = new File(file.getParent()+"\\ProjectsIDOriginal.csv");  
		    BufferedWriter outO = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileO),"utf-8"));  
			File fileF = new File(file.getParent()+"\\ProjectsIDFork.csv");  
		    BufferedWriter outF = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileF),"utf-8"));  
			BufferedReader reader = BF.readFile(file);
			while((line=reader.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if((i/100000)%25==0)
						System.out.println(" ");
					outO.flush();
					outF.flush();
				}
				String[] words = line.split(",");
				String forked_fromS = words[words.length-4];
				if(forked_fromS.equals("\\N")){
					outO.write(line+"\n");
					o++;
				}else{
					Integer forked_from = Integer.valueOf(forked_fromS);
					if(forked_from!=null){
						outF.write(words[0]+","+words[2]+"\n");
						f++;
					}else{
						o++;
						outO.write(words[0]+","+words[2]+"\n");
					}
				}
				
			}
			System.out.println("All down ! "+i);
			System.out.println("OriginalNum is: "+o+" , ForkedNum is "+f);
			outF.close();
			outO.close();
		}catch(Exception e){
			System.out.println(line);
			e.printStackTrace();
		}
		
	}
	public void userReposNum(String type) throws IOException{//分别为Original和Fork的项目进行分析的
		File f = new File(Constants.screenFile+"tables\\projects"+type+".csv");
		BufferedWriter out = new BufferedWriter(
				new FileWriter(Constants.dirname+"\\features\\user"+type+"ReposNum.csv"));
		TreeMap<Integer,Integer> userReposNum = new TreeMap<Integer,Integer>();
		String line = "";
		int i=0;
		BufferedReader r1 = BF.readFile(f);
		System.out.println(f.getName());
		while((line = r1.readLine())!=null){
		//	System.out.println(line);
			i++;
			if(i%10000==0){
				System.out.print(i/10000+" ");
				if((i/10000)%25==0)
					System.out.println(" ");
				out.flush();
			}
			String[] words = line.split(",");
			int rid = Integer.valueOf(words[0]);
			int uid = Integer.valueOf(words[2]);
			Integer c = userReposNum.get(uid);
			if(c==null)
				c=0;
			c++;
			userReposNum.put(uid, c);
		}
		System.out.println("projects: "+i);
		System.out.println("users: "+userReposNum.size());
		for(Map.Entry<Integer,Integer> m:userReposNum.entrySet()){
			int uid = m.getKey();
			out.write(uid+","+userReposNum.get(uid)+"\n");
		}
		out.close();
	}
	public void userReposList(String type) throws IOException{
		String line = "";
		File f = new File(Constants.screenFile+"tables\\projects"+type+".csv");
		BufferedWriter out = new BufferedWriter(
				new FileWriter(Constants.screenFile+"\\features\\user"+type+"ReposList.csv"));
		
		TreeMap<Integer,ArrayList<Integer>> userReposList = new TreeMap<Integer,ArrayList<Integer>>();
		int i=0;
		
		BufferedReader r1 = BF.readFile(f);
		System.out.println(f.getName());
		while((line = r1.readLine())!=null){
			i++;
			if(i%10000==0){
				System.out.print(i/10000+" ");
				if((i/10000)%25==0)
					System.out.println(" ");
				out.flush();
			}
			String[] words = line.split(",");
			int rid = Integer.valueOf(words[0]);
			int uid = Integer.valueOf(words[2]);
			ArrayList<Integer> list = userReposList.get(uid);
			if(list==null){
				list = new ArrayList<Integer>();
			}
			list.add(rid);
			userReposList.put(uid, list);
		}
		System.out.println("projects: "+i);
		System.out.println("users: "+userReposList.size());
		for(Map.Entry<Integer,ArrayList<Integer>> m:userReposList.entrySet()){
			int uid = m.getKey();
			ArrayList<Integer> list = m.getValue();
			out.write(uid+",");
			for(Integer t:list){
				out.write(t+" ");
			}
			out.write("\n");
		}
		
		r1.close();
		out.close();
	}
	public void userReposNet(String userType) throws IOException{
		String line = "";
		int i=0;
		TreeMap<Integer,ArrayList<Integer>> userListMap = new TreeMap<Integer,ArrayList<Integer>>();
    	File usersFile = new File(path+"\\features\\"+userType+"\\usersInfo.csv");
    	BufferedReader reader = BF.readFile(usersFile,"utf8");
    	System.out.println("read  "+usersFile.getName()+", waiting......");
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
			userListMap.put(userid, null);
		}
		System.out.println("\n USERs number is "+userListMap.size());
		
		i=0;
		BufferedReader r1 = BF.readFile(projectsFile);
		System.out.println(projectsFile);
		while((line = r1.readLine())!=null){
			i++;
			if(i%1000000==0){
				System.out.print(i/1000000+" ");
				if((i/1000000)%25==0)
					System.out.println(" ");
			}
			String[] words = line.split(",");
			Integer rid = Integer.valueOf(words[0]);
			Integer uid = Integer.valueOf(words[2]);
			
			String forkedFrom = words[words.length-4];
			if(forkedFrom.equals("\\N")||forkedFrom==null){
				continue;
			}
			
			if(!userListMap.containsKey(uid)){
				continue;
			}
			ArrayList<Integer> list = userListMap.get(uid);
			if(list==null){
				list = new ArrayList<Integer>();
			}
			list.add(rid);
			userListMap.put(uid, list);
		}
		r1.close();
		System.out.println("projects: "+i);
		System.out.println("users: "+userListMap.size());
		BufferedWriter outL = new BufferedWriter(
				new FileWriter(usersFile.getParent()+"\\"+userType+"_ForkReposList.csv"));
		BufferedWriter outN = new BufferedWriter(
				new FileWriter(usersFile.getParent()+"\\"+userType+"_ForkReposNum.csv"));
		for(Map.Entry<Integer,ArrayList<Integer>> m:userListMap.entrySet()){
			int uid = m.getKey();
			ArrayList<Integer> list = m.getValue();
			if(list==null){
//				outN.write(uid+","+0+"\n");
//				outL.write(uid+","+"\n");
			}else{
				outN.write(uid+","+list.size()+"\n");
				outL.write(uid+",");
				for(Integer t:list){
					outL.write(t+" ");
				}
				outL.write("\n");
			}
		}
			
		outN.close();
		outL.close();
	}

	public void userProjectNumMonth() throws Exception{
		Date thisTime = Constants.df.parse(Constants.currentTime);
		int m = thisTime.getMonth();
		int y = thisTime.getYear();
		
		int maxMonth = 106;//最多有多少个月
		
		TreeMap<Integer,TreeMap<Integer,Integer>> authorReposNum = new TreeMap<Integer,TreeMap<Integer,Integer>>();
		String line = "";
		int i=0;
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
//			if(i>900000)
//				break;
			String [] words = line.split(",");
			int userid = Integer.valueOf(words[0]);;
			authorReposNum.put(userid, new TreeMap<Integer,Integer> ());
		}
		System.out.println("\n USERs number is "+authorReposNum.size());
		reader = BF.readFile(orgFile,"utf8");
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
			authorReposNum.put(userid, new TreeMap<Integer,Integer> ());
		}
		System.out.println("\n USERs number is "+authorReposNum.size());
		//File f = new File(Constants.screenFile+"tables\\projects.csv");
		System.out.println(projectsFile.getName());
		BufferedReader r1 = BF.readFile(projectsFile);
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
			int authorid = Integer.valueOf(words[2]);
			Date d = Constants.df.parse(words[words.length-5].substring(1,words[words.length-5].length()-1));
			int month = d.getMonth();
			int year = d.getYear();
			int index = (y-year)*12+(m-month);
			
			TreeMap<Integer,Integer> c = authorReposNum.get(authorid);
			if(c==null){
//				c = new TreeMap<Integer,Integer>();
//				c.put(index, 1);
				continue;
			}else{
				Integer cc = c.get(index);
				if(cc==null){
					cc=0;
				}
				cc++;
				c.put(index, cc);
			}
			authorReposNum.put(authorid, c);
		}
		System.out.println(i);
		System.out.println(authorReposNum.size());
//		for(Map.Entry<Integer, TreeMap<Integer,Integer>> t:authorReposNum.entrySet()){
//			TreeMap<Integer,Integer> c= t.getValue();
//			if(maxMonth<c.size()){
//				maxMonth = c.size();
//			}
//		}
		BufferedWriter out = new BufferedWriter(
				new FileWriter(projectsFile.getParent()+"\\features\\Monthly\\userReposNumMonth.csv"));
		for(Map.Entry<Integer, TreeMap<Integer,Integer>> t:authorReposNum.entrySet()){
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
			BufferedWriter out = new BufferedWriter(
					new FileWriter(projectsFile.getParent()+"\\features\\"+n+"Month\\user"+n+"MonthRepoNum.txt"));
			int i=0;
			File f = new File(projectsFile.getParent()+"\\features\\Monthly\\userReposNumMonth.csv");
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
	
	public void sortListByUid()throws IOException{
		BufferedReader br = BF.readFile("Data\\Feature\\userReposList.txt");
		String line = "";
		TreeMap<Integer,ArrayList<Integer>> userReposList = new TreeMap<Integer,ArrayList<Integer>>();

		while((line = br.readLine())!=null){
			String[] words = line.split(",");
			int uid = Integer.valueOf(words[0]);
			String[]  repos = words[1].split(" ");
			ArrayList<Integer> list = new ArrayList<Integer>();
			for(int i=0;i<repos.length;i++){
				list.add(Integer.valueOf(repos[i]));
			}
			userReposList.put(uid, list);
		}
		System.out.println("readed!");
		List<Map.Entry<Integer, ArrayList<Integer>>> list=
		    new ArrayList<Map.Entry<Integer,ArrayList<Integer>>>(userReposList.entrySet());

		//排序
		Collections.sort(list, new Comparator<Map.Entry<Integer, ArrayList<Integer>>>() {   
		    public int compare(Map.Entry<Integer,ArrayList<Integer>> m1, Map.Entry<Integer,ArrayList<Integer>> m2) {     
		        //return (m2.getValue() - m1.getValue());
		        return (m1.getKey()-m2.getKey());
		//m1-m2     递增顺序
		//m2-m1     递减顺序
		    }
		});
		System.out.println("sorted!");
		BufferedWriter out = new BufferedWriter(new FileWriter("Data\\Feature\\UserReposListSortedByUid.txt"));
		for(Map.Entry<Integer,ArrayList<Integer>> m:list){
			int uid = m.getKey();
			out.write(uid+",");
			ArrayList<Integer> repolist = m.getValue();
			for(int j=0;j<repolist.size();j++){
				if(j==repolist.size()-1)
					out.write(repolist.get(j)+"\n");
				else
					out.write(repolist.get(j)+" ");
			}
		}
		
	}
	
	//20170406
	public void userReposAna(){
		try{
			BufferedReader r = BF.readFile("Data\\Feature\\userReposNum.txt");
			String line = "";
			TreeMap<Integer,Integer> anaMap = new TreeMap<Integer,Integer>();
			int[] datas = {1,2,3,4,5,6,7,8,9,10,20,30,40,50,60,70,80,90,100,200,300,400,500,1000,2000,5000,10000,50000,10000000};
			for(int i=0;i<datas.length;i++){
				anaMap.put(datas[i], 0);
			}
			
			while((line=r.readLine())!=null){
				String[] words = line.split(",");
				int uid = Integer.valueOf(words[0]);
				int repoNum = Integer.valueOf(words[1]);
				int start = 0;
				for(int i=0;i<datas.length;i++){
					if(repoNum<=datas[i]){
						int c = anaMap.get(datas[i]);
						c++;
						anaMap.put(datas[i], c);
						if(i==datas.length-1){
							System.out.println(datas[i]+">=,"+uid);
						}
						break;
					}
				}
			}
			File f = new File("Data\\Feature\\Repos");
			if(!f.exists()){
				f.mkdirs();
			}
			BufferedWriter out = new BufferedWriter(new FileWriter(Constants.screenFile+"\\anaUserRepos.csv",true));
			for(Map.Entry<Integer, Integer> m:anaMap.entrySet()){
				out.write(m.getKey()+","+m.getValue()+"\n");
			}
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void userFingRepos(){
		try{
			File fer = new File("Data\\Feature\\Followers");
			File repo = new File("Data\\Feature\\Repos");
			BufferedReader rer = BF.readFile(Constants.screenFile+"\\userFollowingsNum.txt");
			BufferedReader ring = BF.readFile("Data\\Feature\\userReposNum.txt");
			String line = "";
			TreeMap<Integer,Integer> userFerMap = new TreeMap<Integer,Integer>();
			while((line=rer.readLine())!=null){
				String[] words = line.split(",");
				int uid = Integer.valueOf(words[0]);
				int ferNum = Integer.valueOf(words[1]);
				userFerMap.put(uid,ferNum);
			}
			BufferedWriter out = new BufferedWriter(new FileWriter(Constants.screenFile+"\\userFingRepo.csv"));
			out.write("uid,fingNum,repoNum\n");
			while((line = ring.readLine())!=null){
				String[] words = line.split(",");
				int uid = Integer.valueOf(words[0]);
				int repoNum = Integer.valueOf(words[1]);
				Integer ferNum = userFerMap.get(uid);
				if(ferNum==null)
					ferNum=0;
				userFerMap.remove(uid);
				out.write(uid+","+ferNum+","+repoNum+"\n");
			}
			if(userFerMap.size()>0){
				for(Map.Entry<Integer, Integer> m:userFerMap.entrySet()){
					out.write(m.getKey()+","+m.getValue()+",0"+"\n");
				}
			}
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void userFingORepos(){//原始的项目（非fork）关系
		try{
			String line = "";
			File repo = new File("Data\\Feature\\Repos");
			
			File fer = new File("Data\\Feature\\Followers");
			
			BufferedReader rer = BF.readFile(Constants.screenFile+"\\userFollowingsNum.csv");
			BufferedReader rRepo = BF.readFile("Data\\Feature\\userReposNum.csv");
		
			TreeMap<Integer,Integer> userFerMap = new TreeMap<Integer,Integer>();
			while((line=rer.readLine())!=null){
				String[] words = line.split(",");
				int uid = Integer.valueOf(words[0]);
				int ferNum = Integer.valueOf(words[1]);
				userFerMap.put(uid,ferNum);
			}
			BufferedWriter out = new BufferedWriter(new FileWriter(Constants.screenFile+"\\userFingRepo.csv"));
			out.write("uid,fingNum,repoNum\n");
			while((line = rRepo.readLine())!=null){
				String[] words = line.split(",");
				int uid = Integer.valueOf(words[0]);
				int repoNum = Integer.valueOf(words[1]);
				Integer ferNum = userFerMap.get(uid);
				if(ferNum==null)
					ferNum=0;
				userFerMap.remove(uid);
				out.write(uid+","+ferNum+","+repoNum+"\n");
			}
			if(userFerMap.size()>0){
				for(Map.Entry<Integer, Integer> m:userFerMap.entrySet()){
					out.write(m.getKey()+","+m.getValue()+",0"+"\n");
				}
			}
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void selectProjectOwnerandName(String owner,String name){
		try{
			String line = "";
			int i=0;
	 //   	File usersFP = new File(Constants.screenFile+"\\projects.csv");
	    	System.out.println("read  "+projectsFile.getName()+", waiting......");
	    	BufferedReader reader = BF.readFile(projectsFile,"utf8");
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
				String url = words[1];
				String repoName = words[3];
				
				if(url.contains(owner)&&url.contains(name)){
					System.out.println();
					System.out.println(line);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void selectProjectsByOwnerID(Integer ownerID){
		try{
			String line = "";
			int i=0;
	 //   	File usersFP = new File(Constants.screenFile+"\\projects.csv");
	    	System.out.println("read "+projectsFile.getName()+", waiting......");
	    	BufferedReader reader = BF.readFile(projectsFile,"utf8");
			while((line=reader.readLine())!=null){
				i++;
//				if(i%100000==0){
//					System.out.print(i/100000+" ");
//					if(i/100000%25==0){
//						System.out.println();
//					}
//				}
				String [] words = line.split(",");
				int id = Integer.valueOf(words[0]);
				String url = words[1];
				String repoName = words[3];
				Integer userid = Integer.valueOf(words[2]);
				if(userid.equals(ownerID)){
				//	System.out.println();
					System.out.println(line);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void selectProjectsListByOwnerID(Integer ownerID){
		try{
			String line = "";
			int i=0;
	    	File file = new File(projectsFile.getParent()+"\\features\\userReposList.csv");
	    	System.out.println("read "+file);
	    	BufferedReader reader = BF.readFile(file,"utf8");
			while((line=reader.readLine())!=null){
				i++;
//				if(i%100000==0){
//					System.out.print(i/100000+" ");
//					if(i/100000%25==0){
//						System.out.println();
//					}
//				}
				String [] words = line.split(",");
				
				Integer userid = Integer.valueOf(words[0]);
				if(userid.equals(ownerID)){
				//	System.out.println();
					System.out.println(line);
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void selectRepoID(Integer Repoid){
		try{
			String line = "";
			int i=0;
	    	System.out.println("read  "+projectsFile.getName()+", waiting......");
	    	BufferedReader reader = BF.readFile(projectsFile,"utf8");
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
				String url = words[1];
				String repoName = words[3];
				
				if(Repoid.equals(id)){
					System.out.println();
					System.out.println(line);
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();
		Projects p = new Projects();
//		p.preproProjects();
	// 	p.RepoDeleted();
	//	p.reposYear();
	
	//	p.projectsToOriFork();
	
//		p.userReposNet("ORG");//所有项目的用户拥有数量分析
	//	p.userReposNet("Original");
	//	p.userReposNet("Fork");
	//	p.userProjectNumMonth();
	//	p.getMouthData(12);
	//	p.selectProjectOwnerandName("jasoncalabrese","indy-e4b");
	//	p.selectRepoID(6143269);
	//	p.selectRepoID(63811);
//		p.selectRepoID(126051);
	//	p.selectProjectsByOwnerID(31562345);
	//	p.selectProjectsListByOwnerID(31562345);
		long end = System.currentTimeMillis();
		System.out.println("计算用时="+(end-start)/1000);
	}

}
