package net.vicp.lylab.lyserver;

import java.io.File;

import net.vicp.lylab.core.CoreDef;
import net.vicp.lylab.core.GlobalInitializer;
import net.vicp.lylab.utils.Config;

public class ServerRuntime {
	public static void main(String[] arg) {
		CoreDef.config = new Config(CoreDef.rootPath + File.separator + "config" + File.separator + "config.txt");
		GlobalInitializer.createInstance();
	}
	
	public static void close() {
		GlobalInitializer.destroyInstance();
	}
	
}
