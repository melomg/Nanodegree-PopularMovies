package com.projects.melih.popularmovies.components;

import android.content.Context;
import android.support.annotation.DimenRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Melih Gültekin on 11.03.2018
 *
 * @see <a href="https://github.com/andrewlord1990/materialandroid/blob/master/sample/src/main/java/com/github/andrewlord1990/materialandroidsample/components/grids/GridAutoFitLayoutManager.java">GridAutoFitLayoutManager</a>
 *
 */

public class GridAutoFitLayoutManager extends GridLayoutManager {
    private static final int INITIAL_SPAN_COUNT = 2;
    private final int minColumnWidth;
    private boolean columnWidthChanged = true;

    public GridAutoFitLayoutManager(Context context, @DimenRes int minColumnWidth) {
        super(context, INITIAL_SPAN_COUNT);
        this.minColumnWidth = context.getResources().getDimensionPixelSize(minColumnWidth);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        int width = getWidth();
        int height = getHeight();
        if (columnWidthChanged && (minColumnWidth > 0) && (width > 0) && (height > 0)) {
            int totalSpace;
            if (getOrientation() == VERTICAL) {
                totalSpace = getWidth() - getPaddingRight() - getPaddingLeft();
            } else {
                totalSpace = getHeight() - getPaddingTop() - getPaddingBottom();
            }
            int spanCount = Math.max(1, totalSpace / minColumnWidth);
            setSpanCount(spanCount);
            columnWidthChanged = false;
        }
        super.onLayoutChildren(recycler, state);
    }
}