package com.otoomo.datapermission.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.otoomo.datapermission.annotation.DataPermission;
import com.otoomo.datapermission.suppor.SqlBuilder;
import com.otoomo.datapermission.vo.DataScopeVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author modongning
 * @updateBy modongning
 * @updateBy 2018/8/24 下午4:41
 */
public class DefaultDataPermissionHandler implements DataPermissionHandler {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public String handler(String alias, DataPermission.Scope[] scopeList,
						  String sql, Object parameterObject,
						  Object userDataPermissionConfig) {
		/*
		用户数据权限列表 对象结构为 DataScopeVO
		[{"dataScopeId":[1,2,3],"mapping":"company_id",{"dataScopeId":[1,2,3]}}]
		 */
		List<JSONObject> dataPermissionList = JSON.parseObject(userDataPermissionConfig.toString(), List.class);

		if (null == dataPermissionList) {
			log.info(" ==>  跳过数据权限拦截：数据权限配置为空");
			return sql;
		}

		// 检查数组列表对象及拼接函数
		Function<List<Long>, String> checkArrayAndJoinFunction = (arr) -> {
			if (null == arr && arr.size() == 0)
				return "";
			return org.apache.commons.lang3.StringUtils.join(arr,",");
		};

		//别名处理
		if (!StringUtils.isEmpty(alias)) alias = alias + ".";
		else alias = "";

		SqlBuilder sqlBuilder = new SqlBuilder(sql);
		for (int i = 0; i < dataPermissionList.size(); i++) {
			DataScopeVO dataScopeVO = JSON.parseObject(dataPermissionList.get(i).toJSONString(), DataScopeVO.class);
			Set<Long> dataScope = dataScopeVO.getDataScopeId();

			// =0 时为所有数据权限
			if (dataScope.size() == 0)
				return sql;

			//第一层: AND ( (scopeItem) OR (scopeItem) OR ...)
			//scopeItem 属于第二层
			if (i == 0) {
				sqlBuilder.append(DataPermission.JoinType.AND.getValue());
				sqlBuilder.append("(");
			} else {
				sqlBuilder.append(DataPermission.JoinType.OR.getValue());
			}

			sqlBuilder.append("(");

			int index = 0;

			/*
			第二层：
			scopeItem = ( a.company_id IN (xxx,xxx) ) AND a.team_id IN (xxx,xxx)
			 */
			for (DataPermission.Scope scope : scopeList) {
				String key = alias + scope.key();
				DataPermission.Mapping mapping = scope.mapping();
				DataPermission.JoinType joinType = scope.joinType();

				if (DataPermission.Mapping.COMPANY.equals(mapping)) {
					List<Long> companyIdList = dataScope.stream()
							.collect(Collectors.toList());
					sqlBuilder
							.append(0 < index ? joinType.getValue() : "")
							.append(key)
							.append(DataPermission.JoinType.IN.getValue())
							.append("(")
							.append(checkArrayAndJoinFunction.apply(companyIdList))
							.append(")");
				}else{
					//业务中有其他字段需要添加 DataSourceVo 字段。如team_id字段需要控制权限
					//则需要添加 getTeamIds()数据获取。
					// 后续可以通过DataSourceVo中添加configName来动态指定
				}


				index++;
			}

			sqlBuilder.append(")");
		}
		if (dataPermissionList.size() > 0)
			sqlBuilder.append(")");

		return sqlBuilder.build();
	}
}
