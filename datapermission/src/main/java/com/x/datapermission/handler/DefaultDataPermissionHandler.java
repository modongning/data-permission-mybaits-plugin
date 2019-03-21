package com.x.datapermission.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.sf.json.JSONArray;
import org.apache.commons.collections.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import com.x.datapermission.annotation.DataPermission;
import com.x.datapermission.suppor.SqlBuilder;
import com.x.datapermission.vo.DataScopeVO;

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
	public String handler(String alias, DataPermission.Scope[] scopeList, String sql, Object parameterObject, Object userDataPermissionConfig) {
		/*
		用户数据权限列表 对象结构为 DataScopeVO
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
			return JSONArray.fromObject(arr).join(",");
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

			if (i == 0) {
				sqlBuilder.append(DataPermission.JoinType.AND.getValue());
				sqlBuilder.append("(");
			} else {
				sqlBuilder.append(DataPermission.JoinType.OR.getValue());
			}

			sqlBuilder.append("(");

			int index = 0;

			for (DataPermission.Scope scope : scopeList) {
				String key = alias + scope.key();
				DataPermission.Mapping mapping = scope.mapping();
				DataPermission.JoinType joinType = scope.joinType();

				if (DataPermission.Mapping.COMPANY.equals(mapping)) {
					List<Long> companyIdList = dataScope.stream().collect(Collectors.toList());
					sqlBuilder
							.append(0 < index ? joinType.getValue() : "")
							.append(key)
							.append(DataPermission.JoinType.IN.getValue())
							.append("(")
							.append(checkArrayAndJoinFunction.apply(companyIdList))
							.append(")");
				}else{

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
