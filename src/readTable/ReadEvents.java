package readTable;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReadEvents {

	/**
	 * @param args
	 */
	public BufferedReader readFile(String filePath){
		try{
			BufferedInputStream fis = new BufferedInputStream(new FileInputStream(filePath));
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"utf-8"),5*1024*1024);
			return reader;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public void readevents() throws IOException{
		BufferedReader r = readFile("F:\\MongoDB\\db\\events.bson");
		String line = "";
		int i=0;
		BufferedWriter out = new BufferedWriter(new FileWriter("events1.txt"));
		while((line=r.readLine())!=null){
			i++;
			if(i<10000)
				out.write(line+"\n");
			else 
				break;
		}
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ReadEvents r = new ReadEvents();
		r.readevents();
	}

}
