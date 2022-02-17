package com.example.lostandfounditems;

import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.SimpleDateFormat;

public class newadapter extends FirestoreRecyclerAdapter<Productmodel,newadapter.newviewholder> {

    public newadapter(@NonNull FirestoreRecyclerOptions<Productmodel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull newviewholder holder, int position, @NonNull Productmodel model)
    {

        holder.list_thing.setText(model.getThing());
        holder.list_location.setText(model.getLocation());
        holder.list_description.setText(model.getDescription());
        String timeago = calc(model.getDate());
        holder.time1.setText(timeago);
        Glide.with(holder.imageView.getContext()).load(model.geturi()).into(holder.imageView);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(holder.list_thing.getContext(),details.class);
//                i.putExtra("MyClass", model);
                i.putExtra("thing",model.getThing());
                i.putExtra("location",model.getLocation());
                i.putExtra("description",model.getDescription());
                i.putExtra("userid",model.getuserid());
                i.putExtra("uri",model.geturi());

                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.list_thing.getContext().startActivity(i);


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
    public newviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list,parent,false);

        return new newviewholder(view);
    }


    class newviewholder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        public TextView list_thing;
        public TextView list_location;
        public TextView list_description;
        TextView time1;
        CardView cardView;





        public newviewholder(@NonNull View itemView) {
            super(itemView);

            list_thing = itemView.findViewById(R.id.list_thing);
            list_location= itemView.findViewById(R.id.list_location);
            list_description = itemView.findViewById(R.id.list_description);
            cardView = itemView.findViewById(R.id.card);
            imageView = itemView.findViewById(R.id.recycle_img);
            time1 = itemView.findViewById(R.id.time1);

        }
    }
}
