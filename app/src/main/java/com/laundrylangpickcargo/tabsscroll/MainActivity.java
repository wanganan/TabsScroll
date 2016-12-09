package com.laundrylangpickcargo.tabsscroll;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends FragmentActivity {
    private List<String> tabels;
    private List<TextView> views;
    private List<Fragment> fragments;
    private HorizontalScrollView hsvTabs;
    private LinearLayout tabs;
    private ViewPager vpContent;
    private FragmentAdapter mFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hsvTabs = (HorizontalScrollView) findViewById(R.id.hsv_tabs);
        tabs = (LinearLayout) findViewById(R.id.ll_tabs);
        vpContent = (ViewPager) findViewById(R.id.vp_content);

        tabels = new ArrayList<>();
        views = new ArrayList<>();
        fragments = new ArrayList<>();
        Collections.addAll(tabels, "上装", "下装", "高级皮具养护", "床上寝室用品", "配件及其他", "鞋类", "活动促销");

        for (int i = 0; i < tabels.size(); i++) {
            TabFragment fragment = new TabFragment();
            Bundle bundle = new Bundle();
            bundle.putString("tabel", tabels.get(i));
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        mFragmentAdapter = new FragmentAdapter(
                getSupportFragmentManager(), fragments);
        vpContent.setAdapter(mFragmentAdapter);
        vpContent.setCurrentItem(0);
        vpContent.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(final int position) {
                switchTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        initTabels();
    }

    //初始化头部标签
    public void initTabels() {
        for (int i = 0; i < tabels.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_tab, null);
            TextView tabTv = (TextView) view.findViewById(R.id.tv_tab);
            tabTv.setText(tabels.get(i));
            tabs.addView(view);
            views.add(tabTv);
            final int finalI = i;
            tabTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vpContent.setCurrentItem(finalI);
                }
            });
        }
        switchTab(0);
    }

    //切换标签
    private void switchTab(int position) {
        //获取在当前窗口内的绝对坐标
        View view = tabs.getChildAt(position);
        int[] location = new int[2];
        view.getLocationInWindow(location);
        Log.e("", "view--->x坐标:" + location[0] + "view--->y坐标:" + location[1]);

        //获取屏幕x方向中间点的绝对坐标
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        Log.e("", "screenWidth:" + screenWidth);

        //将选中标签移动到中心位置，因为标签自己也具有宽度，所以要加 view.getWidth() / 2
        hsvTabs.scrollBy(location[0] + view.getWidth() / 2 - screenWidth / 2, 0);

        for (int i = 0; i < views.size(); i++) {
            views.get(i).setBackgroundResource(R.drawable.tv_green);
        }
        views.get(position).setTextColor(Color.WHITE);
        views.get(position).setBackgroundResource(R.drawable.tv_orange);
    }

}
