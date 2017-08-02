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

public class HITS2 {

	/**
	 * @param args
	 */
	double max = 0.00000001;//停止迭代的差值临界点
	int iter = 1000;//默认迭代次数
	
	public static Map<Integer,Double > NodeAuthMap = new HashMap<Integer,Double>();//authority
    public static Map<Integer,Double > NodeHubMap = new HashMap<Integer,Double>();//hub
    
 //   public Set<Node> nodeSet = new HashSet<Node>();
    public Set<Integer> nodeSet2 = new HashSet<Integer>();
 //   File NetFile = new File("");
    public void getInOuts(File NetFile){//输出节点的链出列表和链入列表
    	try{
    		System.out.print("getInOuts .....");
    		BufferedReader read = BF.readFile(NetFile);
    		String line = "";
    		TreeMap<Integer,ArrayList<Integer> > NodeIncomingsMap = new TreeMap<Integer,ArrayList<Integer>>();//入度列表
    		TreeMap<Integer,ArrayList<Integer> > NodeOutgoingsMap = new TreeMap<Integer,ArrayList<Integer>>();//出度列表
    		
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
    		BufferedWriter out = new BufferedWriter(new FileWriter("NodeOutgoings.csv"));
    		for(Map.Entry<Integer, ArrayList<Integer>> m:NodeOutgoingsMap.entrySet()){
    			out.write(m.getKey()+",");
    			for(Integer going:m.getValue()){
    				out.write(going+" ");
    			}
    			out.write("\n");
    		}
    		out.close();
    		BufferedWriter out2 = new BufferedWriter(new FileWriter("NodeIncomings.csv"));
    		for(Map.Entry<Integer, ArrayList<Integer>> m:NodeIncomingsMap.entrySet()){
    			out2.write(m.getKey()+",");
    			for(Integer coming:m.getValue()){
    				out2.write(coming+" ");
    			}
    			out2.write("\n");
    		}
    		out2.close();
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
//    			Node n1 = new Node();
//    			n1.id = Integer.valueOf(words[0]);
//    			Node n2 = new Node();
//    			n2.id = Integer.valueOf(words[1]);
//    			nodeSet.add(n1);
//    			nodeSet.add(n2);
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
			ArrayList<Integer> incomingsList = new ArrayList<Integer>();
			BufferedReader read = BF.readFile("NodeIncomings.csv");
    		String line = "";
    		while((line=read.readLine())!=null){
    			String[] words = line.split(",");
    			int nodeid = Integer.valueOf(words[0]);
    			if(nodeid==id){
    				String[] incomings = words[1].split(" ");
    				for(String s:incomings){
    					incomingsList.add(Integer.valueOf(s));
    				}
    				break;
    			}
    		}
    		return incomingsList;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public ArrayList<Integer> getOutgoingNeighbors(Integer node){
		try{
			int id = node;
			ArrayList<Integer> outgoingsList = new ArrayList<Integer>();
			BufferedReader read = BF.readFile("NodeOutgoings.csv");
    		String line = "";
    		while((line=read.readLine())!=null){
    			String[] words = line.split(",");
    			int nodeid = Integer.valueOf(words[0]);
    			if(nodeid==id){
    				String[] incomings = words[1].split(" ");
    				for(String s:incomings){
    					outgoingsList.add(Integer.valueOf(s));
    				}
    				break;
    			}
    		}
    		return outgoingsList;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
    public void doHits(){
    	double normMax = 0;//用于归一化
    	print();
    	//计算Auth
    	for(Integer n:nodeSet2){
			double auth = 0.0;
			ArrayList<Integer> incomingsList = getIncomingNeighbors(n);
			if(incomingsList!=null){
				for(Integer in:incomingsList){
					auth += NodeHubMap.get(in);
				}
			}
			System.out.println(n+"  "+auth);
			NodeAuthMap.put(n, auth);
			if(normMax<auth){
				normMax = auth;
			}
    	}
    
    	System.out.println(normMax);
    	for(Map.Entry<Integer, Double> m:NodeAuthMap.entrySet()){
    		double auth = m.getValue();
    		auth = auth/normMax;
    		NodeAuthMap.put(m.getKey(), auth);
    	}
    	normMax = 0;
    	//计算Hub
    	for(Integer n:nodeSet2){
    		double hub = 0.0;
    		ArrayList<Integer> outgoingsList = getOutgoingNeighbors(n);
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
   public void print(){//结果输出
	   for(Integer n:nodeSet2){
		   System.out.println(n+" : "+NodeAuthMap.get(n)+"---"+NodeHubMap.get(n));
	   }
	   System.out.println();
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
		  	doHits();
		  	i++;
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
		print();
		
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
   
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HITS2 h = new HITS2();
		File netFile = new File("test.csv");
		h.init(netFile);
		h.reHITS();
	}

}
//class Node{
//	public Integer id;
//	public double auth;
//	public double hub;
//	public ArrayList<Integer> getIncomingNeighbors(Node node){
//		try{
//			int id = node.id;
//			ArrayList<Integer> incomingsList = new ArrayList<Integer>();
//			BufferedReader read = BF.readFile("NodeIncomings.csv");
//    		String line = "";
//    		while((line=read.readLine())!=null){
//    			String[] words = line.split(",");
//    			int nodeid = Integer.valueOf(words[0]);
//    			if(nodeid==id){
//    				String[] incomings = words[1].split(" ");
//    				for(String s:incomings){
//    					incomingsList.add(Integer.valueOf(s));
//    				}
//    				break;
//    			}
//    		}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return null;
//	}
//	public ArrayList<Integer> getOutgoingNeighbors(Node node){
//		try{
//			int id = node.id;
//			ArrayList<Integer> outgoingsList = new ArrayList<Integer>();
//			BufferedReader read = BF.readFile("NodeOutgoings.csv");
//    		String line = "";
//    		while((line=read.readLine())!=null){
//    			String[] words = line.split(",");
//    			int nodeid = Integer.valueOf(words[0]);
//    			if(nodeid==id){
//    				String[] incomings = words[1].split(" ");
//    				for(String s:incomings){
//    					outgoingsList.add(Integer.valueOf(s));
//    				}
//    				break;
//    			}
//    		}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return null;
//	}
//}
