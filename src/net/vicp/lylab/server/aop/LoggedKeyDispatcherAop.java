package net.vicp.lylab.server.aop;

import net.vicp.lylab.core.CoreDef;
import net.vicp.lylab.core.model.Message;
import net.vicp.lylab.core.model.RPCMessage;
import net.vicp.lylab.server.utils.Logger;

public class LoggedKeyDispatcherAop extends SimpleKeyDispatcherAop<Message> {
	@Override
	protected void logger(Message request, Message response) {
		System.out.println("Access key:" + request.getKey() + "\tAccess rpcKey:" + ((RPCMessage) request).getRpcKey()
				+ "\nBefore:" + request + "\nAfter:" + response);
		((Logger) CoreDef.config.getConfig("Singleton").getObject("Logger"))
				.appendLine("Access key:" + request.getKey() + "\tAccess rpcKey:" + ((RPCMessage) request).getRpcKey()
						+ "\nBefore:" + request + "\nAfter:" + response);
	}

}