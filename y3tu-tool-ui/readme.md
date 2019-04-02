###使用
```
 <dependency>
    <groupId>com.y3tu</groupId>
    <artifactId>y3tu-tool-ui</artifactId>
    <version>2.0</version>
    <optional>true</optional>
 </dependency>
```

在spring boot中的Application中使用`@EnableToolUI`注解启用工具界面
启动程序后，浏览器输入http://127.0.0.1:8080(*ip端口*)/xxx(*contentPath*)/y3tu-tool-ui/访问界面
默认路径为y3tu-tool-ui，可以通过配置修改路径。

```
//配置路径
y3tu.tool.ui.url-pattern='/tool-ui/*' 
//配置登录用户名密码 如果不配置则不校验用户名密码
y3tu.tool.ui.login-username='admin'
y3tu.tool.ui.login-password='123456'
//配置白名单ip和黑名单ip
y3tu.tool.ui.allow="127.0.0.1"
y3tu.tool.ui.deny="127.0.0.2"
```

### 打包执行方式：
* windows环境 : mvn clean package -P window
* Linux环境 ：mvn clean package -P linux   
通过此命令可以把vue项目编译打包进jar包中