package com.kiristudio.jh.d_day;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by lee on 2015-07-24.
 */
public class dateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


            AppWidgetManager mgr = AppWidgetManager.getInstance(context);

            Intent update = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

            update.setClass(context, ddayProvider.class);

            update.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, mgr.getAppWidgetIds(new ComponentName(context, ddayProvider.class)));

            context.sendBroadcast(update);


            Log.d("update","broadcast date");




    }
}
