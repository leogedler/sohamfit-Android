package com.sohamfit.sohamfitapp;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

public class VideoPlayer extends AppCompatActivity {

    private Video mVideo;
    private MediaController mMediaController;
    private int mPosition = 0;

    // Views
    private VideoView mVideoView;
    private RelativeLayout mVideoLayout;
    private TextView mVideoTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        // Get video
        mVideo = getIntent().getExtras().getParcelable("video");

        // Set title
//        setTitle(mVideo.videoName);


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Views
        mVideoView = (VideoView) findViewById(R.id.videoPlayer);
        mVideoLayout = (RelativeLayout) findViewById(R.id.videoPlayerLayout);
        mVideoTitle = (TextView) findViewById(R.id.videoTitle);

        mVideoTitle.setText(mVideo.videoName);



        mMediaController = new MediaController(VideoPlayer.this);
//        mMediaController.setAnchorView(mVideoView);
        Uri videoUri = Uri.parse(mVideo.videoMp4Url);
        mVideoView.setMediaController(mMediaController);
        mVideoView.setVideoURI(videoUri);
//        mVideoView.start();

        // When the video file ready for playback.
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {

                mVideoLayout.setVisibility(View.VISIBLE);

                mMediaController.setAnchorView(mVideoView);
                mVideoView.start();
//                videoView.seekTo(position);
//                if (position == 0) {
//                    videoView.start();
//                }



                // When video Screen change size.
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

                        // Re-Set the videoView that acts as the anchor for the MediaController
//                        mMediaController.setAnchorView(mVideoView);
                    }
                });
            }
        });


    }

    // When you change direction of phone, this method will be called.
    // It store the state of video (Current position)
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // Store current position.
        savedInstanceState.putInt("CurrentPosition", mVideoView.getCurrentPosition());
        mVideoView.pause();
    }


    // After rotating the phone. This method is called.
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Get saved position.
        mPosition = savedInstanceState.getInt("CurrentPosition");
        mVideoView.seekTo(mPosition);
    }

}
