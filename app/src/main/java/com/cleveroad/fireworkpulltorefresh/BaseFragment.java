package com.cleveroad.fireworkpulltorefresh;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

abstract class BaseFragment extends Fragment {
    public static final int REFRESH_DELAY = 4500;

    protected List<Integer> colors;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int colorArray[] = getResources().getIntArray(R.array.fireworkColors);
        colors = new ArrayList<>(colorArray.length);
        for (int color : colorArray) {
            colors.add(color);
        }
    }
}
