package com.example.healthapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.healthapp.R;
import com.example.healthapp.model.YoutubeLink;

import java.util.List;


public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.CardViewHolder> {
    private final List<YoutubeLink> cardList;
    private final Context context;

    public PlayListAdapter(List<YoutubeLink> cardList, Context context) {
        this.cardList = cardList;
        this.context = context;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_youtube, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        YoutubeLink item = cardList.get(position);
        // 썸네일 이미지 로드
        Glide.with(context).load(item.getThumbnailUrl()).into(holder.youtubeThumbnail);

        holder.youtubeThumbnail.setOnClickListener(v -> {
            // 유튜브 링크 연결 (앱 또는 웹으로)
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getYoutubeUrl()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {
        ImageView youtubeThumbnail;
        CardViewHolder(@NonNull View itemView) {
            super(itemView);
            youtubeThumbnail = itemView.findViewById(R.id.youtube_thumbnail);
        }
    }
}
