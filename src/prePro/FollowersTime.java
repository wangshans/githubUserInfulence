package prePro;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class FollowersTime {

	/**
	 * @param args
	 */
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public String tablePath = "D:\\github\\mysql-2015-09-25\\dump\\";
	
	public BufferedReader readFile(String filePath){
		System.out.println("read file from "+filePath);
		try{
			
			BufferedInputStream fis = new BufferedInputStream(new FileInputStream(filePath));
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis),5*1024*1024);
			return reader;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public void time(){
		try
		{
		    Date d1 = df.parse("2004-03-26 13:31:40");
		    Date d2 = df.parse("2004-01-02 11:30:24");
		//    long days = diff / (1000 * 60 * 60 * 24);
		    long l=d1.getTime()-d2.getTime();
		    long day=l/(24*60*60*1000);
		    long hour=(l/(60*60*1000)-day*24);
		    long min=((l/(60*1000))-day*24*60-hour*60);
		    long s=(l/1000-day*24*60*60-hour*60*60-min*60);
		    System.out.println(""+day+"天"+hour+"小时"+min+"分"+s+"秒");
		}
		catch (Exception e)
		{
		}
	}
	public void readFollowers() throws IOException, ParseException{
		String file = tablePath+"followers.csv";
		BufferedReader reader = readFile(file);
		String line="";
		ArrayList<String> flist = new ArrayList<String>();
	//	Date baseTime = df.parse("");
		while((line=reader.readLine())!=null){
			String[] words = line.split(",");
//			String ids = words[0];
//			String fids = words[1];
//			String times = words[2];
			flist.add(line);
		}
		System.out.println(flist.size());
		
		//排序
		Collections.sort(flist, new Comparator<String>() {   
		    public int compare(String s1, String s2) {     
		        String[] w1 = s1.split(",");
		        String[] w2 = s2.split(",");
		        Date d1=null,d2=null;
				try {
					d1 = df.parse(w1[2].substring(1,w1[2].length()-1));
		            d2 = df.parse(w2[2].substring(1,w2[2].length()-1));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        long l = d2.getTime()-d1.getTime();
		        if(l>0)
		        	return 1;
		        else
		        	return 0;
		    }
		});
		System.out.println("sorted");
		BufferedWriter out = new BufferedWriter(new FileWriter("followersByTime.txt"));
		for(String s:flist){
			out.write(s+"\n");
		}
		out.close();
	}
	public void SpitFollowersByMonth() throws IOException, ParseException{
		String file = "followersByTime.txt";
		BufferedReader reader = readFile(file);
		String line="";
		Date baseTime = df.parse("2015-05-04 00:00:00");
		System.out.println(baseTime.getYear());
		int year = baseTime.getYear(),month = baseTime.getMonth();
		System.out.println(year+" "+month+" ");
		int i=0;
		year = year+1900;
		month = month+1;
		BufferedWriter out = new BufferedWriter(
				new FileWriter("Data\\spliFollowersByTime\\"+"fol"+year+"-"+month+".txt"));
		while((line=reader.readLine())!=null){
			String[] words = line.split(",");
			Date d = df.parse(words[2].substring(1,words[2].length()-1));
			if(d.getYear()+1900==year&&d.getMonth()+1==month){
				out.write(line+"\n");
			}else{
				out.close();
				year = d.getYear();
				month = d.getMonth();
				year = year+1900;
				month = month+1;
				out = new BufferedWriter(
						new FileWriter("Data\\spliFollowersByTime\\"+"fol"+year+"-"+month+".txt"));
			//	System.out.println(year+" "+month+" ");
				out.write(line+"\n");
			}
		}
		out.close();
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		FollowersTime s = new FollowersTime();
	//	s.time();
	//	s.readFollowers();
		s.SpitFollowersByMonth();
	}

}
