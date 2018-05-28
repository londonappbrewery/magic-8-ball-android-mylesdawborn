package com.londonappbrewery.magiceightball;

import android.app.Activity;
import android.content.res.TypedArray;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import java.util.Random;

import com.squareup.seismic.ShakeDetector;

import static android.view.Gravity.CENTER;
import static android.view.ViewGroup.LayoutParams;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class MainActivity extends AppCompatActivity implements ShakeDetector.Listener {

    ShakeDetector sd = new ShakeDetector(this);
    SensorManager sensorManager;
    TextView flashShake;

    private void fadeOutAndHideImage(final ImageView answerTriangle) {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(1000);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                answerTriangle.setVisibility(View.GONE);
                sensorDetect();
                textBlink();
            }
            public void onAnimationRepeat(Animation animation) {

            }
            public void onAnimationStart(Animation animation) {

            }
        });

        answerTriangle.startAnimation(fadeOut);
    }

    public void sensorDetect () {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sd.start(sensorManager);
    }

    public void textBlink () {
        flashShake = findViewById(R.id.flashShake);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.blink);
        flashShake.startAnimation(anim);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VideoView videoView = findViewById(R.id.videoView);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.back_smoke_fin);
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        sensorDetect();
        textBlink();
    }

    @Override
    public void hearShake() {
        final ImageView answerTriangle = findViewById(R.id.imageView);
        final TypedArray idiomImage = getResources().obtainTypedArray(R.array.apptour);
        final Random rand = new Random();
        final int rndInt = rand.nextInt(idiomImage.length());
        final int resID = idiomImage.getResourceId(rndInt, 0);
        flashShake.clearAnimation();
        flashShake.setVisibility(View.GONE);
        answerTriangle.setVisibility(View.VISIBLE);
        answerTriangle.setImageResource(resID);
        sd.stop();

        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                fadeOutAndHideImage(answerTriangle);
            }
        }.start();

    }
}




