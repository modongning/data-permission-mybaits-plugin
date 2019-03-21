package com.x.generater.support;

import com.x.generater.core.Default;
import com.x.generater.core.IGenCollectionParser;
import com.x.generater.core.XGenCollection;
import com.x.generater.core.XValidate;
import com.x.generater.annotation.XGenField;
import com.x.generater.annotation.XGenModel;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XGenCollectionParser1 implements IGenCollectionParser {
	private Class<?> pojoClass;
	private XGenCollection collection;

	Map<String, Object> classInfo;
	Map<String, Object> tableInfo;
	List<Map<String, Object>> keys;
	List<Map<String, Object>> queryConditions;
	List<String> ascConditions;
	List<String> descConditions;
	List<Map<String, Object>> columns;
	List<Map<String, Object>> inserList;
	List<Map<String, Object>> updateList;
	
	public XGenCollectionParser1(Class<?> pojoClass, XGenCollection collection) {
		this.pojoClass = pojoClass;
		this.collection = collection;
	}
	
	@Override
	public void parse() {
		check();
		
		// 暂时用上面的数据处理静态页面
		classInfo = collection.getClassInfo();
		tableInfo = collection.getTableInfo();
		keys = collection.getKeys();
		queryConditions = collection.getQueryConditions();
		ascConditions = collection.getAscConditions();
		descConditions = collection.getDescConditions();
		columns = collection.getColumns();
		inserList = collection.getInsertList();
		updateList = collection.getUpdateList();

		parseClassInfo(classInfo);
		parseTableInfo(tableInfo);

		handlerClassFileds(this.pojoClass);
	}

	private void handlerClassFileds(Class clazz) {
		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields) {
			XGenField xf = field.getAnnotation(XGenField.class);
			if (xf != null) {
				parseKeys(field, xf, keys);
				parseKeyAutoIncrease(xf, tableInfo);
				parseQueryConditions(field, xf, queryConditions);
				parseAscConditions(field, xf, ascConditions);
				parseDescConditions(field, xf, descConditions);
				parseColumns(field, xf, columns);
				parseInsertList(field, xf, inserList);
				parseUpdateList(field, xf, updateList);
			}
		}
		if(null != clazz.getGenericSuperclass()){
			Class<?> superclass = clazz.getSuperclass();
			handlerClassFileds(superclass);
		}
	}

	/**
	 * POJO注解校验
	 */
	private void check() {
		if (!Serializable.class.isAssignableFrom(this.pojoClass)) {
			throw new RuntimeException(this.pojoClass.getSimpleName() + "需要实现Serializable接口");
		}
		if (this.pojoClass.getAnnotation(XGenModel.class) == null) {
			throw new RuntimeException(this.pojoClass.getSimpleName() + "需要使用@XGenModel注解");
		}
	}
	
	/**
	 * POJO类信息
	 * @param classInfo
	 */
	private void parseClassInfo(Map<String, Object> classInfo) {
		String className = this.pojoClass.getSimpleName();
		String packageName = this.pojoClass.getPackage().getName();
		packageName = packageName.substring(0, packageName.lastIndexOf("."));
		classInfo.put("class", this.pojoClass);
		classInfo.put("className", className);
		classInfo.put("classFullName", this.pojoClass.getName());
		classInfo.put("packageName", packageName);
		// 默认句柄名称，className首字母小写
		classInfo.put("handleName", GenerateUtil.getClassHandleName(className));
	}
	
	/**
	 * 数据库表信息
	 * @param tableInfo
	 */
	private void parseTableInfo(Map<String, Object> tableInfo) {
		XGenModel xm = this.pojoClass.getAnnotation(XGenModel.class);
		tableInfo.put("tableName", GenerateUtil.toDbName(this.pojoClass.getSimpleName()));
		//tableInfo.put("tableName", this.pojoClass.getSimpleName());
		tableInfo.put("comment", xm.comment());
		tableInfo.put("updateAble", xm.updateAble());
	}
	
	/**
	 * 数据库表主键
	 * @param field
	 * @param xf
	 * @param keys
	 */
	private void parseKeys(Field field, XGenField xf, List<Map<String, Object>> keys) {
		if (xf.key()) {
			Map<String, Object> key = new HashMap<>();
			key.put("keyColumn", GenerateUtil.toDbName(field.getName()));
			key.put("keyField", field.getName());
			Class<?> fieldType = field.getType();
			key.put("jdbcType", parseJdbcType(fieldType, xf));
			keys.add(key);
		}
	}

	/**
	 * 将POJO字段转化为JDBCType
	 * @param fieldType
	 * @param xf
	 * @return
	 */
	private String parseJdbcType(Class<?> fieldType, XGenField xf) {
		if (fieldType == int.class
			|| fieldType == long.class
			|| fieldType == float.class
			|| fieldType == double.class
			|| fieldType == short.class) {
			throw new RuntimeException("数值相关的基础数据类型，需要使用包装类。");
		}
		
		if (fieldType == String.class) {
			return xf.bigText() ? "TEXT" : "VARCHAR";
		} else if (fieldType == Integer.class ) {
			return "INTEGER";
		} else if (fieldType == Long.class) {
			return "BIGINT";
		} else if (fieldType == Date.class) {
			return "DATETIME";
		} else if (fieldType == BigDecimal.class
				|| fieldType == Float.class
				|| fieldType == Double.class) {
			return "DECIMAL";
		} else {
			throw new RuntimeException("无法转化jdbcType，暂不支持" + fieldType + "转化");
		}
	}
	
	// 没有考虑组合主键，并且其中一个键为自增长的情况，这种情况属于数据结构设计有问题
	private void parseKeyAutoIncrease(XGenField xf, Map<String, Object> tableInfo) {
		if (xf.key() && xf.autoIncrease()) {
			tableInfo.put("isKeyAutoIncrease", true);
		}
	}
	
	/**
	 * 数据库查询条件
	 * @param field
	 * @param xf
	 * @param qcs
	 */
	private void parseQueryConditions(Field field, XGenField xf, List<Map<String, Object>> qcs) {
		if (xf.query()) {
			Map<String, Object> qc = new HashMap<>();
			qc.put("queryColumn", GenerateUtil.toDbName(field.getName()));
			qc.put("queryField", field.getName());
			qc.put("comment", xf.comment());
			qc.put("type", field.getGenericType().toString());
			qcs.add(qc);
		}
	}
	
	private void parseAscConditions(Field field, XGenField xf, List<String> ascCon) {
		if (xf.ascOrder()) {
			ascCon.add(GenerateUtil.toDbName(field.getName()));
		}
	}
	
	/**
	 * INSERT的字段
	 * @param field
	 * @param xf
	 * @param insertList
	 */
	private void parseInsertList(Field field, XGenField xf, List<Map<String, Object>> insertList) {
		if (!xf.autoIncrease()) {
			Map<String, Object> insert = new HashMap<>();
			insert.put("insertField", field.getName());
			insert.put("insertColumn", GenerateUtil.toDbName(field.getName()));
			// jdbcType，如果是数值型基础数据类型，会抛出异常
			insert.put("jdbcType", parseJdbcType(field.getType(), xf));
			insert.put("type", field.getGenericType().toString());
			insertList.add(insert);
		}
	}
	
	/**
	 * UPDATE的字段
	 * @param field
	 * @param xf
	 * @param updateList
	 */
	private void parseUpdateList(Field field, XGenField xf, List<Map<String, Object>> updateList) {
		String fieldName = field.getName();
		// 写死关键字，约定必须用createTime/createBy/updateTime/updateBy
		if(!"createTime".equals(fieldName)  && !"createUserId".equals(fieldName)
		   && !"modifyTime".equals(fieldName)  && !"modifyUserId".equals(fieldName)
		   && !xf.key()) {
			Map<String, Object> update = new HashMap<>();
			update.put("updateField", field.getName());
			update.put("updateColumn", GenerateUtil.toDbName(fieldName));
			// jdbcType，如果是数值型基础数据类型，会抛出异常
			update.put("jdbcType", parseJdbcType(field.getType(), xf));
			update.put("type", field.getGenericType().toString());
			updateList.add(update);
		}
	}
	
	// DESC排序暂时使用updateTime字段，此方法生成的数据暂时没用到@2017.02.27
	private void parseDescConditions(Field field, XGenField xf, List<String> descCon) {
		if (xf.descOrder()) {
			descCon.add(GenerateUtil.toDbName(field.getName()));
		}
	}
	
	/**
	 * 数据库表的列
	 * @param field
	 * @param xf
	 * @param columns
	 */
	private void parseColumns(Field field, XGenField xf, List<Map<String, Object>> columns) {
		Map<String, Object> column = new HashMap<String, Object>();
		String fieldName = field.getName();

		int length = xf.length();

		column.put("fieldName", fieldName);
		column.put("columnName", GenerateUtil.toDbName(fieldName));
		column.put("comment", xf.comment());
		column.put("length", length);

		// 类型数据库关键字
		Class<?> fieldType = field.getType();
		// jdbcType，如果是数值型基础数据类型，会抛出异常
		column.put("jdbcType", parseJdbcType(fieldType, xf));

		String columnType = "";
		if (fieldType == String.class) {
			//默认长度为255
			if(0 == length)
				length = 255;
			columnType = xf.bigText() ? "text" : "varchar(" + length + ")";
		} else if (fieldType == Integer.class){
			columnType = "int";
		} else if (fieldType == Long.class) {
			columnType = "bigint";
		} else if (fieldType == Date.class) {
			columnType = "datetime";
		} else if (fieldType == BigDecimal.class
				|| fieldType == Float.class
				|| fieldType == Double.class){
			// 默认精度20，小数4位
			columnType = "decimal(20, 4)";
		}
		column.put("columnType", columnType);
		// 非空数据库关键字
		String notNull = xf.notNull() ? "NOT NULL" : "";

		Default def = xf.def();
		if(!def.equals(Default._DEFAULT)){
			notNull += " DEFAULT " +def;
		}

		column.put("notNull", notNull);

		// 默认值关键字
		/*String def = !StringUtils.isEmpty(xf.def()) ? "DEFAULT " + xf.def(): "";
		column.put("def", def);*/

		// 自增长数据库关键字
		String autoIncrease = xf.autoIncrease() ? "AUTO_INCREMENT" : "";
		column.put("autoIncrease", autoIncrease);
		
		// 前端页面信息，Validate的处理
		XValidate xv = field.getAnnotation(XValidate.class);
		if(xv != null) {
			String vaClassString = xv.toString();
			String vaString = vaClassString.substring(vaClassString.lastIndexOf("(") +1, vaClassString.lastIndexOf(")"));
			String[] validates = vaString.split(",");
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			for(int i = 0; i < validates.length; i++) {
				String[] map = validates[i].split("=");
				String key = map[0].toString().trim();
				String valuse = map[1].toString().trim();
				if(!valuse.equals("false") && !valuse.equals("0") && !key.equals("comment")) {
					sb.append(key);
					sb.append(": ");
					sb.append(valuse);
					sb.append((i + 1) < validates.length ? "," : "");
				}
			}
			sb.append("}");
			column.put("validate", sb.toString());
		}
		columns.add(column);
	}
	
	public XGenCollection getXGenCollection() {
		return this.collection;
	}
}
