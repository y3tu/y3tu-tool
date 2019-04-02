<!-- GFM-TOC -->
### 模块
* [Core](#Core)
* [System](#System)
* [Setting](#Setting)
* [Aop](#Aop)
* [BloomFilter](#BloomFilter)
* [Captcha](#Captcha)
* [Cron](#Cron)
* [Crypto](#Crypto)
* [Db](#Db)
* [FileSystem](#FileSystem)
* [Http](#Http)
* [Script](#Script)
* [Dfa](#Dfa)
* [Extra](#Extra)

<!-- GFM-TOC -->

# Core
核心方法及数据结构包
* ClipboardUtil 系统剪贴板工具类
* RobotUtil 截屏工具
* ScreenUtil 屏幕相关设置
* ThreadUtil 线程池工具
* RandomUtil 随机工具类
* ReflectUtil 反射工具类
* RuntimeUtil 系统运行时工具类，用于执行系统命令的工具
* StrUtil 字符串工具类
* ZipUtil 压缩工具类
* JsonUtil Json工具类
* ArrayUtil 数组工具类
<br/>...


# System
System模块主要获取系统、JVM、内存、CPU等信息，以便动态监测系统状态。
`SystemUtil.dumpSystemInfo()`打印Java运行环境的系统状态信息

# Setting 
Setting模块主要针对Properties文件读写做封装，同时定义一套自己的配置文件规范，实现兼容性良好的配置工具。

# Aop
JDK动态代理封装，提供非IOC下的切面支持

# BloomFilter
布隆过滤，提供一些Hash算法的布隆过滤

# Captcha
图形验证码的实现

# Cron
定时任务模块，提供类Crontab表达式的定时任务，实现参考了Cron4j，同时可以支持秒级别的定时任务定义和年的定义（同时兼容Crontab、Cron4j、Quartz表达式）

# Crypto
加密解密模块，实现了对JDK中加密解密算法的封装，入口为SecureUtil，实现了：
 1. 对称加密（symmetric），例如：AES、DES等
 2. 非对称加密（asymmetric），例如：RSA、DSA等
 3. 摘要加密（digest），例如：MD5、SHA-1、SHA-256、HMAC等
 
# Db
数据库操作工具类

# FileSystem
云端文件操作，对象包含腾讯云、阿里云、七牛云和fastdfs文件服务器。

# Http
基于okHttp3的网络工具

# Script 
Script模块主要针对Java的javax.script封装，可以运行Javascript脚本。

# Dfa
DFA全称为：Deterministic Finite Automaton,即确定有穷自动机。<br>
解释起来原理其实也不难，就是用所有关键字构造一棵树，然后用正文遍历这棵树，遍历到叶子节点即表示文章中存在这个关键字。<br>
我们暂且忽略构建关键词树的时间，每次查找正文只需要O(n)复杂度就可以搞定。<br>

# Extra
其他额外的工具集合
* emoji 
* ftp 
* mail
* qrcode
* servlet
* ssh
* template

