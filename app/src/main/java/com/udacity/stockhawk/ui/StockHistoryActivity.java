package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
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

public class StockHistoryActivity extends AppCompatActivity implements OnChartValueSelectedListener, OnChartGestureListener {
    private final String STOCK_DETAILS_KEY = "STOCK_DETAILS";

    private ArrayList<StockHistoryModel> stockHistoryModelArrayList;
    LineChart mChart;
    @BindView(R.id.selectedValue) TextView selectedValue;
    @BindView(R.id.todays_price_tv) TextView todaysPriceValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_history);
        ButterKnife.bind(this);

        mChart = (LineChart) findViewById(R.id.chart);
        // enable touch gestures
        mChart.setTouchEnabled(true);
        mChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(false);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setOnChartGestureListener(this);
        mChart.setMinOffset(1f);
        mChart.setExtraBottomOffset(18f);
        mChart.setDragOffsetX(40f);
        // no description text
        mChart.getDescription().setEnabled(false);
        mChart.getLegend().setEnabled(false);


        Bundle b = this.getIntent().getExtras();
        StockModel stockModel = b.getParcelable(STOCK_DETAILS_KEY);
        getSupportActionBar().setTitle(stockModel.getSymbol() + " Price History");
        selectedValue.setText("$"+stockModel.getPrice());
        todaysPriceValue.setText("$"+stockModel.getPrice());
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
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(7);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawGridLines(false);
        int color = ResourcesCompat.getColor(getResources(), R.color.chart_line_color, null); //without theme
        xAxis.setAxisLineColor(color);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new MyXAxisValueFormatter(stockHistoryModelArrayList));

        setData();
        mChart.animateX(1500);
        mChart.setVisibleXRangeMaximum(4f);
    }
    private String getDateDay(long timeInMillis){
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        return formatter.format(calendar.getTime());
    }

    public void setData(){
        ArrayList<Entry> entries = new ArrayList<Entry>();
        int i =1;
        for (StockHistoryModel data : stockHistoryModelArrayList) {
            // turn your data into Entry objects
            entries.add(new Entry((float) i, data.getCloseValue()));
            i = i+1;
       }
        LineDataSet dataSet = new LineDataSet(entries, "History Data"); // add entries to dataset
        dataSet.setColors(ColorTemplate.getHoloBlue());
        dataSet.setLineWidth(2f);
        dataSet.setDrawFilled(true);
        dataSet.setCircleRadius(2f);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fill_gradient);
        dataSet.setFillDrawable(drawable);
        dataSet.setValueTextColor(Color.WHITE);

        dataSet.setDrawHighlightIndicators(true);
        dataSet.setHighLightColor(Color.TRANSPARENT);

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
        selectedValue.setVisibility(View.VISIBLE);
        selectedValue.setText(String.valueOf(e.getY())+"$");

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

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "START, x: " + me.getX() + ", y: " + me.getY());
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "END, lastGesture: " + lastPerformedGesture);

        // un-highlight values after the gesture is finished and no single-tap
        if(lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
            mChart.highlightValues(null); // or highlightTouch(null) for callback to onNothingSelected(...)
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        Log.i("LongPress", "Chart longpressed.");
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.i("DoubleTap", "Chart double-tapped.");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.i("SingleTap", "Chart single-tapped.");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i("Fling", "Chart flinged. VeloX: " + velocityX + ", VeloY: " + velocityY);
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        selectedValue.setVisibility(View.INVISIBLE);
        Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);

    }
}
