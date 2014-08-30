package com.zxhd.tlogTool.entity;

import java.util.List;

public class LogserviceObject {
	private List<String> impPath;
	private List<MessageObject> moList;
	public List<String> getImpPath() {
		return impPath;
	}
	public void setImpPath(List<String> impPath) {
		this.impPath = impPath;
	}
	public List<MessageObject> getMoList() {
		return moList;
	}
	public void setMoList(List<MessageObject> moList) {
		this.moList = moList;
	}
}
