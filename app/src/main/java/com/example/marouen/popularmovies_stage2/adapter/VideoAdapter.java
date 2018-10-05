package com.example.marouen.popularmovies_stage2.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.marouen.popularmovies_stage2.R;
import com.example.marouen.popularmovies_stage2.model.Video;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private static final String LOG_TAG = VideoAdapter.class.getSimpleName();

    private final String BASE_URL = "https://www.youtube.com/watch?v=";

    private Context mContext;
    private List<Video> videoList;

    public VideoAdapter(Context mContext, List<Video> videoList) {
        this.mContext = mContext;
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rv_video_item, viewGroup, false);

        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder videoViewHolder, int i) {

        Video video = videoList.get(i);
        String videoUrl = BASE_URL + video.getKey();

        // Set video title text
        videoViewHolder.videoTitleTextView.setText(video.getName());

        // Set onClickListener
        videoViewHolder.videoLinearLayout.setOnClickListener(v -> {

            Log.d(LOG_TAG, videoUrl);

            // Play video via youtube app or web browser
            Uri youtubeVideo = Uri.parse(videoUrl);
            Intent intent = new Intent(Intent.ACTION_VIEW, youtubeVideo);
            if(intent.resolveActivity(mContext.getPackageManager()) != null) {
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {

        TextView videoTitleTextView;
        LinearLayout videoLinearLayout;

        VideoViewHolder(@NonNull View itemView) {
            super(itemView);

            videoLinearLayout = itemView.findViewById(R.id.linear_layout_video);
            videoTitleTextView = itemView.findViewById(R.id.tv_video_title);
        }
    }
}
