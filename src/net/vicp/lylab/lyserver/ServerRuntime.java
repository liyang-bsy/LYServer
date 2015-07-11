package net.vicp.lylab.lyserver;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.text.DecimalFormat;

import net.vicp.lylab.core.CoreDef;
import net.vicp.lylab.core.interfaces.Protocol;
import net.vicp.lylab.server.GlobalInitializer;
import net.vicp.lylab.server.aop.DoActionLong;
import net.vicp.lylab.utils.atomic.AtomicLong;
import net.vicp.lylab.utils.config.TreeConfig;
import net.vicp.lylab.utils.internet.impl.LYLabProtocol;
import net.vicp.lylab.utils.internet.impl.SimpleHeartBeat;
import net.vicp.lylab.utils.internet.protocol.ProtocolUtils;
import net.vicp.lylab.utils.tq.LYTaskQueue;
import net.vicp.lylab.utils.tq.LoneWolf;

public class ServerRuntime extends LoneWolf implements Closeable {
	private static final long serialVersionUID = -6694416303489621634L;
	
	public static boolean running = true;
	
	public static AtomicLong totalRequestCount = new AtomicLong(0L);
	public volatile static AtomicLong[] access = new AtomicLong[] {
		new AtomicLong(),
		new AtomicLong(),
	};
	
	protected static LYTaskQueue tq;
	protected static ServerSocket ss;
	protected static Protocol protocol = new LYLabProtocol();
	
	public static ServerRuntime serverRuntime;
	
	public static void main(String[] arg) throws Exception
	{
		CoreDef.config = new TreeConfig(CoreDef.rootPath + File.separator + "config" + File.separator + "config.txt");
//		LYTimer.setConfig(CoreDef.config.getConfig("timer"));
		ProtocolUtils.setConfig(CoreDef.config.getConfig("protocol"));
		DoActionLong.setConfig(CoreDef.config.getConfig("action"));
		GlobalInitializer.createInstance(CoreDef.config.getConfig("init"), (TreeConfig) CoreDef.config);
		
		tq = (LYTaskQueue) GlobalInitializer.get("LYTaskQueue");
		tq.useWatchDog(true);
		tq.setMaxQueue(CoreDef.config.getInteger("MaxQueue"));
		tq.setMaxThread(CoreDef.config.getInteger("MaxThread"));
		
		serverRuntime = new ServerRuntime();
		serverRuntime.begin("Server");
		
		for(int j = 0;running && j<Integer.MAX_VALUE;j+=1)
		{
			access[0].set(0L);
			access[1].set(0L);
			Thread.sleep(1*1000);
			System.out.println("second:" + j + "\t\ttotal:" + totalRequestCount.get() + "\t\taverage:" + new DecimalFormat("0.00").format(1.0*totalRequestCount.get()/(j)));
			System.out.println("L0:" + access[0].get() + "\tL1:" + access[1].get());
		}
	}

	@Override
	public void exec() {
		try {
			int port = CoreDef.config.getInteger("port");
			ss = new ServerSocket(port);
			while (running) {
				tq.addTask(new DoActionLong(ss
					, protocol.encode(new SimpleHeartBeat())));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		GlobalInitializer.destroyInstance();
	}

	@Override
	public void close() throws IOException {
		ss.close();
	}
	
}
