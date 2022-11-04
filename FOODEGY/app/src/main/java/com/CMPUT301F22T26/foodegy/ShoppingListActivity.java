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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShoppingListActivity extends AppCompatActivity  implements AddIngredientFragment.OnFragmentInteractionListener {
    //list view related variables
    private ListView shoppingListView;
    private ArrayAdapter<ShoppingListItem> shoppingListItemArrayAdapter;
    private ArrayList<ShoppingListItem> shoppingListData;

    private NavigationBarView bottomNavBar;


    final private String android_id = "TEST_ID";
    final private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    final private CollectionReference IngredientStorage = firestore.collection("users")
            .document(android_id).collection("IngredientStorage");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_list_activity);

        shoppingListData = new ArrayList<ShoppingListItem>();
        shoppingListView = findViewById(R.id.shopping_list);

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


        shoppingListItemArrayAdapter = new ShoppingList(this, shoppingListData);
        shoppingListView.setAdapter(shoppingListItemArrayAdapter);
    }

    @Override
    public void addIngredientToDatabase(StorageIngredient newIngredient) {

    }

    @Override
    public void onEditPressed(StorageIngredient ingredient) {

    }
}