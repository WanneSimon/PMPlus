package cc.wanforme.nukkit.PMPlus.loader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import cn.nukkit.Server;
import cn.nukkit.event.plugin.PluginEnableEvent;
import cn.nukkit.plugin.JavaPluginLoader;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginClassLoader;
import cn.nukkit.plugin.PluginDescription;

/**
 * 由于 JavaPluginLoader 类内部属性和部分方法私有化，只好直接搬过来。
 * @author wanne
 * 2019年11月7日
 * 
 */
public abstract class PMPluginLoader extends JavaPluginLoader{

    private final Server server;

    @SuppressWarnings("rawtypes")
	private final Map<String, Class> classes = new HashMap<>();
    private final Map<String, PluginClassLoader> classLoaders = new HashMap<>();
	
	public PMPluginLoader(Server server) {
		super(server);
		this.server = server;
	}
	
    @Override
    public Plugin loadPlugin(String filename) throws Exception {
        return this.loadPlugin(new File(filename));
    }
    
    /** 加载插件的实现*/
	@Override
	public abstract Plugin loadPlugin(File file) throws Exception ;
	
	/**  加载插件配置信息
	 * @param baseDir 根路径
	 */
	@Override
	public abstract PluginDescription getPluginDescription(File baseDir) ;
	
	/** 插件文件名过滤规则*/
	@Override
	public abstract Pattern[] getPluginFilters() ;
	
	protected void initPlugin(PluginBase plugin, PluginDescription description, File dataFolder, File file) {
        plugin.init(this, this.server, description, dataFolder, file);
        plugin.onLoad();
    }
	
    @Override
    public void enablePlugin(Plugin plugin) {
    	super.enablePlugin(plugin);
    }
	
	protected Server getServer() {
		return server;
	}
	
	@SuppressWarnings("rawtypes")
	protected Map<String, Class> getClasses() {
		return classes;
	}
	
	protected Map<String, PluginClassLoader> getClassLoaders() {
		return classLoaders;
	}
	
}
