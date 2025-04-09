package com.createJava.utils;


import java.io.Closeable;
import java.io.IOException;

/**
 * @author moZiA
 * @date 2025/3/25 12:51
 * @description
 */
public class IOUtils {

  public static void closeQuietly(Closeable... closeables) {
    for (Closeable c : closeables) {
      try {
        if (c != null) {
          c.close();
        }
      } catch (IOException ignored) {
        // 可选：记录日志
      }
    }
  }

}