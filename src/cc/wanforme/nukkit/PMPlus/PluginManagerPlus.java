package cc.wanforme.nukkit.PMPlus;

import java.util.HashMap;
import java.util.Map;


import cc.wanforme.nukkit.PMPlus.handler.command.CommandHandler;
import cc.wanforme.nukkit.PMPlus.handler.command.LoadCommand;
import cc.wanforme.nukkit.PMPlus.handler.command.UnloadCommand;
import cc.wanforme.nukkit.PMPlus.util.LoggerHelper;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

/**
 * @author wanne
 * 2019年11月6日
 * 
 */
public class PluginManagerPlus extends PluginBase{
	private volatile static PluginManagerPlus instance = null;
	
	// 自定义主配置文件
	private Config pmCfg = null;
	// 默认 lang_chs.cfg
	private Config langcfg = null;
	
	// pm 所有的命令处理器
	Map<String, CommandHandler> commands = null;
	
	public PluginManagerPlus() {
		super();
		instance = this;
	}
	public static PluginManagerPlus getInstance() {
		return instance;
	}

	
	@Override
	public void onLoad() {
		super.onLoad();
		
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		
		loadConfig();
		loadLangConfig();
		registerPMCommands();
		
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if( args==null || args.length<2) {
			return false;
		}
		
		CommandHandler handler = commands.get(label.toLowerCase() + " " + args[0]);
		if(handler != null) {
			handler.executeCommand(sender, command, label, args);
		} else {
			LoggerHelper.sendLangError(sender, "command_not_registered");
		}
		return true;
	}
	
	/** 注册 pm 的所有命令*/
	private void registerPMCommands() {
		commands = new HashMap<String, CommandHandler>(16);
		// 注册 LoadCommand 对象构建时就会加载未打包的插件（文件夹）
		commands.put("pm load", new LoadCommand("load", getServer()));
		commands.put("pm unload", new UnloadCommand("unload", getServer()));
		
	}
	
	
	/** 加载自定义的配置文件，必须首先启用。（要设置提示日志类型的颜色，启用语言文件夹）*/
	private void loadConfig() {
		// 检查、保存配置文件
		saveResource("pm.cnf", false);
		pmCfg = new Config(getDataFolder().getPath() + "/pm.cnf",  Config.CNF);
		
		LoggerHelper.setWarnColor(pmCfg.getString("info_color", "§a"));
		LoggerHelper.setWarnColor(pmCfg.getString("warn_color", "§e"));
		LoggerHelper.setErrorColor(pmCfg.getString("error_color", "§4"));
		
		String enable_prefix = pmCfg.getString("enable_prefix", "false");
		if("true,1".indexOf(enable_prefix) != -1) {
			LoggerHelper.setPrefix(pmCfg.getString("info_prefix", "[PM]："));
		}
	}
	
	/** 加载语言文件*/
	private void loadLangConfig() {
		saveResource("lang_chs.cnf", false);
		String lang_type = pmCfg.getString("lang", "chs");
		// 加载提示文件
		langcfg = new Config(getDataFolder().getPath() + "/lang_" + lang_type +".cnf", Config.CNF); // lang_chs.cnf
	}


	
	public Config getPmCfg() {
		return pmCfg;
	}
	
	public Config getLangTip() {
		return langcfg;
	}
	
}
