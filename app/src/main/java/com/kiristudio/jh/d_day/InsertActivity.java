package com.kiristudio.jh.d_day;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by lee on 2015-07-21.
 */
public class InsertActivity extends Activity {

    String title;
    int year;
    int month;
    int date;

    Button add;
    EditText mtitle;
    Spinner mmonth, mdate, myear;
    Intent intent;

    int currentyear, currentMonth, currentDate;


    ddayDatabaseHelper helper;
    SQLiteDatabase db;

    public String TABLE_NAME;
    public String DATABASE_NAME = "DDayDataBase";
    public int VER = 1;


    @Override
    public void onBackPressed() {
        intent = new Intent(getApplicationContext(), MainActivity.class);
        backMain();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);


        mtitle = (EditText) findViewById(R.id.title);
        myear = (Spinner) findViewById(R.id.year);
        mmonth = (Spinner) findViewById(R.id.month);
        mdate = (Spinner) findViewById(R.id.date);

        add = (Button) findViewById(R.id.button);


        ArrayAdapter adaptermonth = ArrayAdapter.createFromResource(getApplicationContext(), R.array.month, R.layout.simple_item1);
        ArrayAdapter adapterdate = ArrayAdapter.createFromResource(getApplicationContext(), R.array.date, R.layout.simple_item1);
        ArrayAdapter adpateryear = ArrayAdapter.createFromResource(getApplicationContext(), R.array.year, R.layout.simple_item1);


        myear.setAdapter(adpateryear);
        mmonth.setAdapter(adaptermonth);
        mdate.setAdapter(adapterdate);

        myear.setSelection(Calendar.getInstance().get(Calendar.YEAR) - 1990);
        mmonth.setSelection(Calendar.getInstance().get(Calendar.MONTH));
        mdate.setSelection(Calendar.getInstance().get(Calendar.DATE) - 1);

        myear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentyear = position + 1990;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Toast toast1 = Toast.makeText(getApplicationContext(), R.string.null2, Toast.LENGTH_SHORT);
                toast1.show();
            }
        });

        mmonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentMonth = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Toast toast3 = Toast.makeText(getApplicationContext(), R.string.null3, Toast.LENGTH_SHORT);
                toast3.show();

            }
        });

        mdate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentDate = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Toast toast4 = Toast.makeText(getApplicationContext(), R.string.null4, Toast.LENGTH_SHORT);
                toast4.show();

            }
        });


        add.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    add.setTextColor(Color.BLACK);
                    add.setBackgroundColor(Color.WHITE);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    add.setTextColor(Color.WHITE);
                    add.setBackgroundColor(Color.BLACK);
                }
                return false;
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mtitle.getText().length() == 0) {
                    Toast toast1 = Toast.makeText(getApplicationContext(), R.string.null1, Toast.LENGTH_SHORT);
                    toast1.show();

                } else {


                    title = mtitle.getText().toString().trim();
                    year = currentyear;
                    month = currentMonth;
                    date = currentDate;

                    putData(title, year, month, date);


                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    backMain();
                }


            }
        });


    }

    public void putData(String title, int year, int month, int date) {

        helper = new ddayDatabaseHelper(getApplicationContext(), DATABASE_NAME, null, VER);
        db = helper.getWritableDatabase();
        TABLE_NAME = helper.TABLE_NAME;

        db.execSQL("insert into " + TABLE_NAME + "(title, year, day, date) values ('" + title + "', " + year + ", " + month + ", " + date + ");");


    }

    public void backMain() {

        startActivity(intent);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
