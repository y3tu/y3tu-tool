#### 各个模块的作用
<!-- GFM-TOC -->
* [System](#System)
* [Setting](#Setting)
<!-- GFM-TOC -->

# System
System模块主要获取系统、JVM、内存、CPU等信息，以便动态监测系统状态。
`SystemUtil.dumpSystemInfo()`打印Java运行环境的系统状态信息

# Setting 
Setting模块主要针对Properties文件读写做封装，同时定义一套自己的配置文件规范，实现兼容性良好的配置工具。