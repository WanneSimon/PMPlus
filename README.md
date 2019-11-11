# PMPlus

#### 介绍
  PMPlus 基于 nukkit 插件管理器的插件管理，实现插件动态启用和禁用。可以加载插件、禁用已加载插件、重新启用已禁用的插件。

    注：本插件可以加载未打包插件，所以 eclipse 可以debug了，Idea 也可以通过此插件不打包debug。
    这个插件一开始就是为了解决 eclipse 不能  debug 的问题。有关eclipse 新建工程和 debug 的方式，以后有空再更。

#### 使用方法：
     加载新插件或启用已禁用插件：
     /pm load [{plugin1},{plugin2}...]
     末尾参数可选，默认加载全部（已加载的除外）

     禁用插件：
     /pm unload [{plugin1},{plugin2}...] 
     末尾参数可选，默认禁用全部（此插件以外）
    
