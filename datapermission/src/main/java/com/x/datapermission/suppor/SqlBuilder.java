package com.x.datapermission.suppor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

/**
 * 基础sql拼接工具类
 *
 * @Author modongning
 * @updateBy modongning
 * @updateBy 2018/8/20 上午10:38
 */
public class SqlBuilder {

	private StringBuilder sql = new StringBuilder();

	public SqlBuilder() {
	}

	public SqlBuilder(String sql) {
		this.sql.append(sql);
	}

	/**
	 * 拼接
	 *
	 * @param str
	 * @return
	 */
	public SqlBuilder append(String str) {
		sql.append(" "+str+" ");
		return this;
	}

	/**
	 * select
	 *
	 * @param args
	 * @return
	 */
	public SqlBuilder select(String... args) {
		String keys = "*";

		if (args.length > 0)
			keys = StringUtils.join(args,",");
		sql.append(Key.SELECT).append(keys);

		return this;
	}

	/**
	 * from
	 *
	 * @return
	 */
	public SqlBuilder from() {
		sql.append(Key.FROM);
		return this;
	}

	/**
	 * 子查询
	 *
	 * @param innerSql
	 * @param alias
	 * @return
	 */
	public SqlBuilder inner(String innerSql, String alias) {
		Assert.notNull(innerSql, "子查询不能为空");

		sql.append("(").append(innerSql).append(") ").append(alias);
		return this;
	}

	/**
	 * where
	 *
	 * @return
	 */
	public SqlBuilder where() {
		sql.append(Key.WHERE);
		return this;
	}

	/**
	 * and
	 *
	 * @param key
	 * @param type
	 * @param value
	 * @return
	 */
	public SqlBuilder and(String key, String type, Object value) {
		sql.append(Key.AND).append(key).append(type).append(value);
		return this;
	}

	/**
	 * and in
	 * <p>
	 * example: and xxxx in (xxx)
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public SqlBuilder andIn(String key, Object value) {
		sql.append(Key.AND).append(key).append(Key.IN).append("(").append(value).append(")");
		return this;
	}

	/**
	 * 返回拼接好的sql
	 *
	 * @return
	 */
	public String build() {
		return sql.toString();
	}

	/**
	 * 关键字
	 */
	enum Key {
		SELECT("SELECT "),
		FROM(" FROM "),
		WHERE(" WHERE "),
		AND(" AND "),
		IN(" IN ");

		String value;

		Key(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return this.value;
		}
	}
}
