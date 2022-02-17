package com.example.lostandfounditems.ui.home;

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
import com.example.lostandfounditems.found;
import com.example.lostandfounditems.Add_Items;

public class HomeFragment extends Fragment{

    public CardView AddItem,FoundItem;
    Activity context;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        context = getActivity();



        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onStart() {
        AddItem = (CardView) getActivity().findViewById(R.id.additems);
        FoundItem =(CardView) getActivity().findViewById(R.id.founditems);

        AddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Add_Items.class);
                startActivity(i);
            }
        });

        FoundItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, found.class);
                startActivity(i);
            }
        });
        super.onStart();
    }


}