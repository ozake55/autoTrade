package test.autoTransaction.sbi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Login {

	static String userid = System.getenv("userid");
	static String password = System.getenv("password");

	// static String baseURL = "https://site1.sbisec.co.jp"+"/ETGate/";
	static Response res = null;
	static Document doc = null;

	HostManage host = null;
	
	//String query = null;
	
	Login(HostManage _host){
		host = _host;
		//query = _query;
	}
	
	/*
	Response logout(String _query) throws IOException {
		
	}
	*/
	
	Response execute(String _query) throws IOException {
		
		Connection conn = host.getConnect(_query);
		
			res = conn.method(Method.GET).execute();

			/*cookie check
			// 判定
			if (res.cookies().containsKey("trading_site")) {
				System.out.println("success");
			} else {
				System.out.println("false");
			}
			*/
			
			doc = res.parse();
			// System.out.println(doc.html());

		// ログイン画面のinput tag を取得。ログイン後の画面によってform名が異なるので注意！
		Elements form_login = doc.getElementsByAttributeValue("name", "MyForm01");
		if (form_login.isEmpty()) {
			System.out.println("form_login = null");
			return null;
		}
		
		Elements inputs = form_login.get(0).getElementsByTag("input");


		host.getParam(inputs);
		////////
		//ログイン
		// formパラメータの設定
		HashMap<String, String> param;
		param = host.getParam(inputs);
		param.put("user_id", userid);
		param.put("user_password", password);

		conn = host.getConnect("");

		//formSwitch＋ログイン
		res = host.formSwitch(conn.data(param).cookies(res.cookies()).method(Method.POST).execute());
		// ログイン後の画面
		//doc = res.parse();
		//System.out.println(doc.html());
		
		////////
		//ログイン後の画面は口座画面かチェック（お知らせ等表示される場合がある）

		//もしも、口座画面で無い場合、再度ログイン画面に遷移
		conn = host.getConnect(_query);
		res = conn.cookies(res.cookies()).method(Method.GET).execute();
		//res = host.formSwitch(conn.cookies(res.cookies()).method(Method.GET).execute());
		
		//なぜかcookie無い。。
		//
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
		//String logoutQuery = "_ControlID=WPLETlgR001Control&_DataStoreID=DSWPLETlgR001Control&_PageID=WPLETlgR001Rlgn50&_ActionID=logout&getFlg=on";

		res = login.execute(startQuery);

		// ログイン後の画面
		doc = res.parse();
		System.out.println(doc.html());
		
		// 判定
		if (res.cookies().containsKey("trading_site")) {
			System.out.println("success");
			return;
		} else {
			System.out.println("false");
			return;
		}

		/*
		 * //////// //口座管理画面遷移 //レスポンスから必要情報の抽出 conn = getConnect(AccountStrUrl); try {
		 * res = conn.cookies(res.cookies()).method(Method.GET).execute(); } catch
		 * (IOException e) { e.printStackTrace(); return; } doc = res.parse();
		 * System.out.println(doc.html());
		 */

	}
}
