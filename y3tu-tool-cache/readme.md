####Cache模块包含
> 1.常见缓存实现(FIFO,LFU,LRU...)   
> 2.基于redis的多级缓存实现   
> 3.对redis操作的封装 

* maven引入
```
  <dependency>
    <groupId>com.y3tu</groupId>
    <artifactId>y3tu-tool-cache</artifactId>
    <version>2.0</version>
  </dependency>
  
  <!--因为缓存依赖于spring切面，所以需要引入spring aop-->
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-aop</artifactId>
  </dependency>
  
  <!--基于redis的操作和多级缓存引入(如果仅使用一级缓存则不需要引入)-->
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-redis</artifactId>
  </dependency>
  
```
* 配置
```
y3tu:
  tool:
    cache:
      layer:
        #开启缓存统计
        stats: true
      #如果不使用spring boot的redis配置方式，也可以使用下面自定义的redis配置
      redis:
        #开启自定义redis配置
        enable: true
        #Matser的ip地址
        hostName: 119.23.205.255
        #端口号
        port: 6379
        #如果有密码
        password: ****
        #客户端超时时间单位是毫秒 默认是2000
        timeout: 10000
        #最大空闲数
        maxIdle: 300
        #连接池的最大数据库连接数。设为0表示无限制,如果是jedis 2.4以后用maxTotal
        #maxActive: 600
        #控制一个pool可分配多少个jedis实例,用来替换上面的maxActive,如果是jedis 2.4以后用该属性
        maxTotal: 1000
        #最大建立连接等待时间。如果超过此时间将接到异常。设为-1表示无限制。
        maxWaitMillis: 1000
        #连接的最小空闲时间 默认1800000毫秒(30分钟)
        minEvictableIdleTimeMillis: 300000
        #每次释放连接的最大数目,默认3
        numTestsPerEvictionRun: 1024
        #逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
        timeBetweenEvictionRunsMillis: 30000
        #是否在从池中取出连接前进行检验,如果检验失败,则从池中去除连接并尝试取出另一个
        testOnBorrow: true
        #在空闲时检查有效性, 默认false
        testWhileIdle: true
        #redis集群配置
        spring.cluster.nodes: 119.23.205.255:6379
        spring.cluster.max-redirects: 2

```

#### redis的使用
1.在`Application`类上加入注解`@EnableToolRedis`   
2.使用`RedisRepository`中定义的方法操作redis
  
#### 缓存的使用


##### 使用
 1.在`Application`类上加入注解`@EnableToolCache`   
 2.在需要缓存的方法上加上注解
   * `@Cacheable`:优先从缓存中获取数据，未获取就执行被缓存方法，然后把数据放入缓存内
   * `@CacheEvict`:删除缓存
   * `@CachePut`:插入数据到缓存
 ```
   @Override
   @Cacheable(value = "people", key = "#person.id", depict = "用户信息缓存",cacheMode = CacheMode.ALL
           firstCache = @FirstCache(expireTime = 4),
           secondaryCache = @SecondaryCache(expireTime = 15, preloadTime = 8, forceRefresh = true))
   public Person findOne(Person person) {
       Person p = new Person(2L, "name2", 12,"address2");
       logger.info("为id、key为:" + p.getId() + "数据做了缓存");
       return p;
   } 
   
 ```
 3.说明：
 * 默认的缓存模式`CacheMode.ALL`，同时使用一、二级缓存。可以仅使用一级缓存`CacheMode.ONLY_FIRST`或者仅使用二级redis缓存`CacheMode.ONLY_SECOND`。
 * 只有当注解中的`value`,`firstCache`,`secondaryCache`完全一样时，生成的缓存内部key才一样(同一个缓存),否则就是两个不同的缓存，获取的值也不同。



