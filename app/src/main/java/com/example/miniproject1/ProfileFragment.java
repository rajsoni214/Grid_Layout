package com.example.miniproject1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;


public class ProfileFragment extends Fragment {
ImageView img_profile, img_changeprofile;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        // open gallery to choose profile photo
        img_profile=v.findViewById(R.id.img_profile);
        img_changeprofile=v.findViewById(R.id.img_changeprofile);
        img_changeprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(Intent.ACTION_GET_CONTENT);
                in.setType("image/*");
                startActivityForResult(in,1);
            }
        });





        // select all info about current user from database
        TextView txt_pname =v.findViewById(R.id.txt_pname);
        TextView txt_pemail =v.findViewById(R.id.txt_pemail);
        TextView txt_pname2 =v.findViewById(R.id.txt_pname2);
        TextView txt_pabout=v.findViewById(R.id.txt_pabout);
        String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            txt_pname.setText(snapshot.child("name").getValue(String.class));
            txt_pname2.setText(snapshot.child("name").getValue(String.class));
            txt_pemail.setText(snapshot.child("email").getValue(String.class));
                Picasso.get()
                        .load(snapshot.child("pic").getValue(String.class))
                        .placeholder(R.drawable.baseline_person_24)
                        .error(R.drawable.baseline_person_24)
                        .into(img_profile);
                txt_pabout.setText(snapshot.child("About").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // logout from account
        ImageView img_logout1 = v.findViewById(R.id.img_logout1);
        img_logout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent in=new Intent(getContext(), login.class);
                startActivity(in);
            }
        });


        // change about of current user on click of pencil


        ImageView img_changeabout=v.findViewById(R.id.img_changeabout);
        img_changeabout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
                alert.setTitle("About");
                alert.setMessage("tell your vibe...");
                EditText input=new EditText(getContext());
                alert.setView(input);
                alert.setPositiveButton("CHANGE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
Toast.makeText(getContext(),input.getText()+"",Toast.LENGTH_LONG).show();
                        HashMap<String,Object> data=new HashMap<>();
                        data.put("About",input.getText().toString());
                        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(data);
                        txt_pabout.setText(input.getText());
                        dialogInterface.dismiss();
                    }
                });
                alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                     dialogInterface.dismiss();
                    }
                });
                alert.show();

            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1)
        {
            img_profile.setImageURI(data.getData());
            FirebaseStorage.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).putFile(data.getData()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                 taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                     @Override
                     public void onSuccess(Uri uri) {
                         HashMap<String,Object> data=new HashMap<>();
                         data.put("pic",uri.toString());
                         FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(data);
                         Toast.makeText(getContext(),"Profile Updated",Toast.LENGTH_LONG).show();
                     }
                 });
                }
            });


        }
    }
}











