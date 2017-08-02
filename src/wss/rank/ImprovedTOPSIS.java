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
import wss.githubFeatures.Features;

public class ImprovedTOPSIS {

	/**
	 * @param args
	 */
	public File normalize(File file){//使用log进行标准化
		System.out.println("\nNormalize "+file.getName());
		File outF = new File(file.getParent()+"\\N_"+file.getName());
		try{
			 int i=0;
             String line = "";
             double max = 0.0;
             BufferedReader r1 = BF.readFile(file);
             System.out.print("\tmax....");
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
                 if(max<score){
                	 max = score;
                 }
             }
             System.out.println();
             r1 = BF.readFile(file);
             i=0;
             BufferedWriter out = new BufferedWriter(new FileWriter(outF));
             System.out.print("\tcompute....");
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
                 double newScore = Math.log(1+score)/Math.log(1+max);
                 out.write(userId+","+newScore+"\n");
             }
         //    System.out.println();
             out.close();
             r1.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println();
		return outF;
	}
	public File combineFeatures(File FeaturesPath){
    	try{
    		 TreeMap<Integer,String> userFeatures = new TreeMap<Integer,String>();
    		 
             File outFile = new File(FeaturesPath.getParent()+"\\TOPSIS\\FeaturesCombine"+n+".csv");
             BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
            
             long st = System.currentTimeMillis();
             String title = "id";
             File[] featureFiles = FeaturesPath.listFiles();
             for(File f:featureFiles){
            	 int i=0;
            	 File ff = normalize(f);
            	 BufferedReader r1 = BF.readFile(ff);
            	 title = title+","+f.getName().replaceAll(".csv", "");
                 String line = "";
                 System.out.print("\tcombine....");
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
//	                    	 System.out.print("\tline...."+line);
//	                    	 break;
                     }else{
                    	  userFeatures.put(userId,(feature+","+temp));
                     }
                   
                     
                 }
                 System.out.println();
                 r1.close();
                 long et = System.currentTimeMillis();
                 long time = (et-st)/1000;
             }
             
            out.write(title+"\n");
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
	public double AverageF(ArrayList<Double> list){
		double Af = 0;
	
		for(double f:list){
			Af += f;
		}
		return Af/list.size();
	}
	public double StandardF(ArrayList<Double> list){
		double sf = 0;
		double af = AverageF(list);
		for(double f:list){
			sf += Math.pow((af-f), 2);//平方和
		}
		sf = Math.sqrt(sf);//开方
		sf = sf/list.size();
		return sf;
	}
	public double getSi(ArrayList<Double> list){
		double s=0.0;
		double dMin = dMin(list);
		double dMax = dMax(list);
		if(dMax+dMin==0){
			return 0;
		}
	//	System.out.println(dMax+" "+dMin);
		double sf = StandardF(list);
		double af = AverageF(list);
		s = (1-(sf/af))*dMin/(dMax+dMin);
		return s;
	}

	public File getTOPSIS(File FeatureFile){
		String line = "";
		try{
			BufferedReader read = BF.readFile(FeatureFile);
			
			int i=0;
			TreeMap<Integer,Double> userACQMap = new TreeMap<Integer,Double>();
			while((line = read.readLine())!=null){
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
			File outFile = new File(FeatureFile.getParent()+"\\TOPSISResult"+n+".csv");
			BufferedWriter out = new BufferedWriter(
					new FileWriter(outFile));
			for(Map.Entry<Integer, Double> m:list){
				out.write(m.getKey()+","+m.getValue()+"\n");
			}
			out.close();
			return outFile;
		}catch(Exception e){
			System.out.println();
			e.printStackTrace();
		}
		return null;
	}
	public void getUserInfo(File f){
		try{
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	   public void addFeature(File featureFile){
		      String line = "";
		      String[] words=null;
		      int i=0;
		      try{
		          TreeMap<Integer,String> userFeaturesMap = new TreeMap<Integer,String>();
		     //     String FeaturesPath = Constants.screenFile+"features\\";
		          File userFeaturesFile = new File(Constants.screenFile+"features\\userFeatures.csv");
		          if(!userFeaturesFile.exists()){
		        	  System.out.println("userFeatures不存在");
		          }else{
		          	 	System.out.println("Read "+userFeaturesFile.getName());
		               BufferedReader r1 = BF.readFile(userFeaturesFile);
		               File userFeatures2File = new File
		               (Constants.screenFile+"features\\TOPSIS\\userFeatures"+n+".csv");
		               BufferedWriter out = new BufferedWriter(new FileWriter(userFeatures2File));
		               while((line = r1.readLine())!=null){
		            	   i++;
		            	   if(i%100000==0){
		            		   System.out.print(i/100000+" ");
//		            		   if(i/100000%10==0){
//		            			   System.out.println();
//		            		   }
		            	   }
		                   words = line.split(",");
		                   if(words[0].contains("id")){
		                	   out.write(line+",");
		                	   continue;
		                   }
		                   int userId = Integer.valueOf(words[0]);
		                   userFeaturesMap.put(userId, line);
		               }
		           //    int featuresNum = words[1].split(" ").length;
		               System.out.println(userFeaturesFile.getName()+" 涉及到的用户人数："+userFeaturesMap.size());
		               System.out.println("Read "+userFeaturesFile.getName()+" completed!");
		              out.write(featureFile.getName().replaceAll(".csv", "")+"\n");
		               i = 0;
		               System.out.println("Read "+featureFile.getName());
		               r1 = BF.readFile(featureFile);
		               while((line = r1.readLine())!=null){
		            	   i++;
		            	   if(i%100000==0){
		            		   System.out.print(i/100000+" ");
//		            		   if(i/100000%10==0){
//		            			   System.out.println();
//		            		   }
		            	   }
		                   words = line.split(",");
		                   int userId = Integer.valueOf(words[0]);
		                   String features = userFeaturesMap.get(userId);
		                   if(features==null)//原来的用户列表里没有这个用户，则舍弃
		                       continue;
		               //    System.out.println(line);
		                   out.write(features+","+words[1]+"\n");
		                   userFeaturesMap.remove(userId);
		               }
		               System.out.println("Read "+featureFile.getName()+" completed!");
		               out.close();
		          }
		      }catch(Exception e){
		          System.out.println(line);
		          e.printStackTrace();
		      }
		  }
	int n=2;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ImprovedTOPSIS t = new ImprovedTOPSIS();
		try{
			File featuresPath = new File(Constants.screenFile+"features\\users");
			File features = t.combineFeatures(featuresPath);
		//	File features = new File(Constants.screenFile+"");
			File f = t.getTOPSIS(features);
			t.addFeature(f);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}