package com.createJava.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author moZiA
 * @date 2025/3/24 16:05
 * @description
 */
public class DataUtils {

  // 日期格式常量
  public static final String YYYY_MM_DD = "yyyy-MM-dd";
  public static final String _YYYYMMDD = "yyyy/MM/dd";
  public static final String YYYYMMDD = "yyyyMMdd";

  public static final String YYYY_M_D_HH_MM = "yyyy/M/d HH:mm";

  public static final String YYYY_MM_DD_HH_MM = "yyyy/MM/dd HH:mm";
  public static final String YYYY_MM_DD_HH_MM_SS = "yyyy/MM/dd HH:mm:ss";

  /**
   * 格式化日期
   *
   * @param date    日期对象
   * @param pattern 格式模式
   * @return 格式化后的字符串
   */
  public static String format(Date date, String pattern) {
    return new SimpleDateFormat(pattern).format(date);
  }

  /**
   * 解析字符串为日期对象
   *
   * @param dateStr 日期字符串
   * @param pattern 格式模式
   * @return 解析后的日期对象
   */
  public static Date parse(String dateStr, String pattern) {
    try {
      return new SimpleDateFormat(pattern).parse(dateStr);
    } catch (ParseException e) {
      throw new RuntimeException("日期解析失败: " + dateStr, e);
    }
  }


}