package com.example.zhiyue.smartfitnesscenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class TutorialActivity extends YouTubeBaseActivity {

    private static final String API_KEY = "YOURAPIKEY";
    private YouTubePlayerView youTubePlayerView;
    private Button btnPlayVideo, btnBack;
    private YouTubePlayer.OnInitializedListener onInitializedListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        youTubePlayerView = (YouTubePlayerView)findViewById(R.id.youtube_player_view);
        btnPlayVideo = (Button)findViewById(R.id.btnPlayVideo);
        btnBack = (Button)findViewById(R.id.btnBack);

        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                //youTubePlayer.cueVideo("DekuSxJgpbY");
                if (null == youTubePlayer) return;

                // Start buffering
                if (!b) {
                    youTubePlayer.loadVideo("cZnsLVArIt8");
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                if (youTubeInitializationResult.isUserRecoverableError())
                {
                    youTubeInitializationResult.getErrorDialog(TutorialActivity.this, 1).show();
                }
                Toast.makeText(TutorialActivity.this, "Error while initializing YouTubePlayer.", Toast.LENGTH_SHORT).show();
            }
        };

        btnPlayVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                youTubePlayerView.initialize(API_KEY, onInitializedListener);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(TutorialActivity.this, BmrActivity.class));
            }
        });
    }
}
