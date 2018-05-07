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

public class HostManage {

	String baseURL = null;

	HostManage(String _baseURL) {
		this.baseURL = _baseURL;
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
		return res;
	}

	HashMap<String, String> getParam(Elements inputs) {
		// ログイン
		// formパラメータの設定
		HashMap<String, String> param = new HashMap<String, String>();
		for (Element ele : inputs) {
			// input tagを再設定
			param.put(ele.attr("name"), ele.attr("value"));
		}
		// 追加
		// param.remove("ACT_login");
		// param.put("ACT_login.x", "131");
		// param.put("ACT_login.y", "15");
		// param.put("BW_FLG", "chrome,65");
		// param.put("JS_FLG", "1");
		return param;
	}
	
	Connection getConnect(String query) throws MalformedURLException {

		Connection conn = null;
		if (query == null) {query = "";}
		if (!query.equals("")){
			query = "?"+query;
		}	

		URL url = new URL(this.baseURL);
		String host = url.getHost();

		conn = Jsoup.connect(baseURL + query)
				.header("Accept",
						"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
				.header("Accept-Encoding", "gzip, deflate, br").header("Accept-Language", "ja,en-US;q=0.9,en;q=0.8")
				.header("Host", host)
				.header("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
				.header("Connection", "keep-alive").ignoreContentType(true).ignoreHttpErrors(true);
		return conn;
	}
}
