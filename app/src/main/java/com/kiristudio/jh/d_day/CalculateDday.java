package com.kiristudio.jh.d_day;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by lee on 2015-07-22.
 */
public class CalculateDday {




    Context mContext;



    public CalculateDday(Context context) {
        mContext = context;

    }

    public String FullDateForm(int year, int month, int date){

        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month-1,date);

        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
        String Date = form.format( calendar.getTime());

        return Date;
    }


    public long CalculatedDday(int year, int month, int date) {

        Calendar calendar = Calendar.getInstance();

        calendar.set(year, month - 1, date);

        //현재 날짜 반환
        Calendar today = Calendar.getInstance();

        //calendar 와 today를 시간형식으로 변환
        long ddayTime = calendar.getTimeInMillis();
        long todayTime = today.getTimeInMillis();


        long Dday = ddayTime - todayTime;

        //Dday의 폼 = 초단위
        //day로 변환
        long finalDay = Dday / 86400000 ;


        //d+를 위해서 조건 주기
        //calculate 메서드로 동시 관리
        //DataBase 는  각 날자를 integer 로 가지게 수정
        //getView 시에만 calculate를 이용해서 현재의  dday를 갱신하는 방식


        return finalDay;

    }
}
