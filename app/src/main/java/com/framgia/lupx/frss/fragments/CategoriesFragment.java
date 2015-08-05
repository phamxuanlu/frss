package com.framgia.lupx.frss.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.lupx.frss.AppConfigs;
import com.framgia.lupx.frss.FakeData;
import com.framgia.lupx.frss.R;
import com.framgia.lupx.frss.activities.CategoryDetailActivity;
import com.framgia.lupx.frss.adapters.ListCategoriesAdapter;
import com.framgia.lupx.frss.adapters.RecyclerViewItemClickListener;
import com.framgia.lupx.frss.database.DatabaseHelper;
import com.framgia.lupx.frss.models.RSSCategory;
import com.framgia.lupx.frss.utils.DisplayUtils;
import com.framgia.lupx.frss.widgets.GridItemDecoration;

import java.util.List;

/**
 * Created by FRAMGIA\pham.xuan.lu on 24/07/2015.
 */
public class CategoriesFragment extends Fragment {

    private List<RSSCategory> categories;
    private RecyclerView listCategories;
    private ListCategoriesAdapter adapter;
    private static final int GRID_SPACING_DP = 4;
    private static final int GRID_COLUMNS = 2;
    private boolean isLoadFavorite;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isLoadFavorite) {
            try {
                DatabaseHelper<RSSCategory> dbHelper = new DatabaseHelper<RSSCategory>(getActivity(), AppConfigs.getInstance().DATABASE_NAME, RSSCategory.class);
                categories = dbHelper.getAllRows();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            categories = FakeData.getInstance().fakeCategories;
        }
    }

    public void setLoadFavorite(boolean isLoadFavorite) {
        this.isLoadFavorite = isLoadFavorite;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_news_categories, container, false);
        listCategories = (RecyclerView) view.findViewById(R.id.listCategories);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), GRID_COLUMNS);
        listCategories.addItemDecoration(new GridItemDecoration(DisplayUtils.dpToPixels(GRID_SPACING_DP), GRID_COLUMNS));
        listCategories.setLayoutManager(layoutManager);
        adapter = new ListCategoriesAdapter(getActivity(), categories);
        listCategories.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Intent intent = new Intent(getActivity(), CategoryDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(CategoryDetailActivity.CATEGORY_URL_ID, categories.get(position).url);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });
        return view;
    }

}