package com.createJava;


import com.createJava.bean.TableInfo;
import com.createJava.builder.BuildBase;
import com.createJava.builder.BuildMapper;
import com.createJava.builder.BuildMapperXml;
import com.createJava.builder.BuildPo;
import com.createJava.builder.BuildQuery;
import com.createJava.builder.BuilderTable;
import com.createJava.utils.JsonUtils;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author moZiA
 * @date 2025/3/22 17:37
 * @description
 */
public class Main {

  private static final Logger log = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {

    long start = System.currentTimeMillis();

    //构建表
    List<TableInfo> tableInfoList = BuilderTable.getTables();

    BuildBase.execute();

    for (TableInfo tableInfo : tableInfoList) {
      //构建pojo
      BuildPo.execute(tableInfo);
      //构建query
      BuildQuery.execute(tableInfo);
      //构建mapper
      BuildMapper.execute(tableInfo);
      //构建xml
      BuildMapperXml.execute(tableInfo);
    }

    long end = System.currentTimeMillis();

    log.info("程序总运行时长: {}", start - end);


  }
}
