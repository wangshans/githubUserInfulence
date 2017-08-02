//package analysisTables;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import mydata.ReadtoDatabase;
//
//
//public class RunGetData {
//	public static final String txtDir = "D:\\mysql-2015-09-25\\my\\";
//	//List<String> txtfiles = new ArrayList<String>();
//	
//	//将分离的文档的名字放到列表中
//	public List<String> setfiles(){
//		
//		List<String> txtfiles = new ArrayList<String>();
//		for(int i=0;i<36;i++)
//		{
//			txtfiles.add(i,new String(txtDir+"text"+i+".txt"));
//		}
//		return txtfiles;
//		
//	}
//	
//	//依次将分离的文档text0--36，读入数据库中；
//	public void run(){
//		
//		ReadtoDatabase instance = new ReadtoDatabase();
//		List<String> txtfiles = new ArrayList<String>();
//		for(int i=0;i<1;i++)
//		{
//			txtfiles.add(i,new String(txtDir+"text"+i+".txt"));
//		}
//		for(String myfilename:txtfiles){
//			long start = System.currentTimeMillis();
//			instance.readBigFile(myfilename);
//			instance.outtodb();
//			long end = System.currentTimeMillis();
//			System.out.println("计算用时="+(end-start)/1000);
//		}
//		
//	}
//	
//	
//}