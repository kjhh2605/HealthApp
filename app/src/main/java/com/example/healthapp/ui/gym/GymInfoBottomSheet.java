package com.example.healthapp.ui.gym;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.healthapp.R;
import com.example.healthapp.model.Gym;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class GymInfoBottomSheet extends BottomSheetDialogFragment {
    // 인자 키
    private static final String ARG_GYM = "gym";
    private static final String ARG_IMAGE_RES = "imageRes";

    // Gym 객체로 인스턴스 생성 (이미지 리소스 고정)
    public static GymInfoBottomSheet newInstance(Gym gym) {
        GymInfoBottomSheet fragment = new GymInfoBottomSheet();
        Bundle args = new Bundle();
        args.putSerializable(ARG_GYM, (Serializable) gym);
        args.putInt(ARG_IMAGE_RES, R.drawable.gym_default); // 임의의 고정 이미지
        fragment.setArguments(args);
        return fragment;
    }

    // Gym 객체와 이미지 리소스로 인스턴스 생성 (필요시 사용)
    public static GymInfoBottomSheet newInstance(Gym gym, int imageRes) {
        GymInfoBottomSheet fragment = new GymInfoBottomSheet();
        Bundle args = new Bundle();
        args.putSerializable(ARG_GYM, (Serializable) gym);
        args.putInt(ARG_IMAGE_RES, imageRes);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_gym_info, container, false);

        // 뷰 바인딩
        TextView tvGymName = view.findViewById(R.id.tv_gym_name);
        ImageView ivGymImage = view.findViewById(R.id.iv_gym_image);
        TextView tvTrainer1 = view.findViewById(R.id.tv_trainer1);
        TextView tvCert1 = view.findViewById(R.id.tv_cert1);
        TextView tvTrainer2 = view.findViewById(R.id.tv_trainer2);
        TextView tvCert2 = view.findViewById(R.id.tv_cert2);
        TextView tvEquipment = view.findViewById(R.id.tv_equipment);
        TextView tvRegion = view.findViewById(R.id.tv_region);
        TextView tvPrice = view.findViewById(R.id.tv_price);

        if (getArguments() != null) {
            Gym gym = (Gym) getArguments().getSerializable(ARG_GYM);
            int imageRes = getArguments().getInt(ARG_IMAGE_RES, R.drawable.gym_default);

            if (gym != null) {
                // 헬스장 이름, 이미지
                tvGymName.setText(gym.getName());
                ivGymImage.setImageResource(imageRes);

                // 트레이너 정보
                HashMap<String, ArrayList<String>> trainers = gym.getTrainerList();
                if (trainers != null && !trainers.isEmpty()) {
                    ArrayList<String> trainerNames = new ArrayList<>(trainers.keySet());
                    if (trainerNames.size() > 0) {
                        tvTrainer1.setText(trainerNames.get(0));
                        ArrayList<String> certs = trainers.get(trainerNames.get(0));
                        tvCert1.setText(certs != null && !certs.isEmpty() ? String.join(", ", certs) : "자격증 정보 없음");
                    }
                    if (trainerNames.size() > 1) {
                        tvTrainer2.setText(trainerNames.get(1));
                        ArrayList<String> certs = trainers.get(trainerNames.get(1));
                        tvCert2.setText(certs != null && !certs.isEmpty() ? String.join(", ", certs) : "자격증 정보 없음");
                    }
                }

                // 보유 머신 리스트
                ArrayList<String> machines = gym.getMachineList();
                if (machines != null && !machines.isEmpty()) {
                    tvEquipment.setText(String.join(", ", machines));
                }

                // 지역, 가격
                tvRegion.setText(gym.getRegion() != null ? gym.getRegion() : "주소 정보 없음");
                tvPrice.setText("월 " + gym.getPrice() + "원");
            }
        }

        return view;
    }

}
