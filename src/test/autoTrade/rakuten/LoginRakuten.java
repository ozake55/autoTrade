package test.autoTrade.rakuten;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

import test.autoTrade.Login;
import test.autoTrade.TradeUtil;
import test.autoTrade.exception.FailedToGetInputScreenException;
import test.autoTrade.sbi.AccountListYenSbi;
import test.autoTrade.sbi.InterfaceScreen;
import test.autoTrade.sbi.LoginSbi;
import test.autoTrade.sbi.UtilSbi;

public class LoginRakuten extends Login {
	
	//ログイン専用
	//<form name="loginform" action="https://member.rakuten-sec.co.jp/app/Login.do" method="POST" target="_top">
	static final String startUrl = "https://www.rakuten-sec.co.jp/ITS/V_ACT_Login.html";
	static final String loginUrl = "https://member.rakuten-sec.co.jp/app/Login.do";
	static final String loginFormName = "loginform";
	
	private static LoginRakuten loginSingleton = new LoginRakuten();
	private LoginRakuten(){
		util = new TradeUtil(null);
	}
	
	
	public static void main(String[] args) throws URISyntaxException {
		// TODO Auto-generated method stub
		String loginJson = System.getenv("rakuten_login_json");
		
		//castしない
		Login loginProc = LoginRakuten.getInstance();

		try {
			if (loginProc.login(loginJson)) {

				/*
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
				*/
			}
		} catch (IOException e) {

		} catch (FailedToGetInputScreenException e) {
			System.out.println("FailedToGetInputScreenException発生");			
		}
		
	}
	
	public static LoginRakuten getInstance() {
		return loginSingleton;
	}
	
	@Override
	public boolean login(String loginInputParamJson) throws IOException, FailedToGetInputScreenException, URISyntaxException {
		//loginInputParamJson={"user_id":"user_id", "user_password":"user_password", "_ActionID":"loginPortfolio"}
		HashMap<String, String> param=super.getLoginForm(startUrl,loginFormName,loginInputParamJson);
		
		//login POST request
		res = connectMethodPost(loginUrl,param);
		
		// ログイン後の画面
		doc = res.parse();
		System.out.println(doc.html());
		
		//Rg_sec or checkTk どっち？
		return super.loginDecision("Rg_sec");
		
	}
}
