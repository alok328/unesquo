package com.alok328.raj.unesquo;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alok328.raj.unesquo.Interface.ItemClickListener;
import com.alok328.raj.unesquo.Model.News;
import com.alok328.raj.unesquo.Model.Quiz;
import com.alok328.raj.unesquo.ViewHolder.NewsViewHolderAdmin;
import com.alok328.raj.unesquo.ViewHolder.QuizViewHolder;
import com.alok328.raj.unesquo.ViewHolder.QuizViewHolderAdmin;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dmax.dialog.SpotsDialog;
import info.hoang8f.widget.FButton;

public class AdminQuizList extends AppCompatActivity {

    RecyclerView listQuizAdmin;
    RecyclerView.LayoutManager layoutManager;

    FButton back, addContent;

    Button editInstructionButton;
    LinearLayout quizAminLinear, instAdminLinear;
    FButton goBackQuizAdmin,upadteInstructionAdmin;
    EditText editInstruction;

    Quiz newQuiz;

    FirebaseDatabase databaseAdmin;
    DatabaseReference quizListAdmin;
    DatabaseReference intruction;

    EditText edtQuestion, edtAnswer;

    SpotsDialog dialog;

    TextView question;

    FirebaseRecyclerAdapter<Quiz,QuizViewHolderAdmin> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_quiz_list);

        quizAminLinear = findViewById(R.id.quizAdminLinear);
        instAdminLinear = findViewById(R.id.instructionAdminLinear);

        editInstructionButton = findViewById(R.id.editInstructionButton);
        editInstructionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizAminLinear.setVisibility(View.INVISIBLE);
                instAdminLinear.setVisibility(View.VISIBLE);
            }
        });

        goBackQuizAdmin = findViewById(R.id.goBackQuizAdmin);
        goBackQuizAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizAminLinear.setVisibility(View.VISIBLE);
                instAdminLinear.setVisibility(View.INVISIBLE);
            }
        });

        editInstruction = findViewById(R.id.editInstruction);
        databaseAdmin = FirebaseDatabase.getInstance();
        quizListAdmin = databaseAdmin.getReference("Quiz");
        intruction = databaseAdmin.getReference("Instruction");

        upadteInstructionAdmin = findViewById(R.id.updateInstructionAdmin);
        upadteInstructionAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                if(editInstruction.getText().length()>=1){
                    intruction.child("data").setValue(editInstruction.getText().toString());
                    Toast.makeText(AdminQuizList.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
                    editInstruction.setText("");
                    dialog.dismiss();
                }
                else{
                    dialog.dismiss();
                    Snackbar.make(getCurrentFocus(),"Instruction field shouln't be empty!",Snackbar.LENGTH_SHORT)
                            .setAction("a",null).show();
                }
            }
        });

        goBackQuizAdmin.setButtonColor(R.color.colorPrimary);
        upadteInstructionAdmin.setButtonColor(R.color.colorPrimary);
        back = findViewById(R.id.goBack);
        addContent = findViewById(R.id.addContent);

        back.setButtonColor(R.color.colorPrimary);
        addContent.setButtonColor(R.color.colorPrimary);

        question = findViewById(R.id.quizQuestionAdmin);

        listQuizAdmin = findViewById(R.id.listQuizAdmin);
        listQuizAdmin.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listQuizAdmin.setLayoutManager(layoutManager);
        dialog = (SpotsDialog) new SpotsDialog.Builder().setContext(this).setTheme(R.style.Custom).build();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        dialog.show();
        loadQuiz();
    }

    private void showDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminQuizList.this, R.style.Custom);
        alertDialog.setTitle("Add Quiz");
        alertDialog.setMessage("Please fill the following");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View add_quiz_layout = layoutInflater.inflate(R.layout.add_new_quiz_layout,null);

        edtQuestion = add_quiz_layout.findViewById(R.id.edtQuestion);
        edtAnswer = add_quiz_layout.findViewById(R.id.edtAnswer);

        alertDialog.setView(add_quiz_layout);
        alertDialog.setIcon(R.drawable.add);

        alertDialog.setPositiveButton("Add Quiz", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newQuiz = new Quiz(edtQuestion.getText().toString(), edtAnswer.getText().toString());
                if(newQuiz!=null && edtQuestion.getText().length()!=0 && edtAnswer.getText().length()!=0){
                    quizListAdmin.push().setValue(newQuiz);
                }
                else{
                    Toast.makeText(AdminQuizList.this, "Fields should not be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void loadQuiz() {
        adapter = new FirebaseRecyclerAdapter<Quiz, QuizViewHolderAdmin>
                (Quiz.class, R.layout.quiz_item, QuizViewHolderAdmin.class, quizListAdmin)
        {
            @Override
            protected void populateViewHolder(QuizViewHolderAdmin viewHolder, final Quiz model, int position) {
                viewHolder.quizQuestion.setText(model.getQuestion());

                dialog.dismiss();
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(getBaseContext(), model.getAnswer(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        listQuizAdmin.setAdapter(adapter);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if(item.getTitle().equals("Delete")){
            deleteNews(adapter.getRef(item.getOrder()).getKey());
        }

        return super.onContextItemSelected(item);
    }

    private void deleteNews(final String key) {
        AlertDialog.Builder alertDialog =new AlertDialog.Builder(AdminQuizList.this);
        alertDialog.setTitle("Delete News");
        alertDialog.setMessage("Are you sure?");
        alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                quizListAdmin.child(key).removeValue();
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
