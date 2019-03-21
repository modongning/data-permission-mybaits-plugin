package com.x.generater.support;

import java.io.IOException;
import java.io.Writer;
import java.util.Properties;

import com.x.generater.core.IGenerater;
import com.x.generater.core.XGenCollection;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class MybatisMapperGenerater implements IGenerater {
	private XGenCollection collection;
	private String className; // 类名
	private String xmlTargetFilePath; // xml产生的文件路径
	private String javaTargetFilePath; // java产生的文件路径
	private static final String XML_TEMPLATE_PATH = "template/TMybatis.vm"; // xml模板文件路径
	private static final String JAVA_TEMPLATE_PATH = "template/TMapper.vm"; // java模板文件路径
	
	public MybatisMapperGenerater(XGenCollection collection) {
		this.collection = collection;
		this.className = (String)collection.getClassInfo().get("className");
		this.xmlTargetFilePath = this.getClass().getClassLoader().getResource("").getPath() + this.className + "Mapper.xml";
		this.javaTargetFilePath =this.getClass().getClassLoader().getResource("").getPath() + this.className + "Mapper.java";
	}
	
	@Override
	public void generate() {
		Properties prop = new Properties();
		String key = "file.resource.loader.class";
		String value = "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader";
		// 使用自定义流处理
		// String key = "file.resource.loader.class";
		// String value = "xframework.core.generater.support.StructuredGlobbingResourceLoader";
		prop.put(key,value);
		VelocityEngine ve = new VelocityEngine();
		ve.init(prop);
		VelocityContext context = new VelocityContext();
		context.put("collection", this.collection);
		
		generateXML(ve, context);
		generateJava(ve, context);
	}
	
	private void generateXML(VelocityEngine ve, VelocityContext context) {
		Template t = ve.getTemplate(XML_TEMPLATE_PATH, "UTF-8");
		Writer writer;
		try {
			writer = new FileWriterWithEncoding(this.xmlTargetFilePath, "UTF-8");
			t.merge(context, writer);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void generateJava(VelocityEngine ve, VelocityContext context) {
		Template t = ve.getTemplate(JAVA_TEMPLATE_PATH, "UTF-8");
		Writer writer;
		try {
			writer = new FileWriterWithEncoding(this.javaTargetFilePath, "UTF-8");
			t.merge(context, writer);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
