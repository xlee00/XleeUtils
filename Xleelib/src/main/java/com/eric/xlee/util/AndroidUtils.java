package com.eric.xlee.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 * Created by xlee on 20/12/15.
 */
public class AndroidUtils {
    private static final String ROOT_NAME = "Xlee";
    public static final String EXTRA = "extra";
    private static final String CONFIG_DIR = "config";
    private static final String PIC_DIR = "pic";
    private static final String PIC_CACHE_DIR = "pic_cache";
    private static final String TEMP_DIR = "temp";
    public static final String START_PIC = "start_pic.png";
    public final static String APP_NAME = "xlee.apk";

    public static Drawable getLocalDrawable(Context ctx, int drawableId) {
        Drawable drawable = null;
        final float ratio = ScreenParameter.getRadtio(ctx);
        try {
            if (ratio < 0.9f) {
                drawable = ctx.getResources().getDrawableForDensity(drawableId, DisplayMetrics.DENSITY_DEFAULT);
                if (drawable != null) {
                    drawable = zoomDrawable(drawable, ratio / 1.0f);
                }
            } else if (ratio >= 0.9f && ratio <= 1.1f) {
                drawable = ctx.getResources().getDrawableForDensity(drawableId, DisplayMetrics.DENSITY_DEFAULT);
            } else if (ratio > 1.1f && ratio < 1.4f) {
                drawable = ctx.getResources().getDrawableForDensity(drawableId, DisplayMetrics.DENSITY_DEFAULT);
                if (drawable != null) {
                    drawable = zoomDrawable(drawable, ratio / 1.0f);
                }
            } else if (ratio >= 1.4f && ratio <= 1.6f) {
                drawable = ctx.getResources().getDrawableForDensity(drawableId, DisplayMetrics.DENSITY_XHIGH);
            } else if (ratio > 1.6f) {
                drawable = ctx.getResources().getDrawableForDensity(drawableId, DisplayMetrics.DENSITY_XHIGH);
                if (drawable != null) {
                    drawable = zoomDrawable(drawable, ratio / 1.5f);
                }
            }
        } catch (Exception e) {
        }
        return drawable;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    public static Drawable zoomDrawable(Drawable drawable, float ratio) {
        Bitmap bmp = null;
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        bmp = drawableToBitmap(drawable);
        Matrix matrix = new Matrix();
        matrix.postScale(ratio, ratio);
        bmp = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
        return new BitmapDrawable(bmp);
    }

    public static String getVesionName(Context context) {
        String versionName = null;
        try {
            String name = context.getApplicationInfo().packageName;
            versionName = context.getPackageManager().getPackageInfo(name, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("", e.getMessage());
        }
        return versionName;
    }

    /**
     * @throws
     * @Title: getRealVersionCode
     * @Description: TODO(获取真实的版本号)
     */
    public static int getRealVersionCode(Context context) {
        int versionCode = 0;
        try {
            String name = context.getApplicationInfo().packageName;
            versionCode = context.getPackageManager().getPackageInfo(name, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("", e.getMessage());
        }
        return versionCode;
    }

    /**
     * @throws
     * @Title: getVersionCode
     * @Description: TODO(获取mainfest中自定义的版本号， 如果没有定义则获 取真实的版本号，主要为经典版切换而新增的逻辑)
     */
    public static int getVersionCode(Context ctx) {
        int versionCode = 0;
        try {
            versionCode = ctx.getPackageManager()
                    .getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA).metaData
                    .getInt(("versionCode"));
        } catch (Throwable e) {
            e.printStackTrace();
            versionCode = 0;
        }
        if (versionCode == 0) {
            versionCode = getRealVersionCode(ctx);
        }
        return versionCode;
    }

    public static String getClassicVersion(Context ctx) {
        String classic = null;
        try {
            classic = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA).metaData
                    .getString(("classic"));
        } catch (Throwable e) {
            e.printStackTrace();
            classic = null;
        }
        return classic;
    }

    public static boolean downLoafFileFromNet(String path, String url) {
        return downLoafFileFromNet(new File(path), url);
    }

    public static boolean downLoafFileFromNet(File file, String url) {
        boolean sucess = false;
        HttpURLConnection connection = null;
        OutputStream os = null;
        InputStream is = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();
            int count = -1;
            byte[] buffer = new byte[2048];
            os = new FileOutputStream(file);
            is = connection.getInputStream();
            while ((count = is.read(buffer)) != -1) {
                os.write(buffer, 0, count);
            }
            sucess = true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            FileUtils.closeIO(os);
            FileUtils.closeIO(is);
        }
        return sucess;
    }

    public static boolean isGZIPInputStream(byte[] data) {
        int headerData = (int) ((data[0] << 8) | data[1] & 0xFF);
        return headerData == 0x1f8b;
    }

