package com.example.miniproject1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;

public class ChatBox extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);
        // lets set adapter into viewpager......
        ViewPager2 views=findViewById(R.id.viewpager1);
        ChatFragmentAdapter adapter=new ChatFragmentAdapter
                (getSupportFragmentManager(),getLifecycle());
        views.setAdapter(adapter);

        // let's set position of tabs according to thr position of viewpager
        TabLayout tabs=findViewById(R.id.tab);
        new TabLayoutMediator(tabs, views,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        if(position==0)
                            tab.setText("chats " );
                        else if (position==1) {
                            tab.setText("Status " );

                        }
                        else {
                            tab.setText("Profile " );
                        }
                    }
            }).attach();
//      tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
//      {
//          @Override
//          public void onTabSelected(TabLayout.Tab tab) {
//              views.setCurrentItem(tab.getPosition());
//          }
//
//          @Override
//          public void onTabUnselected(TabLayout.Tab tab) {
//
//          }
//
//          @Override
//          public void onTabReselected(TabLayout.Tab tab) {
//
//          }
//      });
//      // change position of tabs on change of viewpager pages
//        views.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
//                tabs.getTabAt(position).select();
//            }
//        });
    }
    @Override
    protected void onStart() {

        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(this,"Please Login First",Toast.LENGTH_LONG).show();
            Intent in=new Intent(this, login.class);
            startActivity(in);
        }
    }
}