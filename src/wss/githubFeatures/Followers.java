package wss.githubFeatures;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import utils.BF;
import utils.Constants;

public class Followers {

	/**
	 * @param args
	 */
//	File followersFile = new File(Constants.dirname+"dump\\followers.csv");
	File followersFile = new File(Constants.dumpFile+"features\\USR\\followers.csv");
	File usersFile = new File(followersFile.getParent()+"\\usersInfo.csv");
	public void setFollowersFile(File f){
		followersFile = f;
	}
	public void setUsersFile(File f){
		usersFile =f;
	}
	public void userFollowersNet() throws IOException{
		System.out.println("userFollowersNet~~~~~~~~~~~~~~");
		String line = "";
		int i=0;
		TreeMap<Integer,ArrayList<Integer>> userFollowersListMap = new TreeMap<Integer,ArrayList<Integer>>();
		
//    	BufferedReader reader = BF.readFile(usersFile,"utf8");
//    	System.out.println("read  "+usersFile.getName()+", waiting......");
//		while((line=reader.readLine())!=null){
//			i++;
//			if(i%100000==0){
//				System.out.print(i/100000+" ");
//				if(i/100000%25==0){
//					System.out.println();
//				}
//			}
//			String [] words = line.split(",");
//			int userid = Integer.valueOf(words[0]);;
//			userFollowersListMap.put(userid, new ArrayList<Integer> ());
//		}
//		System.out.println("\n USERs number is "+userFollowersListMap.size());
		BufferedWriter out = new BufferedWriter(
				new FileWriter(followersFile.getParent()+"\\userFollowersNum2.csv"));
		BufferedWriter outL = new BufferedWriter(
				new FileWriter(followersFile.getParent()+"\\userFollowersList2.csv"));
	
		System.out.println(followersFile.getName());
		BufferedReader r1 = BF.readFile(followersFile);
		i=0;
		while((line = r1.readLine())!=null){
			i++;
			if(i%1000000==0){
				System.out.print(i/1000000+" ");
				if(i/1000000%25==0){
					System.out.println();
				}
			}
			String[] words = line.split(",");
			int follower_id = Integer.valueOf(words[0]);
			int user_id = Integer.valueOf(words[1]);
			ArrayList<Integer> c = userFollowersListMap.get(user_id);
			if(c==null){
				c=new ArrayList<Integer> () ;
			}
			c.add(follower_id);
			userFollowersListMap.put(user_id, c);
		}
		System.out.println("\nUSERs number is "+userFollowersListMap.size());
		for(Map.Entry<Integer, ArrayList<Integer>> m:userFollowersListMap.entrySet()){
			int t = m.getKey();//userid
			out.write(t+","+m.getValue().size()+"\n");
			outL.write(t+",");
			for(Integer f:m.getValue()){
				outL.write(f+" ");
			}
			outL.write("\n");
		}
		out.close();
		outL.close();
	}
	public File userFollowersNum() throws IOException{
		System.out.println("userFollowersNum~~~~~~~~~~~~~~"+followersFile.getPath());
		String line = "";
		int i=0;
		TreeMap<Integer,Integer> userFollowersNumMap = new TreeMap<Integer,Integer>();
		File outFile = new File(followersFile.getParent()+"\\userFollowersNum.csv");
		BufferedWriter out = new BufferedWriter(
				new FileWriter(outFile));
	
		System.out.println(followersFile.getName());
		BufferedReader r1 = BF.readFile(followersFile);
		i=0;
		while((line = r1.readLine())!=null){
			i++;
			if(i%1000000==0){
				System.out.print(i/1000000+" ");
				if(i/1000000%25==0){
					System.out.println();
				}
			}
			String[] words = line.split(",");
			int follower_id = Integer.valueOf(words[0]);
			int user_id = Integer.valueOf(words[1]);
			Integer c = userFollowersNumMap.get(user_id);
			if(c==null){
				c=0;
			}
			c++;
			userFollowersNumMap.put(user_id, c);
		}
		System.out.println("\nUSERs number is "+userFollowersNumMap.size());
		for(Map.Entry<Integer, Integer> m:userFollowersNumMap.entrySet()){
			int t = m.getKey();//userid
			out.write(t+","+m.getValue()+"\n");
			
		}
		out.close();
		return outFile;
	}
	public void userFerFersNumList() throws IOException{//
		//用户的每个Follower拥有的Follower数量
		System.out.println("userFerFersNumList~~~~~~~~~~~~~~");
//		BufferedWriter outL = new BufferedWriter(
//				new FileWriter(Constants.screenFile+"features\\userFollowersList.txt"));
		String line = "";
		String[] words = null;
		int i=0;
		TreeMap<Integer,Integer> userFollowersNumMap = new TreeMap<Integer,Integer>();
		File usersFP = new File(Constants.screenFile+"\\features\\userFollowersNum.csv");
    	BufferedReader reader = BF.readFile(usersFP,"utf8");
    	System.out.println("read  "+usersFP.getName()+", waiting......");
		while((line=reader.readLine())!=null){
			if(line.contains("id")){
				continue;
			}
			i++;
			if(i%100000==0){
				System.out.print(i/100000+" ");
				if(i/100000%25==0){
					System.out.println();
				}
			}
			words = line.split(",");
			int userid = Integer.valueOf(words[0]);
			Integer num = Integer.valueOf(words[words.length-2]);
			userFollowersNumMap.put(userid, num);
		}
		System.out.println("\n USERs number is "+userFollowersNumMap.size());
		
		BufferedWriter out = new BufferedWriter(
				new FileWriter(Constants.screenFile+"features\\userFerFersNumList.txt"));
		File listFile = new File(Constants.screenFile+"features\\userFollowersList.csv");
		System.out.println(listFile.getName());
		BufferedReader r1 = BF.readFile(listFile);
		i=0;
		while((line = r1.readLine())!=null){
			i++;
			if(i%100000==0){
				System.out.print(i/100000+" ");
				if(i/100000%25==0){
					System.out.println();
				}
			}
			words = line.split(",");
			if(words.length==1){
				out.write(line+"\n");
				continue;
			}
			int uid = Integer.valueOf(words[0]);
			out.write(uid+",");
			
			String[] fersNums = words[1].split(" ");
			for(String f:fersNums){
				out.write(userFollowersNumMap.get(Integer.valueOf(f))+" ");
			}
			out.write("\n");
		}
		out.close();
	}
	
