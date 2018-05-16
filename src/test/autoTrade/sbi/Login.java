package test.autoTrade.sbi;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import test.autoTrade.exception.FailedToGetInputScreenException;

public class Login {
	
	private static Login loginSingleton = new Login();
		
	static final String hostAndPath = "https://site1.sbisec.co.jp" + "/ETGate/";
	
	//ログイン専用
	static final String startQuery = "_ControlID=WPLETlgR001Control&_DataStoreID=DSWPLETlgR001Control&_PageID=WPLETlgR001Rlgn50&_ActionID=login&getFlg=on";
	
	//口座一覧
	//static final String afterQuery = "_ControlID=WPLETacR001Control&_DataStoreID=DSWPLETacR001Control&_PageID=DefaultPID&_ActionID=DefaultAID&getFlg=on";
	//ホーム
	//String sfterQuery =  "_ControlID=WPLEThmR001Control&_DataStoreID=DSWPLEThmR001Control&_PageID=DefaultPID&_ActionID=DefaultAID&getFlg=on";
	
	static final String logoutQuery = "_ControlID=WPLETlgR001Control&_DataStoreID=DSWPLETlgR001Control&_PageID=WPLETlgR001Rlgn50&_ActionID=logout&getFlg=on";

	static final String logoutIndicateMes = "SBI証券をご利用いただきありがとうございました。";
	
	
	///////
	Document doc = null;
	Response res = null;
	SbiUtil sbiUtil = null;
	///////

	private Login(){
		sbiUtil = new SbiUtil(hostAndPath);
	}
	
	public static Login getInstance() {
		return loginSingleton;
	}
	
	boolean login(String userid, String password) throws IOException, FailedToGetInputScreenException {
		
		//ログイン前はformSwitch必要なし
		Connection conn = sbiUtil.getConnect(startQuery);
		
		//redirectを行いたくない場合は、.followRedirects(false).execute();
		res = conn.method(Method.GET)
				//.followRedirects(false)
				.execute();
		
		//resをチェック。302か307の場合はメンテナンス中と判断？それとも、redirect後、formが取れなかったらで判断？
		//→通常のログイン画面でredirectしているか？
		//
		//maintenanceException
		
		doc = res.parse();

		// ログイン画面のinput tag を取得。ログイン後の画面によってform名が異なるので注意！
		Elements form_login = doc.getElementsByAttributeValue("name", "MyForm01");
		if (form_login.isEmpty()) {
			System.out.println("form_login = null");
			throw new FailedToGetInputScreenException();
		}

		Elements inputs = form_login.get(0).getElementsByTag("input");

		sbiUtil.getParam(inputs);
		////////
		// ログイン
		// formパラメータの設定
		HashMap<String, String> param;
		
		//inputs elementからform param 生成
		param = sbiUtil.getParam(inputs);
		
		param.put("user_id", userid);
		param.put("user_password", password);
		//param.put("user_id", "yamada");
		//param.put("user_password", "tarou1234");
		
		//置き換え
		//param.put("_ActionID", "loginAcInfo");
		param.put("_ActionID", "loginPortfolio");
		
		res = connectMethodPost("",param);
		
		// ログイン後の画面
		//doc = res.parse();
		//System.out.println(doc.html());
		
		/////

		////
		// ログインできているか、response headerのcookieをチェック。
		// 以後のresponseには新たなset-cookieは含まれないので注意
		if (res.cookies().containsKey("trading_site")) {
			System.out.println("login success");
			return true;
		} else {
			
			System.out.println("login false");
			return false;
		}
	}

		////////
	/*
	public void getScreen() throws IOException {
	
		// ログイン後の画面は口座画面かチェック（お知らせ等表示される場合がある）
		// もしも、口座画面で無い場合、再度ログイン画面に遷移
		Connection conn = sbiUtil.getConnect(afterQuery);
		// formSwitchはログイン時だけだと思う。。
		// res = conn.cookies(res.cookies()).method(Method.GET).execute();
		res = sbiUtil.formSwitch(conn.cookies(res.cookies()).method(Method.GET).execute());
		//Document doc = res.parse();
	}
	*/
	
	boolean logout() throws IOException {
		
		if (res == null) {
			return false;
		}

		//Connection conn = sbiUtil.getConnect(logoutQuery);
		//sbiUtil.formSwitch(conn.cookies(res.cookies()).method(Method.GET).execute());
		doc = connectMethodGet(logoutQuery);
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
	
	
	public Document connectMethodGet(String query) throws IOException {
		res = conGet(query);
		doc = res.parse();
		return doc;
	}

	private Response conGet(String query) throws IOException {
		
		Connection conn = sbiUtil.getConnect(query);
		res = connectExp(conn.cookies(res.cookies()).method(Method.GET).execute());
		return res;
	}
	
	private Response connectMethodPost(String query, HashMap<String, String> param) throws IOException {
		Connection conn = sbiUtil.getConnect(query);
		// formSwitch＋ログイン
		res = connectExp(conn.data(param).cookies(res.cookies()).method(Method.POST).execute());
		return res;
	}
	
	public Response connectExp(Response res) throws IOException {
		// formSwitchはログイン時だけだと思う。。
		res = sbiUtil.formSwitch(res);
		
		//sessionが切れているかチェック
		
		//IOExceptionとlogoutExceptionをcatchしてハンドリング
		
		
		return res;
	}
	
}
