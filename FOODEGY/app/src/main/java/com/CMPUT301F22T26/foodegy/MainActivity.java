package com.CMPUT301F22T26.foodegy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AddIngredientFragment.OnFragmentInteractionListener {
    ListView ingredientListView;
    ArrayAdapter<StorageIngredient> ingredientAdapter;
    ArrayList<StorageIngredient> ingredientData;

    List<String> descriptions = Arrays.asList("Sausage", "Mushroom", "Onion", "Pepperoni");
    List<String> bbDates = Arrays.asList("2022-09-10", "2023-01-27", "2022-11-05", "2022-10-01");
    List<String> locations = Arrays.asList("Fridge", "Fridge", "Pantry", "Fridge");
    List<Integer> amounts = Arrays.asList(12, 24, 4, 1);
    List<Integer> units = Arrays.asList(5, 2, 1, 3);
    List<String> categories = Arrays.asList("Meat", "Vegetable", "Vegetable", "Meat");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ingredientData = new ArrayList<StorageIngredient>();
        ingredientListView = findViewById(R.id.ingredientList);

        for (int i = 0; i < 4; i++){

           ingredientData.add(new StorageIngredient(descriptions.get(i), bbDates.get(i), locations.get(i), amounts.get(i), units.get(i), categories.get(i)));

        }
        ingredientAdapter = new IngredientList(this, ingredientData);
        ingredientListView.setAdapter(ingredientAdapter);

        final FloatingActionButton addIngredientButton = findViewById(R.id.floatingActionButton);
        addIngredientButton.setOnClickListener((v)->{
            new AddIngredientFragment().show(getSupportFragmentManager(), "ADD_INGREDIENT");
        });


    };



    @Override
    public void onOkPressed(StorageIngredient newIngredient){
        ingredientAdapter.add(newIngredient);
    }
}