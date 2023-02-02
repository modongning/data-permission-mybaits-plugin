# 项目介绍

## 数据权限控制：

- 目前控制的数据默认的字段是：company_id
  com.otoomo.datapermission.annotation.DataPermission.Mapping
  
  数据权限的字段，是可以通过@DataPermission的scope自定的。具体可以看使用案例
  com.otoomo.datapermission.annotation.DataPermission.scope


# 使用说明

## 添加项目依赖

    ```
    <dependency>
        <groupId>com.otoomo</groupId>
        <artifactId>data-permission-mybaits-plugin</artifactId>
        <version>1.0.0</version>
    </dependency>
    ```

## 在 Application 启动类配置扫描 xframework-datapermission 包

```
@ComponentScan(value={"com.otoomo.datapermission"})
public class SysManageApplication {
    ...
}
```

## 在需要数据权限控制的Mapper类添加注解

```
@DataPermission(
    alias = "a",
    scope = {
        @DataPermission.Scope(
            key = "belong_Value", 
            mapping = DataPermission.Mapping.PARTNER
        ),
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
 
> 假设缓存中的数据权限结构如下：

    ```
    数据权限的数据在用户登录的时候已经确认并缓存到了缓存中，具体业务查看：com.x.bmp.sysmanage.service.UserDataPermissionService.queryUserDataPermissionByUser
    
     [{
         "dataScopeId":12081960692228096,
         "subPartnerIdList": [
             12081342544093184,
             12081342544093184
         ],
         "teamIdList": [
             12081342544093184,
             12081342544093184
         ]
     },{
         "dataScopeId":12081342544093184,
         "subPartnerIdList": [],
         "teamIdList": []
     }]
    ```

    对应的生成的数据权限控制查询SQL:
    
    ```
     SELECT 
        a.partner_id partnerId,
        a.sub_partner_id subPartnerId,
        a.team_id teamId
    FROM policy a
    WHERE a.state = 'UW'
    AND 1=1
    -- 这里就是插件动态拼接的语句
    AND
     (
     (a.partner_id = 12081342544093184 AND a.sub_partner_id IN (12081342544093184,12081342544093184) AND a.team_id IN (12081342544093184,12081342544093184))
     OR
     (a.partner_id = 12081960692228096)
     )
    ```
    
---

# 核心类说明

## 数据权限注解类： @DataPermission

1. 属性说明：

- `alias`
    
    (默认) sql别名，不能为空，如果没有别名，则传入 ""
    
    ```
    alias = "a"
    ```
    
- `scope`   
    
    (默认) 数据权限拦截数据配置列表,默认值为 ： DataPermission.Mapping.PARTNER
    
    ```
    scope = {
        @DataPermission.Scope(key = "belong_value", mapping = DataPermission.Mapping.PARTNER)
    }
    ```
    
    - `@DataPermission.Scope`
    
        数据权限控制具体配置信息
        
        - `key`
         
            sql中数据控制字段的别名
        
        - `mapping`
        
            指定key对应控制的数据是哪一个权限数据
          
    
- `filter`
 
    需要数据权限控制的方法拦截配置
    
    ```
    filter = {
            @DataPermission.FilterMethod(name = "query"),        // 使用默认 alias, 使用默认 scope 
            @DataPermission.FilterMethod(name = "pageQuery"),    // 使用默认 alias, 使用默认 scope
            @DataPermission.FilterMethod(
                    name = "count",
                    scope = {
                            @DataPermission.Scope(key = "belong_value", mapping = DataPermission.Mapping.PARTNER),
                    }
            )
    }
    ```
    
    - `@DataPermission.FilterMethod`
        
        配置需要数据拦截的方法
        
        - `alias`
        
            数据权限拦截方法中对应sql的别名,如果没有配置`默认使用外层的 alias`
        
        - `name`
            
            方法名称
        
        - `scope` 
            
             方法对应的数据权限拦截数据配置列表，如果没有配置 `默认使用 外层的 scope`
       
       ```
        例1：select a.belong_value belongValue from user a 则： name = "belong_value"
       ```
   - `scope`
      
      key属性中对应的值对应的数据权限,为 DataPermission.Scope 中的对应数据
      
            scope = DataPermission.Scope.PARTNER
        
         
 - 完整示例：
 
    ```
    @DataPermission(
            alias = "a",
            scope = {
                    @DataPermission.Scope(key = "belong_value", mapping = DataPermission.Mapping.PARTNER),
            },
            filter = {
                    @DataPermission.FilterMethod(name = "query"),
                    @DataPermission.FilterMethod(alias = "p",name = "pageQuery"),
                    @DataPermission.FilterMethod(name = "count")
            }
    )
    ```
     
## 数据权限拦截器接口 ： DataPermissionInterceptor

 - 数据权限拦截器的实现方式是Mybatis的拦截器的基础上实现的

 - 已实现并且默认使用的数据权限拦截器： `DefaultDataPermissionInterceptor`
 
 - 如果有特殊的数据权限业务，可自定义拦截器，实现`DataPermissionInterceptor`接口 ： 
 
    - 1.自定义数据权限拦截器：
    
        ```
        @Intercepts({
                //拦截StatementHandler类的prepare方法，args为prepare方法的参数
                @Signature(method = "prepare", type = StatementHandler.class, args = {Connection.class, Integer.class})
        })
        public class MyDataPermissionInterceptor implements DataPermissionInterceptor {
        
            @Override
            public Object intercept(Invocation invocation) throws Throwable {
                //TODO 数据权限控制业务逻辑
            }
        
        }
        ```
    - 2.配置自定义拦截器生效
        ```
        @Bean
        public DataPermissionInterceptor dataPermissionInterceptor() {
            return new MyDataPermissionInterceptor();
        }
        ```
--- 


    