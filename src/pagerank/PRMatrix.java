package pagerank;
import Jama.Matrix;  
public class PRMatrix {

	/**
	 * @param args
	 */
	
	  
        static double alpha =0.85;  

        public void matri() {
        	double[][] arrays ={
        			{0d,1.0d/2,1.0d/2,0,1.0d/2},  
        			{1.0d/4,0,0,0,0},  
        			{1.0d/4,0,0,1,1.0d/4},  
        			{1.0d/4,1.0d/2,1.0d/2,0,0},  
        			{1.0d/4,0,0,0,0}  
        	};  
        	double[][] arrayS = {
        			{0d,0d,0d,0},  
        			{1.0d/3,0,1d,0},  
        			{1.0d/3,1.0d/2,0,0},  
        			{1.0d/3,1.0d/2,0,1}, 
        	};
        	double[][] arrayPR={{1},{1},{1},{1}};  
        	double[][] arrayMinimum = {{0.01d},{0.01d},{0.01d},{0.01d}};  
        	int n = arrayS.length;  
        	Matrix s = new Matrix(arrayS);//源矩阵  
        	Matrix pageRank = new Matrix(arrayPR);//page rank初始值  
        	Matrix minimum = new Matrix(arrayMinimum);//极小值  
        	Matrix u=new Matrix(n,n,1.0d);//单元矩阵  
        	Matrix g=s.times(alpha).plus(u.times((1-alpha)/n));  
   
                /* 
  
                 * 开始迭代计算适合的pageRank值。（也就是求矩阵g，特征值为1 的特征向量pageRank。ps：近似值） 
  
                 */  
   
        	Matrix pageRankPre = pageRank;  
        	pageRank = g.times(pageRank);  
        	int iterator =1;  
        	while(true){  
        		if(compareAbs(minimum,pageRankPre.minus(pageRank))){  
        			break;  
        		}else{  
        			pageRankPre = pageRank;  
        			pageRank = g.times(pageRank);  
        			iterator ++;  
        		}  
        	}  
        	System.out.print("倒数第一次迭代结果：");  
        	pageRankPre.print(2, 2);  
        	System.out.print("最后结果：");  
        	pageRank.print(2, 2);  
        	System.out.print("迭代次数："+iterator);  
        }  
   
      /* 
       * 同阶矩阵，比较对应元素的的绝对值。如果对任意的i,j=1,2,...,n。都有|a(ij)|>|b(ij)|则返回true,否则返回false 
       * */  
   
   public static boolean compareAbs(Matrix a,Matrix b){  
	   boolean flag = true;  
	   for(int i=0;i<a.getRowDimension();i++){  
		   for(int j=0;j<a.getColumnDimension();j++){  
			   if(Math.abs(a.get(i, j))<=Math.abs(b.get(i, j))){
				   
				   flag = false;
				   break;  
			   	}
		   }
	   }
	   return flag;
   }   
   public static void main(String[] args) {
		// TODO Auto-generated method stub
	   PRMatrix p = new PRMatrix();
	   p.matri();
	}

}
