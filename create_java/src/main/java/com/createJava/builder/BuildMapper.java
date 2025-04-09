package com.createJava.builder;


import com.createJava.bean.Constants;
import com.createJava.bean.FieldInfo;
import com.createJava.bean.TableInfo;
import com.createJava.utils.IOUtils;
import com.createJava.utils.StringUtils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author moZiA
 * @date 2025/3/25 15:59
 * @description
 */
public class BuildMapper {

  private static final Logger log = LoggerFactory.getLogger(BuildMapper.class);

  public static void execute(TableInfo tableInfo) {
    File folder = new File(Constants.PATH_MAPPERS);
    if (!folder.exists()) {
      folder.mkdirs();
    }

    String className = tableInfo.getBeanName() + Constants.SUFFIX_MAPPERS;
    File poFile = new File(folder, className + ".java");

    OutputStream out = null;
    OutputStreamWriter outw = null;
    BufferedWriter bw = null;
    try {

      out = new FileOutputStream(poFile);
      outw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
      bw = new BufferedWriter(outw);

      // 导包
      bw.write("package " + Constants.PACKAGE_MAPPERS + ";");
      bw.newLine();
      bw.newLine();
      bw.write("import org.apache.ibatis.annotations.Param;");

      bw.newLine();
      bw.newLine();
      BuildComment.createClassComment(bw, tableInfo.getComment() + "mapper");
      bw.newLine();
      bw.write("public interface " + className + "<T, P>" + " extends BaseMapper {");
      bw.newLine();
      bw.newLine();

      Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();

      for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
        List<FieldInfo> keyFieldInfoList = entry.getValue();
        Integer index = 0;
        StringBuilder methodName = new StringBuilder();
        StringBuilder methodParams = new StringBuilder();
        for (FieldInfo fieldInfo : keyFieldInfoList) {
          index++;
          methodName.append(StringUtils.uperCaseFirstLetter(fieldInfo.getPropertyName()));
          if (index < keyFieldInfoList.size()) {
            methodName.append("And");
          }
          methodParams.append(
              "@Param(\"" + fieldInfo.getPropertyName() + "\") " + fieldInfo.getJavaType() + " "
                  + fieldInfo.getPropertyName());
          if (index < keyFieldInfoList.size()) {
            methodParams.append(", ");
          }
        }

        BuildComment.createFieldComment(bw, "根据" + methodName + "查询");
        bw.newLine();
        bw.write("\tT selectBy" + methodName + "(" + methodParams + ");");
        bw.newLine();
        bw.newLine();

        BuildComment.createFieldComment(bw, "根据" + methodName + "更新");
        bw.newLine();
        bw.write(
            "\tInteger updateBy" + methodName + "(@Param(\"bean\") T t, " + methodParams + ");");
        bw.newLine();
        bw.newLine();

        BuildComment.createFieldComment(bw, "根据" + methodName + "删除");
        bw.newLine();
        bw.write("\tInteger deleteBy" + methodName + "(" + methodParams + ");");
        bw.newLine();
        bw.newLine();

      }

      bw.write("}");
      bw.flush();

    } catch (Exception e) {
      log.error("创建mappers失败", e);
    } finally {
      IOUtils.closeQuietly(bw, outw, out);
    }

  }

}