package com.example.lostandfounditems.ui.added_items;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lostandfounditems.Datamodel;
import com.example.lostandfounditems.R;
import com.example.lostandfounditems.myadapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class added_items extends Fragment {

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter adapter;
    FirebaseUser User;
    String UserId;
    Activity context;

    myadapter myadapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_added_items, container, false);

        User = FirebaseAuth.getInstance().getCurrentUser();
        UserId = User.getUid();

        recyclerView = (RecyclerView)view.findViewById(R.id.showdata);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Query query = firebaseFirestore.collection("Add items").whereEqualTo("userid",UserId);


        FirestoreRecyclerOptions<Datamodel> options = new FirestoreRecyclerOptions.Builder<Datamodel>()
                .setQuery(query, Datamodel.class)
                .build();

        myadapter = new myadapter(options);

        recyclerView.setAdapter(myadapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        myadapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        myadapter.stopListening();
    }
}