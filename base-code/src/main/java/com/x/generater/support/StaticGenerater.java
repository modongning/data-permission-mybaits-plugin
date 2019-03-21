package com.x.generater.support;

import com.x.generater.core.IGenerater;
import com.x.generater.core.XGenCollection;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.IOException;
import java.io.Writer;
import java.util.Properties;

public class StaticGenerater implements IGenerater {
	private XGenCollection collection;
	 private String className; // 类名
	private String handleName;// 参数名
	private String listHtmlTargetFilePath; 	 // list vue
	private String listJSTargetFilePath;     // list js
	private static final String LISTHTML_TEMPLATE_PATH = "template/static/vue_list_page.vm";
	private static final String LISTJS_TEMPLATE_PATH = "template/static/vue_list_js.vm";

	public StaticGenerater(XGenCollection collection) {
		this.collection = collection;
		this.className = (String)collection.getClassInfo().get("className");
		this.handleName = (String)collection.getClassInfo().get("handleName");
		this.listHtmlTargetFilePath = this.getClass().getClassLoader().getResource("").getPath()+ this.className + ".vue";
		this.listJSTargetFilePath = this.getClass().getClassLoader().getResource("").getPath()+ this.className + "Api.js";
	}

	@Override
	public void generate() {
		Properties prop = new Properties();
		String key = "file.resource.loader.class";
		String value = "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader";
		prop.put(key,value);
		VelocityEngine ve = new VelocityEngine();
		ve.init(prop);
		VelocityContext context = new VelocityContext();
		context.put("collection", this.collection);

		generateListHtml(ve, context);
		generateListJS(ve, context);
	}

	private void generateListHtml(VelocityEngine ve, VelocityContext context) {
		Template t = ve.getTemplate(LISTHTML_TEMPLATE_PATH, "UTF-8");
		Writer writer;
		try {
			writer = new FileWriterWithEncoding(this.listHtmlTargetFilePath, "UTF-8");
			t.merge(context, writer);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void generateListJS(VelocityEngine ve, VelocityContext context) {
		Template t = ve.getTemplate(LISTJS_TEMPLATE_PATH, "UTF-8");
		Writer writer;
		try {
			writer = new FileWriterWithEncoding(this.listJSTargetFilePath, "UTF-8");
			t.merge(context, writer);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
