package com.alok328.raj.unesquo;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alok328.raj.unesquo.Interface.ItemClickListener;
import com.alok328.raj.unesquo.Model.News;
import com.alok328.raj.unesquo.Model.Quiz;
import com.alok328.raj.unesquo.Model.User;
import com.alok328.raj.unesquo.ViewHolder.NewsViewHolder;
import com.alok328.raj.unesquo.ViewHolder.NewsViewHolderAdmin;
import com.alok328.raj.unesquo.ViewHolder.QuizViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import dmax.dialog.SpotsDialog;
import info.hoang8f.widget.FButton;

public class CurrentDataList extends AppCompatActivity {

    RecyclerView listNewsAdmin;
    RecyclerView.LayoutManager layoutManager;

    SpotsDialog dialog;
    News newNews;

    FButton signUp;

    EditText edtUsernameAdmin, edtPasswordAdmin;
    User newAdmin;
    DatabaseReference adminList;

    FirebaseDatabase database;
    DatabaseReference newsList;
    FirebaseStorage storage;
    StorageReference storageReference;

    MaterialEditText edtTitle, edtDescription;
    FButton btnUpload, btnSelect;

    FButton stateButton;
    FButton addContent;
    TextView title;

    Uri saveUri;
    private final int PICK_IMAGE_REQUEST = 71;

    FirebaseRecyclerAdapter<News,NewsViewHolderAdmin> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_data_list);

        database = FirebaseDatabase.getInstance();
        newsList = database.getReference("News");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        title = findViewById(R.id.title);

        adminList = database.getReference("Admin");



        signUp = findViewById(R.id.signUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CurrentDataList.this, R.style.Custom);
                alertDialog.setTitle("Add Admin");
                alertDialog.setMessage("Please enter the details");

                LayoutInflater layoutInflater = getLayoutInflater();
                View add_admin_layout = layoutInflater.inflate(R.layout.signup_layout,null);

                edtUsernameAdmin = (EditText)add_admin_layout.findViewById(R.id.usernameSignUp);
                edtPasswordAdmin = (EditText) add_admin_layout.findViewById(R.id.passwordSignup);

                alertDialog.setView(add_admin_layout);
                alertDialog.setIcon(R.drawable.add);

                alertDialog.setPositiveButton("SignUp", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        if(edtUsernameAdmin.getText().length()<1 || edtPasswordAdmin.getText().length()<1){
                            Toast.makeText(CurrentDataList.this, "Please enter valid data", Toast.LENGTH_SHORT).show();
                        }else{
                            newAdmin = new User(edtPasswordAdmin.getText().toString());
                            adminList.child(edtUsernameAdmin.getText().toString()).setValue(newAdmin);

                            Toast.makeText(CurrentDataList.this, "SignUp Successful!", Toast.LENGTH_SHORT).show();
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
        });

        dialog = (SpotsDialog) new SpotsDialog.Builder().setContext(this).setTheme(R.style.Custom).build();

        listNewsAdmin = findViewById(R.id.listNewsAdmin);
        listNewsAdmin.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listNewsAdmin.setLayoutManager(layoutManager);

        stateButton = findViewById(R.id.stateButton);
        stateButton.setButtonColor(R.color.colorPrimary);
        addContent = findViewById(R.id.addButton);
        addContent.setButtonColor(R.color.buttonText);
        addContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        dialog.show();
        loadNews();

        stateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent quizIntentAdmin = new Intent(CurrentDataList.this, AdminQuizList.class);
                startActivity(quizIntentAdmin);
            }
        });

    }

    private void showDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CurrentDataList.this, R.style.Custom);
        alertDialog.setTitle("Add News");
        alertDialog.setMessage("Please enter the details");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View add_news_layout = layoutInflater.inflate(R.layout.add_new_news_layout,null);

        edtTitle = add_news_layout.findViewById(R.id.edtTitle);
        edtDescription = add_news_layout.findViewById(R.id.edtDescription);
        btnSelect = add_news_layout.findViewById(R.id.btnSelectImg);
        btnUpload = add_news_layout.findViewById(R.id.btnUpload);

        btnUpload.setButtonColor(R.color.colorPrimary);
        btnSelect.setButtonColor(R.color.colorPrimary);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtTitle.getText().length()!=0)
                    uploadImage();
                else
                    Toast.makeText(CurrentDataList.this, "Fields should not be empty!", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.setView(add_news_layout);
        alertDialog.setIcon(R.drawable.add);

        alertDialog.setPositiveButton("Add News", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(newNews!=null && edtTitle.getText().length()!=0 && edtDescription.getText().length()!=0){
                    newsList.push().setValue(newNews);
                }
                else{
                    Toast.makeText(CurrentDataList.this, "Fields should not be empty!", Toast.LENGTH_SHORT).show();
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

    private void uploadImage() {
        if(saveUri!=null){
            final SpotsDialog dialog = (SpotsDialog) new SpotsDialog.Builder().setMessage("Uploading")
                    .setContext(this).setTheme(R.style.Custom).build();
            dialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+ imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            dialog.dismiss();
                            Toast.makeText(CurrentDataList.this, "Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    newNews = new News(edtTitle.getText().toString(), edtDescription.getText().toString(),
                                            uri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(CurrentDataList.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()
                                    / taskSnapshot.getTotalByteCount());
                            dialog.setMessage("Uploaded" + progress + "%");
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){

            saveUri = data.getData();
            btnSelect.setText("Image Selected!");

        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose Image"), PICK_IMAGE_REQUEST);

    }


    private void loadNews() {
        adapter = new FirebaseRecyclerAdapter<News, NewsViewHolderAdmin>
                (News.class, R.layout.article_item, NewsViewHolderAdmin.class, newsList)
        {
            @Override
            protected void populateViewHolder(NewsViewHolderAdmin viewHolder, final News model, int position) {
                viewHolder.newsName.setText(model.getTitle());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.newsImage);

                dialog.dismiss();
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(getBaseContext(), model.getDescription(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        listNewsAdmin.setAdapter(adapter);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if(item.getTitle().equals("Delete")){
            deleteNews(adapter.getRef(item.getOrder()).getKey());
        }

        return super.onContextItemSelected(item);
    }

    private void deleteNews(final String key) {
        AlertDialog.Builder alertDialog =new AlertDialog.Builder(CurrentDataList.this);
        alertDialog.setTitle("Delete News");
        alertDialog.setMessage("Are you sure?");
        alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newsList.child(key).removeValue();
                deleteImage(key);
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

    private void deleteImage(final String key) {
        newsList.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String uri = dataSnapshot.child(key).child("image").getValue(String.class);

                try{
                    if(!uri.equals(null)){

                        StorageReference pic = FirebaseStorage.getInstance().getReferenceFromUrl(uri);
                        pic.delete();
                        finish();

                    }


                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "Logout Successful!", Toast.LENGTH_SHORT).show();
        finish();
    }


}
