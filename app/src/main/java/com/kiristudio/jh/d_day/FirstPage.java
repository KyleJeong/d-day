package com.kiristudio.jh.d_day;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by lee on 2015-07-21.
 */
public class FirstPage extends RelativeLayout {

    public String DATABASE_NAME = "DDayDataBase";
    public int VER = 1;

    String mYear;
    String mMonth;
    String mDay;

    public FirstPage(Context context, int year, int month, int day) {
        super(context);


        mYear = "" + year;
        mDay = "" + day;
        //월 설정
        switch (month) {

            case 1:
                mMonth = "January";
                break;
            case 2:
                mMonth = "February";
                break;
            case 3:
                mMonth = "March";
                break;
            case 4:
                mMonth = "April";
                break;
            case 5:
                mMonth = "May";
                break;
            case 6:
                mMonth = "June";
                break;
            case 7:
                mMonth = "July";
                break;
            case 8:
                mMonth = "August";
                break;
            case 9:
                mMonth = "September";
                break;
            case 10:
                mMonth = "October";
                break;
            case 11:
                mMonth = "November";
                break;
            case 12:
                mMonth = "December";
                break;

        }

        init(context);
    }

    public FirstPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    public void init(Context context) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.first_page, this);

        TextView year = (TextView) findViewById(R.id.year);
        TextView month = (TextView) findViewById(R.id.month);
        TextView day = (TextView) findViewById(R.id.day);


        year.setTypeface(Typeface.MONOSPACE);
        day.setTypeface(Typeface.MONOSPACE);
        month.setTypeface(Typeface.MONOSPACE);

        year.setText(mYear);
        month.setText(mMonth);
        day.setText(mDay);


        ddayDatabaseHelper helper = new ddayDatabaseHelper(context, DATABASE_NAME, null, VER);
        SQLiteDatabase db = helper.getWritableDatabase();
        String TABLE_NAME = helper.TABLE_NAME;
        String[] args = {"0"};
        Cursor cur = db.rawQuery("select title from " + TABLE_NAME + " where title > ?", args);
        int totalCount = cur.getCount();

        if (totalCount != 0) {

            year.setText(mYear);
            month.setText(mMonth);
            day.setText(mDay);

        } else if (totalCount == 0) {

            year.setTypeface(Typeface.MONOSPACE);
            day.setTypeface(Typeface.MONOSPACE);
            month.setTypeface(Typeface.MONOSPACE);


            year.setTextSize(25);
            month.setTextSize(16);
            day.setTextSize(16);
            year.setText(R.string.hello1);
            month.setText(R.string.how);
            day.setText(R.string.delete);
            year.setPadding(0, 30, 0, 0);
            month.setPadding(0, 30, 0, 0);
            day.setPadding(0,5,0,0);
            day.setTextColor(Color.YELLOW);
        }


        cur.close();
        db.close();
    }
}
