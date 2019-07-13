package com.alok328.raj.unesquo;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.alok328.raj.unesquo.Adapter.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AlphaAnimation animation = new AlphaAnimation(0.0f,1.0f);
        animation.setDuration(3000);

        ImageView logo = findViewById(R.id.logo);
        logo.setAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent home = new Intent(MainActivity.this, Home.class);
                startActivity(home);
                finish();
            }
        },3000);

    }
}
