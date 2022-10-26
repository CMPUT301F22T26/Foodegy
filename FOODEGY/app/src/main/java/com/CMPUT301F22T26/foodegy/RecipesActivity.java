package com.CMPUT301F22T26.foodegy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RecipesActivity extends AppCompatActivity {
    Button sortbutton;
    Button categorybutton;
    Button addbutton;
    Button viewmorebutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        addbutton = findViewById(R.id.addRecipe);
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipesActivity.this, AddRecipeActivity.class);
                startActivity(intent);
            }
        });

    }

}