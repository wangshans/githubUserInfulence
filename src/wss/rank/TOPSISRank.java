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

public class TOPSISRank {

	/**
	 * @param args
	 * 基于排序的TOPSIS算法
	 */
	public void sortFileAverage(File f){//对于分值相同的用户取其排名平均值为该分值排名
		try{
			System.out.println("sort------------"+f.getName());
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
			File outFile2 = new File(path+"rank\\Average\\"+"rankA_"+f.getName());
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
	public File sortFileFirst(File f){//对于分值相同的用户取最靠前排名为其排名值
		try{
			System.out.println("sort------------"+f.getName());
         	BufferedReader reader = BF.readFile(f);
     		String line = "";
     		TreeMap<Integer,Double> userScoreMap = new TreeMap<Integer,Double>();
     		ArrayList<Double> scoreList = new ArrayList<Double> ();
     		while((line=reader.readLine())!=null){
     			String[] words = line.split(",");
     			Double score = Double.valueOf(words[1]);
     			userScoreMap.put(Integer.valueOf(words[0]),score);
     			if(scoreList.contains(score)){
     				continue;
     			}
     			scoreList.add(score);
     		}
     		System.out.println("\tTotal Number = "+userScoreMap.size()+"++++++++");
			
			//排序
			Collections.sort(scoreList, new Comparator<Double>(){   
			    public int compare(Double m1, Double m2) {     
			        //return (m2.getValue() - m1.getValue());
			        return (m2.compareTo(m1));
			//m1-m2     递增顺序
			//m2-m1     递减顺序
			    }
			});
			System.out.println("\tsorted "+"++++++++");
			File outFile = new File(f.getParent()+"\\rank\\"+f.getName());
			BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
			int i=0;
			for(Map.Entry<Integer, Double> m:userScoreMap.entrySet()){
				i++;
				if(i%10000==0){
    				System.out.print(i/10000+" ");
    				if(i/10000%25==0){
    					System.out.println();
    				}
    				out.flush();
    			}
				int user = m.getKey();
				Double score = m.getValue();
				int rank = scoreList.indexOf(score)+1;
				out.write(user+","+rank+"\n");
			}
			out.close();
			System.out.println(" ");
			return outFile;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public File combineFeatures(String path){
    	try{
    		 TreeMap<Integer,String> userFeatures = new TreeMap<Integer,String>();
    		 
             File outFile = new File(path+"FeaturesCombine.csv");
             BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
            
             long st = System.currentTimeMillis();
             File FeaturesPath = new File(path+"combine\\");
             
             File[] featureFiles = FeaturesPath.listFiles();
             for(File f:featureFiles){
            	 int i=0;
            //	 File ff = normalize(f);
            //	 File ff = sortFileFirst(f);
            	 BufferedReader r1 = BF.readFile(f);
            	 
                 String line = "";
                 while((line = r1.readLine())!=null){
                	 i++;
 	    			if(i%100000==0){
 	    				System.out.print(i/100000+" ");
 	    				if(i/100000%25==0){
 	    					System.out.println();
 	    				}
 	    			}
                     String[] words = line.split(",");
                     int userId = Integer.valueOf(words[0]);
                     String temp = words[1];
                     String feature =  userFeatures.get(userId);
                     if(feature==null){
                    	 userFeatures.put(userId, temp);
                     }else{
                    	 userFeatures.put(userId,(feature+","+temp));
                     }
                 }
                 r1.close();
                 long et = System.currentTimeMillis();
                 long time = (et-st)/1000;
             }
             
            
             for(Map.Entry<Integer, String> m:userFeatures.entrySet()){
            	 out.write(m.getKey()+","+m.getValue()+"\n");
             }
             out.close();
             System.out.println("\n Down!");
             return outFile;
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return null;
	}
	public double getMax(ArrayList<Double> list){//求最大
		double max =list.get(0);//初始化为第一个值
		for(double f:list){
			if(max<f){
				max = f;
			}
		}
		return max;
	}
	public double getMin(ArrayList<Double> list){//求最小
		double min =list.get(0);//初始化为第一个值
		for(double f:list){
			if(min>f){
				min = f;
			}
		}
		return min;
	}
	public double dMax(ArrayList<Double> list){
		double dMax = 0;
		double aMax = getMax(list);
		for(double d:list){
			dMax += Math.pow(aMax-d,2);
		}
		dMax = Math.sqrt(dMax);
		return dMax;
	}
	public double dMin(ArrayList<Double> list){
		double dMin = 0;
		double aMin = getMin(list);
		for(double d:list){
			dMin += Math.pow(aMin-d,2);
		}
		dMin = Math.sqrt(dMin);
		return dMin;
	}
	public double getSi(ArrayList<Double> list){
		double s=0.0;
		double dMin = dMin(list);
		double dMax = dMax(list);
		if(dMax+dMin==0){
			return 0;
		}
	//	System.out.println(dMax+" "+dMin);
		
	//	s = dMin/(dMax+dMin);
		s = dMax/(dMax+dMin);//消极结果除以消极和积极结果的和
		return s;
	}

	public void getTOPSIS(File FeatureFile){
		try{
			BufferedReader read = BF.readFile(FeatureFile);
			String line = "";
			int i=0;
			TreeMap<Integer,Double> userACQMap = new TreeMap<Integer,Double>();
			while((line = read.readLine())!=null){
				 i++;
	    			if(i%100000==0){
	    				System.out.print(i/100000+" ");
	    				if(i/100000%25==0){
	    					System.out.println();
	    				}
	    			}
				String[] words = line.split(",");
				int id = Integer.valueOf(words[0]);//第一个字段为UserID
				ArrayList<Double> fList = new ArrayList<Double>();
				for(int j=1;j<words.length;j++){
					double fj = Double.valueOf(words[j]);
					fList.add(fj);
				}
				double si = getSi(fList);
				userACQMap.put(id, si);
			//	break;
			}
			System.out.println(userACQMap.size());
			List<Map.Entry<Integer, Double>> list=
			    new ArrayList<Map.Entry<Integer, Double>>(userACQMap.entrySet());

			//排序
			Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>(){   
			    public int compare(Map.Entry<Integer, Double> m1, Map.Entry<Integer,Double> m2) {     
			    	return (m2.getValue().compareTo(m1.getValue()));
			//m1-m2     递增顺序
			//m2-m1     递减顺序
			    }
			});
			BufferedWriter out = new BufferedWriter(
					new FileWriter(FeatureFile.getParent()+"\\ACQResult.csv"));
			for(Map.Entry<Integer, Double> m:list){
				out.write(m.getKey()+","+m.getValue()+"\n");
			}
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void run(){
		try{
			File features = combineFeatures(path);
			getTOPSIS(features);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	String path = "";
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
