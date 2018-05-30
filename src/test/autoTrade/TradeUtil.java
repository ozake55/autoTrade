package test.autoTrade;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TradeUtil {
	protected String baseURL = null;
	
	public TradeUtil(String _baseURL) {
		this.baseURL = _baseURL;
	}
	
	public HashMap<String, String> getParam(Elements inputs) {
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
	
	public Connection getConnect(String urlStr) throws MalformedURLException, URISyntaxException {

		Connection conn = null;

		if (urlStr == null) {urlStr = "";}
		URI checkAbsolute = new URI(urlStr);
		
		URL hostUrl = null;
		String connectUrl = null;
		if (checkAbsolute.isAbsolute()) {
			//絶対パス
			hostUrl = new URL(urlStr);
			connectUrl = urlStr;
		} else {
			if (!urlStr.equals("")){
				urlStr = "?"+urlStr;
			}
			hostUrl = new URL(this.baseURL);
			connectUrl = baseURL + urlStr;
		}
		
		String host = hostUrl.getHost();

		conn = Jsoup.connect(connectUrl)
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
