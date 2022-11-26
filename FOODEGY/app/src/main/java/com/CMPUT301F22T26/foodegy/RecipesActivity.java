package com.CMPUT301F22T26.foodegy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.CMPUT301F22T26.foodegy.databinding.ActivityRecipesBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Activity for user interacting with recipes.
 * Shows the list of Recipes and lets the user add or view a Recipe
 */
public class RecipesActivity extends AppCompatActivity {
    private FloatingActionButton addbutton;
    private ActivityRecipesBinding binding;
    private ArrayList<Recipe> listViewRecipe;
    private RecipeAdapter listadapter;
    private Spinner sortingSpinner;
    private String sortingAttribute = "title_insensitive";

    // connect to the firebase & provide a test id
    final private DatabaseManager dbm = DatabaseManager.getInstance();
    final private CollectionReference RecipesCollection = dbm.getRecipesCollection();
    private Query sortedRecipes = RecipesCollection.orderBy(sortingAttribute);

    // the navbar for navigating between activities
    private NavigationBarView bottomNavBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);


        binding = ActivityRecipesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bottomNavBar = (NavigationBarView) findViewById(R.id.bottom_nav);
        bottomNavBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            // see which navbar item has been clicked
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ingredients:
                        startActivity(new Intent(getBaseContext(), IngredientsActivity.class));
                        break;
                    case R.id.meal_plan:
                        startActivity(new Intent(getBaseContext(), MealPlanActivity.class));
                        break;
                    case R.id.shopping_cart:
                        startActivity(new Intent(getBaseContext(), ShoppingListActivity.class));
                        break;
                    case R.id.recipes:
                        break;

                }

                return false;
            }
        });

        // render the data
        listViewRecipe = new ArrayList<Recipe>();
        listadapter = new RecipeAdapter(RecipesActivity.this,listViewRecipe);
        binding.foodList.setAdapter(listadapter);
        binding.foodList.setClickable(true);

        binding.foodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(RecipesActivity.this, ViewRecipeActivity.class);
                Recipe r = listViewRecipe.get(i);
                intent.putExtra("title", r.getTitle());
                intent.putExtra("hours", r.getHours());
                intent.putExtra("minutes", r.getMinutes());
                intent.putExtra("servingValue", r.getServingValue());
                intent.putExtra("category", r.getCategory());
                intent.putExtra("imageFileName", r.getImageFileName());
                intent.putExtra("comments", r.getComments());
                intent.putExtra("id", r.getId());
                intent.putParcelableArrayListExtra("ingredients",r.getIngredients());
                /*
                   leaving the line below as a commented line since we are not dealing with viewing list of ingredients.
                       */
                //intent.putExtra("ingredients",listViewRecipe.get(position).getIngredients());
                startActivity(intent);
            }
        });
        addbutton = findViewById(R.id.addRecipe);
        addbutton.setOnClickListener((v) -> {
            Intent intent = new Intent(RecipesActivity.this, AddRecipeActivity.class);
            startActivity(intent);
        });


        // handle the sorting of the recipe list
        sortingSpinner = findViewById(R.id.sortRecipesSpinner);
        sortingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // figure out which item was picked
                String[] sortingArray = getResources().getStringArray(R.array.sort_recipes);

                // the names in the list are different from the attribute names, switch them
                if ("Title".equals(sortingArray[i])) {
                    sortingAttribute = "title_insensitive";
                }
                else if ("Prep Time".equals(sortingArray[i])) {
                    // HANDLE THIS BELOW
                    sortingAttribute = "PREP TIME";
                }
                else if ("Servings".equals(sortingArray[i])) {
                    sortingAttribute = "servingValue";
                }
                else if ("Category".equals(sortingArray[i])) {
                    sortingAttribute = "category";
                }

                // prep time is stored in two attributes, hours & minutes. we need to sort by both
                if ("PREP TIME".equals(sortingAttribute)) {
                    sortedRecipes = RecipesCollection
                            .orderBy("hours", Query.Direction.ASCENDING)
                            .orderBy("minutes", Query.Direction.ASCENDING);
                }
                else {
                    sortedRecipes
                            .orderBy(sortingAttribute, Query.Direction.ASCENDING);
                }

                // THIS IS THE LISTENER FOR THE RECIPE DATABASE CHANGING
                sortedRecipes.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        reloadRecipes(value);
                        listadapter.notifyDataSetChanged();
                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }


    /**
     * Given a query snapshot, reloads & repopulates the list of recipes.
     *  Called when a recipe is added, deleted, edited, or the list is sorted
     * @param snapshot
     *  The snapshot from the query of the (sorted) recipes collection
     */
    public void reloadRecipes(QuerySnapshot snapshot) {
        listViewRecipe.clear();
        for (QueryDocumentSnapshot doc : snapshot) {
            // runs through each recipe
            Map<String, Object> data = doc.getData();
            // get the ingredients
            ArrayList<Map<String, Object>> documentIngredients = (ArrayList<Map<String, Object>>) data.get("ingredients");

            // loop through ingredients from the firestore and put them in ings
            ArrayList<RecipeIngredient> ings = new ArrayList<>();
            for(Map<String, Object> ingredient : documentIngredients) {
                String unit = (String)ingredient.get("unit");
                String amount = (String)ingredient.get("amount");
                String desc = (String)ingredient.get("description");
                String category = (String)ingredient.get("category");
                ings.add(new RecipeIngredient(desc, category, amount, unit));
            }

            // get other data fields
            String category = (String)data.get("category");
            String comments = (String)data.get("comments");
            int hours = doc.getLong("hours").intValue();
            int minutes = doc.getLong("minutes").intValue();
            String imageFileName = (String)data.get("imageFileName");
            int servingValue = doc.getLong("servingValue").intValue();
            String title = (String)data.get("title");

            Recipe r = new Recipe(
                    title, hours, minutes, servingValue, category,
                    imageFileName, comments, ings
            );
            r.setId(doc.getId());
            // download the image!!!
            if (imageFileName != null && !"".equals(imageFileName)) {
                Task t = dbm.getUserFilesRef().child(imageFileName).getDownloadUrl();
                t.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("RecipeAdapter", "Got download URL for " + uri.toString());
                        r.setRecipeImage(uri);
                        listadapter.notifyDataSetChanged();
                    }
                });
            }
            listViewRecipe.add(r);
            listadapter.notifyDataSetChanged();
        }

    }

    /**
     * Returns the ArrayList of recipes, used for testing purposes
     * @return
     *  the list of current recipes
     */
    public ArrayList<Recipe> getListViewRecipe() {
        return listViewRecipe;
    }
}