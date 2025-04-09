package com.createJava.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author moZiA
 * @date 2025/3/24 10:06
 * @description
 */
public class JsonUtils {

  private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);

  public static String convertObj2Json(Object o) {
    if (o == null) {
      return null;
    }
    return JSON.toJSONString(o, SerializerFeature.DisableCircularReferenceDetect);
  }

}