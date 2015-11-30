package com.eric.xlee.activitys;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import android.widget.TextView;

import com.eric.xlee.adapter.ViewPagerAdapter;
import com.eric.xlee.log.Toast;
import com.eric.xlee.utils.R;
import com.eric.xlee.widgets.VerticalViewPager;

import java.util.ArrayList;
import java.util.Locale;


public class VerticalViewPagerDemo extends FragmentActivity {
    private static final String TAG = "VerticalViewPagerDemo";
    private VerticalViewPager verticalViewPagerDemo;
    private ViewPagerAdapter mViewPagerAdapter;

    private static final float MIN_SCALE = 0.9f;
    private static final float MIN_ALPHA = 0.7f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.vertical_viewpager);
        verticalViewPagerDemo = (VerticalViewPager) findViewById(R.id.verticalviewpager);
        // verticalViewPagerDemo.setAdapter(new
        // DummyAdapter(this.getSupportFragmentManager()));
        // verticalViewPagerDemo.setPageMargin(getResources().getDimensionPixelSize(R.dimen.pagemargin));
        // verticalViewPagerDemo.setPageMarginDrawable(new
        // ColorDrawable(getResources().getColor(
        // android.R.color.holo_green_dark)));
        LayoutInflater inflate = LayoutInflater.from(this);
        View view1 = inflate.inflate(R.layout.view_pager_item, null);
        ((TextView) view1.findViewById(R.id.textView)).setText("这是第1个View");
        View view2 = inflate.inflate(R.layout.view_pager_item, null);
        ((TextView) view2.findViewById(R.id.textView)).setText("这是第2个View");
        View view3 = inflate.inflate(R.layout.view_pager_item, null);
        ((TextView) view3.findViewById(R.id.textView)).setText("这是第3个View");
        View view4 = inflate.inflate(R.layout.view_pager_item, null);
        ((TextView) view4.findViewById(R.id.textView)).setText("这是第4个View");
        View view5 = inflate.inflate(R.layout.view_pager_item, null);
        ((TextView) view5.findViewById(R.id.textView)).setText("这是第5个View");
        View view6 = inflate.inflate(R.layout.view_pager_item, null);
        ((TextView) view6.findViewById(R.id.textView)).setText("这是第6个View");
        ArrayList<View> views = new ArrayList<View>();
        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(view4);
        views.add(view5);
        views.add(view6);
        mViewPagerAdapter = new ViewPagerAdapter(views);
        verticalViewPagerDemo.setAdapter(mViewPagerAdapter);
        verticalViewPagerDemo.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View view, float position) {
                int pageWidth = view.getWidth();
                int pageHeight = view.getHeight();
                // VstLog.i(
                // "transformPage",
                // "Position=" + position + "  view.getId()="
                // + ((TextView) view.findViewById(R.id.textView)).getText());
                if (position < -1) { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    view.setAlpha(0);

                } else if (position <= 1) { // [-1,1]
                    // Modify the default slide transition to shrink the page as
                    // well
                    float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                    float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                    float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                    float accelerateFactor = (float) Math.max(0, -0.5 + Math.abs(position));
                    if (position < 0) {
                        // view.setTranslationY(vertMargin - horzMargin / 2 -
                        // pageHeight * 8 * accelerateFactor
                        // * accelerateFactor);
                        view.setTranslationY(vertMargin - horzMargin / 2);
                    } else {
                        // view.setTranslationY(-vertMargin + horzMargin / 2 +
                        // pageHeight * 8 * accelerateFactor
                        // * accelerateFactor);
                        view.setTranslationY(-vertMargin + horzMargin / 2);
                    }

                    // Scale the page down (between MIN_SCALE and 1)
                    view.setScaleX(scaleFactor);
                    view.setScaleY(scaleFactor);

                    // Fade the page relative to its size.
                    view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA) - MIN_ALPHA
                            * Math.min(0.95f, accelerateFactor * 2f));
                    // view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1
                    // - MIN_SCALE) * (1 - MIN_ALPHA));

                } else { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    view.setAlpha(0);
                }
            }
        });
        // try {
        // Field field = VerticalViewPager.class.getDeclaredField("mScroller");
        // field.setAccessible(true);
        // FixedSpeedScroller scroller = new FixedSpeedScroller(this, new
        // DecelerateInterpolator());
        // // FixedSpeedScroller scroller = new FixedSpeedScroller(this, new
        // // AccelerateInterpolator());
        // // FixedSpeedScroller scroller = new FixedSpeedScroller(this);
        // field.set(verticalViewPagerDemo, scroller);
        // scroller.setmDuration(500);
        // } catch (Exception e) {
        // VstLog.e(TAG, e.toString());
        // }
    }

    class FixedSpeedScroller extends Scroller {
        private int mDuration = 1500;

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        public void setmDuration(int time) {
            mDuration = time;
        }

        public int getmDuration() {
            return mDuration;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getRepeatCount() % 3 != 0) {
            return true;
        }
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
            }
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            }
            switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (verticalViewPagerDemo.getCurrentItem() < mViewPagerAdapter.getCount() - 1) {
                    verticalViewPagerDemo.setCurrentItem(verticalViewPagerDemo.getCurrentItem() + 1);
                }
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                if (verticalViewPagerDemo.getCurrentItem() > 0) {
                    verticalViewPagerDemo.setCurrentItem(verticalViewPagerDemo.getCurrentItem() - 1);
                }
                break;
            default:
                break;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    public class DummyAdapter extends FragmentPagerAdapter {

        public DummyAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class
            // below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
            case 0:
                return "PAGE 1";
            case 1:
                return "PAGE 2";
            case 2:
                return "PAGE 3";
            }
            return null;
        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // View rootView = inflater.inflate(R.layout.fragment_layout,
            // container, false);
            // TextView textView = (TextView)
            // rootView.findViewById(R.id.textview);
            // textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            // return rootView;
            return null;
        }

    }

    long waitTime = 2000;
    long touchTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - touchTime) >= waitTime) {
                Toast.makeText(this, this.getResources().getString(R.string.press_again), Toast.LENGTH_SHORT)
                        .show();
                touchTime = currentTime;
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
