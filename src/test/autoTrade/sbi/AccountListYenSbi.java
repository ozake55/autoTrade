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

public class AccountListYenSbi implements InterfaceScreen {
	
	//static final String accountListQuery = "_ControlID=WPLETacR001Control&_DataStoreID=DSWPLETacR001Control&_PageID=DefaultPID&_ActionID=DefaultAID&getFlg=on";
	static final String accountListQuery = "_ControlID=WPLETacR001Control&_PageID=WPLETacR001Rlst10&_DataStoreID=DSWPLETacR001Control&getFlg=on&_ActionID=displayBK&gamen_status=A&gamen_status2=B&gamen_status3=S&account_get_kbn=2";

	static final String AccountListYenIndicateMes = "口座サマリー";
	
	
	
	//それ以外はエラー

	LoginSbi login = null;
	///////

	public LoginSbi getLogin() {
		return login;
	}

	public void setLogin(LoginSbi login) {
		this.login = login;
	}

	/*
	 * コンストラクタ
	 */
	public AccountListYenSbi(LoginSbi _login){
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
	
	public boolean getScreen() throws IOException {

		login.doc = login.conGetDocument(accountListQuery);
		
		Elements div = login.doc.getElementsByAttributeValue("class", "title-text");
		for (Element ele : div) {
			List<Node> nodes = ele.childNodes();
			for (Node node : nodes) {
				if (node instanceof Element) {
					Elements p = ((Element) node).getElementsByTag("b");
					for (Element element : p) {
						//口座サマリー
						System.out.println(element.childNode(0).toString().trim());
						if (AccountListYenIndicateMes.equals(element.childNode(0).toString().trim())) {
							return true;							
						}
					}
				}
			}
		}
		return false;
	

		/*
		SbiUtil sbiUtil = login.getSbiUtil();
		Connection conn = sbiUtil.getConnect(accountListQuery);
		Response res = sbiUtil.formSwitch(conn.cookies(login.getRes().cookies()).method(Method.GET).execute());
		login.setRes(res);
		*/
	}
	
}

