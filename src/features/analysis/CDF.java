package features.analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Map;
import java.util.TreeMap;

import utils.BF;

public class CDF {

	/**
	 * @param args
	 */
	String path = "E:\\mysql-2015-09-25\\screen\\Features\\allfeatures\\";
	int[] datas = {1,2,3,4,5,6,7,8,9,10,20,30,40,50,60,70,80,90,100,200,300,400,
			       500,1000,2000,5000,10000,50000,10000000};
	public boolean CDFAna(String featureName,boolean cdf){//cdf表征是否为累积计算，cdf=true,累积；否则不累计
		try{
			BufferedReader r = BF.readFile(path+featureName+".csv");
			String line = "";
			int total=0;
			if(featureName.startsWith("user")){
				total = 848982;
			}else if(featureName.startsWith("repo")){
				total = 10059784;
			}else{
				System.out.println("featureName is wrong _  "+featureName);
				return false;
			}
			TreeMap<Integer,Integer> anaMap = new TreeMap<Integer,Integer>();
			for(int i=0;i<datas.length;i++){
				anaMap.put(datas[i], 0);
			}
			
			while((line=r.readLine())!=null){
				String[] words = line.split(",");
				int uid = Integer.valueOf(words[0]);
				int repoNum = Integer.valueOf(words[1]);
				int start = 0;
				for(int i=0;i<datas.length;i++){
					if(repoNum<=datas[i]){
						int c = anaMap.get(datas[i]);
						c++;
						anaMap.put(datas[i], c);
//						if(i==datas.length-1){
//							System.out.println(datas[i]+">=,"+uid);
//						}
						if(!cdf)
							break;
					}
				}
			}
			File f = new File(new File(path).getParent()+"\\CDF\\");
			if(!f.exists()){
				f.mkdirs();
			}
			
			BufferedWriter out = new BufferedWriter(new FileWriter(
					f.getPath()+"\\"+featureName+".csv",true));
			out.write("0,0,0\n");
			for(Map.Entry<Integer, Integer> m:anaMap.entrySet()){
				out.write(m.getKey()+","+(double)m.getValue()/total+","+m.getValue()+"\n");
			}
			out.close();
			System.out.println("Down! ");
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CDF c = new CDF();
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
        boolean cdf = false;
    	c.CDFAna(f10,cdf);
//    	c.CDFAna(f1,cdf);
//		c.CDFAna(f2,cdf);
//		c.CDFAna(f3,cdf);
//		c.CDFAna(f4,cdf);
//		c.CDFAna(f7,cdf);
//		c.CDFAna(f9,cdf);
		
	}

}