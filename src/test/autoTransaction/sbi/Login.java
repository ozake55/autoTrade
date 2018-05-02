package test.autoTransaction.sbi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class Login {

	static String userid = System.getenv("userid");
	static String password = System.getenv("password");

	static Response res = null;
	static Document doc = null;

	HostManage host = null;
	
	Login(HostManage _host){
		host = _host;
		//query = _query;
	}
	
	boolean logout(String _query) throws IOException {
		
		if (res == null) {
			return false;
		}

		Connection conn = host.getConnect(_query);
		// res = conn.cookies(res.cookies()).method(Method.GET).execute();
		// formSwitchはログイン時だけだと思うけど。。
		res = host.formSwitch(conn.cookies(res.cookies()).method(Method.GET).execute());

		doc = res.parse();
		// System.out.println(doc.html());

		Elements div = doc.getElementsByAttributeValue("class", "alC");

		ArrayList<String> list = new ArrayList<>();
		for (Element ele : div) {
			List<Node> nodes = ele.childNodes();
			for (Node node : nodes) {
				if (node instanceof Element) {
					Elements p = ((Element) node).getElementsByTag("p");
					for (Element element : p) {
						if (element.attr("class").equals("fl01")) {
							// "SBI証券をご利用いただきありがとうございました。"
							System.out.println(element.childNode(0).toString().trim());
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	Response login(String _query) throws IOException {

		
		Connection conn = host.getConnect(_query);

		res = conn.method(Method.GET).execute();
		doc = res.parse();

		// ログイン画面のinput tag を取得。ログイン後の画面によってform名が異なるので注意！
		Elements form_login = doc.getElementsByAttributeValue("name", "MyForm01");
		if (form_login.isEmpty()) {
			System.out.println("form_login = null");
			return null;
		}

		Elements inputs = form_login.get(0).getElementsByTag("input");

		host.getParam(inputs);
		////////
		// ログイン
		// formパラメータの設定
		HashMap<String, String> param;
		param = host.getParam(inputs);
		param.put("user_id", userid);
		param.put("user_password", password);
		//param.put("user_id", "yamada");
		//param.put("user_password", "tarou1234");

		conn = host.getConnect("");

		// formSwitch＋ログイン
		res = host.formSwitch(conn.data(param).cookies(res.cookies()).method(Method.POST).execute());
		// ログイン後の画面
		// doc = res.parse();
		// System.out.println(doc.html());
		/////

		////
		// ログインできているか、response headerのcookieをチェック。
		// 以後のresponseには新たなset-cookieは含まれないので注意
		if (res.cookies().containsKey("trading_site")) {
			System.out.println("login success");
		} else {
			System.out.println("login false");
			return null;
		}

		////////
		// ログイン後の画面は口座画面かチェック（お知らせ等表示される場合がある）

		// もしも、口座画面で無い場合、再度ログイン画面に遷移
		conn = host.getConnect(_query);
		// res = conn.cookies(res.cookies()).method(Method.GET).execute();
		// formSwitchはログイン時だけだと思う。。
		res = host.formSwitch(conn.cookies(res.cookies()).method(Method.GET).execute());
		////////
		return res;
	}
	
	public static void main(String[] args) throws IOException {

		HostManage hostMng = new HostManage("https://site1.sbisec.co.jp" + "/ETGate/");
		Login login = new Login(hostMng);
		//重要なお知らせがあると、必ずお知らせ画面が表示されてしまうため、ログイン後に、再度"口座画面"にアクセス必要
		
		//口座管理
		String startQuery = "_ControlID=WPLETacR001Control&_DataStoreID=DSWPLETacR001Control&_PageID=DefaultPID&_ActionID=DefaultAID&getFlg=on";
		//ホーム
		//String startQuery =  "_ControlID=WPLEThmR001Control&_DataStoreID=DSWPLEThmR001Control&_PageID=DefaultPID&_ActionID=DefaultAID&getFlg=on";
		String logoutQuery = "_ControlID=WPLETlgR001Control&_DataStoreID=DSWPLETlgR001Control&_PageID=WPLETlgR001Rlgn50&_ActionID=logout&getFlg=on";
		
		res = login.login(startQuery);
		if (res != null){
			// ログイン後の画面
			doc = res.parse();
			System.out.println(doc.html());
			
			//LOGOUT
			login.logout(logoutQuery);
		}
		

	}
}
