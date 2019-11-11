package cc.wanforme.nukkit.PMPlus.util;

import cc.wanforme.nukkit.PMPlus.PluginManagerPlus;
import cn.nukkit.command.CommandSender;

/**
 * @author wanne
 * 2019年11月7日
 * 
 */
public class LoggerHelper {
//	private static String prefix="[PM]：";
	private static String prefix="";
	private static String info_color = "§a";
	private static String error_color = "§e";
	private static String warn_color = "§4";
	
	public static void setInfoColor(String color){
		info_color = color;
	}
	public static void setWarnColor(String color){
		warn_color = color;
	}
	public static void setErrorColor(String color){
		error_color = color;
	}
	public static void setPrefix(String prefix) {
		LoggerHelper.prefix = prefix;
	}
	
	// 以下是控制台日志
	/** 警告信息（控制台）*/
	public static void logWarning(String msg) {
		PluginManagerPlus.getInstance().getServer().getLogger().warning(warn_color + prefix + msg);
	}
	
	/** 错误信息（控制台）*/
	public static void logError(String msg) {
		PluginManagerPlus.getInstance().getServer().getLogger().error(error_color + prefix + msg);
	}
	
	/** 打印（控制台）*/
	public static void logInfo(String msg) {
		PluginManagerPlus.getInstance().getServer().getLogger().info(info_color + prefix + msg);
	}
	
	// 以下是发送消息
	/** 发送信息*/
	public static void sendInfo(CommandSender sender, String msg) {
		sendInfo(sender, msg, false);
	}

	/** 发送并（后台）打印信息
	 * @param isLog 是否要（控制台）打印信息 */
	public static void sendInfo(CommandSender sender, String msg, boolean isLog) {
		sender.sendMessage(info_color + prefix + msg);
		if(sender.isPlayer() && isLog) {
			logInfo(msg);
		}
	}
	
	public static void sendWarning(CommandSender sender, String msg, boolean isLog) {
		sender.sendMessage(warn_color + prefix + msg);
		
		if(sender.isPlayer() && isLog) {
			logInfo(msg);
		}
	}
	
	/** 发送错误信息（控制台）*/
	public static void sendError(CommandSender sender, String msg, boolean isLog) {
		sender.sendMessage(error_color + prefix + msg);
		
		if(sender.isPlayer() && isLog) {
			logError(msg);
		}
	}
	
	
	
	// 以下是从语言配置文件中取出消息
	/** 警告信息（控制台）*/
	public static void logLangWarning(String key) {
		String msg = PluginManagerPlus.getInstance().getLangTip().getString(key, key);
		PluginManagerPlus.getInstance().getServer().getLogger().warning(warn_color + prefix + msg);
	}
	
	/** 打印（控制台）*/
	public static void logLangInfo(String key) {
		String msg = PluginManagerPlus.getInstance().getLangTip().getString(key, key);
		PluginManagerPlus.getInstance().getServer().getLogger().info(info_color + prefix + msg);
	}
	/** 错误（控制台）*/
	public static void logLangError(String key) {
		String msg = PluginManagerPlus.getInstance().getLangTip().getString(key, key);
		PluginManagerPlus.getInstance().getServer().getLogger().error(error_color + prefix + msg);
	}

	
	/** 发送信息*/
	public static void sendLangInfo(CommandSender sender, String key) {
		sendLangInfo(sender, key, false);
	}
	
	/** 发送并（后台）打印信息
	 * @param isLog 是否要（控制台）打印信息 */
	public static void sendLangInfo(CommandSender sender, String key, boolean isLog) {
		String msg = PluginManagerPlus.getInstance().getLangTip().getString(key, key);
		
		sender.sendMessage(info_color + prefix + msg);
		
		if(sender.isPlayer() && isLog) {
			logInfo(msg);
		}
	}
	
	/** 发送警告*/
	public static void sendLangWarning(CommandSender sender, String key) {
		sendLangWarning(sender, key, false);
	}
	
	/** 发送并（后台）打印信息
	 * @param isLog 是否要（控制台）打印信息 */
	public static void sendLangWarning(CommandSender sender, String key, boolean isLog) {
		String msg = PluginManagerPlus.getInstance().getLangTip().getString(key, key);
		
		sender.sendMessage(warn_color + prefix + msg);
		
		if(sender.isPlayer() && isLog) {
			logWarning(msg);
		}
	}
	
	/** 发送信息，默认控制台会打印错误*/
	public static void sendLangError(CommandSender sender, String key) {
		sendLangError(sender, key, true);
	}
	
	/** 发送信息*/
	public static void sendLangError(CommandSender sender, String key, boolean isLog) {
		String msg = PluginManagerPlus.getInstance().getLangTip().getString(key, key);
		
		sender.sendMessage(error_color + prefix + msg);
		
		if(sender.isPlayer() && isLog) {
			logError(msg);
		}
	}
	
	
}
