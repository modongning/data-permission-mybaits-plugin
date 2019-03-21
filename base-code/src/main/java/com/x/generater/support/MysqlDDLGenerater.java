package com.x.generater.support;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import com.x.generater.core.IGenerater;
import com.x.generater.core.MysqlDBInfo;
import com.x.generater.core.XGenCollection;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class MysqlDDLGenerater implements IGenerater {
	private XGenCollection collection;
	private MysqlDBInfo mysqlDBInfo;
	private String tableName; // 数据库表名
	private String ddlFilePath; // DDL文件所在路径
	private static final String TEMPLATE_PATH = "template/TDDL.vm"; // 模板文件路径
	
	public MysqlDDLGenerater(XGenCollection collection, MysqlDBInfo mysqlDBInfo) {
		this.collection = collection;
		this.mysqlDBInfo = mysqlDBInfo;
		this.tableName = (String)this.collection.getTableInfo().get("tableName");
		this.ddlFilePath = this.getClass().getClassLoader().getResource("").getPath()+ this.tableName +".sql";
	}
	
	@Override
	public void generate() {
		makeDDLSql();
		createTable();
	}
	
	/**
	 * 生成DDL语句
	 */
	private void makeDDLSql() {
		Properties prop = new Properties();
		String key = "file.resource.loader.class";
		String value = "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader";
		prop.put(key,value);
		VelocityEngine ve = new VelocityEngine();
		ve.init(prop);
		VelocityContext context = new VelocityContext();
		context.put("collection", this.collection);
		Template t = ve.getTemplate(TEMPLATE_PATH, "UTF-8");
		Writer writer;
		try {
			writer = new FileWriterWithEncoding(this.ddlFilePath, "UTF-8");
			t.merge(context, writer);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 连接数据库执行DDL语句
	 */
	private void createTable() {
		try {
			Connection conn = null;
			// 加载数据库驱动
			Class.forName(this.mysqlDBInfo.getDriver());
			String url = this.mysqlDBInfo.getConnUrl();
			String userName = this.mysqlDBInfo.getUserName();
			String password = this.mysqlDBInfo.getPassword();
			conn = (Connection) DriverManager.getConnection(url, userName, password);
			Statement statement = conn.createStatement();
			// 检查数据库中是否已经存在该表
			StringBuffer checkTableSql = new StringBuffer();
			checkTableSql.append("select TABLE_NAME from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA='");
			checkTableSql.append(this.mysqlDBInfo.getDbName());
			checkTableSql.append("' and TABLE_NAME='");
			checkTableSql.append(this.tableName);
			checkTableSql.append("'");
			ResultSet result = statement.executeQuery(checkTableSql.toString());
			if (result.next()) {
				System.err.println("表已存在: " + this.tableName);
			} else {
				// 读取通过模板生成的SQL文件
				String ddl = FileUtils.readFileToString(new File(this.ddlFilePath), "UTF-8");
				statement.execute(ddl);
				System.out.println("表部署成功：" + this.tableName);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
