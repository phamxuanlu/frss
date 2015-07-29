package com.framgia.lupx.frss.widgets;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by FRAMGIA\pham.xuan.lu on 28/07/2015.
 */
public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private int spacingPx;
    private int gridSize;
    private boolean needLeftSpacing = false;

    public GridItemDecoration(int gridSpacingPx, int gridSize) {
        this.spacingPx = gridSpacingPx;
        this.gridSize = gridSize;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int frameWidth = (int) ((parent.getWidth() - (float) spacingPx * (gridSize - 1)) / gridSize);
        int padding = parent.getWidth() / gridSize - frameWidth;
        int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();
        if (itemPosition < gridSize) {
            outRect.top = 0;
        } else {
            outRect.top = spacingPx;
        }
        if (itemPosition % gridSize == 0) {
            outRect.left = 0;
            outRect.right = padding;
            needLeftSpacing = true;
        } else if ((itemPosition + 1) % gridSize == 0) {
            needLeftSpacing = false;
            outRect.right = 0;
            outRect.left = padding;
        } else if (needLeftSpacing) {
            needLeftSpacing = false;
            outRect.left = spacingPx - padding;
            if ((itemPosition + 2) % gridSize == 0) {
                outRect.right = spacingPx - padding;
            } else {
                outRect.right = spacingPx / 2;
            }
        } else if ((itemPosition + 2) % gridSize == 0) {
            needLeftSpacing = false;
            outRect.left = spacingPx / 2;
            outRect.right = spacingPx - padding;
        } else {
            needLeftSpacing = false;
            outRect.left = spacingPx / 2;
            outRect.right = spacingPx / 2;
        }
        outRect.bottom = 0;
    }

}