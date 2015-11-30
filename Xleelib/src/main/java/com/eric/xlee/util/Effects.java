package com.eric.xlee.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;

import com.eric.xlee.log.LogUtil;

/**
 * Created by xlee on 30/11/15.
 */
public class Effects {
    private static final String TAG = "VstEffercts";
    private static boolean mAnimationIsRunning = false;

    public enum ScaleType {
        LARGE, SMALE
    }

    public enum TextAction {
        SHOW, DISMISS
    }

    public enum Direction {
        LEFT, RIGHT
    }

    private static Interpolator interpolator = new AccelerateDecelerateInterpolator();

    /**
     * 焦点状态缩放动画
     *
     * @param view     执行动画的对象实例
     * @param scale    缩放系数
     * @param duration 动画执行的时间
     * @param type     动画类型，放大或者缩小
     */
    public static void focusAnimation(View view, float scale, int duration, ScaleType type) {
        if (view == null || scale <= 1) {
            return;
        }
        if (type == ScaleType.SMALE) {
            scale = 1.0f;
        }
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", scale);
        scaleX.setDuration(duration);
        scaleX.setInterpolator(interpolator);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", scale);
        scaleY.setDuration(duration);
        scaleY.setInterpolator(interpolator);
        AnimatorSet set = new AnimatorSet();
        set.play(scaleX).with(scaleY);
        set.start();
    }

    /**
     * 焦点状态透明度动画
     *
     * @param view
     * @param toAlpha
     * @param duration
     */
    public static void focusAlpha(View view, float toAlpha, int duration) {
        if (view == null || toAlpha == view.getAlpha()) {
            return;
        }
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", toAlpha);
        alpha.setDuration(duration);
        alpha.setInterpolator(interpolator);
        alpha.start();
    }

    /**
     * 点击状态的动画
     *
     * @param view     执行动画的对象实例
     * @param scale    缩放系数
     * @param duration 动画执行的时间
     */
    public static void clickAnimation(View view, float scale, int duration) {
        clickAnimation(view, 1f, scale, duration);
    }

    /**
     * 点击状态的动画
     *
     * @param view     执行动画的对象实例
     * @param scale    缩放系数
     * @param duration 动画执行的时间
     */
    public static void clickAnimation(View view, float from, float scale, int duration) {
        if (view == null) {
            return;
        }
        duration = duration / 2;// 先将总的时间折半
        // 先缩小
        ObjectAnimator scaleSX = ObjectAnimator.ofFloat(view, "scaleX", from);
        scaleSX.setDuration(duration);
        scaleSX.setInterpolator(interpolator);
        ObjectAnimator scaleSY = ObjectAnimator.ofFloat(view, "scaleY", from);
        scaleSY.setDuration(duration);
        scaleSY.setInterpolator(interpolator);
        // 再放大
        // scale=1.0f/scale;
        ObjectAnimator scaleLX = ObjectAnimator.ofFloat(view, "scaleX", scale);
        scaleLX.setDuration(duration);
        scaleLX.setInterpolator(interpolator);
        ObjectAnimator scaleLY = ObjectAnimator.ofFloat(view, "scaleY", scale);
        scaleLY.setDuration(duration);
        scaleLY.setInterpolator(interpolator);
        // 一起执行
        AnimatorSet set = new AnimatorSet();
        set.play(scaleSX).with(scaleSY);
        set.play(scaleLX).with(scaleLY).after(duration);// 放大动画延迟缩小动画执行的时间
        set.start();
    }

    /**
     * 隐藏和显示动画（纵向收缩和透明度）
     *
     * @param view     执行效果的试图
     * @param duration 效果时间
     * @param action   执行的动作（显示或隐藏）
     */
    public static void homeTextAnimation(TextView view, int duration, TextAction action) {
        if (view == null || view.getText() == null || view.getText().equals("")) {
            return;
        }
        float scale;
        float alph;
        if (action == TextAction.SHOW) {
            scale = 1.0f;
            alph = 1.0f;
        } else {
            scale = 0.0f;
            alph = 0.0f;
        }
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", scale);
        scaleY.setDuration(duration);
        scaleY.setInterpolator(interpolator);
        ObjectAnimator alphAni = ObjectAnimator.ofFloat(view, "alpha", alph);
        alphAni.setDuration(duration);
        alphAni.setInterpolator(interpolator);
        AnimatorSet set = new AnimatorSet();
        set.play(scaleY).with(alphAni);
        set.start();
    }

