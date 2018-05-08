package test.autoTrade.sbi;

import java.io.IOException;

import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;

public class AutoTrade {
	
	static Response res = null;
	static Document doc = null;
	
	public static void main(String[] args) throws IOException {
		
		String userid = System.getenv("userid");
		String password = System.getenv("password");
		
		// TODO Auto-generated method stub
		LoginLogout loginProcess = new LoginLogout();
				
		res = loginProcess.login(userid, password);
		if (res != null){
			// ログイン後の画面
			doc = res.parse();
			System.out.println(doc.html());
			
			//LOGOUT
			loginProcess.logout();
		}
	}

}
