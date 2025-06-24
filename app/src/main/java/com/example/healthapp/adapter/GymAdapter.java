package com.example.healthapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.healthapp.R;
import com.example.healthapp.model.Gym;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GymAdapter extends RecyclerView.Adapter<GymAdapter.GymViewHolder> {
    private List<Gym> gymList;
    private Context context;
    private OnGymClickListener listener;

    public GymAdapter(List<Gym> gymList, Context context, OnGymClickListener listener) {
        this.gymList = gymList;
        this.context = context;
        this.listener = listener;
    }

    public interface OnGymClickListener {
        void onGymClick(Gym gym);
    }

    @NotNull
    @Override
    public GymViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gym, parent, false);
        GymViewHolder holder = new GymViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(GymViewHolder holder, int position) {
        Gym gym = gymList.get(position);
        holder.tvName.setText(gym.getName());
        holder.tvRegion.setText(gym.getRegion());
        holder.tvPrice.setText("월 " + gym.getPrice() + "원");

        // 머신(기구) 정보 표시
        ArrayList<String> machines = gym.getMachineList();
        holder.tvMachine1.setText(machines != null && machines.size() > 0 ? "#" + machines.get(0) : "");
        holder.tvMachine2.setText(machines != null && machines.size() > 1 ? "#" + machines.get(1) : "");

        holder.itemView.setOnClickListener(v -> {
            if (listener != null && position != RecyclerView.NO_POSITION) {
                listener.onGymClick(gym);
            }
        });
    }

    @Override
    public int getItemCount() {
        return gymList != null ? gymList.size() : 0;
    }

    public void updateList(List<Gym> newList) {
        this.gymList = newList;
        notifyDataSetChanged();
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
