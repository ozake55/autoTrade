package test.autoTrade.rakuten;

import java.io.IOException;

import test.autoTrade.Login;
import test.autoTrade.exception.FailedToGetInputScreenException;
import test.autoTrade.sbi.AccountListYenSbi;
import test.autoTrade.sbi.InterfaceScreen;
import test.autoTrade.sbi.LoginSbi;

public class LoginRakuten extends Login {
	
	private static LoginRakuten loginSingleton = new LoginRakuten();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String loginJson = System.getenv("sbi_login_json");
		
		//castしない
		LoginRakuten loginProc = LoginRakuten.getInstance();
/*
		try {
			if (loginProc.login(loginJson)) {
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
		*/
	}
	
	public static LoginRakuten getInstance() {
		return loginSingleton;
	}
}
