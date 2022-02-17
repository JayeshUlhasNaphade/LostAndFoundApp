package com.example.lostandfounditems;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class found extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView firestorelist;
    newadapter adapter;
    FirebaseUser User;
    String UserId;
    Query query;
    static FirestoreRecyclerOptions<Productmodel> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Found Items");






        User = FirebaseAuth.getInstance().getCurrentUser();
        UserId = User.getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firestorelist = findViewById(R.id.list);
        firestorelist.setLayoutManager(new LinearLayoutManager(this));

        query = firebaseFirestore.collection("Add items");

        options = new FirestoreRecyclerOptions.Builder<Productmodel>()
                .setQuery(query, Productmodel.class)
                .build();


        adapter = new newadapter(options);
        firestorelist.setAdapter(adapter);


    }




    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.searchmenu,menu);

        MenuItem item = menu.findItem(R.id.search);

        SearchView searchView = (SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                processsearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                processsearch(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void processsearch(String s)
    {
        FirestoreRecyclerOptions<Productmodel> options=new FirestoreRecyclerOptions.Builder<Productmodel>()
                .setQuery(FirebaseFirestore.getInstance().collection("Add items").orderBy("thing").startAt(s).endAt(s+"\uf8ff") ,Productmodel.class)
                .build();

        adapter = new newadapter(options);
        adapter.startListening();
        firestorelist.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home){
            Intent i = new Intent(found.this,MainActivity2.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}