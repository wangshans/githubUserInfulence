package action;

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
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class result {

	/**
	 * @param args
	 */
	String time = "";
	public BufferedReader readFile(String filePath){
		System.out.println("read file from "+filePath);
		try{
			BufferedInputStream fis = new BufferedInputStream(new FileInputStream(filePath));
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis),5*1024*1024);
			return reader;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public TreeMap<Integer,Integer> getRepoOwner() throws Exception{
		BufferedReader read = readFile(" ");
		TreeMap<Integer,Integer> repoOwnerMap = new TreeMap<Integer,Integer>();
		String line = "";
		int i=0;
		Set<Integer> points = new TreeSet<Integer>();
	//	Set<Integer> points = new TreeSet<Integer>();
		while((line = read.readLine())!=null){
			
		}
		return repoOwnerMap;
		
	}
	public void repoWatchersNET() throws IOException{
		
		BufferedWriter out = new BufferedWriter(new FileWriter("Data\\result\\"+time+"RWsNum.txt"));
	//	BufferedWriter out2 = new BufferedWriter(new FileWriter("testData\\RepowatchersList.txt"));
		
		TreeMap<Integer,Integer> userWatchersNum = new TreeMap<Integer,Integer>();
//		TreeMap<Integer,ArrayList<Integer>> userWatchersList = new TreeMap<Integer,ArrayList<Integer>>();
		String line = "";
		int i=0;
		BufferedReader r1 = readFile("Data\\spliWatchersByTime\\watchers"+time+".txt");
		Set<Integer> points = new TreeSet<Integer>();
	//	Set<Integer> points = new TreeSet<Integer>();
		while((line = r1.readLine())!=null){
			i++;
			String[] words = line.split(",");
			int rid = Integer.valueOf(words[0]);
			int wid = Integer.valueOf(words[1]);
			points.add(rid);
			Integer c = userWatchersNum.get(rid);
			if(c==null)
				c=0;
			c++;
			userWatchersNum.put(rid, c);
//			ArrayList<Integer> list = userWatchersList.get(rid);
//			if(list==null){
//				list = new ArrayList<Integer>();
//			}
//			list.add(wid);
//			userWatchersList.put(rid, list);
		}
		List<Map.Entry<Integer, Integer>> list=
		    new ArrayList<Map.Entry<Integer, Integer>>(userWatchersNum.entrySet());

		//排序
		Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>(){   
		    public int compare(Map.Entry<Integer, Integer> m1, Map.Entry<Integer, Integer> m2){     
		        double d = m2.getValue()-m1.getValue();
		        if(d>0)
		        	return 1;
		        else
		        	return 0;
		    }
		});
		
		for(Map.Entry<Integer, Integer> m:list){
			out.write(m.getKey()+","+m.getValue()+"\n");
//			ArrayList<Integer> list = RepowatchersList.get(t);
//			out2.write(t+" ");
//			for(Integer w:list){
//				out2.write(w+",");
//			}
//			out2.write("\n");
		}
		out.close();
	//	out2.close();
	}
	public void userWatchersNet() throws Exception{
	
		String connectStr = "jdbc:mysql://localhost:3306/github";  
        connectStr += "?useServerPrepStmts=false&rewriteBatchedStatements=true";  
        String username = "root";  
        String password = "123456";  
		Class.forName("com.mysql.jdbc.Driver");  
    	Connection conn = DriverManager.getConnection(connectStr, username,password);  
        Statement stmt = conn.createStatement();  
         
               
		BufferedWriter out = new BufferedWriter(new FileWriter("Data\\result\\"+time+"UWsNum00.txt"));
		String line = "";
		int i=0;
		BufferedReader r1 = readFile("Data\\result\\"+time+"RWsNum.txt");
		while((line = r1.readLine())!=null){
			i++;
			String[] words = line.split(",");
			int rid = Integer.valueOf(words[0]);
			int wnum = Integer.valueOf(words[1]);
			String sql = "select owner_id from ghtorrent.projects where id="+rid;  
			ResultSet rs = stmt.executeQuery(sql); 
			int ownerid=0;
		    if(rs.next())  
		    {  
		    	ownerid = rs.getInt(1);//或者为rs.getString(1)，根据数据库中列的值类型确定，参数为第一列  
		    }  
		    out.write(ownerid+","+wnum+"\n");
		}
//		List<Map.Entry<Integer, Integer>> list=
//		    new ArrayList<Map.Entry<Integer, Integer>>(userWatchersNum.entrySet());
//
//		//排序
//		Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>(){   
//		    public int compare(Map.Entry<Integer, Integer> m1, Map.Entry<Integer, Integer> m2){     
//		        double d = m2.getValue()-m1.getValue();
//		        if(d>0)
//		        	return 1;
//		        else
//		        	return 0;
//		    }
//		});
//		
//		for(Map.Entry<Integer, Integer> m:list){
//			out.write(m.getKey()+","+m.getValue()+"\n");
//		}
		out.close();
	}
	public void userWatchersNet2() throws Exception{
		BufferedWriter out = new BufferedWriter(new FileWriter("Data\\result\\"+time+"UWsNum.txt"));
		TreeMap<Integer,Integer> userWatchersNum = new TreeMap<Integer,Integer>();
		String line = "";
		int i=0;
		BufferedReader r1 = readFile("Data\\result\\"+time+"UWsNum00.txt");
		while((line = r1.readLine())!=null){
			i++;
			String[] words = line.split(",");
			int ownerid = Integer.valueOf(words[0]);
			int wnum = Integer.valueOf(words[1]);
			Integer c = userWatchersNum.get(ownerid);
			if(c==null){
				userWatchersNum.put(ownerid, wnum);
			}else{
				userWatchersNum.put(ownerid, c+wnum);
			}
		}
		List<Map.Entry<Integer, Integer>> list=
		    new ArrayList<Map.Entry<Integer, Integer>>(userWatchersNum.entrySet());

		//排序
		Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>(){   
		    public int compare(Map.Entry<Integer, Integer> m1, Map.Entry<Integer, Integer> m2){     
		        double d = m2.getValue()-m1.getValue();
		        if(d>0)
		        	return 1;
		        else
		        	return 0;
		    }
		});
		
		for(Map.Entry<Integer, Integer> m:list){
			out.write(m.getKey()+","+m.getValue()+"\n");
		}
		out.close();
	}
	public void userReposNET() throws IOException{
		
		BufferedWriter out = new BufferedWriter(new FileWriter("Data\\result\\"+time+"URsNum.txt"));
	//	BufferedWriter out2 = new BufferedWriter(new FileWriter("testData\\userReposList.txt"));
		
		TreeMap<Integer,Integer> RepowatchersNum = new TreeMap<Integer,Integer>();
	//	TreeMap<Integer,ArrayList<Integer>> RepowatchersList = new TreeMap<Integer,ArrayList<Integer>>();
		String line = "";
		int i=0;
		BufferedReader r1 = readFile("Data\\spliProjectsByTime\\pro"+time+".txt");
		Set<Integer> points = new TreeSet<Integer>();
	//	Set<Integer> points = new TreeSet<Integer>();
		while((line = r1.readLine())!=null){
			i++;
			String[] words = line.split(",");
			int rid = Integer.valueOf(words[0]);
			int uid = Integer.valueOf(words[1]);
			points.add(uid);
			Integer c = RepowatchersNum.get(uid);
			if(c==null)
				c=0;
			c++;
			RepowatchersNum.put(uid, c);
//			ArrayList<Integer> list = RepowatchersList.get(uid);
//			if(list==null){
//				list = new ArrayList<Integer>();
//			}
//			list.add(rid);
//			RepowatchersList.put(uid, list);
		}
		List<Map.Entry<Integer, Integer>> list=
		    new ArrayList<Map.Entry<Integer, Integer>>(RepowatchersNum.entrySet());

		//排序
		Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>(){   
		    public int compare(Map.Entry<Integer, Integer> m1, Map.Entry<Integer, Integer> m2){     
		        double d = m2.getValue()-m1.getValue();
		        if(d>0)
		        	return 1;
		        else
		        	return 0;
		    }
		});
		
		
		for(Map.Entry<Integer, Integer> m:list){
			out.write(m.getKey()+","+m.getValue()+"\n");
//			ArrayList<Integer> list = RepowatchersList.get(t);
//			out2.write(t+" ");
//			for(Integer w:list){
//				out2.write(w+",");
//			}
//			out2.write("\n");
		}
		out.close();
	//	out2.close();
	}
	public void userFollowersNET() throws IOException{
		
		BufferedWriter out = new BufferedWriter(new FileWriter("Data\\result\\"+time+"UFsNum.txt"));
	//	BufferedWriter out2 = new BufferedWriter(new FileWriter("testData\\userFollowersList.txt"));
		
		TreeMap<Integer,Integer> userFollowersNum = new TreeMap<Integer,Integer>();
	//	TreeMap<Integer,ArrayList<Integer>> userFollowersList = new TreeMap<Integer,ArrayList<Integer>>();
		String line = "";
		int i=0;
		BufferedReader r1 = readFile("Data\\spliFollowersByTime\\fol"+time+".txt");
		Set<Integer> points = new TreeSet<Integer>();
	//	Set<Integer> points = new TreeSet<Integer>();
		while((line = r1.readLine())!=null){
			i++;
			String[] words = line.split(",");
			int uid = Integer.valueOf(words[0]);
			int fid = Integer.valueOf(words[1]);
			points.add(uid);
			Integer c = userFollowersNum.get(uid);
			if(c==null)
				c=0;
			c++;
			userFollowersNum.put(uid, c);
//			ArrayList<Integer> list = userFollowersList.get(uid);
//			if(list==null){
//				list = new ArrayList<Integer>();
//			}
//			list.add(fid);
//			userFollowersList.put(uid, list);
		}
		List<Map.Entry<Integer, Integer>> list=
		    new ArrayList<Map.Entry<Integer, Integer>>(userFollowersNum.entrySet());

		//排序
		Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>(){   
		    public int compare(Map.Entry<Integer, Integer> m1, Map.Entry<Integer, Integer> m2){     
		        double d = m2.getValue()-m1.getValue();
		        if(d>0)
		        	return 1;
		        else
		        	return 0;
		    }
		});
		
		
		for(Map.Entry<Integer, Integer> m:list){
			out.write(m.getKey()+","+m.getValue()+"\n");
//			ArrayList<Integer> list = userFollowersList.get(t);
//			out2.write(t+" ");
//			for(Integer w:list){
//				out2.write(w+",");
//			}
//			out2.write("\n");
		}
		out.close();
	//	out2.close();
	}
	public void normalize(String sortedfile) throws IOException{
		BufferedReader reader = readFile("Data\\result\\"+sortedfile);
		String line="";
		int flag = 0;
		double normal=0.0;
		BufferedWriter out = new BufferedWriter(
				new FileWriter("Data\\result\\"+"normal\\"+sortedfile));
		while((line = reader.readLine())!=null){
			String[] words = line.split(",");
			double score = Double.valueOf(words[1]);
			if(flag==0){
				normal=score;
				flag=1;
			}
			score = score/normal;
			out.write(words[0]+","+score+"\n");
		}
		out.close();
	}
	public void normalize(File sortedfile) throws IOException{
		BufferedReader reader = readFile(""+sortedfile);
		String line="";
		int flag = 0;
		double normal=0.0;
		BufferedWriter out = new BufferedWriter(
				new FileWriter("Data\\result\\"+"normal\\"+sortedfile.getName()));
		while((line = reader.readLine())!=null){
			String[] words = line.split(",");
			double score = Double.valueOf(words[1]);
			if(flag==0){
				normal=score;
				flag=1;
			}
			score = score/normal;
			out.write(words[0]+","+score+"\n");
		}
		out.close();
	}
	public void writeFeature() throws Exception{  
		String line = "";
		String path = "Data\\result\\2015-1";
		BufferedReader readFol = readFile(path+"UFsNum.txt");
		TreeMap<Integer,Integer> folMap = new TreeMap<Integer,Integer>();
		while((line=readFol.readLine()) != null){
			String[] ws = line.split(",");
			Integer uid = Integer.valueOf(ws[0]);
			int folNum = Integer.valueOf(ws[1]);
			folMap.put(uid, folNum);
		}
		
		BufferedReader readRepo = readFile(path+"URsNum.txt");
		TreeMap<Integer,Integer> repoMap = new TreeMap<Integer,Integer>();
		while((line=readRepo.readLine()) != null){
			String[] ws = line.split(",");
			Integer uid = Integer.valueOf(ws[0]);
			int Num = Integer.valueOf(ws[1]);
			repoMap.put(uid, Num);
		}
		
		BufferedReader readWat = readFile(path+"UWsNum.txt");
		TreeMap<Integer,Integer> WatMap = new TreeMap<Integer,Integer>();
		while((line=readWat.readLine()) != null){
			String[] ws = line.split(",");
			Integer uid = Integer.valueOf(ws[0]);
			int Num = Integer.valueOf(ws[1]);
			WatMap.put(uid, Num);
		}
		BufferedReader readPRSco = readFile(path+"UserPR.txt");
		TreeMap<Integer,Double> PRScoMap = new TreeMap<Integer,Double>();
		while((line=readPRSco.readLine()) != null){
			String[] ws = line.split(",");
			Integer uid = Integer.valueOf(ws[0]);
			Double Num = Double.valueOf(ws[1]);
			PRScoMap.put(uid, Num);
		}
		BufferedReader readHSco = readFile(path+"watchersUserH.txt");
		TreeMap<Integer,Double> HScoMap = new TreeMap<Integer,Double>();
		while((line=readHSco.readLine()) != null){
			String[] ws = line.split(",");
			Integer uid = Integer.valueOf(ws[0]);
			double Num = Double.valueOf(ws[1]);
			HScoMap.put(uid, Num);
		}
		BufferedWriter out = new BufferedWriter(new FileWriter(path+"userFeatures.txt"));
		for(Map.Entry<Integer, Integer> m:folMap.entrySet()){
			int uid = m.getKey();
			int folnum = m.getValue();
			Integer reponum = repoMap.get(uid);
			if(reponum==null)
				reponum = 0;
			Integer watnum = WatMap.get(uid);
			if(watnum==null)
				watnum = 0;
			Double prScore = PRScoMap.get(uid);
			if(prScore==null)
				prScore = 0.0;
			Double hscore = HScoMap.get(uid);
			if(hscore==null)
				hscore = 0.0;
			out.write(uid+","+folnum+","+reponum+","+watnum+","+prScore+","+hscore+"\n");
		}
		out.close();
	}
	public void rank() throws IOException{
		BufferedReader reader = readFile("");
		String line="";
		int flag = 0;
		double normal=0.0;
		BufferedWriter out = new BufferedWriter(new FileWriter("Data\\result\\"+"normal\\"));
		while((line = reader.readLine())!=null){
			String[] words = line.split(",");
			double score = Double.valueOf(words[1]);
			if(flag==0){
				normal=score;
				flag=1;
			}
			score = score/normal;
			out.write(words[0]+","+score+"\n");
		}
		out.close();
	}
	public double max(ArrayList<Double> l){
		double max=l.get(0);
		for(double f:l){
			if(f>max)
				max = f;
		}
		return max;
	}
	public double min(ArrayList<Double> l){
		double min=l.get(0);
		for(double f:l){
			if(f<min)
				min = f;
		}
		return min;
	}
	public double avg(ArrayList<Double> l){
		double avg=0.0;
		for(double f:l){
			avg += f;
		}
		avg = avg/l.size();
		return avg;
	}
	public ArrayList<Double> normalized(ArrayList<Double> list){
		double max = max(list);
		ArrayList<Double> llist = new ArrayList<Double>();
		for(double d:list){
			double dd = Math.log(1+d)/Math.log(1+max);
			llist.add(dd);
		}
		return llist;
	}
	public double TOPSIS(String line){
		double ss=0.0;
		String[] words = line.split(",");
		ArrayList<Double> featuresList = new ArrayList<Double>();
		for(int i=1;i<words.length;i++){//0为uid,所以从1开始
			double f=Double.valueOf(words[i]);
			featuresList.add(f);
		}
		featuresList = normalized(featuresList);
		
		double Aplus = max(featuresList);
		double Aminus = min(featuresList);
		double fAvg = avg(featuresList);
		double dplus = 0.0;
		double dminus = 0.0;
		double SF = 0.0;
		for(double f:featuresList){
			dplus += Math.pow((Aplus-f), 2);
			dminus += Math.pow((f-Aminus), 2);
			SF += Math.pow((fAvg-f), 2);
		}
		dplus = Math.sqrt(dplus);
		dminus = Math.sqrt(dminus);
		SF = Math.sqrt(SF);
		SF = SF/5;
		
		ss = (1-SF/fAvg)*(dminus/(dplus+dminus));
		return ss;
	}
	public void ACQR() throws Exception{
		BufferedReader read = readFile("Data\\result\\2015-1userFeatures.txt");
		String line = "";
		TreeMap<Integer,Double> userScore = new TreeMap<Integer,Double>();
		while((line=read.readLine())!=null){
			String[] ws = line.split(",");
			int uid = Integer.valueOf(ws[0]);
			double score = TOPSIS(line);
			userScore.put(uid, score);
		}
		
		List<Map.Entry<Integer, Double>> list = new ArrayList<Map.Entry<Integer, Double>>(userScore.entrySet());
		//排序
		Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {   
		    public int compare(Map.Entry<Integer, Double> m1, Map.Entry<Integer, Double> m2) {     
		        //return (m2.getValue() - m1.getValue());
		        return (m2.getValue().compareTo(m1.getValue()));
		//m1-m2     递增顺序
		//m2-m1     递减顺序
		    }
		});
		BufferedWriter out = new BufferedWriter(new FileWriter("Data\\result\\2015-1userRank.txt"));
		for(Map.Entry<Integer, Double> m:list){
			out.write(m.getKey()+","+m.getValue()+"\n");
		}
		out.close();
	}
	public double consistency(List<Integer> list1,List<Integer> list2,int num){//list1和list2,前num名一致比率
		int concy = 0;
		list1 = list1.subList(0, num);
		list2 = list2.subList(0, num);
		for(int id1:list1){
			if(list2.contains(id1)){
				concy++;
			}
		}
		double parent = (double)concy/(double)num;
		return parent;
	}
	public void bijiao() throws Exception{
		String line = "";
		String path = "Data\\result\\2015-1";
		BufferedReader readFol = readFile(path+"userRank.txt");
		ArrayList<Integer> rankList = new ArrayList<Integer>();
		while((line=readFol.readLine()) != null){
			String[] ws = line.split(",");
			Integer uid = Integer.valueOf(ws[0]);
			rankList.add(uid);
		}
		
		BufferedReader readRepo = readFile(path+"watchersUserH.txt");
		ArrayList<Integer> folList = new ArrayList<Integer>();
		while((line=readRepo.readLine()) != null){
			String[] ws = line.split(",");
			Integer uid = Integer.valueOf(ws[0]);
			folList.add(uid);
		}
		System.out.println(consistency(rankList,folList,1000));
	}
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		result r = new result();
		long s = System.currentTimeMillis();
		//r.writeFeature();
	//	r.ACQR();
		r.bijiao();
		long end = System.currentTimeMillis();
		System.out.println(" Time "+(end-s)/1000);
	}

}
class user{
	int id;
	int watchersNum;
	int followersNum;
	int repoNum;
	
}
