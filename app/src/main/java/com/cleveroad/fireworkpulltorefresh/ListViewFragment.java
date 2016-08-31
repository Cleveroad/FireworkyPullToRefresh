package com.cleveroad.fireworkpulltorefresh;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cleveroad.ptr.FireworkyPullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListViewFragment extends Fragment implements FireworkyPullToRefreshLayout.OnRefreshListener {
    public static final int REFRESH_DELAY = 4500;
    private static final int ITEMS_COUNT = 25;
    private static final List<Object> mDummyList;
    static {
        mDummyList = new ArrayList<>(ITEMS_COUNT);
        for (int i = 0; i < ITEMS_COUNT; i++) {
            mDummyList.add(new Object());
        }
    }

    @BindView(R.id.listView)
    ListView mListView;
    @BindView(R.id.pullToRefresh)
    FireworkyPullToRefreshLayout mPullRefreshView;

    private boolean mIsRefreshing;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listview, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPullRefreshView.setOnRefreshListener(this);

        mListView.setAdapter(new SampleAdapter(getActivity(), R.layout.list_item, mDummyList));

        mPullRefreshView.post(new Runnable() {
            @Override
            public void run() {
                mPullRefreshView.setRefreshing(mIsRefreshing);
            }
        });
    }

    @Override
    public void onRefresh() {
        mIsRefreshing = true;
        mPullRefreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPullRefreshView.setRefreshing(mIsRefreshing = false);
            }
        }, REFRESH_DELAY);
    }

    class SampleAdapter extends ArrayAdapter<Object> {
        private final LayoutInflater mInflater;

        SampleAdapter(Context context, int layoutResourceId, List<Object> list) {
            super(context, layoutResourceId, list);
            mInflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.list_item, parent, false);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            return convertView;
        }

        class ViewHolder {
            //put your views here
        }
    }
}
