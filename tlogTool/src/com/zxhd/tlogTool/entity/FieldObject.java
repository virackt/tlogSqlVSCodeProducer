package com.zxhd.tlogTool.entity;

public class FieldObject {
	// SQL类型
	public String sqlType;
	// java类型
	private String javaType;
	// 属性名
	public String name;
	// set方法
	public String setMethod;
	// get方法
	public String getMethod;
	// comment
	public String comment;

	
	public String getSqlType() {
		return sqlType;
	}
	public void setSqlType(String sqlType) {
		this.sqlType = sqlType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSetMethod() {
		return setMethod;
	}
	public void setSetMethod(String setMethod) {
		this.setMethod = setMethod;
	}
	public String getGetMethod() {
		return getMethod;
	}
	public void setGetMethod(String getMethod) {
		this.getMethod = getMethod;
	}
	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}
	public String getJavaType(){
		return this.javaType;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public FieldObject(String type, String name, String comment) {
		super();
		this.sqlType = type;
		changeToJavaType();
		this.name = name;
		this.comment = comment;
		this.setMethod = "public void set" + name.substring(0,1).toUpperCase() + name.substring(1) + "(" + javaType + " " + name + "){";
		this.getMethod = "public " + javaType + " get" + name.substring(0, 1).toUpperCase() + name.substring(1) + "(){";
	}
	
	private void changeToJavaType(){
		if(sqlType.contains("int")){
			this.javaType = "int";
		} else if(sqlType.contains("varchar")){
			this.javaType = "String";
		} else if (sqlType.contains("time")){
			this.javaType = "String";
		}
	}
	
}
