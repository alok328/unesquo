package com.alok328.raj.unesquo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.squareup.picasso.Picasso;

public class NewsDetails extends AppCompatActivity {

    KenBurnsView imageNewsDetail;
    TextView titleNewsDetail, descriptionNewsDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        imageNewsDetail = findViewById(R.id.imageNewsDetail);
        titleNewsDetail = findViewById(R.id.titleNewsDetail);
        descriptionNewsDetail = findViewById(R.id.descriptionNewsDetail);

        String urlImage = getIntent().getStringExtra("image");
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");

        Picasso.with(getApplicationContext())
                .load(urlImage)
                .into(imageNewsDetail);

        titleNewsDetail.setText(title);

        descriptionNewsDetail.setMovementMethod(new ScrollingMovementMethod());
        descriptionNewsDetail.setText(description);
    }
}
