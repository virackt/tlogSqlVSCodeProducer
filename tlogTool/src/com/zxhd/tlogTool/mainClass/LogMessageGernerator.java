package com.zxhd.tlogTool.mainClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import com.zxhd.tlogTool.entity.FieldObject;
import com.zxhd.tlogTool.entity.LogserviceObject;
import com.zxhd.tlogTool.entity.MessageObject;
import com.zxhd.tlogTool.util.AppConfig;

public class LogMessageGernerator {
	// 协议输出路径
	private static String msgOutputPath = "D:/workspace/tlog1.0/src/";
	// 逻辑类输出路径
	private static String logServicePath = "D:/workspace/tlog1.0/src/com/zxhd/log/service/LogService.java";
	// 映射配置文件输出路径
	private static String mapConfPath = "D:/workspace/tlog1.0/src/mapConf.ini";
	// 建表语句配置文件
	private static String createSqlPath = "D:/workspace/tlog1.0/src/createSql.ini";
	// excel路径
	private static String EXCEL_PATH = "D:/config/tlog.xlsx";
	// package名
	private static final String packageName = "com.zxhd.log.entity";
	// 父类名称
	private static final String fatherName = "ILogCodec";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AppConfig.init();
		msgOutputPath = AppConfig.getString("msgOutputPath");
		logServicePath = AppConfig.getString("logServicePath");
		mapConfPath = AppConfig.getString("mapConfPath");
		createSqlPath = AppConfig.getString("createSqlPath");
		EXCEL_PATH = AppConfig.getString("EXCEL_PATH");
		
