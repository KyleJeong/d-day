package com.kiristudio.jh.d_day;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;

import com.fsn.cauly.CaulyAdInfo;
import com.fsn.cauly.CaulyAdInfoBuilder;
import com.fsn.cauly.CaulyCloseAd;
import com.fsn.cauly.CaulyCloseAdListener;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.SimpleSwipeUndoAdapter;

import java.util.Calendar;


//created by lee...
//앞을로 모든일이 잘 풀린다!!!!


public class MainActivity extends Activity implements CaulyCloseAdListener {

    private static final String APP_CODE = "VMLf7vPa"; // 광고 요청을 위한 App Code
    CaulyCloseAd mCloseAd ; // CloseAd광고 객체

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }




    public String DATABASE_NAME = "DDayDataBase";
    public int VER = 1;
    public String TABLE_NAME;

    public static DynamicListView dView;
    ImageButton create, log;
    ViewFlipper flipper;

    ddayDatabaseHelper helper;
    SQLiteDatabase db;
    Cursor cur1;

    dayListAdapter adapter;

    String[] title;
    String[] date;
    long[] dddday;

    float oldX;
    float oldY;

    float newX;
    float newY;

    int total;

    @Override
    protected void onResume() {
        super.onResume();
        invalidate();

        if (mCloseAd != null)
            mCloseAd.resume(this); // 필수 호출
    }

    public void invalidate() {


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


            for (int i = 0; i < total; i++) {
                cur1.moveToNext();

                // Toast toast = Toast.makeText(getApplicationContext(),"year : " + cur1.getInt(1) +"day : " + cur1.getInt(2)+"date : " + cur1.getInt(3),Toast.LENGTH_SHORT);
                //toast.show();

                String Fulldate = newday.FullDateForm(cur1.getInt(1), cur1.getInt(2), cur1.getInt(3));
                long FullDday = newday.CalculatedDday(cur1.getInt(1), cur1.getInt(2), cur1.getInt(3));

                title[i] = (String) cur1.getString(0);
                dddday[i] = FullDday;
                date[i] = Fulldate;

                Log.d("content name", "title : " + title[i]);
                Log.d("content name", "dday : " + dddday[i]);
                Log.d("content name", "date : " + date[i]);
            }
        }
        cur1.close();


        dView = (DynamicListView) findViewById(R.id.listView);


        adapter = new dayListAdapter(getApplicationContext());
        SimpleSwipeUndoAdapter simpleSwipeUndoAdapter = new SimpleSwipeUndoAdapter(adapter, getApplicationContext(), new DismissCallback(adapter));

        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(simpleSwipeUndoAdapter);
        animationAdapter.setAbsListView(dView);
        assert animationAdapter.getViewAnimator() != null;
        animationAdapter.getViewAnimator().setInitialDelayMillis(200);
        dView.setAdapter(animationAdapter);
        dView.enableSimpleSwipeUndo();
        //바로삭제
        // dView.enableSwipeToDismiss(new DismissCallback(adapter));


        for (int i = 0; i < total; i++) {
            Log.d("content ", "title : " + title[i]);
            Log.d("content ", "dday : " + dddday[i]);
            Log.d("content ", "date : " + date[i]);
            addDday(adapter, title[i], date[i], dddday[i]);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //CloseAd 초기화
        CaulyAdInfo closeAdInfo = new CaulyAdInfoBuilder(APP_CODE).build();
        mCloseAd = new CaulyCloseAd();
/* Optional
//원하는 버튼의 문구를 설정 할 수 있다.
mCloseAd.setButtonText("취소", "종료");
//원하는 텍스트의 문구를 설정 할 수 있다.
mCloseAd.setDescriptionText("종료하시겠습니까?");
*/
        mCloseAd.setAdInfo(closeAdInfo);
        mCloseAd.setCloseAdListener(this);
        mCloseAd.disableBackKey();// CaulyCloseAdListener 등록
// 종료광고 노출 후 back버튼 사용을 막기 원할 경우 disableBackKey();을 추가한다
// mCloseAd.disableBackKey();

        flipper = (ViewFlipper) findViewById(R.id.flipper);
        create = (ImageButton) findViewById(R.id.createbutton);
        log = (ImageButton) findViewById(R.id.log);

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater infltaer = getLayoutInflater();
                final View dialogView = infltaer.inflate(R.layout.how_to, null);


                AlertDialog.Builder bilder = new AlertDialog.Builder(MainActivity.this);

                bilder.setView(dialogView);

                bilder.setNeutralButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        dialog.cancel();
                    }
                });


                AlertDialog dialog = bilder.create();
                dialog.show();
            }
        });



        //button설정

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getApplicationContext(), InsertActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                finish();
            }
        });


        //첫페이지 - 날짜 정보 가져오기
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);

        FirstPage first = new FirstPage(getApplicationContext(), year, month, day);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        flipper.addView(first, params);
        //첫번째 뷰 추가

        //둘페이지 - 날짜 정보 가져오기
        SecondPage second = new SecondPage(getApplicationContext());
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        flipper.addView(second, params2);
        //두번째 뷰 추가

        LinearLayout mainpage1 = (LinearLayout) findViewById(R.id.mainpage);

        mainpage1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    oldX = event.getX();
                    oldY = event.getY();
                    Log.d("move", "" + oldX);

                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    newX = event.getX();
                    newY = event.getY();
                    Log.d("move", "" + oldX);
                    if (Math.abs(newX - oldX) > Math.abs(newY - oldY)) {
                        Log.d("move", "" + oldX);
                        if (newX - oldX < 0) {
                            Log.d("move", "" + oldX);
                            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_to_right);
                            flipper.setAnimation(anim);
                            flipper.showNext();
                        } else if (newX - oldX > 0) {
                            Log.d("move", "" + oldX);
                            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left);
                            flipper.setAnimation(anim);
                            flipper.showPrevious();
                        }

                    }

                }


                return true;
            }
        });


    }


    //추가시 호출
    public void addDday(dayListAdapter adapter, String title, String date, long dday) {

        ddayListItem item = new ddayListItem();
        item.setTitle(title);
        item.setDate(date);
        item.setDday(dday);

        adapter.addItem(item);


    }



    private class DismissCallback implements OnDismissCallback {

        private final dayListAdapter mAdapter;

        DismissCallback(dayListAdapter adapter) {
            mAdapter = adapter;
        }

        @Override
        public void onDismiss(ViewGroup viewGroup, int[] ints) {


            for (int position : ints) {

                Log.d("position", "" + position);

                String backTitle = adapter.removeTitle(position);
                Log.d("title", backTitle);

                db.execSQL("delete from " + TABLE_NAME + " where title = '" + backTitle + "'");
                invalidate();
            }

        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
// 앱을 처음 설치하여 실행할 때, 필요한 리소스를 다운받았는지 여부.


            if (mCloseAd.isModuleLoaded()) {
                mCloseAd.show(this);
            } else {
// 광고에 필요한 리소스를 한번만 다운받는데 실패했을 때 앱의 종료팝업 구현
                showDefaultClosePopup();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showDefaultClosePopup() {
        new android.support.v7.app.AlertDialog.Builder(this).setTitle("").setMessage("종료 하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("아니요", null)
                .show();
    }

    // CaulyCloseAdListener
    @Override
    public void onFailedToReceiveCloseAd(CaulyCloseAd ad, int errCode, String errMsg) {
    }

    // CloseAd의 광고를 클릭하여 앱을 벗어났을 경우 호출되는 함수이다.
    @Override
    public void onLeaveCloseAd(CaulyCloseAd ad) {
    }

    // CloseAd의 request()를 호출했을 때, 광고의 여부를 알려주는 함수이다.
    @Override

    public void onReceiveCloseAd(CaulyCloseAd ad, boolean isChargable) {
    }

    //왼쪽 버튼을 클릭 하였을 때, 원하는 작업을 수행하면 된다.
    @Override
    public void onLeftClicked(CaulyCloseAd ad) {
    }

    //오른쪽 버튼을 클릭 하였을 때, 원하는 작업을 수행하면 된다.
//Default로는 오른쪽 버튼이 종료로 설정되어있다.
    @Override
    public void onRightClicked(CaulyCloseAd ad) {
        finish();
    }

    @Override
    public void onShowedCloseAd(CaulyCloseAd ad, boolean isChargable) {
    }

}


