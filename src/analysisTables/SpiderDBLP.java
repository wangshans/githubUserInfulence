package analysisTables;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SpiderDBLP {
	String RootUrl = "D:\\github\\api.xml";
	
	public BufferedReader readFile(File file){//读取指定文档，返回Reader
		System.out.println("——————load file from "+file.getName()+", waiting...");
		BufferedInputStream fis;
		BufferedReader reader = null;
		try{
			fis = new BufferedInputStream(new FileInputStream(file));
			reader = new BufferedReader(new InputStreamReader(fis,"utf-8"),5*1024*1024);
		}catch(Exception e){
			e.printStackTrace();
		}
		return reader;	
	}
	public boolean getPapersTitle(){
		
		try{
			BufferedWriter out = new BufferedWriter(
					new FileWriter("D:\\github\\DBLPpapers.txt"));
			BufferedReader r = readFile(new File(RootUrl));
			String line = "";
			String[] words;
			int i=0;
			while((line = r.readLine())!=null){
				
//				if(!line.contains("<info><title>"))
//					continue;
				words = line.split("<info><title>");
				
				if(words.length<2)
					continue;
			//	System.out.print(words.length);
				String title = words[1].split("</title>")[0];
				i++;
			//	System.out.println(title);
				out.write(i+", "+title+"\n");
			}
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}

	
	public static void main(String[] args) throws IOException {
		//
		long a = System.currentTimeMillis();
		SpiderDBLP s = new SpiderDBLP();
		s.getPapersTitle();
//		BugReport br = s.getBugReport(523163);
//		
//		s.printBR(br);
//		
		long b = System.currentTimeMillis();
		System.out.println(" used Times --------  "+(b-a)/1000);
			
	}
}