		List<MessageObject> moList = new ArrayList<MessageObject>();
		List<String> impPath =  new ArrayList<String>();
		LogserviceObject lo = new LogserviceObject();
		lo.setImpPath(impPath);
		lo.setMoList(moList);
		getExcelInfo(moList, impPath);
		for(MessageObject mo : moList){
			System.out.println(mo.getCreateSql());
			velocityMessage(mo, msgOutputPath + mo.getPkgName().replace(".", "/") + "/"+ mo.getClassName() + ".java");
		}
		velocityLogService(lo, logServicePath);
		velocityMessageConst(lo, mapConfPath);
		velocityCreateSql(lo, createSqlPath);
		
	}
	
	
	public static void getExcelInfo(List<MessageObject> moList, List<String> impPath){
		XSSFWorkbook hssfWB;
		try {
			hssfWB = new XSSFWorkbook(new FileInputStream(new File(EXCEL_PATH)));
			XSSFSheet sheet = hssfWB.getSheetAt(0);
			Iterator<Row> it = sheet.rowIterator();
			MessageObject mo = null;
			List<FieldObject> fieldList = null;
			int type = 10000;
			int rowNum = 0;
			while(it.hasNext()){
				XSSFRow row = (XSSFRow) it.next();
				if(row == null){
					break;
				}
				rowNum++;
				Cell cell = row.getCell(0);
				if(cell != null){
					if(cell.getStringCellValue().startsWith("表名")){
						fieldList = new ArrayList<FieldObject>();
						mo = new MessageObject();
						moList.add(mo);
						cell = row.getCell(1);
						String logName = cell.getStringCellValue();
						mo.setTableName(logName);
						mo.setClassName(logNameToClassName(logName));
						cell = row.getCell(2);
						mo.setClassComment(cell.getStringCellValue());
						mo.setPkgName(packageName);
						impPath.add(packageName + "." + mo.getClassName());
						mo.setFatherName(fatherName);
						mo.setType(type++);
					} else {
						String name = cell.getStringCellValue();
						if(StringUtils.isEmpty(name)){
							continue;
						}
						cell = row.getCell(1);
						String fieldType = cell.getStringCellValue();
						cell = row.getCell(2);
						String comment = cell.getStringCellValue();
						try {
							FieldObject fo = new FieldObject(fieldType, name, comment);
							fieldList.add(fo);
							mo.setFieldList(fieldList);
						} catch (Exception e) {
							e.printStackTrace();
							System.out.println(rowNum);
						}
						
					}
				} else {
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("ok");
		
		
	}
	
	private static String logNameToClassName(String logName){
		return logName.substring(0, 1).toUpperCase() + logName.substring(1) + "Message";
	}
	
	
	/**
	 * 生成日志的各种协议
	 * @param mo
	 * @param outpath
	 */
	public static void velocityMessage(MessageObject mo, String outpath){
		 //获取模板引擎
       VelocityEngine ve = new VelocityEngine();
       //模板文件所在的路径
       String path = "./conf/";
       //设置参数
       ve.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, path);
       //处理中文问题
       ve.setProperty(Velocity.INPUT_ENCODING,"UTF-8");
       ve.setProperty(Velocity.OUTPUT_ENCODING,"UTF-8");
       try 
       {
           //初始化模板
           ve.init();
           //获取模板(hello.html)
           //Velocity模板的名称
           Template template = ve.getTemplate("message.vm");
           //获取上下文
           VelocityContext root = new VelocityContext();
           //把数据填入上下文
           root.internalPut("msg", mo);
           //输出
           Writer mywriter = new PrintWriter(new FileOutputStream(new File(outpath))); 
           System.out.println(outpath);
           template.merge(root, mywriter);
           mywriter.flush();           
       } 
       catch (Exception e) 
       {
           e.printStackTrace();
       }
	}

	
	/**
	 * 生成logservice类
	 * @param obj
	 * @param outpath
	 */
	public static void velocityLogService(LogserviceObject obj, String outpath){
		 //获取模板引擎
      VelocityEngine ve = new VelocityEngine();
      //模板文件所在的路径
      String path = "./conf/";
      //设置参数
      ve.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, path);
      //处理中文问题
      ve.setProperty(Velocity.INPUT_ENCODING,"UTF-8");
      ve.setProperty(Velocity.OUTPUT_ENCODING,"UTF-8");
      try 
      {
          //初始化模板
          ve.init();
          //获取模板(hello.html)
          //Velocity模板的名称
          Template template = ve.getTemplate("logService.cm");
          //获取上下文
          VelocityContext root = new VelocityContext();
          //把数据填入上下文
          root.internalPut("obj", obj);
          //输出
          Writer mywriter = new PrintWriter(new FileOutputStream(new File(outpath))); 
          System.out.println(outpath);
          template.merge(root, mywriter);
          mywriter.flush();           
      } 
      catch (Exception e) 
      {
          e.printStackTrace();
      }
	}
	/**
	 * 生成协议映射配置文件
	 * @param obj
	 * @param outpath
	 */
	public static void velocityMessageConst(LogserviceObject obj, String outpath){
		VelocityEngine ve = new VelocityEngine();
		String path = "./conf/";
		ve.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, path);
		ve.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
		ve.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
		try {
			ve.init();
			Template template = ve.getTemplate("messageConstMap.vm");
			VelocityContext root = new VelocityContext();
			root.internalPut("obj", obj);
			 //输出
			Writer mywriter = new PrintWriter(new FileOutputStream(new File(outpath))); 
			template.merge(root, mywriter);
			mywriter.flush();    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 生成建表语句
	 * @param obj
	 * @param outpath
	 */
	public static void velocityCreateSql(LogserviceObject obj, String outpath){
		VelocityEngine ve = new VelocityEngine();
		String path = "./conf/";
		ve.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, path);
		ve.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
		ve.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
		try {
			ve.init();
			Template template = ve.getTemplate("createSql.vm");
			VelocityContext root = new VelocityContext();
			root.internalPut("obj", obj);
			 //输出
			Writer mywriter = new PrintWriter(new FileOutputStream(new File(outpath))); 
			template.merge(root, mywriter);
			mywriter.flush();    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
