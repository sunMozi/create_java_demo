package com.createJava.builder;


import com.createJava.bean.Constants;
import com.createJava.bean.FieldInfo;
import com.createJava.bean.TableInfo;
import com.createJava.utils.StringUtils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author moZiA
 * @date 2025/3/24 13:50
 * @description
 */
public class BuildQuery {

  private static final Logger log = LoggerFactory.getLogger(BuildQuery.class);

  public static void execute(TableInfo tableInfo) {
    File folder = new File(Constants.PATH_QUERY);
    if (!folder.exists()) {
      folder.mkdirs();
    }

    String className = tableInfo.getBeanName() + Constants.SUFFIX_BEAN_QUERY;

    File poFile = new File(folder, className + ".java");

    OutputStream out = null;
    OutputStreamWriter outw = null;
    BufferedWriter bw = null;
    try {

      out = new FileOutputStream(poFile);
      outw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
      bw = new BufferedWriter(outw);

      /// 导包
      bw.write("package " + Constants.PACKAGE_QUERY + ";");
      bw.newLine();

      if (tableInfo.getHaveBigDecimal()) {
        bw.newLine();
        bw.write("import java.math.BigDecimal;");
        bw.newLine();
      }

      if (tableInfo.getHaveDate() || tableInfo.getHaveDateTime()) {
        bw.write("import java.util.Date;");
        bw.newLine();
      }

      bw.newLine();
      BuildComment.createClassComment(bw, tableInfo.getComment() + "查询对象");
      bw.newLine();
      bw.write("public class " + className + " {");
      bw.newLine();
      bw.newLine();


      //属性生成
      for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
        BuildComment.createFieldComment(bw, fieldInfo.getComment());
        bw.newLine();
        bw.write("\tprivate " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ";");
        bw.newLine();
        bw.newLine();

        //String
        if (ArrayUtils.contains(Constants.SQL_STRING_TYPE, fieldInfo.getSqlType())) {
          String propertyName = fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_FUZZY;
          bw.write("\tprivate " + fieldInfo.getJavaType() + " " + propertyName + ";");
          bw.newLine();
          bw.newLine();
        }
        // date
        if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, fieldInfo.getSqlType())
            || ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, fieldInfo.getSqlType())) {
          bw.write("\tprivate String " + fieldInfo.getPropertyName()
              + Constants.SUFFIX_BEAN_QUERY_TIME_START + ";");
          bw.newLine();
          bw.newLine();
          bw.write("\tprivate String " + fieldInfo.getPropertyName()
              + Constants.SUFFIX_BEAN_QUERY_TIME_END + ";");
          bw.newLine();
          bw.newLine();
        }


      }
      //get set 方法

      buildGetSet(bw, tableInfo.getFieldList());
      buildGetSet(bw, tableInfo.getFieldExtendList());

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

  private static void buildGetSet(BufferedWriter bw, List<FieldInfo> fieldInfoList)
      throws IOException {
    for (FieldInfo fieldInfo : fieldInfoList) {
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
  }

}