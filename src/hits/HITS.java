package hits;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import readTable.ReadWatchers;
import utils.BF;

public class HITS {

	/**
	 * @param args
	 */
	double max = 0.00000001;//停止迭代的差值临界点
	int iter = 1000;//默认迭代次数
	
	public static Map<Integer,Double > NodeAuthMap = new HashMap<Integer,Double>();//authority
    public static Map<Integer,Double > NodeHubMap = new HashMap<Integer,Double>();//hub
    
	TreeMap<Integer,ArrayList<Integer> > NodeIncomingsMap = new TreeMap<Integer,ArrayList<Integer>>();//入度列表
	TreeMap<Integer,ArrayList<Integer> > NodeOutgoingsMap = new TreeMap<Integer,ArrayList<Integer>>();//出度列表

 //   public Set<Node> nodeSet = new HashSet<Node>();
    public Set<Integer> nodeSet2 = new HashSet<Integer>();
 //   File NetFile = new File("");
    public void getInOuts(File NetFile){//输出节点的链出列表和链入列表
    	try{
    		System.out.print("getInOuts .....");
    		BufferedReader read = BF.readFile(NetFile);
    		String line = "";
    		
    		while((line=read.readLine())!=null){
    			String[] words = line.split(",");
    			Integer node1 = Integer.valueOf(words[0]);
    			Integer node2 = Integer.valueOf(words[1]);//node1指向node
    			ArrayList<Integer> outgoings = NodeOutgoingsMap.get(node1);
    			if(outgoings==null){
    				outgoings = new ArrayList<Integer>();
    			}
    			outgoings.add(node2);
    			NodeOutgoingsMap.put(node1, outgoings);
    			
    			ArrayList<Integer> incomings = NodeIncomingsMap.get(node2);
    			if(incomings==null){
    				incomings = new ArrayList<Integer>();
    			}
    			incomings.add(node1);
    			NodeIncomingsMap.put(node2, incomings);
    		}
//    		BufferedWriter out = new BufferedWriter(new FileWriter("data\\NodeOutgoings.csv"));
//    		for(Map.Entry<Integer, ArrayList<Integer>> m:NodeOutgoingsMap.entrySet()){
//    			out.write(m.getKey()+",");
//    			for(Integer going:m.getValue()){
//    				out.write(going+" ");
//    			}
//    			out.write("\n");
//    		}
//    		out.close();
//    		BufferedWriter out2 = new BufferedWriter(new FileWriter("data\\NodeIncomings.csv"));
//    		for(Map.Entry<Integer, ArrayList<Integer>> m:NodeIncomingsMap.entrySet()){
//    			out2.write(m.getKey()+",");
//    			for(Integer coming:m.getValue()){
//    				out2.write(coming+" ");
//    			}
//    			out2.write("\n");
//    		}
//    		out2.close();
    		System.out.println("down");
    	}catch(Exception e){
    		e.printStackTrace();
    	}
		
    }
    public void getNode(File NetFile){
    	System.out.print("getNode .....");
    	try{
    		BufferedReader read = BF.readFile(NetFile);
    		String line = "";
    		while((line=read.readLine())!=null){
    			String[] words = line.split(",");
    			nodeSet2.add(Integer.valueOf(words[0]));
    			nodeSet2.add(Integer.valueOf(words[1]));
    		}
    		System.out.println(nodeSet2.size());
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void init( File NetFile){//初始化，为1  
    	System.out.println("init.....");
    	getNode(NetFile);
    	getInOuts(NetFile);
		for(Integer n:nodeSet2){
			NodeAuthMap.put(n, 1.0);
			NodeHubMap.put(n, 1.0);
		}
    }
    public ArrayList<Integer> getIncomingNeighbors(Integer node){
		try{
			int id = node;
    		for(Map.Entry<Integer, ArrayList<Integer>> m:NodeIncomingsMap.entrySet()){
    			int nodeid = m.getKey();
    			if(nodeid==id){
    				return m.getValue();
    			}
    		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public ArrayList<Integer> getOutgoingNeighbors(Integer node){
		try{
			int id = node;
    		for(Map.Entry<Integer, ArrayList<Integer>> m:NodeOutgoingsMap.entrySet()){
    			int nodeid = m.getKey();
    			if(nodeid==id){
    				return m.getValue();
    			}
    		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
    public void doHits(){
    	double normMax = 0;//用于归一化
    //	System.out.println("\t doHITS ");
    //	print();
    	//计算Auth
    	int i =0;
    	for(Integer n:nodeSet2){
//    		i++;
//    		if(i%1000==0){
//    			System.out.println(i/1000);
//    		}
			double auth = 0.0;
			ArrayList<Integer> incomingsList = NodeIncomingsMap.get(n);
			if(incomingsList!=null){
				for(Integer in:incomingsList){
					auth += NodeHubMap.get(in);
				}
			}
	//		System.out.println(n+"  "+auth);
			NodeAuthMap.put(n, auth);
			if(normMax<auth){
				normMax = auth;
			}
    	}
    //	System.out.println(norm);
    	for(Map.Entry<Integer, Double> m:NodeAuthMap.entrySet()){
    		double auth = m.getValue();
    		auth = auth/normMax;
    		NodeAuthMap.put(m.getKey(), auth);
    	}
    	normMax = 0;
    	i=0;
    	//计算Hub
    	for(Integer n:nodeSet2){
//    		i++;
//    		if(i%1000==0){
//    			System.out.println(i/1000);
//    		}
    		double hub = 0.0;
    		ArrayList<Integer> outgoingsList = NodeOutgoingsMap.get(n);
    		if(outgoingsList!=null){
    			for(Integer out:outgoingsList){
        			hub += NodeAuthMap.get(out);
        		}
    		}
    		
    		NodeHubMap.put(n, hub);
    		if(normMax<hub){
				normMax = hub;
			}
    	}
    	for(Map.Entry<Integer, Double> m:NodeHubMap.entrySet()){
    		double hub = m.getValue();
    		hub = hub/normMax;
    		NodeHubMap.put(m.getKey(), hub);
    	}
    }
   public void print(){//结果打印
	   for(Integer n:nodeSet2){
		   System.out.println(n+" : "+NodeAuthMap.get(n)+"---"+NodeHubMap.get(n));
	   }
	   System.out.println();
   }
   public void write(){//结果输出
	   try{
		   BufferedWriter out = new BufferedWriter(new FileWriter(netFile.getName().replaceAll(".csv", "")+"_NodeAuthHub.csv"));
		   out.write("id,auth,hub\n");
		   for(Integer n:nodeSet2){
			   out.write(n+","+NodeAuthMap.get(n)+","+NodeHubMap.get(n)+"\n");
		   }
		   out.close();
	   }catch(Exception e){
		   e.printStackTrace();
	   }
	  
   }
   //循环迭代
   public void reHITS(){
	   
		System.out.println("开始HITS的迭代计算~~~~~~~");
		
		boolean flag = false;
		Map<Integer,Double > CheckAuthMap = new HashMap<Integer,Double>();//authority
		Map<Integer,Double > CheckHubMap = new HashMap<Integer,Double>();//hub
		int i=0;//记录迭代次数
		while(!flag){
			Iterator<Integer> t1 = NodeAuthMap.keySet().iterator();
			while(t1.hasNext()){
				Integer uid = t1.next();
				CheckAuthMap.put(uid, NodeAuthMap.get(uid));
		  		}
		  	Iterator<Integer> t2 = NodeHubMap.keySet().iterator();
		  	while(t2.hasNext()){
				Integer uid = t2.next();
				CheckHubMap.put(uid, NodeHubMap.get(uid));
		  	}
		  	
		  	i++;
		  	System.out.print(i+" ");
		  	if(i%10==0){
		  		System.out.println();
		  	}
		  	doHits();
		  	Iterator<Integer> t3 = NodeAuthMap.keySet().iterator();
	  		while(t3.hasNext()){
				Integer uid = t3.next();
				double da = NodeAuthMap.get(uid)-CheckAuthMap.get(uid);
				if(Math.abs(da)>max){
					flag = false;
					break;
				}
				double dh = NodeHubMap.get(uid)-CheckHubMap.get(uid);
				if(Math.abs(dh)>max){
					flag = false;
					break;
				}
				flag = true;
	  		}
		}
	//	print();
		write();
        System.out.println("总共进行了    "+i+"次迭代。");
   }
   public void reHITS(int iter){
	   
		System.out.println("开始HITS的迭代计算~~~~~~~");
		boolean flag = false;
		Map<Integer,Double > CheckAuthMap = new HashMap<Integer,Double>();//authority
		Map<Integer,Double > CheckHubMap = new HashMap<Integer,Double>();//hub
		int i=0;//记录迭代次数
		while(iter>0){
		  	doHits();
		  	iter--;
		}
     //  System.out.println("总共进行了    "+i+"次迭代。");
  }
   public void clear(){
	   File incomingFile = new File("NodeIncomings.csv");
	   incomingFile.delete();
	   File outgoingFile = new File("NodeOutgoings.csv");
	   outgoingFile.delete();
   }
	File netFile = new File("FollowNet.csv");
	public void getFollowNet(){
		try{
			 BufferedWriter out = new BufferedWriter(new FileWriter("FollowNet.csv"));

			File f = new File("E:\\mysql-2015-09-25\\screen\\tables\\followers0.csv");
			String line = "";
			BufferedReader read = BF.readFile(f);
    		while((line=read.readLine())!=null){
    			String[] words = line.split(",");
    			out.write(words[1]+","+words[0]+"\n");
    		}
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HITS h = new HITS();
		h.getFollowNet();
		h.init(h.netFile);
		h.reHITS();
		h.write();
	//	h.clear();
	}

}
