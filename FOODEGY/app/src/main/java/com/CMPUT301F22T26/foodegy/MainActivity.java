package com.CMPUT301F22T26.foodegy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FragmentCompleteIngredient.OnFragmentInteractionListener{
    //list view related variables
    ListView shoppingListView;
    ArrayAdapter<ShoppingListItem> shoppingListItemArrayAdapter;
    public static ArrayList<ShoppingListItem> shoppingListData;

    //giving some inital values
    List<String> names = Arrays.asList("Apple", "Bread", "Cream cheese");
    List<String> des = Arrays.asList("healthy", "whole wheat", "good with bagels");
    List<String> bbda = Arrays.asList("1-11-2022", "2-11-2022", "2-12-2022");
    List<String> locations = Arrays.asList("Fridge", "Pantry", "Fridge");
    List<String> amounts = Arrays.asList("3", "3", "2");
    List<String> units = Arrays.asList("2", "2", "5");
    List<String> cates = Arrays.asList("Fruits", "Grain", "Dairy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_list_activity);

//        Button buttonToActivity = (Button) findViewById(R.id.button);
//        buttonToActivity.setOnClickListener(view -> {
//            Intent intent;
//            intent = new Intent(view.getContext(), ShoppingListActivity.class);
//            view.getContext().startActivity(intent);});

        shoppingListData = new ArrayList<ShoppingListItem>();
        shoppingListView = findViewById(R.id.shopping_list);

        //populating the list with two initial values
        shoppingListData.add(new ShoppingListItem(names.get(0), des.get(0), bbda.get(0), locations.get(0), amounts.get(0), units.get(0), cates.get(0)));
        shoppingListData.add(new ShoppingListItem(names.get(1), des.get(1), bbda.get(1), locations.get(1), amounts.get(1), units.get(1), cates.get(1)));
        shoppingListData.add(new ShoppingListItem(names.get(2), des.get(2), bbda.get(2), locations.get(2), amounts.get(2), units.get(2), cates.get(2)));

        shoppingListItemArrayAdapter = new ShoppingList(this, shoppingListData);
        shoppingListView.setAdapter(shoppingListItemArrayAdapter);
    }

    @Override
    public void onOkPressed(ShoppingListItem boughtFood, ShoppingListItem newFood) {
        shoppingListData.remove(boughtFood);
        shoppingListItemArrayAdapter.notifyDataSetChanged();



    }
}