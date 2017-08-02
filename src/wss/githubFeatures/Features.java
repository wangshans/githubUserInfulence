package wss.githubFeatures;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import utils.BF;
import utils.Constants;

public class Features {

    /**该类用于合并各个feature，
     * 并创建对应的arff 格式的文件
     * @param args
     */

    public void userSumNum(File FeaturesPath){//参数为需要求和的特征名称列表
		String line = null;
		try{
			TreeMap<Integer,Double> userNumMap = new TreeMap<Integer,Double>();
            
            File[] features = FeaturesPath.listFiles();
            String title = "id";
            for(File f:features){
           	  title = title +","+f.getName();
            }
            title = title.replaceAll(".csv", "");
            title = title.replaceAll("user", "");
            int fi=0;
            for(File f:features){
            	fi++;
            	int i=0;
            	System.out.print(fi+" "+f.getName()+":\t");
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
    		File outFile = new File(FeaturesPath.getParent()+"\\Sum"+FeaturesPath.getName()+".csv");
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
    public void userSmplify(){//简化用户基本信息，保留id，login,
    	File file = new File(Constants.screenFile + "tables\\users.csv");
    	System.out.println("read  "+file+", waiting......");
		String line = null;
        try{
        	File outFile = new File(Constants.screenFile+"features\\userFeatures.csv");
            PrintWriter out = new PrintWriter(new BufferedWriter(
            		new OutputStreamWriter(new FileOutputStream(outFile),"utf-8")));
            out.write("id,login\n");
        	BufferedReader reader = new BufferedReader(new FileReader(file),5*1024*1024);//如果是读大文件  则设置缓存
    		int i=0,count=0;
    	//	line=reader.readLine();
    		while((line=reader.readLine())!=null){
    			i++;
    			if(i%100000==0){
    				System.out.print(i/100000+" ");
    				if(i/100000%25==0){
    					System.out.println();
    				}
    			}
    			String[] words = line.split(",");
    			ArrayList<String> list = new ArrayList<String>();// 每行记录一个list  
    			String tempstr = "";
    			for(int j=0;j<words.length;j++){
    				tempstr += words[j];
    				if(tempstr.contains("\"")){//包含引号
    					if(tempstr.startsWith("\"")){//引号开头
    						if(tempstr.endsWith("\"")&&tempstr.length()>1){//结尾也为引号
								if(tempstr.endsWith("\\\"")){//结尾的引号不是转义字符加引号，即为真实的引号
									//结尾引号可能为转义引号
									if(tempstr.indexOf("\\\\\"")!=-1){//结尾为\\"这种形式，即转义的是\\
										list.add(tempstr);
										tempstr = "";
									}else if(BF.countZi(tempstr,"\\\"")%2!=0){//含有单数个\"
										list.add(tempstr);
										tempstr = "";
										continue;
									}else{
										tempstr +=",";
		    							continue;
									}
								}else{
									list.add(tempstr);
									tempstr = "";
									continue;
								}
	    					}else{//引号开头，非引号结尾，连接下一个子串
    							tempstr +=",";
    							continue;
	    					}
    				}
    			}else{//不含引号
					if(tempstr.equals("\\N")){
						list.add(tempstr);
						tempstr = "";
					}else{
						list.add(tempstr);
						tempstr = "";
					}
				}
    		}
    			
			if(list.size()!=13){
				System.out.println("\n"+list.size()+" i:"+i);
				System.out.println(line);
				for(String s:list){
					System.out.println(s);
				}
				break;
			}
			out.write(list.get(0)+","+list.get(1)+"\n");
		}
    	out.flush();
    	out.close();
    	System.out.println("\n 筛选后用户后的用户数量为："+count);
        }catch(Exception e){
			System.out.println(line);
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
               File userFeatures2File = new File(Constants.screenFile+"features\\userFeatures2.csv");
               BufferedWriter out = new BufferedWriter(new FileWriter(userFeatures2File));
               while((line = r1.readLine())!=null){
            	   i++;
            	   if(i%100000==0){
            		   System.out.print(i/100000+" ");
//            		   if(i/100000%10==0){
//            			   System.out.println();
//            		   }
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
//            		   if(i/100000%10==0){
//            			   System.out.println();
//            		   }
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
    public void addFeatures(File featuresPath){
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
                 File userFeatures2File = new File(Constants.screenFile+"features\\userFeatures2.csv");
                 BufferedWriter out = new BufferedWriter(new FileWriter(userFeatures2File));
                 while((line = r1.readLine())!=null){
                	 
                	 i++;
                	 if(i%100000==0){
                		 System.out.print(i/100000+" ");
                	 }
                	 words = line.split(",");
                	 if(words[0].contains("id")){
                		 out.write(line);
                		 continue;
                     }
                     int userId = Integer.valueOf(words[0]);
                     userFeaturesMap.put(userId, line);
                 }
             //    int featuresNum = words[1].split(" ").length;
                 System.out.println(userFeaturesFile.getName()+" 涉及到的用户人数："+userFeaturesMap.size());
                 System.out.println("Read "+userFeaturesFile.getName()+" completed!");
                 File[] files = featuresPath.listFiles();
                 for(File featureFile :files){
                	 out.write(","+featureFile.getName().replaceAll(".csv", ""));
                 }
                 out.write("\n");
                 for(File featureFile :files){
                	 i = 0;
                     System.out.print("Read "+featureFile.getName()+" ... ");
                     r1 = BF.readFile(featureFile);
                     while((line = r1.readLine())!=null){
		              	 i++;
		              	 if(i%100000==0){
		              		 System.out.print(i/100000+" ");
		              	 }
		                 words = line.split(",");
		                 int userId = Integer.valueOf(words[0]);
                         String features = userFeaturesMap.get(userId);
                         if(features==null)//原来的用户列表里没有这个用户，则舍弃
                             continue;
                         
                         userFeaturesMap.put(userId,features+","+words[1]);
                     }
                     System.out.println("Read "+featureFile.getName()+" completed!");
                 }
                 for(Map.Entry<Integer, String> m:userFeaturesMap.entrySet()){
                	 out.write(m.getKey()+","+m.getValue()+"\n");
                 }
                 out.close();
            }
        }catch(Exception e){
            System.out.println(line);
            e.printStackTrace();
        }
    }
    public void combineFeatures(String entity){
    	try{
    		 TreeMap<Integer,String> userFeatures = new TreeMap<Integer,String>();
    		 File FeaturesPath = new File(Constants.screenFile+"features\\"+entity+"\\");
            
             File[] features = FeaturesPath.listFiles();
             String title = "id";
             for(File f:features){
            	  title = title +","+f.getName();
             }
             File outFile = new File(Constants.screenFile+"features\\"+entity+"Features.csv");
             BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
             title = title.replaceAll(".csv", "");
             title = title.replaceAll(entity+"s", "");
             title = title.replaceAll(entity, "");
             out.write(title+"\n");
             out.flush();
             int i=0;
             long st = System.currentTimeMillis();
             for(File f:features){
            	 i++;
            	System.out.print(i+": ");
            	 BufferedReader r1 = BF.readFile(f);
            	
                 String line = "";
                 
                
                 title = title +","+f.getName();
                 int u = 0;//第c行
                 while((line = r1.readLine())!=null){
                     String[] words = line.split(",");
                     int userId = Integer.valueOf(words[0]);
                     String temp = words[1];
                     String feature =  userFeatures.get(userId);
                     if(feature==null){
                    	 userFeatures.put(userId, temp);
                     }else{
                    	 userFeatures.put(userId, (feature+","+temp));
                     }
                     
                 }
                 r1.close();
                 long et = System.currentTimeMillis();
                 long time = (et-st)/1000;
                 System.out.println("\t"+entity+" Features size : "+userFeatures.size());
             }
             
            
             for(Map.Entry<Integer, String> m:userFeatures.entrySet()){
            	 out.write(m.getKey()+","+m.getValue()+"\n");
             }
             out.close();
             System.out.println("title: "+title);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public int combineFeatures(String entity,int startID,int num){
    	//entity要整合特征的实体
    	//startID 起始ID,包括该ID
    	//num 数量
    	int endID = 0;
    	try{
    		System.out.println(entity + " : "+startID+" -- ");
    		 TreeMap<Integer,String> userFeatures = new TreeMap<Integer,String>();
    		 File FeaturesPath = new File(Constants.path+"features\\"+entity+"\\");
            
             File[] features = FeaturesPath.listFiles();
            
             File outFile = new File(Constants.path+"features\\"+entity+"Features.csv");
             BufferedWriter out = new BufferedWriter(new FileWriter(outFile,true));
             if(startID==1){
	             String title = "id";
	             for(File f:features){
	            	  title = title +","+f.getName();
	             }
	             title = title.replaceAll(".csv", "");
	             title = title.replaceAll(entity+"s", "");
	             title = title.replaceAll(entity, "");
	             out.write(title+"\n");
	             out.flush();
             }
             int i=0;
             long st = System.currentTimeMillis();
             for(File f:features){
            	 i++;
            	 System.out.print(i+": ");
            	 BufferedReader r1 = BF.readFile(f);
            	
                 String line = "";
                 int u = 0;//第c行
                 while((line = r1.readLine())!=null){
                     String[] words = line.split(",");
                     int userID = Integer.valueOf(words[0]);
                     if(userID<startID){
                    	 continue;
                     }
                     u++;
                     if(u==num){
                    	 endID = userID;
                    	 break;
                     }
                     String temp = words[1];
                     String feature =  userFeatures.get(userID);
                     if(feature==null){
                    	 userFeatures.put(userID, temp);
                     }else{
                    	 userFeatures.put(userID, (feature+","+temp));
                     }
                 }
                 r1.close();
                 long et = System.currentTimeMillis();
                 long time = (et-st)/1000;
                 System.out.println("\t"+entity+" Features size : "+userFeatures.size());
             }
             
            
             for(Map.Entry<Integer, String> m:userFeatures.entrySet()){
            	 out.write(m.getKey()+","+m.getValue()+"\n");
             }
             out.close();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return endID;
    }
    public Integer getLine(double percent) throws IOException{//得到followers排名前percent的界限
        String FeaturesPath = "Data\\Feature\\";
        TreeMap<Integer,Integer> userFollowersNum = new TreeMap<Integer,Integer>();
        File f = new File(FeaturesPath+"userFollowersNum.txt");
        System.out.println("Read "+f.getName());
        BufferedReader r1 = BF.readFile(f);
        String line = "";
        try {
            while((line = r1.readLine())!=null){
                String[] words = line.split(",");
                int userId = Integer.valueOf(words[0]);
                int FollowersNum = Integer.valueOf(words[1]);
                userFollowersNum.put(userId, FollowersNum);
            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int totalNum = userFollowersNum.size();
        System.out.println("Read "+f.getName()+" completed!");

        List<Map.Entry<Integer, Integer>> list=
            new ArrayList<Map.Entry<Integer, Integer>>(userFollowersNum.entrySet());

        //排序
        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {   
            public int compare(Map.Entry<Integer, Integer> m1, Map.Entry<Integer, Integer> m2) {     
                //return (m2.getValue() - m1.getValue());
                return (m2.getValue())-(m1.getValue());
        //m1-m2     递增顺序
        //m2-m1     递减顺序
            }
        });
//        BufferedWriter out = new BufferedWriter(
//                new FileWriter("Data\\Feature\\UserFollowersByNum.txt"));
//        for(Map.Entry<Integer, Integer> m:list){
//           
//            out.write(m.getKey()+","+m.getValue()+"\n");
//        }
//        out.close();
        return list.get((int)(totalNum*percent)).getValue();
    }
    public Integer getLine(int num) throws IOException{//得到followers排名前num的界限
        String FeaturesPath = "Data\\Feature\\";
        TreeMap<Integer,Integer> userFollowersNum = new TreeMap<Integer,Integer>();
        File f = new File(FeaturesPath+"userFollowersNum.txt");
        System.out.println("Read "+f.getName());
        BufferedReader r1 = BF.readFile(f);
        String line = "";
        try {
            while((line = r1.readLine())!=null){
                String[] words = line.split(",");
                int userId = Integer.valueOf(words[0]);
                int FollowersNum = Integer.valueOf(words[1]);
                userFollowersNum.put(userId, FollowersNum);
            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int totalNum = userFollowersNum.size();
        System.out.println("Read "+f.getName()+" completed!");

        List<Map.Entry<Integer, Integer>> list=
            new ArrayList<Map.Entry<Integer, Integer>>(userFollowersNum.entrySet());

        //排序
        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {   
            public int compare(Map.Entry<Integer, Integer> m1, Map.Entry<Integer, Integer> m2) {     
                //return (m2.getValue() - m1.getValue());
                return (m2.getValue())-(m1.getValue());
            }
        });
        return list.get(num).getValue();
    }
    public void getARFF2(int percent){//分成两类，前百分之十和后百分之九十
    	String line = "";
        try{
            String FeaturesPath = "Data\\Feature\\";
            File f = new File(FeaturesPath+"UserFeatures.txt");
            System.out.println("Read "+f.getName());
            BufferedReader r1 = BF.readFile(f);
            int featuresNum = 0;//特征数量；
            while((line = r1.readLine())!=null){
                String[] words = line.split(",");
                featuresNum = words.length-2;//减去第一个id和第二个followerNum
            }
            System.out.println(featuresNum);
            BufferedWriter out= new BufferedWriter(
                    new FileWriter("Data\\UserFeaturesN"+percent+".arff"));
            //写arrf文件的头
            out.write("@RELATION UserFeatures"+"\n\n");
            out.write("@ATTRIBUTE rank "+"  {1,0}"+"\n");
            for(int j = 0;j<featuresNum;j++){
                out.write("@ATTRIBUTE "+"feature"+j+"  numeric"+"\n");
            }
            out.write("\n@DATA\n");
            int top10pline = getLine(percent);
            System.out.println("前百分之十的用户followersNum 界限为"+top10pline);
            r1 = BF.readFile(f);
            while((line = r1.readLine())!=null){
                String[] words = line.split(",");
                int userId = Integer.valueOf(words[0]);
                int followersNum =Integer.valueOf(words[1]);
            //    System.out.println(userId+" : "+followersNum+"___"+top10pline);
                if(followersNum>=top10pline){//前百分之十的界限
                    out.write(1+" ");//在前百分之十之外,userId+","+
                }
                else{
                    out.write(0+" ");
                }
                String feature="";
                for(int i=2;i<words.length;i++){
                	out.write(words[i]+" ");
                }
                out.write("\n");
            }
            out.close();
        }catch(Exception e){
        	System.out.println(line);
        	
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        Features f = new Features();
        long start = System.currentTimeMillis();
    //    f.combine();
        String feature0 = "userYear";
        String feature1 = "userFollowersNum";
        String feature2 = "userReposNum";
        String feature3 = "userOriginalReposNum";
        String feature4 = "userForkedReposNum";//从被人fork的项目的数量
        String feature5 = "userWatchersNum";
        String feature6 = "UserPR";
        String feature7 = "userCommitsNum";
        String feature8 = "userIssuesNum";
        String feature9 = "userForksNum";//项目被fork的数量
        
//        File Monthes = new File(Constants.screenFile+"features\\allActivity");
//        f.userSumNum(Monthes);
 //       f.userSmplify();
        File file = new File(Constants.screenFile+"features\\TOPSISResult.csv");
        f.addFeature(file);
        File users = new File(Constants.screenFile+"features\\users");
 //       f.addFeatures(users);
     //   f.combineFeatures("users");
        long end = System.currentTimeMillis();
        System.out.println("计算用时="+(end-start)/1000);

    }

}
