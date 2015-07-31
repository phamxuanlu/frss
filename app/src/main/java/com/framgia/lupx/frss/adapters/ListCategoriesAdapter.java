package com.framgia.lupx.frss.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.framgia.lupx.frss.AppConfigs;
import com.framgia.lupx.frss.R;
import com.framgia.lupx.frss.models.RSSCategory;

import java.util.List;

/**
 * Created by FRAMGIA\pham.xuan.lu on 24/07/2015.
 */
public class ListCategoriesAdapter extends RecyclerView.Adapter<ListCategoriesAdapter.CategoryViewHolder> {

    private Context context;
    private List<RSSCategory> categories;
    private LayoutInflater inflater;
    private RecyclerViewItemClickListener listener;

    public ListCategoriesAdapter(Context context, List<RSSCategory> data) {
        this.context = context;
        this.categories = data;
        this.inflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(RecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = inflater.inflate(R.layout.news_category_row, viewGroup, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, final int position) {
        RSSCategory category = categories.get(position);
        holder.lbCategoryName.setText(category.name);
        holder.lbCategoryName.setTypeface(AppConfigs.getInstance().ROBOTO_LIGHT);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories == null ? 0 : categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        public TextView lbCategoryName;

        public CategoryViewHolder(View view) {
            super(view);
            lbCategoryName = (TextView) view.findViewById(R.id.lbCategoryName);
        }
    }

}