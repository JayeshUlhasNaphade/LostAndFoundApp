package com.example.lostandfounditems;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class details extends AppCompatActivity {

    TextView thing,location,name,mobile,description;
    Button btn;
    ImageView img;
    CollectionReference mDoc;
    DocumentReference gdoc;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    FirebaseUser User;
    String UserId;
    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Details");


        thing = findViewById(R.id.detail_thing);
        location = findViewById(R.id.detail_location);
        description = findViewById(R.id.detail_description);
        name = findViewById(R.id.detail_name);
        mobile = findViewById(R.id.detail_contact);
        img = findViewById(R.id.show_image);

        Intent i = getIntent();
        image = i.getStringExtra("uri");

        thing.setText(getIntent().getStringExtra("thing").toString());
        location.setText(getIntent().getStringExtra("location").toString());
        description.setText(getIntent().getStringExtra("description").toString());
        Picasso.get().load(image).into(img);
      //  location.setText(getIntent().getStringExtra("userid").toString());


        User = FirebaseAuth.getInstance().getCurrentUser();
        UserId = User.getUid();
        mDoc = fStore.collection("Add items");
        System.out.println(getIntent().getStringExtra("userid").toString());
        Toast.makeText(details.this,"data retrieved", Toast.LENGTH_SHORT).show();
        gdoc=fStore.collection("Users").document(getIntent().getStringExtra("userid").toString());
        gdoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Toast.makeText(details.this,documentSnapshot.getString("name"),Toast.LENGTH_SHORT).show();

                Users u=new Users(documentSnapshot.getString("name"),documentSnapshot.getString("uri"),documentSnapshot.getString("mobile"),documentSnapshot.getString("rollno"));
           //     Users comment = documentSnapshot.toObject(Users.class);
                  name.setText(u.getName());
                  mobile.setText(u.getMob());
                Toast.makeText(details.this,"data retrieved",Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        super.onBackPressed();
    }
}