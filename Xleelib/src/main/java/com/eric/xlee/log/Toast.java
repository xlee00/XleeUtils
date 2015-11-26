/**
 *TaostEric.java[V 1.0.0]
 *classes : com.eric.xlee.log.Toast
 * Xlee Create at 4 Nov 2015 11:16:19
 */
package com.eric.xlee.log;


import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.eric.xlee.text.TypefaceUtils;
import com.eric.xlee.lib.R;

/**
 * com.eric.xlee.log.Toast
 * 
 * @author Xlee <br/>
 *         create at 4 Nov 2015 11:16:19
 */
public class Toast extends android.widget.Toast {
    private TextView mTxtToast;

    public Toast(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTxtToast = (TextView) inflater.inflate(R.layout.toast_txt, null);
        TypefaceUtils.ROBOTO_CONDENSED_REGULAR.setTypeface(mTxtToast);
        setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 40);
        setDuration(android.widget.Toast.LENGTH_LONG);
        setView(mTxtToast);
    }

    public void setText(CharSequence text) {
        mTxtToast.setText(text);
    }

    public void setText(int resId) {
        mTxtToast.setText(resId);
    }

    public static android.widget.Toast makeText(Context context, CharSequence text, int duration) {
        android.widget.Toast result = new Toast(context);
        result.setText(text);
        result.setDuration(duration);
        return result;
    }

    public static android.widget.Toast makeText(Context context, int resId, int duration) {
        return makeText(context, context.getText(resId), duration);
    }
}
