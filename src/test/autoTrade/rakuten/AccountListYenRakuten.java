package test.autoTrade.rakuten;

import java.io.IOException;
import java.net.URISyntaxException;

import test.autoTrade.InterfaceScreen;
import test.autoTrade.Login;

public class AccountListYenRakuten implements InterfaceScreen {
	
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
		return false;
	}

}
