package wss.rank;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import utils.BF;

public class RankCompare {

	/**
	 * @param args
	 * 各个排名之间进行比较
	 * 方式1：Spearman correlation
	 */
	int TotalUser = 848982;
	String path = "E:\\github-2015-09-25\\rank\\";

	public ArrayList<Integer> topPercent(File f,int p){//取前百分之x
		ArrayList<Integer> list = new ArrayList<Integer>();
		try{
			String line = "";
			int num = TotalUser*p/100;
			int i = 0;
			BufferedReader reader = BF.readFile(f);
			while((line=reader.readLine())!=null){
				i++;
				if(i>num){
					break;
				}
				String[] words = line.split(",");
				list.add(Integer.valueOf(words[0]));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	public ArrayList<Integer> topNum(File f,int num){//取Top-N
		ArrayList<Integer> list = new ArrayList<Integer>();
		try{
			String line = "";
			int i = 0;
			BufferedReader reader = BF.readFile(f);
			while((line=reader.readLine())!=null){
				i++;
				if(i>num){
					break;
				}
				String[] words = line.split(",");
				list.add(Integer.valueOf(words[0]));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	public ArrayList<Integer> getUsersRank(File f,ArrayList<Integer> usersList){
		//根据评分文件和用户列表得到用户排名列表，跟用户列表顺序相同
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i=0;i<usersList.size();i++){
			list.add(0);
		}
		ArrayList<Integer> tempList = new ArrayList<Integer>();
		TreeMap<Integer,Integer> tempMap = new TreeMap<Integer,Integer>();
		try{
			String line = "";
			int i = 0;
			BufferedReader reader = BF.readFile(f);
			while((line=reader.readLine())!=null){
				i++;
				if(usersList.size()==0){
					break;
				}
				String[] words = line.split(",");
				Integer user = Integer.valueOf(words[0]);
				Integer rank = Integer.valueOf(words[2]);
				if(usersList.contains(user)){
					int index = usersList.indexOf(user);
					list.set(index, rank);
				//	usersList.remove(user);
					tempList.add(rank);
					tempMap.put(user, rank);
				}
			}

			List<Map.Entry<Integer,Integer>> list2 =
			    new ArrayList<Map.Entry<Integer,Integer>>(tempMap.entrySet());
			Collections.sort(list2, new Comparator<Map.Entry<Integer,Integer>>() {
			    public int compare(Map.Entry<Integer,Integer> o1, Map.Entry<Integer,Integer> o2) {
			        return (o2.getValue()- o1.getValue());
			    }
			});
			for(Map.Entry<Integer, Integer> m:list2){
				Integer user = m.getKey();
				Integer rank = m.getValue();
				int realRank = 0;
				int index = usersList.indexOf(user);
				list.set(index, rank);
			//	usersList.remove(user);
				tempList.add(rank);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	public TreeMap<Integer,Integer> getUsersRankMap(File f,ArrayList<Integer> usersList){
		//根据评分文件和用户列表得到用户和用户评分列表
		TreeMap<Integer,Integer> map = new TreeMap<Integer,Integer>();
		try{
			String line = "";
			int i = 0;
			BufferedReader reader = BF.readFile(f);
			while((line=reader.readLine())!=null){
				i++;
				if(usersList.size()==0){
					break;
				}
				String[] words = line.split(",");
				Integer user = Integer.valueOf(words[0]);
				Integer rank = Integer.valueOf(words[1]);
				if(usersList.contains(user)){
					map.put(user, rank);
					usersList.remove(user);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}
	public double Spearman(List<Integer> list1,List<Integer> list2){
		int n = list1.size();
		if(list2.size()!=n){
			System.out.println("两个列表长度不同");
			return 0;
		}
		double d2 = 0;
		for(int i=0;i<list1.size();i++){
			Integer id1 = list1.get(i);
			int rank2 = list2.indexOf(id1);
			d2 += (rank2-i)*(rank2-i);//差的平方和
		}
		System.out.println(6*d2);
		System.out.println(n*n*n-n);

		double s = 1-6*d2/(n*n*n-n);
		return s;
	}
	/*
	 * 目前问题：如果取前100名，两个列表涉及到的用户不统一怎么办呢？
	 * 
	 */
	public double consistency(ArrayList<Integer> list1,ArrayList<Integer> list2){
		double c = 0.0;
		try{
			int count=0;
			for(Integer t:list1){
				if(list2.contains(t)){
					count++;
				}
			}
			c = (double)count/list1.size();
		}catch(Exception e){
			e.printStackTrace();
		}
		return c;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long s = System.currentTimeMillis();
		RankCompare rc = new RankCompare();
		File RUR = new File(rc.path +"score\\RUR.csv");
		File ACQ = new File(rc.path +"score\\ACQ.csv");
		File A = new File(rc.path +"score\\A.csv");
		File Q = new File(rc.path +"score\\Q.csv");
		File C = new File(rc.path +"score\\C.csv");
		File F = new File(rc.path +"score\\userFollowers.csv");
		File HITS = new File(rc.path +"score\\userAuthority.csv");
		File PR = new File(rc.path +"score\\userPageRankScore.csv");
		ArrayList<File> rankFiles = new ArrayList<File>();
		rankFiles.add(A);
		rankFiles.add(C);
		rankFiles.add(Q);
		rankFiles.add(ACQ);
		rankFiles.add(F);
		rankFiles.add(HITS);
		rankFiles.add(PR);
		rankFiles.add(RUR);
	//	ArrayList<Integer> usersList = rc.topNum(RUR, 100);
		ArrayList<Integer> usersList = rc.topPercent(RUR, 1);
		System.out.println(usersList);
		System.out.println(usersList.size());
//		ArrayList<Integer> rankList1 = rc.getUsersRank(RUR, usersList);
//		System.out.println(rankList1.size());
//		System.out.println(rankList1);

		
	//	ArrayList<Integer> usersList2 = rc.topNum(ACQ, 100);
		ArrayList<Integer> usersList2 = rc.topPercent(ACQ, 1);
		ArrayList<Integer> rankList2 = rc.getUsersRank(ACQ, usersList);
//		System.out.println(rankList2.size());
//		System.out.println(rankList2);
//		System.out.println(rc.consistency(usersList, usersList2));
//		double d = rc.Spearman(rankList1, rankList2);
//		System.out.println(d);
		long e = System.currentTimeMillis();
		System.out.println("Time "+(e-s)/1000);
	}

}

																												