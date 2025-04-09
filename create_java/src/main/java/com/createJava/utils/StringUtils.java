package com.createJava.utils;


/**
 * @author moZiA
 * @date 2025/3/23 10:38
 * @description
 */
public class StringUtils {


  /**
   * 将单词 首字母 转换为大写
   *
   * @param fileld
   * @return
   */
  public static String uperCaseFirstLetter(String fileld) {
    if (org.apache.commons.lang3.StringUtils.isEmpty(fileld)) {
      return fileld;
    }
    return fileld.substring(0, 1).toUpperCase() + fileld.substring(1);
  }


  /**
   * 将单词 首字母 转换为小写
   *
   * @param fileld
   * @return
   */
  public static String lowerCaseFirstLetter(String fileld) {
    if (org.apache.commons.lang3.StringUtils.isEmpty(fileld)) {
      return fileld;
    }
    return fileld.substring(0, 1).toLowerCase() + fileld.substring(1);
  }


}