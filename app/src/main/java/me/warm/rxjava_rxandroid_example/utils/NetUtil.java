package me.warm.rxjava_rxandroid_example.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 作者: 51hs_android
 * 时间: 2017/2/8
 * 简介: 通用工具类
 */
public class NetUtil {

  /**
   * 检查是否有网络
   */
  public static boolean isNetworkAvailable(Context context) {

    NetworkInfo info = getNetworkInfo(context);
    return info != null && info.isAvailable();
  }


  /**
   * 检查是否是WIFI
   */
  public static boolean isWifi(Context context) {

    NetworkInfo info = getNetworkInfo(context);
    if (info != null) {
      if (info.getType() == ConnectivityManager.TYPE_WIFI) {
        return true;
      }
    }
    return false;
  }


  /**
   * 检查是否是移动网络
   */
  public static boolean isMobile(Context context) {

    NetworkInfo info = getNetworkInfo(context);
    if (info != null) {
      if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
        return true;
      }
    }
    return false;
  }


  private static NetworkInfo getNetworkInfo(Context context) {

    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
        Context.CONNECTIVITY_SERVICE);
    return cm.getActiveNetworkInfo();
  }


}
