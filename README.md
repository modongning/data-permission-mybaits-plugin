# basesuport

项目基础支持：通用工具类；代码生成器；请求统一拦截；统一响应拦截等；基于ibatis实现数据权限控制

- base-core

    通用工具类；代码生成器；请求统一拦截；统一响应拦截等

- dataopermission

    基于ibatis实现数据权限控制

# 使用Dataopermission

## 添加项目依赖

    ```
    <dependency>
        <groupId>com.x</groupId>
        <artifactId>datapermission</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </dependency>
    ```

## 在 Application 启动类配置扫描 xframework-datapermission 包

```
@ComponentScan(value={"com.x.datapermission"})
public class SysManageApplication {
    ...
}
```
    
## 在需要数据权限控制的Mapper类添加注解
    
```
@DataPermission(
    alias = "a",
    scope = {
        @DataPermission.Scope(key = "belong_Value", mapping = DataPermission.Mapping.PARTNER),
    },
    filter = {
        @DataPermission.FilterMethod(name = "query"),
        @DataPermission.FilterMethod(name = "pageQuery"),
        @DataPermission.FilterMethod(name = "count")
    }
)
@Mapper
public abstract class UserMapper {
    
    User getByKey(Map<String, Object> keyMap);

    List<User> query(Map<String, Object> queryMap);

    List<User> pageQuery(Map<String, Object> pageQueryMap);
    
    int count(Map<String, Object> couuserntMap);

}
```
