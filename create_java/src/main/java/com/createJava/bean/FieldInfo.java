package com.createJava.bean;


import lombok.Data;

/**
 * @author moZiA
 * @date 2025/3/22 18:00
 * @description
 */
@Data
public class FieldInfo {

  //字段名称
  private String FieldName;

  //bean 名称
  private String propertyName;

  private String sqlType;

  //字段类型
  private String javaType;

  //字段备注
  private String comment;

  //字段是否为自增

  private Boolean isAutoIncrement;


}