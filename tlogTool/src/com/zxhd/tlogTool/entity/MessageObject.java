package com.zxhd.tlogTool.entity;

import java.util.List;

public class MessageObject {
	// 包名
	public String pkgName;
	// 表明
	public String tableName;
	// 类注释
	public String classComment;
	// 类名
	public String className;
	// 父类名称
	public String fatherName;
	// 协议标记
	private int type;
	private List<FieldObject> fieldList;
	// 构造函数参数
	private String fields;
	// 初始化传参
	private String instanceStr;
	// 建表语句
	private String createSql;
	// 更新语句
	private String updateSql;
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public List<FieldObject> getFieldList() {
		return fieldList;
	}
	public void setFieldList(List<FieldObject> fieldList) {
		this.fieldList = fieldList;
		StringBuffer fieldSb = new StringBuffer();
		StringBuffer instanceSb = new StringBuffer();
		StringBuffer createSqlSb = new StringBuffer("iEventId bigint(20) comment '事件id', ");
		int size = fieldList.size();
		for(FieldObject obj : fieldList){
			size--;
			fieldSb.append(obj.getJavaType());
			fieldSb.append(" ");
			fieldSb.append(obj.getName());
			instanceSb.append(obj.getName());
			createSqlSb.append(obj.getName());
			createSqlSb.append(" ");
			createSqlSb.append(obj.getSqlType());
			createSqlSb.append(" comment'");
			createSqlSb.append(obj.comment);
			createSqlSb.append("'");
			if(size != 0){
				fieldSb.append(",");
				instanceSb.append(",");
			}
			createSqlSb.append(",");
		}
		if(createSqlSb.toString().contains("iUserId")){
			createSqlSb.append("primary key(iUserId, serverId, iEventId)");
		} else if(createSqlSb.toString().contains("vUUid")){
			createSqlSb.append("primary key(vUUid, serverId, iEventId)");
		}else if(createSqlSb.toString().contains("serverId")){
			createSqlSb.append("primary key(serverId, iEventId)");
		} else {
			createSqlSb.append("primary key(dActionTime, iEventId)");
		} 
		this.fields = fieldSb.toString();
		this.instanceStr = instanceSb.toString();
		this.createSql = createSqlSb.toString(); 
		createUpdateSql(fieldList);
	}
	public String getPkgName() {
		return pkgName;
	}
	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
	}
	public String getFatherName() {
		return fatherName;
	}
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}
	public String getFields() {
		return fields;
	}
	public void setFields(String fields) {
		this.fields = fields;
	}
	public String getInstanceStr() {
		return instanceStr;
	}
	public void setInstanceStr(String instanceStr) {
		this.instanceStr = instanceStr;
	}
	public String getCreateSql() {
		return createSql;
	}
	public void setCreateSql(String createSql) {
		this.createSql = createSql;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	private void createUpdateSql(List<FieldObject> fieldList){
		StringBuffer updateSqlSb = new StringBuffer();
		int size = fieldList.size();
		for(FieldObject obj : fieldList){
			size--;
			if(obj.getJavaType().equals("String")){
				updateSqlSb.append("\"'\" + ");
				updateSqlSb.append(obj.getName());
				updateSqlSb.append("+ \"'\"");
				
			} else {
				updateSqlSb.append(obj.getName());
			}
			if(size != 0){
				updateSqlSb.append("+\",\"+");
			}
		}
		this.updateSql = updateSqlSb.toString();
	}
	public String getUpdateSql() {
		return updateSql;
	}
	public void setUpdateSql(String updateSql) {
		this.updateSql = updateSql;
	}
	public String getClassComment() {
		return classComment;
	}
	public void setClassComment(String classComment) {
		this.classComment = classComment;
	}
	
}
