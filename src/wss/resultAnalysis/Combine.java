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

public class Combine {

	/**
	 * @param args
	 */
//	File usersFile = new File(Constants.screenFile+"tables\\usersUSR.csv");
	File usersFile = new File(Constants.dirname+"dump\\features\\ORG\\usersInfo.csv");
	TreeMap<Integer,String> userFeaturesMap = new TreeMap<Integer,String>();
	public void init(){
		try{
			System.out.println("init...");
			 BufferedReader r1 = BF.readFile(usersFile);
             String line = "";
             String[] words=null;
             while((line = r1.readLine())!=null){
                 words = line.split(",");
                 if(words[0].contains("id")){
            		 continue;
            	 }
                 userFeaturesMap.put(Integer.valueOf(words[0]),words[1]);
             }
             r1.close();
             System.out.println("\t Features size : "+userFeaturesMap.size());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void combineFeaturesRank(String path){
    	try{
    		 File FeaturesPath = new File(path);
             File[] features = FeaturesPath.listFiles();
             String title = "id,login";
             String filename = "";
             int i=0;
             String[] words = null;
             for(File f:features){
            	 i++;
            	 System.out.print(i+": ");
            	 BufferedReader r1 = BF.readFile(f);
                 String line = "";
                 title = title +","+f.getName();
                 filename = filename+f.getName();
                 while((line = r1.readLine())!=null){
                	
                //	 System.out.println(line);
                //	 break;
                     words = line.split(",");
                     if(words[0].contains("id")){
                		 continue;
                	 }
                     int userId = Integer.valueOf(words[0]);
                     String rank = words[3];
                     String feature =  userFeaturesMap.get(userId);
                     if(feature==null){
                    	 continue;
                     }else{
                    	 userFeaturesMap.put(userId, (feature+","+rank));
                     }
                 }
                 r1.close();
                 System.out.println("\t Features size : "+userFeaturesMap.size());
             }
             title = title.replaceAll(".csv", "");
             filename = filename.replaceAll("user","").replaceAll(".csv", "").replaceAll("Num","");
             File outFile = new File(usersFile.getParent()+"\\"+usersFile.getName().replace(".csv","")+"Rank"+filename+".csv");
             BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
             
             out.write(title+"\n");
             out.flush();
             System.out.println("\tsort....");
             List<Map.Entry<Integer, String>> list=
 			    new ArrayList<Map.Entry<Integer, String>>(userFeaturesMap.entrySet());

 			//排序
 			Collections.sort(list, new Comparator<Map.Entry<Integer, String>>() {   
 			    public int compare(Map.Entry<Integer, String> m1, Map.Entry<Integer, String> m2) {     
 			        //return (m2.getValue() - m1.getValue());
 			    	String[] words1 = m1.getValue().split(",");
 			    	String[] words2 = m2.getValue().split(",");
 			        return (Double.valueOf(words1[1]).compareTo(Double.valueOf(words2[1])));
 			//m1-m2     递增顺序
 			//m2-m1     递减顺序
 			    }
 			});
 			System.out.println("\tsorted,writing....");
             for(Map.Entry<Integer, String> m:list){
            	 words = m.getValue().split(",");
            	 int rank1 = Integer.valueOf(words[1]);
            	 int rank2 = Integer.valueOf(words[2]);
            	 out.write(m.getKey()+","+m.getValue()+"\n");
             }
             out.close();
             System.out.println("title: "+title);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
	public void combineFeaturesRankD(String path){//两者之间的排序差
    	try{
    		 //TreeMap<Integer,String> userFeaturesMap = new TreeMap<Integer,String>();
    		 File FeaturesPath = new File(path);
            
             File[] features = FeaturesPath.listFiles();
             String title = "id,login";
             String filename = "";
             int i=0;
             String[] words = null;
             for(File f:features){
            	 i++;
            	 System.out.print(i+": ");
            	 BufferedReader r1 = BF.readFile(f);
                 String line = "";
                 title = title +","+f.getName();
                 filename = filename+f.getName();
                 while((line = r1.readLine())!=null){
                	
                //	 System.out.println(line);
                //	 break;
                     words = line.split(",");
                     if(words[0].contains("id")){
                		 continue;
                	 }
                     int userId = Integer.valueOf(words[0]);
                     if(words[0].contains("id")){
                    	 continue;
                     }
                     String rank = words[3];
                     String feature =  userFeaturesMap.get(userId);
                     if(feature==null){
                    	continue;
                     }else{
                    	 userFeaturesMap.put(userId, (feature+","+rank));
                     }
                 }
                 r1.close();
                 System.out.println("\t Features size : "+userFeaturesMap.size());
             }
             title = title.replaceAll(".csv", "");
             filename = filename.replaceAll("user","").replaceAll(".csv", "").replaceAll("Num","");
             File outFile = new File(path+"\\"+usersFile.getName().replace(".csv","")+"RankD"+filename+".csv");
             BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
             
             out.write(title+"\n");
             out.flush();
             System.out.println("\tsort....");
             List<Map.Entry<Integer, String>> list=
 			    new ArrayList<Map.Entry<Integer, String>>(userFeaturesMap.entrySet());

 			//排序
 			Collections.sort(list, new Comparator<Map.Entry<Integer, String>>() {   
 			    public int compare(Map.Entry<Integer, String> m1, Map.Entry<Integer, String> m2) {     
 			        //return (m2.getValue() - m1.getValue());
 			    	String[] words1 = m1.getValue().split(",");
 			    	String[] words2 = m2.getValue().split(",");
 	            	Integer d1 = Integer.valueOf(words1[1])-Integer.valueOf(words1[2]);
 	            	Integer d2 = Integer.valueOf(words2[1])-Integer.valueOf(words2[2]);
// 	            	d1 = Math.abs(d1);
// 	            	d2 = Math.abs(d2);
 			        return (d2.compareTo(d1));
 			//m1-m2     递增顺序
 			//m2-m1     递减顺序
 			    }
 			});
 			System.out.println("\tsorted,writing....");
             for(Map.Entry<Integer, String> m:list){
            	 words = m.getValue().split(",");
            	 int d = Integer.valueOf(words[1])-Integer.valueOf(words[2]);
            	 out.write(m.getKey()+","+m.getValue()+","+Math.abs(d)+"\n");
             }
             out.close();
             System.out.println("title: "+title);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
	public void combineFeaturesScoreD(String path){//两者之间的排序差
    	try{
    		 File FeaturesPath = new File(path);
             File[] features = FeaturesPath.listFiles();
             String title = "id,login";
             String filename = "";
             int i=0;
             String[] words = null;
             for(File f:features){
            	 i++;
            	 System.out.print(i+": ");
            	 BufferedReader r1 = BF.readFile(f);
                 String line = "";
                 title = title +","+f.getName();
                 filename = filename+f.getName();
                 while((line = r1.readLine())!=null){
                	
                //	 System.out.println(line);
                //	 break;
                     words = line.split(",");
                     if(words[0].contains("id")){
                		 continue;
                	 }
                     int userId = Integer.valueOf(words[0]);
                     if(words[0].contains("id")){
                    	 continue;
                     }
                     String rank = words[2];
                     String feature =  userFeaturesMap.get(userId);
                     if(feature==null){
                    	continue;
                     }else{
                    	 userFeaturesMap.put(userId, (feature+","+rank));
                     }
                 }
                 r1.close();
                 System.out.println("\t Features size : "+userFeaturesMap.size());
             }
             title = title.replaceAll(".csv", "");
             filename = filename.replaceAll("user","").replaceAll(".csv", "").replaceAll("Num","");
             File outFile = new File(path+"\\"+usersFile.getName().replace(".csv","")+"ScoreD"+filename+".csv");
             BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
             
             out.write(title+"\n");
             out.flush();
             System.out.println("\tsort....");
             List<Map.Entry<Integer, String>> list=
 			    new ArrayList<Map.Entry<Integer, String>>(userFeaturesMap.entrySet());

 			//排序
 			Collections.sort(list, new Comparator<Map.Entry<Integer, String>>() {   
 			    public int compare(Map.Entry<Integer, String> m1, Map.Entry<Integer, String> m2) {     
 			        //return (m2.getValue() - m1.getValue());
 			    	String[] words1 = m1.getValue().split(",");
 			    	String[] words2 = m2.getValue().split(",");
 	            	Integer d1 = Integer.valueOf(words1[1])-Integer.valueOf(words1[2]);
 	            	Integer d2 = Integer.valueOf(words2[1])-Integer.valueOf(words2[2]);
// 	            	d1 = Math.abs(d1);
// 	            	d2 = Math.abs(d2);
 			        return (d2.compareTo(d1));
 			//m1-m2     递增顺序
 			//m2-m1     递减顺序
 			    }
 			});
 			System.out.println("\tsorted,writing....");
             for(Map.Entry<Integer, String> m:list){
            	 words = m.getValue().split(",");
            	 int d = Integer.valueOf(words[1])-Integer.valueOf(words[2]);
            	 out.write(m.getKey()+","+m.getValue()+","+Math.abs(d)+"\n");
             }
             out.close();
             System.out.println("title: "+title);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
	public void combineFeaturesScore(String path){
    	try{
    		 File FeaturesPath = new File(path);
            
             File[] features = FeaturesPath.listFiles();
             String title = "id";
             String filename = "";
             int i=0;
             String[] words = null;
             for(File f:features){
            	 i++;
            	 System.out.print(i+": ");
            	 BufferedReader r1 = BF.readFile(f);
                 String line = "";
                 title = title +","+f.getName();
                 filename = filename+f.getName();
                 while((line = r1.readLine())!=null){
                	
                //	 System.out.println(line);
                //	 break;
                     words = line.split(",");
                     if(words[0].contains("id")){
                		 continue;
                	 }
                     int userId = Integer.valueOf(words[0]);
                     if(words[0].contains("id")){
                    	 continue;
                     }
                     String score = words[2];
                     String feature =  userFeaturesMap.get(userId);
                     if(feature==null){
                    	 continue;
                     }else{
                    	 userFeaturesMap.put(userId, (feature+","+score));
                     }
                 }
                 r1.close();
                 System.out.println("\t Features size : "+userFeaturesMap.size());
             }
             title = title.replaceAll(".csv", "");
             filename = filename.replaceAll("user","").replaceAll(".csv", "").replaceAll("Num", "");
             File outFile = new File(usersFile.getParent()+"\\usersScore"+filename+".csv");
             BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
             
             out.write(title+"\n");
             out.flush();
             System.out.println("\tsort....");
             List<Map.Entry<Integer, String>> list=
 			    new ArrayList<Map.Entry<Integer, String>>(userFeaturesMap.entrySet());

 			//排序
 			Collections.sort(list, new Comparator<Map.Entry<Integer, String>>() {   
 			    public int compare(Map.Entry<Integer, String> m1, Map.Entry<Integer, String> m2) {     
 			        //return (m2.getValue() - m1.getValue());
 			    	String[] words1 = m1.getValue().split(",");
 			    	String[] words2 = m2.getValue().split(",");
 			        return (Double.valueOf(words2[1]).compareTo(Double.valueOf(words1[1])));
 			//m1-m2     递增顺序
 			//m2-m1     递减顺序
 			    }
 			});
 			System.out.println("\tsorted,writing....");
             for(Map.Entry<Integer, String> m:list){
         //    for(Map.Entry<Integer, String> m:userFeaturesMap.entrySet()){
            	 words = m.getValue().split(",");
            	 out.write(m.getKey()+","+m.getValue()+"\n");
             }
             out.close();
             System.out.println("title: "+title);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
	public void combineFeaturesScorelog10(String path){
    	try{
    		 File FeaturesPath = new File(path);
            
             File[] features = FeaturesPath.listFiles();
             String title = "id";
             String filename = "";
             int i=0;
             String[] words = null;
             for(File f:features){
            	 i++;
            	 System.out.print(i+": ");
            	 BufferedReader r1 = BF.readFile(f);
                 String line = "";
                 title = title +","+f.getName();
                 filename = filename+f.getName();
                 while((line = r1.readLine())!=null){
                	
                //	 System.out.println(line);
                //	 break;
                     words = line.split(",");
                     if(words[0].contains("id")){
                		 continue;
                	 }
                     int userId = Integer.valueOf(words[0]);
                     if(words[0].contains("id")){
                    	 continue;
                     }
                     Integer score = Integer.valueOf(words[2]);
                     if(score==0){
                    	 
                     }
                     double score2 = Math.log10(score);
                     String feature =  userFeaturesMap.get(userId);
                     
                     if(feature==null){
                    	continue;
                     }else{
                    	 userFeaturesMap.put(userId, (feature+","+score2));
                     }
                 }
                 r1.close();
                 System.out.println("\t Features size : "+userFeaturesMap.size());
             }
             title = title.replaceAll(".csv", "");
             filename = filename.replaceAll("user","").replaceAll(".csv", "").replaceAll("Num", "");
             File outFile = new File(path+"\\"+usersFile.getName().replace(".csv","")+"Score"+filename+".csv");
             BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
             
             out.write(title+"\n");
             out.flush();
 //            System.out.println("\tsort....");
//             List<Map.Entry<Integer, String>> list=
// 			    new ArrayList<Map.Entry<Integer, String>>(userFeaturesMap.entrySet());
//
// 			//排序
// 			Collections.sort(list, new Comparator<Map.Entry<Integer, String>>() {   
// 			    public int compare(Map.Entry<Integer, String> m1, Map.Entry<Integer, String> m2) {     
// 			        //return (m2.getValue() - m1.getValue());
// 			    	String[] words1 = m1.getValue().split(",");
// 			    	String[] words2 = m2.getValue().split(",");
// 			        return (Double.valueOf(words1[2]).compareTo(Double.valueOf(words2[2])));
// 			//m1-m2     递增顺序
// 			//m2-m1     递减顺序
// 			    }
// 			});
// 			System.out.println("\tsorted,writing....");
//             for(Map.Entry<Integer, String> m:list){
             for(Map.Entry<Integer, String> m:userFeaturesMap.entrySet()){
            	 words = m.getValue().split(",");
            	 out.write(m.getKey()+","+m.getValue()+"\n");
             }
             out.close();
             System.out.println("title: "+title);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Combine f = new Combine();
		f.usersFile = new File(Constants.dirname+"dump\\features\\USR\\usersInfo.csv");
		f.init();
	//	String combinePath = Constants.screenFile+"features\\combine";
		String combinePath = f.usersFile.getParent()+"\\toCombine\\";
		f.combineFeaturesRank(combinePath);
//		f.combineFeaturesScore(combinePath);
	//	f.combineFeaturesScorelog10(Constants.screenFile+"features\\combine");
//		TreeMap<Integer,String> m = new TreeMap<Integer,String>();
//		m.put(1, null);
//		if(m.get(1)==null){
//			System.out.print("....");
//		}
//		System.out.println("ddd "+m.get(1));
	}

}
