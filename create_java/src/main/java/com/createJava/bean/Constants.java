package com.createJava.bean;


import com.createJava.utils.PropertiesUtils;

/**
 * @author moZiA
 * @date 2025/3/22 18:07
 * @description
 */
public class Constants {

  //是否忽略表前缀
  public static Boolean IGNORE_TABLE_PREFIX;

  //参数 bean 后缀
  public static String SUFFIX_BEAN_QUERY;
  public static String SUFFIX_BEAN_QUERY_FUZZY;
  public static String SUFFIX_BEAN_QUERY_TIME_START;
  public static String SUFFIX_BEAN_QUERY_TIME_END;
  public static String SUFFIX_MAPPERS;

  public static String AUTHOR_COMMENT;

  //需要忽略的属性
  public static String IGNORE_BEAN_TOJSON_FILED;
  public static String IGNORE_BEAN_TOJSON_EXPRESION;
  public static String IGNORE_BEAN_TOJSON_CLASS;

  // 日期序列化, 反序列化
  public static String BEAN_DATE_FORMAT_EXPRESION;
  public static String BEAN_DATE_FORMAT_CLASS;

  public static String BEAN_DATE_UNFORMAT_EXPRESION;
  public static String BEAN_DATE_UNFORMAT_CLASS;

  private static String PATH_JAVA = "java";

  private static String PATH_RESOURCES = "resources";


  //文件生成路径
  public static String PACKAGE_BASE;

  public static String PACKAGE_PO;
  public static String PACKAGE_QUERY;
  public static String PACKAGE_UTILS;
  public static String PACKAGE_ENUMS;
  public static String PACKAGE_MAPPERS;

  public static String PATH_BASE;

  public static String PATH_PO;
  public static String PATH_QUERY;
  public static String PATH_UTILS;
  public static String PATH_ENUMS;
  public static String PATH_MAPPERS;
  public static String PATH_MAPPERS_XMLS;

  static {
    AUTHOR_COMMENT = PropertiesUtils.getString("author.comment");

    //需要忽略的属性
    IGNORE_BEAN_TOJSON_FILED = PropertiesUtils.getString("ignore.bean.tojson.filed");
    IGNORE_BEAN_TOJSON_EXPRESION = PropertiesUtils.getString("ignore.bean.tojson.expresion");
    IGNORE_BEAN_TOJSON_CLASS = PropertiesUtils.getString("ignore.bean.tojson.class");

    // 日期序列化, 反序列化
    BEAN_DATE_FORMAT_EXPRESION = PropertiesUtils.getString("bean.date.format.expresion");
    BEAN_DATE_FORMAT_CLASS = PropertiesUtils.getString("bean.date.format.class");

    BEAN_DATE_UNFORMAT_EXPRESION = PropertiesUtils.getString("bean.date.unformat.expresion");
    BEAN_DATE_UNFORMAT_CLASS = PropertiesUtils.getString("bean.date.unformat.class");

    IGNORE_TABLE_PREFIX = Boolean.valueOf(PropertiesUtils.getString("ignore.table.prefix"));
    SUFFIX_BEAN_QUERY = PropertiesUtils.getString("suffix.bean.query");
    // 后缀
    SUFFIX_BEAN_QUERY_FUZZY = PropertiesUtils.getString("suffix.bean.query.fuzzy");
    SUFFIX_BEAN_QUERY_TIME_START = PropertiesUtils.getString("suffix.bean.query.time.start");
    SUFFIX_BEAN_QUERY_TIME_END = PropertiesUtils.getString("suffix.bean.query.time.end");
    SUFFIX_MAPPERS = PropertiesUtils.getString("suffix.mappers");

    PACKAGE_BASE = PropertiesUtils.getString("package.base");
    //PO
    PACKAGE_PO = PACKAGE_BASE + "." + PropertiesUtils.getString("package.po");
    PACKAGE_QUERY = PACKAGE_BASE + "." + PropertiesUtils.getString("package.param");
    PACKAGE_UTILS = PACKAGE_BASE + "." + PropertiesUtils.getString("package.utils");
    PACKAGE_ENUMS = PACKAGE_BASE + "." + PropertiesUtils.getString("package.enums");
    PACKAGE_MAPPERS = PACKAGE_BASE + "." + PropertiesUtils.getString("package.mappers");

    PATH_BASE = PropertiesUtils.getString("path.base");
    PATH_BASE = PATH_BASE + PATH_JAVA;

    PATH_PO = PATH_BASE + "/" + PACKAGE_PO.replace(".", "/");
    PATH_QUERY = PATH_BASE + "/" + PACKAGE_QUERY.replace(".", "/");
    PATH_UTILS = PATH_BASE + "/" + PACKAGE_UTILS.replace(".", "/");
    PATH_ENUMS = PATH_BASE + "/" + PACKAGE_ENUMS.replace(".", "/");
    PATH_MAPPERS = PATH_BASE + "/" + PACKAGE_MAPPERS.replace(".", "/");
    PATH_MAPPERS_XMLS = PropertiesUtils.getString("path.base") + PATH_RESOURCES;
    PATH_MAPPERS_XMLS =
        PropertiesUtils.getString("path.base") + PATH_RESOURCES + "/" + PACKAGE_MAPPERS.replace(".",
            "/");


  }

  public static String[] SQL_DATE_TIME_TYPES = new String[]{"datetime", "timestamp"};
  public static String[] SQL_DATE_TYPES = new String[]{"date"};
  public static String[] SQL_DECIMAL_TYPE = new String[]{"decimal", "double", "float"};
  public static String[] SQL_STRING_TYPE = new String[]{"char", "varchar", "text", "mediumtext",
      "longtext"};
  public static String[] SQL_INTEGER_TYPE = new String[]{"int", "tinyint"};
  public static String[] SQL_LONG_TYPE = new String[]{"bigint"};


  public static void main(String[] args) {
    System.out.println(PATH_MAPPERS_XMLS);
  }


}