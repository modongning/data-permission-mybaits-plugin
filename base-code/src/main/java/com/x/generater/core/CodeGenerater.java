package com.x.generater.core;

import com.x.generater.support.*;

public class CodeGenerater {
	public void generateCode(Class<?> pojoClass, GenerateConfig config, MysqlDBInfo mdb, String bPath, String fPath) {
		XGenCollection xc = new XGenCollection();
		new XGenCollectionParser1(pojoClass, xc).parse();
		if (config.genAll) {
			new MysqlDDLGenerater(xc, mdb).generate();
			new MybatisMapperGenerater(xc).generate();
			new ServiceGenerater(xc).generate();
			new ControllerGenerater(xc).generate();
			new StaticGenerater(xc).generate();
		} else {
			if (config.genTable) {
				new MysqlDDLGenerater(xc, mdb).generate();
			}
			if (config.genMapper) {
				new MybatisMapperGenerater(xc).generate();
			}
			if (config.genService) {
				new ServiceGenerater(xc).generate();
			}
			if (config.genController) {
				new ControllerGenerater(xc).generate();
			}
			if (config.genStatic) {
				new StaticGenerater(xc).generate();
			}
		}
		
		// 复制文件
		FileToProjectMover ftpm = new FileToProjectMover(pojoClass, bPath, fPath);
		ftpm.copyFiles();
		
		System.out.println("代码生成完毕...");
	}
}
