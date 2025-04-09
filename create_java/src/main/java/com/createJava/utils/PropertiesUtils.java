package com.createJava.utils;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.util.PropertiesUtil;

/**
 * @author moZiA
 * @date 2025/3/22 17:10
 * @description
 */
public class PropertiesUtils {

  private static Properties props = new Properties();
  private static Map<String, String> PROPERMAP = new ConcurrentHashMap<>();

  static {
    InputStream is = null;
    try {
      is = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties");
      props.load(new InputStreamReader(is, StandardCharsets.UTF_8));
      props.load(is);
      Iterator<Object> iterator = props.keySet().iterator();
      while (iterator.hasNext()) {
        String key = (String) iterator.next();
        PROPERMAP.put(key, props.getProperty(key));
      }
    } catch (Exception e) {

    } finally {
      if (is != null) {
        try {
          is.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public static String getString(String key) {
    return PROPERMAP.get(key);
  }


}