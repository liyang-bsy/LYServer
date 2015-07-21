package net.vicp.lylab.lyserver.filter;

import java.net.Socket;

import net.vicp.lylab.core.NonCloneableBaseObject;
import net.vicp.lylab.core.model.Message;
import net.vicp.lylab.server.filter.Filter;

public class TestFilter extends NonCloneableBaseObject implements Filter {

	@Override
	public Message doFilter(Socket socket, Message request) {
		System.out.println("Client info:\nIP:" + socket.getInetAddress().getHostAddress() + "\nport:" + socket.getPort() + "\nmessage:" + request);
		return null;
	}

}
