package org.chinasafety.cqj.anfutong.utils;

/**
 * Created by mini on 17/5/21.
 */

public class StringUtils {

    public static String noNull(Object obj) {
        if (obj == null) {
            return "";
        } else {
            return obj.toString();
        }
    }

    public static String integerNoNull(Object obj) {
        if (obj == null) {
            return "0";
        } else {
            return obj.toString();
        }
    }

}
