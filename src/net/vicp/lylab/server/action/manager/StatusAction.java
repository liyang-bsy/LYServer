package net.vicp.lylab.server.action.manager;

import java.util.Date;

import net.vicp.lylab.core.BaseAction;
import net.vicp.lylab.utils.Utils;

public class StatusAction extends BaseAction {

	@Override
	public void exec() {
		try {
			do {
				getResponse().getBody().put("Current time", Utils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
				getResponse().success();
			} while (false);
		} catch (Exception e) {
			log.error("Exception detected:" + Utils.getStringFromException(e));
		}
	}

	@Override
	public boolean foundBadParameter() {
		// TODO Auto-generated method stub
		return false;
	}

}
