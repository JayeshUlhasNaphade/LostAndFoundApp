package com.example.lostandfounditems.ui.profile;

import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lostandfounditems.Editprofile;
import com.example.lostandfounditems.MainActivity2;
import com.example.lostandfounditems.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    public static final String Name_key = "name";
    public static final String MobileNO_key = "mobile";
    public static final String RollNo_key = "rollno";
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    FirebaseUser User;
    StorageReference storageReference;
    DocumentReference mDoc;
    ImageView imageview;
    TextView name,mobile,rollno;
    String UserId;
    Activity context;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        context = getActivity();


        return inflater.inflate(R.layout.fragment_profile, container, false);


    }

    @Override
    public void onStart() {
        Button btn = (Button) context.findViewById(R.id.editprofile);
        imageview = (ImageView) context.findViewById(R.id.showProfile);
        name = (TextView) context.findViewById(R.id.name_sp);
        mobile = (TextView) context.findViewById(R.id.mobile_sp);
        rollno = (TextView) context.findViewById(R.id.rollno_sp);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, Editprofile.class);
                startActivity(i);
            }
        });

        User = FirebaseAuth.getInstance().getCurrentUser();
        UserId = User.getUid();
        mDoc = fStore.collection("Users").document(UserId);
        storageReference = FirebaseStorage.getInstance().getReference(UserId);

        mDoc.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()){
                            String Nameview = task.getResult().getString("name");
                            String Mobileview = task.getResult().getString("mobile");
                            String Rollnoview = task.getResult().getString("rollno");
                            String Uri = task.getResult().getString("uri");

                            Picasso.get().load(Uri).into(imageview);
                            name.setText(Nameview);
                            mobile.setText(Mobileview);
                            rollno.setText(Rollnoview);

                        }
                        else{
                            Toast.makeText(context, "No Profile Exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


        super.onStart();
    }
}