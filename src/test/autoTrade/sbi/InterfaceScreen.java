package test.autoTrade.sbi;

import java.io.IOException;

import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;

public interface InterfaceScreen {
	Login getLogin();
	void setLogin(Login login);
	//Response getRes();
	//void setRes(Response res);
	//SbiUtil getSbiUtil();
	//void setSbiUtil(SbiUtil sbiUtil);
	boolean getScreen() throws IOException;
}
