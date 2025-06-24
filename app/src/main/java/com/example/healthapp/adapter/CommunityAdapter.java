package com.example.healthapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthapp.R;
import com.example.healthapp.model.UserTip;

import java.util.List;

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.CommunityViewHolder> {
    private List<UserTip> tipList;
    private Context context;
    private OnTipClickListener listener;

    public CommunityAdapter(List<UserTip> tipList, Context context, OnTipClickListener listener) {
        this.tipList = tipList;
        this.context = context;
        this.listener = listener;
    }

    public interface OnTipClickListener {
        void onLikeClick(UserTip tip);
        void onDislikeClick(UserTip tip);
    }

    @NonNull
    @Override
    public CommunityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_tip, parent, false);
        return new CommunityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityViewHolder holder, int position) {
        UserTip tip = tipList.get(position);

        // 뷰 바인딩
        holder.tvWorkout.setText(tip.getWorkout());
        holder.tvWriter.setText(tip.getNickname());
        holder.tvContent.setText(tip.getContext());
        holder.tvMachine.setText(tip.getMachine());
        holder.tvEtc.setText(tip.getEtc());
        holder.tvLikeCount.setText(String.valueOf(tip.getLikes()));
        holder.tvDislikeCount.setText(String.valueOf(tip.getDislikes()));

        holder.btnLike.setOnClickListener(v -> listener.onLikeClick(tip));
        holder.btnDislike.setOnClickListener(v -> listener.onDislikeClick(tip));
    }

    @Override
    public int getItemCount() {
        return tipList != null ? tipList.size() : 0;
    }

    public static class CommunityViewHolder extends RecyclerView.ViewHolder {
        TextView tvWorkout, tvWriter, tvContent, tvLikeCount, tvDislikeCount, tvMachine, tvEtc;
        ImageButton btnLike, btnDislike;

        public CommunityViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWorkout = itemView.findViewById(R.id.tv_workout);
            tvMachine = itemView.findViewById(R.id.tv_machine);
            tvEtc = itemView.findViewById(R.id.tv_etc_add);
            tvWriter = itemView.findViewById(R.id.tv_writer);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvLikeCount = itemView.findViewById(R.id.tv_like_count);
            tvDislikeCount = itemView.findViewById(R.id.tv_dislike_count);
            btnLike = itemView.findViewById(R.id.btn_like);
            btnDislike = itemView.findViewById(R.id.btn_dislike);
        }
    }
}
