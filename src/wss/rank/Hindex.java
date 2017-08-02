package wss.rank;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import utils.BF;
import utils.Constants;

public class Hindex {

	/**
	 * @param args
	 */
	File watchersFile = new File(Constants.dirname+"dump\\watchers.csv");
	File usersFile = new File(Constants.dirname+"dump\\features\\USR\\userInfo.csv");
	
	public void userWatcherNumList(){
		try{
			String line = "";
			int i=0;
			BufferedReader rRW = BF.readFile(watchersFile.getParent()+"\\features\\repoWatchersNum.csv");
			TreeMap<Integer,Integer> rwnumMap = new TreeMap<Integer,Integer>();
			while((line=rRW.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if((i/100000)%25==0)
						System.out.println(" ");
				}
				String[]words = line.split(",");
				int rid = Integer.valueOf(words[0]);

			//	System.out.print(uid+" - ");
				int watchersNum = Integer.valueOf(words[1]);
				rwnumMap.put(rid, watchersNum);
 			}
			System.out.println(i+" ");
			System.out.println("rwnum: "+rwnumMap.size());
//			BufferedWriter out = new BufferedWriter(new FileWriter
//					(Constants.screenFile+"features\\usersWatchersSumAveMaxNum.csv"));
//			BufferedWriter outList = new BufferedWriter(new FileWriter
//					(Constants.screenFile+"features\\usersWatchersNumList.csv"));
			BufferedWriter outList = new BufferedWriter(new FileWriter
					(usersFile.getParent()+"\\usersWatcherNumList.csv"));
			BufferedWriter outH = new BufferedWriter(new FileWriter
					(usersFile.getParent()+"\\usersWatcherHindex.csv"));
			BufferedReader read = BF.readFile(usersFile.getParent()+"\\userReposList.csv");
			i=0;
		//	TreeMap<Integer,ArrayList<Integer>> uwlist = new TreeMap<Integer,ArrayList<Integer>>();
			while((line=read.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if((i/100000)%25==0)
						System.out.println(" ");
					outList.flush();
					outH.flush();
				}
				String[]words = line.split(",");
//				if(words.length==1){
//					outList.write(words[0]+","+"\n");
//					outH.write(words[0]+","+0+"\n");
//				}else{
				ArrayList<Integer> watchersNumList = new ArrayList<Integer>();
				int h_index = 0;
		//		Integer uid = Integer.valueOf(words[0]);
				String[] reposid = words[1].split(" ");
				for(String rid:reposid){
					Integer repoid = Integer.valueOf(rid);
					Integer num = rwnumMap.get(repoid);
					if(num==null)
						num=0;
					watchersNumList.add(num);
				}
				//排序
	 			Collections.sort(watchersNumList,new Comparator<Integer>(){
	 				public int compare(Integer i1,Integer i2){
	 					return i2.compareTo(i1);
	 				}
	 			});
				
				outList.write(words[0]);
				for(int j=1;j<watchersNumList.size()+1;j++){
					int num = watchersNumList.get(j-1);
					outList.write(","+num);
				}
				outList.write("\n");
				for(int j=1;j<watchersNumList.size()+1;j++){
					int num = watchersNumList.get(j-1);
					if(j>num){
						h_index = j-1;
						break;
					}
					
				}
				outH.write(words[0]+","+h_index+"\n");
			//	break;
 			}
			System.out.println("users total num is "+i);
			
			outList.close(); 
			outH.close(); 
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Hindex h = new Hindex();
		h.userWatcherNumList();
	}

}
