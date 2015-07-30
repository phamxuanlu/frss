package com.framgia.lupx.frss.fragments;

import android.app.Activity;
import android.content.Intent;
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
import com.framgia.lupx.frss.activities.ArticleDetailActivity;
import com.framgia.lupx.frss.adapters.CategoryDetailAdapter;
import com.framgia.lupx.frss.adapters.RecyclerViewItemClickListener;
import com.framgia.lupx.frss.models.RSSCategory;
import com.framgia.lupx.frss.utils.DisplayUtils;
import com.framgia.lupx.frss.widgets.GridItemDecoration;

/**
 * Created by FRAMGIA\pham.xuan.lu on 27/07/2015.
 */
public class CategoryDetailFragment extends Fragment {

    public static final String IS_SHOW_GRID = "IS_SHOW_GRID";
    private static final int GRID_SPACING_DP = 10;
    private static final int GRID_COLUMNS = 2;
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
        view = inflater.inflate(R.layout.category_detail_fragment, container, false);
        setupViews(view);
        return view;
    }

    private void setupViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.listItems);
        isShowGrid = getArguments().getBoolean(IS_SHOW_GRID);
        if (isShowGrid) {
            adapter = new CategoryDetailAdapter(getActivity(), category, isShowGrid);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            recyclerView.addItemDecoration(new GridItemDecoration(DisplayUtils.dpToPixels(GRID_SPACING_DP), GRID_COLUMNS));
            recyclerView.setAdapter(adapter);
        } else {
            adapter = new CategoryDetailAdapter(getActivity(), category, isShowGrid);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapter);
        }
        adapter.setOnItemClickListener(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Intent intent = new Intent(getActivity(), ArticleDetailActivity.class);
                intent.putExtra(ArticleDetailActivity.ARTICLE_URL, category.items.get(position).link);
                getActivity().startActivity(intent);
            }
        });
    }

}