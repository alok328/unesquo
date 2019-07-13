package com.alok328.raj.unesquo;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alok328.raj.unesquo.Interface.ItemClickListener;
import com.alok328.raj.unesquo.Model.Instruction;
import com.alok328.raj.unesquo.Model.News;
import com.alok328.raj.unesquo.Model.Quiz;
import com.alok328.raj.unesquo.ViewHolder.NewsViewHolder;
import com.alok328.raj.unesquo.ViewHolder.QuizViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;
import com.squareup.picasso.Picasso;

import dmax.dialog.SpotsDialog;

public class FragmentQuiz extends Fragment{

    View view;

    Button instButton, backQuizButton;

    RecyclerView listQuiz;
    RecyclerView.LayoutManager layoutManager;

    SpotsDialog dialog;
    FirebaseDatabase database;
    DatabaseReference quizList;
    DatabaseReference intruction;
    String ins;

    LinearLayout quizLinear, instLinear;

    TextView inst;

    FirebaseRecyclerAdapter<Quiz,QuizViewHolder> adapter;

    public FragmentQuiz() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.quiz_fragment, container, false);

        database = FirebaseDatabase.getInstance();
        quizList = database.getReference("Quiz");
        intruction = database.getReference("Instruction");

        dialog = (SpotsDialog) new SpotsDialog.Builder().setContext(getContext()).setTheme(R.style.Custom).build();

        listQuiz = view.findViewById(R.id.listQuiz);
        listQuiz.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        listQuiz.setLayoutManager(layoutManager);

        quizLinear = view.findViewById(R.id.quizLinear);
        instLinear = view.findViewById(R.id.instLinear);

        inst = (TextView) view.findViewById(R.id.instText);
        //ins = intruction.toString();



        intruction.child("data").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                inst.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        instButton = view.findViewById(R.id.instButton);
        instButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                quizLinear.setVisibility(View.INVISIBLE);
                instLinear.setVisibility(View.VISIBLE);

            }
        });


        backQuizButton = view.findViewById(R.id.backQuizButton);
        backQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizLinear.setVisibility(View.VISIBLE);
                instLinear.setVisibility(View.INVISIBLE);
            }
        });

        dialog.show();
        loadQuiz();
        return view;
    }

    private void loadQuiz() {
        adapter = new FirebaseRecyclerAdapter<Quiz, QuizViewHolder>
                (Quiz.class, R.layout.quiz_item, QuizViewHolder.class, quizList)
        {
            @Override
            protected void populateViewHolder(QuizViewHolder viewHolder, final Quiz model, int position) {
                viewHolder.quizQuestion.setText(model.getQuestion());

                dialog.dismiss();
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(),R.style.Custom);
                        alertDialog.setTitle("Question : "+ model.getQuestion());
                        alertDialog.setMessage("Answer : "+ model.getAnswer());
                        alertDialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    }
                });
            }
        };
        listQuiz.setAdapter(adapter);
    }



}
