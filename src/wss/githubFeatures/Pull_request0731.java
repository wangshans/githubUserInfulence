package wss.githubFeatures;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import utils.BF;
import utils.Constants;

public class Pull_request0731 {

	/**
	 * @param args
	 * 1.pull_request_history,分为两个，一个是终态，一个是创建
	 * 
	 * 1.与pull_request_history 结合，得到：id，head_repo_id,base_repo_id,action,actor_id,created_at
	 * pull_request里的id与pull_rquest_history里的pull_request_id对应
	 * 
	 */
	File pull_request_historyFile = new File(Constants.dumpFile+"pull_request_history.csv");
	public void Pull_reuests_Historysplit10(){//按pull_request_id分成10份
		try{
			System.out.print("pull_request_history is splited to 10 files, ");
			String line = "";
			int i=0;
			BufferedReader reader = BF.readFile(pull_request_historyFile,"utf-8");
	    	i=0;
			while((line=reader.readLine())!=null){
				i++;
			}
			System.out.println("i..."+i);
			int per = 2100000;
//			int per = ((i/100000)+1)*100000/10;
//			System.out.println("per..."+per);
		//	ArrayList<BufferedWriter> outList = new ArrayList<BufferedWriter>();
			for(int j=0;j<10;j++){
				BufferedWriter out = new BufferedWriter(
						new FileWriter(pull_request_historyFile.getParent()+"\\p_rH_"+j+".csv",true));
	//			outList.add(out);
			//	TreeSet<Integer> pull_requestIDSet = new TreeSet<Integer>();
		    	System.out.println("read  "+pull_request_historyFile.getName()+", waiting......");
		    	reader = BF.readFile(pull_request_historyFile,"utf-8");
		    	i=0;
		    	System.out.println(per*j+" to "+per*(j+1));
				while((line=reader.readLine())!=null){
					i++;
					if(i%100000==0){
						System.out.print(i/100000+" ");
						if(i/100000%25==0){
							System.out.println();
						}
					}
					String [] words = line.split(",");
					
	    			Integer id = Integer.valueOf(words[0]);
	    			Integer pull_Request_id = Integer.valueOf(words[1]);
	    		
	    			if(pull_Request_id<=per*(j+1)&&pull_Request_id>per*j){
	    				out.write(line+"\n");
	    			}
				}
				out.close();
			}
			int count=0,sum=0;
			
//			out2.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void Pull_reuests_historyFinalAction(){//按时间先后确定终态
		try{
			String line = "";
			int i=0;
		//	Pull_reuests_Historysplit10();
			for(int j=0;j<10;j++){
				BufferedReader reader = BF.readFile(pull_request_historyFile.getParent()+"\\p_rH_"+j+".csv");
				TreeMap<Integer,String> PR_ActionMap = new TreeMap<Integer,String>();//pull_request,state
				System.out.println("read  "+j+", waiting......");
				i=0;
				while((line=reader.readLine())!=null){
					i++;
					if(i%100000==0){
						System.out.print(i/100000+" ");
						if(i/100000%25==0){
							System.out.println();
						}
					}
					String [] words = line.split(",");
	    			Integer id = Integer.valueOf(words[0]);
	    			Integer pull_Request_id = Integer.valueOf(words[1]);
	    			
	    			String time = words[2].substring(1,words[2].length()-1);
	   				Date tempD = Constants.df.parse(time);
	    		//	String tempaction = words[3].substring(1, words[3].length()-1);
	    			
	    			String lastLine = PR_ActionMap.get(pull_Request_id);//已经存在Line
	    			
	    			if(lastLine==null){
	    				PR_ActionMap.put(pull_Request_id,line);
	    			}else{
	    				String lastTime = lastLine.split(",")[2];
	        			lastTime = lastTime.substring(1,lastTime.length()-1);
	        			Date lastD = Constants.df.parse(lastTime);
	    				if(tempD.compareTo(lastD)>0){//越新越大
	    					PR_ActionMap.put(pull_Request_id,line);
	    				}
	    			}
				}
				System.out.println("\n "+PR_ActionMap.size()+"..."+i);
				BufferedWriter out2 = new BufferedWriter(
						new FileWriter(pull_request_historyFile.getParent()+"\\pull_requests_history_final.csv",true));
				for(Map.Entry<Integer, String> m:PR_ActionMap.entrySet()){
					out2.write(m.getValue()+"\n");
				}
				out2.close();
				PR_ActionMap.clear();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	File pull_request_history_finalFile = new File(Constants.dumpFile+"pull_requests_history_final.csv");
	File pull_request_history_openFile = new File(Constants.dumpFile+"pull_requests_history_open.csv");
	File pull_requestFile = new File(Constants.dumpFile+"pull_requests.csv");
	public void getNewPull_request(File historyFile){
		/*
		 * 组合pull_request和pull_request_history表，
		 * 输出：id，head_repo_id,base_repo_id,created_At,action,actor
		 */
		try{
			String line = "";
			int i=0,per = 5000000;
			int count=0,cnull = 0;
			for(int j=0;j<4;j++){
				System.out.println(j*per+"....."+per*(j+1));
				TreeMap<Integer,String> PR_ActionMap = new TreeMap<Integer,String>();//pull_request,state
				//	TreeSet<Integer> MergedSet = new TreeSet<Integer>();//pull_request,state
			    	System.out.println("read  "+historyFile.getName()+", waiting......");
			    	BufferedReader reader = BF.readFile(historyFile,"utf-8");
			    	i=0;
					while((line=reader.readLine())!=null){
						i++;
						if(i%100000==0){
							System.out.print(i/100000+" ");
							if(i/100000%25==0){
								System.out.println();
							}
						}
						if(i<=j*per)
							continue;
					//	if(i>j*per&&i<=per*(j+1)){
							String [] words = line.split(",");
							Integer pull_Request_id = Integer.valueOf(words[1]);
							String tempLine = words[2]+","+words[3]+","+words[4];
							PR_ActionMap.put(pull_Request_id,tempLine);
				//		}
						if(i>per*(j+1)){
							break;
						}
			
					}
					System.out.println("\n PR_ActionMap number is "+PR_ActionMap.size());
					BufferedWriter out = new BufferedWriter(
							new FileWriter(historyFile.getPath().replaceAll("_history", ""),true));
					i=0;
					
					System.out.println(pull_requestFile.getName());
					BufferedReader r1 = BF.readFile(pull_requestFile);
					while((line = r1.readLine())!=null){
						i++;
						if(i%1000000==0){
							System.out.print(i/1000000+" ");
							if(i/1000000%25==0){
								System.out.println();
							}
						}
						String[] words = line.split(",");
						Integer id = Integer.valueOf(words[0]);
						String tempLine = PR_ActionMap.get(id);
						if(tempLine==null){
							cnull++;
							continue;
						}else{
							out.write(id+","+words[1]+","+words[2]+","+tempLine+"\n");
							count++;
						}
					}
					System.out.println("\n所有pull_request数量 ： "+i);
					
					out.close();
			}
			System.out.println("new pull_request数量 ： "+count);
			System.out.println("null pull_request数量 ： "+cnull);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Pull_request0731 pr = new Pull_request0731();
		pr.getNewPull_request(pr.pull_request_history_openFile);
	}

}
