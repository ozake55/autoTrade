package test.autoTrade.sbi;

import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import test.autoTrade.TradeUtil;

public class UtilSbi extends TradeUtil {
	
	static final String logoutMes = "口座をお持ちのお客様はログインしてください。";


	

	UtilSbi(String _baseURL) {
		super(_baseURL);
	}

	public String getBaseURL() {
		return baseURL;
	}

	public Response formSwitch(Response res) throws IOException {

		
		Connection conn = null;
		Document doc = null;

		//これをやらないと、通常res.parse();は一度しかできない
		res.bufferUp();
		
		doc = res.parse();
		//System.out.println(doc.html());
		////////

		////////
		// formSwitch check
		//formSwitchはSBI独自ルールのため
		Elements formSwitch = doc.getElementsByAttributeValue("name", "formSwitch");

		if (!formSwitch.isEmpty()) {

			this.baseURL = formSwitch.attr("action");

			Elements formSwitch_inputs = formSwitch.get(0).getElementsByTag("input");
			// ログインに必要なパラメータの設定
			HashMap<String, String> formSwitch_param = new HashMap<String, String>();
			for (Element ele : formSwitch_inputs) {
				formSwitch_param.put(ele.attr("name"), ele.attr("value"));
			}

			conn = this.getConnect("");
			res = conn.data(formSwitch_param).cookies(res.cookies()).method(Method.POST).execute();
			// doc = res.parse();
			// System.out.println(doc.html());
			////////
		}
		
		//ログアウトしているかどうか判定
		//ログアウトしている状態
		Elements logoutElements = doc.getElementsByAttributeValue("class", "fll01 bold");
		if (!logoutElements.isEmpty()) {
			Element element = logoutElements.get(0);
			//<p class="fll01 bold">口座をお持ちのお客様はログインしてください。</p>
			if (logoutMes.equals(element.childNode(0).toString().trim())) {
				//ログアウトしている状態
				System.out.println(element.childNode(0).toString().trim());
				//logoutExceptionをthrow
				
			}
		}
	
		return res;
	}


	

}
