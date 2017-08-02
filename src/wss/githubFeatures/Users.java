package wss.githubFeatures;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import utils.BF;
import utils.Constants;

import com.csvreader.CsvReader;

public class Users {//文档格式预处理

	/**
	 * @param args
	 */
//	File usersFile = new File(Constants.screenFile+"tables\\users.csv");
//	File usersFile = new File(Constants.dirname+"dump\\users.csv");
	File usersFile = new File("f:\\users.csv");
	public void setUsersFile(File f){
		usersFile = f;
	}
	public void preproUsers(){//格式预处理
		File file = new File(Constants.dumpFile+"users.csv");
    	System.out.println("read  "+file+", waiting......");
		String [] words;
		String line = null;
        try{
        	File filename = new File(file.getParent()+"\\preproed\\"+file.getName());
            filename.createNewFile();
            PrintWriter out = new PrintWriter(new BufferedWriter(
            		new OutputStreamWriter(new FileOutputStream(filename),"utf-8")));
	       
        	BufferedReader reader = new BufferedReader(new FileReader(file),5*1024*1024);//如果是读大文件  则    即，设置缓存
    		int i=0;
    		while((line=reader.readLine())!=null){
    			
    			if(i!=0&&i%10000==0){
    				System.out.print(i/10000+" ");
    				if(i/10000%25==0){
    					System.out.println();
    				}
    			}
    			
    			String next_row = "";
    			while(line.endsWith("\\"))//以转义字符\结尾，表示该行未结束
				{
					if(line.charAt(line.length()-1)=='\\'){
						line = line.substring(0, line.length()-1);
					}
					next_row = reader.readLine();
					if(next_row.startsWith("\\")){
						next_row = next_row.substring(1);
					}
//					if(next_row.length()==1 || next_row.length()==0){
//					}
					
					line += next_row;
					
				}
    			if(!line.endsWith("\\N")){
    				while(!line.endsWith("\"")){
    					next_row = reader.readLine();
    					line += next_row;
    				}
    			}
    			i++;
    			out.write(line+"\n");
    			words = line.split(",");
    			if(words[0].compareTo("id")==0){
    				continue;
    			}

    		}
    		System.out.println("\nTotal users' number is "+i);
    	out.flush();
    	out.close();
        }catch(Exception e){
			System.out.println(line);
			e.printStackTrace();
		}
	}
	public void userwithoutFakeDeleted(){//
		File file = new File(Constants.dumpFile + "preproed\\users.csv");
    	System.out.println("read  "+file+", waiting......");
		String line = null;
        try{
        	int d=0;//标记为删除的数量
        	int f=0;//标记为fake的数量
        	File filename = new File(Constants.dirname+"\\screen\\usersWithoutDF.csv");
            filename.createNewFile();
            PrintWriter out = new PrintWriter(new BufferedWriter(
            		new OutputStreamWriter(new FileOutputStream(filename),"utf-8")));
            BufferedWriter outD = new BufferedWriter(new FileWriter(
            		Constants.dirname+"\\screen\\usersDeleted.csv"));//deleted的用户输出
            BufferedWriter outF = new BufferedWriter(new FileWriter(
            		Constants.dirname+"\\screen\\usersFake.csv"));//fake的用户输出
        	BufferedReader reader = new BufferedReader(new FileReader(file),5*1024*1024);//如果是读大文件  则    即，设置缓存
    		int i=0,count=0;
    		line=reader.readLine();
    		while((line=reader.readLine())!=null){
    			
    			i++;
    			if(i%100000==0){
    				System.out.print(i/100000+" ");
    				if(i/100000%25==0){
    					System.out.println();
    				}
    			}
    			String[] words = line.split(",");
    			ArrayList<String> list = new ArrayList<String>();// 每行记录一个list  
    			String tempstr = "";

    			for(int j=0;j<words.length;j++){
    				tempstr += words[j];
    				/*
    				 * 两种情况，
    				 * 一种为字符串，含引号，且引号开头，引号结尾
    				 * 		
    				 * 另一种情况，不含引号，为数字或\N
    				 */
    		//		System.out.println(tempstr);
    				if(tempstr.contains("\"")){//包含引号
    					if(tempstr.startsWith("\"")){//引号开头
    				//		System.out.println("引号开头");
    						if(tempstr.endsWith("\"")&&tempstr.length()>1){//结尾也为引号
    				//			System.out.println("结尾为引号");
								if(tempstr.endsWith("\\\"")){//结尾的引号不是转义字符加引号，即为真实的引号
					//				System.out.println("结尾引号可能为转义引号");
									//结尾引号可能为转义引号
									if(tempstr.indexOf("\\\\\"")!=-1){//结尾为\\"这种形式，即转义的是\\
										list.add(tempstr);
										tempstr = "";
										
									}else if(BF.countZi(tempstr,"\\\"")%2!=0){//含有单数个\"
										list.add(tempstr);
										tempstr = "";
										continue;
									}else{
										tempstr +=",";
		    							continue;
									}
									
									
								}else{
									list.add(tempstr);
									tempstr = "";
									continue;
								}
	    					}else{//引号开头，非引号结尾，连接下一个子串
    							tempstr +=",";
    							continue;
	    					}
    				}
    			}else{//不含引号
					if(tempstr.equals("\\N")){
						list.add(tempstr);
						tempstr = "";
					}else{
						list.add(tempstr);
						tempstr = "";
					}
				}
    		}
    			
    			if(list.size()!=13){
    				System.out.println("\n"+list.size()+" i:"+i);
    				System.out.println(line);
    				for(String s:list){
    					System.out.println(s);
    				}
    				break;
    			}

    			Integer userid = Integer.valueOf(list.get(0));
    			Integer deleted = Integer.valueOf(list.get(6));
   				Integer fake = Integer.valueOf(list.get(5));
   		
    			if(deleted==1){//已删除的用户数量,
    				d++;
    				outD.write(line+"\n");
    			}
    			if(fake==1){//已删除的用户数量,
    				f++;
    				outF.write(line+"\n");
    			}
    			if(deleted==0 && fake==0){
    				count++;
    				out.write(line+"\n");
    			}
    		
    		}
    	out.flush();
    	out.close();
    	outD.close();
    	outF.close();
    	System.out.println("\n 已删除的用户数量为："+d);
    	System.out.println("\n FAKE的用户数量为："+f);
    	System.out.println("\n 筛选后用户后的用户数量为："+count);
        }catch(Exception e){
			System.out.println(line);
			e.printStackTrace();
		}
	}
	
