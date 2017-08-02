package mydata;

import java.sql.Connection;
import java.sql.DriverManager;




public class ConnectMysql {
	String driver = "com.mysql.jdbc.Driver";
	String url = "jdbc:mysql://localhost/ghtorrent150925";
	String user = "root";
	String password = "123456";
	
	public Connection con_mysql(){
		try{
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url,user,password);
				if(!conn.isClosed()){
				System.out.println("Succeed connecting to the Database!");
				return conn;
			}
			else 
				return null;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args){
		ConnectMysql c = new ConnectMysql();
		c.con_mysql();
	}
}
