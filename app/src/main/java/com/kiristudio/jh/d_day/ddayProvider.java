package com.kiristudio.jh.d_day;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Created by lee on 2015-07-23.
 */
public class ddayProvider extends AppWidgetProvider {
    public static final String ACTION_CLICK = "com.kiristudio.widget.CLICK";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);


        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {

            Onupdate(context, appWidgetManager ,appWidgetIds[i]);


        }
    }

    public void Onupdate(Context context,AppWidgetManager appWidgetManager, int appWidgetIds){

        int appWidgetId = appWidgetIds;

        Log.e("provide", appWidgetId + "");

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.dday_widget);





        //cur값을 넘겨받아서 newday로 처리

        SharedPreferences preferences = context.getSharedPreferences("dday", Context.MODE_PRIVATE);


        String title1 = (String) preferences.getString("Title" + appWidgetId, "null");
        Integer year1 =  preferences.getInt("Year" + appWidgetId, 0000);
        Integer month1 =  preferences.getInt("Month" + appWidgetId, 0);
        Integer day = preferences.getInt("Day" + appWidgetId, 0);

        int day1 = day;

        CalculateDday newday = new CalculateDday(context);


        String Fulldate = newday.FullDateForm(year1, month1, day1);
        long FullDday = newday.CalculatedDday(year1, month1, day1);
        // helper = new ddayDatabaseHelper(context, DATABASE_NAME, null, VER);
        // db = helper.getWritableDatabase();

        //String[] args = {""+title1};
        //title1은 맞는데 rawQuery가 안됨......
        views.setTextViewText(R.id.ddayTitle2, title1);
        views.setTextViewText(R.id.ddayDate2, Fulldate);


        if (FullDday > 0) {
            views.setTextColor(R.id.ddayCount2, Color.WHITE);

            if (FullDday < 10) {
                views.setTextColor(R.id.ddayCount2, Color.RED);
            }
            views.setTextViewText(R.id.ddayCount2, "D-" + FullDday);

        } else if (FullDday == 0) {
            views.setTextColor(R.id.ddayCount2, Color.WHITE);
            views.setTextViewText(R.id.ddayCount2, "D-DAY");

        } else if (FullDday < 0) {
            views.setTextColor(R.id.ddayCount2, Color.WHITE);
            views.setTextViewText(R.id.ddayCount2, "D+" + Math.abs(FullDday));

        }

        Intent intent = new Intent(context, ddayProvider.class);
        intent.setAction(ACTION_CLICK);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pending = PendingIntent.getBroadcast(context, appWidgetId,
                intent, 0);

        views.setOnClickPendingIntent(R.id.imgbutton, pending);


        appWidgetManager.updateAppWidget(appWidgetIds, views);

    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d("Click", "recycled");

        String action = intent.getAction();

        if (action != null && action.equals(ACTION_CLICK)) {
            int id = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            Onupdate(context, AppWidgetManager.getInstance(context), id);   // 버튼이 클릭되면 새로고침 수행

        }



    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);

       for(int cococount= 0; cococount < appWidgetIds.length; cococount++){
            Log.e("delete", appWidgetIds[cococount] + "");
        }
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }
}
