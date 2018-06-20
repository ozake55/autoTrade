package test.autoTrade.rakuten;

import java.io.IOException;

import java.net.URISyntaxException;
import java.text.MessageFormat;

import test.autoTrade.InterfaceScreen;
import test.autoTrade.Login;


//https://member.rakuten-sec.co.jp/app/ass_jp_stk_possess_lst.do;BV_SessionID=2792B930C9B4D1F97E2FCA5B3701016C.42ad2c3e?eventType=directInit&type=&sub_type=&local=&gmn=S&smn=01&lmn=01&fmn=01

public class AccountListYenRakuten implements InterfaceScreen {
	
	static final String accountListTemplate = "https://member.rakuten-sec.co.jp/app/ass_jp_stk_possess_lst.do;BV_SessionID={0}?eventType=directInit&type=&sub_type=&local=&gmn=S&smn=01&lmn=01&fmn=01";
	
	
	Login login = null;
	
	/*
	 * コンストラクタ
	 */
	public AccountListYenRakuten(Login _login){
		login = _login;
	}
	
	@Override
	public boolean getScreen() throws IOException, URISyntaxException {
		// TODO Auto-generated method stub
		login.doc = login.conGetDocument(login.accountListYenHoldingsUrl);
		return false;
	}

}
