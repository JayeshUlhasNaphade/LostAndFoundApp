package com.example.lostandfounditems;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lostandfounditems.ui.profile.ProfileFragment;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Editprofile extends AppCompatActivity {

    public static final String Name_key = "name";
    public static final String MobileNO_key = "mobile";
    public static final String RollNo_key = "rollno";
    private Uri Imageuri;
    private static final int PICK_IMAGE=1;
    String TAG;
    FirebaseUser User;
    String UserId;
    Button button;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    UploadTask uploadTask;
    StorageReference storageReference;
    DocumentReference mDoc;
    ImageView imageview;
    EditText name,Mobile,rollno;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Edit Profile");

        ActionBar actionBar;
        actionBar = getSupportActionBar();

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#999999"));
        actionBar.setBackgroundDrawable(colorDrawable);


        name = findViewById(R.id.name);
        Mobile = findViewById(R.id.mobile);
        rollno = findViewById(R.id.rollno);
        button = findViewById(R.id.save_profile);
        imageview = findViewById(R.id.profile_sp);

        User = FirebaseAuth.getInstance().getCurrentUser();
        UserId = User.getUid();
        mDoc = fStore.collection("Users").document(UserId);
        storageReference = FirebaseStorage.getInstance().getReference(UserId);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uploaddata();
            }
        });

    }



    public void chooseImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE || resultCode == RESULT_OK || data != null || data.getData() != null){

            Imageuri = data.getData();
            Picasso.get().load(Imageuri).into(imageview);
        }
    }

    private String getFileExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void Uploaddata(){

        String Nameview = name.getText().toString();
        String Mobileview = Mobile.getText().toString();
        String Rollnoview = rollno.getText().toString();



        if(!TextUtils.isEmpty(Nameview) || !TextUtils.isEmpty(Mobileview) || !TextUtils.isEmpty(Rollnoview) || Imageuri != null){

            final StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + getFileExt(Imageuri));

            uploadTask = reference.putFile(Imageuri);

            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return  reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){

                        Uri downloaduri =task.getResult();


                        Map<String, Object> datatosave = new HashMap<String, Object>();
                        datatosave.put(Name_key, Nameview);
                        datatosave.put(MobileNO_key, Mobileview);
                        datatosave.put(RollNo_key, Rollnoview);
                        datatosave.put("uri",downloaduri.toString());


                        mDoc.set(datatosave).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Editprofile.this,"Profile Saved",Toast.LENGTH_SHORT).show();
                                Log.d(TAG,"Profile saved SuccessFully");


                                Intent i = new Intent(Editprofile.this, ProfileFragment.class);
                                startActivity(i);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Profile Not Saved");
                            }
                        });
                    }
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
            }
        else{

            Toast.makeText(this,"All Fields are Required",Toast.LENGTH_SHORT).show();
            }
        }


    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home){

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount()==0){
            this.finish();
        }
        else{
            super.onBackPressed();
        }
    }

}
