package com.udacity.stockhawk.ui;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.udacity.stockhawk.model.StockHistoryModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Xeda on 03-02-2017.
 */

public class MyValueFormatter implements IValueFormatter {

    private DecimalFormat mFormat;
    public MyValueFormatter() {
        mFormat = new DecimalFormat("###,###,##0.00"); // use one decimal
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        // write your logic here
        return  "$"+ mFormat.format(value); // e.g. append a dollar-sign
    }
}