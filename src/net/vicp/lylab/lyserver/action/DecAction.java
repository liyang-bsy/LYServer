package net.vicp.lylab.lyserver.action;

import net.vicp.lylab.core.BaseAction;

public class DecAction extends BaseAction {

	@Override
	public void exec() {
		do {
			Integer i = (Integer) getRequest().getBody().get("int");
			if (i == null) {
				getResponse().setCode(-2);
				getResponse().setMessage("数字不存在");
				break;
			}
			getResponse().getBody().put("int", i-1);
		getResponse().success(); } while (false);
	}

}
