package rongji.framework.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 
 * @Title: EntityUtil.java
 * @Package
 * @Description: 数据库映射成实体类(有注释)
 * @author Administrator
 * @date 2015年12月4日 上午11:32:30
 * @version V1.0
 */
public class EntityUtil {

	/**
	 * 类型
	 */
	private String type_char = "char";
	private String type_ui = "uniqueidentifier";
	private String type_date = "date";
	private String type_int = "int";
	private String type_bigint = "bigint";
	private String type_text = "text";
	private String type_bit = "bit";
	private String type_smallint = "smallint";
	private String type_folat = "folat";
	private String type_varbinary = "varbinary";
	private String type_tinyint = "tinyint";
	private String type_decimal = "decimal";
	private String type_sysname = "sysname";
	/**
	 * 文件保存位置
	 */
	private String bean_path = "d:/entity_bean";
	//private String mapper_path = "d:/entity_mapper";

	/**
	 * 包路径
	 */
	private String bean_package = "rongji.cmis.model";
	//private String mapper_package = "com.bling.saysays.mapper";
	//private String mapper_extends = "SqlMapper";

	/**
	 * 数据库连接信息
	 */
	private String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";// 驱动
	private String userName = "sa";// 用户名
	private String password = "swzzb@123456";// 密码
	private String url = "jdbc:sqlserver://192.168.0.118:1433; DatabaseName=CMIS";// 连接
	/**
	 * 表名
	 */
	@SuppressWarnings("unused")
	private String tableName;
	/**
	 * 实体类名
	 */
	private String beanName;
	@SuppressWarnings("unused")
	private String mapperName;

	/**
	 * 数据库连接
	 */
	private Connection conn;

	/**
	 * 
	 * @Title: init
	 * @Description: 初始化数据库连接
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @return void 返回类型
	 * @throws
	 * @author Administrator
	 */
	private void init() throws ClassNotFoundException, SQLException {
		Class.forName(driverName);
		conn = DriverManager.getConnection(url, userName, password);
	}

	/**
	 * 
	 * @Title: getTables
	 * @Description: 读取数据库表名
	 * @return
	 * @throws SQLException
	 * @return List<String> 返回类型
	 * @throws
	 * @author Administrator
	 */
	private List<String> getTables() throws SQLException {
		List<String> tables = new ArrayList<String>();
		PreparedStatement pstate = conn.prepareStatement("select name from sysobjects where type='U'");
		ResultSet results = pstate.executeQuery();
		while (results.next()) {
			tables.add(results.getString(1));
		}
		return tables;
	}

	/**
	 * 
	 * @Title: processTable
	 * @Description: 设置生成的实体类名称
	 * @param table
	 * @return void 返回类型
	 * @throws
	 * @author Administrator
	 */
	private void processTable(String table) {
		beanName = table.toUpperCase();
		mapperName = beanName + "Mapper";
	}

	/**
	 * 
	 * @Title: processType
	 * @Description: 设置生成实体类属性的类型
	 * @param type
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author Administrator
	 */
	private String processType(String type) {
		if (type.indexOf(type_char) > -1 || type.indexOf(type_text) > -1 || type.indexOf(type_sysname) > -1 || type.indexOf(type_ui) > -1) {
			return "String";
		} else if (type.equals(type_int) || type.equals(type_bigint)) {
			return "Integer";
		} else if (type.indexOf(type_date) > -1) {
			return "Date";
		} else if (type.indexOf(type_bit) > -1) {
			return "Boolean";
		} else if (type.equals(type_smallint) || type.equals(type_tinyint)) {
			return "Short";
		} else if (type.equals(type_folat)) {
			return "Double";
		} else if (type.indexOf(type_varbinary) > -1) {
			return "byte[]";
		} else if (type.indexOf(type_decimal) > -1) {
			return "BigDecimal";
		}
		return null;
	}

	/**
	 * 
	 * @Title: processField
	 * @Description: 设置实体类属性名，对应数据库表字段名称
	 * @param field
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author Administrator
	 */
	private String processField(String field) {
		StringBuffer sb = new StringBuffer(field.length());
		field = field.toLowerCase();
		String[] fields = field.split("_");
		String temp = null;
		sb.append(fields[0]);
		for (int i = 1; i < fields.length; i++) {
			temp = fields[i].trim();
			sb.append(temp.substring(0, 1).toUpperCase()).append(temp.substring(1));
		}
		return sb.toString();
	}

