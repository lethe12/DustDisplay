package com.ScrollablePanel;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by weifeng on 2017/9/14.
 */

public class HistoryDataPanelAdapter extends PanelAdapter{
    @Override
    public int getRowCount() {
        return 0;
    }

    @Override
    public int getColumnCount() {
        return 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int row, int column) {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }
}
