package com.CMPUT301F22T26.foodegy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.CMPUT301F22T26.foodegy.databinding.ActivityRecipesBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This activity will allow the user to navigate and manage their shopping cart, as well as mark
 * items as purchased and add them to storage
 */
public class ShoppingListActivity extends AppCompatActivity implements AddIngredientFragment.OnFragmentInteractionListener {
    //list view related variables
    private ListView shoppingListView;
    private ArrayAdapter<ShoppingListItem> shoppingListItemArrayAdapter;
    private ArrayList<ShoppingListItem> shoppingListData;

    private NavigationBarView bottomNavBar;

    // initialize the firebase
    final private String android_id = "TEST_ID";
    final private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    final private CollectionReference IngredientStorage = firestore.collection("users")
            .document(android_id).collection("IngredientStorage");

    //giving some inital values
    // since for now, the ShoppingList is not hooked up to the MealPlan list
    List<String> names = Arrays.asList("Apple", "Bread", "Cream cheese");
    List<Integer> amounts = Arrays.asList(3, 3, 2);
    List<Integer> units = Arrays.asList(2, 2, 5);


    List<String> cates = Arrays.asList("Vegetable", "Grain", "Dairy");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_list_activity);

        shoppingListData = new ArrayList<ShoppingListItem>();
        shoppingListView = findViewById(R.id.shopping_list);

        shoppingListData.add(new ShoppingListItem(names.get(0), amounts.get(0), units.get(0), cates.get(0)));
        shoppingListData.add(new ShoppingListItem(names.get(1), amounts.get(1), units.get(1), cates.get(1)));
        shoppingListData.add(new ShoppingListItem(names.get(2), amounts.get(2), units.get(2), cates.get(2)));

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

    /**
     * Responsible for calling the AddIngredientFragment
     * (this method gets called from inside a Shopping List View when the user
     * marks that particular item as bought).
     * This method will also remove the user's item from the shopping list as it is displayed on screen
     * @param bought the item that has been bought
     */
    public void inflateFragment(ShoppingListItem bought){
        shoppingListData.remove(bought);
        shoppingListItemArrayAdapter.notifyDataSetChanged();
        new AddIngredientFragment(bought).show(getSupportFragmentManager(), "ADD_SHOPPING_LIST_ITEM");
    }


    /**
     * This handles adding the new ingredient onto the Firebase
     * @param newIngredient the new ingredient that we want to store
     */
    @Override
    public void addIngredientToDatabase(StorageIngredient newIngredient) {
        // add an ingredient
        IngredientStorage
                .add(newIngredient)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // get the auto-generated ID for the item
                        Log.d("MainActivity",
                                "Added storage ingredient "+newIngredient.getDescription()+", id="+documentReference.getId());
                        newIngredient.setId(documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MainActivity",
                                "Failed to add storage ingredient "+newIngredient.getDescription());
                    }
                })
        ;

    }

    /**
     * There is no editing in shopping list activity so this method is simply overwritten
     * @param ingredient the ingredient the user would be trying to edit
     */
    @Override
    public void onEditPressed(StorageIngredient ingredient) {

    }
}