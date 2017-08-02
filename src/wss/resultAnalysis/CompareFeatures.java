package wss.resultAnalysis;

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

public class CompareFeatures {

	/**
	 * @param args
	 */
	// File userInfoFile = new File(Constants.screenFile+"features\\ORG\\userInfo.csv");
	public File sortFileEquallyInt(File f){//对于分值相同的用户取最靠前排名为其排名值
		try{
			System.out.print("sort--"+f.getName()+" ");
         	BufferedReader reader = BF.readFile(f);
     		String line = "";
     		String[] words = null;
     		TreeMap<Integer,Integer> userScoreMap = new TreeMap<Integer,Integer>();
     		while((line=reader.readLine())!=null){
     			if(line.contains("id")){
     				continue;
     			}
     			words = line.split(",");
     			int score = Integer.valueOf(words[1]);
     			userScoreMap.put(Integer.valueOf(words[0]),score);
     		}
     //		System.out.println("\tTotal Number = "+userScoreMap.size()+"++++++++");
			List<Map.Entry<Integer, Integer>> list=
			    new ArrayList<Map.Entry<Integer, Integer>>(userScoreMap.entrySet());

			//排序
			Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {   
			    public int compare(Map.Entry<Integer, Integer> m1, Map.Entry<Integer, Integer> m2) {     
			        //return (m2.getValue() - m1.getValue());
			        return (m2.getValue() - m1.getValue());
			//m1-m2     递增顺序
			//m2-m1     递减顺序
			    }
			});
		
			File outFile = new File(f.getParent()+"\\rank\\rank_"+f.getName());
			BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
			int i=0;
			ArrayList<Integer> scoreList = new ArrayList<Integer>();
			for(Map.Entry<Integer, Integer> m:list){
				i++;
				if(i%100000==0){
    				System.out.print(i/100000+" ");
    				if(i/100000%25==0){
    					System.out.println();
    				}
    				out.flush();
    			}
				int user = m.getKey();
				Integer score = m.getValue();
				int rank=i;
				if(scoreList.contains(score)){
					
				}else{
					scoreList.add(score);
				}
				rank = scoreList.indexOf(score)+1;
				out.write(user+","+score+","+rank+"\n");
			}
			out.close();
			System.out.println("\tsorted ");
			return outFile;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public File sortFileDouble(File f){//对于分值相同的用户取最靠前排名为其排名值
		try{
			System.out.print("sort--"+f.getName()+" ");
         	BufferedReader reader = BF.readFile(f);
     		String line = "";
     		String[] words = null;
     		TreeMap<Integer,Double> userScoreMap = new TreeMap<Integer,Double>();
     		while((line=reader.readLine())!=null){
     			if(line.contains("id")){
     				continue;
     			}
     			words = line.split(",");
     			Double score = Double.valueOf(words[1]);
     			userScoreMap.put(Integer.valueOf(words[0]),score);
     		}
     //		System.out.println("\tTotal Number = "+userScoreMap.size()+"++++++++");
			List<Map.Entry<Integer, Double>> list=
			    new ArrayList<Map.Entry<Integer, Double>>(userScoreMap.entrySet());

//			//排序
			Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {   
			    public int compare(Map.Entry<Integer, Double> m1, Map.Entry<Integer, Double> m2) {     
			        //return (m2.getValue() - m1.getValue());
			        return(m2.getValue().compareTo(m1.getValue()));
			//m1-m2     递增顺序
			//m2-m1     递减顺序
			    }
			});
		
			File outFile = new File(f.getParent()+"\\rank\\rank_"+f.getName());
			BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
			int i=0;
			ArrayList<Double> scoreList = new ArrayList<Double>();
			for(Map.Entry<Integer, Double> m:list){
				i++;
				if(i%100000==0){
    				System.out.print(i/100000+" ");
    				if(i/100000%25==0){
    					System.out.println();
    				}
    				out.flush();
    			}
				int user = m.getKey();
				Double score = m.getValue();
				int rank=i;
				if(scoreList.contains(score)){
					
				}else{
					scoreList.add(score);
				}
				rank = scoreList.indexOf(score)+1;
				out.write(user+","+score+","+rank+"\n");
			}
			out.close();
			System.out.println("\tsorted ");
			return outFile;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	TreeMap<Integer,String> userInfoMap = new TreeMap<Integer,String>();
	public void readUserInfo(File toSort){
		try{
			String line="";
			int i=0;
			String[] words = null;
			File usersInfoFile = new File(new File(toSort.getParent())+"\\usersInfo.csv");
	        System.out.print("Read "+usersInfoFile.getName()+" ");
	        BufferedReader r1 = BF.readFile(usersInfoFile);
			while((line = r1.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
				    if(i/100000%25==0){
				        System.out.println();
				    }
				}
				words = line.split(",");
				if(words[0].contains("id")){
				    continue;
				}
				int userId = Integer.valueOf(words[0]);
				userInfoMap.put(userId, words[1]);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public File sortInfo(File f){//对于分值相同的用户取最靠前排名为其排名值
		String line = "";
		try{
			
			int i=0;
			String[] words = null;
//			TreeMap<Integer,String> userInfoMap = new TreeMap<Integer,String>();
//			File usersInfoFile = new File(new File(f.getParent()).getParent()+"\\usersInfo.csv");
//	        System.out.print("Read "+usersInfoFile.getName()+" ");
//	        BufferedReader r1 = BF.readFile(usersInfoFile);
//			while((line = r1.readLine())!=null){
//				i++;
//				if(i%100000==0){
//					System.out.print(i/100000+" ");
//				    if(i/100000%25==0){
//				        System.out.println();
//				    }
//				}
//				words = line.split(",");
//				if(words[0].contains("id")){
//				    continue;
//				}
//				int userId = Integer.valueOf(words[0]);
//				userInfoMap.put(userId, words[1]);
//			}
			System.out.println("");
			System.out.print("sort--"+f.getName()+" ");
		
			TreeMap<Integer,Double> userFeatureMap = new TreeMap<Integer,Double>();
         	BufferedReader reader = BF.readFile(f);
     		while((line=reader.readLine())!=null){
     		//	System.out.println(line);
     			words = line.split(",");
     			if(words[0].contains("id")){
     				continue;
     			}
     			Integer id = Integer.valueOf(words[0]);
     			
     			if(userInfoMap.get(id)!=null){
     				Double score = Double.valueOf(words[1]);
         	//		scoreList.add(score);
         			userFeatureMap.put(id, score);
//         			System.out.println(line+" ");
//         			System.out.println(id+" ");
     			}
     		}
//			//排序
     		reader.close();
     		ArrayList<Map.Entry<Integer,Double>> list = new ArrayList<Map.Entry<Integer, Double>>(userFeatureMap.entrySet());
     		Collections.sort(list, new Comparator<Map.Entry<Integer,Double>>() {   
			    public int compare(Map.Entry<Integer,Double> m1,Map.Entry<Integer,Double> m2) {     
			        //return (m2.getValue() - m1.getValue());
			        return(m2.getValue().compareTo(m1.getValue()));
			    }
			});
     		
     		System.out.println("sorted,  writing...");
//		
			File outFile = new File(new File(f.getParent()).getParent()+"\\sortInfo\\"+f.getName());
			BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
			out.write("id,login,"+f.getName().replace(".csv","")+",rank\n");
			i=0;
			ArrayList<Double> scoreList = new ArrayList<Double>();
			for(Map.Entry<Integer, Double> m:list){
				i++;
				if(i%100000==0){
    				System.out.print(i/100000+" ");
    				if(i/100000%25==0){
    					System.out.println();
    				}
    				out.flush();
    			}
				Integer userID = m.getKey();
				
				Double score = m.getValue();
				int rank=i;
				if(!scoreList.contains(score)){
					scoreList.add(score);
				}
				rank = scoreList.indexOf(score)+1;
				out.write(userID+","+userInfoMap.get(userID)+","+score+","+rank+"\n");
			}
			out.close();
			System.out.println("Completed");
			return outFile;
		}catch(Exception e){
			System.out.println(line);
			e.printStackTrace();
		}
		return null;
	}
	
	public File addRank(File f){//已经是排好序的，在每行最后一个字段加上rank
		try{
			System.out.print("sort--"+f.getName()+" ");
         	BufferedReader reader = BF.readFile(f);
     		String line = "";
     		String[] words = null;
     		File outFile = new File(f.getParent()+"\\rank\\rank_"+f.getName());
			BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
			int i=0;
			ArrayList<String> scoreList = new ArrayList<String>();
			long s = System.currentTimeMillis();
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
    				out.flush();
    				long e = System.currentTimeMillis();
    				System.out.println("Time ... "+(e-s)/1000);
    			}
     			words = line.split(",");
				if(!scoreList.contains(words[1])){
					scoreList.add(words[1]);
				}
			//	int rank = scoreList.indexOf(words[1])+1;
				int rank = scoreList.size();
				out.write(words[0]+","+words[1]+","+rank+"\n");
     		}
     //		System.out.println("\tTotal Number = "+userScoreMap.size()+"++++++++");
			out.close();
			System.out.println("\tsorted ");
			return outFile;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	 public void addUserInfo(File file){
	      String line = "";
	      String[] words=null;
	      int i=0;
	      try{
	          TreeMap<Integer,String> userFeaturesMap = new TreeMap<Integer,String>();
	     //     String FeaturesPath = Constants.screenFile+"features\\";
	          File userInfoFile = new File(Constants.screenFile+"features\\userInfo.csv");
	          
	          System.out.print("Read "+userInfoFile.getName()+" ");
	          BufferedReader r1 = BF.readFile(userInfoFile);
			  File outFile = new File(file.getParent()+"\\"+file.getName().replaceAll("rank_", ""));
			  BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
			  while((line = r1.readLine())!=null){
				  i++;
				  if(i%100000==0){
					  System.out.print(i/100000+" ");
				      if(i/100000%25==0){
				          System.out.println();
				      }
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
			  out.write(outFile.getName().replaceAll(".csv", "")+",rank\n");
			  i = 0;
			  System.out.println("Read "+file.getName());
			  r1 = BF.readFile(file);
			  String tempF = "";
			  while((line = r1.readLine())!=null){
				   i++;
				   tempF = "";
				   if(i%100000==0){
					   System.out.print(i/100000+" ");
				       if(i/100000%25==0){
				           System.out.println();
				       }
				   }
				   words = line.split(",");
				   int userId = Integer.valueOf(words[0]);
				   if(words.length>2){
                  	 for(int j=1;j<words.length;j++){
                      	 tempF = tempF+","+words[j];
                       }
                   }else{
                  	 tempF = ","+words[1];
                   }
				   String features = userFeaturesMap.get(userId);
				   if(features==null)//原来的用户列表里没有这个用户，则舍弃
				         continue;
				   out.write(features+tempF+"\n");
				   userFeaturesMap.remove(userId);
			   }
			   System.out.println("Read "+file.getName()+" completed!");
			   out.close();
			  file.delete();
	      }catch(Exception e){
	          System.out.println(line);
	          e.printStackTrace();
	      }
	}
	 public void addUserInfoOrgUsr(File file){
	      String line = "";
	      String[] words=null;
	      int i=0;
	      try{
	          TreeMap<Integer,String> userORGFeaturesMap = new TreeMap<Integer,String>();
	          TreeMap<Integer,String> userUSRFeaturesMap = new TreeMap<Integer,String>();
	     //     String FeaturesPath = Constants.screenFile+"features\\";
	          
			  File outFileORG = new File(file.getParent()+"\\ORG"+file.getName().replaceAll("rank_", ""));
			  BufferedWriter outORG = new BufferedWriter(new FileWriter(outFileORG));
			  File outFileUSR = new File(file.getParent()+"\\USR"+file.getName().replaceAll("rank_", ""));
			  BufferedWriter outUSR = new BufferedWriter(new FileWriter(outFileUSR));
			  
			  File userInfoFile = new File(Constants.screenFile+"features\\userInfo.csv");
	          
	          System.out.print("Read "+userInfoFile.getName()+" ");
	          BufferedReader r1 = BF.readFile(userInfoFile);
			  while((line = r1.readLine())!=null){
				  i++;
				  if(i%100000==0){
					  System.out.print(i/100000+" ");
				      if(i/100000%25==0){
				          System.out.println();
				      }
				  }
				  words = line.split(",");
				  if(words[0].contains("id")){
					  outORG.write(line+",");
					  outUSR.write(line+",");
				      continue;
				  }
				  int userId = Integer.valueOf(words[0]);
				  Integer type = Integer.valueOf(words[2]);
				  if(type==1){
					  userUSRFeaturesMap.put(userId, line);
				  }else{
					  userORGFeaturesMap.put(userId, line);
				  }
				 
			  }
			   //    int featuresNum = words[1].split(" ").length;
			  outORG.write(outFileORG.getName().replaceAll(".csv", "")+",rank\n");
			  outUSR.write(outFileUSR.getName().replaceAll(".csv", "")+",rank\n");
			  i = 0;
			  System.out.println("Read "+file.getName());
			  r1 = BF.readFile(file);
			  String tempF = "";
			  while((line = r1.readLine())!=null){
				   i++;
				   tempF = "";
				   if(i%100000==0){
					   System.out.print(i/100000+" ");
				       if(i/100000%25==0){
				           System.out.println();
				       }
				   }
				   words = line.split(",");
				   Integer userId = Integer.valueOf(words[0]);
				   if(words.length>2){
                 	 for(int j=1;j<words.length;j++){
                     	 tempF = tempF+","+words[j];
                      }
                  }else{
                 	 tempF = ","+words[1];
                  }
				   String features = userORGFeaturesMap.get(userId);
				   if(features==null){
					 features = userUSRFeaturesMap.get(userId);
					 if(features==null){
						 continue;
					 }
					  outUSR.write(features+tempF+"\n");
					  continue;
				   }
				   outORG.write(features+tempF+"\n");
				 //  userFeaturesMap.remove(userId);
			   }
			   System.out.println("Read "+file.getName()+" completed!");
			   outORG.close();
			   outUSR.close();
			  file.delete();
	      }catch(Exception e){
	          System.out.println(line);
	          e.printStackTrace();
	      }
	}
	 public void splitToUsrOrg(File f){//将一个文件按照
		 
	 }
	String path = Constants.dirname+"dump\\";
	public static void main(String[] args){
		// TODO Auto-generated method stub
		CompareFeatures c = new CompareFeatures();
	//	File f = new File(Constants.screenFile+"features\\users\\userFollowersNum.csv");
		File toSort = new File(c.path+"features\\USR\\toSort\\");
		File[] files = toSort.listFiles();
		int i=0;
		c.userInfoMap.clear();
		c.readUserInfo(toSort);
		for(File f:files){
			i++;
			System.out.println(i+" : ");
			if(f.isDirectory()){
				continue;
			}
		//	File f2 = c.sortFileEquallyInt(f);
			c.sortInfo(f);
		//	File f2 = c.addRank(f);
		//	File f2 =new File("D:\\mysql-2017-01-19\\screen\\features\\users\\rank\\rank_userPageRankScore.csv");
		//	c.addUserInfo(f2);
		//	c.addUserInfoOrgUsr(f2);
		//	f2.delete();
		}
	//	File f2 = c.sortInfo(new File(Constants.dirname+"dump\\features\\ORG\\usersWatcherHindex.csv"));
	}

}
