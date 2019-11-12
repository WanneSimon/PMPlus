package cc.wanforme.nukkit.PMPlus.handler.command;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cc.wanforme.nukkit.PMPlus.PluginManagerPlus;
import cc.wanforme.nukkit.PMPlus.loader.PMPluginLoader;
import cc.wanforme.nukkit.PMPlus.loader.UnpackedPluginLoader;
import cc.wanforme.nukkit.PMPlus.util.LoggerHelper;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginLoader;
import cn.nukkit.plugin.PluginManager;

/** 
 * 加载插件 处理器 <br>
 * command: /pm load [{plugin1},{plugin2}...] <br>
 * 末尾参数可选，默认加载全部（已加载的除外）
 * （废除）command: /pm load {plugin_name} [soft|force] <br>
 * （废除）末尾 soft和force 是可选参数（不填时，默认soft）。soft-软加载，依赖的插件没有加载时会加载失败。force-强制加载，忽略依赖进行加载，可能会异常。
 * @author wanne
 * 2019年11月7日
 * 
 */
public class LoadCommand extends CommandHandler{
	public static final String UNPACKED_LOADER = "unpackedLoader";
	
	// 未打包类型加载器
	protected Map<String, PluginLoader> pluginLoaders ;
	
	public LoadCommand(String type, Server server) {
		super(type,server);
		pluginLoaders = new HashMap<String, PluginLoader>(16);
		
		pluginLoaders.put(UNPACKED_LOADER, new UnpackedPluginLoader(server));
		
		// 加载所有文件夹下面插件
		loadAllUnpackedPlugins();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// 检查是不是只有两个参数，不指定插件名，加载所有插件
		if(args.length == 1) {
			loadAllUnpackedPlugins();
		} else if(args.length == 2) {
			// 检查命令是否匹配
			String type = args[0];
			if(!type.equals(getType())) {
				return false;
			}
			
			// 检查加载方式
			// to do ...
			
			String[] pluginNames = args[1].split(",");
			StringBuffer fail = new StringBuffer("");
			StringBuffer success = new StringBuffer("");
			
			for (String name : pluginNames) {
				Plugin plugin = this.loadPluginCoreMethod(name, pluginLoaders);
//				Plugin plugin = server.getPluginManager().loadPlugin(pluginDir, pluginLoaders);
				if( plugin == null) {
					// 交给 nukkit 自己去加载
					plugin = PluginManagerPlus.getInstance().getServer().getPluginManager().loadPlugin(
							PluginManagerPlus.getInstance().getServer().getPluginPath() + "/" + name + ".jar");
					if(plugin == null) {
						fail.append("[" + name + "] ");
					} else {
						success.append("[" + name + "] ");
					}
//					continue;
//					LoggerHelper.sendLangError( sender, "plguin_load_fail");
//					return false;
				} else {
					success.append("[" + name + "] ");
				}
			}
			
//			LoggerHelper.sendLangInfo( sender, "plguin_load_success");
			LoggerHelper.sendLangInfo(sender, "loaded_success");
			LoggerHelper.sendInfo(sender, success.toString(), true);
			LoggerHelper.sendLangWarning(sender, "loaded_fail");
			LoggerHelper.sendWarning(sender, fail.toString(), true);
		} else {
			return false;
		}
		
		return true;
	}

	/** 加载  plugins 文件夹下的所有插件*/
	public void loadAllUnpackedPlugins() {
		LoggerHelper.logLangInfo("load_start");
		
		String pluginPath = server.getPluginPath();
		File pluginParent = new File(pluginPath);
		
		File[] files = pluginParent.listFiles();
		StringBuffer info = new StringBuffer("");
		for (File file : files) {
			if(file.isDirectory()) {
				try {
					Plugin plugin = this.loadPluginCoreMethod(file, pluginLoaders);
					if( plugin != null) {
						info.append( "[" + file.getName() + "--" + plugin.getName() + "_v" + plugin.getDescription().getVersion() + "] ");
//						LoggerHelper.logLangInfo("plguin_load_success");
					}
				} catch (Exception e) {
//					e.printStackTrace();
					continue;
				}
			}
		}
		
		// 插件启用过程中会输出一些信息，使用分割线分割一下
		LoggerHelper.logLangInfo("hr_small");
		LoggerHelper.logLangInfo("loaded_success");
		LoggerHelper.logInfo(info.toString());
//		LoggerHelper.logLangInfo("plguin_load_success");
		LoggerHelper.logLangInfo("load_finish");
	}

	/**加载插件的关键方法，需要重写
	 * @param file
	 * @param loaders
	 * @return
	 */
	protected Plugin loadPluginCoreMethod(String name, Map<String, PluginLoader> loaders) {
		PluginManager pluginManager = server.getPluginManager();
		Plugin plugin = pluginManager.getPlugin(name);
		
		// 检查插件是否已经加载过，然后被 禁用了
		if(!pluginManager.isPluginEnabled(plugin)) { // 该方法内部进行了判空和其它检查
			pluginManager.enablePlugin(plugin);
		} else if(plugin == null){ // 插件没有加载过，属于新插件
//			File pluginDir = new File(server.getPluginPath() + "/" + name);
//			plugin =  pluginManager.loadPlugin(pluginDir, loaders);
			plugin = this.loadPluginCoreMethod(new File(server.getPluginPath() + "/" + name), loaders);
		} else { // 未知情况，加载失败
			plugin = null;
		}
		return plugin;
	}

	/**加载插件的关键方法，需要重写
	 * @param file
	 * @param loaders
	 * @return
	 */
	protected Plugin loadPluginCoreMethod(File pluginDir, Map<String, PluginLoader> loaders) {
		return server.getPluginManager().loadPlugin(pluginDir, loaders);
	}

	
	@Override
	protected String getUsage() {
		return "/pm load [{plugin1},{plugin2}...]";
	}
}
