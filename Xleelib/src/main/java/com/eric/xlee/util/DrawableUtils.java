package com.eric.xlee.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

public class DrawableUtils {

    /**
     * @param @param ctx
     * @param @param bgColor 背景颜色
     * @param @param roundRadius 圆角的大小
     * @return GradientDrawable 返回类型
     * @throws
     * @Description: TODO(等同在xml中定义shape)
     */
    public static GradientDrawable getGradientDrawable(Context ctx, String bgColor, int roundRadius) {
        return getGradientDrawable(ctx, bgColor, roundRadius, "#00000000", 0);
    }

    /**
     * @param @param  ctx
     * @param @param  bgColor 背景颜色
     * @param @param  roundRadius 圆角的大小
     * @param @param  strokeColor 边界的颜色值
     * @param @param  strokeWidth 边界的宽度
     * @param @return 设定文件
     * @return GradientDrawable 返回类型
     * @throws
     * @Description: TODO(等同在xml中定义shape)
     */
    public static GradientDrawable getGradientDrawable(Context ctx, String bgColor, int roundRadius,
                                                       String strokeColor, int strokeWidth) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.parseColor(bgColor));
        gd.setCornerRadius(ScreenParameter.getFitSize(ctx, roundRadius));
        gd.setStroke(ScreenParameter.getFitSize(ctx, strokeWidth), Color.parseColor(strokeColor));
        return gd;
    }

    public static GradientDrawable getNomalDrawable(Context ctx, String bgColor) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.parseColor(bgColor));
        int radSize = ScreenParameter.getFitSize(ctx, 10);
        float rads[] = new float[]{radSize, radSize, radSize, radSize, radSize, radSize, radSize, radSize};
        gd.setCornerRadii(rads);
        return gd;
    }

    public static GradientDrawable getToastDrawable(Context ctx) {
        GradientDrawable gd = getNomalDrawable(ctx, "#90000000");
        gd.setStroke(ScreenParameter.getFitSize(ctx, 1), Color.parseColor("#ffffffff"));
        return gd;
    }
}
