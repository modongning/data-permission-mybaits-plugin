package com.x.generater.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XGenCollection {
	// 类信息
	private Map<String, Object> classInfo;
	// 数据库表信息
	private Map<String, Object> tableInfo;
	// 数据库表主键信息
	private List<Map<String, Object>> keys;
	// 查询条件
	private List<Map<String, Object>> queryConditions;
	// ASC排序条件
	private List<String> ascConditions;
	// DESC排序条件
	private List<String> descConditions;
	// 数据库表的列
	private List<Map<String, Object>> columns;
	// 用于INSERT的列
	private List<Map<String, Object>> insertList;
	// 用于UPDATE的列
	private List<Map<String, Object>> updateList;
	// 页面查询条件
	private List<Map<String, Object>> pageQueryConditions;
	// 页面表单信息
	private List<Map<String, Object>> pageFormFields;
	
	public XGenCollection() {
		classInfo = new HashMap<>();
		tableInfo = new HashMap<>();
		columns = new ArrayList<>();
		keys = new ArrayList<>();
		queryConditions = new ArrayList<>();
		ascConditions = new ArrayList<>();
		descConditions = new ArrayList<>();
		insertList = new ArrayList<>();
		updateList = new ArrayList<>();
		pageQueryConditions = new ArrayList<>();
		pageFormFields = new ArrayList<>();
	}

	public List<Map<String, Object>> getInsertList() {
		return insertList;
	}

	public void setInsertList(List<Map<String, Object>> insertList) {
		this.insertList = insertList;
	}

	public List<Map<String, Object>> getUpdateList() {
		return updateList;
	}

	public void setUpdateList(List<Map<String, Object>> updateList) {
		this.updateList = updateList;
	}

	public Map<String, Object> getClassInfo() {
		return classInfo;
	}

	public void setClassInfo(Map<String, Object> classInfo) {
		this.classInfo = classInfo;
	}

	public Map<String, Object> getTableInfo() {
		return tableInfo;
	}

	public void setTableInfo(Map<String, Object> tableInfo) {
		this.tableInfo = tableInfo;
	}

	public List<Map<String, Object>> getColumns() {
		return columns;
	}

	public void setColumns(List<Map<String, Object>> columns) {
		this.columns = columns;
	}

	public List<Map<String, Object>> getKeys() {
		return keys;
	}

	public void setKeys(List<Map<String, Object>> keys) {
		this.keys = keys;
	}

	public List<Map<String, Object>> getQueryConditions() {
		return queryConditions;
	}

	public void setQueryConditions(List<Map<String, Object>> queryConditions) {
		this.queryConditions = queryConditions;
	}

	public List<String> getAscConditions() {
		return ascConditions;
	}

	public void setAscConditions(List<String> ascConditions) {
		this.ascConditions = ascConditions;
	}

	public List<String> getDescConditions() {
		return descConditions;
	}

	public void setDescConditions(List<String> descConditions) {
		this.descConditions = descConditions;
	}

	public List<Map<String, Object>> getPageQueryConditions() {
		return pageQueryConditions;
	}

	public void setPageQueryConditions(List<Map<String, Object>> pageQueryConditions) {
		this.pageQueryConditions = pageQueryConditions;
	}

	public List<Map<String, Object>> getPageFormFields() {
		return pageFormFields;
	}

	public void setPageFormFields(List<Map<String, Object>> pageFormFields) {
		this.pageFormFields = pageFormFields;
	}
}
