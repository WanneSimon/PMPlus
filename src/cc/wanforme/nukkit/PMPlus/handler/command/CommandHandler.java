package cc.wanforme.nukkit.PMPlus.handler.command;


import cc.wanforme.nukkit.PMPlus.util.LoggerHelper;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

/**
 * @author wanne
 * 2019年11月7日
 * 
 */
public abstract class CommandHandler {

	/** 命令类型，会用来检查是否是处理器要处理的命令，和第一个参数保持一致 */
	private String type;
	protected Server server;
	
	public CommandHandler(String type, Server server) {
			this.type = type;
			this.server = server;
	}
	
	/** 对参数进行 检查后，调用 处理方法*/
	public boolean executeCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args== null || !getType().equals(args[0])) {
			return false;
		}
		if ( !onCommand(sender, command, label, args)) {
			LoggerHelper.sendInfo(sender, getUsage(), true);
		}
		return true;
	}
	
	/** 子类需要重写的处理方法，如果执行失败，会调用 #getUsage() 提示用法
	 * @param label 主命令
	 * @param args 参数
	 */
	protected abstract boolean onCommand(CommandSender sender, Command command, String label, String[] args) ;
	
	/** 子命令使用方法，如果 onCommand执行失败，就会通过此方法获取使用提示*/
	protected abstract String getUsage() ;
	
	public String getType() {
		return type;
	}
	
}
