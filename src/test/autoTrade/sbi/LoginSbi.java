package test.autoTrade.sbi;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import test.autoTrade.InterfaceScreen;
import test.autoTrade.Login;
import test.autoTrade.TradeUtil;
import test.autoTrade.exception.FailedToGetInputScreenException;

public class LoginSbi extends Login {
	
	public static void main(String[] args) throws IOException, URISyntaxException {
		
		String loginJson = System.getenv("sbi_login_json");
		
		//castどうする？
		Login loginProc = LoginSbi.getInstance();

		try {
			if (loginProc.login(loginJson)) {
				// ログイン成功

				// LOGOUT
				// loginProc.logout();

				InterfaceScreen accountListYenProc = new AccountListYenSbi(loginProc);

				// 画面
				if (accountListYenProc.getScreen()) {
					//screen取得成功
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
	
	
		
	static final String hostAndPath = "https://site1.sbisec.co.jp" + "/ETGate/";
	
	//ログイン専用
	static final String startQuery = "_ControlID=WPLETlgR001Control&_DataStoreID=DSWPLETlgR001Control&_PageID=WPLETlgR001Rlgn50&_ActionID=login&getFlg=on";
	static final String loginFormName = "MyForm01";
	
	//口座一覧
	//static final String afterQuery = "_ControlID=WPLETacR001Control&_DataStoreID=DSWPLETacR001Control&_PageID=DefaultPID&_ActionID=DefaultAID&getFlg=on";
	//ホーム
	//String sfterQuery =  "_ControlID=WPLEThmR001Control&_DataStoreID=DSWPLEThmR001Control&_PageID=DefaultPID&_ActionID=DefaultAID&getFlg=on";
	
	static final String logoutQuery = "_ControlID=WPLETlgR001Control&_DataStoreID=DSWPLETlgR001Control&_PageID=WPLETlgR001Rlgn50&_ActionID=logout&getFlg=on";

	static final String logoutIndicateMes = "SBI証券をご利用いただきありがとうございました。";
	
	////////
	//
	private static LoginSbi loginSingleton = new LoginSbi();
	private LoginSbi(){
		util = new UtilSbi(hostAndPath);
	}
	
	public static LoginSbi getInstance() {
		return loginSingleton;
	}
	
	@Override
	public boolean login(String loginInputParamJson) throws IOException, FailedToGetInputScreenException, URISyntaxException {
				
		//loginInputParamJson={"user_id":"user_id", "user_password":"user_password", "_ActionID":"loginPortfolio"}
		HashMap<String, String> param=super.getLoginForm(startQuery,loginFormName,loginInputParamJson);

		//置き換える場合
		//param.put("_ActionID", "loginAcInfo");
		
		//login POST request
		res = connectMethodPost("",param);
		
		// ログイン後の画面
		//doc = res.parse();
		//System.out.println(doc.html());
		////
		
		//ログイン成功したかチェック
		boolean loginFlg = super.loginDecision("trading_site");
		
		if (loginFlg) {
			//SBIの場合、一覧画面のURLは固定
			super.accountListYenUrl = AccountListYenSbi.accountListQuery;
		}
		
		return loginFlg;
	}
	
	boolean logout() throws IOException, URISyntaxException {
		
		if (res == null) {
			return false;
		}

		//Connection conn = sbiUtil.getConnect(logoutQuery);
		//sbiUtil.formSwitch(conn.cookies(res.cookies()).method(Method.GET).execute());
		doc = conGetDocument(logoutQuery);
		//System.out.println(doc.html());

		Elements div = doc.getElementsByAttributeValue("class", "alC");

		for (Element ele : div) {
			List<Node> nodes = ele.childNodes();
			for (Node node : nodes) {
				if (node instanceof Element) {
					Elements p = ((Element) node).getElementsByTag("p");
					for (Element element : p) {
						if (element.attr("class").equals("fl01")) {
							
							System.out.println(element.childNode(0).toString().trim());
							
							if (logoutIndicateMes.equals(element.childNode(0).toString().trim())) {
								// "SBI証券をご利用いただきありがとうございました。"
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	

	@Override
	protected Response connectMethodGet(String query) throws IOException, URISyntaxException {
		Response _res = super.connectMethodGet(query);
		res = connectExp(_res);
		return res;
	}
	
	@Override
	protected Response connectMethodPost(String query, HashMap<String, String> param) throws IOException, URISyntaxException {
		//Connection conn = util.getConnect(query);
		//Response _res = conn.data(param).cookies(res.cookies()).method(Method.POST).execute();
		// formSwitch＋ログイン
		Response _res = super.connectMethodPost(query, param);
		res = connectExp(_res);
		return res;
	}
	
	public Response connectExp(Response res) throws IOException, URISyntaxException {
		// formSwitchはログイン時だけだと思う。。
		res = ((UtilSbi) util).formSwitch(res);
		
		//sessionが切れているかチェック
		
		//IOExceptionとlogoutExceptionをcatchしてハンドリング
		
		
		return res;
	}
	
}
