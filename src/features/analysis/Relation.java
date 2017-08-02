package features.analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Map;
import java.util.TreeMap;

import utils.BF;

public class Relation {

	/**
	 * @param args
	 */
	String path = "E:\\mysql-2015-09-25\\screen\\Features\\allfeatures\\";
	public void userRelation(String feature1,String feature2){
		try{
			File f = new File(new File(path).getParent()+"\\Relations");
			if(!f.exists()){
				f.mkdirs();
			}
			
			BufferedReader rer = BF.readFile(path+feature1+".csv");
			BufferedReader ring = BF.readFile(path+feature2+".csv");
			String line = "";
			TreeMap<Integer,Integer> userFerMap = new TreeMap<Integer,Integer>();
			while((line=rer.readLine())!=null){
				String[] words = line.split(",");
				int uid = Integer.valueOf(words[0]);
				int ferNum = Integer.valueOf(words[1]);
				userFerMap.put(uid,ferNum);
			}
			int i=0;
			feature1 = feature1.replaceAll("user", "");
			feature1 = feature1.replaceAll("Num", "");
			feature2 = feature2.replaceAll("user", "");
			feature2 = feature2.replaceAll("Num", "");
			BufferedWriter out = new BufferedWriter(new FileWriter(f.getPath()+"\\user"+feature1+feature2+".csv"));
			out.write("uid,ferNum,fingNum\n");
			while((line = ring.readLine())!=null){
				String[] words = line.split(",");
				int uid = Integer.valueOf(words[0]);
				int fingNum = Integer.valueOf(words[1]);
				Integer ferNum = userFerMap.get(uid);
				if(ferNum==null)
					ferNum=0;
				userFerMap.remove(uid);
				out.write(uid+","+ferNum+","+fingNum+"\n");
				i++;
			}
			if(userFerMap.size()>0){
				for(Map.Entry<Integer, Integer> m:userFerMap.entrySet()){
					out.write(m.getKey()+","+m.getValue()+",0"+"\n");
				}
			}
			out.close();
			System.out.println("All down! "+i);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Relation r = new Relation();
		String f0 = "userYear";
        String f1 = "userFollowersNum";
        String f10 = "userFollowingsNum";
        String f2 = "userReposNum";
        String f3 = "userOriginalReposNum";
        String f4 = "userForkReposNum";//从被人fork的项目的数量
        String f5 = "userWatchersNum";
        String f6 = "UserPR";
        String f7 = "userCommitsNum";
        String f8 = "userIssuesNum";
        String f9 = "userForkedSumNum";//项目被fork的数量
	//	r.userRelation(f1,f10);
		r.userRelation(f1,f4);
	}

}