    public static String doGetByGZIP(String url) {
        System.out.println("doGetByGZIP url=" + url);
        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            connection.connect();
            is = new BufferedInputStream(connection.getInputStream());
            is.mark(2);
            byte[] header = new byte[2];
            int result = is.read(header);
            is.reset();
            if (result != -1 && isGZIPInputStream(header)) {
                is = new GZIPInputStream(is);
            }
            os = new ByteArrayOutputStream();
            int count = -1;
            byte[] buffer = new byte[1024];
            while ((count = is.read(buffer)) != -1) {
                os.write(buffer, 0, count);
            }
            return os.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            FileUtils.closeIO(is);
            FileUtils.closeIO(os);
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    /**
     * @param @param pool 设定文件
     * @return void 返回类型
     * @throws
     * @Description: TODO(关闭线程池的方法)
     */
    public static void closeExecutorService(ExecutorService pool) {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                pool.shutdownNow();
                if (!pool.awaitTermination(60, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 获取app的名字
     *
     * @param ctx
     * @return
     */
    public static String getApplicationName(Context ctx) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = ctx.getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }

    /**
     * 获取图片文件夹，主要放首页图片，不随文件的增加自动删除
     *
     * @param context
     * @return
     */
    public static String getPicDir(Context context) {
        File dir = new File(setupFinalRootDir(context), PIC_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath();
    }

    public static String setupFinalRootDir(Context context) {
        File root = null;
        // 优先判断是否有sd card 有可能 sd 卡 目录创建失败
        if (Environment.getExternalStorageState().equals((Environment.MEDIA_MOUNTED))) {
            // String DirName = context.getPackageName();
            File sdCardDir = Environment.getExternalStorageDirectory();
            root = new File(sdCardDir, ROOT_NAME);
            if (!root.exists()) {
                root.mkdirs();
            }
            if (root.exists()) {
                FileUtils.modifyFile(root);
                return root.getAbsolutePath();
            }
        }
        // cache
        root = context.getCacheDir();
        if (!root.exists()) {
            root.mkdirs();
        }
        if (root.exists()) {
            FileUtils.modifyFile(root);
            return root.getAbsolutePath();
        }

        throw new IllegalArgumentException("您的设备不支持缓存目录的创建，会影响你的使用体验！！！");
    }

    /**
     * @param @param viewgroup 设定文件
     * @return void 返回类型
     * @throws
     * @Description: TODO(清除界面上的所有数据)
     */
    public static void clearViewData(ViewGroup viewgroup) {
        if (viewgroup == null) {
            return;
        }
        for (int i = 0; i < viewgroup.getChildCount(); i++) {
            View child = viewgroup.getChildAt(i);
            child.setBackgroundResource(0);
            child.clearAnimation();
            child.clearFocus();
            child.setOnKeyListener(null);
            child.setOnFocusChangeListener(null);
            child.setFocusable(false);
            child.setClickable(false);
            child.setEnabled(false);
            if (child instanceof ViewGroup) {
                clearViewData((ViewGroup) child);
            } else {
                if (child instanceof ImageView) {
                    ((ImageView) child).setImageResource(0);
                }
                child = null;
            }
        }
    }

    /**
     * @param @param  ctx
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     * @Description: TODO(获取当前Activity运行的名称)
     */
    public static String getRunningActivityName(Context ctx) {
        String contextString = ctx.toString();
        return contextString.split("@")[0];
    }

    // CPU个数
    public static int getNumCores() {
        try {
            File dir = new File("/sys/devices/system/cpu/");
            File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                        return true;
                    }
                    return false;
                }
            });
            return files.length;
        } catch (Exception e) {
            return 1;
        }
    }

    /**
     * @throws true 为性能好
     * @Title: isExcellentDevice
     * @Description: TODO(判断该设备是否为性能较好的设备)
     */
    public static boolean isExcellentDevice(Context ctx) {
        try {
            if (ctx == null) {
                return false;
            }
            int cpuCount = getNumCores();
            if (cpuCount <= 1) {
                return false;
            } else {
                ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
                ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
                am.getMemoryInfo(mi);
                float availMem = mi.availMem / 1024 / 1024f;
                int maxMem = 60;
                if (ScreenParameter.getDenSity(ctx) > 1) {
                    maxMem = 80;
                }
                if (availMem < maxMem) {
                    return false;
                } else {
                    int limitMem = am.getMemoryClass();
                    if ((limitMem >= (maxMem * 4 / 3)) || cpuCount >= 4) {
                        if ("m201".equals(Build.MODEL)) {
                            return false;
                        }
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String printSystemInfo() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(date);
        StringBuilder sb = new StringBuilder();
        sb.append("--------------------  系统信息  " + time + "  --------------------");
        sb.append("\nBOARD        :" + android.os.Build.BOARD);
        sb.append("\nDEVICE       :" + android.os.Build.DEVICE);
        sb.append("\nPRODUCT      :" + android.os.Build.PRODUCT);
        sb.append("\nMANUFACTURER :" + android.os.Build.MANUFACTURER);
        sb.append("\nCODENAME     :" + android.os.Build.VERSION.CODENAME);
        sb.append("\nRELEASE      :" + android.os.Build.VERSION.RELEASE);
        // sb.append("\nSDK          :" + android.os.Build.VERSION.SDK);
        return sb.toString();
    }
}
