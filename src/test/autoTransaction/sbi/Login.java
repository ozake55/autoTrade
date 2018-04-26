package test.autoTransaction.sbi;

import java.io.IOException;
import java.util.HashMap;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Login {
	public static void main(String[] args) throws IOException {
        Connection conn = null;
        Response res = null;
        Document doc = null;

        // login_formの取得
        String strURL = "https://site2.sbisec.co.jp/ETGate/";
        conn = Jsoup
            .connect(strURL)
            .header("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
            .header("Accept-Encoding", "gzip, deflate, br")
            .header("Accept-Language", "ja,en-US;q=0.9,en;q=0.8")
            .header("Host", "site1.sbisec.co.jp")
            .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
            .header("Connection", "keep-alive").ignoreContentType(true).ignoreHttpErrors(true);
        
    try {
            res = conn.execute();
            doc = res.parse();
            //System.out.println(doc.html());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }


    //レスポンスから必要情報の抽出            
        Elements form_login = doc.getElementsByAttributeValue("name","form_login");
        Elements inputs = form_login.get(0).getElementsByTag("input");

    //ログインに必要なパラメータの設定
        HashMap<String, String> param = new HashMap<String, String>();
        for(Element ele :inputs){
            param.put(ele.attr("name"), ele.attr("value"));
        }
        param.put("user_id", "yamada");
        param.put("user_password", "tarou");
        
        param.remove("ACT_login");
        param.put("ACT_login.x", "94");
        param.put("ACT_login.y", "16");
        
        param.put("BW_FLG", "chrome,65");
        param.put("JS_FLG", "1");
        param.put("_ActionID", "login");

        //ログイン用アクセス
        try {
            //res = conn.data(param).cookies(res.cookies()).execute();
            //doc = res.parse();
            doc = conn.data(param).cookies(res.cookies()).post();
            System.out.println(doc.html());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

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
