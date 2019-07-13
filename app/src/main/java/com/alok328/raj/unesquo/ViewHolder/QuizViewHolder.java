package com.alok328.raj.unesquo.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alok328.raj.unesquo.Interface.ItemClickListener;
import com.alok328.raj.unesquo.R;

public class QuizViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView quizQuestion;

    private ItemClickListener itemClickListener;

    public QuizViewHolder(View itemView) {
        super(itemView);

        quizQuestion = itemView.findViewById(R.id.quizQuestion);

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
