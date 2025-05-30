package com.example.healthapp.ui.gym;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.healthapp.R;


public class GymInfoBottomSheet extends com.google.android.material.bottomsheet.BottomSheetDialogFragment {
    private static final String ARG_EQUIPMENT = "equipment";
    private static final String ARG_GYM_NAME = "gymName";

    public static GymInfoBottomSheet newInstance(String gymName, String equipment) {
        GymInfoBottomSheet fragment = new GymInfoBottomSheet();
        Bundle args = new Bundle();
        args.putString(ARG_EQUIPMENT, equipment);
        args.putString(ARG_GYM_NAME, gymName);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_gym_info, container, false);

        TextView tvGymName = view.findViewById(R.id.tv_gym_name);
        TextView tvEquipment = view.findViewById(R.id.tv_equipment);

        if (getArguments() != null) {
            tvGymName.setText(getArguments().getString(ARG_GYM_NAME, ""));
            tvEquipment.setText(getArguments().getString(ARG_EQUIPMENT, ""));
        }

        return view;
    }
}
