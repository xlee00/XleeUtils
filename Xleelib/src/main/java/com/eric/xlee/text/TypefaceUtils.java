/**
 * TypefaceUtils.java[V 1.0.0]
 * classes : com.eric.xlee.text.TypefaceUtils
 * Xlee Create at 19 Nov 2015 10:28:19
 */
package com.eric.xlee.text;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.widget.TextView;

/**
 * com.eric.xlee.text.TypefaceUtils
 * 使用方式
 * <p/>
 * TypefaceUtils.ROBOTO_LIGHT.setTypeface(textView);<br/>
 * TypefaceUtils.ROBOTO_CONDENSED_REGULAR.setTypeface(textView);<br/>
 * TypefaceUtils.ROBOTO_CONDENSED_LIGHT.setTypeface(textView);
 *
 * @author Xlee <br/>
 *         create at 19 Nov 2015 10:28:19
 */
public enum TypefaceUtils {
    ROBOTO_LIGHT(Path.PATH_TYPEFACE_ROBOTO_LIGHT), ROBOTO_CONDENSED_REGULAR(Path.PATH_TYPEFACE_ROBOTO_CONDENSED_REGULAR), ROBOTO_CONDENSED_LIGHT(
            Path.PATH_TYPEFACE_ROBOTO_CONDENSED_LIGHT);
    private static final String TAG = TypefaceUtils.class.getSimpleName();

    private interface Path {
        String PATH_TYPEFACE_ROBOTO_LIGHT = "fonts/Roboto-Light.ttf";
        String PATH_TYPEFACE_ROBOTO_CONDENSED_REGULAR = "fonts/RobotoCondensed-Regular.ttf";
        String PATH_TYPEFACE_ROBOTO_CONDENSED_LIGHT = "fonts/RobotoCondensed-Light.ttf";
    }

    private String mTypfacePath;
    private Typeface mTypeface;

    TypefaceUtils(String path) {
        mTypfacePath = path;
    }

    public void setTypeface(@NonNull TextView view) {
        if (null == view) {
            throw new IllegalArgumentException("view is null");
        }
        if (null == mTypeface) {
            mTypeface = Typeface.createFromAsset(view.getContext().getApplicationContext().getAssets(), mTypfacePath);
        }
        if (null != mTypeface) {
            view.setTypeface(mTypeface);
        }
    }
}
