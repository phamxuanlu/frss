package com.framgia.lupx.frss.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.framgia.lupx.frss.AppConfigs;
import com.framgia.lupx.frss.R;
import com.framgia.lupx.frss.asynctasks.LoadImageAsyncTask;
import com.framgia.lupx.frss.models.RSSCategory;
import com.framgia.lupx.frss.models.RSSItem;

/**
 * Created by FRAMGIA\pham.xuan.lu on 27/07/2015.
 */
public class CategoryDetailAdapter extends RecyclerView.Adapter<CategoryDetailAdapter.ItemViewHolder> {

    private RSSCategory category;
    private Context context;
    private LayoutInflater inflater;
    private boolean isGrid;
    private RecyclerViewItemClickListener listener;

    public CategoryDetailAdapter(Context context, RSSCategory category, boolean isGrid) {
        this.category = category;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.isGrid = isGrid;
    }

    public void setOnItemClickListener(RecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (isGrid) {
            view = inflater.inflate(R.layout.category_grid_item, parent, false);
        } else {
            view = inflater.inflate(R.layout.category_list_item, parent, false);
        }
        return new ItemViewHolder(view);
    }

    @Override

    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        RSSItem item = category.items.get(position);
        holder.txtTitle.setText(item.title);
        holder.txtDes.setText(item.description);
        if (holder.task != null && !holder.task.isCancelled()) {
            holder.task.cancel(true);
        }
        if (item.thumbnail != null && !item.thumbnail.equals("")) {
            holder.task = new LoadImageAsyncTask(context, holder.img);
            holder.task.execute(item.thumbnail);
            holder.img.setVisibility(View.VISIBLE);
        } else {
            holder.img.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(v, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return category.items == null ? 0 : category.items.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;
        public TextView txtTitle;
        public TextView txtDes;
        public LoadImageAsyncTask task;

        public ItemViewHolder(View view) {
            super(view);
            img = (ImageView) view.findViewById(R.id.imgFeatureImage);
            txtDes = (TextView) view.findViewById(R.id.txtDescription);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            txtTitle.setTypeface(AppConfigs.getInstance().ROBOTO_CL);
            txtDes.setTypeface(AppConfigs.getInstance().ROBOTO_CL);
        }
    }

}