package com.kiristudio.jh.d_day;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by lee on 2015-07-19.
 */
public class dayListContent extends RelativeLayout {

    Context mContext;
    TextView title, date, dday;

    long curDay = 0;

    public dayListContent(Context context) {
        super(context);

        mContext = context;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.d_day_content, this);


        dday = (TextView) findViewById(R.id.ddayCount);
        date = (TextView) findViewById(R.id.ddayDate);
        title = (TextView) findViewById(R.id.ddayTitle);



        dday.setTypeface(Typeface.MONOSPACE);
        title.setTypeface(Typeface.MONOSPACE);
        date.setTypeface(Typeface.MONOSPACE,Typeface.ITALIC);
        /*
        ???작동이 잘 안됨?? why??
        if (curDay <= 10 && curDay >= 0) {
            dday.setTextColor(Color.RED);

        } else {
            dday.setTextColor(Color.argb(255, 198, 198, 198));
        }*/
        title.setTextColor(Color.argb(255, 198, 198, 198));


    }

    public dayListContent(Context context, AttributeSet attrs) {
        super(context, attrs);


    }


    public void setDday(long dday) {
//날짜별 색 변환 포함
        curDay = dday;

        if (dday > 0) {
            this.dday.setTextColor(Color.argb(255, 198, 198, 198));

            if (dday < 10) {
                this.dday.setTextColor(Color.RED);
            }
            this.dday.setText("D-");
            String dayplus = "" + dday;
            this.dday.append(dayplus);

        } else if (dday == 0) {
            this.dday.setTextColor(Color.argb(255, 198, 198, 198));
            this.dday.setText("D-DAY");

        } else if (dday < 0) {
            this.dday.setTextColor(Color.argb(255, 198, 198, 198));
            this.dday.setText("D+");
            String dayplus = "" + Math.abs(dday);
            this.dday.append(dayplus);
        }

    }

    public long getDday() {


        return curDay;
    }

    public void setDate(String date) {

        this.date.setText(date);
    }

    public String getDate() {
        String date = this.date.getText().toString();

        return date;
    }

    public void setTitle(String title) {

        this.title.setText(title);
    }

    public String getTitle() {
        String title = this.title.getText().toString();

        return title;
    }
}
