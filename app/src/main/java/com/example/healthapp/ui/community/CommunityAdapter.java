package com.example.healthapp.ui.community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        void onTipClick(UserTip tip);
    }

    @NonNull
    @Override
    public CommunityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_tip_item, parent, false);
        return new CommunityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityViewHolder holder, int position) {
        UserTip tip = tipList.get(position);

        // 뷰 바인딩
        holder.tvExercise.setText(tip.getMachine() != null ? tip.getMachine(): "운동명 없음");
        holder.tvWriter.setText(tip.getNickname() != null ? tip.getNickname(): "익명");
        holder.tvContent.setText(tip.getContext());
        holder.tvLikeCount.setText(String.valueOf(tip.getLikes()));
        holder.tvDislikeCount.setText(String.valueOf(tip.getDislikes()));
    }

    @Override
    public int getItemCount() {
        return tipList != null ? tipList.size() : 0;
    }

    public static class CommunityViewHolder extends RecyclerView.ViewHolder {
        TextView tvExercise, tvWriter, tvContent,tvLikeCount, tvDislikeCount;

        public CommunityViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExercise = itemView.findViewById(R.id.tv_workout);
            tvWriter = itemView.findViewById(R.id.tv_writer);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvLikeCount = itemView.findViewById(R.id.tv_like_count);
            tvDislikeCount = itemView.findViewById(R.id.tv_dislike_count);
        }
    }
}
