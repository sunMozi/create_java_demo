package com.createJava.builder;


import com.createJava.bean.Constants;
import com.createJava.bean.FieldInfo;
import com.createJava.bean.TableInfo;
import com.createJava.utils.JsonUtils;
import com.createJava.utils.PropertiesUtils;
import com.createJava.utils.StringUtils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author moZiA
 * @date 2025/3/22 17:27
 * @description
 */

public class BuilderTable {

  private static final Logger log = LoggerFactory.getLogger(BuilderTable.class);
  private static Connection conn = null;
  private static final String SQL_SHOW_TABLE_STATUS = "show table status";
  private static final String SQL_SHOW_TABLE_FIELDS = "show full fields from %s";
  private static final String SQL_SHOW_TABLE_INDEX = "SHOW INDEX FROM %s";


  static {
    String driverName = PropertiesUtils.getString("db.driver.name");
    String url = PropertiesUtils.getString("db.url");
    String user = PropertiesUtils.getString("db.root");
    String password = PropertiesUtils.getString("db.password");

    try {
      Class.forName(driverName);
      conn = DriverManager.getConnection(url, user, password);
    } catch (Exception e) {
      log.error("数据库连接失败", e);
    }
  }


  /**
   * @author MOZI
   */
  public static List<TableInfo> getTables() {
    PreparedStatement ps = null;
    ResultSet tableResult = null;

    List<TableInfo> tableInfoList = new ArrayList<>();

    try {
      ps = conn.prepareStatement(SQL_SHOW_TABLE_STATUS);
      tableResult = ps.executeQuery();

      while (tableResult.next()) {
        String tableName = tableResult.getString("name");
        String comment = tableResult.getString("comment");

        String beanName = tableName;
        if (Constants.IGNORE_TABLE_PREFIX) {
          beanName = tableName.substring(beanName.indexOf("_") + 1);
        }

        beanName = processFiled(beanName, true);
        TableInfo tableInfo = new TableInfo();
        tableInfoList.add(tableInfo);
        tableInfo.setTableName(tableName);
        tableInfo.setBeanName(beanName);
        tableInfo.setComment(comment);
        tableInfo.setBeanParamName(beanName + Constants.SUFFIX_BEAN_QUERY);

        readFieldInfo(tableInfo);
        getKeyIndexInfo(tableInfo);

      }

    } catch (Exception e) {
      log.error("创建表失败");
    } finally {
      if (tableResult != null) {
        try {
          tableResult.close();
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
      }
      if (ps != null) {
        try {
          ps.close();
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
      }
      if (conn != null) {
        try {
          conn.close();
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
      }
    }
    return tableInfoList;
  }

  private static List<FieldInfo> readFieldInfo(TableInfo tableInfo) {
    PreparedStatement ps = null;
    ResultSet fieldResult = null;

    List<FieldInfo> fieldInfoList = new ArrayList<>();
    List<FieldInfo> fieldExtendList = new ArrayList<>();
    try {
      ps = conn.prepareStatement(String.format(SQL_SHOW_TABLE_FIELDS, tableInfo.getTableName()));
      fieldResult = ps.executeQuery();

      while (fieldResult.next()) {
        String field = fieldResult.getString("field");
        String type = fieldResult.getString("type");
        String extra = fieldResult.getString("extra");
        String comment = fieldResult.getString("comment");

        if (type.indexOf("(") > 0) {
          type = type.substring(0, type.indexOf("("));
        }
        FieldInfo fieldInfo = new FieldInfo();
        fieldInfoList.add(fieldInfo);
        fieldInfo.setFieldName(field);
        String propertyName = processFiled(field, false);
        fieldInfo.setComment(comment);
        fieldInfo.setSqlType(type);
        fieldInfo.setIsAutoIncrement("auto_increment".equalsIgnoreCase(extra) ? true : false);
        fieldInfo.setPropertyName(propertyName);
        fieldInfo.setJavaType(processJavaType(type));

        if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, type)) {
          tableInfo.setHaveDateTime(true);
        }
        if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, type)) {
          tableInfo.setHaveDate(true);
        }
        if (ArrayUtils.contains(Constants.SQL_DECIMAL_TYPE, type)) {
          tableInfo.setHaveBigDecimal(true);
        }

        if (ArrayUtils.contains(Constants.SQL_STRING_TYPE, type)) {
          FieldInfo fuzzyField = new FieldInfo();
          fuzzyField.setJavaType(fieldInfo.getJavaType());
          fuzzyField.setPropertyName(propertyName + Constants.SUFFIX_BEAN_QUERY_FUZZY);
          fuzzyField.setFieldName(fieldInfo.getFieldName());
          fieldExtendList.add(fuzzyField);
        }

        if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, type) || ArrayUtils.contains(
            Constants.SQL_DATE_TIME_TYPES, type)) {

          FieldInfo timeStartField = new FieldInfo();
          timeStartField.setJavaType("String");
          timeStartField.setPropertyName(
              fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_START);
          timeStartField.setFieldName(fieldInfo.getFieldName());
          fieldExtendList.add(timeStartField);

          FieldInfo timeEndField = new FieldInfo();
          timeEndField.setJavaType("String");
          timeEndField.setPropertyName(
              fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_END);
          timeEndField.setFieldName(fieldInfo.getFieldName());
          fieldExtendList.add(timeEndField);
        }
      }
      tableInfo.setFieldList(fieldInfoList);
      tableInfo.setFieldExtendList(fieldExtendList);
      log.info("tableInfo: {}", JsonUtils.convertObj2Json(tableInfo));

    } catch (Exception e) {
      log.error("读取表失败");
    } finally {
      if (fieldResult != null) {
        try {
          fieldResult.close();
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
      }
      if (ps != null) {
        try {
          ps.close();
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
      }
    }
    return fieldInfoList;
  }

  private static void getKeyIndexInfo(TableInfo tableInfo) {
    PreparedStatement ps = null;
    ResultSet fieldResult = null;

    try {

      Map<String, FieldInfo> tempMap = new HashMap<>();
      for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
        tempMap.put(fieldInfo.getFieldName(), fieldInfo);
      }
      ps = conn.prepareStatement(String.format(SQL_SHOW_TABLE_INDEX, tableInfo.getTableName()));
      fieldResult = ps.executeQuery();
      while (fieldResult.next()) {
        String keyName = fieldResult.getString("Key_name");
        Integer nonUnique = fieldResult.getInt("Non_unique");
        String columnName = fieldResult.getString("column_name");

        if (nonUnique == 1) {
          continue;
        }

        List<FieldInfo> keyFieldList = tableInfo.getKeyIndexMap().get(keyName);
        if (keyFieldList == null) {
          keyFieldList = new ArrayList<>();
          tableInfo.getKeyIndexMap().put(keyName, keyFieldList);
        }
        keyFieldList.add(tempMap.get(columnName));

      }

    } catch (Exception e) {
      log.error("读取索引失败");
    } finally {
      if (fieldResult != null) {
        try {
          fieldResult.close();
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
      }
      if (ps != null) {
        try {
          ps.close();
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }


  /**
   * 将tb_product_info 转换为 productInfo
   *
   * @param filed
   * @param uperCaseFirstletter 是否忽略 前缀
   * @return
   */
  private static String processFiled(String filed, boolean uperCaseFirstletter) {
    StringBuilder sb = new StringBuilder();
    String[] fileds = filed.split("_");
    sb.append(uperCaseFirstletter ? StringUtils.uperCaseFirstLetter(fileds[0]) : fileds[0]);
    for (int i = 1, len = fileds.length; i < len; i++) {
      sb.append(StringUtils.uperCaseFirstLetter(fileds[i]));
    }
    return sb.toString();
  }


  private static String processJavaType(String type) {
    if (ArrayUtils.contains(Constants.SQL_INTEGER_TYPE, type)) {
      return "Integer";
    } else if (ArrayUtils.contains(Constants.SQL_LONG_TYPE, type)) {
      return "Long";
    } else if (ArrayUtils.contains(Constants.SQL_STRING_TYPE, type)) {
      return "String";
    } else if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, type) || ArrayUtils.contains(
        Constants.SQL_DATE_TYPES, type)) {
      return "Date";
    } else if (ArrayUtils.contains(Constants.SQL_DECIMAL_TYPE, type)) {
      return "BigDecimal";
    } else {
      throw new RuntimeException("无法识别的类型: " + type);
    }
  }

}