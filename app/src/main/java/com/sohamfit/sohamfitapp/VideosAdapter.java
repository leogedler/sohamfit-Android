package com.sohamfit.sohamfitapp;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by leonardogedler on 4/21/17.
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.CustomViewHolder> {
    private List<Video> feedVideosList;
    private Context mContext;



    public VideosAdapter(Context context, List<Video> feedVideosList) {
        this.feedVideosList = feedVideosList;
        this.mContext = context;

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_video_row, viewGroup, false);

        CustomViewHolder vh = new CustomViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, int position) {

        final Video videoItem = feedVideosList.get(position);

        //Download image using picasso library
        Picasso.with(mContext).load(videoItem.videoMp4Url).into(holder.imageView);

        // Setting video info
        holder.textView.setText(videoItem.videoName);
        holder.timeView.setText(videoItem.videoDuration);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Nivel del video: "+ videoItem.videoLevel, Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(mContext, VideoViewActivity.class);
//                intent.putExtra("videoUrl", videoItem.getString("videoMp4Url"));
//                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return (null != feedVideosList ? feedVideosList.size() : 0);
    }


    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected TextView textView;
        protected TextView timeView;
        protected CardView cardView;


        public CustomViewHolder(View view) {
            super(view);
            this.cardView = (CardView) view.findViewById(R.id.card_view);
            this.imageView = (ImageView) view.findViewById(R.id.thumbnail);
            this.textView = (TextView) view.findViewById(R.id.title);
            this.timeView = (TextView) view.findViewById(R.id.time);


        }
    }

}
