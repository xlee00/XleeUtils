package com.eric.xlee.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.lang.reflect.Method;
import java.math.BigDecimal;

/**
 * Created by xlee on 20/12/15.
 *
 * @Description: TODO(获取屏幕的一些参数)
 */
public class ScreenParameter {

    private static final float sDefaultWidthTV = 1280.0f;
    private static final float sDefaultHeightTV = 720.0f;
    private static final float sDefaultWidthPhone = 320.0f;
    private static final float sDefaultHeightPhone = 480.0f;

    private static float sDefaultWidth = sDefaultWidthTV;
    private static float sDefaultHeight = sDefaultHeightTV;

    private static float sDensity;
    private static int sWidth, sHeight;
    private static int sDensityDpi;
    private static float sRatioX = 0;
    private static float sRatioY = 0;
    private static float sRadtio;

    private static boolean isVertical = false;

    /**
     * @throws
     * @Title: setVerticalMode
     * @Description: TODO(设置是否为手机版适配模式 ，true是竖屏模式，false为横屏模式 ，默认为横屏模式。)
     */
    public static void setVerticalMode(boolean isVertical) {
        ScreenParameter.isVertical = isVertical;
    }


    public static int getScreenHeight(Context context) {
        if (sHeight == 0) {
            synchronized (ScreenParameter.class) {
                if (sHeight == 0) {
                    return getDisplaySize(context, true);
                }
            }
        }
        return sHeight;
    }

    public static int getScreenWidth(Context context) {
        if (sWidth == 0) {
            synchronized (ScreenParameter.class) {
                if (sWidth == 0) {
                    return getDisplaySize(context, false);
                }
            }
        }
        return sWidth;
    }

    public static float getRadtio(Context ctx) {
        if (sRadtio == 0) {
            synchronized (ScreenParameter.class) {
                if (sRadtio == 0) {
                    sRadtio = Math.min(getRatioX(ctx), getRatioY(ctx));
                }
            }
        }
        return sRadtio;
    }

    public static float getRatioX(Context context) {
        if (sRatioX == 0) {
            synchronized (ScreenParameter.class) {
                if (sRatioX == 0) {
                    if (isVertical) {
                        sRatioX = Math.min(getScreenWidth(context), getScreenHeight(context)) / sDefaultHeight;
                    } else {
                        sRatioX = Math.max(getScreenWidth(context), getScreenHeight(context)) / sDefaultWidth;
                    }
                }
            }
        }
        return sRatioX;
    }

    public static float getRatioY(Context context) {
        if (sRatioY == 0) {
            synchronized (ScreenParameter.class) {
                if (sRatioY == 0) {
                    if (isVertical) {
                        sRatioY = Math.max(getScreenWidth(context), getScreenHeight(context)) / sDefaultWidth;
                    } else {
                        sRatioY = Math.min(getScreenWidth(context), getScreenHeight(context)) / sDefaultHeight;
                    }
                }
            }
        }
        return sRatioY;
    }

    /**
     * @param @param  width
     * @param @return 设定文件
     * @return float 返回类型
     * @throws
     * @Description: TODO(根据设计图的标准不同配置标准宽)
     */
    public static float setMeasureWidth(float width) {
        sDefaultWidth = width;
        return sDefaultWidth;
    }

    /**
     * @param @param  height
     * @param @return 设定文件
     * @return float 返回类型
     * @throws
     * @Description: TODO(根据设计图的标准不同配置标准高)
     */
    public static float setMeasureHeight(float height) {
        sDefaultHeight = height;
        return sDefaultHeight;
    }

    public static float getDenSity(Context context) {
        if (sDensity == 0) {
            synchronized (ScreenParameter.class) {
                if (sDensity == 0) {
                    DisplayMetrics metric = new DisplayMetrics();
                    ((WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE))
                            .getDefaultDisplay().getMetrics(metric);
                    sDensity = metric.density;
                }
            }
        }
        return sDensity;
    }

    public static int getDensityDpi(Context context) {
        if (sDensityDpi == 0) {
            synchronized (ScreenParameter.class) {
                if (sDensityDpi == 0) {
                    DisplayMetrics metric = new DisplayMetrics();
                    ((WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE))
                            .getDefaultDisplay().getMetrics(metric);
                    sDensityDpi = metric.densityDpi;
                }
            }
        }
        return sDensityDpi;
    }

    public static int getFitSize(View v, int size) {
        boolean isAutoFit = true;
        return getFitSize(v.getContext(), size, isAutoFit);
    }

    public static int getFitSize(Context context, int size) {
        return getFitSize(context, size, true);
    }

    public static int getFitSize(Context context, int size, boolean autoFit) {
        if (!autoFit) {
            return size;
        }
        float radio = getRadtio(context);
        if (Math.abs(getRatioX(context) - getRatioY(context)) < 0.15) {
            radio = getRatioX(context);
        }
        return new BigDecimal(radio * (float) size).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
    }

    public static int getFitWidth(Context context, int size) {
        return getFitSize(context, size, true);
    }

    public static int getFitWidth(View v, int size) {
        return getFitSize(v, size);
    }

    public static int getFitWidth(Context context, int size, boolean autoFit) {
        return getFitSize(context, size, autoFit);
    }

    public static float getFitSize(Context context, float size, boolean autoFit) {
        if (!autoFit) {
            return size;
        }
        return new BigDecimal(getRatioX(context) * size).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
    }

    public static int getFitHeight(Context context, int size) {
        return getFitHeight(context, size, true);
    }

