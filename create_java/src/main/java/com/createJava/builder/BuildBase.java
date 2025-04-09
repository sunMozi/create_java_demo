package com.createJava.builder;


import com.createJava.bean.Constants;
import com.createJava.utils.IOUtils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author moZiA
 * @date 2025/3/25 11:58
 * @description
 */
public class BuildBase {

  private static Logger log = LoggerFactory.getLogger(BuildBase.class);

  public static void execute() {
    List<String> headInfoList = new ArrayList<>();

    //生成 date Enum

    headInfoList.add("package " + Constants.PACKAGE_ENUMS);
    BuildBase.build(headInfoList, "DateTimePatternEnum", Constants.PATH_ENUMS);

    headInfoList.clear();

    headInfoList.add("package " + Constants.PACKAGE_UTILS);
    BuildBase.build(headInfoList, "DateUtils", Constants.PATH_UTILS);

    headInfoList.clear();
    headInfoList.add("package " + Constants.PACKAGE_MAPPERS);
    BuildBase.build(headInfoList, "BaseMapper", Constants.PATH_MAPPERS);

  }

  public static void build(List<String> headInfoList, String fileName, String outPutPath) {
    File folder = new File(outPutPath);
    if (!folder.exists()) {
      folder.mkdirs();
    }

    File javaFile = new File(outPutPath, fileName + ".java");

    OutputStream out = null;
    OutputStreamWriter outw = null;
    BufferedWriter bw = null;

    InputStream in = null;
    InputStreamReader inr = null;
    BufferedReader bf = null;

    try {
      out = new FileOutputStream(javaFile);
      outw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
      bw = new BufferedWriter(outw);

      String templatePath = BuildBase.class.getClassLoader()
          .getResource("template/" + fileName + ".txt").getPath();

      in = new FileInputStream(templatePath);
      inr = new InputStreamReader(in, StandardCharsets.UTF_8);
      bf = new BufferedReader(inr);

      for (String head : headInfoList) {
        bw.write(head + ";");
        bw.newLine();
      }

      String lineInfo = null;
      while ((lineInfo = bf.readLine()) != null) {
        bw.write(lineInfo);
        bw.newLine();
      }
      bw.flush();

    } catch (Exception e) {
      log.error("生成基础类: {} 失败:", fileName, e);
    } finally {
      IOUtils.closeQuietly(bf, inr, in, bw, outw, out);
    }
  }


}