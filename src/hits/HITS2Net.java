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

public class HITS2Net {

	/**
	 * @param args
	 */
	double max = 0.00000001;//停止迭代的差值临界点
	int iter = 1000;//默认迭代次数
	
	public static Map<Integer,Double > Node1AuthMap = new HashMap<Integer,Double>();//authority
    public static Map<Integer,Double > Node1HubMap = new HashMap<Integer,Double>();//hub
    public static Map<Integer,Double > Node2AuthMap = new HashMap<Integer,Double>();//authority
    public static Map<Integer,Double > Node2HubMap = new HashMap<Integer,Double>();//hub
    
	TreeMap<Integer,ArrayList<Integer> > Node1OutgoingsMap = new TreeMap<Integer,ArrayList<Integer>>();//出度列表
	TreeMap<Integer,ArrayList<Integer> > Node2IncomingsMap = new TreeMap<Integer,ArrayList<Integer>>();//入度列表
	TreeMap<Integer,ArrayList<Integer> > Node2OutgoingsMap = new TreeMap<Integer,ArrayList<Integer>>();//出度列表
	TreeMap<Integer,ArrayList<Integer> > Node1IncomingsMap = new TreeMap<Integer,ArrayList<Integer>>();//入度列表

 //   public Set<Node> nodeSet = new HashSet<Node>();
    public Set<Integer> node1Set = new HashSet<Integer>();
    public Set<Integer> node2Set = new HashSet<Integer>();
 //   File NetFile = new File("");
    public void getInOuts(File Net1,File Net2){
    	getNode1InOuts(Net1);
    	getNode2InOuts(Net2);
    }
    public void getNode1InOuts(File Net1File){//Node1指向Node2,输出节点的链出列表和链入列表
    	try{
    		System.out.print("getInOuts .....");
    		BufferedReader read = BF.readFile(Net1File);
    		String line = "";
    		
    		while((line=read.readLine())!=null){
    			String[] words = line.split(",");
    			Integer node1 = Integer.valueOf(words[0]);
    			Integer node2 = Integer.valueOf(words[1]);//node1指向node
    			ArrayList<Integer> outgoings = Node1OutgoingsMap.get(node1);
    			if(outgoings==null){
    				outgoings = new ArrayList<Integer>();
    			}
    			outgoings.add(node2);
    			Node1OutgoingsMap.put(node1, outgoings);
    			
    			ArrayList<Integer> incomings = Node2IncomingsMap.get(node2);
    			if(incomings==null){
    				incomings = new ArrayList<Integer>();
    			}
    			incomings.add(node1);
    			Node2IncomingsMap.put(node2, incomings);
    		}
    		BufferedWriter out = new BufferedWriter(new FileWriter("Node1Outgoings.csv"));
    		for(Map.Entry<Integer, ArrayList<Integer>> m:Node1OutgoingsMap.entrySet()){
    			out.write(m.getKey()+",");
    			for(Integer going:m.getValue()){
    				out.write(going+" ");
    			}
    			out.write("\n");
    		}
    		out.close();
    		BufferedWriter out2 = new BufferedWriter(new FileWriter("Node2Incomings.csv"));
    		for(Map.Entry<Integer, ArrayList<Integer>> m:Node2IncomingsMap.entrySet()){
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
    public void getNode2InOuts(File Net2File){//Node2指向Node1,输出节点的链出列表和链入列表
    	try{
    		System.out.print("getInOuts .....");
    		BufferedReader read = BF.readFile(Net2File);
    		String line = "";
    		
    		while((line=read.readLine())!=null){
    			String[] words = line.split(",");
    			Integer node1 = Integer.valueOf(words[0]);
    			Integer node2 = Integer.valueOf(words[1]);//node1指向node
    			ArrayList<Integer> outgoings = Node2OutgoingsMap.get(node1);
    			if(outgoings==null){
    				outgoings = new ArrayList<Integer>();
    			}
    			outgoings.add(node2);
    			Node2OutgoingsMap.put(node1, outgoings);
    			
    			ArrayList<Integer> incomings = Node1IncomingsMap.get(node2);
    			if(incomings==null){
    				incomings = new ArrayList<Integer>();
    			}
    			incomings.add(node1);
    			Node1IncomingsMap.put(node2, incomings);
    		}
    		BufferedWriter out = new BufferedWriter(new FileWriter("Node2Outgoings.csv"));
    		for(Map.Entry<Integer, ArrayList<Integer>> m:Node2OutgoingsMap.entrySet()){
    			out.write(m.getKey()+",");
    			for(Integer going:m.getValue()){
    				out.write(going+" ");
    			}
    			out.write("\n");
    		}
    		out.close();
    		BufferedWriter out2 = new BufferedWriter(new FileWriter("Node1Incomings.csv"));
    		for(Map.Entry<Integer, ArrayList<Integer>> m:Node1IncomingsMap.entrySet()){
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
    public void getNode(File Net1File,File Net2File){
    	System.out.print("getNode .....");
    	try{
    		BufferedReader read = BF.readFile(Net1File);
    		String line = "";
    		while((line=read.readLine())!=null){
    			String[] words = line.split(",");
    			node1Set.add(Integer.valueOf(words[0]));
    			node2Set.add(Integer.valueOf(words[1]));
    		}
    		BufferedReader read2 = BF.readFile(Net2File);
    		while((line=read2.readLine())!=null){
    			String[] words = line.split(",");
    			node2Set.add(Integer.valueOf(words[0]));
    			node1Set.add(Integer.valueOf(words[1]));
    		}
    		System.out.println(node1Set.size()+" "+node2Set.size());
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void init( File Net1,File Net2){//初始化，为1  
    	System.out.println("init.....");
    	getNode(Net1,Net2);
    	getInOuts(Net1,Net2);
		for(Integer n:node1Set){
			Node1AuthMap.put(n, 1.0);
			Node1HubMap.put(n, 1.0);
		}
		for(Integer n:node2Set){
			Node2AuthMap.put(n, 1.0);
			Node2HubMap.put(n, 1.0);
		}
    }
//    
    public void doHits(){
    	double normMax = 0;//用于归一化
    //	print();
    	//计算Auth
    	for(Integer n:node1Set){
			double auth = 0.0;
			ArrayList<Integer> incomingsList = Node1IncomingsMap.get(n);
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
    	//计算Hub
    	for(Integer n:node1Set){
    		double hub = 0.0;
    		ArrayList<Integer> outgoingsList = Node1OutgoingsMap.get(n);
    		if(outgoingsList!=null){
    			for(Integer out:outgoingsList){
        			hub += Node2AuthMap.get(out);
        		}
    		}
    		
    		Node1HubMap.put(n, hub);
    		if(normMax<hub){
    			normMax = hub;
    		}
    	}
    	for(Map.Entry<Integer, Double> m:Node1HubMap.entrySet()){
    		double hub = m.getValue();
    		hub = hub/normMax;
    		Node1HubMap.put(m.getKey(), hub);
    	}
		normMax = 0;	
		//计算Node2的Auth
    	for(Integer n:node2Set){
			double auth = 0.0;
			ArrayList<Integer> incomingsList = Node2IncomingsMap.get(n);
			if(incomingsList!=null){
				for(Integer in:incomingsList){
					auth += Node1HubMap.get(in);
				}
			}
	//		System.out.println(n+"  "+auth);
			Node2AuthMap.put(n, auth);
			if(normMax<auth){
				normMax = auth;
			}
    	}
    //	System.out.println(norm);
    	for(Map.Entry<Integer, Double> m:Node2AuthMap.entrySet()){
    		double auth = m.getValue();
    		auth = auth/normMax;
    		Node2AuthMap.put(m.getKey(), auth);
    	}
    	normMax = 0;
    	//计算Hub
    	for(Integer n:node2Set){
    		double hub = 0.0;
    		ArrayList<Integer> outgoingsList = Node1OutgoingsMap.get(n);
    		if(outgoingsList!=null){
    			for(Integer out:outgoingsList){
        			hub += Node1AuthMap.get(out);
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
	   System.out.print("node1 \t");
	   for(Integer n:node1Set){
		   System.out.println(n+" : "+Node1AuthMap.get(n)+"---"+Node1HubMap.get(n));
	   }
	   System.out.print("\nnode2 \t");

	   for(Integer n:node1Set){
		   System.out.println(n+" : "+Node2AuthMap.get(n)+"---"+Node2HubMap.get(n));
	   }
	   System.out.println();
   }
   public void write(){//结果输出
	   try{
		   BufferedWriter out = new BufferedWriter(new FileWriter("Node1AuthHub.csv"));
		   out.write("id,auth,hub\n");
		   for(Integer n:node1Set){
			   out.write(n+","+Node1AuthMap.get(n)+","+Node1HubMap.get(n)+"\n");
		   }
		   out.close();
		   BufferedWriter out2 = new BufferedWriter(new FileWriter("Node2AuthHub.csv"));
		   out2.write("id,auth,hub\n");
		   for(Integer n:node2Set){
			   out2.write(n+","+Node2AuthMap.get(n)+","+Node2HubMap.get(n)+"\n");
		   }
		   out2.close();
	   }catch(Exception e){
		   e.printStackTrace();
	   }
	  
   }
   //循环迭代
   public void reHITS(){
	   
		System.out.println("开始HITS的迭代计算~~~~~~~");
		
		boolean flag=false;
		Map<Integer,Double > Check1AuthMap = new HashMap<Integer,Double>();//authority
		Map<Integer,Double > Check1HubMap = new HashMap<Integer,Double>();//hub
		Map<Integer,Double > Check2AuthMap = new HashMap<Integer,Double>();//authority
		Map<Integer,Double > Check2HubMap = new HashMap<Integer,Double>();//hub
		int i=0;//记录迭代次数
		while(!flag){
			Iterator<Integer> t1 = Node1AuthMap.keySet().iterator();
			while(t1.hasNext()){
				Integer uid = t1.next();
				Check1AuthMap.put(uid, Node1AuthMap.get(uid));
		  		}
		  	Iterator<Integer> t2 = Node1HubMap.keySet().iterator();
		  	while(t2.hasNext()){
				Integer uid = t2.next();
				Check1HubMap.put(uid, Node1HubMap.get(uid));
		  	}
		  	Iterator<Integer> t3 = Node2AuthMap.keySet().iterator();
			while(t3.hasNext()){
				Integer uid = t3.next();
				Check2AuthMap.put(uid, Node2AuthMap.get(uid));
		  		}
		  	Iterator<Integer> t4 = Node2HubMap.keySet().iterator();
		  	while(t4.hasNext()){
				Integer uid = t4.next();
				Check2HubMap.put(uid, Node2HubMap.get(uid));
		  	}
		  	i++;
		  	System.out.print(i+" ");
		  	if(i%10==0){
		  		System.out.println();
		  	}
		  	doHits();
		  	Iterator<Integer> t5 = Node1AuthMap.keySet().iterator();
	  		while(t5.hasNext()){
				Integer uid = t5.next();
				double da = Node1AuthMap.get(uid)-Check1AuthMap.get(uid);
				System.out.println(da);
				if(Math.abs(da)>max){
					flag = false;
					break;
				}
				double dh = Node1HubMap.get(uid)-Check1HubMap.get(uid);
				if(Math.abs(dh)>max){
					flag = false;
					break;
				}
	  		}
	  		Iterator<Integer> t6 = Node2AuthMap.keySet().iterator();
	  		while(t6.hasNext()){
				Integer uid = t6.next();
				double da = Node2AuthMap.get(uid)-Check2AuthMap.get(uid);
				if(Math.abs(da)>max){
					flag = false;
					break;
				}
				double dh = Node2HubMap.get(uid)-Check2HubMap.get(uid);
				if(Math.abs(dh)>max){
					flag = false;
					break;
				}
				flag = true;
	  		}
	  		
		}
	//	print();
		
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
   public void getWatchNet(){
		try{
			System.out.println("getWatchNet");
			 BufferedWriter out = new BufferedWriter(new FileWriter("WatchNet.csv"));

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
   public void getOwnedNet(){
		try{
			System.out.println("getOwnedNet");
			 BufferedWriter out = new BufferedWriter(new FileWriter("OwnedNet.csv"));

			File f = new File("E:\\mysql-2015-09-25\\screen\\tables\\projects.csv");
			String line = "";
			BufferedReader read = BF.readFile(f);
   		while((line=read.readLine())!=null){
   			String[] words = line.split(",");
   			out.write(words[0]+","+words[2]+"\n");
   		}
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HITS2Net h = new HITS2Net();
		File test1 = new File("WatchNet.csv");
		File test2 = new File("OwnedNet.csv");
		h.getOwnedNet();
		h.getWatchNet();
		h.init(test1,test2);
	//	h.reHITS();
	//	h.write();
	//	h.clear();
	}

}