	/**
	 * 
	 * @Title: processResultMapId
	 * @Description: 生成bean的名字
	 * @param beanName
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author Administrator
	 */
	//private String processResultMapId(String beanName) {
		//return beanName.toUpperCase();
	//}

	/**
	 * 
	 * @Title: outputAuthor
	 * @Description: 输出作者时间等信息
	 * @param bw
	 * @param text
	 * @throws IOException
	 * @return void 返回类型
	 * @throws
	 * @author Administrator
	 */
	private void outputAuthor(BufferedWriter bw, String text) throws IOException {
		bw.newLine();
		bw.newLine();
		bw.write("/**");
		bw.newLine();
		bw.write(" *<pre>");
		bw.newLine();
		bw.write(" *  " + text);
		bw.newLine();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		bw.write(" *  时间:" + sdf.format(new java.util.Date()));
		bw.newLine();
		bw.write(" *</pre>");
		bw.newLine();
		bw.write(" * @author EntityUtil工具类");
		bw.newLine();
		bw.write(" * @version 1.0");
		bw.newLine();
		bw.write("**/");
		bw.newLine();
		bw.newLine();
	}

	/**
	 * 
	 * @Title: outputBaseBean
	 * @Description: 生成BaseBean
	 * @throws IOException
	 * @return void 返回类型
	 * @throws
	 * @author Administrator
	 */
	//private void outputBaseBean() throws IOException {
		//File folder = new File(bean_path);
		//if (!folder.exists()) {
			//folder.mkdir();
		//}
		//File beanFile = new File(bean_path, "BaseBean.java");
		//BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(beanFile)));
		//bw.write("package " + bean_package + ";");
		//bw.newLine();
		//bw.write("import java.io.Serializable;");
		//this.outputAuthor(bw, "排序基类");
		//bw.newLine();
		//bw.write("@SuppressWarnings(\"serial\")");
		//bw.newLine();
		//bw.write("public class BaseBean implements Serializable{");
		//bw.newLine();
		//bw.write("\t/**排序**/");
		//bw.newLine();
		//bw.write("\tprivate String orderSql;");
		//bw.newLine();
		// 生成get 和 set方法

		//bw.write("\tpublic String getOrderSql(){");
		//bw.newLine();
		//bw.write("\t\treturn this.orderSql;");
		//bw.newLine();
		//bw.write("\t}");
		//bw.newLine();
		//bw.newLine();

		//bw.write("\tpublic void setOrderSql(String orderSql){");
		//bw.newLine();
		//bw.write("\t\tthis.orderSql=orderSql;");
		//bw.newLine();
		//bw.write("\t}");
		//bw.newLine();
		//bw.newLine();

		//bw.write("}");
		//bw.newLine();
		//bw.flush();
		//bw.close();
	//}

