package com.example.miniproject1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject1.model.UserModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.userview> {
    Context context;
    ArrayList<UserModel> users;

    TextView username, userlastmsg, usermsgdate;
    ImageView userpic;


    public UserListAdapter(Context context1, ArrayList<UserModel> users) {
        context = context1;
        this.users = users;
    }

    @NonNull
    @Override
    public userview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sampleuserdesign, parent, false);
        return new userview(view);
    }

    @Override
    public void onBindViewHolder(@NonNull userview holder, int position) {
        username.setText(users.get(position).name);
        userlastmsg.setText(users.get(position).About);
        if (users.get(position).About==null||users.get(position).About=="")
        {
            userlastmsg.setText("he is to busy to update about");
        }
        else{
            userlastmsg.setText(users.get(position).About);
        }
        Picasso.get()
                .load(users.get(position).pic)
                .placeholder(R.drawable.baseline_person_24)
                .error(R.drawable.baseline_person_24)
                .into(userpic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context, MessengerActivity.class);
               in.putExtra("name",users.get(position).name) ;
                in.putExtra("uid",users.get(position).uid);
                in.putExtra("pic",users.get(position).pic);

                context.startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class userview extends RecyclerView.ViewHolder {

        public userview(@NonNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.username);
            userlastmsg=itemView.findViewById(R.id.userlastmsg);
            usermsgdate=itemView.findViewById(R.id.userlastmsgdate);
            userpic=itemView.findViewById(R.id.userpic);
        }
    }
}
