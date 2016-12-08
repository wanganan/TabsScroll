package com.laundrylangpickcargo.tabsscroll;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zcc on 2016/5/19.
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    List<Fragment> fragmentList = new ArrayList<>();
    public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment ff  = (Fragment)super.instantiateItem(container, position);
        return ff;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
