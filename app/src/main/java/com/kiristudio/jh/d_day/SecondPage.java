package com.kiristudio.jh.d_day;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.DigitalClock;
import android.widget.RelativeLayout;

/**
 * Created by lee on 2015-07-23.
 */
public class SecondPage extends RelativeLayout {

    Context mContext;

    public SecondPage(Context context) {
        super(context);
        init(context);
    }

    public SecondPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context){
        mContext = context;


        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.second_page, this);


        DigitalClock clock = (DigitalClock)findViewById(R.id.digitalClock);
        clock.setTypeface(Typeface.MONOSPACE);
        clock.setTextColor(Color.WHITE);








    }



}
