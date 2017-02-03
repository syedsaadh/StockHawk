package com.udacity.stockhawk.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xeda on 02-02-2017.
 */

public class StockModel implements Parcelable {
    private  String symbol;
    private  String price;
    private  String absoluteChange;
    private  String percentageChange;
    private  String history;
    public StockModel(String symbol, String price, String absoluteChange, String percentageChange, String history){
        this.symbol = symbol;
        this.price = price;
        this.absoluteChange = absoluteChange;
        this.percentageChange = percentageChange;
        this.history = history;
    }

    public  String getSymbol() {
        return symbol;
    }

    public  String getPrice() {
        return price;
    }

    public  String getAbsoluteChange() {
        return absoluteChange;
    }

    public  String getHistory() {
        return history;
    }

    public  String getPercentageChange() {
        return percentageChange;
    }

    public ArrayList<StockHistoryModel> getHistoryArray(){
        ArrayList<StockHistoryModel> stockHistoryArray = new ArrayList<StockHistoryModel>();
        String[] lines = this.history.split("\n");
        for (String line : lines) {
            String[] substrings = line.split(", ");
            StockHistoryModel stockHistory = new StockHistoryModel(substrings[0], substrings[1]);
            stockHistoryArray.add(stockHistory);
        }
        return stockHistoryArray;
    }

    public void setAbsoluteChange(String absoluteChange) {
        this.absoluteChange = absoluteChange;
    }

    public  void setPercentageChange(String percentageChange) {
        this.percentageChange = percentageChange;
    }

    public  void setHistory(String history) {
        this.history = history;
    }

    public  void setPrice(String price) {
        this.price = price;
    }

    public  void setSymbol(String symbol) {
        this.symbol = symbol;
    }


    protected StockModel(Parcel in) {
        symbol = in.readString();
        price = in.readString();
        absoluteChange = in.readString();
        percentageChange = in.readString();
        history = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(symbol);
        dest.writeString(price);
        dest.writeString(absoluteChange);
        dest.writeString(percentageChange);
        dest.writeString(history);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<StockModel> CREATOR = new Parcelable.Creator<StockModel>() {
        @Override
        public StockModel createFromParcel(Parcel in) {
            return new StockModel(in);
        }

        @Override
        public StockModel[] newArray(int size) {
            return new StockModel[size];
        }
    };
}