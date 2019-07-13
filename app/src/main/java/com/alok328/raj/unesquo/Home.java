package com.alok328.raj.unesquo;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.alok328.raj.unesquo.Adapter.ViewPagerAdapter;

public class Home extends AppCompatActivity {

    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;

    ImageView topImg;

    public void adminSection(View view){
        Intent intent = new Intent(Home.this, AdminLogin.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tabLayout = findViewById(R.id.tabLayout);
        appBarLayout = findViewById(R.id.appbar);
        viewPager = findViewById(R.id.viewPager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentNews(), "News");
        adapter.addFragment(new FragmentQuiz(), "Quiz");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        topImg = findViewById(R.id.topImg);

    }

    public void facebook(View view){
        Intent fbIntent = new Intent(Home.this, FacebookWebpage.class);
        startActivity(fbIntent);
    }

    public void about(View view){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this, R.style.CustomDialog);

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View learnMoreView = layoutInflater.inflate(R.layout.learn_more_layout,null);

        alertDialog.setView(learnMoreView);

        alertDialog.show();
    }

    public void privacyPolicy(View view){
        Intent privacyPolicyIntent = new Intent(Home.this, PrivacyPolicyWebPage.class);
        startActivity(privacyPolicyIntent);
    }
}
