package com.CMPUT301F22T26.foodegy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class AddRecipeActivity extends AppCompatActivity {

    EditText titleText;
    EditText hourText;
    EditText minuteText;
    EditText servingsText;
    EditText amountText;
    EditText commentText;

    Button imageButton;
    Button ingredientsButton;
    Button submitButton;
    Button cancelButton;

    Spinner categorySpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipe_activity);

        titleText = findViewById(R.id.title_text);
        hourText = findViewById(R.id.hour_text);
        minuteText = findViewById(R.id.minute_text);
        servingsText = findViewById(R.id.servings_text);
        amountText = findViewById(R.id.amount_text);
        commentText = findViewById(R.id.comment_text);

        imageButton = findViewById(R.id.image_button);
        ingredientsButton = findViewById(R.id.ingredient_button);
        submitButton = findViewById(R.id.submit_button);
        cancelButton = findViewById(R.id.cancel_button);
        
    }
}
