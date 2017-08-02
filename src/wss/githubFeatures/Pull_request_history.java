package wss.githubFeatures;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import utils.BF;
import utils.Constants;

public class Pull_request_history {

	/**
	 * @param args
	 */
	
	public void splitPull_reuests_History(){//
		/**
		 * 对pull_request——history预处理。
		 * 以pull_requestID为主键，数数
		 */
		String line = null;
		int i=0;
        try{	
        	int count=0;
        	
        	ArrayList<BufferedWriter> outsList = new ArrayList<BufferedWriter>();
        	for(String action:actions){
        		BufferedWriter out = new BufferedWriter(new FileWriter
        				(Constants.screenFile+"tables\\"+action+"_Pull_Request.csv"));
        		outsList.add(out);
        	}
    		
    		BufferedReader read = BF.readFile(Constants.screenFile+"tables\\pull_request_history.csv");
    		ArrayList<Integer> pr_idList = new ArrayList<Integer> ();
    		while((line = read.readLine())!=null){
    			i++;
    			if(i%10000==0){
    				System.out.print(i/10000+" ");
    				if(i/10000%25==0){
    					System.out.println();
    				}
    				for(int j=0;j<actions.length;j++){
    					outsList.get(j).flush();
    				}
    			}
    			String[] words = line.split(",");
    			int id = Integer.valueOf(words[0]);
    			Integer pull_Request_id = Integer.valueOf(words[1]);
    			
    			String action = words[3];
    			for(int j=0;j<actions.length;j++){
    				String s = actions[j];
    				if(action.contains(s)){
    					outsList.get(j).write(line+"\n");
    				}
    			}
    		}
    		System.out.println("\n selected number : "+count);
    		for(int j=0;j<actions.length;j++){
				outsList.get(j).close();
			}
        }catch(Exception e){
        	System.out.println(line);
        	e.printStackTrace();
        }
		
	}
	ArrayList<String> actionsList = new ArrayList<String> ();
	{
		actionsList.add("opened");//0
		actionsList.add("merged");//1
		actionsList.add("closed");//2
		actionsList.add("reopened");//3
	}
//	public void Pull_reuests_HistoryFinal_Action(){//当前pull_request的终态
//		String line = null;
//		int i=0;
//        try{	
//        	int count=0;
//        	
//        	BufferedWriter out = new BufferedWriter(new FileWriter
//        			(Constants.screenFile+"tables\\Pull_Request_History_Action.csv"));
//        	TreeMap<Integer,Integer> PR_ActionsListMap = new TreeMap<Integer,Integer>();//pull_request,state
//	    	File usersFP = new File(Constants.dirname+"dump\\pull_request_history.csv");
//	    	System.out.println("read  "+usersFP.getName()+", waiting......");
//	    	BufferedReader reader = BF.readFile(usersFP,"utf-8");
//			while((line=reader.readLine())!=null){
//				i++;
//				if(i%100000==0){
//					System.out.print(i/100000+" ");
//					if(i/100000%25==0){
//						System.out.println();
//					}
//				}
//				String [] words = line.split(",");
//				int id = Integer.valueOf(words[0]);
//    			Integer pull_Request_id = Integer.valueOf(words[1]);
//    			Integer c = PR_ActionsListMap.get(pull_Request_id);
//    			Integer ac = actionsList.indexOf(words[3]);
//    			if(c==null){
//    				c = ac;
//    			}else{
//    				if(c==1&&ac==2){//merge碰到closed的情况，认为是merge
//    					c=1;
//    				}else{//否则为最终状态
//    					c = ac;
//    				}
//    			}
//    			PR_ActionsListMap.put(pull_Request_id,c);
//    			
//			}
//			System.out.println("\n repos number is "+PR_ActionsListMap.size());
//			
//    		BufferedReader read = BF.readFile(Constants.dirname+"dump\\pull_request_history.csv");
//    		ArrayList<Integer> pr_idList = new ArrayList<Integer> ();
//    		while((line = read.readLine())!=null){
//    			i++;
//    			if(i%10000==0){
//    				System.out.print(i/10000+" ");
//    				if(i/10000%25==0){
//    					System.out.println();
//    				}
//    				for(int j=0;j<actions.length;j++){
//    					outsList.get(j).flush();
//    				}
//    			}
//    			String[] words = line.split(",");
//    			int id = Integer.valueOf(words[0]);
//    			Integer pull_Request_id = Integer.valueOf(words[1]);
//    			
//    			String action = words[3];
//    			for(int j=0;j<actions.length;j++){
//    				String s = actions[j];
//    				if(action.contains(s)){
//    					outsList.get(j).write(line+"\n");
//    				}
//    			}
//    		}
//    		System.out.println("\n selected number : "+count);
//    		for(int j=0;j<actions.length;j++){
//				outsList.get(j).close();
//			}
//        }catch(Exception e){
//        	System.out.println(line);
//        	e.printStackTrace();
//        }
//		
//	}
	public void Pull_reuests_HistoryFinal_Action0(){//结合pull_request_history表，确定pull_request的状态
		try{
			String line = "";
			int i=0;
			TreeMap<Integer,String> PR_ActionMap = new TreeMap<Integer,String>();//pull_request,state
			File file = new File(Constants.screenFile+"tables\\pull_requestsHistory5.csv");
	    	System.out.println("read  "+file.getName()+", waiting......");
	    	BufferedReader reader = BF.readFile(file,"utf-8");
			while((line=reader.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%25==0){
						System.out.println();
					}
				}
//				if(i<=15000000){
//					continue;
//				}
				if(i>5000000){
					break;
				}
				String [] words = line.split(",");
    			Integer id = Integer.valueOf(words[0]);
    			Integer pull_Request_id = Integer.valueOf(words[1]);
    			
    			String time = words[2].substring(1,words[2].length()-1);
   				Date tempD = Constants.df.parse(time);
    			String tempaction = words[3].substring(1, words[3].length()-1);
    			
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
					new FileWriter(Constants.screenFile+"tables\\pull_requestsHistory6.csv",true));
			for(Map.Entry<Integer, String> m:PR_ActionMap.entrySet()){
				out2.write(m.getValue()+"\n");
			}
			out2.close();
//			BufferedWriter out = new BufferedWriter(
//					new FileWriter(Constants.screenFile+"tables\\pull_requests.csv"));
//			i=0;
//			int count=0;
//			File f = new File(Constants.dirname+"dump\\pull_request_history.csv");
//			System.out.println(f.getName());
//			BufferedReader r1 = BF.readFile(f);
//			while((line = r1.readLine())!=null){
//				i++;
//				if(i%1000000==0){
//					System.out.print(i/1000000+" ");
//					if(i/1000000%25==0){
//						System.out.println();
//					}
//				}
//				String[] words = line.split(",");
//				Integer id = Integer.valueOf(words[0]);
//				Integer ac = PR_ActionsListMap.get(id);
//				if(ac==null){
//					out2.write(line+",\\N\n");
//					count++;
//				}else{
//					out.write(line+","+ac+"\n");
//				}
//			}
//			System.out.println("\n所有pull_request数量 ： "+i);
//			System.out.println("merge 为空的pull_request数量 ： "+count);
//			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
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
	public void CheckPull_reuests_History(){//以pull_request——id排序
		try{
			String line = "";
			int i=0;
		//	TreeMap<Integer,String> PR_ActionMap = new TreeMap<Integer,String>();//pull_request,state
			TreeSet<Integer> idSet = new TreeSet<Integer>();
			File usersFP = new File(Constants.screenFile+"tables\\pull_requestsHistory5.csv");
	    	System.out.println("read  "+usersFP.getName()+", waiting......");
	    	BufferedReader reader = BF.readFile(usersFP,"utf-8");
			while((line=reader.readLine())!=null){
				i++;
				if(i%100000==0){
					System.out.print(i/100000+" ");
					if(i/100000%25==0){
						System.out.println();
					}
				}
//				if(i<=15000000){
//					continue;
//				}
//				if(i>20000000){
//					break;
//				}
				String [] words = line.split(",");
				Integer id = Integer.valueOf(words[0]);
    			Integer pull_Request_id = Integer.valueOf(words[1]);
    			idSet.add(pull_Request_id);
    		
			}
			System.out.println("\n "+idSet.size()+"..."+i);

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void screenOpenPR(){
		String line = null;
		int i=0;
        try{	
        	TreeMap<Integer,Integer> authorPRsNum = new TreeMap<Integer,Integer>();
        	File usersFP = new File(Constants.path+"\\tables\\users.csv");
        	BufferedReader reader = BF.readFile(usersFP,"utf8");
        	System.out.println("read  "+usersFP.getName()+", waiting......");
    		while((line=reader.readLine())!=null){
    			i++;
    			if(i%10000==0){
    				System.out.print(i/10000+" ");
    				if(i/10000%25==0){
    					System.out.println();
    				}
    			}
    			String [] words = line.split(",");
    			int userid = Integer.valueOf(words[0]);;
    			authorPRsNum.put(userid, 0);
    		}
    		System.out.println("\n USERs number is "+authorPRsNum.size());
    		
    		i=0;
    		BufferedWriter out = new BufferedWriter(new FileWriter(Constants.path+"OpenPR_User.csv"));
    		BufferedReader r1 = BF.readFile(Constants.path+"openPull_Request.csv");
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
    			if(words[4].contains("\\N"))
    			{
    				continue;
    			}
    			int actorId = Integer.valueOf(words[4]);
    			
    			Integer c = authorPRsNum.get(actorId);
    			if(c==null){
    				continue;
    			}else{
    				out.write(line+"\n");
    			}
    		}
    		System.out.println("\n number : "+i);
    		
    		out.close();
        }catch(Exception e){
        	e.printStackTrace();
        }
		
	}
	public void userPull_reuests_HNum(String action){
		String line = null;
		int i=0;
        try{	
        	TreeMap<Integer,Integer> authorPRsNum = new TreeMap<Integer,Integer>();
        	File usersFP = new File(Constants.screenFile+"tables\\users.csv");
        	BufferedReader reader = BF.readFile(usersFP,"utf8");
        	System.out.println("read  "+usersFP.getName()+", waiting......");
    		while((line=reader.readLine())!=null){
    			i++;
    			if(i%100000==0){
    				System.out.print(i/100000+" ");
    				if(i/100000%25==0){
    					System.out.println();
    				}
    			}
    			String [] words = line.split(",");
    			int userid = Integer.valueOf(words[0]);;
    			authorPRsNum.put(userid, 0);
    		}
    		System.out.println("\n USERs number is "+authorPRsNum.size());
    		
    		i=0;
    		BufferedReader r1 = BF.readFile(Constants.screenFile+"tables\\"+action+"_Pull_Request.csv");
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
    			int actorId = Integer.valueOf(words[4]);

    			Integer c = authorPRsNum.get(actorId);
    			if(c==null){
    				System.out.println(line);
    				continue;
    			}else{
    				c++;
    				authorPRsNum.put(actorId, c);
    			}
    		}
    		System.out.println("\nnumber : "+i);
    		System.out.println(" users 数量 : "+authorPRsNum.size());
    		
    		BufferedWriter out = new BufferedWriter(new FileWriter
    				(Constants.screenFile+"features\\user_"+action+"_Pull_requestHNum.csv"));
    		for(Map.Entry<Integer, Integer> t:authorPRsNum.entrySet()){
    			int rid =t.getKey();
    			out.write(rid+","+t.getValue()+"\n");
    		}
    		out.close();
        }catch(Exception e){
        	e.printStackTrace();
        }
		
	}
	File usrFile = new File(Constants.dumpFile+"features\\USR\\usersInfo.csv");
	File orgFile = new File(Constants.dumpFile+"features\\ORG\\usersInfo.csv");
	public void userOpenPull_request_historyNumMonth() throws Exception{
		DecimalFormat doubleDF = new DecimalFormat("######0.00");   
		Date currentTime =  Constants.df.parse( Constants.currentTime);
		Date startTime =  Constants.df.parse(Constants.startTime);
		int m = currentTime.getMonth();
		int y = currentTime.getYear();
		
		int maxMonth = 106;//最多有多少个月
		BufferedWriter out = new BufferedWriter(new FileWriter
				(pull_request_historyFile.getParent()+"\\features\\Monthly\\userOpen_Pull_requestH_MonthNum.csv"));
		TreeMap<Integer,TreeMap<Integer,Integer>> authorIssuesNum = new TreeMap<Integer,TreeMap<Integer,Integer>>();
		String line = "";
		int i=0;
	//	File usersFP = new File(Constants.screenFile+"tables\\users.csv");
    	BufferedReader reader = BF.readFile(usrFile,"utf8");
    	System.out.println("read  "+usrFile.getName()+", waiting......");
		while((line=reader.readLine())!=null){
			i++;
			if(i%100000==0){
				System.out.print(i/100000+" ");
				if(i/100000%25==0){
					System.out.println();
				}
			}
			String [] words = line.split(",");
			int userid = Integer.valueOf(words[0]);
			TreeMap<Integer,Integer> t = new TreeMap<Integer,Integer> ();
//			for(int j=0;j<106;j++){
//				t.put(j, 0);
//			}
			authorIssuesNum.put(userid,t);
		}
		System.out.println("\n USERs number is "+authorIssuesNum.size());
		reader = BF.readFile(orgFile,"utf8");
    	System.out.println("read  "+orgFile.getName()+", waiting......");
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
			int userid = Integer.valueOf(words[0]);;
			TreeMap<Integer,Integer> t = new TreeMap<Integer,Integer> ();
//			for(int j=0;j<106;j++){
//				t.put(j, 0);
//			}
			authorIssuesNum.put(userid,t);
		}
		System.out.println("\n USERs number is "+authorIssuesNum.size());
	//	File f = new File(Constants.screenFile+"tables\\"+action+"_Pull_Request.csv");
		System.out.println(pull_request_historyFile.getName());
		BufferedReader r1 = BF.readFile(pull_request_historyFile);
		i=0;
		while((line = r1.readLine())!=null){
			i++;
			if(i%100000==0){
				System.out.print(i/100000+" ");
				if(i/100000%25==0){
					System.out.println();
				}
			}
			String[] words = line.split(",");
		//	System.out.println(line);
			int cid = Integer.valueOf(words[0]);
			
			String action = words[3];
			if(!action.equals("\"opened\"")){
				continue;
			}
			if(words[4].contains("\\N")){
				continue;
			}
			int authorid = Integer.valueOf(words[4]);
			
			Date d = Constants.df.parse(words[2].substring(1,words[2].length()-1));
			if(d.compareTo(startTime)<0||d.compareTo(currentTime)>0){//
				continue;
			}
			int month = d.getMonth();
			int year = d.getYear();
			int index = (y-year)*12+(m-month);
			
			TreeMap<Integer,Integer> c = authorIssuesNum.get(authorid);
			if(c==null){
//				c = new TreeMap<Integer,Integer>();
//				c.put(index, 1);
				continue;
			}else{
				Integer cc = c.get(index);
				if(cc==null){
					cc=0;
				//	continue;
				}
				cc++;
				c.put(index, cc);
			}
			authorIssuesNum.put(authorid, c);
		}
		System.out.println(i);
		System.out.println(authorIssuesNum.size());
//		for(Map.Entry<Integer, TreeMap<Integer,Integer>> t:authorIssuesNum.entrySet()){
//			TreeMap<Integer,Integer> c= t.getValue();
//			if(maxMonth<c.size()){
//				maxMonth = c.size();
//			}
//		}
		for(Map.Entry<Integer, TreeMap<Integer,Integer>> t:authorIssuesNum.entrySet()){
			int rid =t.getKey();
			out.write(rid+",");
			TreeMap<Integer,Integer> c= t.getValue();
			for(int j=0;j<maxMonth;j++){
				
				Integer cNum = c.get(j);
				if(cNum==null){
					cNum=0;
				}
				out.write(cNum+" ");
			}
			out.write("\n");
		}
		out.close();
	}
	public void getMouthData(int n){//获取近N个月的数据
		String line = "";
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter
					(pull_request_historyFile.getParent()+"\\features\\"+n+"Month\\user"+n+"MonthOpen_Pull_requestsNum.csv"));
			int i=0;
			File f = new File(pull_request_historyFile.getParent()+"\\features\\Monthly\\userOpen_Pull_requestH_MonthNum.csv");
			System.out.println(f.getName());
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
				String id = words[0];
				String [] months = words[1].split(" ");
				int sum = 0;
				for(int j=0;j<n;j++){
					sum += Integer.valueOf(months[j]);
				}
				out.write(id+","+sum+"\n");
			}
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	String[] actions = {"opened","merged","closed","reopened"};
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Pull_request_history p = new Pull_request_history();
	//	p.splitPull_reuests_History();
	
//		String action = p.actions[0];
//		p.userPull_reuests_HNum(action);
//		p.userOpenPull_request_historyNumMonth();
//		p.getMouthData(3);
//		p.getMouthData(12);
		p.Pull_reuests_historyFinalAction();
//		p.CheckPull_reuests_History();
		
	}

}
