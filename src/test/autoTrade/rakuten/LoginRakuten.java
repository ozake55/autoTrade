package test.autoTrade.rakuten;

import java.io.IOException;

import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.HashMap;

import org.jsoup.select.Elements;

import test.autoTrade.InterfaceScreen;
import test.autoTrade.Login;
import test.autoTrade.TradeUtil;
import test.autoTrade.exception.FailedToGetInputScreenException;
import test.autoTrade.exception.ResponseAnalysisException;
import test.autoTrade.sbi.AccountListYenSbi;
import test.autoTrade.sbi.LoginSbi;
import test.autoTrade.sbi.UtilSbi;

public class LoginRakuten extends Login {
	
	//ログイン専用
	//<form name="loginform" action="https://member.rakuten-sec.co.jp/app/Login.do" method="POST" target="_top">
	static final String startUrl = "https://www.rakuten-sec.co.jp/ITS/V_ACT_Login.html";
	static final String loginUrl = "https://member.rakuten-sec.co.jp/app/Login.do";
	static final String loginFormName = "loginform";
	static final String jsessionName = "BV_SessionID";
	
	private String session = null;
	
	private static LoginRakuten loginSingleton = new LoginRakuten();
	private LoginRakuten(){
		util = new TradeUtil(null);
	}
	
	
	public static void main(String[] args) throws URISyntaxException, ResponseAnalysisException {
		// TODO Auto-generated method stub
		String loginJson = System.getenv("rakuten_login_json");
		
		//castしない
		Login loginProc = LoginRakuten.getInstance();

		try {
			if (loginProc.login(loginJson)) {

				
				// ログイン成功

				// LOGOUT
				// loginProc.logout();

				InterfaceScreen accountListYenProc = new AccountListYenRakuten(loginProc);

				// 画面
				if (accountListYenProc.getScreen()) {
					System.out.println(loginProc.doc.html());
				} else {
					System.out.println(loginProc.doc.html());
					// LOGOUT 2回読んでも問題なし
					// loginProc.logout();
				}
				
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
	public boolean login(String loginInputParamJson) throws IOException, URISyntaxException, FailedToGetInputScreenException, ResponseAnalysisException {
		HashMap<String, String> param=super.getLoginForm(startUrl,loginFormName,loginInputParamJson);
		
		//login POST request
		res = connectMethodPost(loginUrl,param);
		
		// ログイン後の画面
		doc = res.parse();
		System.out.println(doc.html());
		
		//ログイン成功したかチェック
		boolean loginFlg = super.loginDecision("Rg_sec");			//Rg_sec or checkTk どっち？
		
		if (loginFlg) {
			//楽天の場合、ホーム画面からURLを取得するので、ホーム画面かどうかチェック
			
			//BV_SessionIDを取得
			Elements form_login = doc.getElementsByAttributeValue("id", "siteID");
			if (form_login.isEmpty()) {
				throw new ResponseAnalysisException("doc.getElementsByAttributeValue(\"id\", \"siteID\")");
			}

			Elements a = form_login.get(0).getElementsByTag("a");
			if (a.isEmpty()) {
				throw new ResponseAnalysisException("form_login.get(0).getElementsByTag(\"a\")");
			}

			String href  = a.get(0).attr("href");
			if (href.isEmpty()) {
				throw new ResponseAnalysisException("a.get(0).attr(\"href\")");
			}
			
			super.jsessionid = href.substring(href.indexOf(jsessionName+"=")+13, href.indexOf("?"));
			
			//口座一覧のテンプレートにBV_SessionIDをセット
			super.accountListYenUrl = MessageFormat.format(AccountListYenRakuten.accountListTemplate, super.jsessionid);
			//System.out.println(super.accountListYenUrl);
			
			
			//ホーム画面から口座一覧URL取得して設定
			//super.accountListYenUrl =
		}
		
		return loginFlg;
		
	}
}