    /**
     * 整个界面的回弹动画 在界面的最右边按右键、或者在界面的最左边按左键时调此效果实现回弹效果
     *
     * @param view      整个界面的view
     * @param direction 按键的方向，亦即目前的边界方向（左或右）
     */
    public static void shakeAnimation(View view, Direction direction) {
        if (view == null) {
            return;
        }
        // 先将界面缩小，并向边界的反方向平移
        ObjectAnimator scalexS = ObjectAnimator.ofFloat(view, "scaleX", 0.9f);
        scalexS.setDuration(250);
        scalexS.setInterpolator(interpolator);
        ObjectAnimator scaleyS = ObjectAnimator.ofFloat(view, "scaleY", 0.9f);
        scaleyS.setDuration(250);
        scaleyS.setInterpolator(interpolator);
        float toX;
        if (direction == Direction.LEFT) {
            toX = view.getX() + 100;
        } else {
            toX = view.getX() - 100;
        }
        ObjectAnimator tratexM = ObjectAnimator.ofFloat(view, "x", toX);
        tratexM.setDuration(250);
        tratexM.setInterpolator(interpolator);
        // 还原界面大小，复位界面位置
        ObjectAnimator scalexL = ObjectAnimator.ofFloat(view, "scaleX", 1.0f);
        scalexL.setDuration(250);
        scalexL.setInterpolator(interpolator);
        ObjectAnimator scaleyL = ObjectAnimator.ofFloat(view, "scaleY", 1.0f);
        scaleyL.setDuration(250);
        scaleyL.setInterpolator(interpolator);
        ObjectAnimator tratex0 = ObjectAnimator.ofFloat(view, "x", 0);
        tratex0.setDuration(250);
        tratex0.setInterpolator(interpolator);

        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(interpolator);
        // set.play(tratexM);
        // set.play(tratex0).after(250);
        set.play(scalexS).with(scaleyS).with(tratexM);
        set.play(scalexL).with(scaleyL).with(tratex0).after(250);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mAnimationIsRunning = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimationIsRunning = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mAnimationIsRunning = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                mAnimationIsRunning = true;
            }
        });
        set.start();
    }

    /**
     * 一个控件相对与另外一个控件的滑动动画
     *
     * @param staticView 静止的控件
     * @param moveView   执行滑动的控件
     * @param duration
     */
    public static void relativeTraslate(View staticView, View moveView, int duration) {
        if (staticView == null || moveView == null) {
            return;
        }
        ObjectAnimator traX = ObjectAnimator.ofFloat(moveView, "x", staticView.getX()
                + (staticView.getWidth() - moveView.getWidth()) / 2);
        LogUtil.i(TAG, "toX:" + staticView.getX() + (staticView.getWidth() - moveView.getWidth()) / 2);
        traX.setDuration(duration);
        traX.setInterpolator(interpolator);
        traX.start();
        // ObjectAnimator traY=ObjectAnimator.ofFloat(moveView, "y",
        // staticView.getY()+staticView.getHeight());
        // traY.setDuration(duration);
        // traY.setInterpolator(interpolator);
        //
        // AnimatorSet set=new AnimatorSet();
        // set.play(traX).with(traY);
        // set.start();
    }

    /**
     * 一个控件相对与另外一个控件的滑动动画
     *
     * @param staticView 静止的控件
     * @param moveView   执行滑动的控件
     * @param duration
     */
    public static void relativeTraslateAutoFit(View staticView, View moveView, int duration) {
        if (staticView == null || moveView == null) {
            return;
        }
        ObjectAnimator traX = ObjectAnimator.ofFloat(moveView, "x", staticView.getX() - (10) / 2);
        LogUtil.i(TAG, "toX:" + (staticView.getX() - (10) / 2));
        traX.setDuration(duration);
        traX.setInterpolator(interpolator);
        ViewWrapper viewWrapper = new ViewWrapper(moveView);
        ObjectAnimator scWidth = ObjectAnimator.ofInt(viewWrapper, "width", staticView.getWidth() + 10);
        scWidth.setDuration(duration);
        scWidth.setInterpolator(interpolator);
        AnimatorSet set = new AnimatorSet();
        set.play(traX).with(scWidth);
        set.start();
    }

    private static class AutoFitView {
        View mView;

        AutoFitView(View view) {
            this.mView = view;
        }

        protected int getWidth() {
            return mView.getWidth();
        }

        protected void setWidth(int width) {
            mView.getLayoutParams().width = width;
            mView.requestLayout();
        }
    }

    /**
     * 飞框动画
     *
     * @param border      框，飞动的空间
     * @param relateView  与飞框相对的空间
     * @param borderWidth
     * @param duration    飞框的边线宽度
     */
    public static void flyBorder(View border, View relateView, int borderWidth, int duration) {
        if (border == null || relateView == null || borderWidth <= 0) {
            return;
        }
        ObjectAnimator traX = ObjectAnimator.ofFloat(border, "x", relateView.getX() - borderWidth);
        traX.setDuration(duration);
        traX.setInterpolator(interpolator);
        ObjectAnimator traY = ObjectAnimator.ofFloat(border, "y", relateView.getY() - borderWidth);
        traY.setDuration(duration);
        traY.setInterpolator(interpolator);

        ViewWrapper viewWrapper = new ViewWrapper(border);
        ObjectAnimator scWidth = ObjectAnimator.ofInt(viewWrapper, "width", relateView.getWidth() + borderWidth * 2);
        scWidth.setDuration(duration);
        scWidth.setInterpolator(interpolator);
        ObjectAnimator scHeight = ObjectAnimator.ofInt(viewWrapper, "height", relateView.getHeight() + borderWidth * 2);
        scHeight.setDuration(duration);
        scHeight.setInterpolator(interpolator);
        AnimatorSet set = new AnimatorSet();
        set.play(traX).with(traY).with(scWidth).with(scHeight);
        set.start();
    }

    /**
     * 飞框动画
     *
     * @param border      框，飞动的空间
     * @param relateView  与飞框相对的空间
     * @param borderWidth
     * @param duration    飞框的边线宽度
     */
    public static void flyBorder(View border, View relateView, int borderWidth, int duration, boolean onScreen) {
        if (border == null || relateView == null || borderWidth <= 0) {
            return;
        }
        int[] location = new int[2];
        if (onScreen) {
            relateView.getLocationOnScreen(location);
        } else {
            relateView.getLocationInWindow(location);
        }
        ObjectAnimator traX = ObjectAnimator.ofFloat(border, "x", location[0] - borderWidth);
        traX.setDuration(duration);
        traX.setInterpolator(interpolator);
        ObjectAnimator traY = ObjectAnimator.ofFloat(border, "y", location[1] - borderWidth);
        traY.setDuration(duration);
        traY.setInterpolator(interpolator);

        ViewWrapper viewWrapper = new ViewWrapper(border);
        ObjectAnimator scWidth = ObjectAnimator.ofInt(viewWrapper, "width", relateView.getWidth() + borderWidth * 2);
        scWidth.setDuration(duration);
        scWidth.setInterpolator(interpolator);
        ObjectAnimator scHeight = ObjectAnimator.ofInt(viewWrapper, "height", relateView.getHeight() + borderWidth * 2);
        scHeight.setDuration(duration);
        scHeight.setInterpolator(interpolator);
        AnimatorSet set = new AnimatorSet();
        set.play(traX).with(traY).with(scWidth).with(scHeight);
        set.start();
    }

    public static void smoothMove(View moveView, int toX, int duration) {
        ObjectAnimator traX = ObjectAnimator.ofFloat(moveView, "x", toX);
        traX.setDuration(duration);
        traX.setInterpolator(interpolator);
        traX.start();
    }

    public static boolean animationIsRunning() {
        return mAnimationIsRunning;
    }

    public static class ViewWrapper {
        private View mTarget;

        public ViewWrapper(View target) {
            mTarget = target;
        }

        public int getWidth() {
            return mTarget.getLayoutParams().width;
        }

        public void setWidth(int width) {
            mTarget.getLayoutParams().width = width;
            mTarget.requestLayout();
        }

        public void setHeight(int height) {
            mTarget.getLayoutParams().height = height;
            mTarget.requestLayout();
        }

        public int getHeight() {
            return mTarget.getLayoutParams().height;
        }
    }
}
