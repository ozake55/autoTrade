package test.autoTransaction.sbi;

import java.io.IOException;
import java.util.HashMap;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Tools {

	static Response formSwitch(Response res) throws Throwable {
		
		Document doc = null;
	       doc = res.parse();
	        System.out.println(doc.html());
	        ////////
	        
	        ////////
	        //formSwitch check
	        Elements formSwitch = doc.getElementsByAttributeValue("name","formSwitch");
	        
	        if (!formSwitch.isEmpty()) {
		        /*
	        	
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
	            //doc = res.parse();
	            ////////
	        */
	        }
		return res;
	}
}
