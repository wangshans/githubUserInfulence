package mydata;

import java.io.BufferedReader;  
import java.io.FileInputStream;  
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.PreparedStatement;  
import java.sql.SQLException;  
import java.sql.Timestamp;

import utils.Constants;
//批量插入Mysql数据库
public class TxttoDB {
	
	    private String insert_sql;  
	    private String charset;  
	    private boolean debug;  
	  
	    private String connectStr;  
	    private String username;  
	    private String password;  
	  
	    
	    public void DbStoreHelper(String table) {         
	        connectStr = "jdbc:mysql://localhost:3306/ghtorrent";  
	        connectStr += "?useServerPrepStmts=false&rewriteBatchedStatements=true";  
	        insert_sql = "INSERT INTO ghtorrent.";
	        insert_sql += table;
	        //insert_sql +=" (user_id,follower_id,created_at) VALUES (?,?,?)";  //follower
	       // insert_sql +=" (id,url,owner_id,name,description,language,created_at,forked_from,deleted) values (?,?,?,?,?,?,?,?,?)";//projects
	        insert_sql +=" (repo_id,user_id,created_at) VALUES (?,?,?)"; 
	        System.out.println(insert_sql);
	        charset = "gbk";  
	        debug = true;  
	        username = "root";  
	        password = "123456";  
	    }  
	  
	    /**
		* 判断一个字符是Ascill字符还是其它字符（如汉，日，韩文字符）
	* 
		* @param c
		* @return
		*/
		public static boolean isLetter(char c) {
		int k = 0x80;
		return (c / k) == 0 ? true : false;
		}
	    
