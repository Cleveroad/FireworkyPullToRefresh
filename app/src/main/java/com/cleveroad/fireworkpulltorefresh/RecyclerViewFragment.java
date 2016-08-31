package com.cleveroad.fireworkpulltorefresh;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cleveroad.ptr.FireworkyPullToRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewFragment extends Fragment {
    private static final int REFRESH_DELAY = 4500;
    private static final int ITEMS_COUNT = 25;

    @BindView(R.id.pullToRefresh)
    FireworkyPullToRefreshLayout mPullToRefresh;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private boolean mIsRefreshing;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecyclerView();
        initRefreshView();

        mPullToRefresh.post(new Runnable() {
            @Override
            public void run() {
                mPullToRefresh.setRefreshing(mIsRefreshing);
            }
        });
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new SampleAdapter());
    }

    private void initRefreshView() {
        mPullToRefresh.setOnRefreshListener(new FireworkyPullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mIsRefreshing = true;
                mPullToRefresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefresh.setRefreshing(mIsRefreshing = false);
                    }
                }, REFRESH_DELAY);
            }
        });
    }

    class SampleAdapter extends RecyclerView.Adapter<RecyclerViewFragment.SampleHolder> {
        @Override
        public RecyclerViewFragment.SampleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new RecyclerViewFragment.SampleHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerViewFragment.SampleHolder holder, int position) {
        }

        @Override
        public int getItemCount() {
            return ITEMS_COUNT;
        }
    }

    static class SampleHolder extends RecyclerView.ViewHolder {
        SampleHolder(View itemView) {
            super(itemView);
        }
    }
}
