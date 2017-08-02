package hits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NewHITS {
	public static Map<Integer,ArrayList<Integer> > NodePOutListMap = new HashMap();//第一种节点以及出度列表
	public static Map<Integer,ArrayList<Integer> > NodePInListMap = new HashMap();//第一种节点以及其入度列表
	public static Map<Integer,ArrayList<Integer> > NodeQOutListMap = new HashMap();//第二种节点以及其出度列表
    public static Map<Integer,ArrayList<Integer> > NodeQInListMap = new HashMap();//第二种节点以及其入度列表
	
    double max = 0.0000001;
    //结果
    public static Map<Integer,Double[] > NodePAHMap = new HashMap();//P节点以及{Authority,Hub}
    public static Map<Integer,Double[] > NodeQAHMap = new HashMap();//P节点以及{Authority,Hub}
    
    //初始化，将所有节点的A和H都设为1
    public void init(){     
    	//初始化第一种节点，节点P，的A和H
       Iterator<Integer> t1 = NodePAHMap.keySet().iterator();
    		while(t1.hasNext()){
    			Integer pid = t1.next();
    			Double[] ah = NodePAHMap.get(pid);
    			ah[0] = 1.0;
    			ah[1] = 1.0;
    			NodePAHMap.put(pid, ah);//初始化为1.0
    		//	System.out.print(uid+" ");
    		} 	
    	//	System.out.println(" ");
    		Iterator<Integer> t2 = NodeQAHMap.keySet().iterator();
    		while(t2.hasNext()){
    			Integer qid = t2.next();
    			Double[] ah = NodePAHMap.get(qid);
    			ah[0] = 1.0;
    			ah[1] = 1.0;
    			NodePAHMap.put(qid, ah);//初始化为1.0
  
    		//	System.out.print(oid+" ");
    		} 
    	//	System.out.println(" ");
        }
    public Map<Integer,Double[]> ComputeAH(Map<Integer,ArrayList<Integer>> InListMap, Map<Integer,ArrayList<Integer>> OutListMap){
//    	System.out.println("authority JISUAN");
    	Map<Integer,Double[] > AHMap = new HashMap();//P节点以及{Authority,Hub}
    	Iterator<Integer> t1 = AHMap.keySet().iterator();
    	while(t1.hasNext()){
			Integer pid = t1.next();
		//	System.out.print(uid+", related other points ：  ");
		//	UserAuthMap.put(uid, 0.0);
			Double[] ah = {0.0,0.0};
			double auth = 0.0;
			double hub = 0.0;
 			double norm =0.0;//用于归一化
			
			if(InListMap.get(pid)==null){
				//System.out.println(" 没有followers ");
			}else{
		//		System.out.print(uid+", related other points ：  ");
				ArrayList<Integer> PInList = InListMap.get(pid);//与该user 有关联的另一类节点的列表
			//	System.out.println("\t rt.UserandListMap.get(uid).size:  "+rt.UserandListMap.get(uid).size());
		//		System.out.print(uid+":"+RaOtherList.size()+"----");
				
				Iterator it1 = PInList.iterator();
				while(it1.hasNext()){
		        	Integer qid = (Integer) it1.next();
		        	auth += AHMap.get(qid)[1];//【1】是hub
		        	
		        	norm += Math.pow(AHMap.get(qid)[1], 2); // calculate the sum of the squared auth values to normalise	      
		        	
		        //	System.out.print(oid+","+OtherHubMap.get(oid)+" ");
		        }
				norm = Math.sqrt(norm);//平方和的开方，用于归一化
				if(norm!=0){
					ah[0] = auth/norm;
					AHMap.put(pid, ah);//
				}else{
					AHMap.put(pid, ah);
				}
				
			}
			//节点P的出度，计算，Hub值
			ArrayList<Integer> POutList = OutListMap.get(pid);//与该user 有关联的另一类节点的列表
			if(POutList==null){
				//System.out.println(" 没有followers ");
			}else{
			//System.out.println("");
				Iterator it1 = POutList.iterator();
				while(it1.hasNext()){
		        	Integer qid = (Integer) it1.next();
		        	hub += AHMap.get(qid)[0];//【0】是authority
		        	
		        	norm += Math.pow(AHMap.get(qid)[0], 2); // calculate the sum of the squared auth values to normalise	      
		        	
		        //	System.out.print(oid+","+OtherHubMap.get(oid)+" ");
		        }
				norm = Math.sqrt(norm);//平方和的开方，用于归一化
				if(norm!=0){
					ah[1] = hub/norm;
				}
				AHMap.put(pid, ah);//
			}
    	}
    	return AHMap;
    }
    public void copyMap(Map map1,Map map2){//将map2复制给map1
    	Iterator<Integer> tt = map1.keySet().iterator();
		while(tt.hasNext()){
			Integer uid = tt.next();
			map2.put(uid, map1.get(uid));
		}
    }
    
    public void doHITS(){
        
        //计算P节点的hub和Authority值
    	NodePAHMap = ComputeAH(NodePInListMap,NodePOutListMap);
        //计算Q节点的hub和Authority值
    	NodeQAHMap = ComputeAH(NodeQInListMap,NodeQOutListMap); 	
        
    }
  
    //循环迭代
    public void reHITS(){
 	   
 		System.out.println("开始HITS的迭代计算~~~~~~~");
 		boolean flagP = false, flagQ = false;
 		Map<Integer,Double[] > CheckPMap = new HashMap<Integer,Double[]>();//authority
 		Map<Integer,Double[] > CheckQMap = new HashMap<Integer,Double[]>();//hub
 		int i=0;//记录迭代次数
 		while(!flagP&&!flagQ){
 			Iterator<Integer> t1 = NodePAHMap.keySet().iterator();
 			while(t1.hasNext()){
 				Integer uid = t1.next();
 				CheckPMap.put(uid, NodePAHMap.get(uid));
 		  		}
 		  	Iterator<Integer> t2 = NodePAHMap.keySet().iterator();
 		  	while(t2.hasNext()){
 				Integer uid = t2.next();
 				CheckQMap.put(uid, NodeQAHMap.get(uid));
 		  	}
 		  	doHITS();
 		  	i++;
 		  	Iterator<Integer> t3 = NodePAHMap.keySet().iterator();
 	  		while(t3.hasNext()){
 				Integer uid = t3.next();
 				double dPA = NodePAHMap.get(uid)[0]-CheckPMap.get(uid)[0];
 				double dPH = NodePAHMap.get(uid)[1]-CheckPMap.get(uid)[1];
 				if(Math.abs(dPA)<=max||Math.abs(dPH)<=max){//一旦发现有大于阈值的差，就跳出循环，在进行一次迭代
 					flagP = false;
 					break;
 				}
 				flagP = true;
 			}
 	  		if(flagP == true){//P节点已经满足到要求的精度
 	  			Iterator<Integer> t4 = NodeQAHMap.keySet().iterator();
 	 	  		while(t3.hasNext()){
 	 				Integer uid = t3.next();
 	 				double dQA = NodeQAHMap.get(uid)[0]-CheckPMap.get(uid)[0];
 	 				double dQH = NodeQAHMap.get(uid)[1]-CheckPMap.get(uid)[1];
 	 				if(Math.abs(dQA)<=max||Math.abs(dQH)<=max){//一旦发现有大于阈值的差，就跳出循环，在进行一次迭代
 	 					flagQ = false;
 	 					break;
 	 				}
 	 				flagQ = true;//所有的Q都满足到要求的精度
 	 			}
 	  		}
 	  	}			
     //    BF.Log("总共进行了    "+i+"次迭代。");
         System.out.println("总共进行了    "+i+"次迭代。");
    }
    public void reHITS(int iter){
 	   
 		System.out.println("开始HITS的迭代计算(迭代"+iter+"次)~~~~~~~");
 		boolean flag = false;
 		Map<Integer,Double[] > CheckPMap = new HashMap<Integer,Double[]>();//authority
 		Map<Integer,Double[] > CheckQMap = new HashMap<Integer,Double[]>();//hub
 		while(iter>0){
 		  	doHITS();
 		  	if(iter==2){//将倒数第二次的结果保存下来，用于计算得到的结果的精度
 		  		Iterator<Integer> t1 = NodePAHMap.keySet().iterator();
 	 			while(t1.hasNext()){
 	 				Integer uid = t1.next();
 	 				CheckPMap.put(uid, NodePAHMap.get(uid));
 	 		  		}
 	 		  	Iterator<Integer> t2 = NodePAHMap.keySet().iterator();
 	 		  	while(t2.hasNext()){
 	 				Integer uid = t2.next();
 	 				CheckQMap.put(uid, NodeQAHMap.get(uid));
 	 		  	}
 		  	}
 		  	iter--;
 		}
 		
 		Iterator<Integer> t3 = NodePAHMap.keySet().iterator();
 		double maxD = 1;//最大的差值，就是精度
  		while(t3.hasNext()){
			Integer uid = t3.next();
			double dPA = NodePAHMap.get(uid)[0]-CheckPMap.get(uid)[0];
			if(maxD<dPA)
				maxD = dPA;
			double dPH = NodePAHMap.get(uid)[1]-CheckPMap.get(uid)[1];
			if(maxD<dPA)
				maxD = dPA;
		}
	  		
		Iterator<Integer> t4 = NodeQAHMap.keySet().iterator();
  		while(t3.hasNext()){
			Integer uid = t3.next();
			double dQA = NodeQAHMap.get(uid)[0]-CheckPMap.get(uid)[0];
			if(maxD<dQA)
				maxD = dQA;
			double dQH = NodeQAHMap.get(uid)[1]-CheckPMap.get(uid)[1];
			if(maxD<dQA)
				maxD = dQA;
		}
	  	
        System.out.println("此时的精度（最大的差）为    "+maxD+" 。");
   }
    public void printAll(){//结果输出
 	   System.out.println("NodePAHMap is : ");
 	   	Iterator<Integer> t1 = NodePAHMap.keySet().iterator();
    		while(t1.hasNext()){
 			Integer pid = t1.next();
 			System.out.println("\t"+pid+","+NodePAHMap.get(pid)[0]+","+NodePAHMap.get(pid)[1]);
    		}
    		
    		System.out.println("NodeQAHMap is : ");
    		Iterator<Integer> t2 = NodeQAHMap.keySet().iterator();
    		while(t2.hasNext()){
 			Integer qid = t2.next();
 			System.out.println("\t"+qid+","+NodeQAHMap.get(qid)[0]+","+NodeQAHMap.get(qid)[1]);
    		}
    }
    public static void main(String[] args){
    	
    }
}
