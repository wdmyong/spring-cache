# spring-cache
配置spring-cache作数据层缓存

### 初次从0开始配置，确实踩了不少坑，以前都是各种包一次性加满
### 下面大致记录下过程
1. ContextLoaderListener的理解不深入，没加导致通过DispatcherServlet的
init-param配置的spring-servlet.xml中能够注入bean，但是context-param
配置没法生效；
2. 数据库相关的包少了：spring-tx、spring-jdbc，最SB的是mysql-connector-java；
3. 对于mvc:annotation-driven的理解DefaultAnnotationHandlerMapping、
AnnotationMethodHandlerAdapter的自动创建；