	/**
	 * 
	 * @Title: outputBean
	 * @Description: 输出转换成功的实体类
	 * @param columns
	 * @param types
	 * @param comments
	 * @throws IOException
	 * @return void 返回类型
	 * @throws
	 * @author Administrator
	 */
	private void outputBean(List<String> columns, List<String> types, List<String> comments) throws IOException {
		File folder = new File(bean_path);
		if (!folder.exists()) {
			folder.mkdir();
		}
		// 设置保存路径及文件名
		File beanFile = new File(bean_path, beanName + ".java");
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(beanFile)));
		// 导入基本包
		bw.write("package " + bean_package + ";");
		bw.newLine();
		// 日期类
		bw.write("import java.util.Date;");
		bw.newLine();
		bw.write("import javax.persistence.Entity;");
		this.outputAuthor(bw, beanName + "实体类");
		bw.newLine();
		bw.write("@SuppressWarnings(\"serial\")");
		bw.newLine();
		bw.write("@Entity");
		bw.newLine();
		// 类名
		bw.write("public class " + beanName + " extends BaseEntity{");
		bw.newLine();
		int size = columns.size();
		// 注释格式
		for (int i = 0; i < size; i++) {
			bw.write("\t/**");
			bw.newLine();
			bw.write("\t * " + comments.get(i));
			bw.newLine();
			bw.write("\t */");
			bw.newLine();
			bw.write("\tprivate " + this.processType(types.get(i)) + " " + this.processField(columns.get(i)) + ";");
			bw.newLine();
		}
		bw.newLine();
		// 生成get 和 set方法
		String tempField = null;
		String _tempField = null;
		String tempType = null;
		for (int i = 0; i < size; i++) {
			tempType = this.processType(types.get(i));
			_tempField = this.processField(columns.get(i));
			tempField = _tempField.substring(0, 1).toUpperCase() + _tempField.substring(1);
			bw.newLine();
			bw.write("\tpublic void set" + tempField + "(" + tempType + " _" + _tempField + "){");
			bw.newLine();
			bw.write("\t\tthis." + _tempField + "=_" + _tempField + ";");
			bw.newLine();
			bw.write("\t}");
			bw.newLine();

			bw.write("\tpublic " + tempType + " get" + tempField + "(){");
			bw.newLine();
			bw.write("\t\treturn this." + _tempField + ";");
			bw.newLine();
			bw.write("\t}");
			bw.newLine();
		}
		bw.newLine();
		bw.write("}");
		bw.newLine();
		bw.flush();
		bw.close();
	}

	/**
	 * 
	 * @Title: outputMapper
	 * @Description: 生成相应的Mapper类
	 * @throws IOException
	 * @return void 返回类型
	 * @throws
	 * @author Administrator
	 */
	//private void outputMapper() throws IOException {
		//File folder = new File(mapper_path);
		//if (!folder.exists()) {
			//folder.mkdir();
		//}
		//File mapperFile = new File(mapper_path, mapperName + ".java");
		//BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mapperFile)));
		//bw.write("package " + mapper_package + ";");
		//bw.newLine();
		//bw.write("import " + bean_package + "." + beanName + ";");
		//this.outputAuthor(bw, mapperName + "数据库操作接口类");
		//bw.newLine();
		//bw.write("public interface " + mapperName + " extends " + mapper_extends + "<" + beanName + "> {");
		//bw.newLine();
		//bw.newLine();
		//bw.write("}");
		//bw.flush();
		//bw.close();
	//}

	/**
	 * 
	 * @Title: outputMapperXml
	 * @Description: 生成XML文件
	 * @param columns
	 * @param types
	 * @param comments
	 * @throws IOException
	 * @return void 返回类型
	 * @throws
	 * @author Administrator
	 */
	//private void outputMapperXml(List<String> columns, List<String> types, List<String> comments) throws IOException {
		//File folder = new File(mapper_path);
		//if (!folder.exists()) {
			//folder.mkdir();
		//}
		//File mapperXmlFile = new File(mapper_path, mapperName + ".xml");
		//BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mapperXmlFile)));
		//bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		//bw.newLine();
		//bw.write("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" ");
		//bw.newLine();
		//bw.write("    \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
		//bw.newLine();
		//bw.write("<mapper namespace=\"" + mapper_package + "." + mapperName + "\">");
		//bw.newLine();
		//bw.newLine();
		//bw.write("\t<!--实体映射-->");
		//bw.newLine();
		//bw.write("\t<resultMap id=\"" + this.processResultMapId(beanName) + "ResultMap\" type=\"" + beanName + "\">");
		//bw.newLine();
		//bw.write("\t\t<!--" + comments.get(0) + "-->");
		//bw.newLine();
		//bw.write("\t\t<id property=\"" + this.processField(columns.get(0)) + "\" column=\"" + columns.get(0) + "\" />");
		//bw.newLine();
		//int size = columns.size();
		//for (int i = 1; i < size; i++) {
			//bw.write("\t\t<!--" + comments.get(i) + "-->");
			//bw.newLine();
			//bw.write("\t\t<result property=\"" + this.processField(columns.get(i)) + "\" column=\"" + columns.get(i) + "\" />");
			//bw.newLine();
		//}
		//bw.write("\t</resultMap>");

		//bw.newLine();
		//bw.newLine();

		//bw.write("\t<!--分页类型映射-->");
		//bw.newLine();
		//bw.write("\t<parameterMap type=\"PageParam\" id=\"pageParamMap\">");
		//bw.newLine();

		//bw.write("\t\t<parameter property=\"t\" resultMap=\"" + this.processResultMapId(beanName) + "ResultMap\"/>");
		//bw.newLine();

		//bw.write("\t\t<parameter property=\"start\" javaType=\"int\"/>");
		//bw.newLine();

		//bw.write("\t\t<parameter property=\"num\" javaType=\"int\"/>");
		//bw.newLine();

		//bw.write("\t</parameterMap>");
		//bw.newLine();
		//bw.newLine();

		// 下面开始写SqlMapper中的方法
		//this.outputSqlMapperMethod(bw, columns, types);

		//bw.write("</mapper>");
		//bw.flush();
		//bw.close();
	//}

	/**
	 * 
	 * @Title: outputSqlMapperMethod
	 * @Description: 基本操作方法
	 * @param bw
	 * @param columns
	 * @param types
	 * @throws IOException
	 * @return void 返回类型
	 * @throws
	 * @author Administrator
	 */
	//private void outputSqlMapperMethod(BufferedWriter bw, List<String> columns, List<String> types) throws IOException {
		//int size = columns.size();
		// 写add - insert方法
		//bw.write("\t<!--添加-->");
		//bw.newLine();
		//bw.write("\t<insert id=\"add\" parameterType=\"" + beanName + "\">");
		//bw.newLine();
		//bw.write("\t\tinsert into " + tableName);
		//bw.newLine();
		//bw.write(" \t\t(");
		//for (int i = 1; i < size; i++) {
			//bw.write(columns.get(i));
			//if (i != size - 1) {
				//bw.write(",");
			//}
		//}
		//bw.write(") ");
		//bw.newLine();
		//bw.write("\t\tvalues ");
		//bw.newLine();
		//bw.write(" \t\t(");
		//for (int i = 1; i < size; i++) {
			//bw.write("#{" + this.processField(columns.get(i)) + "}");
			//if (i != size - 1) {
				//bw.write(",");
			//}
		//}
		//bw.write(") ");
		//bw.newLine();
		//bw.write("\t</insert>");
		//bw.newLine();
		//bw.newLine();
		// add insert写入完毕

		// 写update
		//bw.write("\t<!--更新：只更新有值字段-->");
		//bw.newLine();
		//bw.write("\t<update id=\"update\" parameterType=\"" + beanName + "\">");
		//bw.newLine();
		//bw.write("\t\tupdate " + tableName + " set " + columns.get(0) + "=${" + this.processField(columns.get(0)) + "}");
		//bw.newLine();
		//String tempField = null;
		//for (int i = 1; i < size; i++) {
			//tempField = this.processField(columns.get(i));
			//bw.write("\t\t<if test=\"" + tempField + "!=null\">");
			//bw.newLine();
			//bw.write("\t\t\t," + columns.get(i) + "=#{" + tempField + "}");
			//bw.newLine();
			//bw.write("\t\t</if>");
			//bw.newLine();
		//}
		//bw.write("\t\twhere " + columns.get(0) + "=#{" + this.processField(columns.get(0)) + "}");
		//bw.newLine();
		//bw.write("\t</update>");
		//bw.newLine();
		//bw.newLine();
		// update写完

		// 写delete
		//bw.write("\t<!--删除：根据主键编号删除-->");
		//bw.newLine();
		//bw.write("\t<delete id=\"delete\" parameterType=\"int\">");
		//bw.newLine();
		//bw.write("\t\tdelete from " + tableName);
		//bw.newLine();
		//bw.write("\t\twhere " + columns.get(0) + "=#{" + this.processField(columns.get(0)) + "}");
		//bw.newLine();
		//bw.write("\t</delete>");
		//bw.newLine();
		//bw.newLine();
		// delete写完

		// 写get
		//bw.write("\t<!--查找：根据主键编号查找-->");
		//bw.newLine();
		//bw.write("\t<select id=\"get\" parameterType=\"int\" resultMap=\"" + this.processResultMapId(beanName) + "ResultMap\">");
		//bw.newLine();
		//bw.write("\t\tselect * from " + tableName);
		//bw.newLine();
		//bw.write("\t\twhere " + columns.get(0) + "=#{" + this.processField(columns.get(0)) + "}");
		//bw.newLine();
		//bw.write("\t</select>");
		//bw.newLine();
		//bw.newLine();
		// get写完

		// 写findEqual
		//bw.write("\t<!--==查找，匹配有值字段-->");
		//bw.newLine();
		//bw.write("\t<select id=\"findEqual\" parameterType=\"" + beanName + "\" resultMap=\"" + this.processResultMapId(beanName) + "ResultMap\">");
		//bw.newLine();
		//bw.write("\t\tselect * from " + tableName + "where 1=1 ");
		//bw.newLine();
		//tempField = null;
		//for (int i = 0; i < size; i++) {
			//tempField = this.processField(columns.get(i));
			//bw.write("\t\t<if test=\"" + tempField + "!=null\">");
			//bw.newLine();
			//bw.write("\t\t\tand " + columns.get(i) + "=#{" + tempField + "}");
			//bw.newLine();
			//bw.write("\t\t</if>");
			//bw.newLine();
		//}
		//bw.write("\t\t<if test=\"orderSql!=null\">");
		//bw.newLine();
		//bw.write("\t\t\t #{orderSql}");
		//bw.newLine();
		//bw.write("\t\t</if>");
		//bw.newLine();
		//bw.write("\t</select>");
		//bw.newLine();
		//bw.newLine();
		// findEqual写完

		// 写findLike
		//bw.write("\t<!--like查找，匹配有值字段-->");
		//bw.newLine();
		//bw.write("\t<select id=\"findLike\" parameterType=\"" + beanName + "\" resultMap=\"" + this.processResultMapId(beanName) + "ResultMap\">");
		//bw.newLine();
		//bw.write("\t\tselect * from " + tableName + "where 1=1 ");
		//bw.newLine();
		//tempField = null;
		//for (int i = 0; i < size; i++) {
			//tempField = this.processField(columns.get(i));
			//bw.write("\t\t<if test=\"" + tempField + "!=null\">");
			//bw.newLine();
			//bw.write("\t\t\tand " + columns.get(i) + " like %#{" + tempField + "}%");
			//bw.newLine();
			//bw.write("\t\t</if>");
			//bw.newLine();
		//}
		//bw.write("\t\t<if test=\"orderSql!=null\">");
		//bw.newLine();
		//bw.write("\t\t\t #{orderSql}");
		//bw.newLine();
		//bw.write("\t\t</if>");
		//bw.newLine();
		//bw.write("\t</select>");
		//bw.newLine();
		//bw.newLine();
		// findLike写完

		// 写getCountLike
		//bw.write("\t<!--查询条数，匹配有值字段-->");
		//bw.newLine();
		//bw.write("\t<select id=\"getCountLike\" parameterType=\"" + beanName + "\" resultType=\"int\">");
		//bw.newLine();
		//bw.write("\t\tselect count(*) from " + tableName + "where 1=1 ");
		//bw.newLine();
		//tempField = null;
		//for (int i = 0; i < size; i++) {
			//tempField = this.processField(columns.get(i));
			//bw.write("\t\t<if test=\"" + tempField + "!=null\">");
			//bw.newLine();
			//bw.write("\t\t\tand " + columns.get(i) + " like %#{" + tempField + "}%");
			//bw.newLine();
			//bw.write("\t\t</if>");
			//bw.newLine();
		//}
		//bw.write("\t</select>");
		//bw.newLine();
		//bw.newLine();
		// getCountLike写完

		// 写getCountEqual
		//bw.write("\t<!--查询条数，匹配有值字段-->");
		//bw.newLine();
		//bw.write("\t<select id=\"getCountEqual\" parameterType=\"" + beanName + "\" resultType=\"int\">");
		//bw.newLine();
		//bw.write("\t\tselect count(*) from " + tableName + "where 1=1 ");
		//bw.newLine();
		//tempField = null;
		//for (int i = 0; i < size; i++) {
			//tempField = this.processField(columns.get(i));
			//bw.write("\t\t<if test=\"" + tempField + "!=null\">");
			//bw.newLine();
			//bw.write("\t\t\tand " + columns.get(i) + "=#{" + tempField + "}");
			//bw.newLine();
			//bw.write("\t\t</if>");
			//bw.newLine();
		//}
		//bw.write("\t</select>");
		//bw.newLine();
		//bw.newLine();
		// getCountEqual写完

		// 写listEqual
		//bw.write("\t<!--==查找，匹配有值字段-->");
		//bw.newLine();
		//bw.write("\t<select id=\"listEqual\" parameterType=\"" + beanName + "\" resultMap=\"" + this.processResultMapId(beanName) + "ResultMap\">");
		//bw.newLine();
		//bw.write("\t\tselect * from " + tableName + "where 1=1 ");
		//bw.newLine();
		//tempField = null;
		//for (int i = 0; i < size; i++) {
			//tempField = this.processField(columns.get(i));
			//bw.write("\t\t<if test=\"" + tempField + "!=null\">");
			//bw.newLine();
			//bw.write("\t\t\tand " + columns.get(i) + "=#{" + tempField + "}");
			//bw.newLine();
			//bw.write("\t\t</if>");
			//bw.newLine();
		//}
		//bw.write("\t\t<if test=\"orderSql!=null\">");
		//bw.newLine();
		//bw.write("\t\t\t #{orderSql}");
		//bw.newLine();
		//bw.write("\t\t</if>");
		//bw.newLine();
		//bw.write("\t</select>");
		//bw.newLine();
		//bw.newLine();
		// listEqual写完

		// 写listLike
		//bw.write("\t<!--like查找，匹配有值字段-->");
		//bw.newLine();
		//bw.write("\t<select id=\"listLike\" parameterType=\"" + beanName + "\" resultMap=\"" + this.processResultMapId(beanName) + "ResultMap\">");
		//bw.newLine();
		//bw.write("\t\tselect * from " + tableName + "where 1=1 ");
		//bw.newLine();
		//tempField = null;
		//for (int i = 0; i < size; i++) {
			//tempField = this.processField(columns.get(i));
			//bw.write("\t\t<if test=\"" + tempField + "!=null\">");
			//bw.newLine();
			//bw.write("\t\t\tand " + columns.get(i) + " like %#{" + tempField + "}%");
			//bw.newLine();
			//bw.write("\t\t</if>");
			//bw.newLine();
		//}
		//bw.write("\t\t<if test=\"orderSql!=null\">");
		//bw.newLine();
		//bw.write("\t\t\t #{orderSql}");
		//bw.newLine();
		//bw.write("\t\t</if>");
		//bw.newLine();
		//bw.write("\t</select>");
		//bw.newLine();
		//bw.newLine();
		// listLike写完

		// 写pageListEqual
		//bw.write("\t<!--==分页查找，匹配有值字段-->");
		//bw.newLine();
		//bw.write("\t<select id=\"listEqual\" parameterType=\"" + beanName + "\" resultMap=\"" + this.processResultMapId(beanName) + "ResultMap\">");
		//bw.newLine();
		//bw.write("\t\tselect * from " + tableName + "where 1=1 ");
		//bw.newLine();
		//tempField = null;
		//for (int i = 0; i < size; i++) {
			//tempField = this.processField(columns.get(i));
			//bw.write("\t\t<if test=\"" + tempField + "!=null\">");
			//bw.newLine();
			//bw.write("\t\t\tand " + columns.get(i) + "=#{" + tempField + "}");
			//bw.newLine();
			//bw.write("\t\t</if>");
			//bw.newLine();
		//}
		//bw.write("\t\t<if test=\"orderSql!=null\">");
		//bw.newLine();
		//bw.write("\t\t\t #{orderSql}");
		//bw.newLine();
		//bw.write("\t\t</if>");
		//bw.newLine();
		//bw.write("\t\t\tlimit #{start},#{num}");
		//bw.newLine();
		//bw.write("\t</select>");

		//bw.newLine();
		//bw.newLine();
		// pageListEqual写完

		// 写pageListLike
		//bw.write("\t<!--like分页查找，匹配有值字段-->");
		//bw.newLine();
		//bw.write("\t<select id=\"listLike\" parameterType=\"" + beanName + "\" resultMap=\"" + this.processResultMapId(beanName) + "ResultMap\">");
		//bw.newLine();
		//bw.write("\t\tselect * from " + tableName + "where 1=1 ");
		//bw.newLine();
		//tempField = null;
		//for (int i = 0; i < size; i++) {
			//tempField = this.processField(columns.get(i));
			//bw.write("\t\t<if test=\"" + tempField + "!=null\">");
			//bw.newLine();
			//bw.write("\t\t\tand " + columns.get(i) + " like %#{" + tempField + "}%");
			//bw.newLine();
			//bw.write("\t\t</if>");
			//bw.newLine();
		//}
		//bw.write("\t\t<if test=\"orderSql!=null\">");
		//bw.newLine();
		//bw.write("\t\t\t #{orderSql}");
		//bw.newLine();
		//bw.write("\t\t</if>");
		//bw.newLine();
		//bw.write("\t\t\tlimit #{start},#{num}");
		//bw.newLine();
		//bw.write("\t</select>");
		//bw.newLine();
		//bw.newLine();
		// pageListLike写完

		// bw.write("");
		//bw.newLine();
	//}

	/**
	 * 
	 * @Title: generateOne
	 * @Description: 生成器(单表)
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 * @return void 返回类型
	 * @throws
	 * @author Administrator
	 */
	@SuppressWarnings("resource")
	public void generateOne() throws ClassNotFoundException, SQLException, IOException {
		this.init();

		List<String> columns = null;
		List<String> types = null;
		List<String> comments = null;
		PreparedStatement pstate = null;
		List<String> tables = this.getTables();

		Scanner in = new Scanner(System.in);
		String table = "";
		System.out.print("请输入表名:");
		table = in.next();
		while (!tables.contains(table)) {
			System.out.print("表名有误，请重新输入:");
			table = in.next();
		}
		System.out.println("正在生成...");
		columns = new ArrayList<String>();
		types = new ArrayList<String>();
		comments = new ArrayList<String>();
		// SQL语句
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT convert(varchar(100), C.Name) AS FieldName,T.Name AS DataType,convert(varchar(100), ISNULL(P.value, '')) AS FieldComment ");
		sql.append("FROM sys.columns  C INNER JOIN  sys.types T ON C.system_type_id = T.user_type_id ");
		sql.append("LEFT JOIN dbo.syscomments M ON M.id = C.default_object_id ");
		sql.append("LEFT JOIN sys.extended_properties P ON P.major_id = C.object_id AND C.column_id = P.minor_id ");
		sql.append("WHERE C.[object_id] = OBJECT_ID('dbo." + table + "') ORDER BY C.Column_Id ASC");
		pstate = conn.prepareStatement(sql.toString());
		ResultSet results = pstate.executeQuery();
		while (results.next()) {
			columns.add(results.getString("FieldName"));
			types.add(results.getString("DataType"));
			comments.add(results.getString("FieldComment"));
		}
		tableName = table;
		this.processTable(table);
		//this.outputBaseBean();
		this.outputBean(columns, types, comments);
		//this.outputMapper();
		//this.outputMapperXml(columns, types, comments);
		conn.close();
		System.out.println("生成成功！");
	}

	/**
	 * 
	* @Title: generateAll
	* @Description: 生成器(全部表)
	* @throws ClassNotFoundException
	* @throws SQLException
	* @throws IOException
	* @return void    返回类型
	* @throws
	* @author Administrator
	 */
	public void generateAll() throws ClassNotFoundException, SQLException, IOException {
		this.init();

		List<String> columns = null;
		List<String> types = null;
		List<String> comments = null;
		PreparedStatement pstate = null;
		List<String> tables = this.getTables();

		System.out.println("正在生成...");
		for (String table : tables) {
			columns = new ArrayList<String>();
			types = new ArrayList<String>();
			comments = new ArrayList<String>();
			// SQL语句
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT convert(varchar(100), C.Name) AS FieldName,T.Name AS DataType,convert(varchar(100), ISNULL(P.value, '')) AS FieldComment ");
			sql.append("FROM sys.columns  C INNER JOIN  sys.types T ON C.system_type_id = T.user_type_id ");
			sql.append("LEFT JOIN dbo.syscomments M ON M.id = C.default_object_id ");
			sql.append("LEFT JOIN sys.extended_properties P ON P.major_id = C.object_id AND C.column_id = P.minor_id ");
			sql.append("WHERE C.[object_id] = OBJECT_ID('dbo." + table + "') ORDER BY C.Column_Id ASC");
			pstate = conn.prepareStatement(sql.toString());
			ResultSet results = pstate.executeQuery();
			while (results.next()) {
				columns.add(results.getString("FieldName"));
				types.add(results.getString("DataType"));
				comments.add(results.getString("FieldComment"));
			}
			tableName = table;
			this.processTable(table);
			//this.outputBaseBean();
			this.outputBean(columns, types, comments);
			//this.outputMapper();
			//this.outputMapperXml(columns, types, comments);
		}
		conn.close();
		System.out.println("生成成功！");
	}

	/**
	 * 
	 * @Title: main
	 * @Description: main方法
	 * @param args
	 * @return void 返回类型
	 * @throws
	 * @author Administrator
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		try {
			System.out.println("菜单--1：单表生成    2：全部生成");
			Scanner in = new Scanner(System.in);
			String choice = "";
			System.out.print("请选择：");
			choice = in.next();
			while(!(choice.equals("1")) && !(choice.equals("2"))){
				System.out.print("请重新输入：");
				choice = in.next();
			}
			if(choice.equals("1")){
				new EntityUtil().generateOne();
			}else if(choice.equals("2")){
				new EntityUtil().generateAll();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
