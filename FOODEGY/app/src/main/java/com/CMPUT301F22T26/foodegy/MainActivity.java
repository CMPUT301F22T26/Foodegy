package com.CMPUT301F22T26.foodegy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Activity that is displayed at the beginning of the app & that will direct the user
 * to all the other activities & tabs
 */
public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // locate the 4 buttons & respond to button click by starting the appropriate activity
        Button buttonToIngredientActivity = (Button) findViewById(R.id.buttonToIngredientActivity);
        buttonToIngredientActivity.setOnClickListener(view -> {
            Intent intent;
            intent = new Intent(view.getContext(), IngredientsActivity.class);
            view.getContext().startActivity(intent);});

        Button buttonToMealPlanActivity = (Button) findViewById(R.id.buttonToMealPlanActivity);
        buttonToMealPlanActivity.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), MealPlanActivity.class);
            view.getContext().startActivity(intent);});

        Button buttonRecipesActivity = (Button) findViewById(R.id.buttonToRecipesActivity);
        buttonRecipesActivity.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), RecipesActivity.class);
            view.getContext().startActivity(intent);});

        Button buttonToShoppingListActivity = (Button) findViewById(R.id.buttonToShoppingListActivity);
        buttonToShoppingListActivity.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), ShoppingListActivity.class);
            view.getContext().startActivity(intent);});

    }
}