package net.vicp.lylab.lyserver.action.manager;

import java.io.IOException;

import net.vicp.lylab.core.BaseAction;
import net.vicp.lylab.lyserver.ServerBegin;

public class StopAction extends BaseAction {

	@Override
	public void exec() {
		System.out.println("host:" + socket.getHost() + "发来终止请求");
		ServerBegin.running = false;
		try {
			ServerBegin.ss.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		response.success();
	}

}
