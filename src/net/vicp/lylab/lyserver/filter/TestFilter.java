package net.vicp.lylab.lyserver.filter;

import net.vicp.lylab.core.NonCloneableBaseObject;
import net.vicp.lylab.server.filter.Filter;
import net.vicp.lylab.utils.internet.LYSocket;
import net.vicp.lylab.utils.internet.impl.Message;

public class TestFilter extends NonCloneableBaseObject implements Filter {

	@Override
	public Message doFilter(LYSocket socket, Message request) {
		System.out.println("Client info:\nIP:" + socket.getHost() + "\nport:" + socket.getPort() + "\nmessage:" + request);
		return null;
	}

}
