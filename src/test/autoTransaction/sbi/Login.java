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
	
	static String baseURL = "https://site1.sbisec.co.jp"+"/ETGate/";
	static Response res = null;
	static Document doc = null;

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
		            .header("Host", host)
		            .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
		            .header("Connection", "keep-alive").ignoreContentType(true).ignoreHttpErrors(true);
		 return conn;
	}
	public static void main(String[] args) throws IOException {
        
        //Connection conn = getConnect(strURL");
        Connection conn = getConnect(baseURL+"?_ControlID=WPLETacR001Control&_PageID=DefaultPID&_DataStoreID=DSWPLETacR001Control&getFlg=on&_ActionID=DefaultAID");
    try {
            res = conn.method(Method.GET).execute();
            doc = res.parse();
            //doc = conn.get();
            //System.out.println(doc.html());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

    //ログイン画面のinput tag を取得
        //Elements form_login = doc.getElementsByAttributeValue("name","form_login");
        Elements form_login = doc.getElementsByAttributeValue("name","MyForm01");
        Elements inputs = form_login.get(0).getElementsByTag("input");

        if (form_login.isEmpty()) {
        	System.out.println("form_login = null");
        	return;
        }

        ////////
        //パラメータの追加設定
        HashMap<String, String> param = new HashMap<String, String>();
        for(Element ele :inputs){
        	//inpu tagを再設定
            param.put(ele.attr("name"), ele.attr("value"));
        }
        //追加
        //param.remove("ACT_login");
        //param.put("ACT_login.x", "131");
        //param.put("ACT_login.y", "15");
        //param.put("BW_FLG", "chrome,65");
        //param.put("JS_FLG", "1");
        param.put("user_id", userid);
        param.put("user_password", password);
        
        conn = getConnect(baseURL);
        try {
            res = conn.data(param).cookies(res.cookies()).method(Method.POST).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        doc = res.parse();
        System.out.println(doc.html());
        ////////
        
        ////////
        //formSwitch check
        Elements formSwitch = doc.getElementsByAttributeValue("name","formSwitch");
        if (!formSwitch.isEmpty()) {
        	
        	baseURL = formSwitch.attr("action");
        	
        	Elements formSwitch_inputs = formSwitch.get(0).getElementsByTag("input");
            //ログインに必要なパラメータの設定
            HashMap<String, String> formSwitch_param = new HashMap<String, String>();
            for(Element ele :formSwitch_inputs){
            	formSwitch_param.put(ele.attr("name"), ele.attr("value"));
            }
            conn = getConnect(baseURL);
            try {
                res = conn.data(formSwitch_param).cookies(res.cookies()).method(Method.POST).execute();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            doc = res.parse();
            ////////
        }
        
        //ログイン後画面
        System.out.println(doc.html());      

        //判定
        if (res.cookies().containsKey("trading_site")) {
        	System.out.println("success");
            return;
        } else {
        	System.out.println("false");
            return;
        }
        
        /*
        ////////
        //口座管理画面遷移
        //レスポンスから必要情報の抽出
        conn = getConnect(AccountStrUrl);
        try {
            res = conn.cookies(res.cookies()).method(Method.GET).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        doc = res.parse();
        System.out.println(doc.html());
         */
        
	}
}
