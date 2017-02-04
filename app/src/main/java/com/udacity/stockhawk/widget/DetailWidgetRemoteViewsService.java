package com.udacity.stockhawk.widget;

import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.ui.MainActivity;

import timber.log.Timber;

/**
 * Created by Xeda on 04-02-2017.
 */

public class DetailWidgetRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }
                data = getContentResolver().query(Contract.Quote.URI,
                        Contract.Quote.QUOTE_COLUMNS.toArray(new String[]{}),
                        null, null, Contract.Quote.COLUMN_SYMBOL);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION || data == null || !data.moveToPosition(position)) {
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.list_item_quote);
                data.moveToPosition(position);
                //Timber.d("  ==== " + data.getString(Contract.Quote.POSITION_SYMBOL));
                views.setTextViewText(R.id.symbol, data.getString(Contract.Quote.POSITION_SYMBOL));
                views.setTextViewText(R.id.price, "$"+data.getString(Contract.Quote.POSITION_PRICE));
                views.setTextViewText(R.id.change, data.getString(Contract.Quote.POSITION_ABSOLUTE_CHANGE));


                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.list_item_quote);

            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getLong(Contract.Quote.POSITION_ID);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