	public void userFolloweesNet() throws IOException{
		BufferedWriter out = new BufferedWriter(new FileWriter(
				followersFile.getParent()+"\\userFolloweesNum.csv"));
		BufferedWriter out2 = new BufferedWriter(new FileWriter(
				followersFile.getParent()+"\\userFolloweesList.csv"));
		
		TreeMap<Integer,ArrayList<Integer>> userFolloweesListMap = new TreeMap<Integer,ArrayList<Integer>>();
		String line = "";
		int i=0;
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
			int userid = Integer.valueOf(words[0]);
			userFolloweesListMap.put(userid, new ArrayList<Integer> ());
		}
		System.out.println("\n USERs number is "+userFolloweesListMap.size());
		System.out.println(followersFile.getName());
		BufferedReader r1 = BF.readFile(followersFile);
		i=0;
		while((line = r1.readLine())!=null){
			i++;
			if(i%1000000==0){
				System.out.print(i/1000000+" ");
				if((i/1000000)%25==0)
					System.out.println(" ");
			}
			String[] words = line.split(",");
			int fid = Integer.valueOf(words[0]);
			int uid = Integer.valueOf(words[1]);
			ArrayList<Integer> list = userFolloweesListMap.get(fid);
			if(list==null){
				list = new ArrayList<Integer>();
			}
			list.add(uid);
			userFolloweesListMap.put(fid, list);
		}
		System.out.println("\n USERs number is "+userFolloweesListMap.size());

