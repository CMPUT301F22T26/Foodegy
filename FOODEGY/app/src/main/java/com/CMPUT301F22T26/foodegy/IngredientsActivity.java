package com.CMPUT301F22T26.foodegy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The activity that handles user interactions with their Storage Ingredients.
 * Includes viewing, adding, editing, deleting Storage Ingredients
 */
public class IngredientsActivity extends AppCompatActivity implements AddIngredientFragment.OnFragmentInteractionListener {
    private ListView ingredientListView;
    private ArrayAdapter<StorageIngredient> ingredientAdapter;
    private ArrayList<StorageIngredient> ingredientData;
    private String sortingAttribute = "description";

    final private DatabaseManager dbm = DatabaseManager.getInstance();
    final private CollectionReference IngredientStorage = dbm.getIngredientStorageCollection();

    private Spinner sortingSpinner;
    private NavigationBarView bottomNavBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        sortingSpinner = findViewById(R.id.ingredientSortSpinner);
        bottomNavBar = (NavigationBarView) findViewById(R.id.bottom_nav);
        bottomNavBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ingredients:
                        break;
                    case R.id.meal_plan:
                        startActivity(new Intent(getBaseContext(), MealPlanActivity.class));
                        break;
                    case R.id.shopping_cart:
                        startActivity(new Intent(getBaseContext(), ShoppingListActivity.class));
                        break;
                    case R.id.recipes:
                        startActivity(new Intent(getBaseContext(), RecipesActivity.class));
                        break;

                }

                return false;
            }
        });
        ingredientData = new ArrayList<StorageIngredient>();
        ingredientListView = findViewById(R.id.ingredientList);

        ingredientAdapter = new IngredientList(this, ingredientData);
        ingredientListView.setAdapter(ingredientAdapter);


        // button to add ingredient
        final FloatingActionButton addIngredientButton = findViewById(R.id.floatingActionButton);
        addIngredientButton.setOnClickListener((v) -> {
            new AddIngredientFragment().show(getSupportFragmentManager(), "ADD_INGREDIENT");
        });

        // tap an item to view
        ingredientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                new ViewIngredientFragment(ingredientData.get(i)).show(getSupportFragmentManager(), "VIEW_INGREDIENT");
            }
        });

        // handle sorting
        sortingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] attributes = getResources().getStringArray(R.array.sort_ingredients);
                if ("Description".equals(attributes[i])) {
                    sortingAttribute = "description";
                }
                else if ("Best Before Date".equals(attributes[i])) {
                    sortingAttribute = "bestBeforeDate";
                }
                else if ("Location".equals(attributes[i])) {
                    sortingAttribute = "location";
                }
                else if ("Category".equals(attributes[i])) {
                    sortingAttribute = "category";
                }
                // reload the list!!
                IngredientStorage.orderBy(sortingAttribute).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        reloadIngredients(queryDocumentSnapshots);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // when something is changed in the firestore, update the list!
        IngredientStorage
                .orderBy(sortingAttribute)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        reloadIngredients(value);
                    }
                });
    };

    public void reloadIngredients(QuerySnapshot snapshot) {
        ingredientData.clear();
        for (QueryDocumentSnapshot doc : snapshot) {
            // reinitialize the whole list
            String id = doc.getId();
            Map<String, Object> data = doc.getData();
            String description = (String)data.get("description");
            String bestBefore = (String)data.get("bestBeforeDate");
            String location = (String)data.get("location");
            int amount = doc.getLong("amount").intValue();
            int unitCost = doc.getLong("unitCost").intValue();
            String category = (String)data.get("category");
            StorageIngredient newIngredient = new StorageIngredient(
                    description,
                    bestBefore,
                    location,
                    amount,
                    unitCost,
                    category
            );
            newIngredient.setId(id);
            ingredientData.add(newIngredient);
        }
        ingredientAdapter.notifyDataSetChanged();
    }
    /**
     * Adds a new StorageIngredient to the firestore
     * @param newIngredient
     *  The ingredient to be added
     */
    @Override
    public void addIngredientToDatabase(StorageIngredient newIngredient){
        // add an ingredient
        // NOTE: we do not need to add the newIngredient to the ingredientData list, as that is handled
        //   in the IngredientStorage.addSnapshotListener above!!!
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
     * Summons the fragment to edit ingredient.
     * Called whenever a StorageIngredient is edited. Updates the firestore accordingly
     * @param ingredient
     *  The ingredient to be updated
     */
    @Override
    public void onEditPressed(StorageIngredient ingredient) {
        new AddIngredientFragment(ingredient).show(getSupportFragmentManager(), "EDIT_INGREDIENT");
    }

    /**
     * Deletes an ingredient form a user's IngredientStorage
     * @param id
     *  id of ingredient to be deleted
     */
    public void deleteIngredientFromDatabase(String id) {
        // Note: we do not need to remove the ingredient from the ingredient list or adapter, that
        //   is handled with the IngredientStorage.addSnapshotListener above !!!!
        IngredientStorage
                .document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("MainActivity", "Succesfully deleted "+id);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MainActivity", "Failed to delete "+id);
                    }
                });
    }

    /**
     * Returns ingredient data, used for testing purposes
     * @return
     *  The ArrayList of current storage ingredients
     */
    public ArrayList<StorageIngredient> getIngredientData() {
        return ingredientData;
    }
}