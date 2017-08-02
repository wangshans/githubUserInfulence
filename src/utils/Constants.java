package utils;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public interface Constants {

//	String dirname = "D:\\mysql-2017-01-19\\";//
//	String currentTime = "2017-01-20 00:00:00";
//	String dirname = "E:\\mysql-2015-09-25\\";
	String currentTime = "2015-09-26 00:00:00";

	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	String screenFile = dirname + "screen\\";
	String dumpFile = dirname+"dump\\";
	
	String startTime =   "2008-04-10 00:00:00";

}
