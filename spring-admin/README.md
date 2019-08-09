Actuator 可以用于监控 SpringBoot 应用，并查看应用内部的状态，可以通过 HTTP 或 JMX 方式。

## 默认的 Endpoint

默认的 path 前缀为`actuator`，可以通过`management.endpoints.web.base-path`属性配置。

通过`/actuator`可以查看所有的 endpoints:

```shell
$ http :8080/actuator
```

大部分的 endpoint 默认是禁用的，可以通过属性`management.endpoints.web.exposure.include`和`management.endpoints.web.exposure.exclude`进行配置。
可以使用通配符`’*‘`表示所有：

```text
management:
  endpoints:
    web:
      base-path: /actuator
      exposure:
        include: '*'
        exclude: heapdump,threaddump
```

### /info

为`/info`提供数据有多种方式，第一种是静态的，在配置文件中定义以`info`为前缀的属性。

另一种是动态的，即实现`InfoContributor`接口，实现`contribute()`方法即可，代码参考`InfoContributor.java`。

另外，Actuator 提供了一些预先定义的`InfoContributor`实现，比如`BuildInfoContributor`可以返回项目的 build 信息，如果要启用，需要在 maven/gradle 中添加 build-info 任务，以 gradle 为例：

```gradle
springBoot {
  buildInfo()
}
```

gradle 会在最后生成的 jar 包中生成`build-info.properties`文件，`/info`会读取该文件中的内容。

如果希望在`/info`中看到最新的 Git commit 信息，在 gradle 中添加 plugin：

```gradle
plugins {
    id "com.gorylenko.gradle-git-properties" version "2.0.0"
}
```

默认输出的是简要信息，可以通过配置`management.info.git.mode=full`输出更详细的 commit 信息。

### /health

`/health`主要显示当前系统以及外部依赖组件（如 database, MQ）的健康状态，包括：`UP`, `DOWN`, `UNKNOWN`, `OUT-OF-SERVICE`等，这是聚合状态，即所有组件是`UP`，最后的状态才是`UP`，只要有一个组件的状态是`DOWN`，最后的状态是`DOWN`。

`management.endpoint.health.show-details`：显示更详细的信息。

也可以实现`HealthIndicator`接口，扩展`/health`，代码示例见`MyHealthIndicator.java`。

### /loggers

`/loggers`用于查看基于 package 的日志配置信息。

`/loggers/{package-name}`：查看某个 package 的日志配置。

`POST /loggers/{package-name}`：用于修改 package 的日志配置，如：

```shell
$ http post :8080/actuator/loggers/org.nkcoder.admin configuredLevel=DEBUG
```

### /env

`/env`查看所有属性信息，包括环境变量、JVM 系统属性、Spring Cloud Config 属性，以及`application.yml`和`application.properties`中的属性。

`/env/{property-name}`：可以根据属性名查询属性值。

`POST /env`：可以用于设置属性。
`DELETE /env`：可以用于删除属性。

不过这些修改操作都是临时的，系统重启后会恢复原值。

### /metrics

返回包括内存、GC、请求等指标的值或统计信息。

`/metrics`：返回可以度量的指标的列表。
`/metrics/{metric-name}`：返回具体指标的详细信息。

### 其它 endpoint

`/conditions`：查看`AutoConfiguration`下 bean 的加载信息，比如`@ConditionOnMissingBean`， `@ConditionOnClass`等。

`/mappings`：查看 Spring MVC 中 REST 的映射信息。

`/httptrace`: 查看系统最近 100 个请求的处理情况，包括何时处理的、耗时、header 信息等。

`/threaddump`：返回当前所有线程活动的快照信息。

`/heapdump`：会下载一个`HPROF`的 heap dump 文件。

## 自定义 Endpoint

因为需要同时支持 HTTP 和 JMX MBeans，Actuator 使用的并不是`@Controller`或`@RestController`，而是`@Endpoint`，对应的操作注解为：
`@ReadOperation, @WriteOperation, @DeleteOperation`。

所以，定义一个 Endpoint，与定义一个 Controller 类似，只是替换成对应的注解即可，示例代码参考`MyEndPoint.java`。

`@JmxEndpoint`只是用于 JMX MBean，而`@WebEndpoint`只是用于 HTTP 请求。

## 安全控制

Actuator 并不关心安全问题，所以安全问题可以交给 Spring Security 来控制。

Actuator 的请求路径在`/actuator`下（可配置），所以完全可以当作一般的 REST 节点来配置安全。

为了可以不用依赖固定的`/actuator`前缀，Actuator 还提供了`EndpointRequest`配置，如：

```java
http.requestMatcher(EndpointRequest.toAnyEndpoint().excluding("health", "info"))
  .authorizeRequests().anyRequest().hasRole("ADMIN")
  .and().httpBasic();
```
