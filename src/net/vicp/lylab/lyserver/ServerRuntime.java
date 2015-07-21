package net.vicp.lylab.lyserver;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.text.DecimalFormat;

import net.vicp.lylab.core.CoreDef;
import net.vicp.lylab.core.GlobalInitializer;
import net.vicp.lylab.core.model.SimpleHeartBeat;
import net.vicp.lylab.server.aop.Aop;
import net.vicp.lylab.utils.Config;
import net.vicp.lylab.utils.atomic.AtomicLong;
import net.vicp.lylab.utils.internet.ToClientLongSocket;
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

	protected static ServerSocket ss;
	protected static LYTaskQueue tq;
	
	public static ServerRuntime serverRuntime;
	
	public static void main(String[] arg) throws Exception
	{
		CoreDef.config = new Config(CoreDef.rootPath + File.separator + "config" + File.separator + "config.txt");
		Aop.setConfig(CoreDef.config.getConfig("action"));
		GlobalInitializer.createInstance(CoreDef.config.getConfig("init"), CoreDef.config);
		
		tq = (LYTaskQueue) GlobalInitializer.get("LYTaskQueue");
		tq.setMaxQueue(CoreDef.config.getInteger("MaxQueue"));
		tq.setMaxThread(CoreDef.config.getInteger("MaxThread"));
		
		serverRuntime = new ServerRuntime();
		serverRuntime.begin("Server");

		//重算hub
		Integer recalcTimeInteger = 0;
		boolean recalc = true;

		for(int j = 0;j<Integer.MAX_VALUE;j+=1)
		{
			totalRequestCount.getAndAdd(access[0].getAndSet(0L));
			totalRequestCount.getAndAdd(access[1].getAndSet(0L));
			Thread.sleep(1*1000);
			if(recalc && j > 8)
			{
				recalcTimeInteger = j;
				recalc = false;
				totalRequestCount.set(0L);
				System.out.println("recalc");
			}
			System.out.println("second:" + j + "\t\ttotal:" + totalRequestCount.get() + "\t\taverage:" + new DecimalFormat("0.00").format(1.0*totalRequestCount.get()/(j-recalcTimeInteger)));
			System.out.println("L0:" + access[0].get() + "\tL1:" + access[1].get());
		}
	}

	@Override
	public void exec() {
		try {
			int port = CoreDef.config.getInteger("port");
			ss = new ServerSocket(port);
			while (running) {
				tq.addTask(new ToClientLongSocket(ss
					, new SimpleHeartBeat()));
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
