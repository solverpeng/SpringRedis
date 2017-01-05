package com.solverpeng.util;

import org.apache.commons.lang3.StringUtils;


public class Utils {
    //特殊字符的正则
    public static String regex = "[<>]";

    /**
     * 判断参数中是否有特殊字符 。包含特殊字符返回true
     */
    public static boolean checkInvalidParam(String param) {
        int beginLength = param.trim().length();
        int endLength = param.trim().replaceAll(regex, "").trim().length();
        return beginLength == endLength ? false : true;
    }


    public static boolean isNumber(String s) {
        if (StringUtils.isNotBlank(s))
            return s.matches("[\\d.]+");
        return false;
    }

}
