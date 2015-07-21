package net.vicp.lylab.lyserver.action.manager;

import net.vicp.lylab.core.BaseAction;
import net.vicp.lylab.lyserver.ServerRuntime;

public class StopAction extends BaseAction {

	@Override
	public void exec() {
		System.out.println("host:" + clientSocket.getInetAddress().getHostAddress() + "发来终止请求");
		ServerRuntime.running = false;
		ServerRuntime.serverRuntime.callStop();
		response.success();
	}

}
