package wss.resultAnalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import utils.BF;
import utils.Constants;

public class StatisticsAnaysis {

	/**
	 * @param args
	 */
	public boolean MeanMedianSDAna(File feature){//平均值，中位数，标准差
		try{
			System.out.print(feature.getName()+": ");
			BufferedReader r = BF.readFile(feature);
			TreeMap<Integer,Double> treeMap = new TreeMap<Integer,Double>();
			TreeMap<Double,Integer> scoreNumMap = new TreeMap<Double,Integer>();
			String line = "";
			int N=0;
			double sum = 0;
			line = r.readLine();//首行标题行
			while((line=r.readLine())!=null){
				String[] words = line.split(",");
				
				int uid = Integer.valueOf(words[0]);
				double f = Double.valueOf(words[2]);
				sum += f;
				treeMap.put(uid, f);
				Integer c = scoreNumMap.get(f);
				if(c==null){
					c=0;
				}
				c++;
				scoreNumMap.put(f,c);
			}
			System.out.println("Down! "+treeMap.size());
			N = treeMap.size();
			double mean = sum/N;
			//排序
			List<Map.Entry<Integer, Double>> maplist = 
				new ArrayList<Map.Entry<Integer, Double>>(treeMap.entrySet());
			  Collections.sort(maplist,new Comparator<Map.Entry<Integer, Double>>(){  
				public int compare(Map.Entry<Integer, Double> e1, Map.Entry<Integer,Double> e2) {  //重载Comparator的compare函数
					return e1.getValue().compareTo(e2.getValue());  
				}  
			});  
			Double median = 0.0;//中位数
			double mode=0.0;//众数
			int max = 0;
			int i=0,index1=0,index2 = 0;
			if(scoreNumMap.size()%2==0){//偶数个，中位数为中间两个数的平均值
				index1 = scoreNumMap.size()/2;
				index2 = index1+1;
			}else{//奇数个，中位数为中间数
				index1 = scoreNumMap.size()/2+1;
				index2 = index1;
			}
		    for(Map.Entry<Double, Integer> m:scoreNumMap.entrySet()){
		    	i++;
		    	if(i==index1){
		    		median += m.getKey();
		    	}
		    	if(i==index2){
		    		median += m.getKey();
		    	}
		    	if(max<m.getValue()){
		    		max = m.getValue();
		    		mode = m.getKey();
		    	}
		    }
		    median = median/2;
			//标准差
			double sum2 = 0;//平方和
			for(Map.Entry<Integer, Double> m:maplist){
				double d = m.getValue()-mean;//与平均数的差
				sum2 += Math.pow(d, 2);//平方和
			}
			double staD = Math.sqrt(sum2/N);//标准差为差的平方和除以N再开方
			//System.out.println(featureName+","+mean+","+median+","+staD);
			File outFile = new File(Constants.screenFile+"features\\StatisticsAnalysis.csv");
			BufferedWriter out = null;
			if(!outFile.exists()){
				out = new BufferedWriter(new FileWriter(outFile,true));
				out.write("FeatureName,Mean,Median,StatisticsDeviation,mode\n");
			}else{
				out = new BufferedWriter(new FileWriter(outFile,true));
			}
			out.write(feature.getName().replaceAll(".csv","")+","+mean+","
					+median+","+staD+","+mode+"\n");
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}
	public void FeatureUserCountAna(File f){//特征以及其对应用户的数量
		try{
			BufferedReader r = BF.readFile(f);
			System.out.print(f.getName()+" ... ");
			String line = "";
			TreeMap<Integer,Integer> anaMap = new TreeMap<Integer,Integer>();
			int i=0;
			while((line=r.readLine())!=null){
				i++;
				String[] words = line.split(",");
				if(words[0].contains("id")){
					continue;
				}
				int uid = Integer.valueOf(words[0]);
				int feature = Integer.valueOf(words[2]);
				Integer c = anaMap.get(feature);
				if(c==null){
					c=0;
				}
				c++;
				anaMap.put(feature, c);//
			}
			System.out.println(i);
			BufferedWriter out = new BufferedWriter(new FileWriter
					(Constants.screenFile+"Analysis\\Sta_"+f.getName()));
			BufferedWriter outLog = new BufferedWriter(new FileWriter
					(Constants.screenFile+"Analysis\\StaLog_"+f.getName()));
			for(Map.Entry<Integer, Integer> m:anaMap.entrySet()){
				out.write(m.getKey()+","+m.getValue()+"\n");
				outLog.write(Math.log10(m.getKey())+","+Math.log10(m.getValue())+"\n");
			}
			out.close();
			outLog.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void Correlation(File f1,File f2){
		try{
			
			String line = "";
			int i=0;
			TreeMap<Integer,Integer> anaMap = new TreeMap<Integer,Integer>();
			BufferedReader r1 = BF.readFile(f1);
			System.out.print(f1.getName()+" ... ");
			while((line=r1.readLine())!=null){
				i++;
				String[] words = line.split(",");
				if(words[0].contains("id")){
					continue;
				}
				int uid = Integer.valueOf(words[0]);
				int feature = Integer.valueOf(words[2]);
				
				anaMap.put(uid, feature);//
			}
			System.out.println(i);
			
			BufferedWriter out = new BufferedWriter(new FileWriter
					(Constants.screenFile+"Analysis\\"
							+f1.getName().replaceAll(".csv","")+"VS"+f2.getName().replaceAll("user", "")));
			BufferedReader r2 = BF.readFile(f2);
			System.out.print(f2.getName()+" ... ");
			while((line=r2.readLine())!=null){
				i++;
				String[] words = line.split(",");
				if(words[0].contains("id")){
					continue;
				}
				Integer uid = Integer.valueOf(words[0]);
				out.write(uid+","+anaMap.get(uid)+","+words[2]+"\n");
			}
			System.out.println(i);
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String f0 = "userYear";
        String f1 = "userFollowersNum";
        String f10 = "userFolloweesNum";
        String f2 = "userReposNum";
        String f3 = "userOriginalReposNum";
        String f4 = "userForkReposNum";//从被人fork的项目的数量
        String f5 = "userWatchersSumNum";
        String f6 = "UserPR";
        String f7 = "userCommitsNum";
        String f8 = "userIssuesNum";
        String f9 = "userForkedSumNum";//项目被fork的数量
        String f11 = "user_opened_Pull_requestHNum";
        StatisticsAnaysis s = new StatisticsAnaysis();
        String[] features = {f1};//f10,f2,f3,f4,f5,f7,f8,f9,f11
        for(String ff:features){
        	File f = new File(Constants.screenFile+"features\\"+ff+".csv");
        //    s.MeanMedianSDAna(f);
        	s.FeatureUserCountAna(f);
        }
        File path = new File("");
        
	}

}
