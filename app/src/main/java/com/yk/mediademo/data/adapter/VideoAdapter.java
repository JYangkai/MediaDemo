package com.yk.mediademo.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yk.mediademo.R;
import com.yk.mediademo.data.bean.Video;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    private Context context;

    private List<Video> list;

    public VideoAdapter(List<Video> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Video video = list.get(position);
                if (onItemClickVideoListener != null) {
                    onItemClickVideoListener.onItemClickVideo(video);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Video video = list.get(position);
        Glide.with(context).load(video.getPath()).into(holder.ivCover);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView ivCover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.ivCover);
        }
    }

    private OnItemClickVideoListener onItemClickVideoListener;

    public void setOnItemClickVideoListener(OnItemClickVideoListener onItemClickVideoListener) {
        this.onItemClickVideoListener = onItemClickVideoListener;
    }

    public interface OnItemClickVideoListener {
        void onItemClickVideo(Video video);
    }
}
