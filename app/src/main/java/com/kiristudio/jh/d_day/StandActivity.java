package com.kiristudio.jh.d_day;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by lee on 2015-07-18.
 */
public class StandActivity extends Activity {

    TextView main, main2;
    ImageView loading;

    Handler handler, newh;

    MainThread thread;
    twoThread thread2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stand);


        main = (TextView) findViewById(R.id.standingtext);
        main2 = (TextView) findViewById(R.id.standingsubtext);

        loading = (ImageView) findViewById(R.id.loading);


        Typeface face = Typeface.createFromAsset(this.getAssets(), "segoeuisymbol.ttf");

        main.setTypeface(face);
        main2.setTypeface(face);


        //애니메이션 쓰레드 가 동작하는동안 같이 진행되며 1.4초후 토스트를 띄움
        thread = new MainThread();
        //애니메이션을 재생하기 위한 쓰레드
        thread2 = new twoThread();
        thread2.start();
        thread.start();

    }

    private class twoThread extends Thread {

        public twoThread() {
            handler = new Handler();
        }

        @Override
        public void run() {

            handler.post(new Runnable() {
                @Override
                public void run() {

                    //로딩 애니메이션 구현
                    Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
                    loading.startAnimation(anim);


                }
            });

        }
    }


    private class MainThread extends Thread {

        public MainThread() {

            newh = new Handler();
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1200);


                //1.4초 카운트

                newh.post(new Runnable() {
                    @Override
                    public void run() {

                        //넘어가는 효과 없이 전환
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);

                        finish();


                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            thread.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
