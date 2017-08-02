package wss.githubFeatures;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import utils.BF;
import utils.Constants;

public class UsersORG {

	/**
	 * @param args
	 */
	String path = Constants.dumpFile;
	File org_membersFile=new File(Constants.dumpFile+"organization_members.csv");
	File orgFile=new File(Constants.dumpFile+"features\\ORG\\usersInfo.csv");
	public void preproOrg_members(){
		String line = "";
		int i=0;
		try{
			BufferedReader rRW = BF.readFile(orgFile);
			System.out.println("read "+orgFile.getName());
			TreeMap<Integer,ArrayList<Integer>> orgUsersListMap = new TreeMap<Integer,ArrayList<Integer>>();
			while((line=rRW.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if((i/100000)%25==0)
						System.out.println(" ");
				}
				String[]words = line.split(",");
				Integer orgId = Integer.valueOf(words[0]);
				orgUsersListMap.put(orgId, new ArrayList<Integer>());
 			}
			System.out.println(i+" ");
			System.out.println("rwnum: "+orgUsersListMap.size());

			BufferedReader rUR = BF.readFile(org_membersFile);
			i=0;
			while((line=rUR.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if((i/100000)%25==0)
						System.out.println(" ");
				}
				String[]words = line.split(",");
				Integer orgId = Integer.valueOf(words[0]);
				Integer userId = Integer.valueOf(words[1]);
				ArrayList<Integer> c = orgUsersListMap.get(orgId);
				if(c==null){
				//	c = new ArrayList<Integer>();
					continue;
				}
				c.add(userId);
				orgUsersListMap.put(orgId, c);
				
			}
			BufferedWriter out = new BufferedWriter(new FileWriter
					(orgFile.getParent()+"\\orgUsersList.csv"));
			BufferedWriter outN = new BufferedWriter(new FileWriter
					(orgFile.getParent()+"\\orgUsersNum.csv"));
			for(Map.Entry<Integer,ArrayList<Integer>> m:orgUsersListMap.entrySet()){
				out.write(m.getKey()+",");
				for(Integer c:m.getValue()){
					out.write(c+" ");
				}
				out.write("\n");
				outN.write(m.getKey()+","+m.getValue().size()+"\n");
			}
			out.close();
			outN.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void orgFollowers(){
		String line = "";
		int i=0;
		try{
			File rwFile = new File(path+"\\features\\USR\\userFollowersNum.csv");
			BufferedReader rRW = BF.readFile(rwFile);
			System.out.println("read "+rwFile.getName());
			TreeMap<Integer,Integer> userFollowersNumMap = new TreeMap<Integer,Integer>();
			while((line=rRW.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if((i/100000)%25==0)
						System.out.println(" ");
				}
				String[]words = line.split(",");
				int rid = Integer.valueOf(words[0]);

			//	System.out.print(uid+" - ");
				int watchersNum = Integer.valueOf(words[1]);
				userFollowersNumMap.put(rid, watchersNum);
 			}
			System.out.println(i+" ");
			System.out.println("rwnum: "+userFollowersNumMap.size());
//			BufferedWriter out = new BufferedWriter(new FileWriter
//					(Constants.screenFile+"features\\usersWatchersSumAveMaxNum.csv"));
//			BufferedWriter outList = new BufferedWriter(new FileWriter
//					(Constants.screenFile+"features\\usersWatchersNumList.csv"));
			BufferedWriter outMax = new BufferedWriter(new FileWriter
					(orgFile.getParent()+"\\orgMemberFollowerMaxNum.csv"));
			BufferedWriter outSum = new BufferedWriter(new FileWriter
					(orgFile.getParent()+"\\orgMemberFollowerSumNum.csv"));
			BufferedWriter outAve = new BufferedWriter(new FileWriter
					(orgFile.getParent()+"\\orgMemberFollowerAveNum.csv"));
			BufferedWriter outList = new BufferedWriter(new FileWriter
					(orgFile.getParent()+"\\orgMemberFollowerNumList.csv"));
			BufferedWriter outH = new BufferedWriter(new FileWriter
					(orgFile.getParent()+"\\orgMemberFollowerHindex.csv"));
			BufferedReader rUR = BF.readFile(orgFile.getParent()+"\\orgUsersList.csv");
			i=0;
		//	TreeMap<Integer,ArrayList<Integer>> uwlist = new TreeMap<Integer,ArrayList<Integer>>();
			while((line=rUR.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if((i/100000)%25==0)
						System.out.println(" ");
					outMax.flush();
					outSum.flush();
					outAve.flush();
				}
				String[]words = line.split(",");
				int uid = Integer.valueOf(words[0]);
				if(words.length==1){
					outSum.write(uid+","+0+"\n");
					outAve.write(uid+","+0+"\n");
					outMax.write(uid+","+0+"\n");
					outH.write(uid+","+0+"\n");
					outList.write(uid+","+0+"\n");
				}else{

					int sumNum = 0,maxNum=0;
					String[] reposid = words[1].split(" ");
					for(String rid:reposid){
						Integer repoid = Integer.valueOf(rid);
						Integer Num = userFollowersNumMap.get(repoid);
						if(Num==null)
							Num=0;
						sumNum += Num;
						if(maxNum<Num){
							maxNum = Num;
						}
					}
					double aveNum = (double)sumNum/reposid.length;
					outSum.write(uid+","+sumNum+"\n");
					outAve.write(uid+","+aveNum+"\n");
					outMax.write(uid+","+maxNum+"\n");
				
					ArrayList<Integer> watchersNumList = new ArrayList<Integer>();
					int h_index = 0;
			//		Integer uid = Integer.valueOf(words[0]);
					for(String rid:reposid){
						Integer repoid = Integer.valueOf(rid);
						Integer num = userFollowersNumMap.get(repoid);
						if(num==null)
							num=0;
						watchersNumList.add(num);
					}
					//排序
		 			Collections.sort(watchersNumList,new Comparator<Integer>(){
		 				public int compare(Integer i1,Integer i2){
		 					return i2.compareTo(i1);
		 				}
		 			});
					
					outList.write(words[0]);
					for(int j=1;j<watchersNumList.size()+1;j++){
						int num = watchersNumList.get(j-1);
						outList.write(","+num);
					}
					outList.write("\n");
					for(int j=1;j<watchersNumList.size()+1;j++){
						int num = watchersNumList.get(j-1);
						if(j>num){
							h_index = j-1;
							break;
						}
						
					}
					outH.write(words[0]+","+h_index+"\n");
				}
			//	break;
 			}
			System.out.println("users total num is "+i);
			
			outSum.close(); 
			outMax.close(); 
			outAve.close();
			outH.close();
			outList.close();
		}catch(Exception e){
			System.out.println(line);
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		UsersORG u = new UsersORG();
//		u.preproOrg_members();
//		u.orgFollowers();
	}

}
