package com.x.base;


import com.x.utils.data.Page;

import java.util.List;

/**
 * dao操作接口，用于继承
 *
 * @param <M>
 * @param <T>
 */
public interface BaseMapper<M, T> {


	/**
	 * 根据ID查询
	 */
	M findById(T t);

	/**
	 * 列表查询
	 */
	List<M> query(M m);

	/**
	 * 分页查询
	 */
	<V> List<V> pageQuery(Page<?,V> page);

	/**
	 * 数量统计
	 */
	<S> int count(S s);

	/**
	 * 新增
	 */
	Long save(M m);

	/**
	 * 批量新增
	 */
	int saveBatch(List<M> list);

	/**
	 * 按主键更新全部字段
	 */
	int update(M m);

	/**
	 * 按主键删除
	 */
	int deleteById(T t);

}
