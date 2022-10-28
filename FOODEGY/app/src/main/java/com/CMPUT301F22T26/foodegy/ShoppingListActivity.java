package com.CMPUT301F22T26.foodegy;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ShoppingListActivity extends AppCompatActivity {

//    //list view related variables
//    ListView shoppingListView;
//    ArrayAdapter<ShoppingListItem> shoppingListItemArrayAdapter;
//    ArrayList<ShoppingListItem> shoppingListData;
//
//    //giving some inital values
//    List<String> names = Arrays.asList("A", "B");
//    List<String> des = Arrays.asList("a", "b");
//    List<String> bbda = Arrays.asList("a", "b");
//    List<String> locations = Arrays.asList("a", "b");
//    List<String> amount = Arrays.asList("a", "b");
//    List<String> units = Arrays.asList("a", "b");
//    List<String> cates = Arrays.asList("a", "b");


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_list_activity);

        //Get the intent that started this activity and extract the String
//        Intent intent = getIntent();
//        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        //Initialize list variables
//        shoppingListData = new ArrayList<ShoppingListItem>();
//        shoppingListView = findViewById(R.id.shopping_list);
//
//        //populating the list with two initial values
//        shoppingListData.add(new ShoppingListItem(names.get(0), des.get(0), bbda.get(0), locations.get(0), amount.get(0), units.get(0), cates.get(0)));
//        shoppingListData.add(new ShoppingListItem(names.get(1), des.get(1), bbda.get(1), locations.get(1), amount.get(1), units.get(1), cates.get(1)));
//
//        shoppingListItemArrayAdapter = new ShoppingList(this, shoppingListData);
//        shoppingListView.setAdapter(shoppingListItemArrayAdapter);

    }
}
