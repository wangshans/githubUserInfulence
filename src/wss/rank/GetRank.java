package wss.rank;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import utils.BF;
import utils.Constants;


public class GetRank {

	/**
	 * @param args
	 * 1.各个列表按分值排序；
	 * 2.用排名名次替代分值；
	 */
//	String path = Constants.screenFile+"";

	
	public void sortFileEqually(File f){//对于分值相同的用户取最靠前排名为其排名值
		try{
			System.out.println("sort------------"+f.getName());
         	BufferedReader reader = BF.readFile(f);
     		String line = "";
     		String[] words = null;
     		TreeMap<Integer,Double> userScoreMap = new TreeMap<Integer,Double>();
     		while((line=reader.readLine())!=null){
     			words = line.split(",");
     			double score = Double.valueOf(words[1]);
     			userScoreMap.put(Integer.valueOf(words[0]),score);
     		}
     		System.out.println("\tTotal Number = "+userScoreMap.size()+"++++++++");
			List<Map.Entry<Integer, Double>> list=
			    new ArrayList<Map.Entry<Integer, Double>>(userScoreMap.entrySet());

			//排序
			Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {   
			    public int compare(Map.Entry<Integer, Double> m1, Map.Entry<Integer, Double> m2) {     
			        //return (m2.getValue() - m1.getValue());
			        return (m2.getValue()).toString().compareTo(m1.getValue().toString());
			//m1-m2     递增顺序
			//m2-m1     递减顺序
			    }
			});
			System.out.println("\tsorted "+"++++++++");
			BufferedWriter out = new BufferedWriter(new FileWriter(Constants.screenFile+"rank\\Equally\\"+f.getName()));
			File outFile2 = new File(Constants.screenFile+"rank\\"+"rankE_"+f.getName());
			BufferedWriter out2 = new BufferedWriter(new FileWriter(outFile2));
			int i=0;
			ArrayList<Double> scoreList = new ArrayList<Double>();
			for(Map.Entry<Integer, Double> m:list){
				i++;
				if(i%10000==0){
    				System.out.print(i/10000+" ");
    				if(i/10000%25==0){
    					System.out.println();
    				}
    				out.flush();
    				out2.flush();
    			}
				int user = m.getKey();
				Double score = m.getValue();
				out.write(user+","+score+"\n");
				int rank=i;
				if(scoreList.contains(score)){
					
				}else{
					scoreList.add(score);
				}
				rank = scoreList.indexOf(score)+1;
			//	System.out.println(user+","+score+","+i+","+rank);
				out2.write(user+","+score+","+rank+"\n");
			}
			out.close();
			out2.close();
			System.out.println(" ");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void sortFileAverage(File f){//对于分值相同的用户取其排名平均值为该分值排名
		try{
			System.out.println("sort------------");
    		double total=0;
         	BufferedReader reader = BF.readFile(f);
     		String line = "";
     		TreeMap<Integer,Double> userScoreMap = new TreeMap<Integer,Double>();
     		while((line=reader.readLine())!=null){
     			String[] words = line.split(",");
     			double score = Double.valueOf(words[1]);
     			userScoreMap.put(Integer.valueOf(words[0]),score);
     		}
     		System.out.println("\tTotal Number = "+userScoreMap.size()+"++++++++");
			List<Map.Entry<Integer, Double>> list=
			    new ArrayList<Map.Entry<Integer, Double>>(userScoreMap.entrySet());

			//排序
			Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {   
			    public int compare(Map.Entry<Integer, Double> m1, Map.Entry<Integer, Double> m2) {     
			        //return (m2.getValue() - m1.getValue());
			        return (m2.getValue()).toString().compareTo(m1.getValue().toString());
			//m1-m2     递增顺序
			//m2-m1     递减顺序
			    }
			});
			File outFile = new File(f.getPath());
			BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
		
			int i=0;
			TreeMap<Double,ArrayList<Integer>> userRankMap = new TreeMap<Double,ArrayList<Integer>>();
			for(Map.Entry<Integer, Double> m:list){
				i++;
				double score = m.getValue();
				out.write(m.getKey()+","+score+"\n");
				int rank=i;
				ArrayList<Integer> c = userRankMap.get(score);
				if(c==null){
					c = new ArrayList<Integer>();
					c.add(rank);
					c.add(1);
				}else{
					int rankSum = c.get(0);//第一个为重复分数之和
					int rankNum = c.get(1);//第二个数为重复分数的数量
					rankSum += rank;
					rankNum ++;
					c.set(0, rankSum);
					c.set(1, rankNum);
				}
				userRankMap.put(score, c);
			}
			out.close();
			
			//
			List<Map.Entry<Double, ArrayList<Integer>>> list2=
			    new ArrayList<Map.Entry<Double, ArrayList<Integer>>>(userRankMap.entrySet());

			//排序
			Collections.sort(list2, new Comparator<Map.Entry<Double, ArrayList<Integer>>>() {   
			    public int compare(Map.Entry<Double, ArrayList<Integer>> m1, Map.Entry<Double, ArrayList<Integer>> m2) {     
			        //return (m2.getValue() - m1.getValue());
			        return (m2.getKey()).toString().compareTo(m1.getKey().toString());
			//m1-m2     递增顺序
			//m2-m1     递减顺序
			    }
			});
			File outFile2 = new File(Constants.screenFile+"rank\\Average\\"+"rankA_"+f.getName());
			BufferedWriter out2 = new BufferedWriter(new FileWriter(outFile2));

			for(Map.Entry<Double, ArrayList<Integer>> m:list2){
				i++;
				double score = m.getKey();
				ArrayList<Integer> c = m.getValue();
				double sum = c.get(0);//rankSum
				double num = c.get(1);//rankNum
			//	ArrayList<Integer> usersList = new ArrayList<Integer>();
				double rank = sum/num;
				System.out.println("\t"+sum+","+num+","+rank);
				int k=0;
				for(Map.Entry<Integer, Double> m2:userScoreMap.entrySet()){
					int user = m2.getKey();
					double score2 = m2.getValue();
					if(score2==score){
						k++;
					//	usersList.add(user);
						out2.write(user+","+rank+"\n");
						if(k==num)
							break;
					}
					
				}
//				if(usersList.size()!=num){
//					System.out.println("wrong");
//				}
			}
			out2.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GetRank g = new GetRank();
		File f = new File(Constants.screenFile+"rank\\score");
		File[] files = f.listFiles();
		for(File ff:files){
			long s = System.currentTimeMillis();
			g.sortFileEqually(ff);
			long e = System.currentTimeMillis();
			System.out.println("Time : "+(e-s)/1000);
		}
		
	
	}

}
