package com.CMPUT301F22T26.foodegy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonToActivity = (Button) findViewById(R.id.button);
        buttonToActivity.setOnClickListener(view -> {
            Intent intent;
            intent = new Intent(view.getContext(), MealPlanActivity.class);
            view.getContext().startActivity(intent);});
    }
}
