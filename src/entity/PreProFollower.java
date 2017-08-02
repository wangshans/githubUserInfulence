package entity;

import java.util.ArrayList;
import java.util.List;

public class PreProFollower {
	
		public int id ;
		private double pr;  //PR值
		public int outd;//出度
		
		private List<Integer> MyfollowingOut = new ArrayList<Integer>();  //该用户所关注的用户  
	    /* 内链(另外页面链接本页面) */  
	    private List<Integer> MyfollowerIn = new ArrayList<Integer>();  //关注该用户的人
	    
	    public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
	    public double getPr() {  
	        return pr;  
	    }  
	    public int getOutd() {
			return outd;
		}

		public void setOutd(int outd) {
			this.outd = outd;
		}

		public void setPr(double pr) {  
	        this.pr = pr;  
	    }

		public List<Integer> getMyfollowingOut() {
			return MyfollowingOut;
		}

		public void setMyfollowingOut(List<Integer> myfollowingOut) {
			MyfollowingOut = myfollowingOut;
		}

		public List<Integer> getMyfollowerIn() {
			return MyfollowerIn;
		}

		public void setMyfollowerIn(List<Integer> myfollowerIn) {
			MyfollowerIn = myfollowerIn;
		}

		public void setFollower_id(Integer string) {
			// TODO Auto-generated method stub
			
		}  
	  
	   
}

