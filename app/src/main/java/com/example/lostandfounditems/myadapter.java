package com.example.lostandfounditems;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class myadapter extends FirestoreRecyclerAdapter<Datamodel,myadapter.myviewholder> {

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


    public myadapter(@NonNull FirestoreRecyclerOptions<Datamodel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull Datamodel model) {
        holder.show_thing.setText(model.getThing());
        holder.show_location.setText(model.getLocation());
        holder.show_description.setText(model.getDescription());
        String timeago = calc(model.getDate());
        holder.time2.setText(timeago);
        Glide.with(holder.img1.getContext()).load(model.getUri()).into(holder.img1);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.show_thing.getContext());
                builder.setTitle("Delete");
                builder.setMessage("Are you sure you want to delete.");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        getSnapshots().getSnapshot(position).getReference().delete();

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                    }
                });
                builder.show();
            }
        });
    }

    private String calc(String date) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        try {
            long time = sdf.parse(date).getTime();
            long now = System.currentTimeMillis();
            CharSequence ago =
                    DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
            return ago+"";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.showdata,parent,false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder{


        ImageView img1;
        TextView show_thing,show_location,show_description;
        ImageView delete;
        TextView time2;


        public myviewholder(@NonNull View itemView) {
            super(itemView);
             show_thing = itemView.findViewById(R.id.showdata_thing);
             show_location = itemView.findViewById(R.id.showdata_location);
             show_description = itemView.findViewById(R.id.showdata_description);

             img1 = itemView.findViewById(R.id.showdata_img);
             delete = itemView.findViewById(R.id.delete);
             time2 = itemView.findViewById(R.id.time2);
        }
    }
}
