package net.vicp.lylab.server.aop;

import java.net.Socket;

import org.apache.commons.lang3.StringUtils;

import net.vicp.lylab.core.BaseAction;
import net.vicp.lylab.core.CoreDef;
import net.vicp.lylab.core.model.Message;
import net.vicp.lylab.server.aop.SimpleKeyDispatcherAop;
import net.vicp.lylab.server.filter.Filter;
import net.vicp.lylab.server.utils.Logger;
import net.vicp.lylab.utils.Utils;
import net.vicp.lylab.utils.internet.HeartBeat;

public class LoggedKeyDispatcherAop extends SimpleKeyDispatcherAop {

	@Override
	public byte[] doAction(Socket client, byte[] requestByte, int offset) {
		Message request = null;

		String key = null;
		BaseAction action = null;
		Message response = new Message();
		try {
			do {
				try {
					Object obj = protocol.decode(requestByte, offset);
					if(obj instanceof HeartBeat)
						return protocol.encode(obj);
					request = (Message) obj;
				} catch (Exception e) {
					log.debug(Utils.getStringFromException(e));
				}
				if(request == null) {
					response.setCode(0x00001);
					response.setMessage("Message not found");
					break;
				}
				// do start filter
				if (filterChain != null && filterChain.size() != 0)
					for (Filter filter : filterChain) {
						Message ret = null;
						if ((ret = filter.doFilter(client, request)) != null)
							return protocol.encode(ret);
					}
				// gain key from request
				key = request.getKey();
				if (StringUtils.isBlank(key)) {
					response.setCode(0x00002);
					response.setMessage("Key not found");
					break;
				}
				response.setKey(key);
				// get action related to key
				try {
					action = (BaseAction) CoreDef.config.getConfig("Aop").getNewInstance(key + "Action");
				} catch (Exception e) { }
				if (action == null) {
					response.setCode(0x00003);
					response.setMessage("Action not found");
					break;
				}
				// Initialize action
				action.setSocket(client);
				action.setRequest(request);
				action.setResponse(response);
				// execute action
				try {
					action.exec();
				} catch (Throwable t) {
					log.error(Utils.getStringFromThrowable(t));
				}
			} while (false);
		} catch (Exception e) {
			log.error(Utils.getStringFromException(e));
		}
		// to logger
		System.out.println("Access key:" + key  + "\nBefore:" + request + "\nAfter:" + response);
		((Logger) CoreDef.config.getConfig("Singleton").getObject("Logger")).appendLine(
				"Access key:" + key  + "\nBefore:" + request + "\nAfter:" + response);
		return protocol.encode(response);
	}

}