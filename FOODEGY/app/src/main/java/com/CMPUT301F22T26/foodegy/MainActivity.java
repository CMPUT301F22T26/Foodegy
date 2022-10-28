package com.CMPUT301F22T26.foodegy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonToActivity = (Button) findViewById(R.id.button);
        buttonToActivity.setOnClickListener(view -> {
        Intent intent;
        intent = new Intent(view.getContext(), ShoppingListActivity.class);
        view.getContext().startActivity(intent);});
    }
}