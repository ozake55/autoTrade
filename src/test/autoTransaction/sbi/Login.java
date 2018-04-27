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
	
	static Connection getConnect(String strUrl) throws MalformedURLException {
		 Connection conn = null;
		 
		 URL url = new URL(strUrl);
		 String host = url.getHost();
		 
		 conn = Jsoup
		            .connect(strUrl)
		            .header("Accept",
		                    "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
		            .header("Accept-Encoding", "gzip, deflate, br")
		            .header("Accept-Language", "ja,en-US;q=0.9,en;q=0.8")
		            //.header("Host", "site1.sbisec.co.jp")
		            .header("Host", host)
		            .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
		            .header("Connection", "keep-alive").ignoreContentType(true).ignoreHttpErrors(true);
		 return conn;
	}
	public static void main(String[] args) throws IOException {
        Response res = null;
        Document doc = null;

        String strURL = "https://site1.sbisec.co.jp";
        Connection conn = getConnect(strURL+"/ETGate/");
        
        // login_formの取得
   
        
    try {
    		//res = conn.execute();
            res = conn.method(Method.GET).execute();
            doc = res.parse();
            //doc = conn.get();
            //System.out.println(doc.html());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }


    //レスポンスから必要情報の抽出
        Elements form_login = doc.getElementsByAttributeValue("name","form_login");
        Elements inputs = form_login.get(0).getElementsByTag("input");
        String form_login_action = form_login.attr("action");

        if (form_login.isEmpty()) {
        	System.out.println("form_login = null");
        	return;
        }

        ////////
    //ログインに必要なパラメータの設定
        HashMap<String, String> param = new HashMap<String, String>();
        for(Element ele :inputs){
            param.put(ele.attr("name"), ele.attr("value"));
        }
        param.put("user_id", userid);
        param.put("user_password", password);
        
        param.remove("ACT_login");
        param.put("ACT_login.x", "94");
        param.put("ACT_login.y", "16");
        
        param.put("BW_FLG", "chrome,65");
        param.put("JS_FLG", "1");
        param.put("_ActionID", "login");

        conn = getConnect(strURL+form_login_action);
        //ログイン用アクセス
        try {
            res = conn.data(param).cookies(res.cookies()).method(Method.POST).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        doc = res.parse();
        //doc = conn.data(param).cookies(res.cookies()).post();
        System.out.println(doc.html());
        ////////
        
        ////////
        //formSwitch check        
        Elements formSwitch = doc.getElementsByAttributeValue("name","formSwitch");
        if (!formSwitch.isEmpty()) {
        	String formSwitch_action = formSwitch.attr("action");
        	Elements formSwitch_inputs = formSwitch.get(0).getElementsByTag("input");
            //ログインに必要なパラメータの設定
            HashMap<String, String> formSwitch_param = new HashMap<String, String>();
            for(Element ele :formSwitch_inputs){
            	formSwitch_param.put(ele.attr("name"), ele.attr("value"));
            }
            conn = getConnect(formSwitch_action);
            try {
                res = conn.data(formSwitch_param).cookies(res.cookies()).method(Method.POST).execute();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            doc = res.parse();
            //doc = conn.data(param).cookies(res.cookies()).post();
            System.out.println(doc.html());
            ////////
        }
        

        //https://www.sbisec.co.jp/ETGate/?OutSide=on&_ControlID=WPLETacR001Control&_PageID=DefaultPID&_DataStoreID=DSWPLETacR001Control&_SeqNo=2003_06_12_10_02_34.574_ExecuteThread%3A__45__for_queue%3A__wplExecute_Queue__WPLETlgR001Rlgn20_login&getFlg=on&_ActionID=DefaultAID&int_pr1=150313_cmn_gnavi:2_dmenu_01
        //レスポンスから必要情報の抽出            
        Elements link2_account_menu = doc.getElementsByAttributeValue("id","link2_account_menu");
        //Elements tag_a = link2_account_menu.get(0).getElementsByTag("a");

        //判定
        if (res.cookies().containsKey("trading_site")) {
        	System.out.println("success");
            return;
        } else {
        	System.out.println("false");
            return;
        }
	}
}
