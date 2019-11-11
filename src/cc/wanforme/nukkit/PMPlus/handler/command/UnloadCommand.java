package cc.wanforme.nukkit.PMPlus.handler.command;

import java.util.Map;

import cc.wanforme.nukkit.PMPlus.PluginManagerPlus;
import cc.wanforme.nukkit.PMPlus.util.LoggerHelper;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginManager;

/**
 * @author wanne
 * 2019年11月8日<br>
 * /pm unload [{plugin1},{plugin2}...] <br> 末尾参数可选，默认禁用全部（除了自己以外）<br><br>
 *  插件的加载步骤：<br>
 *  	通过 加载器加载后，保存到所有已加载的插件集合中，保存此插件的所有（命令）权限，所有命令保存到命令集合中。<br>
 *  	而命令里面不仅包含主命令，还有所有命令别名  和 nukkit 自己定义规则为每个插件命名的命令，nukkit并没有提供清除插件命令的方法，想要完全移除很难。<br><br>
 *  关于卸载插件：<br>
 *  	禁用插件，关闭定时任务 和 注册的所有监听器（处理器），移除所有插件（命令）权限。<br> 
 *  	但是，并没从 PluginManager 中移除此插件 ，也没有 从命令集合中将此插件的命令移除。<br>
 *  	加上定时任务和监听器另外启动了线程，保留了插件的信息，想要完全卸载插件，几乎不可能，只能禁用掉，所以 nukkit api 只能禁用插件，不能卸载插件。<br>
 *  	<br><br>
 *  关于定时任务：<br>
 *  	定时任务在插件启用后才能注册（和实际情况好像有点不一样），<br>
 *  	新生成一个 任务处理器，存放到任务池中（结构是Map，但键是新生成，可以理解为自增，所以不会发生覆盖）。<br>
 *  	由此也可以看出，插件一旦禁用掉，根本没有办法正常重启。<br>
 *  	定时任务的添加：{@link cn.nukkit.scheduler.ServerScheduler#addTask(Plugin, Runnable, int, int, boolean)}<br><br>
 *  PS：仅禁用插件可以防止 插件的依赖丢失！！！
 */
public class UnloadCommand extends CommandHandler{
	/*

	 */
	
	public UnloadCommand(String type, Server server) {
		super(type, server);
	}

	@Override
	protected boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length==1 ) {
			unloadAll(sender);
		}else if (args.length==2) {
			String[] pluginNames = args[1].split(",");
			PluginManager pluginManager = PluginManagerPlus.getInstance().getServer().getPluginManager();
			StringBuffer exceptionPlugin = new StringBuffer("");
			for (String name : pluginNames) {
				Plugin plugin = pluginManager.getPlugin(name);
				if(plugin != null) {
					try {
//						pluginManager.disablePlugin(plugin);
						this.unloadPluginCoreMethod(pluginManager, plugin);
					} catch (Exception e) {
						e.printStackTrace();
						exceptionPlugin.append("["+name+"] ");
					}
				}
			}
			
			if(exceptionPlugin.toString().equals("")) {
				LoggerHelper.sendWarning(sender, exceptionPlugin.toString(), true);
			}
			LoggerHelper.sendLangInfo(sender, "unload_finish");
		}else {
			return false;
		}
		
		return true;
	}

	/** 卸载所有（如果有插件依赖，可能会出问题）*/
	protected void unloadAll(CommandSender sender) {
		PluginManager pluginManager = PluginManagerPlus.getInstance().getServer().getPluginManager();
		 Map<String, Plugin> plugins = pluginManager.getPlugins();
		StringBuffer exceptionPlugins = new StringBuffer("");
		UnloadCommand context = this;
		plugins.forEach((k, plugin) -> {
			try {
				if(!plugin.getName().equals(PluginManagerPlus.getInstance().getName())) {
//					pluginManager.disablePlugin(plugin);
//					this.unloadPluginCoreMethod(pluginManager, plugin);
					context.unloadPluginCoreMethod(pluginManager, plugin);
				}
			} catch (Exception e) {
				e.printStackTrace();
				exceptionPlugins.append("["+plugin.getName()+"] ");
			}
		});
		
		if(exceptionPlugins.toString().equals("")) {
			LoggerHelper.sendWarning(sender, exceptionPlugins.toString(), true);
		}
		LoggerHelper.sendLangInfo(sender, "unload_finish");
	}
	
	protected void unloadPluginCoreMethod(PluginManager pluginManager, Plugin plugin) {
//		HandlerList.getRegisteredListeners(plugin); // 可以获取到插件所有注册的监听器
		
		pluginManager.disablePlugin(plugin);
	}
	
	@Override
	protected String getUsage() {
		return "/pm unload [{plugin1},{plugin2}...]";
	}

	
}
