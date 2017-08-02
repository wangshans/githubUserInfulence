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


public class ACQR {

	/**
	 * @param args
	 */
	/* f1:originalReposNum+userCommentsNum+userCommitsNum+userPullRequestNum+userIssueNum
	 * f2:userFollowersNum
	 * f3:userPageRankScore
	 * f4:userForksNum+userWatchersNum
	 */

	public void userSumNum(){//参数为需要求和的特征名称列表
		String line = null;
		
		try{
			TreeMap<Integer,Double> userNumMap = new TreeMap<Integer,Double>();
			File FeaturesPath = new File(Constants.path+"sum\\");
            
            File[] features = FeaturesPath.listFiles();
            String title = "id";
            for(File f:features){
           	  title = title +","+f.getName();
            }
            
            for(File f:features){
            	int i=0;
				BufferedReader r1 = BF.readFile(f);
				while((line = r1.readLine())!=null){
	    			i++;
	    			if(i%100000==0){
	    				System.out.print(i/100000+" ");
	    				if(i/100000%25==0){
	    					System.out.println();
	    				}
	    			}
	    			String[] words = line.split(",");
	    			
	    			int id = Integer.valueOf(words[0]);
	    			Double num = Double.valueOf(words[1]);
	    			Double c = userNumMap.get(id);
	    			if(c==null){
	    				c=0.0;
	    			}
	    			c += num;
	    			userNumMap.put(id, c);
	    			
				}
				System.out.println();
				r1.close();
        	}
    		System.out.println("\n users number : "+userNumMap.size());
    		File outFile = new File(Constants.path+"FeatureSum.csv");
    		BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
    		for(Map.Entry<Integer, Double> t:userNumMap.entrySet()){
    			int rid =t.getKey();
    			out.write(rid+","+t.getValue()+"\n");
    		}
    		out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	//归一化，normalized
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
	  public void combineFeatures(){
	    	try{
	    		 TreeMap<Integer,String> userFeatures = new TreeMap<Integer,String>();
	    		 
	             File outFile = new File(Constants.path+"FeaturesCombine.csv");
	             BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
	            
	             long st = System.currentTimeMillis();
	             File FeaturesPath = new File(Constants.path+"combine\\");
	             
	             File[] features = FeaturesPath.listFiles();
	             for(File f:features){
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
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
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
	public void getACQR(String fileName){
		try{
			File ACQRFile = new File(fileName);
			BufferedReader read = BF.readFile(ACQRFile);
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
				double f1 = Double.valueOf(words[1]);
				double f2 = Double.valueOf(words[2]);
				double f3 = Double.valueOf(words[3]);
				double f4 = Double.valueOf(words[4]);
				ArrayList<Double> fList = new ArrayList<Double>();
				fList.add(f1);
				fList.add(f2);
				fList.add(f3);
				fList.add(f4);
				double si = getSi(fList);
//				if(id==1488){
//					System.out.println(f1+","+f2+","+f3+","+f4);
//					System.out.println(si);
//					break;
//				}
				userACQMap.put(id, si);
			//	break;
			}
			System.out.println(userACQMap.size());
			List<Map.Entry<Integer, Double>> list=
			    new ArrayList<Map.Entry<Integer, Double>>(userACQMap.entrySet());

			//排序
			Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {   
			    public int compare(Map.Entry<Integer, Double> m1, Map.Entry<Integer,Double> m2) {     
			    	return (m2.getValue().compareTo(m1.getValue()));
			//m1-m2     递增顺序
			//m2-m1     递减顺序
			    }
			});
			BufferedWriter out = new BufferedWriter(
					new FileWriter(ACQRFile.getParent()+"\\ACQResult.csv"));
			for(Map.Entry<Integer, Double> m:list){
				out.write(m.getKey()+","+m.getValue()+"\n");
			}
			out.close();
		}catch(Exception e){
			
		}
	}
	public void getACQR2(String fileName){
		try{
			File ACQRFile = new File(fileName);
			BufferedReader read = BF.readFile(ACQRFile);
			String line = "";
			int i=0;
			TreeMap<Integer,ArrayList<Double>> userACQMap = new TreeMap<Integer,ArrayList<Double>>();
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
				double f1 = Double.valueOf(words[1]);
				double f2 = Double.valueOf(words[2]);
				double f3 = Double.valueOf(words[3]);
				double f4 = Double.valueOf(words[4]);
				ArrayList<Double> fList = new ArrayList<Double>();
				fList.add(f1);
				fList.add(f2);
				fList.add(f3);
				fList.add(f4);
				double si = getSi(fList);

				fList.add(si);
				userACQMap.put(id, fList);
			//	break;
			}
			System.out.println(userACQMap.size());
			List<Map.Entry<Integer, ArrayList<Double>>> list=
			    new ArrayList<Map.Entry<Integer, ArrayList<Double>>>(userACQMap.entrySet());

			//排序
			Collections.sort(list, new Comparator<Map.Entry<Integer, ArrayList<Double>>>() {   
			    public int compare(Map.Entry<Integer, ArrayList<Double>> m1,
			    		Map.Entry<Integer,ArrayList<Double>> m2) {  
			    	ArrayList<Double> tempList1 = m1.getValue();
			    	ArrayList<Double> tempList2 = m2.getValue();
			    	int[] fs = {4,2,1,3,0};
			    	int k=0;
			    	for(int j:fs){
			    		k=j;
			    		if(tempList2.get(j)!=tempList1.get(j)){
				    		return tempList2.get(j).compareTo(tempList1.get(j));
				    	}
			    		
			    	}
			    	return tempList2.get(k).compareTo(tempList1.get(k));
			//m1-m2     递增顺序
			//m2-m1     递减顺序
			    }
			});
			BufferedWriter out = new BufferedWriter(
					new FileWriter(ACQRFile.getParent()+"\\ACQResult.csv"));
			for(Map.Entry<Integer, ArrayList<Double>> m:list){
				out.write(m.getKey());
				ArrayList<Double> tempList = m.getValue();
				for(double d:tempList){
					out.write(","+d);
				}
				out.write("\n");
			}
			out.close();
		}catch(Exception e){
			
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ACQR a = new ACQR();
		a.userSumNum();
	//	a.combineFeatures();
	//	String file = a.path+"ACQ.csv";
//		a.getACQR(file);
		//System.out.println(Math.sqrt(9));
	}

}
