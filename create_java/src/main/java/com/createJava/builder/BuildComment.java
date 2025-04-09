package com.createJava.builder;


import com.createJava.bean.Constants;
import com.createJava.utils.DataUtils;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Date;

/**
 * @author moZiA
 * @date 2025/3/24 15:38
 * @description
 */
public class BuildComment {

  public static void createClassComment(BufferedWriter bw, String classComment) throws IOException {
    bw.write("/**");
    bw.newLine();
    bw.write(" * @author: " + Constants.AUTHOR_COMMENT);
    bw.newLine();
    bw.write(" * @date: " + DataUtils.format(new Date(), DataUtils.YYYY_M_D_HH_MM));
    bw.newLine();
    bw.write(" * @description: " + classComment);
    bw.newLine();
    bw.write(" */");
  }

  public static void createFieldComment(BufferedWriter bw, String fieldComment) throws IOException {
    if (fieldComment == null) {
      fieldComment = "";
    }
    bw.write("\t/**");
    bw.newLine();
    bw.write("\t * " + fieldComment);
    bw.newLine();
    bw.write(" \t */");
  }

  public static void createMethodComment() {

  }

}