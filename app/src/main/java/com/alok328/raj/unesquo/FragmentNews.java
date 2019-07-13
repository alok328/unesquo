package com.alok328.raj.unesquo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alok328.raj.unesquo.Interface.ItemClickListener;
import com.alok328.raj.unesquo.Model.News;
import com.alok328.raj.unesquo.ViewHolder.NewsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import dmax.dialog.SpotsDialog;

public class FragmentNews extends Fragment{

    View view;

    RecyclerView listNews;
    RecyclerView.LayoutManager layoutManager;

    SpotsDialog dialog;
    FirebaseDatabase database;
    DatabaseReference newsList;

    String newsID;
    FirebaseRecyclerAdapter<News,NewsViewHolder> adapter;

    public FragmentNews() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.news_fragment, container, false);

        database = FirebaseDatabase.getInstance();
        newsList = database.getReference("News");

        dialog = (SpotsDialog) new SpotsDialog.Builder().setContext(getContext()).setTheme(R.style.Custom).build();

        listNews = view.findViewById(R.id.listNews);
        listNews.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        listNews.setLayoutManager(layoutManager);

        dialog.show();
        loadNews();
        return view;

    }


    private void loadNews() {

        adapter = new FirebaseRecyclerAdapter<News, NewsViewHolder>
                (News.class, R.layout.article_item, NewsViewHolder.class, newsList)
        {
            @Override
            protected void populateViewHolder(NewsViewHolder viewHolder, final News model, int position) {
                viewHolder.newsName.setText(model.getTitle());
                Picasso.with(getContext()).load(model.getImage()).into(viewHolder.newsImage);

                dialog.dismiss();
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
//                        Toast.makeText(getContext(), model.getTitle(), Toast.LENGTH_SHORT).show();
                        Intent detailsIntent = new Intent(getActivity(), NewsDetails.class);
                        detailsIntent.putExtra("title",model.getTitle());
                        detailsIntent.putExtra("description", model.getDescription());
                        detailsIntent.putExtra("image", model.getImage());
                        startActivity(detailsIntent);
                    }
                });
            }
        };
        listNews.setAdapter(adapter);
    }
}
