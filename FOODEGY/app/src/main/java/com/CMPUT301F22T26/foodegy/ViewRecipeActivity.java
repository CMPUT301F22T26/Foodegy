package com.CMPUT301F22T26.foodegy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.CMPUT301F22T26.foodegy.databinding.ActivityViewRecipeBinding;

public class ViewRecipeActivity extends AppCompatActivity {

    ActivityViewRecipeBinding binding;
    Button cancel;
    Button edit;
    Button delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        binding = ActivityViewRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = this.getIntent();

        if(intent!=null){
            String title = intent.getStringExtra("title");
            String hours = intent.getStringExtra("hours");
            String minutes = intent.getStringExtra("minutes");
            String servingVaue = intent.getStringExtra("servingValue");
            String category = intent.getStringExtra("category");
            String amount = intent.getStringExtra("amount");
            int imageIDint = intent.getIntExtra("imageidint", R.drawable.poutine);
           // Uri imageIDuri = intent.getData("imageiduri");
            String comments = intent.getStringExtra("comments");

            binding.titleText.setText(title);
            binding.timeText.setText(hours +" : " +minutes);
            binding.servingsText.setText(servingVaue);
            binding.categoryText.setText(category);
            binding.amountText.setText(amount);
            binding.foodImage.setImageResource(imageIDint);
         //   binding.foodImage.setImageURI(imageIDuri);
            binding.commentText.setText(comments);

            cancel = findViewById(R.id.cancelButton);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ViewRecipeActivity.this, RecipesActivity.class);
                    startActivity(intent);
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







        }
    }

}