package com.sohamfit.sohamfitapp;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

public class VideoPlayer extends AppCompatActivity {

    private Video mVideo;
    private MediaController mMediaController;
    private int mPosition = 0;

    // Views
    private VideoView mVideoView;
    private LinearLayout mVideoLayout;
    private TextView mVideoTitle;
    private TextView mVideoDescription;
    private TextView mInstructorName;
    private ImageView mInstructorImage;
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        // Get video
        mVideo = getIntent().getExtras().getParcelable("video");


        // Views
        mVideoView = (VideoView) findViewById(R.id.videoPlayer);
        mVideoLayout = (LinearLayout) findViewById(R.id.videoPlayerLayout);
        mVideoTitle = (TextView) findViewById(R.id.videoTitle);
        mVideoDescription = (TextView) findViewById(R.id.videoDescription);
        mInstructorName = (TextView) findViewById(R.id.instructorName);
        mInstructorImage = (ImageView) findViewById(R.id.instructorImage);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        // Set info
        mVideoTitle.setText(mVideo.videoName);
        mVideoDescription.setText(mVideo.videoDescription);
        mInstructorName.setText(StringHelper.capitalize(mVideo.videoInstructorName));
        Picasso.with(this).load(mVideo.videoInstructorImage).transform(new RoundedTransformation(140, 0)).into(mInstructorImage);



        mMediaController = new MediaController(VideoPlayer.this);
//        mMediaController.setAnchorView(mVideoView);
        Uri videoUri = Uri.parse(mVideo.videoMp4Url);
        mVideoView.setMediaController(mMediaController);
        mVideoView.setVideoURI(videoUri);
//        mVideoView.start();

        // When the video file ready for playback.
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {

                mProgressBar.setVisibility(View.GONE);
                mVideoLayout.setVisibility(View.VISIBLE);

                mMediaController.setAnchorView(mVideoView);
                mVideoView.start();


                // When video Screen change size.
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

                        mVideoView.setMinimumHeight(height);
                    }
                });
            }
        });


    }

}
