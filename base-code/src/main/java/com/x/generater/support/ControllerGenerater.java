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

public class ControllerGenerater implements IGenerater {
	private XGenCollection collection;
	private String className; // 类名
	private String controllerTargetFilePath;
	private static final String CONTROLLER_TEMPLATE_PATH = "template/TController.vm";
	
	public ControllerGenerater(XGenCollection collection) {
		this.collection = collection;
		this.className = (String)collection.getClassInfo().get("className");
		this.controllerTargetFilePath =this.getClass().getClassLoader().getResource("").getPath() + this.className + "Controller.java";
	}
	
	@Override
	public void generate() {
		Properties prop = new Properties();
		String key = "file.resource.loader.class";
		String value = "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader";
		// 使用自定义流处理
		// String key = "file.resource.loader.class";
		// String value = "com.x.framework.generater.support.StructuredGlobbingResourceLoader";
		prop.put(key,value);
		VelocityEngine ve = new VelocityEngine();
		ve.init(prop);
		VelocityContext context = new VelocityContext();
		context.put("collection", this.collection);
		Template t = ve.getTemplate(CONTROLLER_TEMPLATE_PATH, "UTF-8");
		Writer writer;
		try {
			writer = new FileWriterWithEncoding(this.controllerTargetFilePath, "UTF-8");
			t.merge(context, writer);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
