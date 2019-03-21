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

public class ServiceGenerater implements IGenerater {
	private XGenCollection collection;
	private String className; // 类名
	private String serviceTargetFilePath;
	private String serviceImplTargetFilePath;
	private static final String SERVICE_TEMPLATE_PATH = "template/TService.vm";
	private static final String SERVICEIMPL_TEMPLATE_PATH = "template/TServiceImpl.vm";
	
	public ServiceGenerater(XGenCollection collection) {
		this.collection = collection;
		this.className = (String)collection.getClassInfo().get("className");
		this.serviceTargetFilePath = this.getClass().getClassLoader().getResource("").getPath()+ this.className + "Service.java";
		this.serviceImplTargetFilePath = this.getClass().getClassLoader().getResource("").getPath()+ this.className + "ServiceImpl.java";
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
		
		generateService(ve, context);
		generateServiceImpl(ve, context);
	}
	
	private void generateService(VelocityEngine ve, VelocityContext context) {
		Template t = ve.getTemplate(SERVICE_TEMPLATE_PATH, "UTF-8");
		Writer writer;
		try {
			writer = new FileWriterWithEncoding(this.serviceTargetFilePath, "UTF-8");
			t.merge(context, writer);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void generateServiceImpl(VelocityEngine ve, VelocityContext context) {
		Template t = ve.getTemplate(SERVICEIMPL_TEMPLATE_PATH, "UTF-8");
		Writer writer;
		try {
			writer = new FileWriterWithEncoding(this.serviceImplTargetFilePath, "UTF-8");
			t.merge(context, writer);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
