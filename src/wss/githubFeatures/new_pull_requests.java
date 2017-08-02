package wss.githubFeatures;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Map;
import java.util.TreeMap;

import utils.BF;
import utils.Constants;

public class new_pull_requests {

	/**
	 * @param args
	 */
	File new_pull_requestsFile = new File(Constants.dumpFile+"new_pull_requests.csv");
	/*
	 * id,head,base,created_At,action,actor
	 * */
	public void headrepoPull_requestsNum(){//项目提出的pull_request数量，head_repo_id
		try{
			String line = "";
			int i=0,count=0;
			int k=0;
			TreeMap<Integer,Integer> repoNumMap = new TreeMap<Integer,Integer>();
			TreeMap<Integer,Integer> repoMergedNumMap = new TreeMap<Integer,Integer>();
	    	System.out.println("read  "+new_pull_requestsFile.getName()+", waiting......");
	    	BufferedReader reader = BF.readFile(new_pull_requestsFile,"utf-8");
			while((line=reader.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%25==0){
						System.out.println();
					}
				}
				String[] words = line.split(",");
				int pull_requestId = Integer.valueOf(words[0]);
				if(words[1].equals("\\N")){
					count++;
					continue;
				}
				int head_repoId = Integer.valueOf(words[1]);
				String action = words[4];
				Integer c = repoNumMap.get(head_repoId);
				if(c==null){
					c=0;
				}
				c++;
				repoNumMap.put(head_repoId, c);
				if(action.contains("merged")){
					Integer cc = repoMergedNumMap.get(head_repoId);
					if(cc==null){
						cc=0;
					}
					cc++;
					repoMergedNumMap.put(head_repoId, cc);
					k++;
				}
			}
			System.out.println("\n所有pull_request数量 ： "+i);
			System.out.println("head_repoId  为空的pull_request数量 ： "+count);
			System.out.println("涉及repo数量 ： "+repoNumMap.size());
			
			BufferedWriter out = new BufferedWriter(new FileWriter
					(new_pull_requestsFile.getParent()+"\\features\\repoPull_requestNumMergedNum.csv"));
			for(Map.Entry<Integer, Integer> t:repoNumMap.entrySet()){
				int rid =t.getKey();
//				Integer n = t.getValue();
//				if(n!=0){
//					k++;
//				}
				Integer mergedNum = repoMergedNumMap.get(rid);
				if(mergedNum==null){
					mergedNum=0;
				}
				out.write(t.getKey()+","+t.getValue()+","+mergedNum+"\n");
			}
			out.close();
			System.out.println("merged 有 pull_request ： "+k);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	File forksFile = new File(Constants.dumpFile+"forks.csv");
	public void forkRepoPull_requestsNum(){//项目提出的pull_request数量，head_repo_id
		try{
			String line = "";
			int i=0,count=0;
			int k=0;
			System.out.println("read  "+forksFile.getName()+", waiting......");
	    	BufferedReader reader = BF.readFile(forksFile,"utf-8");
	    	TreeMap<Integer,Integer> repoForkedFromMap = new TreeMap<Integer,Integer>();
			while((line=reader.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%25==0){
						System.out.println();
					}
				}
				String[] words = line.split(",");
				Integer rid = Integer.valueOf(words[0]);
				Integer forkedFrom = Integer.valueOf(words[2]);
				repoForkedFromMap.put(rid, forkedFrom);
			}
			System.out.println("\n repoForkedFromMap  "+repoForkedFromMap.size()+", waiting......");
			TreeMap<Integer,Integer> repoNumMap = new TreeMap<Integer,Integer>();
			TreeMap<Integer,Integer> repoMergedNumMap = new TreeMap<Integer,Integer>();
//			BufferedWriter out = new BufferedWriter(new FileWriter
//					(new_pull_requestsFile.getParent()+"\\features\\pull_request2.csv"));
	    	System.out.println("\nread  "+new_pull_requestsFile.getName()+", waiting......");
	    	reader = BF.readFile(new_pull_requestsFile,"utf-8");
	    	i=0;
			while((line=reader.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%25==0){
						System.out.println();
					}
				//	break;
				}
				String[] words = line.split(",");
				Integer pull_requestId = Integer.valueOf(words[0]);
				if(words[1].equals("\\N")){
					count++;
					continue;
				}
				Integer head_repoId = Integer.valueOf(words[1]);
				Integer base_repoId = Integer.valueOf(words[2]);
				if(repoForkedFromMap.containsKey(head_repoId)){
					Integer tempForked = repoForkedFromMap.get(head_repoId);
				//	out.write(pull_requestId+","+head_repoId+","+tempForked+","+base_repoId+"\n");
					if(tempForked.equals(base_repoId)){
						String action = words[4];
						Integer c = repoNumMap.get(head_repoId);
						if(c==null){
							c=0;
						}
						c++;
						repoNumMap.put(head_repoId, c);
						if(action.contains("merged")){
							Integer cc = repoMergedNumMap.get(head_repoId);
							if(cc==null){
								cc=0;
							}
							cc++;
							repoMergedNumMap.put(head_repoId, cc);
							k++;
						}
					}
				}
			}
			System.out.println("\n所有pull_request数量 ： "+i);
			System.out.println("head_repoId  为空的pull_request数量 ： "+count);
			System.out.println("涉及repo数量 ： "+repoNumMap.size());
			
			BufferedWriter out = new BufferedWriter(new FileWriter
					(new_pull_requestsFile.getParent()+"\\features\\forkrepoPull_requestNumMergedNum.csv"));
			for(Map.Entry<Integer, Integer> t:repoNumMap.entrySet()){
				int rid =t.getKey();
//				Integer n = t.getValue();
//				if(n!=0){
//					k++;
//				}
				Integer mergedNum = repoMergedNumMap.get(rid);
				if(mergedNum==null){
					mergedNum=0;
				}
				out.write(t.getKey()+","+t.getValue()+","+mergedNum+"\n");
			}
			out.close();
			System.out.println("merged 有 pull_request ： "+k);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void userPull_requestsNum(){//项目提出的pull_request数量，head_repo_id
		try{
			String line = "";
			int i=0,count=0;
			int k=0;
			System.out.println("read  "+forksFile.getName()+", waiting......");
	    	BufferedReader reader = BF.readFile(forksFile.getParent()+"\\userReposList.csv");
	    	TreeMap<Integer,Integer> repoForkedFromMap = new TreeMap<Integer,Integer>();
			while((line=reader.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%25==0){
						System.out.println();
					}
				}
				String[] words = line.split(",");
				Integer rid = Integer.valueOf(words[0]);
				Integer forkedFrom = Integer.valueOf(words[2]);
				repoForkedFromMap.put(rid, forkedFrom);
			}
			System.out.println("\n repoForkedFromMap  "+repoForkedFromMap.size()+", waiting......");
			TreeMap<Integer,Integer> repoNumMap = new TreeMap<Integer,Integer>();
			TreeMap<Integer,Integer> repoMergedNumMap = new TreeMap<Integer,Integer>();
//			BufferedWriter out = new BufferedWriter(new FileWriter
//					(new_pull_requestsFile.getParent()+"\\features\\pull_request2.csv"));
	    	System.out.println("\nread  "+new_pull_requestsFile.getName()+", waiting......");
	    	reader = BF.readFile(new_pull_requestsFile,"utf-8");
	    	i=0;
			while((line=reader.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%25==0){
						System.out.println();
					}
				//	break;
				}
				String[] words = line.split(",");
				Integer pull_requestId = Integer.valueOf(words[0]);
				if(words[1].equals("\\N")){
					count++;
					continue;
				}
				Integer head_repoId = Integer.valueOf(words[1]);
				Integer base_repoId = Integer.valueOf(words[2]);
				if(repoForkedFromMap.containsKey(head_repoId)){
					Integer tempForked = repoForkedFromMap.get(head_repoId);
				//	out.write(pull_requestId+","+head_repoId+","+tempForked+","+base_repoId+"\n");
					if(tempForked.equals(base_repoId)){
						String action = words[4];
						Integer c = repoNumMap.get(head_repoId);
						if(c==null){
							c=0;
						}
						c++;
						repoNumMap.put(head_repoId, c);
						if(action.contains("merged")){
							Integer cc = repoMergedNumMap.get(head_repoId);
							if(cc==null){
								cc=0;
							}
							cc++;
							repoMergedNumMap.put(head_repoId, cc);
							k++;
						}
					}
				}
			}
			System.out.println("\n所有pull_request数量 ： "+i);
			System.out.println("head_repoId  为空的pull_request数量 ： "+count);
			System.out.println("涉及repo数量 ： "+repoNumMap.size());
			
			BufferedWriter out = new BufferedWriter(new FileWriter
					(new_pull_requestsFile.getParent()+"\\features\\forkrepoPull_requestNumMergedNum.csv"));
			for(Map.Entry<Integer, Integer> t:repoNumMap.entrySet()){
				int rid =t.getKey();
//				Integer n = t.getValue();
//				if(n!=0){
//					k++;
//				}
				Integer mergedNum = repoMergedNumMap.get(rid);
				if(mergedNum==null){
					mergedNum=0;
				}
				out.write(t.getKey()+","+t.getValue()+","+mergedNum+"\n");
			}
			out.close();
			System.out.println("merged 有 pull_request ： "+k);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new_pull_requests n = new new_pull_requests();
	//	n.headrepoPull_requestsNum();
		n.forkRepoPull_requestsNum();
	}

}
