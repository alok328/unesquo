package com.alok328.raj.unesquo.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alok328.raj.unesquo.Interface.ItemClickListener;
import com.alok328.raj.unesquo.R;

public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView newsName;
    public ImageView newsImage;

    private ItemClickListener itemClickListener;

    public NewsViewHolder(View itemView) {
        super(itemView);

        newsName = itemView.findViewById(R.id.newsName);
        newsImage = itemView.findViewById(R.id.newsImage);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }


}
