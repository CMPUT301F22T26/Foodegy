package com.CMPUT301F22T26.foodegy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.CMPUT301F22T26.foodegy.databinding.ActivityRecipesBinding;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShoppingListActivity extends AppCompatActivity {
    //list view related variables
    private ListView shoppingListView;
    private ArrayAdapter<ShoppingListItem> shoppingListItemArrayAdapter;
    private ArrayList<ShoppingListItem> shoppingListData;

    private NavigationBarView bottomNavBar;

    //giving some inital values
    List<String> names = Arrays.asList("Apple", "Bread");
    List<String> des = Arrays.asList("healthy", "whole wheat");
    List<String> bbda = Arrays.asList("1-11-2022", "2-11-2022");
    List<String> locations = Arrays.asList("fridge", "bakery");
    List<String> amounts = Arrays.asList("3", "3");
    List<String> units = Arrays.asList("2", "2");
    List<String> cates = Arrays.asList("fruits", "breads");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_list_activity);

        bottomNavBar = (NavigationBarView) findViewById(R.id.bottom_nav);
        bottomNavBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ingredients:
                        startActivity(new Intent(getBaseContext(), IngredientsActivity.class));
                        break;
                    case R.id.meal_plan:
                        startActivity(new Intent(getBaseContext(), MealPlanActivity.class));
                        break;
                    case R.id.shopping_cart:
                        break;
                    case R.id.recipes:
                        startActivity(new Intent(getBaseContext(), RecipesActivity.class));
                        break;

                }

                return false;
            }
        });

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

        shoppingListItemArrayAdapter = new ShoppingList(this, shoppingListData);
        shoppingListView.setAdapter(shoppingListItemArrayAdapter);
    }
}