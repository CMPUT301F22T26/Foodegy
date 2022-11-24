package com.CMPUT301F22T26.foodegy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;

import com.CMPUT301F22T26.foodegy.databinding.ActivityViewRecipeBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

/**
 * This class allows the user to view the recipe, and all of the data/details that the recipe has
 */
public class ViewRecipeActivity extends AppCompatActivity {

    private ActivityViewRecipeBinding binding;
    private FloatingActionButton cancel;
    private Button edit;
    private Button delete;
    private String title;
    private int hours;
    private int minutes;
    private int servingValue;
    private String category;
    private String imageFileName;
    private String comments;
    private String id;


    final private DatabaseManager dbm = DatabaseManager.getInstance();
    final private StorageReference userFilesRef = dbm.getUserFilesRef();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        binding = ActivityViewRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        final Intent intent = this.getIntent();

        if(intent==null) {
            return;
        }
        title = intent.getStringExtra("title");
        hours = intent.getIntExtra("hours", -1);
        minutes = intent.getIntExtra("minutes", -1);
        servingValue = intent.getIntExtra("servingValue", -1);
        category = intent.getStringExtra("category");
        String amount = intent.getStringExtra("amount");
        imageFileName = intent.getStringExtra("imageFileName");
        comments = intent.getStringExtra("comments");
        id = intent.getStringExtra("id");

        binding.titleText.setText(title);
        String minutesString = minutes<10 ? "0"+minutes : ""+minutes;
        binding.timeText.setText(hours +" : " +minutesString);
        binding.servingsText.setText(String.valueOf(servingValue));
        binding.categoryText.setText(category);

        // get download url & put it in the imageview
        Context context = this;
        if (imageFileName != null && !"".equals(imageFileName)) {
            userFilesRef.child(imageFileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Log.d("ViewRecipeActivity", "Got download URL for " + uri.toString());
                    String url = uri.toString();
                    Glide.with(context).load(url).into(binding.foodImage);
                }
            });
        }
        binding.commentText.setText(comments);
        cancel = findViewById(R.id.cancelButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        delete = findViewById(R.id.deleteButton);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // delete the recipe! from the firestore!
                dbm.deleteRecipeFromDatabase(id, imageFileName);
                finish();
            }
        });

        edit = findViewById(R.id.editButton);
        System.out.println(title);
        edit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent2 = new Intent(ViewRecipeActivity.this, EditRecipeActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("title",title);
                                        bundle.putInt("hours",hours);
                                        bundle.putInt("minutes",minutes);
                                        bundle.putInt("servingValue",servingValue);
                                        bundle.putString("category",category);
                                        bundle.putString("imageFileName",imageFileName);
                                        bundle.putString("comments",comments);
                                        bundle.putString("id",id);
                                        intent2.putExtras(bundle);


                                        /*intent.putExtra("title",title);
                                        intent.putExtra("hours",hours);
                                        intent.putExtra("minutes",minutes);
                                        intent.putExtra("servingValue",servingValue);
                                        intent.putExtra("category",category);
                                        intent.putExtra("imageFileName",imageFileName );
                                        intent.putExtra("comments",comments);
                                        intent.putExtra("id",id);*/
                                        startActivity(intent2);
                                    }
                                }

        );

    }

}