package com.kiristudio.jh.d_day;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;

/**
 * Created by lee on 2015-07-23.
 */
public class selectActivity3 extends Activity {

    @Override
    protected void onDestroy() {
        db.close();

        super.onDestroy();
    }

    public String DATABASE_NAME = "DDayDataBase";
    public int VER = 1;
    public String TABLE_NAME;

    ddayDatabaseHelper helper;
    SQLiteDatabase db;
    Cursor cur1;

    int total;

    String[] title2;
    Integer[] year2;
    Integer[] month2;
    Integer[] day2;


    String[] title;
    String[] date;
    long[] dddday;

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    ListView listView;


    public selectActivity3() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);

        setContentView(R.layout.activity_select);

        TextView textView = (TextView) findViewById(R.id.selectText);
        textView.setTypeface(Typeface.MONOSPACE);


        TextView tip = (TextView) findViewById(R.id.tip);
        tip.setTypeface(Typeface.MONOSPACE);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        listView = (ListView) findViewById(R.id.listView);

        final dayListAdapter adapter = new dayListAdapter(getApplicationContext());

        helper = new ddayDatabaseHelper(getApplicationContext(), DATABASE_NAME, null, VER);
        db = helper.getWritableDatabase();


        TABLE_NAME = helper.TABLE_NAME;

        CalculateDday newday = new CalculateDday(getApplicationContext());


        String[] args = {"0"};
        cur1 = db.rawQuery("select title, year, day, date from " + TABLE_NAME + " where title >= ?", args);

        total = cur1.getCount();

        Log.d("total", total + "");

        if (total > 0) {

            title = new String[total];
            dddday = new long[total];
            date = new String[total];

            title2 = new String[total];
            year2 = new Integer[total];
            month2 = new Integer[total];
            day2 = new Integer[total];


            for (int i = 0; i < total; i++) {
                cur1.moveToNext();

                // Toast toast = Toast.makeText(getApplicationContext(),"year : " + cur1.getInt(1) +"day : " + cur1.getInt(2)+"date : " + cur1.getInt(3),Toast.LENGTH_SHORT);
                //toast.show();


                String Fulldate = newday.FullDateForm(cur1.getInt(1), cur1.getInt(2), cur1.getInt(3));
                long FullDday = newday.CalculatedDday(cur1.getInt(1), cur1.getInt(2), cur1.getInt(3));

                title2[i] = (String) cur1.getString(0);
                year2[i] = (Integer) cur1.getInt(1);
                month2[i] = (Integer) cur1.getInt(2);
                day2[i] = (Integer) cur1.getInt(3);


                title[i] = (String) cur1.getString(0);
                dddday[i] = FullDday;
                date[i] = Fulldate;

                Log.d("content name", "title : " + title[i]);
                Log.d("content name", "dday : " + dddday[i]);
                Log.d("content name", "date : " + date[i]);
            }
        }
        cur1.close();


        listView.setAdapter(adapter);


        for (int i = 0; i < total; i++) {
            Log.d("content ", "title : " + title[i]);
            Log.d("content ", "dday : " + dddday[i]);
            Log.d("content ", "date : " + date[i]);
            addDday(adapter, title[i], date[i], dddday[i]);

        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());


                // 이거말고 cur 값을 저장해서 넘겨주기 그래야 갱신때 dday 계산해서 갱신이 됨
                SharedPreferences preferences = getSharedPreferences("dday", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                Log.e("select", mAppWidgetId + "");

                editor.putString("Title" + mAppWidgetId, title2[position]);
                editor.putInt("Year" + mAppWidgetId, year2[position]);
                editor.putInt("Day" + mAppWidgetId, Integer.valueOf(day2[position]));
                editor.putInt("Month" + mAppWidgetId, month2[position]);


                editor.commit();

            RemoteViews views = new RemoteViews(getApplicationContext().getPackageName(), R.layout.dday_widget3);

            long dday = adapter.items.get(position).getDday();

            if(dday>0)

            {
                views.setTextColor(R.id.ddayCount2, Color.WHITE);

                if (dday < 10) {
                    views.setTextColor(R.id.ddayCount2, Color.RED);
                }
                views.setTextViewText(R.id.ddayCount2, "D-" + dday);

            }

            else if(dday==0)

            {
                views.setTextColor(R.id.ddayCount2, Color.WHITE);
                views.setTextViewText(R.id.ddayCount2, "D-DAY");

            }

            else if(dday<0)

            {
                views.setTextColor(R.id.ddayCount2, Color.WHITE);
                views.setTextViewText(R.id.ddayCount2, "D+" + Math.abs(dday));

            }


            views.setTextViewText(R.id.ddayTitle2,adapter.items.get(position).

            getTitle()

            );
            views.setTextViewText(R.id.ddayDate2,adapter.items.get(position).

            getDate()

            );
            // views.setTextViewText(R.id.ddayCount2,dday);
               /* views.setString(R.id.ddayTitle2,null,adapter.items.get(position).getTitle());
                views.setString(R.id.ddayDate2, null, adapter.items.get(position).getDate());
                views.setLong(R.id.ddayCount2, null, adapter.items.get(position).getDday());
*/
            appWidgetManager.updateAppWidget(mAppWidgetId,views);


            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,mAppWidgetId);

            setResult(RESULT_OK, resultValue);

            finish();

        }
    }

    );


}

    public void addDday(dayListAdapter adapter, String title, String date, long dday) {

        ddayListItem item = new ddayListItem();
        item.setTitle(title);
        item.setDate(date);
        item.setDday(dday);

        adapter.addItem(item);


    }

}
