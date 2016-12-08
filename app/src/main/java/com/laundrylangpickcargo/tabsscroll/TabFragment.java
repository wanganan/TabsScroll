package com.laundrylangpickcargo.tabsscroll;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Sinaan on 2016/12/8.
 */
public class TabFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab,null);
        ((TextView) view.findViewById(R.id.tab)).setText("这是第"+getArguments().getInt("position")+"个");
        return view;
    }
}
