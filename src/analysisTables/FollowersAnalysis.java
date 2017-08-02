//package analysisTables;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.FileWriter;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Map;
//import java.util.TreeMap;
//
//import readTable.NewReadTable;
//import readTable.ReadFollowers;
//import entity.Follower;
//
//public class FollowersAnalysis {
//
//	/**
//	 * @param args
//	 */
//	/*
//	 * 1.follower: user——follower:num,list
//	 * 2.followee: user——followee:num,list
//	 * 
//	*/
//	public void userfollowerAna(){
//		//已知 followerList
//		ReadFollowers rf = new ReadFollowers();
//		rf.readFollowers();
//		System.out.println(rf.FollowersList.size());
//		
//		try{
//			BufferedWriter out = new BufferedWriter(new FileWriter(rf.tablePath+"user_Follower.txt"));
//			for(int i=0;i<rf.FollowersList.size();i++){
//				Follower f = rf.FollowersList.get(i);
//				int user_id = f.getUser_id();
//				ArrayList<Integer> UserFollowerList = new ArrayList<Integer>();
//				int f_id = f.getFollower_id();
//				UserFollowerList.add(f_id);
//				
//				while((i+1)<rf.FollowersList.size()&&rf.FollowersList.get(i+1).getUser_id() == user_id){
//					Follower nextF = rf.FollowersList.get(i+1);
//					UserFollowerList.add(nextF.getFollower_id());
//					i++;
//				}
//				out.write(user_id+","+UserFollowerList.size()+",");
//				for(int p:UserFollowerList){
//					out.write(p+" ");
//				}
//				out.write("\n");
//				out.flush();
//			}
//			out.close();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		
//	}
//	
//	public void userfolloweeAna(){
//		ReadFollowers rf = new ReadFollowers();
//		rf.readFollowers();
//		System.out.println(rf.FollowersList.size());
//		 Collections.sort(rf.FollowersList, new Comparator<Follower>() {
//			
//             @Override
//             //可以按User对象的其他属性排序，只要属性支持compareTo方法
//             public int compare(Follower f1, Follower f2) {
//                 int f_id1 = f1.getFollower_id();
//                 int f_id2 = f2.getFollower_id();
//               
//                 return (f_id1-f_id2);
//             }
//         });
//		 try{
//				BufferedWriter out = new BufferedWriter(new FileWriter(rf.tablePath+"user_Followee.txt"));
//				for(int i=0;i<rf.FollowersList.size();i++){
//					Follower f = rf.FollowersList.get(i);
//					int follower_id = f.getFollower_id();
//					ArrayList<Integer> UserFolloweeList = new ArrayList<Integer>();
//					int user_id = f.getUser_id();
//					UserFolloweeList.add(user_id);
//					
//					while((i+1)<rf.FollowersList.size()&&rf.FollowersList.get(i+1).getFollower_id() == follower_id){
//						Follower nextF = rf.FollowersList.get(i+1);
//						UserFolloweeList.add(nextF.getUser_id());
//						i++;
//					}
//					out.write(follower_id+","+UserFolloweeList.size()+",");
//					for(int p:UserFolloweeList){
//						out.write(p+" ");
//					}
//					out.write("\n");
//					out.flush();
//				}
//				out.close();
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//	}
//	public TreeMap<Integer,ArrayList<Integer>> getUserFolloweeMap(){
//		TreeMap<Integer,ArrayList<Integer>> UserFolloweeMap = new TreeMap<Integer,ArrayList<Integer>>();
//		try{
//			NewReadTable nrt = new NewReadTable();
//			BufferedReader r = nrt.readFile(nrt.tablePath+"Analysis\\user_Followee_id.txt");
//			String line = "";
//			String [] ws;
//			while((line = r.readLine())!=null){
//				ws = line.split(",");
//				Integer userid = Integer.valueOf(ws[0]);
//				String[] ids = ws[2].split(" ");
//				ArrayList<Integer> feesList = new ArrayList<Integer>();
//				for(String id :ids){
//					feesList.add(Integer.valueOf(id));
//				}
//				UserFolloweeMap.put(userid, feesList);
//			}
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return UserFolloweeMap;
//	}
//	public TreeMap<Integer,ArrayList<Integer>> getUserFollowerMap(){
//		TreeMap<Integer,ArrayList<Integer>> UserFollowerMap = new TreeMap<Integer,ArrayList<Integer>>();
//		try{
//			NewReadTable nrt = new NewReadTable();
//			BufferedReader r = nrt.readFile(nrt.tablePath+"Analysis\\user_Follower_id.txt");
//			String line = "";
//			String [] ws;
//			while((line = r.readLine())!=null){
//				ws = line.split(",");
//				Integer userid = Integer.valueOf(ws[0]);
//				String[] ids = ws[2].split(" ");
//				ArrayList<Integer> fersList = new ArrayList<Integer>();
//				for(String id :ids){
//					fersList.add(Integer.valueOf(id));
//				}
//				UserFollowerMap.put(userid, fersList);
//			}
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return UserFollowerMap;
//	}
//	public TreeMap<Integer,Integer> getUserFolloweeNumMap(){
//		TreeMap<Integer,Integer> UserFolloweeNumMap = new TreeMap<Integer,Integer>();
//		try{
//			NewReadTable nrt = new NewReadTable();
//			BufferedReader r = nrt.readFile(nrt.tablePath+"Analysis\\user_Followee_num.txt");
//			String line = "";
//			String [] ws;
//			while((line = r.readLine())!=null){
//				ws = line.split(",");
//				Integer userid = Integer.valueOf(ws[0]);
//				Integer num = Integer.valueOf(ws[1]);
//				UserFolloweeNumMap.put(userid, num);
//			}
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return UserFolloweeNumMap;
//	}
//	public TreeMap<Integer,Integer> getUserFollowerNumMap(){
//		TreeMap<Integer,Integer> UserFollowerNumMap = new TreeMap<Integer,Integer>();
//		try{
//			NewReadTable nrt = new NewReadTable();
//			BufferedReader r = nrt.readFile(nrt.tablePath+"Analysis\\user_Follower_num.txt");
//			String line = "";
//			String [] ws;
//			while((line = r.readLine())!=null){
//				ws = line.split(",");
//				Integer userid = Integer.valueOf(ws[0]);
//				Integer num = Integer.valueOf(ws[1]);
//				UserFollowerNumMap.put(userid, num);
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return UserFollowerNumMap;
//	}
//	
//	public void followerAnalysis(){
//		TreeMap<Integer,Integer> uferNum = getUserFollowerNumMap();
//		
//	}
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		long s = System.currentTimeMillis();
//		FollowersAnalysis fa = new FollowersAnalysis();
//	//	System.out.println(" UserFolloweeMap : "+fa.getUserFolloweeNumMap().size());
//	//	System.out.println(" UserFollowerMap : "+fa.getUserFollowerNumMap().size());
//		long e = System.currentTimeMillis();
//		System.out.println("Time : "+(e-s)/1000);
//	}
//
//}
///*public void sort(){
//		try{
//			NewReadTable nrt = new NewReadTable();
//			BufferedReader r = nrt.readFile(nrt.tablePath+"user_Follower.txt");
//			String line = "";
//			String [] ws;
//			TreeMap<Integer,String> uf = new TreeMap<Integer,String>();
//			
//			while((line = r.readLine())!=null){
//				ws = line.split(",");
//				uf.put(Integer.valueOf(ws[0]), line);
//			}
//			System.out.println("uf.size "+uf.size());
//			BufferedWriter out = new BufferedWriter(new FileWriter(nrt.tablePath+"user_Follower1.txt"));
//			ArrayList<Map.Entry<Integer, String>> list = 
//				new ArrayList<Map.Entry<Integer, String>>(uf.entrySet());
//			 Collections.sort(list, new Comparator<Map.Entry<Integer, String>>() {
//				
//	             @Override
//	             public int compare(Map.Entry<Integer, String> m1, Map.Entry<Integer, String> m2) {
//	            	 String l1 = m1.getValue();
//	            	 String l2 = m2.getValue();
//	            	 String[]w1 = l1.split(",");
//	            	 String[]w2 = l2.split(",");
//	                 int n1 = Integer.valueOf(w1[1]);
//	                 int n2 = Integer.valueOf(w2[1]);
//	                 return (n2-n1);
//	             }
//	         });
//		//	System.out.println(id1+" "+"id1s.size "+list.size());
//			for(Map.Entry<Integer, String> b:list){
//				out.write(b.getValue()+"\n");
//			}
//			out.close();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
//	public void readRe(){
//		try{
//			NewReadTable nrt = new NewReadTable();
//			BufferedReader r = nrt.readFile(nrt.tablePath+"user_Followee1.txt");
//			String line = "";
//			String [] ws;
//			BufferedWriter out = new BufferedWriter(new FileWriter(nrt.tablePath+"user_FolloweeNum.txt"));
//			while((line = r.readLine())!=null){
//				ws = line.split(",");
//				out.write(Integer.valueOf(ws[0])+","+Integer.valueOf(ws[1])+"\n");
//			}
//			
//			out.close();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
// * 
// * */

