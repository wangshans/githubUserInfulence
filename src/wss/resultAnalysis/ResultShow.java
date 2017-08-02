package wss.resultAnalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import utils.BF;
import utils.Constants;

public class ResultShow {

	/**
	 * @param args
	 */
	public void addUserUrl(){
		try{
			String line = "";
			int i=0;
			File  f = new File(Constants.screenFile+"table.csv");
			BufferedWriter out = new BufferedWriter(new FileWriter
					(f.getParent()+"\\tableWithURL.csv",true));
			
			BufferedReader read = BF.readFile(f);
		//	line = read.readLine();
			
//			out.write(line+",url\n");
		//	out.write("rank,login,"++",url\n");
			while((line=read.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if((i/100000)%25==0)
						System.out.println(" ");
				}
				String[] words = line.split(",");
				if(words.length!=4)
				{
					System.out.println(line);
					break;
				}
				
				String id = words[0];
				String login = words[1];
				String n = words[2];
				String rank = words[3];
				String url = "https://github.com/"+login;
				if(id.contains("id")){
					url = "url";
				}
				out.write(rank+","+login+","+n+","+url+","+id+"\n");
			//	System.out.print(uid+" - ");
 			}
			out.close();
			System.out.println(i+" ");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void UsertoUrl(File file){
		try{
			String line = "";
			int i=0;
	//		File  f = new File(Constants.screenFile+"table.csv");
			BufferedWriter out = new BufferedWriter(new FileWriter
					(file.getParent()+"\\2"+file.getName()));
			
			BufferedReader read = BF.readFile(file);
		//	line = read.readLine();
			
//			out.write(line+",url\n");
		//	out.write("rank,login,"++",url\n");
			while((line=read.readLine())!=null){
				i++;
				if(i>1000)
					break;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if((i/100000)%25==0)
						System.out.println(" ");
				}
				String[] words = line.split(",");
				
				String id = words[0];
				String login = words[1];
				String url = "https://github.com/"+login.substring(1,login.length()-1);
				if(id.contains("id")){
					url = "url";
				}
				
				line = line.replace(login, url);
				out.write(line+"\n");
			//	System.out.print(uid+" - ");
 			}
			out.close();
			System.out.println(i+" ");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ResultShow r = new ResultShow();
	//	r.addUserUrl();
		File f = new File(Constants.dirname+"dump\\features\\ORG\\ORGScoreWatchHindexAveMaxSum.csv");
		r.UsertoUrl(f);
	}

}
