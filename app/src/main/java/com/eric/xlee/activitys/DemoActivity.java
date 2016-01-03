/**
 * DemoActivity.java [V 1.0.0]
 * Classes : com.eric.xlee.activitys.DemoActivity
 * Xlee Create at 02/01/16 16:20
 */
package com.eric.xlee.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.eric.xlee.adapter.RecyclerViewAdapter;
import com.eric.xlee.app.AppUtils;
import com.eric.xlee.log.LogUtil;
import com.eric.xlee.log.Toast;
import com.eric.xlee.utils.R;

import java.util.ArrayList;


/**
 * com.eric.xlee.activitys.DemoActivity
 *
 * @author Xlee <br/>
 *         Create at 02/01/16 16:20
 */
public class DemoActivity extends Activity {
    private static final String TAG = DemoActivity.class.getSimpleName();

    private static final long WAIT_TO_EXIT_TIME = 2000;
    private static final int WEATHER_ACTIVITY = 0;
    private static final int VERTICAL_VIWEPAGER_ACTIVITY = WEATHER_ACTIVITY + 1;

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private long mTouchTime = 0;
    private String[] mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_list_activity);
        initDatas();
        initViews();
        initEvent();
    }

    protected void initDatas() {
        mItems = getResources().getStringArray(R.array.demo_items);
    }

    protected void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        // 设置item动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ArrayList<String> datas = new ArrayList<String>();
        for (String item : mItems) {
            datas.add(item);
        }
        mRecyclerViewAdapter = new RecyclerViewAdapter(this, datas);
        LogUtil.i(TAG, "datas.size() == " + datas.size());
        LogUtil.i(TAG, "null == mRecyclerView ? " + (null == mRecyclerView ? true : false) + "  null == mRecyclerViewAdapter ? " + (null == mRecyclerViewAdapter ? true : false));
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
    }

    protected void initEvent() {
        mRecyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                switch (position) {
                    case WEATHER_ACTIVITY:
                        intent.setClass(DemoActivity.this, WeatherActivity.class);
                        break;
                    case VERTICAL_VIWEPAGER_ACTIVITY:
                        intent.setClass(DemoActivity.this, VerticalViewPagerDemo.class);
                        break;
                    default:
                        new AlertDialog.Builder(DemoActivity.this)
                                .setTitle(DemoActivity.this.getResources().getString(R.string.warning))
                                .setMessage(DemoActivity.this.getResources().getString(R.string.no_existence)).show();
                        return;
                }
                AppUtils.startActivity(DemoActivity.this, intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(DemoActivity.this, position + " long click", Toast.LENGTH_SHORT).show();
                mRecyclerViewAdapter.delData(position);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (R.id.action_settings == id) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
            long currentTime = System.currentTimeMillis();
            if (WAIT_TO_EXIT_TIME <= (currentTime - mTouchTime)) {
                Toast.makeText(this, this.getResources().getString(R.string.press_again), Toast.LENGTH_SHORT)
                        .show();
                mTouchTime = currentTime;
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
