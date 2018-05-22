package test.autoTrade;

import java.io.IOException;
import java.util.HashMap;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;

public class Login {
	
	public Document doc = null;
	public Response res = null;

	///////
	protected TradeUtil util = null;
	///////
	
	public Document conGetDocument(String query) throws IOException {
		
		res = connectMethodGet(query);
		doc = res.parse();
		return doc;
	}
	
	protected Response connectMethodGet(String query) throws IOException {
		Connection conn = util.getConnect(query);
		Response _res = conn.cookies(res.cookies()).method(Method.GET).execute();
		return _res;
	}
	
	protected Response connectMethodPost(String query, HashMap<String, String> param) throws IOException {
		Connection conn = util.getConnect(query);
		Response _res = conn.data(param).cookies(res.cookies()).method(Method.POST).execute();
		return _res;
	}

}
