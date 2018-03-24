package com.projects.melih.popularmovies.ui.moviedetail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.projects.melih.popularmovies.R;
import com.projects.melih.popularmovies.common.CollectionUtils;
import com.projects.melih.popularmovies.common.Utils;
import com.projects.melih.popularmovies.model.Video;
import com.projects.melih.popularmovies.ui.base.ItemClickListener;

import java.util.List;

/**
 * Created by Melih Gültekin on 15.03.2018
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder> {
    private final Context context;
    private final ItemClickListener<Video> itemClickListener;
    private List<Video> videos;

    VideosAdapter(@NonNull Context context, @NonNull ItemClickListener<Video> itemClickListener) {
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new VideoViewHolder(inflater.inflate(R.layout.item_movie_video, parent, false), context, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.bindTo(videos.get(position));
    }

    @Override
    public int getItemCount() {
        return CollectionUtils.size(videos);
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
        notifyDataSetChanged();
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        private final Context context;
        private final ItemClickListener<Video> itemClickListener;
        private final ImageView ivTrailer;
        private final TextView tvType;
        private final TextView tvTitle;

        VideoViewHolder(@NonNull View itemView, @NonNull Context context, @NonNull ItemClickListener<Video> itemClickListener) {
            super(itemView);
            this.context = context;
            this.itemClickListener = itemClickListener;
            ivTrailer = itemView.findViewById(R.id.image);
            tvType = itemView.findViewById(R.id.type);
            tvTitle = itemView.findViewById(R.id.title);
        }

        void bindTo(@Nullable final Video video) {
            if (video != null) {
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .dontAnimate()
                        .placeholder(R.drawable.ic_movie_placeholder_4_3)
                        .error(R.drawable.ic_movie_placeholder_4_3);
                Glide.with(context)
                        .asBitmap()
                        .apply(options)
                        .load(Utils.getVideoImageYoutubeLink(video.getKey()))
                        .thumbnail(0.1f)
                        .into(ivTrailer);
                tvType.setText(video.getType());
                tvTitle.setText(video.getName());

                itemView.setOnClickListener(v -> itemClickListener.onItemClick(video));
            }
        }
    }
}