package test.autoTrade;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import test.autoTrade.exception.FailedToGetInputScreenException;
import test.autoTrade.exception.ResponseAnalysisException;

public class Login {
	
	public Document doc = null;
	public Response res = null;
	public String jsessionid = null;

	///////
	protected TradeUtil util = null;
	///////
	
	public String accountListYenUrl = null;
	
	public boolean login(String loginInputParamJson) throws IOException, URISyntaxException, FailedToGetInputScreenException, ResponseAnalysisException {
		// TODO Auto-generated method stub
		return false;
	}
	
	public HashMap<String, String> getLoginForm(String startQuery, String loginFormName, String loginInputParamJson) throws IOException, FailedToGetInputScreenException, URISyntaxException {
		// ログイン前はformSwitch必要なし
		Connection conn = util.getConnect(startQuery);

		// redirectを行いたくない場合は、.followRedirects(false).execute();
		res = conn.method(Method.GET)
				// .followRedirects(false)
				.execute();

		// resをチェック。302か307の場合はメンテナンス中と判断？それとも、redirect後、formが取れなかったらで判断？
		// →通常のログイン画面でredirectしているか？
		//
		// maintenanceException

		doc = res.parse();
		System.out.print(doc);
		//return doc;
		
		// ログイン画面のinput tag を取得。ログイン後の画面によってform名が異なるので注意！
		Elements form_login = doc.getElementsByAttributeValue("name", loginFormName);
		if (form_login.isEmpty()) {
			System.out.println("form_login = null");
			throw new FailedToGetInputScreenException();
		}

		Elements inputs = form_login.get(0).getElementsByTag("input");

		////////
		// ログイン
		// formパラメータの設定
		HashMap<String, String> param;
		
		//inputs elementからform param 生成
		param = util.getParam(inputs);
		
		try {
			Map<String, String> map = new HashMap<>();
			ObjectMapper mapper = new ObjectMapper();
			// convert JSON string to Map
			map = mapper.readValue(loginInputParamJson, new TypeReference<Map<String, String>>(){});

			for(Map.Entry<String, String> entry : map.entrySet()){
				param.put(entry.getKey(), entry.getValue());
				//System.out.println(entry.getKey() + ":" + entry.getValue());
			}

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//これでも@Overrideされた子クラスのmethod呼べるけど、わかりずらいのでここでは呼ばない。
		//res = connectMethodPost("",param);

		return param;
	}
	
	// ログインできているか、responseHeader内のcookieをチェック。
	public boolean loginDecision(String cookieName) {
		//ログイン判定
		if (res.cookies().containsKey(cookieName)) {
			System.out.println("login success");
			return true;
		} else {
			
			System.out.println("login false");
			return false;
		}
	}

	
	public Document conGetDocument(String query) throws IOException, URISyntaxException {
		
		res = connectMethodGet(query);
		doc = res.parse();
		return doc;
	}
	
	protected Response connectMethodGet(String urlStr) throws IOException, URISyntaxException {
		Connection conn = util.getConnect(urlStr);
		Response _res = conn.cookies(res.cookies()).method(Method.GET).execute();
		return _res;
	}
	
	protected Response connectMethodPost(String urlStr, HashMap<String, String> param) throws IOException,  URISyntaxException  {
		Connection conn = util.getConnect(urlStr);
		Response _res = conn.data(param).cookies(res.cookies()).method(Method.POST).execute();
		return _res;
	}

	public boolean logout() throws IOException, URISyntaxException {
		// TODO Auto-generated method stub
		return false;
	}






}
