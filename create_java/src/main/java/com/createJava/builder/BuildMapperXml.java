package com.createJava.builder;


import com.createJava.bean.Constants;
import com.createJava.bean.FieldInfo;
import com.createJava.bean.TableInfo;
import com.createJava.utils.IOUtils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author moZiA
 * @date 2025/3/26 13:39
 * @description
 */
public class BuildMapperXml {

  private static final Logger log = LoggerFactory.getLogger(BuildMapperXml.class);

  private static final String BASE_COLUMN_LIST = "base_column_list";
  private static final String BASE_QUERY_CONDITION = "base_query_condition";
  private static final String QUERY_CONDITION = "query_condition";


  public static void execute(TableInfo tableInfo) {
    File folder = new File(Constants.PATH_MAPPERS_XMLS);
    if (!folder.exists()) {
      folder.mkdirs();
    }

    String className = tableInfo.getBeanName() + Constants.SUFFIX_MAPPERS;
    File poFile = new File(folder, className + ".xml");

    OutputStream out = null;
    OutputStreamWriter outw = null;
    BufferedWriter bw = null;
    try {

      out = new FileOutputStream(poFile);
      outw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
      bw = new BufferedWriter(outw);

      bw.write("""
          <?xml version="1.0" encoding="UTF-8"?>
          <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
            "http://mybatis.org/dtd/mybatis-3-mapper.dtd">""");
      bw.newLine();

      bw.write("<mapper namespace=\"" + Constants.PACKAGE_MAPPERS + "." + className + "\">");
      bw.newLine();
      // 生成 map 映射
      buildResultMap(bw, tableInfo);
      // 通用查询sql
      buildGeneralQuery(bw, tableInfo);
      // 基础查询条件
      buildBasicQueryCondition(bw, tableInfo);
      // 扩展查询条件
      buildQueryExtendCondition(bw, tableInfo);
      // 生成
      bw.write("</mapper>");
      bw.flush();

    } catch (Exception e) {
      log.error("创建mapper xml失败", e);
    } finally {
      IOUtils.closeQuietly(bw, outw, out);
    }

  }

  /**
   * 生成 map 映射
   * <p>
   * * @author MOZI
   */
  private static void buildResultMap(BufferedWriter bw, TableInfo tableInfo) throws IOException {

    //生成 map 映射
    bw.write("\t<!--实体映射-->");
    bw.newLine();
    String poClass = Constants.PACKAGE_PO + "." + tableInfo.getBeanName();
    bw.write("\t<resultMap id=\"base_result_map\" type=\"" + poClass + "\">");
    bw.newLine();

    FieldInfo idField = null;
    Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
    for (Entry<String, List<FieldInfo>> stringListEntry : keyIndexMap.entrySet()) {
      if ("PRIMARY".equals(stringListEntry.getKey())) {
        List<FieldInfo> fieldInfoList = stringListEntry.getValue();
        if (fieldInfoList.size() == 1) {
          idField = fieldInfoList.get(0);
          break;
        }
      }
    }

    for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
      bw.write("\t\t<!-- " + fieldInfo.getComment() + " -->");
      bw.newLine();
      String key = "";
      if (idField != null && fieldInfo.getPropertyName().equals(idField.getPropertyName())) {
        key = "id";
      } else {
        key = "result";
      }

      bw.write("\t\t<" + key + " column=\"" + fieldInfo.getFieldName() + "\" property=\""
          + fieldInfo.getPropertyName() + "\"/>");
      bw.newLine();
    }
    bw.write("\t</resultMap>");
    bw.newLine();

  }


  /**
   * 生成通用查询sql
   *
   * @param
   * @return
   * @author MOZI
   */
  private static void buildGeneralQuery(BufferedWriter bw, TableInfo tableInfo) throws IOException {

    bw.newLine();
    bw.write("\t<!--通用查询-->");
    bw.newLine();
    bw.write("\t<sql id=\"" + BASE_COLUMN_LIST + "\">");
    bw.newLine();
    StringBuilder sqlBuilder = new StringBuilder();
    for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
      sqlBuilder.append(fieldInfo.getFieldName()).append(",");
    }
    sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
    bw.write("\t\t" + sqlBuilder.toString());
    bw.newLine();
    bw.write("\t</sql>");
    bw.newLine();
  }

  /**
   * 生成通用查询条件
   *
   * @author MOZI
   */
  private static void buildBasicQueryCondition(BufferedWriter bw, TableInfo tableInfo)
      throws IOException {
    bw.newLine();
    bw.write("\t<!--基础查询条件-->");
    bw.newLine();
    bw.write("\t<sql id=\"" + BASE_QUERY_CONDITION + "\">");
    bw.newLine();
    String srtingQuery = "";
    for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
      if (ArrayUtils.contains(Constants.SQL_STRING_TYPE, fieldInfo.getSqlType())) {
        srtingQuery = " and query." + fieldInfo.getPropertyName() + " != ''";
      }
      bw.write(
          "\t\t<if test=\"query." + fieldInfo.getPropertyName() + " != null" + srtingQuery + "\">");
      bw.newLine();
      bw.write("\t\t\tand " + fieldInfo.getFieldName() + " = #{query." + fieldInfo.getPropertyName()
          + "}");
      bw.newLine();
      bw.write("\t\t</if>");
      bw.newLine();
    }
    bw.newLine();
    bw.write("\t</sql>");
    bw.newLine();
  }

  private static void buildQueryExtendCondition(BufferedWriter bw, TableInfo tableInfo)
      throws IOException {
    bw.newLine();
    bw.write("\t<!--扩展查询条件-->");
    bw.newLine();
    bw.write("\t<sql id=\"" + QUERY_CONDITION + "\">");
    bw.newLine();
    String srtingQuery = "";
    for (FieldInfo fieldInfo : tableInfo.getFieldExtendList()) {
      if (ArrayUtils.contains(Constants.SQL_STRING_TYPE, fieldInfo.getSqlType())) {
        srtingQuery = " and query." + fieldInfo.getPropertyName() + " != ''";
      }
      bw.write("\t\t<if test=\"query." + fieldInfo.getPropertyName() + " != null and query."
          + fieldInfo.getPropertyName() + "!= ''\">");
      bw.newLine();
      bw.write("\t\t\tand " + fieldInfo.getFieldName() + " = #{query." + fieldInfo.getPropertyName()
          + "}");
      bw.newLine();
      bw.write("\t\t</if>");
      bw.newLine();
    }
    bw.newLine();
    bw.write("\t</sql>");
    bw.newLine();
  }

}