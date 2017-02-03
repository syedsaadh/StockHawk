package com.udacity.stockhawk.ui;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.model.StockHistoryModel;

import java.util.ArrayList;

/**
 * Created by Xeda on 03-02-2017.
 */

public class MyMarkerView extends MarkerView {

    private TextView tvContent;
    private ArrayList<StockHistoryModel> stockHistoryModelArrayList;
    public MyMarkerView(Context context, int layoutResource, ArrayList<StockHistoryModel> stockHistoryModelArrayList) {
        super(context, layoutResource);
        this.stockHistoryModelArrayList = stockHistoryModelArrayList;
        // find your layout components
        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        StockHistoryModel stockHistoryModel = stockHistoryModelArrayList.get((int)highlight.getX()-1);

        tvContent.setText("" + e.getY() + "$, " + stockHistoryModel.getTimeString());

        // this will perform necessary layouting
        super.refreshContent(e, highlight);
    }

    private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {

        if(mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
        }

        return mOffset;
    }
}