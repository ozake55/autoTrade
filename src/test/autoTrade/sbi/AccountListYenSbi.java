package test.autoTrade.sbi;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import test.autoTrade.InterfaceScreen;
import test.autoTrade.Login;

public class AccountListYenSbi implements InterfaceScreen {
	
	Login login = null;

	//static final String accountListQuery = "_ControlID=WPLETacR001Control&_DataStoreID=DSWPLETacR001Control&_PageID=DefaultPID&_ActionID=DefaultAID&getFlg=on";
	static final String accountListQuery = "_ControlID=WPLETacR001Control&_PageID=WPLETacR001Rlst10&_DataStoreID=DSWPLETacR001Control&getFlg=on&_ActionID=displayBK&gamen_status=A&gamen_status2=B&gamen_status3=S&account_get_kbn=2";

	static final String AccountListYenIndicateMes = "口座サマリー";
	
	///////

	/*
	 * コンストラクタ
	 */
	public AccountListYenSbi(Login _login){
		login = _login;
	}
	
	@Override
	public boolean getScreen() throws IOException, URISyntaxException {

		login.doc = login.conGetDocument(login.accountListYenUrl);
		
		boolean result = false;
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
							result = true;
							getAccountList();
							return result;
						}
					}
				}
			}
		}
		return result;
	

		/*
		SbiUtil sbiUtil = login.getSbiUtil();
		Connection conn = sbiUtil.getConnect(accountListQuery);
		Response res = sbiUtil.formSwitch(conn.cookies(login.getRes().cookies()).method(Method.GET).execute());
		login.setRes(res);
		*/
	}
	
	public boolean getAccountList() {
		//テキストの含まれるelementの親を逆階層順に取得
		boolean result = false;
		Elements genToku = login.doc.getElementsContainingOwnText("株式（現物/特定預り）");
		if (!genToku.isEmpty()) {
			//株式（現物/特定預り）有
			
			//３階層遡ったelementを取得
			Element genTokuParent = genToku.parents().get(3);
			
			//genToku table
			Elements genTokuTable = genTokuParent.children();

			// 最初のtitle２行を削除
			Elements genTokuContents = genTokuTable.next().next();
			
			// System.out.println(children);
			result = true;
		}
		return result;
	}

	
}