	public void usersToType(){
		String line="";
		try{
			int o=0,u=0;
			int i=0;
		
			File fileU = new File(usersFile.getParent()+"\\features\\USR\\usersInfo.csv");  
		    BufferedWriter outU = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileU),"utf-8"));  
			File fileO = new File(usersFile.getParent()+"\\features\\ORG\\usersInfo.csv");  
		    BufferedWriter outO = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileO),"utf-8"));  
		    File fileFake = new File(usersFile.getParent()+"\\features\\usersFake.csv");  
		    BufferedWriter outFake = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileFake),"utf-8"));  
			File fileDeleted = new File(usersFile.getParent()+"\\features\\usersDeleted.csv");  
		    BufferedWriter outDeleted = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileDeleted),"utf-8"));  
			
		    BufferedReader reader = BF.readFile(usersFile);
			while((line=reader.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if((i/100000)%25==0)
						System.out.println(" ");
					outU.flush();
					outO.flush();
				}
				ArrayList<String> list = BF.csvParse(line);// 每行记录一个list  
				if(list.size()!=13){
    				System.out.println("\n"+list.size()+" i:"+i);
    				System.out.println(line);
    				for(String s:list){
    					System.out.println(s);
    				}
    				break;
    			}

    			Integer userid = Integer.valueOf(list.get(0));
   				String type = list.get(4);
   				Integer fake = Integer.valueOf(list.get(5));
   				if(fake==1){
   					outFake.write(line+"\n");
   					continue;
   				}
   				Integer deleted = Integer.valueOf(list.get(6));
   				if(deleted==1){
   					outDeleted.write(line+"\n");
   					continue;
   				}
				if(type.contains("USR")){
					outU.write(userid+","+list.get(1)+"\n");
				//	outU.write(line+"\n");
					u++;
				}else if(type.contains("ORG")){
					outO.write(userid+","+list.get(1)+"\n");
//					outO.write(line+"\n");
					o++;
				}else{
					System.out.println(line);
					break;
				}
				
			}
			System.out.println("All down ! "+i);
			System.out.println("USR Num is: "+u+" , ORG Num is "+o);
			outU.close();
			outO.close();
			outFake.close();
			outDeleted.close();
		}catch(Exception e){
			System.out.println(line);
			e.printStackTrace();
		}
		
	}
	public void userWithProjects(){
		System.out.println("read  "+projectsFile.getName()+", waiting......");
		String [] words;
		String line = null;
		int i=0;
		
        try{
        	Set<Integer> uidsSet = new TreeSet<Integer>();
        	BufferedReader reader = BF.readFile(projectsFile);
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
    		int count=0;
//    		File users2 = new File(path+"features\\ORG\\2usersInfo.csv");
//            PrintWriter out = new PrintWriter(
//            		new BufferedWriter(new OutputStreamWriter(new FileOutputStream(users2),"utf-8")));
//            
//            File users = new File(path+"features\\ORG\\usersInfo.csv");
//        	System.out.println("read  "+users.getName()+", waiting......");
//        	BufferedReader reader2 = BF.readFile(users,"UTF-8");
//    		i=0;

//    		while((line=reader2.readLine())!=null){
//    			i++;
//    			if(i%10000==0){
//    				System.out.print(i/10000+" ");
//    				if(i/10000%25==0){
//    					System.out.println();
//    				}
//    				out.flush();
//    			}
//    			words = line.split(",");
//    			Integer userid = Integer.valueOf(words[0]);
//    			
//    			if(uidsSet.contains(userid)){
//    				out.write(line+"\n");
//    				count++;
//    			}
//    			
//    		}
//    		System.out.println("\n 拥有项目的用户数量： "+count);
//    		out.close();
    		File usr2 = new File(path+"features\\USR\\2usersInfo.csv");
            PrintWriter out2 = new PrintWriter(
            		new BufferedWriter(new OutputStreamWriter(new FileOutputStream(usr2),"utf-8")));
            
            File usrs = new File(path+"features\\USR\\usersInfo.csv");
        	System.out.println("read  "+usrs.getName()+", waiting......");
        	BufferedReader reader3 = BF.readFile(usrs,"UTF-8");
    		i=0;
    		count=0;
    		while((line=reader3.readLine())!=null){
    			i++;
    			if(i%10000==0){
    				System.out.print(i/10000+" ");
    				if(i/10000%25==0){
    					System.out.println();
    				}
    				out2.flush();
    			}
    			words = line.split(",");
    			Integer userid = Integer.valueOf(words[0]);
    			
    			if(uidsSet.contains(userid)){
    				out2.write(line+"\n");
    				count++;
    			}
    			
    		}
    		System.out.println("\n 拥有项目的用户数量： "+count);
    		out2.close();
    		
        }catch(Exception e){
			System.out.println(i+" "+line);
			e.printStackTrace();
		}
	}
	String path = Constants.dirname+"dump\\";
	File projectsFile = new File(Constants.dirname+"dump\\projectsND.csv");
	File followersFile = new File(path+"followers.csv");
	public void USRWithFollowers(){
    	System.out.println("read  "+followersFile.getName()+", waiting......");
		String [] words;
		String line = null;
		int i=0;
		
        try{
        	Set<Integer> uidsSet = new TreeSet<Integer>();
        	BufferedReader reader = BF.readFile(followersFile);
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
    			Integer userid = Integer.valueOf(words[1]);;
    			uidsSet.add(userid);
    		}
    		System.out.println("\n number is "+uidsSet.size());
    		File users2 = new File(path+"features\\USR\\2usersInfo.csv");
            PrintWriter out = new PrintWriter(
            		new BufferedWriter(new OutputStreamWriter(new FileOutputStream(users2),"utf-8")));
            
            File users = new File(path+"features\\USR\\usersInfo.csv");
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
    		System.out.println("\n 拥有Followers的USR用户数量： "+count);
    		out.close();
    		
        }catch(Exception e){
			System.out.println(i+" "+line);
			e.printStackTrace();
		}
	}
	public void usersYear(){//
		String line="";
	//	File file = new File(Constants+"tables\\users.csv");
    	System.out.println("read  "+usersFile+", waiting......");
        try{
        	DecimalFormat doubleDF = new DecimalFormat("######0.00");   
    		Date thisTime = Constants.df.parse(Constants.currentTime);
    		long t1 = thisTime.getTime();
        	File filename = new File(usersFile.getParent()+"\\features\\userYear.csv");
            PrintWriter out = new PrintWriter(new BufferedWriter(
            		new OutputStreamWriter(new FileOutputStream(filename),"utf-8")));
        	BufferedReader reader = new BufferedReader(new FileReader(usersFile),5*1024*1024);//如果是读大文件  则    即，设置缓存
    		int i=0,count=0;
    		line=reader.readLine();
    		while((line=reader.readLine())!=null){
    			
    			i++;
    			if(i%100000==0){
    				System.out.print(i/100000+" ");
    				if(i/100000%25==0){
    					System.out.println();
    				}
    			}
    			ArrayList<String> list = BF.csvParse(line);// 每行记录一个list  
//    			String tempstr = "";
//
//    			for(int j=0;j<words.length;j++){
//    				tempstr += words[j];
//    				/*
//    				 * 两种情况，
//    				 * 一种为字符串，含引号，且引号开头，引号结尾
//    				 * 		
//    				 * 另一种情况，不含引号，为数字或\N
//    				 */
//    		//		System.out.println(tempstr);
//    				if(tempstr.contains("\"")){//包含引号
//    					if(tempstr.startsWith("\"")){//引号开头
//    				//		System.out.println("引号开头");
//    						if(tempstr.endsWith("\"")&&tempstr.length()>1){//结尾也为引号
//    				//			System.out.println("结尾为引号");
//								if(tempstr.endsWith("\\\"")){//结尾的引号不是转义字符加引号，即为真实的引号
//					//				System.out.println("结尾引号可能为转义引号");
//									//结尾引号可能为转义引号
//									if(tempstr.indexOf("\\\\\"")!=-1){//结尾为\\"这种形式，即转义的是\\
//										list.add(tempstr);
//										tempstr = "";
//										
//									}else if(BF.countZi(tempstr,"\\\"")%2!=0){//含有单数个\"
//										list.add(tempstr);
//										tempstr = "";
//										continue;
//									}else{
//										tempstr +=",";
//		    							continue;
//									}
//									
//									
//								}else{
//									list.add(tempstr);
//									tempstr = "";
//									continue;
//								}
//	    					}else{//引号开头，非引号结尾，连接下一个子串
//    							tempstr +=",";
//    							continue;
//	    					}
//    				}
//    			}else{//不含引号
//					if(tempstr.equals("\\N")){
//						list.add(tempstr);
//						tempstr = "";
//					}else{
//						list.add(tempstr);
//						tempstr = "";
//					}
//				}
//    		}
//    			
    			if(list.size()!=13){
    				System.out.println("\n"+list.size()+" i:"+i);
    				System.out.println(line);
    				for(String s:list){
    					System.out.println(s);
    				}
    				break;
    			}

    			Integer userid = Integer.valueOf(list.get(0));
   				String Stime = list.get(3);
   				Stime = Stime.substring(1,Stime.length()-1);
   				Date dd = Constants.df.parse(Stime);
				long t2 = dd.getTime();
				long time = (t1-t2)/1000;
				double Dyear = (double)time/(60*60*24*365);
//				int year = d.getYear()+1900;
//				int month = d.getMonth()+1;
				//	System.out.println(year+" "+month+" ");
				out.write(userid+","+(doubleDF.format(Dyear))+"\n");
    		}
    	out.flush();
    	out.close();
    	
    	System.out.println("\n 用户数量为："+i);
        }catch(Exception e){
			System.out.println(line);
			e.printStackTrace();
		}
	}
	
	public void getUserInfo(){
		String line="";
    	System.out.println("read  "+usersFile+", waiting......");
        try{
        	File filename = new File(usersFile.getParent()+"\\features\\userInfo.csv");
            PrintWriter out = new PrintWriter(new BufferedWriter(
            		new OutputStreamWriter(new FileOutputStream(filename),"utf-8")));
            out.write("id,login\n");
        	BufferedReader reader = new BufferedReader(new FileReader(usersFile),5*1024*1024);//如果是读大文件  则    即，设置缓存
    		int i=0;
    		while((line=reader.readLine())!=null){
    			i++;
    			if(i%100000==0){
    				System.out.print(i/100000+" ");
    				if(i/100000%25==0){
    					System.out.println();
    				}
    			}
    			ArrayList<String> list = BF.csvParse(line);// 每行记录一个list  
    			if(list.size()!=13){
    				System.out.println("\n"+list.size()+" i:"+i);
    				System.out.println(line);
    				for(String s:list){
    					System.out.println(s);
    				}
    				break;
    			}
    			String type = list.get(4);
    			if(type.contains("USR")){//普通用户为1
    				out.write(list.get(0)+","+list.get(1)+",1\n");
    			}else{//组织用户为0
    				out.write(list.get(0)+","+list.get(1)+",0\n");
    			}
				
    		}
    	out.flush();
    	out.close();
    	
    	System.out.println("\n 用户数量为："+i);
        }catch(Exception e){
			System.out.println(line);
			e.printStackTrace();
		}
	}
	public void selectID(int userid){
		try{
			String line = "";
			int i=0;
	  //  	File usersFP = new File(Constants.screenFile+"tables\\users.csv");
	    	System.out.println("read  "+usersFile.getName()+", waiting......");
	    	BufferedReader reader = BF.readFile(usersFile,"utf8");
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
				
				if(userid == id){
					System.out.println();
					System.out.println(line);
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void selectLogin(String login){
		try{
			String line = "";
			int i=0;
	    	File usersFP = new File(Constants.dirname+"dump\\users.csv");
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
				String lin = words[1].substring(1, words[1].length()-1);
				if(lin.equals(login)){
					System.out.println();
					System.out.println(line);
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Users user = new Users();
//		user.preproUsers();
	//	user.getUserInfo();
	//	user.usersYear();
	//	user.usersToType();
	//	user.userWithProjects();
	//	user.USRWithFollowers();
	//	user.selectID(1954);
		user.selectLogin("wangshans");
	}

}