		public String[] fenli(String s){
			String[] infos = s.split(",");
	//		for(int i=0;i<infos.length;i++){
	//			System.out.println(i+" : "+infos[i]);
	//		}
			String[] new_infos = new String[9];//用来存储正确分离的字段
			int length = infos.length;
	//		System.out.println("length : "+length);
			if(length == 9){
	     		//去掉引号
	     		
	     		infos[1] = infos[1].substring(1,infos[1].length()-1);
	     		infos[3] = infos[3].substring(1,infos[3].length()-1);
	     		
	     		if(infos[4].charAt(infos[4].length()-1)=='"'){
	     			infos[4] = infos[4].substring(1,infos[4].length()-1);
	     		}
	     		else{
	     			infos[4] = infos[4].substring(1);
	     		}
	     		infos[5] = infos[5].substring(1,infos[5].length()-1);
	     		infos[6] = infos[6].substring(1,infos[6].length()-1);
	  //   		for(int i = 0;i<infos.length;i++){
	//	     		System.out.print("infos["+i+"] 是 "+infos[i]+" ; ");
	//	     	}
	     		return infos;
	     	}
			else{
				for(int i=0;i<4;i++){
				//	System.out.println(i+" : "+infos[i]);
					new_infos[i] = infos[i];
				//	System.out.println((8-i)+" : "+infos[length-1-i]);
					new_infos[8-i] = infos[length-1-i];
				}
				for(int i=4;i<length-4;i++){
					if(i==4)
						new_infos[4] = infos[i];
					else{
						new_infos[4] += infos[i];
					}
					
					if(i<length-1-4){
						new_infos[4] += ",";
					}
				//	System.out.println( 4 +" : "+new_infos[4]);
				}
				new_infos[1] = new_infos[1].substring(1,infos[1].length()-1);
				new_infos[3] = new_infos[3].substring(1,infos[3].length()-1);
	     		
	     		if(new_infos[4].charAt(new_infos[4].length()-1)=='"'){
	     			new_infos[4] = new_infos[4].substring(1,new_infos[4].length()-1);
	     		}
	     		else{
	     			new_infos[4] = new_infos[4].substring(1);
	     		}
	     		new_infos[5] = new_infos[5].substring(1,new_infos[5].length()-1);
	     		new_infos[6] = new_infos[6].substring(1,new_infos[6].length()-1);
				return new_infos;
			}
			
		}
	    public String[] douhaofenli(String s){
	    	String[] infos = s.split(","); 
	    	String[] new_infos = new String[9];//用来存储正确分离的字段
	     	
	     	int length = infos.length;
	     	int temp = 0;
	     	int flag = 0;
	     	System.out.println(length);
			System.out.println("处理前结果为： ");
	     	for(int i = 0;i<infos.length;i++){
	     		int lengt = infos[i].length();
	     		if(isLetter(infos[i].charAt(lengt-1))== false){
	     			infos[i]+="\"";
	     		}
	     		
	     		System.out.print("infos["+i+"] 是 "+infos[i]+" ; ");
	     	}
	     	System.out.println(" ");
	     	if(length == 9){
	     		//去掉引号
	     		
	     		infos[1] = infos[1].substring(1,infos[1].length()-1);
	     		infos[3] = infos[3].substring(1,infos[3].length()-1);
	     		infos[4] = infos[4].substring(1,infos[4].length()-1);
	     		infos[5] = infos[5].substring(1,infos[5].length()-1);
	     		infos[6] = infos[6].substring(1,infos[6].length()-1);
	     		for(int i = 0;i<infos.length;i++){
		     		System.out.print("infos["+i+"] 是 "+infos[i]+" ; ");
		     	}
	     		return infos;
	     	}
	     	else{
	     		try{
	     	
	     		if(length >0){
	     			for(int i=0;i<length;i++){
	         			int lo = infos[i].length();
	         			if(infos[i].contains("\"")){//包含引号
	         				if(infos[i].length()<2){//长度为1，即只有一个引号（可能是前引号，也可能是后引号）
	         					if(flag == 0){//flag = 0,表示为前引号
	         						flag = 1;//表示读到第一个引号
	         						infos[temp] = null;//去掉引号
	         					}
	         					if(flag == 1){//表示后引号
	         						flag = 0;//表示引号匹配
	         						temp++;
	         					}
	         				}
	         				else{//长度大于等于2
	         					String first = infos[i].substring(0, 1);//第一个字符
	         					String last = infos[i].substring(lo-1, lo);//最后一个字符
	         					String last_two = infos[i].substring(lo-2, lo-1);//倒数第二个字符
	         					if(first.compareTo("\"") == 0){//第一个元素为引号
	         						flag = 1;//表示读到第一个引号
	         						if(last.compareTo("\"") == 0){//最后一个元素也为引号
	         							if(last_two.compareTo("\\")==0 ){//  \",句中引号，不是结束引号
		         							new_infos[temp] += infos[i].substring(0,lo-2);//去掉 \
		         							new_infos[temp] += "\"";
		         						}else{ //结束引号
		         							new_infos[temp] = infos[i].substring(1,lo-1);
		         							temp++;
		         							flag = 0;
		         						}
	         							
	         						}else{//仅第一个元素为引号，最后一个不是引号，以逗号分开的，需要在句子里再加一个逗号
	         							flag = 1;
	         							new_infos[temp] = infos[i].substring(1)+",";
	         						}
	         					}
	         					
	         					else if(last.compareTo("\"") == 0 ){//第一个元素不是引号，最后一个元素是引号
	         						if(last_two.compareTo("\\")==0 ){
	         							new_infos[temp] += infos[i].substring(0,lo-2);
	         							new_infos[temp] += "\"";
	         						}else{
	         							if(flag == 1){
		         							new_infos[temp] += infos[i].substring(0,lo-1);
			         					}else if(flag == 0){//引号不匹配
		         							System.out.println("这时i是"+i+"Something is wrong!");
		         						}
		         						flag = 0;
		         						temp++;
	         						}
	         						
	         					}
	         					//其他地方元素为引号，不讨论
	         				}
	         			}
	         			else if(flag == 1){//不包含引号,//有前引号,则可能是引号中间的元素
	     					
	         				new_infos[temp] += infos[i];
	         				new_infos[temp] += ",";
	         			}
	         			else if(flag == 0){//不包含引号，无前引号，可能是数字
	         				new_infos[temp] = infos[i];
	         				temp++;
	         			}
		     		}
	     		}
	     		System.out.println(" ");
	     		System.out.println("处理后结果为： ");
	     		for(int i = 0;i<9;i++){
	     			System.out.print("new_infos["+i+"] 是 "+new_infos[i]+" ; ");
	     		}
	     	}catch(Exception e){
	     		e.printStackTrace();
	     	}
	     	
			return new_infos;
	     	}
	     	
		}
	 
	    
	    public void doStoredb(String myFile,String table){ //参数：文件路径；表名
	    	try{
	    		//String srcFile = Constants.dirname + "splited_" + table+"\\text" + ".txt";
	    		String srcFile = myFile;
	    		BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(srcFile), charset));  
	    		System.out.println(srcFile+" is loading...... ");
	        
	        	Class.forName("com.mysql.jdbc.Driver");  
	        	Connection conn = DriverManager.getConnection(connectStr, username,password);  
	 	        conn.setAutoCommit(false); // 设置手动提交  
	 	        int count = 0;  
	 	        PreparedStatement psts = conn.prepareStatement(insert_sql);  
	 	        
