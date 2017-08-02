package mydata;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import utils.BF;
import utils.Constants;

public class ReadtoDatabase{
	public static final String filename="D:\\mysql-2015-09-25\\mydum\\followers.txt";
	//public static final String filename="D:\\mysql-2015-09-25\\dump\\followers.txt";
	
//连接到数据库；
	Connection conn;
	Statement stmt;
	String DRIVER = "com.mysql.jdbc.Driver";
	String url = "jdbc:mysql://localhost/github";
	String username = "root";
	String password = "123456";
	public void connect(){
		try{
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(url,username,password);
			if(!conn.isClosed()){
			System.out.println("Succeed connecting to the Database!");	
			}
			else
				System.out.println("Fail connecting to the Database!");
			stmt = conn.createStatement();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void usersToDB(){
		connect();
		try{
			String line = "";
			File usersFiles = new File("");
			for(File f:usersFiles.listFiles()){
				BufferedReader read = BF.readFile(f);
				while((line=read.readLine())!=null){
					String[] words=line.split(",");
					Integer id = Integer.valueOf(words[0]);
					String login = words[1];
					String name=null;
					String company = null;
					String location = null;
					String email = null;
					String created_at=null;
					String type = null;
					String fake = null;
					String sql1 = "insert into ghtorrent.followers values('"+id+"','"+login+"','"+name+"')"; 
					stmt.executeUpdate(sql1);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally 
		{//释放数据库的资源
			  try {
				  if (stmt != null)
					  stmt.close();
				  if(conn != null && !conn.isClosed())
				  {
					  conn.close();
				  }
			  }catch(Exception e){
					e.printStackTrace();
				}
		}
	}
		
	public void followersToDB(){
		connect();
		try{
			String line = "";
			File usersFiles = new File("D:\\github\\mysql-2015-09-25\\splited_followers");
			for(File f:usersFiles.listFiles()){
				BufferedReader read = BF.readFile(f);
				System.out.println(f.getName());
				while((line=read.readLine())!=null){
					String[] words=line.split(",");
					Integer u_id = Integer.valueOf(words[0]);
					Integer f_id = Integer.valueOf(words[1]);
					String ts = words[2];
				//	System.out.println(ts);	
					String sql1 = "insert into github.followers values('"+u_id+"','"+f_id+"','"+ts+"')"; 
					stmt.executeUpdate(sql1);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally 
		{//释放数据库的资源
			  try {
				  if (stmt != null)
					  stmt.close();
				  if(conn != null && !conn.isClosed())
				  {
					  conn.close();
				  }
			  }catch(Exception e){
					e.printStackTrace();
				}
		}
		System.out.println("Succeed import into database!");
	}
	public void splitedToDB3(String table){
		connect();
		try{
			String line = "";
			File usersFiles = new File("D:\\github\\mysql-2015-09-25\\splited_"+table);
			for(File f:usersFiles.listFiles()){
				BufferedReader read = BF.readFile(f);
				System.out.println(f.getName());
				while((line=read.readLine())!=null){
					String[] words=line.split(",");
					Integer u_id = Integer.valueOf(words[0]);
					Integer f_id = Integer.valueOf(words[1]);
					String ts = words[2];
				//	System.out.println(ts);	
					String sql1 = "insert into github."+table+" values('"+u_id+"','"+f_id+"','"+ts+"')"; 
					stmt.executeUpdate(sql1);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally 
		{//释放数据库的资源
			  try {
				  if (stmt != null)
					  stmt.close();
				  if(conn != null && !conn.isClosed())
				  {
					  conn.close();
				  }
			  }catch(Exception e){
					e.printStackTrace();
				}
		}
		System.out.println("Succeed import into database!");
	}
	public void splitedToDB(String table){
		connect();
		try{
			String line = "";
			File usersFiles = new File("D:\\github\\mysql-2015-09-25\\splited_"+table);
			for(File f:usersFiles.listFiles()){
				BufferedReader read = BF.readFile(f);
				System.out.println(f.getName());
				while((line=read.readLine())!=null){
					String[] words=line.split(",");
					Integer u_id = Integer.valueOf(words[0]);
					Integer f_id = Integer.valueOf(words[1]);
					String ts = words[2];
				//	System.out.println(ts);	
					String sql1 = "insert into github."+table+" values('"+u_id+"','"+f_id+"','"+ts+"')"; 
					stmt.executeUpdate(sql1);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally 
		{//释放数据库的资源
			  try {
				  if (stmt != null)
					  stmt.close();
				  if(conn != null && !conn.isClosed())
				  {
					  conn.close();
				  }
			  }catch(Exception e){
					e.printStackTrace();
				}
		}
		System.out.println("Succeed import into database!");
	}
	public void txtToDB(String table){
		connect();
		try{
			String line = null;
			File f = new File("F:\\myeclipse\\workspace\\github\\Data\\Feature\\"+table+".txt");
			BufferedReader read = BF.readFile(f);
			System.out.println(f.getName());
			while((line=read.readLine())!=null){
				String[] words=line.split(",");
				Integer u_id = Integer.valueOf(words[0]);
				Integer f_id = Integer.valueOf(words[1]);
			//	String ts = words[2];
			//	System.out.println(ts);	
				String sql1 = "insert into github."+table+" values('"+u_id+"','"+f_id+"')"; 
			//	System.out.println(sql1);	
				stmt.executeUpdate(sql1);
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally 
		{//释放数据库的资源
			  try {
				  if (stmt != null)
					  stmt.close();
				  if(conn != null && !conn.isClosed())
				  {
					  conn.close();
				  }
			  }catch(Exception e){
					e.printStackTrace();
				}
		}
		System.out.println("Succeed import into database!");
	}
	public static void main(String[] args) {
		ReadtoDatabase instance = new ReadtoDatabase();
		
		long start = System.currentTimeMillis();
		instance.txtToDB("RepowatchersNum");
		instance.splitedToDB3("watchers");
		instance.followersToDB();
		
		long end = System.currentTimeMillis();
		System.out.println("计算用时="+(end-start)/1000);
	}

}