    public static int getFitHeight(View v, int size) {
        boolean isAutoFit = true;
        return getFitHeight(v.getContext(), size, isAutoFit);
    }

    public static int getFitHeight(Context context, int size, boolean autoFit) {
        if (!autoFit) {
            return size;
        }
        float radio = getRadtio(context);
        if (Math.abs(getRatioX(context) - getRatioY(context)) < 0.15) {
            radio = getRatioY(context);
        }
        return new BigDecimal(radio * (float) size).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
    }

    public static int dip2px(Context context, float dipValue) {
        return (int) (dipValue * getDenSity(context) + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        return (int) (pxValue / getDenSity(context) + 0.5f);
    }

    public static android.view.ViewGroup.LayoutParams getRealLayoutParams(View v,
                                                                          android.view.ViewGroup.LayoutParams params) {
        boolean isAutoFit = true;
        if (!isAutoFit) {
            return params;
        }
        if (params instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) params).leftMargin = ScreenParameter.getFitWidth(v,
                    ((ViewGroup.MarginLayoutParams) params).leftMargin);
            ((ViewGroup.MarginLayoutParams) params).rightMargin = ScreenParameter.getFitWidth(v,
                    ((ViewGroup.MarginLayoutParams) params).rightMargin);
            ((ViewGroup.MarginLayoutParams) params).topMargin = ScreenParameter.getFitHeight(v,
                    ((ViewGroup.MarginLayoutParams) params).topMargin);
            ((ViewGroup.MarginLayoutParams) params).bottomMargin = ScreenParameter.getFitHeight(v,
                    ((ViewGroup.MarginLayoutParams) params).bottomMargin);
        }
        if (params.width != FrameLayout.LayoutParams.WRAP_CONTENT && params.width != FrameLayout.LayoutParams.MATCH_PARENT) {
            params.width = ScreenParameter.getFitWidth(v, params.width);
        }
        if (params.height != FrameLayout.LayoutParams.WRAP_CONTENT && params.height != FrameLayout.LayoutParams.MATCH_PARENT) {
            params.height = ScreenParameter.getFitHeight(v, params.height);
        }
        return params;
    }

    /**
     * 获取android设备的屏幕大小，勾股定理
     *
     * @param ctx
     * @return
     */
    public static double getScreenDimension(Context ctx) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        return Math.sqrt(x + y);
    }


    public static int getDisplaySize(Context context, boolean getHeight) {
        if (sWidth * sHeight == 0) {
            // int sdkVer = Build.VERSION.SDK_INT;
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            display.getMetrics(dm);
            sWidth = dm.widthPixels;
            sHeight = dm.heightPixels;
            // try {
            // if (!checkDeviceHasNavigationBar(context)) {
            // if (sdkVer == 13) {
            // Method realHeight =
            // display.getClass().getMethod("getRealHeight");
            // sHeight = (Integer) realHeight.invoke(display);
            // Method realWidth = display.getClass().getMethod("getRealWidth");
            // sWidth = (Integer) realWidth.invoke(display);
            // } else if (sdkVer > 13 && sdkVer < 17) {
            // Method rawHeight = display.getClass().getMethod("getRawHeight");
            // sHeight = (Integer) rawHeight.invoke(display);
            // Method rawWidth = display.getClass().getMethod("getRawWidth");
            // sWidth = (Integer) rawWidth.invoke(display);
            // } else if (sdkVer >= 17) { // 4.2
            // Method mt = display.getClass().getMethod("getRealSize",
            // Point.class);
            // Point point = new Point();
            // mt.invoke(display, point);
            // sWidth = point.x;
            // sHeight = point.y;
            // }
            // }
            // } catch (Throwable e) {
            // e.printStackTrace();
            // }
            // if (sWidth * sHeight == 0) {// 防止sdk反射出现异常，则用系统方法获取屏幕宽高
            // sWidth = dm.widthPixels;
            // sHeight = dm.heightPixels;
            // }
        }
        if (getHeight) {
            return sHeight;
        } else {
            return sWidth;
        }
    }

    public static int getDisplayHeight(Context context) {
        if (sWidth * sHeight == 0) {
            int sdkVer = Build.VERSION.SDK_INT;
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            display.getMetrics(dm);
            // sWidth = dm.widthPixels;
            sHeight = dm.heightPixels;
            try {
                // if (!checkDeviceHasNavigationBar(context)) {
                if (sdkVer == 13) {
                    Method realHeight = display.getClass().getMethod("getRealHeight");
                    sHeight = (Integer) realHeight.invoke(display);
                    // Method realWidth =
                    // display.getClass().getMethod("getRealWidth");
                    // sWidth = (Integer) realWidth.invoke(display);
                } else if (sdkVer > 13 && sdkVer < 17) {
                    Method rawHeight = display.getClass().getMethod("getRawHeight");
                    sHeight = (Integer) rawHeight.invoke(display);
                    // Method rawWidth =
                    // display.getClass().getMethod("getRawWidth");
                    // sWidth = (Integer) rawWidth.invoke(display);
                } else if (sdkVer >= 17) { // 4.2
                    Method mt = display.getClass().getMethod("getRealSize", Point.class);
                    Point point = new Point();
                    mt.invoke(display, point);
                    // sWidth = point.x;
                    sHeight = point.y;
                }
                // }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            if (sWidth * sHeight == 0) {// 防止sdk反射出现异常，则用系统方法获取屏幕宽高
                // sWidth = dm.widthPixels;
                sHeight = dm.heightPixels;
            }
        }
        return sHeight;
    }
}
