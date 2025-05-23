package com.example.healthapp.ui.gym;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.healthapp.R;
import com.example.healthapp.model.Gym;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GymAdapter extends RecyclerView.Adapter<GymAdapter.GymViewHolder> {
    private List<Gym> gymList;
    private Context context;
    public GymAdapter(List<Gym> gymList,Context context) {
        this.gymList = gymList;
        this.context = context;
    }

    @NotNull
    @Override
    public GymViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // preview_gym 레이아웃 연결
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.preview_gym, parent, false);
        GymViewHolder holder = new GymViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(GymViewHolder holder, int position) {
        //이미지 불러올 때 사용
        //Glide.with(holder.itemView).load(gymList.get(position).getImageUrl()).into(holder.ivGym)

        Gym gym = gymList.get(position);
        holder.tvName.setText(gym.getName());
        holder.tvRegion.setText(gym.getRegion());
        holder.tvPrice.setText("월 " + gym.getPrice() + "원");
//        // 대표 기구 표시
//        List<Machine> machines = gym.getMachineList();
//        holder.tvMachine1.setText(machines.size() > 0 ? "#" + machines.get(0).getName() : "");
//        holder.tvMachine2.setText(machines.size() > 1 ? "#" + machines.get(1).getName() : "");
    }

    @Override
    public int getItemCount() {
        return gymList != null ? gymList.size() : 0;
    }

    public static class GymViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvRegion, tvPrice, tvMachine1, tvMachine2;

        public GymViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_gym_name);
            tvRegion = itemView.findViewById(R.id.tv_gym_region);
            tvPrice = itemView.findViewById(R.id.tv_gym_price);
            tvMachine1 = itemView.findViewById(R.id.tv_machine1);
            tvMachine2 = itemView.findViewById(R.id.tv_machine2);
        }
    }
}
