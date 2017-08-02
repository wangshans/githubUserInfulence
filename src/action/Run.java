package action;

import java.io.File;
import java.io.IOException;

import utils.Constants;
import wss.githubFeatures.Followers;
import wss.githubFeatures.Users;
import wss.resultAnalysis.CompareFeatures;

public class Run {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String path = Constants.dumpFile;
		File usersFile = new File(path+"users.csv");
		File followersFile = new File("E:\\mysql-2015-09-25\\dump\\followers.csv");
		Users u = new Users();
		u.setUsersFile(usersFile);
		u.preproUsers();
		Followers f = new Followers();
		f.setFollowersFile(followersFile);
		File userFollowersNumFile = f.userFollowersNum();
//		CompareFeatures c = new CompareFeatures();
//		c.sortInfo(new File("f:\\userFollowersNum.csv"));
	}

}
