package test.autoTrade.sbi;

import java.io.IOException;

import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;

import test.autoTrade.exception.FailedToGetInputScreenException;

public class AutoTrade {

	static Response res = null;
	static Document doc = null;

	public static void main(String[] args) throws IOException {

		String userid = System.getenv("userid");
		String password = System.getenv("password");

		// TODO Auto-generated method stub
		Login loginProc = Login.getInstance();

		// if ( ((Login) loginProc).login(userid, password) ) {
		try {
			if (loginProc.login(userid, password)) {
				// ログイン成功

				// LOGOUT
				// loginProc.logout();

				InterfaceScreen accountListYenProc = new AccountListYen(loginProc);

				// 画面
				if (accountListYenProc.getScreen()) {
					System.out.println(loginProc.doc.html());
				} else {

					// LOGOUT 2回読んでも問題なし
					// loginProc.logout();
				}
			}
		} catch (IOException e) {

		} catch (FailedToGetInputScreenException e) {
			System.out.println("FailedToGetInputScreenException発生");			
		}

	}

}
