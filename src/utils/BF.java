package utils;

import java.beans.Statement;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;


public class BF{
	public static BufferedReader readFile(String file){
		BufferedReader reader = null;
	//	System.out.println("read file from "+file);
		try{
			BufferedInputStream fis = new BufferedInputStream(new FileInputStream(new File(file)));
			reader = new BufferedReader(new InputStreamReader(fis,"utf-8"),5*1024*1024);		
		}catch(Exception e){
			e.printStackTrace();
		}
		return reader;
	}
	public static BufferedReader readFile(File file){
		BufferedReader reader = null;
	//	System.out.println("read file from "+file.getPath());
		try{
			BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file));
			reader = new BufferedReader(new InputStreamReader(fis,"utf-8"),5*1024*1024);		
		}catch(Exception e){
			e.printStackTrace();
		}
		return reader;
	}
	public static BufferedReader readFile(String filePath,String format){
	//	System.out.println("read file from "+filePath);
		try{
			if(format==null){
				format = "utf-8";
			}
			BufferedInputStream fis = new BufferedInputStream(new FileInputStream(filePath));
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis,format),5*1024*1024);
			return reader;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public static BufferedReader readFile(File file,String format){
	//	System.out.println("read file from "+file.getPath());
		try{
			if(format==null){
				format = "utf-8";
			}
			BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file));
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis,format),5*1024*1024);
			return reader;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public void writeFile(String filename){
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter(filename));
			out.write(" ");
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void writeFile(String s,String filename,boolean b){
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter(filename,b));
			out.write(s+"\n");
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void Log(String s){
		writeFile(s,Constants.logFile,true);
	}
	public static void PrintAndWrite(String s){
		System.out.println(s);
		writeFile(s,Constants.logFile,true);
	}
	public static void PrintAndWrite(String s,String file){
		System.out.println(s);
		writeFile(s,file,true);
	}
	public static void PrintAndWrite(String s,String file,boolean b){
		System.out.println(s);
		writeFile(s,file,b);
	}
	public static void PrintMap(Map m){
		Iterator<String> temp_init = m.keySet().iterator();
	    while(temp_init.hasNext()){
			String key = (String)temp_init.next();
			String value = (String)m.get(key);
			System.out.print(key+"-"+value+" ");
		}
	}
	public static boolean is_Number(String str){
		 for (int i = str.length();--i>=0;){
			 if (!Character.isDigit(str.charAt(i))){
			    return false;
			 }
		}
		return true;
	}
	public void sort_file(String file){
		try{
			BufferedReader reader = readFile(file);
			String line =null;
			String[] words;
			while((line = reader.readLine())!=null){
				
			}
		}catch(Exception e){
			
		}
	}
	public void DBset(){
		try{
			String connectStr = "jdbc:mysql://localhost:3306/ghtorrent";  
	        connectStr += "?useServerPrepStmts=false&rewriteBatchedStatements=true";  
	        String insert_sql = "INSERT INTO ghtorrent.userPR(user_id,user_pr) VALUES (?,?)"; 
	        System.out.println(insert_sql);
	        String charset = "gbk";  
	        boolean debug = true;  
	        String username = "root";  
	        String password = "123456";  
			Class.forName("com.mysql.jdbc.Driver");  
        	Connection conn = DriverManager.getConnection(connectStr, username,password);  
 	        conn.setAutoCommit(false); // 璁剧疆鎵嬪姩鎻愪氦  
 	        int count = 0;  
 	        PreparedStatement psts = conn.prepareStatement(insert_sql);
 	        
// 	        psts.setInt(1, Integer.valueOf(infos[0]));
//	        psts.setInt(2, Integer.valueOf(infos[1]));
	        psts.addBatch();          // 鍔犲叆鎵归噺澶勭悊  
	        
	        psts.executeBatch(); // 鎵ц鎵归噺澶勭悊  
 	        conn.commit();  // 鎻愪氦  
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
	public boolean copyMap(){
		
		return true;
	}
	public static Connection conn() throws ClassNotFoundException, SQLException{
		String connectStr = "jdbc:mysql://localhost:3306/github";  
        connectStr += "?useServerPrepStmts=false&rewriteBatchedStatements=true";  
        String username = "root";  
        String password = "123456";  
		Class.forName("com.mysql.jdbc.Driver");  
    	Connection conn = DriverManager.getConnection(connectStr, username,password); 
    	return conn;
	}
	public static int countChar(String str, char c, int start) {  //鏁板瓧绗︿覆str涓璫鐨勬暟閲�
        int i = 0;  
        int index = str.indexOf(c, start);  
        return index == -1 ? i : countChar(str, c, index + 1) + 1;  
    } 
	public static int getLinesNum(String filepath){//有Followers的用户
		String line = null;
		int i=0;
        try{
        	BufferedReader reader = readFile(filepath);
    		
    		while((line=reader.readLine())!=null){
    			i++;
    		}
    		reader.close();
    		}
    		catch(Exception e){
			System.out.println(line);
			e.printStackTrace();
		}
    	return i;
	}
	public static int countZi(String str,String k){
		int count=0;
		int n = str.indexOf(k);
		while(n!=-1){
			count++;
			str = str.substring(n+k.length());
			n = str.indexOf(k);
		}
	    return count; 
	}
	public static ArrayList<String> csvParse(String line){
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
			if(tempstr.contains("\"")){//包含引号
				if(tempstr.startsWith("\"")){//引号开头
					if(tempstr.endsWith("\"")&&tempstr.length()>1){//结尾也为引号
						if(tempstr.endsWith("\\\"")){//结尾的引号不是转义字符加引号，即为真实的引号
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
		return list;
	}
}
