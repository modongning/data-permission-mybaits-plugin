package com.otoomo.datapermission.interceptor;

import com.otoomo.datapermission.base.Global;
import com.otoomo.datapermission.handler.DataPermissionHandler;
import com.otoomo.datapermission.suppor.SqlBuilder;
import com.otoomo.datapermission.utils.DataPermissionUtils;
import com.otoomo.datapermission.utils.SpringContextUtils;
import com.otoomo.datapermission.utils.ReflectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.otoomo.datapermission.annotation.DataPermission;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.*;

/**
 * Mybatis拦截器
 * 通过判断 DataPermission 注解实现数据权限控制
 *
 * @Author modongning
 * @updateBy modongning
 * @updateBy 2018/8/16 下午2:51
 */
@Intercepts({
		//拦截StatementHandler类的prepare方法，args为prepare方法的参数
		@Signature(method = "prepare", type = StatementHandler.class, args = {Connection.class, Integer.class})
})
public class DefaultDataPermissionInterceptor implements DataPermissionInterceptor {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public Object intercept(Invocation invocation) throws InvocationTargetException, IllegalAccessException {
		String defaultAlias = "";
		try {
			DataPermission.Scope[] defaultScope = null;
			DataPermission.FilterMethod[] methodFilter = null;
			Object userDataPermissionConfig = null;
			Object parameterObject = null;
			DataPermissionHandler dataPermissionHandler = null;

			long startTime = System.currentTimeMillis();
			if (invocation.getTarget() instanceof RoutingStatementHandler) {

				/*
				检查缓存是否有用户数据权限配置数据
				TODO 这里是通过session获取权限配置的，可以更换其他的方式获取，只要能拿到就可以了
				 */
				ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
				if (null == requestAttributes || null == requestAttributes.getRequest()) {
					return invocation.proceed();
				}
				HttpServletRequest request = requestAttributes.getRequest();
				HttpSession loginSession = request.getSession();
				if (null == loginSession) {
					return invocation.proceed();
				}
				userDataPermissionConfig = loginSession.getAttribute(Global.DATA_PERMISSION_KEY);
				if (null == userDataPermissionConfig) {
					return invocation.proceed();
				}

				RoutingStatementHandler handler = (RoutingStatementHandler) invocation.getTarget();
				//通过反射获取属性
				StatementHandler delegate = (StatementHandler) ReflectUtils.getFieldValue(handler, "delegate");
				MappedStatement mappedStatement = (MappedStatement) ReflectUtils.getFieldValue(delegate, "mappedStatement");
				BoundSql boundSql = delegate.getBoundSql();

				parameterObject = boundSql.getParameterObject();

				//获取Mapper类的数据权限注解
				DataPermission dataPermission = DataPermissionUtils.getClassPermission(mappedStatement);

				if (null == dataPermission)
					//不用数据权限控制
					return invocation.proceed();

				String dataPermissionHandlerName = dataPermission.dataPermissionHandler();
				defaultAlias = dataPermission.alias();
				defaultScope = dataPermission.scope();
				methodFilter = dataPermission.filter();

				dataPermissionHandler = SpringContextUtils.getBean(dataPermissionHandlerName, DataPermissionHandler.class);

				if (methodFilter.length == 0) {
					//没有需要拦截的方法
					return invocation.proceed();
				}

				String methodName = DataPermissionUtils.getMethodName(mappedStatement);

				log.info("==>  开始处理数据权限控制流程:{}", methodName);

				final boolean[] filter = {false};
				final String[] dataScopeSql = {boundSql.getSql()};

				String finalDefaultAlias = defaultAlias;
				DataPermission.Scope[] finalDefaultScope = defaultScope;
				Object finalParameterObject = parameterObject;
				DataPermissionHandler finalDataPermissionHandler = dataPermissionHandler;
				Object finalUserDataPermissionConfig = userDataPermissionConfig;

				Arrays.stream(methodFilter)
						.filter(filterMethod -> methodName.equals(filterMethod.name()))
						.forEach(filterMethod -> {
							filter[0] = true;
							String alias = filterMethod.alias();
							DataPermission.Scope[] scope = filterMethod.scope();

							if (StringUtils.isEmpty(alias))
								alias = finalDefaultAlias;
							if (scope.length == 0)
								scope = finalDefaultScope;

							//查询语句的数据权限处理
							dataScopeSql[0] = handlerQueryDataPermission(
									finalDataPermissionHandler,
									alias, scope, dataScopeSql[0],
									finalUserDataPermissionConfig,
									finalParameterObject);
						});

				if (!filter[0])
					log.info(" ==>  跳过数据权限拦截：没有配置数据权限拦截");


			}

			// 将执行权交给下一个拦截器
			Object proceed = invocation.proceed();

			long endTime = System.currentTimeMillis();
			log.info("数据权限拦截处理耗时：{} ms", (endTime - startTime));

			return proceed;
		} catch (Exception e) {
			StringBuffer info = new StringBuffer();
			info.append(e.getMessage()).append("\r\n");

			StackTraceElement[] stackTraceArr = e.getStackTrace();
			for (StackTraceElement stackTrace : stackTraceArr) {
				info.append(stackTrace.toString()).append("\r\n");
			}

			log.info("数据权限处理异常：" + info.toString());
			throw e;
		}
	}

	/**
	 * 创建统计数据权限sql
	 *
	 * @param alias
	 * @param scopeList
	 * @param sql
	 * @return java.lang.String
	 * @author modongning
	 * @updateBy modongning
	 * @updateDate 2018/8/18 上午11:40
	 */
	private String handlerQueryDataPermission(DataPermissionHandler dataPermissionHandler, String alias, DataPermission.Scope[] scopeList, String sql, Object userDataPermissionConfig, Object parameterObject) {
		SqlBuilder sqlBuilder = new SqlBuilder();

		/*
		过滤WHERE后面的其他关键字,拼接数据权限SQL语句后再添加回去
		 */
		boolean needHandler = false;
		String handlerKey = "";
		String afterSql = "";
		if (sql.toUpperCase().contains("GROUP BY")) {
			needHandler = true;
			handlerKey = "GROUP BY";
		} else if (sql.toUpperCase().contains("HAVING")) {
			needHandler = true;
			handlerKey = "HAVING";
		} else if (sql.toUpperCase().contains("ORDER BY")) {
			needHandler = true;
			handlerKey = "ORDER BY";
		} else if (sql.toUpperCase().contains("LIMIT")) {
			needHandler = true;
			handlerKey = "LIMIT";
		}

		if (needHandler) {
			//截取WHERE后面其他关键字的字符
			afterSql = sql.substring(sql.lastIndexOf(handlerKey));
			sql = sql.substring(0, sql.lastIndexOf(handlerKey));
		}

		sqlBuilder.append(sql);

		if (!sql.toUpperCase().contains("WHERE"))
			sqlBuilder.where().append("1=1");

		sql = dataPermissionHandler.handler(alias, scopeList, sqlBuilder.build(), parameterObject, userDataPermissionConfig);

		if (!StringUtils.isEmpty(afterSql))
			// 拼接上WHERE后面过滤掉的sql
			sql += " " + afterSql;

		return sql;
	}
}

