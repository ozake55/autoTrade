package test.autoTrade.sbi;

import java.io.IOException;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class AccountListYen implements InterfaceScreen {
	
	static final String accountListQuery = "_ControlID=WPLETacR001Control&_DataStoreID=DSWPLETacR001Control&_PageID=DefaultPID&_ActionID=DefaultAID&getFlg=on";

	//<div class="title-text"><b>口座サマリー</b></div>

	///////
	//Response res = null;
	//SbiUtil sbiUtil = null;
	Login login = null;
	///////

	public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}

	/*
	 * コンストラクタ
	 */
	AccountListYen(Login _login){
		login = _login;
	}

	/*
	public Response getRes() {
		return res;
	}
	public SbiUtil getSbiUtil() {
		return sbiUtil;
	}
	*/
	
	public Document getScreen() throws IOException {

		Response res = login.connectMethodGet(accountListQuery);
		return res.parse();
		
		/*
		SbiUtil sbiUtil = login.getSbiUtil();
		Connection conn = sbiUtil.getConnect(accountListQuery);
		Response res = sbiUtil.formSwitch(conn.cookies(login.getRes().cookies()).method(Method.GET).execute());
		login.setRes(res);
		*/
	}
	
}

