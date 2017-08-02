package wss.githubFeatures;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.TreeMap;

import com.csvreader.CsvReader;

import utils.BF;

public class ToDB {

	/**
	 * @param args
	 */
	public void TabletoDB(String tableName) throws IOException{
		try{
			TreeMap<Integer,Integer> RepowatchersNum = new TreeMap<Integer,Integer>();
			String line = "";
			File table = new File("E:\\mysql-2015-09-25\\dump\\"+tableName+".csv");
			Connection conn = BF.conn();
		    conn.setAutoCommit(false); // 设置手动提交  
		    int count = 0;  
		    String sqlstr = "create table "+tableName+
		    				"(repo_id int(255),user_id int(255),created_at timestamp);";
		    //获取对象
	        Statement stmt = conn.createStatement();  
	        stmt.executeUpdate(sqlstr);
	        System.out.println("创建成功！");
		    String insert_sql = "INSERT INTO github."+tableName+
		    				"VALUES (?,?,?)"; 
	        System.out.println(insert_sql);
		    PreparedStatement psts = conn.prepareStatement(insert_sql);
			System.out.println(table);
			BufferedReader r1 = BF.readFile(table);
			while((line = r1.readLine())!=null){
				count++;
				String[] words = line.split(",");
				int rid = Integer.valueOf(words[0]);
				int uid = Integer.valueOf(words[1]);
				Timestamp x = Timestamp.valueOf(words[2].substring(1, words[2].length()-1));
				psts.setInt(1, Integer.valueOf(words[0]));
			    psts.setInt(2, Integer.valueOf(words[1]));
			    psts.setTimestamp(3, x);
			    psts.addBatch();          // 加入批量处理  
			    if(count%10000 ==0){
			       psts.executeBatch(); // 执行批量处理  
			 	   conn.commit();  // 提交  
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
	
	public void UserstoDB(){
    	//将结果插入数据库
		String line = "";
		try{
			int i=0;
			BufferedWriter out = new BufferedWriter(
					new FileWriter("E:\\mysql-2015-09-25\\preproed\\usersFP2.txt",true));
			
			Connection conn = BF.conn();
		    conn.setAutoCommit(false); // 设置手动提交  
		    int count = 0;  
		    String sql1 = "drop table github.users2";
		    Statement stmt = conn.createStatement();   //获取对象
		    stmt.executeUpdate(sql1);
		    System.out.println("删除表！");
		    String sqlstr = "create table users2(id int(255)  primary key not null," +
		    		"login varchar(2000)," +
		    		"name varchar(2000),"+
		    		"company varchar(2000),"+
		    		"location varchar(2000),"+
		    		"email varchar(2000),"+
		    		"created_at timestamp,"+
		    		"type varchar(255),"+
		    		"fake int(2),"+
		    		"deleted int(2));";
		    System.out.println(sqlstr);
	    //    Statement stmt = conn.createStatement();   //获取对象
	        stmt.executeUpdate(sqlstr);
	        System.out.println("创建成功！");
		    String insert_sql = "INSERT INTO github.users2 VALUES (?,?,?,?,?,?,?,?,?,?)"; 
	        System.out.println(insert_sql);
		    PreparedStatement psts = conn.prepareStatement(insert_sql);
			String usersFile = "E:\\mysql-2015-09-25\\preproed\\users.csv";
			System.out.println(usersFile);
	//		BufferedReader r1 = BF.readFile(usersFile);
			CsvReader csvReader = new CsvReader(usersFile);

	        // 读表头
	        //csvReader.readHeaders();
	        while (csvReader.readRecord()){
	           // 读一整行
	      //     System.out.println(csvReader.getRawRecord());
	         line = csvReader.getRawRecord();
	         if(csvReader.getColumnCount()==10){
        	 	count++;
//        	 	for(int j=0;j<10;j++){
//        	 		System.out.println(j+" "+csvReader.get(j));
//        	 	}
//        	 	break;
        	 	int id = Integer.valueOf(csvReader.get(0));
//        	 	int errorId = 8843261;//31598,863478,1427287,1784485,2369430,3420087,3807987,6309914,6331910,6363826
// //6395202,6405570,6415778,6615173,7382408,7413928,7888241，8374774,8843261
//        	 	if(id<errorId)
//        	 		continue;
//        	 	if(id==errorId){
//        	 		out.write(line+"\n");
//        	 		out.flush();
//        	 		continue;
//        	 	}
				Timestamp x = Timestamp.valueOf(csvReader.get(6));
				psts.setInt(1, Integer.valueOf(csvReader.get(0)));
			    psts.setString(2, csvReader.get(1));
			    psts.setString(3, csvReader.get(2));
			    psts.setString(4, csvReader.get(3));
			    psts.setString(5, csvReader.get(4));
			    psts.setString(6, csvReader.get(5));
			    psts.setTimestamp(7, x);
			    psts.setString(8, csvReader.get(7));
			    psts.setInt(9, Integer.valueOf(csvReader.get(8)));
			    psts.setInt(10, Integer.valueOf(csvReader.get(9)));
			    
			    psts.addBatch();          // 加入批量处理  
			    if(count%10000 ==0){
			    	System.out.print(count+" ");
			    	if(count/10000%25==0)
			    		System.out.println();
			    	
			       psts.executeBatch(); // 执行批量处理  
			 	   conn.commit();  // 提交  
			    }
	        	//   continue;
           }else{
        	   i++;
        	   int k=0;
        	   out.write(line+"\n");
        	   out.flush();
           }	  
			}
			psts.executeBatch(); // 最后不足1万条的数据  
			conn.commit();  // 提交  
			System.out.println();
		    System.out.println("All down : " + count);  
	        conn.close(); 
	        out.close();
	        System.out.println(i);
	    }catch(Exception e){
	    	System.out.println(line);
	    	e.printStackTrace();
	    }    
	 
	}
	private int countChar(String str, char c, int start) {  //数字符串str中c的数量
        int i = 0;  
        int index = str.indexOf(c, start);  
        return index == -1 ? i : countChar(str, c, index + 1) + 1;  
    } 
	public void otherUserstoDB(){
    	//将结果插入数据库
		String line = "";
		try{
			int i=0;
			BufferedWriter out = new BufferedWriter(
					new FileWriter("E:\\mysql-2015-09-25\\dump\\otherUsers2.txt",true));
			
			Connection conn = BF.conn();
		    conn.setAutoCommit(false); // 设置手动提交  
		    int count = 0;  

		    Statement stmt = conn.createStatement();   //获取对象
//		    String sql1 = "drop table github.otherusers2";
//		    stmt.executeUpdate(sql1);
//		    System.out.println("删除表！");
//		    String sqlstr = "create table github.otherusers2(id int(255)," +
//		    		"login varchar(2000)," +
//		    		"name varchar(2000),"+
//		    		"company varchar(10000),"+
//		    		"location varchar(2000),"+
//		    		"email varchar(2000),"+
//		    		"created_at timestamp,"+
//		    		"type varchar(255),"+
//		    		"fake int(2),"+
//		    		"deleted int(2));";
//		    System.out.println(sqlstr);
//	   //     Statement stmt = conn.createStatement();   //获取对象
//	        stmt.executeUpdate(sqlstr);
//	        System.out.println("创建成功！");
		    String insert_sql = "INSERT INTO github.users2 VALUES (?,?,?,?,?,?,?,?,?,?)"; 
	        System.out.println(insert_sql);
		    PreparedStatement psts = conn.prepareStatement(insert_sql);
			String usersFile = "E:\\mysql-2015-09-25\\preproed\\usersFP2.txt";
			System.out.println(usersFile);
	//		BufferedReader r1 = BF.readFile(usersFile);
			CsvReader csvReader = new CsvReader(usersFile);
	        while (csvReader.readRecord()){
	           // 读一整行
	        	ArrayList<String> temps = new ArrayList<String>();
	        	line = csvReader.getRawRecord();
	        	System.out.println("...."+line);
	        //	System.out.println(csvReader.getColumnCount());
	        	String[] words = line.split(",");
	        	System.out.println(words.length);
	        	int id = Integer.valueOf(words[0]);
	        	if(id<9079605){
	        		continue;
	        	}
	        	temps.add(words[0]);//第一个为ID不会出错
	        	String s  = "";
	        	for(int q=1;q<words.length;q++){
	        		s += words[q];
	        		if(s.startsWith("\"")){
	        			if(s.contains("\\\"")){
		        			s = s.replace("\\\"", "\"\"");
		        		}
		        		int t = countChar(s, '"', 0);
		                if(t% 2 == 1){// 如果双引号是奇数  ，则不是完整的字符串
		                	continue;
		                }  
		                if(s.endsWith("\"")){
		                	s = s.substring(1, s.length()-1);
	                		temps.add(s);
	                		s = "";
	                	}
	        		}else{
	        			temps.add(s);
                		s="";
	        		}
	        }
	        System.out.println("temps "+temps.size());	
				Timestamp x = Timestamp.valueOf(temps.get(6));
				psts.setInt(1, Integer.valueOf(temps.get(0)));
			    psts.setString(2, temps.get(1));
			    psts.setString(3, temps.get(2));
			    psts.setString(4, temps.get(3));
			    psts.setString(5, temps.get(4));
			    psts.setString(6, temps.get(5));
			    psts.setTimestamp(7, x);
			    psts.setString(8, temps.get(7));
			    psts.setInt(9, Integer.valueOf(temps.get(8)));
			    psts.setInt(10, Integer.valueOf(temps.get(9)));
			    
			    psts.addBatch();          // 加入批量处理  
			    psts.executeBatch(); // 执行批量处理  
			 	conn.commit();  // 提交   
			}
	        conn.close(); 
	        System.out.println(i);
	    }catch(Exception e){
	    	System.out.println(line);
	    	e.printStackTrace();
	    }    
	 
		}
	public void UserFeaturestoDB(){
    	//将结果插入数据库
		String line = "";
		try{
			int i=0;
			BufferedWriter out = new BufferedWriter(
					new FileWriter("E:\\mysql-2015-09-25\\screen\\userFeatures.csv",true));
			
			Connection conn = BF.conn();
		    conn.setAutoCommit(false); // 设置手动提交  
		    int count = 0;  
		    Statement stmt = conn.createStatement();   //获取对象
		    String sql1 = "drop table github.userFeatures";
		    stmt.executeUpdate(sql1);
		    System.out.println("删除表！");
		    String sqlstr= "create table github.userFeatures( id int(255)  primary key not null," +
		    		"CommitsNum int(255)," +
		    		"FollowersNum int(255)," +
		    		"FollowingsNum int(255)," +
		    		"ForkReposNum int(255)," +
		    		"IssuesNum int(255)," +
		    		"MergedPull_RequestsNum int(255)," +
		    		"OriginalReposNum int(255)," +
		    		"Pull_RequestsMergedFreq double," +
		    		"Pull_RequestsNum int(255)," +
		    		"ReposNum int(255)," +
		    		"ForksAveNum double," +
		    		"ForksSumNum int(255)," +
		    		"WatchersAveNum double," +
		    		"WatchersSumNum int(255)," +
		    		"Year double);";
		    System.out.println(sqlstr);
	    //    Statement stmt = conn.createStatement();   //获取对象
	        stmt.executeUpdate(sqlstr);
	        System.out.println("创建成功！");
		    String insert_sql = "INSERT INTO github.userFeatures VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; 
	        System.out.println(insert_sql);
		    PreparedStatement psts = conn.prepareStatement(insert_sql);
			String usersFile = "E:\\mysql-2015-09-25\\screen\\features\\UserFeatures.csv";
			BufferedReader r1 = BF.readFile(usersFile,"utf-8");
	        
		
			line=r1.readLine();// 读表头
	        while((line=r1.readLine())!=null){
	        //	System.out.println("line:"+line);
	           // 读一整行
	        	String[] words = line.split(",");
	        //	System.out.println(words.length);
	        
	        	if(words.length==16){
	        	 	count++;
	        	 	int id = Integer.valueOf(words[0]);
					psts.setInt(1, Integer.valueOf(words[0]));
					psts.setInt(2, Integer.valueOf(words[1]));
					psts.setInt(3, Integer.valueOf(words[2]));
					psts.setInt(4, Integer.valueOf(words[3]));
					psts.setInt(5, Integer.valueOf(words[4]));
					psts.setInt(6, Integer.valueOf(words[5]));
					psts.setInt(7, Integer.valueOf(words[6]));
					psts.setInt(8, Integer.valueOf(words[7]));
					psts.setDouble(9, Double.valueOf(words[8]));
					psts.setInt(10, Integer.valueOf(words[9]));
					psts.setInt(11, Integer.valueOf(words[10]));
					psts.setDouble(12, Double.valueOf(words[11]));
					psts.setInt(13, Integer.valueOf(words[12]));
					psts.setDouble(14, Double.valueOf(words[13]));
					psts.setInt(15, Integer.valueOf(words[14]));
					psts.setDouble(16, Double.valueOf(words[15]));
				    psts.addBatch();          // 加入批量处理  
				    if(count%10000 ==0){
				    	System.out.print(count+" ");
				    	if(count/10000%20==0)
				    		System.out.println();
				    	
				       psts.executeBatch(); // 执行批量处理  
				 	   conn.commit();  // 提交  
				    }
	        	//   continue;
	           }else{
	        	   i++;
	        	   int k=0;
	        	   out.write(line+"\n");
	        	   out.flush();
	           }	  
		     //    break;
	        }
			psts.executeBatch(); // 最后不足1万条的数据  
			conn.commit();  // 提交  
			System.out.println();
		    System.out.println("All down : " + count);  
	        conn.close(); 
	        out.close();
	        System.out.println(i);
	    }catch(Exception e){
	    	e.printStackTrace();
	    }    
	 
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ToDB t = new ToDB();
		t.UserFeaturestoDB();
	}

}
