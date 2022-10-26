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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AddIngredientFragment.OnFragmentInteractionListener {
    ListView ingredientListView;
    ArrayAdapter<StorageIngredient> ingredientAdapter;
    ArrayList<StorageIngredient> ingredientData;

    List<String> descriptions = Arrays.asList("Sausage", "Mushroom", "Onion", "Pepperoni");
    List<String> bbDates = Arrays.asList("10-09-2022", "27-01-2023", "05-11-2022", "01-10-2022");
    List<String> locations = Arrays.asList("Fridge", "Fridge", "Pantry", "Fridge");
    List<Integer> amounts = Arrays.asList(12, 24, 4, 1);
    List<Integer> units = Arrays.asList(5, 2, 1, 3);
    List<String> categories = Arrays.asList("Meat", "Vegetable", "Vegetable", "Meat");

    // each device has a unique id, use that as their own personal collection name
    final private String android_id = "TEST_ID";
    final private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    final private CollectionReference IngredientStorage = firestore.collection("users")
            .document(android_id).collection("IngredientStorage");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ingredientData = new ArrayList<StorageIngredient>();
        ingredientListView = findViewById(R.id.ingredientList);

        ingredientAdapter = new IngredientList(this, ingredientData);
        ingredientListView.setAdapter(ingredientAdapter);


        // populate list with a few items first
        // DONT NEED ANYMORE, its all in the firestore!
        for (int i = 0; i < 4; i++){
           //ingredientData.add(new StorageIngredient(descriptions.get(i), bbDates.get(i), locations.get(i), amounts.get(i), units.get(i), categories.get(i)));
            //onAddPressed(new StorageIngredient(descriptions.get(i), bbDates.get(i), locations.get(i), amounts.get(i), units.get(i), categories.get(i)));
        }

        // button to add ingredient
        final FloatingActionButton addIngredientButton = findViewById(R.id.floatingActionButton);
        addIngredientButton.setOnClickListener((v)->{
            new AddIngredientFragment().show(getSupportFragmentManager(), "ADD_INGREDIENT");
        });

        // tap an item to edit
        ingredientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                new AddIngredientFragment(ingredientData.get(i)).show(getSupportFragmentManager(), "EDIT_INGREDIENT");
            }
        });

        // long tap an item to delete
        ingredientListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                StorageIngredient ing = ingredientAdapter.getItem(i);
                deleteIngredientFromDatabase(ing.getId());
                ingredientAdapter.remove(ing);

                return true;
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
        // edit the ingredient
        IngredientStorage
                .document(ingredient.getId())
                .set(ingredient);
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