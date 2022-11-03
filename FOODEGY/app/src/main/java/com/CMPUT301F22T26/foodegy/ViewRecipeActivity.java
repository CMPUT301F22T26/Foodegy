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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class ViewRecipeActivity extends AppCompatActivity {

    private ActivityViewRecipeBinding binding;
    private Button cancel;
    private Button edit;
    private Button delete;

    final private String android_id = "TEST_ID";
    final private CollectionReference RecipesCollection = FirebaseFirestore.getInstance()
            .collection("users").document(android_id).collection("Recipes");
    private StorageReference userFilesRef = FirebaseStorage.getInstance().getReference().child(android_id);

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
        String title = intent.getStringExtra("title");
        String hours = intent.getStringExtra("hours");
        String minutes = intent.getStringExtra("minutes");
        String servingValue = intent.getStringExtra("servingValue");
        String category = intent.getStringExtra("category");
        String amount = intent.getStringExtra("amount");
        String imageFileName = intent.getStringExtra("imageFileName");
        String comments = intent.getStringExtra("comments");
        String id = intent.getStringExtra("id");

        binding.titleText.setText(title);
        binding.timeText.setText(hours +" : " +minutes);
        binding.servingsText.setText(servingValue);
        binding.categoryText.setText(category);
        binding.amountText.setText(amount);

        // get download url & put it in the imageview
        Context context = this;
        final String[] downloadUrl = new String[1];
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

        edit = findViewById(R.id.editButton);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewRecipeActivity.this, AddRecipeActivity.class);
                startActivity(intent);
            }
        });

        delete = findViewById(R.id.deleteButton);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // delete the recipe! from the firestore!
                String id = intent.getStringExtra("id");
                RecipesCollection.document(id).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("ViewRecipeActivity", "Successfully deleted recipe "+id);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("ViewRecipeActivity", "Failed to delete recipe"+id);
                            }
                        });
                // delete the image from the firebase storage
                if (imageFileName != null && !"".equals(imageFileName)) {
                    userFilesRef.child(imageFileName).delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("ViewRecipeActivity", "Successfully deleted image " + imageFileName);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("ViewRecipeActivity", "Failed to delete image " + imageFileName);
                                }
                            });
                }
                finish();
            }
        });
    }

}