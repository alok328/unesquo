package com.alok328.raj.unesquo.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.alok328.raj.unesquo.Interface.ItemClickListener;
import com.alok328.raj.unesquo.R;

public class QuizViewHolderAdmin extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener{

    public TextView quizQuestion;

    private ItemClickListener itemClickListener;

    public QuizViewHolderAdmin(View itemView) {
        super(itemView);

        quizQuestion = itemView.findViewById(R.id.quizQuestion);

        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, 0, getAdapterPosition(), "Delete");
    }
}
