package com.example.lostandfounditems.ui.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.lostandfounditems.R;
import com.example.lostandfounditems.aboutus;

public class SettingFragment extends Fragment {

    CardView about,language;
    Activity context;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        context = getActivity();

        return inflater.inflate(R.layout.fragment_setting, container, false);

    }

    @Override
    public void onStart() {

        about = (CardView)context.findViewById(R.id.about);


        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, aboutus.class);
                startActivity(i);
            }
        });
        super.onStart();
    }
}