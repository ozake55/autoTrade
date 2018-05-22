package test.autoTrade.sbi;

import java.io.IOException;

import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;

public interface InterfaceScreen {
	LoginSbi getLogin();
	void setLogin(LoginSbi login);
	//Response getRes();
	//void setRes(Response res);
	//SbiUtil getSbiUtil();
	//void setSbiUtil(SbiUtil sbiUtil);
	boolean getScreen() throws IOException;
}
