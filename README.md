# PMPlus

#### 介绍
  PMPlus 基于 nukkit 插件管理器的插件管理，实现插件动态启用和禁用。可以加载插件、禁用已加载插件、重新启用已禁用的插件。

    注：本插件可以加载未打包插件，所以 eclipse 可以debug了，Idea 也可以通过此插件不打包debug。
    这个插件一开始就是为了解决 eclipse 不能  debug 的问题。有关eclipse 新建工程和 debug 的方式，以后有空再更。
	
  同步更新：<a href="https://github.com/WanneSimon/PMPlus"> github </a> <a href="https://gitee.com/wanneme/PMPlus/tree/master"> gitee </a>
#### 使用方法：
    当成普通插件，扔到 plugins 文件夹下。
    eclispe 普通工程为例： 
    1. 新建 eclipse 普通 java 项目 - HelloDemo，在工程下新建一个普通文件夹。右键项目 > New > Folder ，Folder Name 填写 "nukkit/plugins/HelloDemo"。 
	2. 添加 nukkit 依赖。右键项目 > Build Path > Configure Build Path，在 Libraries 一栏，选择 add External JARS... ，选择你的 nukkit核心，加进来。 
	3. 修改编译文件输出位置。eclipse 默认输出路径位于当前项目 bin目录下。还是在 第二步 Java Build Path 这里，选择 Source 一栏，最下面 Default output folder，修改成第一步创建的 "nukkit/plugins/HelloDemo"，点击 Apply and Close 保存并关闭。 
	4. 设置项目启动配置。右键工程 > Run As > Run Configurations，双击左侧 Java Applicatoin 新建配置， 
		顶部 Name 填 HelloDemo；Project 不用管，如果这里是空的，请选择为当前项目；Main class ，点击 Search， 会出现 "Nukkit - cn.nukkit"，双击选择。点击Apply保存就可以关闭了。 
		注：这里一定要把 nukkit 核心加入依赖，不然找不到。 
	5. 检查 Debug配置。右键工程 > Debug As > Debug Configurations，看看有没有我们刚刚在第4步新建的配置。 
	6. 加入PMPlus插件。把本插件 PMPlus.jar 扔到 我们一开始新建的文件夹 "nukkit/plugins/HelloDemo" 的 "nukkit/plugins/"里面。 
	7. 启动或debug。如果你写好了插件，准备测试，右键项目，Run As > Java Application 或者 Debug As > Java Application ，选择主程序 "Nukkit - cn.nukkit" 
	
	以上是写给对 eclipse 不是很熟的小伙伴。Idea 我不怎么熟，也懒得写了，可以看看粉鞋大妈的教程，在配置启动时，不用管 "Lanch Before" 。 
 <a href="https://www.cnblogs.com/xtypr/p/nukkit_plugin_start_from_0_build_environment.html">Idea 新建项目</a> 

#### 指令：
     加载新插件或启用已禁用插件：
     /pm load [{plugin1},{plugin2}...]
     末尾参数可选，默认加载全部（已加载的除外）

     禁用插件：
     /pm unload [{plugin1},{plugin2}...] 
     末尾参数可选，默认禁用全部（此插件以外）
    
