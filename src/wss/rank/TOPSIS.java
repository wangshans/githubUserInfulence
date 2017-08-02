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

public class TOPSIS {

	/**
	 * @param args
	 */
	public File normalize(File file){
		System.out.println("Normalize "+file.getName());
		File outF = new File(file.getParent()+"\\N_"+file.getName());
		try{
			 int i=0;
             String line = "";
             double sum = 0.0;
             BufferedReader r1 = BF.readFile(file);
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
                 double score = Double.valueOf(words[1]);
                 sum += Math.pow(score, 2);
             }
             sum = Math.sqrt(sum);
             r1 = BF.readFile(file);
             i=0;
             BufferedWriter out = new BufferedWriter(new FileWriter(outF));
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
                 double score = Double.valueOf(words[1]);
                 score = score/sum;
                 out.write(userId+","+score+"\n");
             }
             out.close();
             r1.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println();
		return outF;
	}
	public File combineFeatures(String path){
	    	try{
	    		 TreeMap<Integer,String> userFeatures = new TreeMap<Integer,String>();
	    		 
	             File outFile = new File(path+"\\FeaturesCombine.csv");
	             BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
	            
	             long st = System.currentTimeMillis();
	             File FeaturesPath = new File(path+"combine\\");
	             
	             File[] featureFiles = FeaturesPath.listFiles();
	             for(File f:featureFiles){
	            	 int i=0;
	            	 File ff = normalize(f);
	            	 BufferedReader r1 = BF.readFile(ff);
	            	 
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
		
		s = dMin/(dMax+dMin);
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
//public File Normalized(File featuresFile){//对FileName中的每一列都标准化
//try{
//	BufferedReader read = BF.readFile(featuresFile);
//	String line = "";
//	int i=0;
//	TreeMap<Integer,Double> userACQMap = new TreeMap<Integer,Double>();
//	ArrayList<Double> normList = new ArrayList<Double>();//用来存储平方和，用于标准化
//	while((line = read.readLine())!=null){
//		
//		 i++;
//		if(i%100000==0){
//			System.out.print(i/100000+" ");
//			if(i/100000%25==0){
//				System.out.println();
//			}
//		}
//		String[] words = line.split(",");
//		int id = Integer.valueOf(words[0]);//第一个字段为UserID
//		for(int j=1;j<words.length;j++){
//			double fj = Double.valueOf(words[j]);
//			if(i==0){//第一行，初始化normList
//				normList.add(Math.pow(fj, 2));
//			}
//			else{
//				double tempNorm = normList.get(j-1);
//				tempNorm += Math.pow(fj, 2);
//				normList.set(j-1, tempNorm);//平方和
//			}
//		}
//	
//	}
//	for(int j=0;j<normList.size();j++){
//		double norm = normList.get(j);
//		norm = Math.sqrt(norm);//开方
//		normList.set(j, norm);
//	}
//	File outFile = new File(featuresFile.getParent()+"\\Norm_"+featuresFile.getName());
//	BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
//	read = BF.readFile(featuresFile);
//	while((line = read.readLine())!=null){
//		 i++;
//			if(i%100000==0){
//				System.out.print(i/100000+" ");
//				if(i/100000%25==0){
//					System.out.println();
//				}
//			}
//		String[] words = line.split(",");
//		int id = Integer.valueOf(words[0]);//第一个字段为UserID
//		out.write(id);
//		for(int j=1;j<words.length;j++){
//			double fj = Double.valueOf(words[j]);
//			double norm = normList.get(j-1);
//			fj = fj/norm;//标准化
//			out.write(","+fj);
//		}
//		out.write("\n");
//	}
//	System.out.println(userACQMap.size());
//	out.close();
//	return outFile;
//}catch(Exception e){
//	e.printStackTrace();
//}
//return null;
//}