	 	        String line = null;  
	 	        String splitedTime;
	 	     //插入Followers
 	        	if(table.compareTo("followers")==0 || table.compareTo("watchers")==0){
 	        		while (null != (line = bfr.readLine())) { 
	 	        		String[] infos = line.split(","); 
	 	        	
	 	        		if (infos.length < 3)   continue;  
	 	 	           /* if (debug) {  
	 	 	                System.out.println(line);  
	 	 	            }  
	 	 	            */
	 	 	            
	 	 	            psts.setInt(1, Integer.valueOf(infos[0]));
	 	 	            psts.setInt(2, Integer.valueOf(infos[1]));
	 	 	         
	 	 			//	splitedTime = infos[2].substring(1, infos[2].length()-1);
	 	 				Timestamp times = Timestamp.valueOf(infos[2]);
	 	 	            psts.setTimestamp(3, times);
	 	 	            psts.addBatch();          // 加入批量处理
	 	 	            count++; 
	 	        	}
 	        	}
	 	            //插入projects
	 	        if(table.compareTo("projects") == 0){
	 	        	while (null != (line = bfr.readLine())) {
	 	        		String[] infos = new String[9];
		 	            infos = fenli(line);
		 	            psts.setInt(1, Integer.valueOf(infos[0]));//id
		 	 	        psts.setString(2, infos[1]);//url
		 	 	        psts.setInt(3, Integer.valueOf(infos[2]));//ower_id
		 	 	        psts.setString(4, infos[3]);//name
		 	 	        psts.setString(5, infos[4]);//description
		 	 	        psts.setString(6, infos[5]);//language
		 	 	        Timestamp times = Timestamp.valueOf(infos[6]);
		 	 	        psts.setTimestamp(7, times);//created_at
		 	 	        if(infos[7].contains("N")){
		 	 	            infos[7] = "0";
		 	 	        }
		 	 	        psts.setInt(8, Integer.valueOf(infos[7]));//forked_from
		 	 	        psts.setInt(9, Integer.valueOf(infos[8]));//deleted
		 	 	        psts.addBatch();          // 加入批量处理  
			 	        count++; 
			 	     }
	 	        }  
	 	        
	 	    
	 	        psts.executeBatch(); // 执行批量处理  
	 	        conn.commit();  // 提交  
	 	       System.out.println("All down : " + count);  
		        conn.close(); 
	        }catch(ClassNotFoundException e){
	        	e.printStackTrace();
	        }catch(SQLException e){
	        	e.printStackTrace();
	        }catch(IOException e){
	        	e.printStackTrace();
	        }catch(Exception e){
	        	e.printStackTrace();
	        }           
	    }  
	    
} 
	    
/*    public void storeToDb(String srcFile) throws IOException {  
BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(srcFile), charset));  
System.out.println(srcFile+" is loading...... ");
try {  
    doStore(bfr);  
} catch (Exception e) {  
    e.printStackTrace();  
} finally {  
    bfr.close();  
}  
}  
*/
/*	    
	    private void doStore_followers(BufferedReader bfr) throws ClassNotFoundException, SQLException, IOException {  
	        Class.forName("com.mysql.jdbc.Driver");  
	        Connection conn = DriverManager.getConnection(connectStr, username,password);  
	        conn.setAutoCommit(false); // 设置手动提交  
	        int count = 0;  
	        PreparedStatement psts = conn.prepareStatement(insert_sql);  
	        
	        String line = null;  
	        String splitedTime;
	        while (null != (line = bfr.readLine())) {  
	            String[] infos = line.split(",");  
	            if (infos.length < 3)   continue;  
	           // if (debug) {  
	         //       System.out.println(line);  
	        //    }  
	            
	            
	            psts.setInt(1, Integer.valueOf(infos[0]));
	            psts.setInt(2, Integer.valueOf(infos[1]));
	         
				splitedTime = infos[2].substring(1, infos[2].length()-1);
				Timestamp times = Timestamp.valueOf(splitedTime);
	            psts.setTimestamp(3, times);
	              
	            psts.addBatch();          // 加入批量处理  
	            count++;              
	        }  
	        psts.executeBatch(); // 执行批量处理  
	        conn.commit();  // 提交  
	        System.out.println("All down : " + count);  
	        conn.close();  
	    }  
	    private void doStore_projects(BufferedReader bfr) throws ClassNotFoundException, SQLException, IOException {  
	        Class.forName("com.mysql.jdbc.Driver");  
	        Connection conn = DriverManager.getConnection(connectStr, username,password);  
	        conn.setAutoCommit(false); // 设置手动提交  
	        int count = 0;  
	        PreparedStatement psts = conn.prepareStatement(insert_sql);  
	        
	        String line = null;  
	        String splitedTime;
	        while (null != (line = bfr.readLine())) {  
	            String[] infos = line.split(",");  
	            if (infos.length < 9)   continue;  
	          // if (debug) {  
	         //       System.out.println(line);  
	         //   }  
	            
	            
	            psts.setInt(1, Integer.valueOf(infos[0]));//id
	            psts.setString(2, infos[1]);//url
	            psts.setInt(3, Integer.valueOf(infos[1]));//ower_id
	            psts.setString(4, infos[1]);//name
	            psts.setString(5, infos[1]);//description
	            psts.setString(6, infos[1]);//language
	            
				splitedTime = infos[2].substring(1, infos[2].length()-1);
				Timestamp times = Timestamp.valueOf(splitedTime);
	            psts.setTimestamp(7, times);//created_at
	           
	            psts.setInt(8, Integer.valueOf(infos[1]));//forked_from
	            psts.setInt(9, Integer.valueOf(infos[1]));//deleted
	            
	            psts.addBatch();          // 加入批量处理  
	            count++;              
	        }  
	        psts.executeBatch(); // 执行批量处理  
	        conn.commit();  // 提交  
	        System.out.println("All down : " + count);  
	        conn.close();  
	    }  

*/

