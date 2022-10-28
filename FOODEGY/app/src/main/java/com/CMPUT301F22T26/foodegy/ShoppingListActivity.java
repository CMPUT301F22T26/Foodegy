package com.CMPUT301F22T26.foodegy;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class ShoppingListActivity extends AppCompatActivity {

    //list view related variables
    ListView shoppingListView;
    ArrayAdapter<ShoppingListItem> shoppingListItemArrayAdapter;
    ArrayList<ShoppingListItem> shoppingListData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_list_activity);

        //Initialize list variables
        shoppingListData = new ArrayList<ShoppingListItem>();
        shoppingListView = findViewById(R.id.shopping_list);

        //populating the list with two initial values
        shoppingListData.add(new ShoppingListItem("Mango", "Sweet fruit", "1-11-2022", "Fridge", "3", "2", "Fruits"));
        shoppingListData.add(new ShoppingListItem("Bread", "whole wheat", "1-11-2022", "Bakery", "3", "2", "Grains"));

        shoppingListItemArrayAdapter = new ShoppingList(this, shoppingListData);
        shoppingListView.setAdapter(shoppingListItemArrayAdapter);

    }
}
