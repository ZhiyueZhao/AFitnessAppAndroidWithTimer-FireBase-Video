package com.example.zhiyue.smartfitnesscenter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class WorkOutActivity extends AppCompatActivity {

    private TextView txvWorkTimmer, txvRestTimmer, txvTotalTimmer;
    private EditText etxRound, etxRest, etxTotal;
    private SharedPreferences workOutPreferences;
    private Button btnStart, btnBackFW;

    private static final String FORMAT = "%02d:%02d:%02d";
    private boolean bFinished = false;

    int iTotalSeconds , iTotalMinutes, iRestSeconds, iRoundSeconds;
    CountDownTimer cdtWork, cdtRest, cdtTotal;
    MediaPlayer workMedia, restMedia, totalMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_out);

        workMedia = new MediaPlayer();
        restMedia = new MediaPlayer();
        totalMedia = new MediaPlayer();
        workMedia = MediaPlayer.create(this, R.raw.correct);
        restMedia = MediaPlayer.create(this, R.raw.correct);
        totalMedia = MediaPlayer.create(this, R.raw.correct);

        txvTotalTimmer = (TextView) findViewById(R.id.txvTotalTimmer);
        txvRestTimmer = (TextView) findViewById(R.id.txvRestTimmer);
        txvWorkTimmer = (TextView) findViewById(R.id.txvWorkTimmer);

        etxRound = (EditText) findViewById(R.id.etxRound);
        etxRest = (EditText) findViewById(R.id.etxRest);
        etxTotal = (EditText) findViewById(R.id.etxTotal);

        btnStart = (Button) findViewById(R.id.btnStart);
        btnBackFW = (Button) findViewById(R.id.btnBackFW);

        //creating SharedPreferences object once (onCreate)
        workOutPreferences = this.getSharedPreferences("workOutPrefs", this.MODE_PRIVATE);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iTotalMinutes = Integer.parseInt(etxTotal.getText().toString());
                iRestSeconds = Integer.parseInt(etxRest.getText().toString());
                iRoundSeconds = Integer.parseInt(etxRound.getText().toString());

                iTotalSeconds = iTotalMinutes * 60;

                if (cdtWork != null) {
                    cdtWork.cancel();
                }

                if (cdtRest != null) {
                    cdtRest.cancel();
                }

                if (cdtTotal != null) {
                    cdtTotal.cancel();
                }

                cdtWork = new CountDownTimer(iRoundSeconds* 1000, 1000) { // adjust the milli seconds here

                    public void onTick(long millisUntilFinished) {

                        txvWorkTimmer.setText(""+String.format(FORMAT,
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                    }

                    public void onFinish() {
                        if(!bFinished) {
                            txvWorkTimmer.setText("Have a rest now!");
                            restMedia.start();
                            cdtRest.start();
                        }
                    }
                };

                cdtRest = new CountDownTimer(iRestSeconds* 1000, 1000) { // adjust the milli seconds here

                    public void onTick(long millisUntilFinished) {

                        txvRestTimmer.setText(""+String.format(FORMAT,
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                    }

                    public void onFinish() {
                        if(!bFinished) {
                            txvRestTimmer.setText("Time to work out!");
                            workMedia.start();
                            cdtWork.start();
                        }
                    }
                };

                cdtTotal = new CountDownTimer(iTotalSeconds* 1000, 1000) { // adjust the milli seconds here

                    public void onTick(long millisUntilFinished) {

                        txvTotalTimmer.setText(""+String.format(FORMAT,
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                    }

                    public void onFinish() {
                        txvTotalTimmer.setText("You done! Great work out!");
                        txvRestTimmer.setText("");
                        txvWorkTimmer.setText("");
                        totalMedia.start();
                        bFinished = true;
                    }
                };

                cdtWork.start();
                cdtTotal.start();
            }
        });

        btnBackFW.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(WorkOutActivity.this, BmrActivity.class));
            }
        });
    }
}
