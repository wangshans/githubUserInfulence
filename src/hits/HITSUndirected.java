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

public class HITSUndirected {

	/**
	 * @param args
	 */
	double max = 0.00000001;//停止迭代的差值临界点
	int iter = 1000;//默认迭代次数
	
	public static Map<Integer,Double > Node1AuthMap = new HashMap<Integer,Double>();//authority
 //   public static Map<Integer,Double > Node1HubMap = new HashMap<Integer,Double>();//hub
 //   public static Map<Integer,Double > Node2AuthMap = new HashMap<Integer,Double>();//authority
    public static Map<Integer,Double > Node2HubMap = new HashMap<Integer,Double>();//hub
    
	TreeMap<Integer,ArrayList<Integer> > Node1NeighborsMap = new TreeMap<Integer,ArrayList<Integer>>();//入度列表
	TreeMap<Integer,ArrayList<Integer> > Node2NeighborsMap = new TreeMap<Integer,ArrayList<Integer>>();//入度列表

 //   public Set<Node> nodeSet = new HashSet<Node>();
    public Set<Integer> node1Set = new HashSet<Integer>();
    public Set<Integer> node2Set = new HashSet<Integer>();
 //   File NetFile = new File("");
    public void getNeighbors(File NetFile){//输出节点的链出列表和链入列表
    	try{
    		System.out.print("getNeighbors .....");
    		BufferedReader read = BF.readFile(NetFile);
    		String line = "";
    		
    		while((line=read.readLine())!=null){
    			String[] words = line.split(",");
    			Integer node1 = Integer.valueOf(words[0]);
    			Integer node2 = Integer.valueOf(words[1]);//node1指向node
    			ArrayList<Integer> neighbors = Node1NeighborsMap.get(node1);
    			if(neighbors==null){
    				neighbors = new ArrayList<Integer>();
    			}
    			if(neighbors.contains(node2))
    				continue;
    			neighbors.add(node2);
    			Node1NeighborsMap.put(node1, neighbors);
    			
    			ArrayList<Integer> neighbors2 = Node2NeighborsMap.get(node2);
    			if(neighbors2==null){
    				neighbors2 = new ArrayList<Integer>();
    			}
    			if(neighbors2.contains(node1))
    				continue;
    			neighbors2.add(node1);
    			Node2NeighborsMap.put(node2, neighbors2);
    		}
    		System.out.println("down-----"+Node1NeighborsMap.size()+" "+Node2NeighborsMap.size());
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
    			node1Set.add(Integer.valueOf(words[0]));
    			node2Set.add(Integer.valueOf(words[1]));
    		}
    		System.out.println(node1Set.size()+" "+node2Set.size());
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void init( File NetFile){//初始化，为1  
    	System.out.println("init.....");
    	getNode(NetFile);
    	getNeighbors(NetFile);
		for(Integer n:node1Set){
			Node1AuthMap.put(n, 1.0);
		//	Node1HubMap.put(n, 1.0);
		}
		for(Integer n:node2Set){
		//	Node2AuthMap.put(n, 1.0);
			Node2HubMap.put(n, 1.0);
		}
    }

    public void doHits(){
    	double normMax = 0;//用于归一化
    //	System.out.println("\t doHITS ");
    //	print();
    	//计算Auth
    	int i =0;
    	for(Integer n:node1Set){
//    		i++;
//    		if(i%1000==0){
//    			System.out.println(i/1000);
//    		}
			double auth = 0.0;
			ArrayList<Integer> incomingsList = Node1NeighborsMap.get(n);
			if(incomingsList!=null){
				for(Integer in:incomingsList){
					auth += Node2HubMap.get(in);
				}
			}
	//		System.out.println(n+"  "+auth);
			Node1AuthMap.put(n, auth);
			if(normMax<auth){
				normMax = auth;
			}
    	}
    //	System.out.println(norm);
    	for(Map.Entry<Integer, Double> m:Node1AuthMap.entrySet()){
    		double auth = m.getValue();
    		auth = auth/normMax;
    		Node1AuthMap.put(m.getKey(), auth);
    	}
    	normMax = 0;
    	i=0;
    	//计算Hub
    	for(Integer n:node2Set){
//    		i++;
//    		if(i%1000==0){
//    			System.out.println(i/1000);
//    		}
    		double hub = 0.0;
    		ArrayList<Integer> neighborsList = Node2NeighborsMap.get(n);
    		if(neighborsList!=null){
    			for(Integer neighbor:neighborsList){
        			hub += Node1AuthMap.get(neighbor);
        		}
    		}
    		
    		Node2HubMap.put(n, hub);
    		if(normMax<hub){
				normMax = hub;
			}
    	}
    	for(Map.Entry<Integer, Double> m:Node2HubMap.entrySet()){
    		double hub = m.getValue();
    		hub = hub/normMax;
    		Node2HubMap.put(m.getKey(), hub);
    	}
    }
   public void print(){//结果打印
	   for(Integer n:node1Set){
		   System.out.println(n+" : "+Node1AuthMap.get(n));
	   }
	   for(Integer n:node2Set){
		   System.out.println(n+" : "+Node2HubMap.get(n));
	   }
	   System.out.println();
   }
   public void write(){//结果输出
	   try{
		   BufferedWriter out = new BufferedWriter(
				   new FileWriter(netFile.getName().replaceAll(".csv", "")+"_Node1Auth.csv"));
		   out.write("id,auth\n");
		   for(Integer n:node1Set){
			   out.write(n+","+Node1AuthMap.get(n)+"\n");
		   }
		   out.close();
		   BufferedWriter out2 = new BufferedWriter(
				   new FileWriter(netFile.getName().replaceAll(".csv", "")+"_Node2Hub.csv"));
		   out2.write("id,hub\n");
		   for(Integer n:node1Set){
			   out2.write(n+","+Node2HubMap.get(n)+"\n");
		   }
		   out2.close();
	   }catch(Exception e){
		   e.printStackTrace();
	   }
	  
   }
   //循环迭代
   public void reHITS(){
	   
		System.out.println("开始HITS的迭代计算~~~~~~~");
		
		boolean flag = false;
		Map<Integer,Double > Check1AuthMap = new HashMap<Integer,Double>();//authority
		Map<Integer,Double > Check2HubMap = new HashMap<Integer,Double>();//hub
		int i=0;//记录迭代次数
		while(!flag){
			Iterator<Integer> t1 = Node1AuthMap.keySet().iterator();
			while(t1.hasNext()){
				Integer uid = t1.next();
				Check1AuthMap.put(uid, Node1AuthMap.get(uid));
		  		}
		  	Iterator<Integer> t2 = Node2HubMap.keySet().iterator();
		  	while(t2.hasNext()){
				Integer uid = t2.next();
				Check2HubMap.put(uid, Node2HubMap.get(uid));
		  	}
		  	
		  	i++;
		  	System.out.print(i+" ");
		  	if(i%10==0){
		  		System.out.println();
		  	}
		  	doHits();
		  	Iterator<Integer> t3 = Node1AuthMap.keySet().iterator();
	  		while(t3.hasNext()){
				Integer uid = t3.next();
				double da = Node1AuthMap.get(uid)-Check1AuthMap.get(uid);
				if(Math.abs(da)>max){
					flag = false;
					break;
				}
	  		}
	  		Iterator<Integer> t4 = Node2HubMap.keySet().iterator();
	  		while(t4.hasNext()){
	  			Integer node2 = t4.next();
		  		double dh = Node2HubMap.get(node2)-Check2HubMap.get(node2);
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
	File netFile = new File("WatchNet.csv");
	public void getFollowNet(){
		try{
			 BufferedWriter out = new BufferedWriter(new FileWriter("FollowNet.csv"));

			File f = new File("E:\\mysql-2015-09-25\\screen\\tables\\watchers.csv");
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
		HITSUndirected h = new HITSUndirected();
	//	h.getFollowNet();
		h.init(h.netFile);
		h.reHITS();
		h.write();
	//	h.clear();
	}

}
