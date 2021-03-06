/**
 * ToastInThread.java [V 1.0.0]
 * Classes : com.eric.xlee.log.ToastInThread
 * Xlee Create at 26/11/2015 18:48
 */
package com.eric.xlee.log;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;

import java.text.MessageFormat;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

/**
 * 在不是主线程的时候无法直接使用toast，但是可以使用该工具类实现toast。当然在主线程的时候也可以使用该类。
 *
 * @author Tony Shen
 *         <p/>
 *         Create at 26/11/2015 18:48
 */
public class ToastInThread {
    private static final String TAG = ToastInThread.class.getSimpleName();

    /**
     * @param activity
     * @param resId    string资源id
     * @param duration
     */
    private static void showToast(final Activity activity, final int resId, final int duration) {
        if (activity == null)
            return;

        final Context context = activity.getApplication();
        activity.runOnUiThread(new Runnable() {

            public void run() {
                Toast.makeText(context, resId, duration).show();
            }
        });
    }

    /**
     * @param activity
     * @param message  toast的内容
     * @param duration
     */
    private static void showToast(final Activity activity, final String message, final int duration) {
        if (activity == null)
            return;
        if (TextUtils.isEmpty(message))
            return;

        final Context context = activity.getApplication();
        activity.runOnUiThread(new Runnable() {

            public void run() {
                Toast.makeText(context, message, duration).show();
            }
        });
    }

    public static void showToast(final Context context, final int resId, final int duration) {
        if (context == null)
            return;

        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(context.getApplicationContext(), resId, duration).show();
                }
            });
        } else {
            Toast.makeText(context.getApplicationContext(), resId, duration).show();
        }
    }

    public static void showToast(final Context context, final String message, final int duration) {
        if (context == null)
            return;
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(context.getApplicationContext(), message, duration).show();
                }

            });
        } else {
            Toast.makeText(context.getApplicationContext(), message, duration).show();
        }
    }

    public static void showLong(final Activity activity, int resId) {
        showToast(activity, resId, LENGTH_LONG);
    }

    public static void showLong(final Activity activity, final String message) {
        showToast(activity, message, LENGTH_LONG);
    }

    public static void showLong(final Activity activity, final String message, final Object... args) {
        String formatted = MessageFormat.format(message, args);
        showToast(activity, formatted, LENGTH_LONG);
    }

    public static void showLong(final Activity activity, final int resId, final Object... args) {
        if (activity == null)
            return;

        String message = activity.getString(resId);
        showLong(activity, message, args);
    }

    public static void showLong(Context context, int resId) {
        showToast(context, resId, LENGTH_LONG);
    }

    public static void showLong(Context context, String message) {
        showToast(context, message, LENGTH_LONG);
    }

    public static void showShort(final Activity activity, final int resId) {
        showToast(activity, resId, LENGTH_SHORT);
    }

    public static void showShort(final Activity activity, final String message) {
        showToast(activity, message, LENGTH_SHORT);
    }

    public static void showShort(final Activity activity, final String message, final Object... args) {
        String formatted = MessageFormat.format(message, args);
        showToast(activity, formatted, LENGTH_SHORT);
    }

    public static void showShort(final Activity activity, final int resId, final Object... args) {
        if (activity == null)
            return;

        String message = activity.getString(resId);
        showShort(activity, message, args);
    }

    public static void showShort(Context context, int resId) {
        showToast(context, resId, LENGTH_SHORT);
    }

    public static void showShort(Context context, String message) {
        showToast(context, message, LENGTH_SHORT);
    }
}
