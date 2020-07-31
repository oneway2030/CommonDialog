package com.oneway.dialoglib.util;

import android.content.Context;

public class ResourcesUtils {

    /**
     * 获得字符串
     *
     * @param strId 字符串id
     * @return 字符串
     */
    public static String getString(Context context, int strId) {
        return context.getResources().getString(strId);
    }

    /**
     * 获得颜色
     *
     * @param colorId 颜色id
     * @return 颜色
     */
    public static int getColor(Context context, int colorId) {
        return context.getResources().getColor(colorId);
    }
}
