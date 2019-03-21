package com.x.generater.core;

public class GenerateConfig {
	public boolean genAll = false;
	public boolean genTable = false;
	public boolean genMapper = false;
	public boolean genService = false;
	public boolean genController = false;
	public boolean genStatic = false;

	public void genAll() {
		this.genAll = true;
	}

	public void genTable() {
		this.genTable = true;
	}

	public void genMapper() {
		this.genMapper = true;
	}

	public void genService() {
		this.genService = true;
	}

	public void genController() {
		this.genController = true;
	}

	public void genStatic() {
		this.genStatic = true;
	}
}
