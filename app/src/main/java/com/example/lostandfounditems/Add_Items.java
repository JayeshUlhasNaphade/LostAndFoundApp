package com.example.lostandfounditems;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;
import me.drakeet.materialdialog.MaterialDialog;

import static android.R.layout.simple_list_item_1;


public class Add_Items extends AppCompatActivity {

    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    DocumentReference mDoc;
    Button add;
    EditText location,thing,description;
    FirebaseUser User;
    String UserId;
    ImageView imageview;
    StorageReference storageReference=FirebaseStorage.getInstance().getReference();;
    UploadTask uploadtask;
    private final int SELECT_IMAGE=1 , REQUEST_CAMERA = 1;
    private Uri uri,imageUri;
    int flag=0;
    final private int REQUEST_CODE_WRITE_STORAGE = 1;
    member member;
    int id=0;
    DocumentReference name;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);



        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Add Items");


        location = findViewById(R.id.location);
        thing = findViewById(R.id.thing);
        description = findViewById(R.id.description);
        add = findViewById(R.id.add);
        imageview = findViewById(R.id.Add_image);

        member = new member();

        User = FirebaseAuth.getInstance().getCurrentUser();
        UserId = User.getUid();
        mDoc = fStore.collection("Add items").document();
        name = fStore.collection("Users").document(UserId);

        initviews();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });

    }

    private void initviews() {
        imageview = (ImageView)findViewById(R.id.Add_image);

        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hasWriteStoragePermission = 0;

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    hasWriteStoragePermission = checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }

                if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_CODE_WRITE_STORAGE);
                    }
                    //return;
                }
                listDialogue();
            }

        });
    }

    private void listDialogue() {
        final ArrayAdapter<String> arrayAdapter
                = new ArrayAdapter<>(this, simple_list_item_1);


        arrayAdapter.add("Take Photo");
        arrayAdapter.add("Choose From Gallery");

        ListView listView = new ListView(this);
        listView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (8 * scale + 0.5f);
        listView.setPadding(0, dpAsPixels, 0, dpAsPixels);
        listView.setDividerHeight(0);
        listView.setAdapter(arrayAdapter);

        final MaterialDialog alert = new MaterialDialog(this).setContentView(listView);
        alert.setBackgroundResource(R.drawable.dialogbox);

        alert.setPositiveButton("Cancel", new View.OnClickListener() {
            @Override public void onClick(View v) {

                alert.dismiss();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    flag=0;

                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    if (i.resolveActivity(getPackageManager()) != null) {
                        System.out.println("qwert");
                        startActivityForResult(i,REQUEST_CAMERA);
                    }else{
                        System.out.println("plplff");
                    }
                    alert.dismiss();

                }else {
                    flag=1;
                    System.out.println("loll");
                    alert.dismiss();
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_IMAGE);

                }
            }
        });

        alert.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(requestCode);
        System.out.println("innnfg"+flag);
        switch (flag) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {

                    imageUri = data.getData();
                    String selectedImagePath = getPath(imageUri);
                    File f = new File(selectedImagePath);
                    Bitmap bmp = Compressor.getDefault(this).compressToBitmap(f);
                    System.out.println(data.getData());
                    imageview.setImageBitmap(bmp);

                }
                break;

            case 0:
                if (resultCode == Activity.RESULT_OK) {

                    Bitmap bmp = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes=new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG,90,bytes);
                    byte bb[]=bytes.toByteArray();
                    String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), bmp, "Title", null);
                    imageUri= Uri.parse(path);
                    imageview.setImageBitmap(bmp);
//
                }

                break;
        }

    }

    private String getPath(Uri uri) {

        if (uri == null) {
            return null;
        }

        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor cursor = managedQuery(uri, projection, null, null,
                null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }

        return uri.getPath();
    }





    private void upload(){
        String Thing = thing.getText().toString();
        String Location = location.getText().toString();
        String Description = description.getText().toString();
        String Userid = UserId;
//        System.out.println("hdhfb");

        dialog = new Dialog(Add_Items.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_wait);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        if(!TextUtils.isEmpty(Thing) || !TextUtils.isEmpty(Location) || !TextUtils.isEmpty(Description) || !TextUtils.isEmpty(Userid) || imageUri != null){

            StorageReference reference = storageReference.child("Image/"+imageUri.getLastPathSegment());
//            System.out.println("innn");
            uploadtask = reference.putFile(imageUri);

            uploadtask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

              //      Toast.makeText(Add_Items.this,"Image upload",Toast.LENGTH_SHORT).show();
                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(
                            new OnCompleteListener<Uri>() {

                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {

                                    Date date = new Date();
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                    String strDate = formatter.format(date);


                                    String generatedFilePath =task.getResult().toString();

                                    User = FirebaseAuth.getInstance().getCurrentUser();
                                    UserId = User.getUid();
                                    Map<String, Object> datatosave = new HashMap<String, Object>();
                                    datatosave.put("location", location.getText().toString());
                                    datatosave.put("thing", thing.getText().toString());
                                    datatosave.put("description",description.getText().toString());
                                    datatosave.put("userid", Userid);
                                    datatosave.put("date",strDate);
                                    datatosave.put("uri",generatedFilePath.toString());
                                    mDoc.set(datatosave).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            dialog.dismiss();
                                            Toast.makeText(Add_Items.this,"Data uploaded successfully",Toast.LENGTH_SHORT).show();
                                            notification();


                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Add_Items.this,"Failed",Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            });

                    System.out.println("here");

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Add_Items.this,"failed",Toast.LENGTH_SHORT).show();
                }
            });


        }
    }

    private void notification() {

        name.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.getResult().exists()) {
                    String Nameview = task.getResult().getString("name");

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel("n", "n", NotificationManager.IMPORTANCE_DEFAULT);

                        NotificationManager manager = getSystemService(NotificationManager.class);

                        manager.createNotificationChannel(channel);
                    }
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "n")
                            .setShowWhen(true)
                            .setWhen(System.currentTimeMillis())
                            .setContentText(Nameview)
                            .setSmallIcon(R.drawable.ic_baseline_notifications)
                            .setAutoCancel(true)
                            .setContentText(Nameview+" added a new post.");


                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
                    managerCompat.notify(999, builder.build());

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            Intent i = new Intent(Add_Items.this,MainActivity2.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {

        super.onBackPressed();
    }




}
