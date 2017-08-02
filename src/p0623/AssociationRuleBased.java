package p0623;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import utils.Constants;

public class AssociationRuleBased {

	/**
	 * @param args
	 * 0623推荐算法，尝试一，基于关联规则的推荐
	 * 基于关联规则的推荐，以关联规则为基础，把已购商品作为规则头，规则体为推荐对象。
	 * 关联规则挖掘可以发现不同商品在销售过程中的相关性，在零售业中已经得到了成功的应用。
	 * 关联规则就是在一个交易数据库中统计购买了商品集X的交易中有多大比例的交易同时购买了商品集Y，
	 * 其直观的意义就是用户在购买某些商品的时候有多大倾向去购买另外一些商品。比如购买牛奶的同时很多人会同时购买面包。
	 * 算法的第一步关联规则的发现最为关键且最耗时，是算法的瓶颈，但可以离线进行。
	 * 其次，商品名称的同义性问题也是关联规则的一个难点。
	 */
	/*首先构造，user，以及其Follow的人的文件
	 * user_id, Follow_id Follow_id .......Follow_id*/
	
	public void UserFollowerList(){
		try{
			BufferedInputStream fis = new BufferedInputStream(new FileInputStream(Constants.Followers));
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"utf-8"),5*1024*1024);
			Map<String,ArrayList<String>> FollowerUserListMap = new TreeMap<String, ArrayList<String>>();
//			BufferedWriter out = new BufferedWriter(new FileWriter(Constants.RootFile+"outs\\FollowerUserList.txt"));
			Map<String,ArrayList<String>> UserFollowerListMap = new TreeMap<String, ArrayList<String>>();
			BufferedWriter out = new BufferedWriter(new FileWriter(Constants.RootFile+"outs\\UserFollowerList.txt"));
			String line = null;
			String[] words ;
			while((line=reader.readLine())!=null){
				words = line.split(",");
				String user_id = words[0];
				String follower_id = words[1];
			//	System.out.println("\t"+user_id+" "+follower_id);
//				ArrayList<String> us = FollowerUserListMap.get(follower_id);
//				if(us==null){
//					ArrayList<String> uus = new ArrayList<String>();
//					uus.add(user_id);
//					FollowerUserListMap.put(follower_id, uus);
//				}else{
//					us.add(user_id);
//					FollowerUserListMap.put(follower_id, us);
//				}
				
				ArrayList<String> fs = UserFollowerListMap.get(user_id);
				if(fs==null){
					ArrayList<String> ffs = new ArrayList<String>();
					ffs.add(follower_id);
					UserFollowerListMap.put(user_id, ffs);
				}else{
					fs.add(follower_id);
					UserFollowerListMap.put(user_id, fs);
				}
				
			}
			//对Map根据关注的人的多少，即list的长度排序，然后输出到txt
			List<Map.Entry<String,ArrayList<String>>> mapList ;
			//		mapList = new ArrayList<Map.Entry<String,ArrayList<String>>>(FollowerUserListMap.entrySet()); 
			mapList = new ArrayList<Map.Entry<String,ArrayList<String>>>(UserFollowerListMap.entrySet()); 
			System.out.println(mapList.size());  
			//通过比较器实现比较排序 
//  	  		Collections.sort(mapList, new Comparator<Map.Entry<String,ArrayList<String>>>(){ 
//			   public int compare(Map.Entry<String,ArrayList<String>> map1,Map.Entry<String,ArrayList<String>> map2){ 
//				   int i1 = map1.getValue().size();
//				   int i2 = map2.getValue().size();
//			    
//				   return (i2-i1); 
//			   } 
//			  }); 
  	  		
			 for(Map.Entry<String,ArrayList<String>> map:mapList){ 
				//   System.out.println(mapping.getKey()+":"+mapping.getValue()); 
				  String userid = map.getKey();
				  ArrayList<String> follows = map.getValue();
				  out.write(userid+" "+follows.size()+",");
				  for(String followid:follows){
					  out.write(followid+",");
				  }
				  out.write("\n");
			 } 
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void FollowerUserList(){
		try{
			BufferedInputStream fis = new BufferedInputStream(new FileInputStream(Constants.Followers));
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"utf-8"),5*1024*1024);
			Map<String,ArrayList<String>> FollowerUserListMap = new TreeMap<String, ArrayList<String>>();
			BufferedWriter out = new BufferedWriter(new FileWriter(Constants.RootFile+"outs\\FollowerUserList.txt"));
			String line = null;
			String[] words ;
			while((line=reader.readLine())!=null){
				words = line.split(",");
				String user_id = words[0];
				String follower_id = words[1];
			//	System.out.println("\t"+user_id+" "+follower_id);
				ArrayList<String> us = FollowerUserListMap.get(follower_id);
				if(us==null){
					ArrayList<String> uus = new ArrayList<String>();
					uus.add(user_id);
					FollowerUserListMap.put(follower_id, uus);
				}else{
					us.add(user_id);
					FollowerUserListMap.put(follower_id, us);
				}
				

				
			}
			//对Map根据关注的人的多少，即list的长度排序，然后输出到txt
			List<Map.Entry<String,ArrayList<String>>> mapList ;
			mapList = new ArrayList<Map.Entry<String,ArrayList<String>>>(FollowerUserListMap.entrySet()); 
			System.out.println(mapList.size());  
			//通过比较器实现比较排序 
//  	  		Collections.sort(mapList, new Comparator<Map.Entry<String,ArrayList<String>>>(){ 
//			   public int compare(Map.Entry<String,ArrayList<String>> map1,Map.Entry<String,ArrayList<String>> map2){ 
//				   int i1 = map1.getValue().size();
//				   int i2 = map2.getValue().size();
//			    
//				   return (i2-i1); 
//			   } 
//			  }); 
  	  		
			 for(Map.Entry<String,ArrayList<String>> map:mapList){ 
				//   System.out.println(mapping.getKey()+":"+mapping.getValue()); 
				  String userid = map.getKey();
				  ArrayList<String> follows = map.getValue();
				  out.write(userid+" "+follows.size()+",");
				  for(String followid:follows){
					  out.write(followid+",");
				  }
				  out.write("\n");
			 } 
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AssociationRuleBased ar = new AssociationRuleBased();
		long s = System.currentTimeMillis();
		ar.FollowerUserList();
		long e = System.currentTimeMillis();
		System.out.println("计算用时： "+(e-s)/1000+" s");
	}

}
