package com.framgia.lupx.frss.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.lupx.frss.R;
import com.framgia.lupx.frss.adapters.CategoryDetailAdapter;
import com.framgia.lupx.frss.models.RSSCategory;

/**
 * Created by FRAMGIA\pham.xuan.lu on 27/07/2015.
 */
public class CategoryDetailFragment extends Fragment {

    public static final String IS_SHOW_GRID = "IS_SHOW_GRID";
    private RecyclerView recyclerView;
    private CategoryDetailAdapter adapter;
    private View view;
    private RSSCategory category;
    private boolean isShowGrid;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = getActivity();
        if (activity instanceof GetDataCallback) {
            category = ((GetDataCallback<RSSCategory>) activity).getData();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.category_detail_fragment, container, false);
            setupViews(view);
        } else {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        return view;
    }

    private void setupViews(View view) {
        isShowGrid = getArguments().getBoolean(IS_SHOW_GRID);
        if (isShowGrid) {
            recyclerView = (RecyclerView) view.findViewById(R.id.listItems);
            adapter = new CategoryDetailAdapter(getActivity(), category, isShowGrid);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            recyclerView.setAdapter(adapter);
        } else {
            recyclerView = (RecyclerView) view.findViewById(R.id.listItems);
            adapter = new CategoryDetailAdapter(getActivity(), category, isShowGrid);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapter);
        }
    }

}