		for(Map.Entry<Integer,ArrayList<Integer>> m:userFolloweesListMap.entrySet()){
			int t = m.getKey();//userid
			ArrayList<Integer> list = m.getValue();
			out.write(t+","+list.size()+"\n");
			out2.write(t+",");
			for(Integer w:list){
				out2.write(w+" ");
			}
			out2.write("\n");
		}
		out.close();
		out2.close();
	}
	public void userFolloweesAna(){
		try{
			BufferedReader r = BF.readFile("Data\\Feature\\Followers\\userFolloweesNum.txt");
			String line = "";
			TreeMap<Integer,Integer> anaMap = new TreeMap<Integer,Integer>();
			int[] datas = {1,2,3,4,5,6,7,8,9,10,20,30,40,50,60,70,80,90,100,200,300,400,500,1000,2000,5000,10000,50000,10000000};
			for(int i=0;i<datas.length;i++){
				anaMap.put(datas[i], 0);
			}
			
			while((line=r.readLine())!=null){
				String[] words = line.split(",");
				int uid = Integer.valueOf(words[0]);
				int ferNum = Integer.valueOf(words[1]);
				int start = 0;
				for(int i=0;i<datas.length;i++){
					if(ferNum<=datas[i]){
						int c = anaMap.get(datas[i]);
						c++;
						anaMap.put(datas[i], c);
					//	break;
					}
				}
			}
			File f = new File("Data\\Feature\\Followers");
			if(!f.exists()){
				f.mkdirs();
			}
			BufferedWriter out = new BufferedWriter(new FileWriter(f.getPath()+"\\anaFollowing.csv",true));
			for(Map.Entry<Integer, Integer> m:anaMap.entrySet()){
				out.write(m.getKey()+","+m.getValue()+"\n");
			}
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void userFerFee(){
		try{
			BufferedReader rer = BF.readFile(Constants.screenFile+"features\\userFollowersNum.csv");
			BufferedReader ring = BF.readFile(Constants.screenFile+"features\\userFolloweesNum.csv");
			String line = "";
			TreeMap<Integer,Integer> userFerMap = new TreeMap<Integer,Integer>();
			while((line=rer.readLine())!=null){
				String[] words = line.split(",");
				int uid = Integer.valueOf(words[0]);
				int ferNum = Integer.valueOf(words[1]);
				userFerMap.put(uid,ferNum);
			}
			BufferedWriter out = new BufferedWriter(new FileWriter
					(Constants.screenFile+"features\\userFerFee.csv"));
			out.write("uid,ferNum,fingNum\n");
			while((line = ring.readLine())!=null){
				String[] words = line.split(",");
				int uid = Integer.valueOf(words[0]);
				int feeNum = Integer.valueOf(words[1]);
				Integer ferNum = userFerMap.get(uid);
				if(ferNum==null)
					ferNum=0;
				userFerMap.remove(uid);
				out.write(uid+","+ferNum+","+feeNum+"\n");
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
	public void userFollowersAna(){
		try{
			BufferedReader r = BF.readFile(Constants.screenFile+"features\\userFollowersNum.csv");
			String line = "";
			TreeMap<Integer,Integer> anaMap = new TreeMap<Integer,Integer>();
//			int[] datas = {1,2,3,4,5,6,7,8,9,10,20,30,40,50,60,70,80,90,100,200,300,400,500,1000,2000,5000,10000,50000};
//			for(int i=0;i<datas.length;i++){
//				anaMap.put(datas[i], 0);
//			}
			
			while((line=r.readLine())!=null){
				String[] words = line.split(",");
				if(words[0].contains("id")){
					continue;
				}
				int uid = Integer.valueOf(words[0]);
				int ferNum = Integer.valueOf(words[2]);
				Integer c = anaMap.get(ferNum);
				if(c==null){
					c=0;
				}
				c++;
				anaMap.put(ferNum, c);//
//				int start = 0;
//				for(int i=0;i<datas.length;i++){
//					if(ferNum<=datas[i]){
//						int c = anaMap.get(datas[i]);
//						c++;
//						anaMap.put(datas[i], c);
//						break;
//					}
//				}
			}
			BufferedWriter out = new BufferedWriter(new FileWriter
					(Constants.screenFile+"Analysis\\StaFollowers.csv",true));
			for(Map.Entry<Integer, Integer> m:anaMap.entrySet()){
				out.write(m.getKey()+","+m.getValue()+"\n");
			}
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void FerFeeANA(){//用户的关注者和粉丝之间重合度
		try{
			BufferedReader r = BF.readFile(followersFile.getParent()+"\\userFollowersList.csv");
			String line = "";
			TreeMap<Integer,ArrayList<Integer>> userFollowersListMap = new TreeMap<Integer,ArrayList<Integer>>();			
			while((line=r.readLine())!=null){
				String[] words = line.split(",");
				if(words[0].contains("id")){
					continue;
				}
				int uid = Integer.valueOf(words[0]);
			//	System.out.println(line);
				if(words.length==1){
					userFollowersListMap.put(uid,new ArrayList<Integer>());
					continue;
				}
				String[] fers = words[1].split(" ");
				ArrayList<Integer> c = userFollowersListMap.get(uid);
				if(c==null){
					c=new ArrayList<Integer>();
				}
				for(String fer:fers){
					c.add(Integer.valueOf(fer));
				}
				userFollowersListMap.put(uid, c);//
			}
			BufferedWriter out = new BufferedWriter(new FileWriter
					(followersFile.getParent()+"\\ferfeeAna.csv"));
			r = BF.readFile(followersFile.getParent()+"\\userFolloweesList.csv");
			while((line=r.readLine())!=null){
				String[] words = line.split(",");
				if(words[0].contains("id")){
					continue;
				}
				
				int uid = Integer.valueOf(words[0]);
				if(words.length==1){
					out.write(uid+","+(0)+","+(0)+"\n");//;
					continue;
				}
				String[] fees = words[1].split(" ");
				ArrayList<Integer> fersList = userFollowersListMap.get(uid);
				double count = 0;//粉丝同时被关注的数量
				if(fersList==null){
					fersList=new ArrayList<Integer>();
				}
				for(String fee:fees){
					if(fersList.contains(Integer.valueOf(fee))){
						count++;
					}
				}
				if(fersList.size()==0||fees.length==0){
					out.write(uid+","+(0)+","+(0)+"\n");//
				}else{
					out.write(uid+","+(count/fersList.size())+","+(count/fees.length)+"\n");//
				}
			}
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void userAna(File f){
		try{
			DecimalFormat df = new DecimalFormat("#.0");
			System.out.println();

			BufferedReader r = BF.readFile(f);
			String line = "";
			TreeMap<Double,Integer> anaMap = new TreeMap<Double,Integer>();
			double[] datas = {0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1.0};
			for(int i=0;i<datas.length;i++){
				anaMap.put(datas[i], 0);
			}
			
			while((line=r.readLine())!=null){
				String[] words = line.split(",");
				if(words[0].contains("id")){
					continue;
				}
				int uid = Integer.valueOf(words[0]);
				double t = Double.valueOf(words[2]);
				t = Double.valueOf(df.format(t));
				Integer c = anaMap.get(t);
				if(c==null){
					c=0;
				}
				c++;
				anaMap.put(t, c);//
//				int start = 0;
//				for(int i=0;i<datas.length;i++){
//					if(ferNum<=datas[i]){
//						int c = anaMap.get(datas[i]);
//						c++;
//						anaMap.put(datas[i], c);
//						break;
//					}
//				}
			}
			BufferedWriter out = new BufferedWriter(new FileWriter
					(f.getParent()+"\\ANA"+f.getName()+"",true));
			for(Map.Entry<Double, Integer> m:anaMap.entrySet()){
				out.write(m.getKey()+","+m.getValue()+"\n");
			}
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void selectFersNumbyID(int userid){
		try{
			String line = "";
			int i=0;
	    	File usersFP = new File(Constants.screenFile+"tables\\followers.csv");
	    	System.out.println("read  "+usersFP.getName()+", waiting......");
	    	BufferedReader reader = BF.readFile(usersFP,"utf8");
			while((line=reader.readLine())!=null){
				i++;
				
//				if(i%100000==0){
//					System.out.print(i/100000+" ");
//					if(i/100000%25==0){
//						System.out.println();
//					}
//				}
				if(line.contains("id")){
					continue;
				}
				String [] words = line.split(",");
				int uid = Integer.valueOf(words[1]);
				
				if(userid == uid){
				//	System.out.println();
					System.out.println(line);
				//	break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Followers f = new Followers();
		long start = System.currentTimeMillis();
	//	f.userFollowersNet();
//		f.userFolloweesNet();
//		f.userFerFersNumList();
	//	f.userFerFee();
//		f.userFollowersAna();
	//	f.FerFeeANA();
		File file = new File("D:\\mysql-2017-01-19\\dump\\features\\USR\\ferfeeAna.csv");
		f.userAna(file);
		long end = System.currentTimeMillis();
		System.out.println("计算用时="+(end-start)/1000);
	}

}
