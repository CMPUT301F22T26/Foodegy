package com.CMPUT301F22T26.foodegy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class IngredientsActivity extends AppCompatActivity implements AddIngredientFragment.OnFragmentInteractionListener {

    ListView ingredientListView;
    ArrayAdapter<StorageIngredient> ingredientAdapter;
    ArrayList<StorageIngredient> ingredientData;

    // each device has a unique id, use that as their own personal collection name
    final private String android_id = "TEST_ID";
    final private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    final private CollectionReference IngredientStorage = firestore.collection("users")
            .document(android_id).collection("IngredientStorage");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        ingredientData = new ArrayList<StorageIngredient>();
        ingredientListView = findViewById(R.id.ingredientList);

        ingredientAdapter = new IngredientList(this, ingredientData);
        ingredientListView.setAdapter(ingredientAdapter);


        // button to add ingredient
        final FloatingActionButton addIngredientButton = findViewById(R.id.floatingActionButton);
        addIngredientButton.setOnClickListener((v)->{
            new AddIngredientFragment().show(getSupportFragmentManager(), "ADD_INGREDIENT");
        });

        // tap an item to view
        ingredientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                new ViewIngredientFragment(ingredientData.get(i)).show(getSupportFragmentManager(), "VIEW_INGREDIENT");
            }
        });


        // when something is changed in the firestore, update the list!
        IngredientStorage
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        ingredientData.clear();
                        for (QueryDocumentSnapshot doc : value) {
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
                });

    };

    /**
     * Adds a new StorageIngredient to the firestore
     * @param newIngredient
     *  The ingredient to be added
     */
    @Override
    public void onAddPressed(StorageIngredient newIngredient){
        // add an ingredient
        ingredientAdapter.add(newIngredient);
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
     * Called whenever a StorageIngredient is edited. Updates the firestore accordingly
     * @param ingredient
     *  The ingredient to be updated
     */
    @Override
    public void onEditPressed(StorageIngredient ingredient) {
        new AddIngredientFragment(ingredient).show(getSupportFragmentManager(), "EDIT_INGREDIENT");
        // edit the ingredient

    }
    public void editIngredientInDatabase(String id, StorageIngredient ingredient){
        IngredientStorage
                .document(id)
                .set(ingredient);

    }

    public void deleteIngredient(StorageIngredient ingredient){

                deleteIngredientFromDatabase(ingredient.getId());
                ingredientAdapter.remove(ingredient);
    }


    /**
     * Deletes an ingredient form a user's IngredientStorage
     * @param id
     *  id of ingredient to be deleted
     */
    public void deleteIngredientFromDatabase(String id) {
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
}