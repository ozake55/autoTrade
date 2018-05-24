package test.autoTrade;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;

import test.autoTrade.exception.FailedToGetInputScreenException;
import test.autoTrade.sbi.AccountListYen;
import test.autoTrade.sbi.InterfaceScreen;
import test.autoTrade.sbi.LoginSbi;
import test.autoTrade.*;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AutoTrade {

	static Response res = null;
	static Document doc = null;

	public static void main(String[] args) throws IOException {
		
		String loginJson = System.getenv("sbi_login_json");
		
		//castしない
		LoginSbi loginProc = LoginSbi.getInstance();

		try {
			if (loginProc.login(loginJson)) {
				// ログイン成功

				// LOGOUT
				// loginProc.logout();

				InterfaceScreen accountListYenProc = new AccountListYen(loginProc);

				// 画面
				if (accountListYenProc.getScreen()) {
					System.out.println(loginProc.doc.html());
				} else {

					// LOGOUT 2回読んでも問題なし
					// loginProc.logout();
				}
			}
		} catch (IOException e) {

		} catch (FailedToGetInputScreenException e) {
			System.out.println("FailedToGetInputScreenException発生");			
		}

	}

}
