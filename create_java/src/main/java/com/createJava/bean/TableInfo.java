package com.createJava.bean;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * @author moZiA
 * @date 2025/3/22 17:52
 * @description
 */
@Data
public class TableInfo {

  //表名
  private String tableName;

  //bean 名称
  private String beanName;

  //参数名称
  private String beanParamName;

  //表注释
  private String comment;

  //字段信息
  private List<FieldInfo> fieldList;

  // 扩展字段信息
  private List<FieldInfo> fieldExtendList;

  //唯一索引集合
  private Map<String, List<FieldInfo>> keyIndexMap = new LinkedHashMap<>();

  //是否有data类型
  private Boolean haveDate = false;

  //是否有时间类型
  private Boolean haveDateTime = false;

  //是否 有 bigdecimal类型
  private Boolean haveBigDecimal = false;


}