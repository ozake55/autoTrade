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
		Element targetEle = div.get(0).child(0);
		//System.out.println(targetEle.text().trim());
		if (AccountListYenIndicateMes.equals(targetEle.text().trim())) {
			return getAccountList();
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
			Element stockListParent = genToku.parents().get(3);
			
			//table
			Elements stockListTable = stockListParent.children();

			// 最初のtitle２行を削除
			Elements stockList = stockListTable.next().next();
		
			//保有銘柄数
			int numberOfStocks=stockList.size()/2;
			
			Map<String, String> stockInfoMap = new HashMap<String, String>();
			stockInfoMap.put("stockCompany", "sbi");
			
			//最初の<TD>node
			for(int i=0; i<numberOfStocks; i++) {
				Element stockInfoEle = stockList.get(i*2);
				
				//StockInfo行の左<TD>				
				Element stockCodeEle = stockInfoEle.child(0);
				//codeの取得
				//文字列を、" "（空白）を区切り文字として2つに分割。[0]がcode
				String[] StockInfoArray = stockCodeEle.text().trim().split(" ",2);
				//int code = Integer.parseInt(StockInfoArray[0].trim());
				String stockCode =  StockInfoArray[0].trim();
				stockInfoMap.put("stockCode", stockCode);
				
				//nameのElement取得（<a>tag）
				Element stockNameEle = stockCodeEle.child(0);
				String stockName = stockNameEle.text().trim();
				stockInfoMap.put("stockName", stockName);
				
				//System.out.println(stockCodeAndNameEle.childNode(0).toString());
				Element stockValueEle = stockList.get(i*2+1);
				System.out.println(stockValueEle);
			}
			// System.out.println(children);
			result = true;
		}
		return result;
	}

	
}

