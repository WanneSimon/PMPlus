package cc.wanforme.nukkit.PMPlus.loader;

import java.io.File;
import java.io.FileInputStream;
import java.util.regex.Pattern;

import cn.nukkit.Server;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginClassLoader;
import cn.nukkit.plugin.PluginDescription;
import cn.nukkit.utils.PluginException;
import cn.nukkit.utils.Utils;

/** 未打包插件加载器，代码更改自 {@link cn.nukkit.plugin.JavaPluginLoader}<br>
 * 加载 未打包（字节码文件 ）形式的插件
 * @author wanne
 * 2019年11月6日
 * 
 */
public class UnpackedPluginLoader extends PMPluginLoader{

	
	public UnpackedPluginLoader(Server server) {
		super(server);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Plugin loadPlugin(File file) throws Exception {
		PluginDescription description = this.getPluginDescription(file);
		if(description == null) {
			return null;
		}
		
		// 直接使用插件的位置作为数据存放位置
		File dataFolder = new File(file.getParentFile(), description.getName());
        if (dataFolder.exists() && !dataFolder.isDirectory()) {
            throw new IllegalStateException("Projected dataFolder '" + dataFolder.toString() + "' for " + description.getName() + " exists and is not a directory");
        }
        

        String className = description.getMain();
        PluginClassLoader classLoader = new PluginClassLoader(this, this.getClass().getClassLoader(), file);
        this.getClassLoaders().put(description.getName(), classLoader);
        PluginBase plugin = null;
        try {
            Class javaClass = classLoader.loadClass(className);

            if (!PluginBase.class.isAssignableFrom(javaClass)) {
                throw new PluginException("Main class `" + description.getMain() + "' does not extend PluginBase");
            }

            try {
                Class<PluginBase> pluginClass = (Class<PluginBase>) javaClass.asSubclass(PluginBase.class);

                plugin = pluginClass.newInstance();
                this.initPlugin(plugin, description, dataFolder, file);
            } catch (ClassCastException e) {
                throw new PluginException("Error whilst initializing main class `" + description.getMain() + "'", e);
            } catch (InstantiationException | IllegalAccessException e) {
                Server.getInstance().getLogger().logException(e);
            }
            
            return plugin;
        } catch (ClassNotFoundException e) {
            throw new PluginException("Couldn't load plugin " + description.getName() + ": main class not found");
        }
//		return super.loadPlugin(file);
	}
	
	/** 读取文件下的插件配置信息，传入插件根路径，其下要有 nukkit.yml 或 plugin.yml 配置信息文件<br>
	 * 能否能获取到该对象是识别插件的主要依据
	 * @param baseDir 根路径
	 */
	@Override
	public PluginDescription getPluginDescription(File baseDir) {
		if(!baseDir.isDirectory()) {
			return null;
		}
		
//		String[] fs = baseDir.list(new FilenameFilter() {
//			@Override
//			public boolean accept(File dir, String name) {
//				if("nukkit.yml".equals(name) || "plugin.yml".equals(name)) {
//					return true;
//				}
//				return false;
//			}
//		});
//		if(fs==null || fs.length==0) {
//			return null;
//		}
//		// 根据字符串排序，nukkit.yml会在plugin.yml前面
//		try (FileInputStream fis = new FileInputStream(new File(fs[0]))){
//			return new PluginDescription(Utils.readFile(fis));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;

		File file = new File(baseDir.getPath() + "/nukkit.yml");
		if( !file.exists() ) {
			file = new File(baseDir.getPath() + "/plugin.yml");
		}
		if( !file.exists() ) {
			return null;
		}
		
		try (FileInputStream fis = new FileInputStream(file)){
			return new PluginDescription(Utils.readFile(fis));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	/** 仅字母开头并数字或字母结束*/
	@Override
	public Pattern[] getPluginFilters() {
		return new Pattern[] {Pattern.compile("^[a-zA-Z]+[a-z0-9A-Z]*$")};
	}
	
}
