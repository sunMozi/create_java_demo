package com.createJava.builder;


import com.createJava.bean.Constants;
import com.createJava.bean.FieldInfo;
import com.createJava.bean.TableInfo;
import com.createJava.utils.DataUtils;
import com.createJava.utils.StringUtils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author moZiA
 * @date 2025/3/24 13:50
 * @description
 */
public class BuildPo {

  private static final Logger log = LoggerFactory.getLogger(BuildPo.class);

  public static void execute(TableInfo tableInfo) {
    File folder = new File(Constants.PATH_PO);
    if (!folder.exists()) {
      folder.mkdirs();
    }

    File poFile = new File(folder, tableInfo.getBeanName() + ".java");

    OutputStream out = null;
    OutputStreamWriter outw = null;
    BufferedWriter bw = null;
    try {

      out = new FileOutputStream(poFile);
      outw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
      bw = new BufferedWriter(outw);

      /// 导包
      bw.write("package " + Constants.PACKAGE_PO + ";");
      bw.newLine();
      bw.newLine();
      bw.write("import java.io.Serializable;");

      if (tableInfo.getHaveBigDecimal()) {
        bw.newLine();
        bw.write("import java.math.BigDecimal;");
        bw.newLine();
      }

      if (tableInfo.getHaveDate() || tableInfo.getHaveDateTime()) {
        bw.write("import java.util.Date;");
        bw.newLine();
        bw.write(Constants.BEAN_DATE_FORMAT_CLASS);
        bw.newLine();
        bw.write(Constants.BEAN_DATE_UNFORMAT_CLASS);
        bw.newLine();
        bw.write("import " + Constants.PACKAGE_ENUMS + ".DateTimePatternEnum;");
        bw.newLine();
        bw.write("import " + Constants.PACKAGE_UTILS + ".DateUtils;");
        bw.newLine();
      }

      for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
        if (ArrayUtils.contains(Constants.IGNORE_BEAN_TOJSON_FILED.split(","),
            fieldInfo.getPropertyName())) {
          bw.write(Constants.IGNORE_BEAN_TOJSON_CLASS);
          bw.newLine();
          break;
        }
      }

      bw.newLine();
      BuildComment.createClassComment(bw, tableInfo.getComment());
      bw.newLine();
      bw.write("public class " + tableInfo.getBeanName() + " implements Serializable {");
      bw.newLine();
      bw.newLine();

      //属性生成
      for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
        BuildComment.createFieldComment(bw, fieldInfo.getComment());
        bw.newLine();

        if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, fieldInfo.getSqlType())) {
          bw.write("\t" + String.format(Constants.BEAN_DATE_FORMAT_EXPRESION,
              DataUtils.YYYY_MM_DD_HH_MM_SS));
          bw.newLine();

          bw.write("\t" + String.format(Constants.BEAN_DATE_UNFORMAT_EXPRESION,
              DataUtils.YYYY_MM_DD_HH_MM_SS));
          bw.newLine();
        }

        if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, fieldInfo.getSqlType())) {
          bw.write(
              "\t" + String.format(Constants.BEAN_DATE_FORMAT_EXPRESION, DataUtils.YYYY_MM_DD));
          bw.newLine();
          bw.write(
              "\t" + String.format(Constants.BEAN_DATE_UNFORMAT_EXPRESION, DataUtils.YYYY_MM_DD));
          bw.newLine();
        }

        if (ArrayUtils.contains(Constants.IGNORE_BEAN_TOJSON_FILED.split(","),
            fieldInfo.getPropertyName())) {
          bw.write("\t" + String.format(Constants.IGNORE_BEAN_TOJSON_EXPRESION));
          bw.newLine();
        }

        bw.write("\tprivate " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ";");
        bw.newLine();
        bw.newLine();
      }
      //get set 方法
      for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
        String tempField = StringUtils.uperCaseFirstLetter(fieldInfo.getPropertyName());
        //set 方法
        bw.write("\tpublic void set" + tempField + "(" + fieldInfo.getJavaType() + " "
            + fieldInfo.getPropertyName() + ") {");
        bw.newLine();
        bw.write(
            "\t\tthis." + fieldInfo.getPropertyName() + " = " + fieldInfo.getPropertyName() + ";");
        bw.newLine();
        bw.write("\t}");
        bw.newLine();
        bw.newLine();
        //get方法
        bw.write("\tpublic " + fieldInfo.getJavaType() + " get" + tempField + "() {");
        bw.newLine();
        bw.write("\t\treturn this." + fieldInfo.getPropertyName() + ";");
        bw.newLine();
        bw.write("\t}");
        bw.newLine();
        bw.newLine();
      }

      // 重写toString
      StringBuilder toStringBuilder = new StringBuilder();
      Integer index = 0;

      for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
        index++;

        String properName = fieldInfo.getPropertyName();

        // 处理日期类型字段
        if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, fieldInfo.getSqlType())) {
          properName = "DateUtils.format(" + properName
              + ", DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())";
        } else if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, fieldInfo.getSqlType())) {
          properName =
              "DateUtils.format(" + properName + ", DateTimePatternEnum.YYYY_MM_DD.getPattern())";
        }

        // 拼接字段信息
        toStringBuilder.append(fieldInfo.getComment()).append(":\" + (").append(properName)
            .append(" == null ? \"空\" : ").append(properName).append(")");

        // 如果不是最后一个字段，添加分隔符
        if (index < tableInfo.getFieldList().size()) {
          toStringBuilder.append(" + \",");
        }
      }

      String toStringStr = toStringBuilder.toString();
      String result = toStringStr.substring(0, toStringStr.length());

      bw.write("\t@Override");
      bw.newLine();
      bw.write("\tpublic String toString() {");
      bw.newLine();
      bw.write("\t\treturn \"" + result + ";");
      bw.newLine();
      bw.write("\t}");
      bw.newLine();

      bw.write("}");
      bw.flush();

    } catch (Exception e) {
      log.error("创建po失败", e);
    } finally {
      if (bw != null) {
        try {
          bw.close();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
      if (outw != null) {
        try {
          outw.close();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
      if (out != null) {
        try {
          out.close();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    }

  }

}