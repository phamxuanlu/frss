package com.framgia.lupx.frss.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.framgia.lupx.frss.AppConfigs;
import com.framgia.lupx.frss.R;
import com.framgia.lupx.frss.models.NavDrawerItem;

import java.util.List;

/**
 * Created by FRAMGIA\pham.xuan.lu on 24/07/2015.
 */
public class NavDrawerAdapter extends RecyclerView.Adapter<NavDrawerAdapter.NavViewHolder> {

    private List<NavDrawerItem> items;
    private Context context;
    private LayoutInflater inflater;
    private RecyclerViewItemClickListener listener;

    public NavDrawerAdapter(Context context, List<NavDrawerItem> items) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.items = items;
    }

    public void setOnItemClickListener(RecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public NavViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = inflater.inflate(R.layout.nav_drawer_row, viewGroup, false);
        return new NavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NavViewHolder holder, final int position) {
        NavDrawerItem item = items.get(position);
        holder.name.setText(item.name);
        holder.name.setTypeface(AppConfigs.getInstance().ROBOTO_LIGHT);
        holder.icon.setImageResource(item.icon);
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
        return items == null ? 0 : items.size();
    }

    public class NavViewHolder extends RecyclerView.ViewHolder {
        public ImageView icon;
        public TextView name;

        public NavViewHolder(View view) {
            super(view);
            icon = (ImageView) view.findViewById(R.id.nav_item_icon);
            name = (TextView) view.findViewById(R.id.nav_item_name);
        }
    }

}