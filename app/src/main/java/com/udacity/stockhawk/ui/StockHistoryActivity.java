package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.model.StockHistoryModel;
import com.udacity.stockhawk.model.StockModel;

import java.sql.Time;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class StockHistoryActivity extends AppCompatActivity implements OnChartValueSelectedListener {
    private final String STOCK_DETAILS_KEY = "STOCK_DETAILS";

    private ArrayList<StockHistoryModel> stockHistoryModelArrayList;
    LineChart mChart;
//    @BindView(R.id.textView) TextView abc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_history);
        //ButterKnife.bind(this);
        mChart = (LineChart) findViewById(R.id.chart);
        // enable touch gestures
        mChart.setTouchEnabled(true);
        mChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(false);
        mChart.setOnChartValueSelectedListener(this);

        // no description text
        mChart.getDescription().setEnabled(false);
        mChart.getLegend().setEnabled(false);


        Bundle b = this.getIntent().getExtras();
        StockModel stockModel = b.getParcelable(STOCK_DETAILS_KEY);

        stockHistoryModelArrayList = stockModel.getHistoryArray();

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setEnabled(false);
        leftAxis.setTextSize(12f);
        leftAxis.setDrawGridLines(false);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawAxisLine(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setCenterAxisLabels(true);
        xAxis.setTextSize(10f);
        xAxis.setDrawGridLines(false);

//        int color = ResourcesCompat.getColor(getResources(), R.color.material_blue_500, null); //without theme
//        xAxis.setGridColor(color);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return "";
            }
        });
        IMarker marker = new MyMarkerView(this, R.layout.custom_marker_view, stockHistoryModelArrayList);
        mChart.setMarker(marker);
        setData();
        mChart.animateX(2500);
    }
    private String getDateDay(long timeInMillis){
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        return formatter.format(calendar.getTime());
    }

//    private long getDayMillis(long timeInMillis){
//        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(timeInMillis);
//        String dateStr = formatter.format(calendar.getTime());
//        long dateRtn =0000;
//        try{
//            Date date = formatter.parse(dateStr);
//            Timber.d(String.valueOf(date.getTime()));
//            dateRtn = date.getTime();
//            return dateRtn;
//        }
//        catch (ParseException e){
//            Timber.d(e);
//            return dateRtn;
//        }
//        finally {
//            return dateRtn;
//        }
//
//    }
    public void setData(){
        ArrayList<Entry> entries = new ArrayList<Entry>();
        float i =1f;
        for (StockHistoryModel data : stockHistoryModelArrayList) {
            // turn your data into Entry objects
            entries.add(new Entry(i++, data.getCloseValue()));
//            Timber.i(data.getCloseValue()+ " == "+ getDateDay(data.getTimeInMillis()));
        }
        LineDataSet dataSet = new LineDataSet(entries, "History Data"); // add entries to dataset
        dataSet.setColors(ColorTemplate.getHoloBlue());
        dataSet.setLineWidth(2f);
        dataSet.setDrawFilled(true);
        dataSet.setCircleRadius(2f);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fill_gradient);
        dataSet.setFillDrawable(drawable);
        dataSet.setValueTextColor(Color.WHITE);
        LineData lineData = new LineData(dataSet);
        lineData.setValueFormatter(new MyValueFormatter());

        mChart.setData(lineData);
        mChart.invalidate(); // refresh
    }

    protected RectF mOnValueSelectedRectF = new RectF();

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;
        RectF bounds = mOnValueSelectedRectF;
        MPPointF position = mChart.getPosition(e, YAxis.AxisDependency.LEFT);
        Timber.i(String.valueOf(h.getX()));
        Log.i("bounds", bounds.toString());
        Log.i("position", position.toString());

        Log.i("x-index",
                "low: " + mChart.getLowestVisibleX() + ", high: "
                        + mChart.getHighestVisibleX());

        MPPointF.recycleInstance(position);
    }
    @Override
    public void onNothingSelected() { }
}
