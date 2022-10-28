package com.CMPUT301F22T26.foodegy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //list view related variables
//    ListView shoppingListView;
//    ArrayAdapter<ShoppingListItem> shoppingListItemArrayAdapter;
//    ArrayList<ShoppingListItem> shoppingListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonToActivity = (Button) findViewById(R.id.button);
        String someMessage = "yo!";
//
        buttonToActivity.setOnClickListener(view -> {
            Intent intent;
            intent = new Intent(this, TempActivity.class);
//            intent.putExtra(EXTRA_MESSAGE, someMessage);
            startActivity(intent);
        });
//        shoppingListData = new ArrayList<ShoppingListItem>();
//        shoppingListView = findViewById(R.id.shopping_list);
//
//        //populating the list with two initial values
//        shoppingListData.add(new ShoppingListItem("Mango", "Sweet fruit", "1-11-2022", "Fridge", "3", "2", "Fruits"));
//        shoppingListData.add(new ShoppingListItem("Bread", "whole wheat", "1-11-2022", "Bakery", "3", "2", "Grains"));
//
//        shoppingListItemArrayAdapter = new ShoppingList(this, shoppingListData);
//        shoppingListView.setAdapter(shoppingListItemArrayAdapter);
    }
}