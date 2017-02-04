package com.udacity.stockhawk.ui;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.udacity.stockhawk.model.StockHistoryModel;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by Xeda on 03-02-2017.
 */

public class MyXAxisValueFormatter implements IAxisValueFormatter {

    private ArrayList<StockHistoryModel> stockHistoryModelArrayList;
    public MyXAxisValueFormatter(ArrayList<StockHistoryModel> stockHistoryModelArrayList) {
        this.stockHistoryModelArrayList = stockHistoryModelArrayList;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
        int count = stockHistoryModelArrayList.size();
        if(value < count){
            StockHistoryModel stockHistoryModel = stockHistoryModelArrayList.get((int)value-1);
            return stockHistoryModel.getTimeString();
        }
        //Timber.e("Stock == "+ stockHistoryModel.getTimeString());
        return "";
    }

    /** this is only needed if numbers are returned, else return 0 */
    public int getDecimalDigits() { return 0; }
}