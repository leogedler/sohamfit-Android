package com.sohamfit.sohamfitapp;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VideoPlayer extends AppCompatActivity {

    private Video mVideo;
    private MediaController mMediaController;
    protected SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    protected SimpleDateFormat mFormatter2 = new SimpleDateFormat("dd/MM/yyy");

    // Views
    private VideoView mVideoView;
    private RelativeLayout mVideoLayout;
    private TextView mVideoTitle;
    private TextView mVideoDescription;
    private TextView mInstructorName;
    private TextView mDate;
    private TextView mLevel;
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
        mVideoLayout = (RelativeLayout) findViewById(R.id.videoPlayerLayout);
        mVideoTitle = (TextView) findViewById(R.id.videoTitle);
        mVideoDescription = (TextView) findViewById(R.id.videoDescription);
        mInstructorName = (TextView) findViewById(R.id.instructorName);
        mInstructorImage = (ImageView) findViewById(R.id.instructorImage);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mDate = (TextView) findViewById(R.id.dateText);
        mLevel = (TextView) findViewById(R.id.videoLevel);

        // Set info
        mVideoTitle.setText(mVideo.videoName);
        mVideoDescription.setText(mVideo.videoDescription);
        mInstructorName.setText(StringHelper.capitalize(mVideo.videoInstructorName));
        Picasso.with(this).load(mVideo.videoInstructorImage).transform(new RoundedTransformation(140, 0)).into(mInstructorImage);
        try {
            Date date = mFormatter.parse(mVideo.createdAt.replaceAll("Z$", "+0000"));
            mDate.setText(getResources().getString(R.string.video_date_text) + " " +mFormatter2.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Set level
        mLevel.setText(StringHelper.capitalize(mVideo.videoLevel));
        if(mVideo.videoLevel.equals("básico")){
            mLevel.setTextColor(ContextCompat.getColor(this, R.color.colorGreen));
        }else if(mVideo.videoLevel.equals("intermedio")){
            mLevel.setTextColor(ContextCompat.getColor(this, R.color.colorBrightOrange));
        }else{
            mLevel.setTextColor(ContextCompat.getColor(this, R.color.colorRed));
        }

        // Set controller
        mMediaController = new MediaController(VideoPlayer.this);
        Uri videoUri = Uri.parse(mVideo.videoMp4Url);
        mVideoView.setMediaController(mMediaController);
        mVideoView.setVideoURI(videoUri);

        // When the video file ready for playback.
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {

                // Change visibility
                mProgressBar.setVisibility(View.GONE);
                mVideoLayout.setVisibility(View.VISIBLE);

                // Anchor controller
                mMediaController.setAnchorView(mVideoView);
                mVideoView.start();

            }
        });

    }

}
