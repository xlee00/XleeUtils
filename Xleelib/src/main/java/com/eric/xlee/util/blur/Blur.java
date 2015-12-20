package com.eric.xlee.util.blur;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;

import java.lang.ref.WeakReference;

public class Blur {
    private static final String TAG = "Blur";
    public static WeakReference<Activity> sCurrentActivity;

    @SuppressLint("NewApi")
    public static Bitmap fastblur(Context context, Bitmap sentBitmap, int radius, int radiusStack) {
        if (Build.VERSION.SDK_INT >= 19) {
            Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
            final RenderScript rs = RenderScript.create(context);
            final Allocation input = Allocation.createFromBitmap(rs, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT);
            final Allocation output = Allocation.createTyped(rs, input.getType());
            final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius(radius);
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);
            return bitmap;
        }
        StackBlurManager stackBlurManager = new StackBlurManager(sentBitmap);
        stackBlurManager.process(radiusStack);
        return stackBlurManager.returnBlurredImage();
    }

    public static Bitmap getBitmapFromView(View v) {
        if (v == null)
            return null;
        int scaleRate = 16;
        Bitmap screenshot = Bitmap.createBitmap(v.getWidth() / scaleRate, v.getHeight() / scaleRate,
                Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(screenshot);
        canvas.translate(-v.getScrollX() / scaleRate, -v.getScrollY() / scaleRate);
        canvas.scale(1.0f / scaleRate, 1.0f / scaleRate);
        v.draw(canvas);
        screenshot = Blur.fastblur(v.getContext().getApplicationContext(), screenshot, 10, 9);
        return screenshot;
    }

    public static Bitmap getBitmapDrawable(Context ctx, int resId) {
        Bitmap res = BitmapFactory.decodeResource(ctx.getResources(), resId);
        // int scaleRate = 16;
        // Bitmap screenshot = Bitmap.createBitmap(res.getWidth() / scaleRate,
        // res.getHeight() / scaleRate,
        // Bitmap.Config.ARGB_4444);
        return Blur.fastblur(ctx, res, 10, 9);
    }

}
