package test.autoTransaction.sbi;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Login {

	String ua = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.117 Safari/537.36";

	//String strURL = "https://site2.sbisec.co.jp/ETGate/?_ControlID=WPLETlgR001Control&_PageID=WPLETlgR001Rlgn50&_DataStoreID=DSWPLETlgR001Control&_ActionID=login&getFlg=on";
	static String strURL = "https://site2.sbisec.co.jp/ETGate/";
	
	public static void main(String[] args) throws IOException {
		//header
		
		//おそらくauto
		//Content-Length: 552
		
		////////
		//Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8
		//Accept-Encoding: gzip, deflate, br
		//Accept-Language: ja,en-US;q=0.9,en;q=0.8
		//Cache-Control: max-age=0
		//Connection: keep-alive
		//Content-Type: application/x-www-form-urlencoded
		
		//Cookie
		
		//host:リクエストのホスト名やポート番号を通知
		//Host: site2.sbisec.co.jp
		
		//origin:CORS(Cross-Origin Resource Sharing)によるクロスドメイン通信の対象ドメインリクエスト
		//Origin: https://site2.sbisec.co.jp

		//Referer:参照元ページの URL
		//Referer: https://site2.sbisec.co.jp/ETGate/?_ControlID=WPLETlgR001Control&_PageID=WPLETlgR001Rlgn50&_DataStoreID=DSWPLETlgR001Control&_ActionID=login&getFlg=on
		
		//User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.117 Safari/537.36
		
		////
		//以下リクエストに設定しない
		//Do Not Trackヘッダ
		//DNT: 1
		//UUpgrade-Insecure-Requests:1	画像などのHTML内Linkがhttpの場合に、clientが自動でhttpsとして解釈するよFlg
		//Upgrade-Insecure-Requests: 1

		
		//ユーザーネーム又はパスワードが正しくありません。(WBLE015002)
		/*
		<div id="user_input">
        <input type="text" name="user_id" size="15" maxlength="32" style="width:170px;" id="imedisable" onfocus="Monc(&quot;UI&quot;)">
       </div> </td> 
      <td class="mtext" align="left" valign="top"> 
       <div id="password_input">
        <input type="password" name="user_password" maxlength="32" size="15" style="width:170px;" onfocus="Monc(&quot;PW&quot;)">
       </div>
       */
		//<input type="radio" name="_ActionID" value="loginAcInfo">口座管理<br>
		Document document = Jsoup.connect(strURL)
				//Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8
				.header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
				//Accept-Encoding: gzip, deflate, br
				.header("Accept-Encoding","gzip, deflate, br")
				//Accept-Language: ja,en-US;q=0.9,en;q=0.8
				.header("Accept-Language","ja,en-US;q=0.9,en;q=0.8")
				//Cache-Control: max-age=0
				.header("Cache-Control","max-age=0")
				//Connection: keep-alive
				.header("Connection","keep-alive")
				//Content-Type: application/x-www-form-urlencoded
				.header("Content-Type","application/x-www-form-urlencoded")
				//host:リクエストのホスト名やポート番号を通知
				//Host: site2.sbisec.co.jp
				.header("Host","site2.sbisec.co.jp")
				//origin:CORS(Cross-Origin Resource Sharing)によるクロスドメイン通信の対象ドメインリクエスト
				//Origin: https://site2.sbisec.co.jp
				.header("Origin","https://site2.sbisec.co.jp")
				//Referer:参照元ページの URL
				//Referer: https://site2.sbisec.co.jp/ETGate/?_ControlID=WPLETlgR001Control&_PageID=WPLETlgR001Rlgn50&_DataStoreID=DSWPLETlgR001Control&_ActionID=login&getFlg=on
				.header("Referer","https://site2.sbisec.co.jp/ETGate/?_ControlID=WPLETlgR001Control&_PageID=WPLETlgR001Rlgn50&_DataStoreID=DSWPLETlgR001Control&_ActionID=login&getFlg=on")
				//User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.117 Safari/537.36
				.header("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.117 Safari/537.36")
				
				////
				.data("_PageID", "WPLETlgR001Rlgn10")
				//.data("_DataStoreID", "xxxxxxxxxx_default_task_240_WPLETlgR001Rlgn50_login")
				.data("_ControlID", "WPLETlgR001Control")
				.data("_WID", "NoWID")
				//.data("_ORGWID", "")
				.data("_WID", "NoWID")
				//.data("_WIDManager", "")
				//.data("_WBSessionID", "xxxx_xxxx_xxxx_xxxxx")
				//.data("_preProcess", "")
				//.data("_TimeOutControl", "")
				.data("_WIDMode", "0")
				//.data("_WindowName", "")
				//.data("_ReturnPageInfo", "")
				.data("getFlg", "on")
				.data("JS_FLG", "1")
				.data("BW_FLG", "chrome,66")
				.data("burl", "iris_top")
				.data("cat1", "market")
				.data("cat2", "top")
				.data("dir", "tl1-top|tl2-map|tl5-jpn")
				.data("file", "index.html")
				.data("sw_page", "Banking")
				//.data("sw_param1", "")
				//.data("sw_param2", "")
				.data("user_id", "yamada")
				.data("user_password", "tarou")
				.data("logon.x", "70")
				.data("logon.y", "15")
				.data("_ActionID", "loginAcInfo")
				.post();
		System.out.println(document.html());
	}
}
