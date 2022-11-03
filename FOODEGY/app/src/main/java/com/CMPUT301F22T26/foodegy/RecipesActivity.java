package com.CMPUT301F22T26.foodegy;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.CMPUT301F22T26.foodegy.databinding.ActivityRecipesBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;

public class RecipesActivity extends AppCompatActivity {
    private Button sortbutton;
    private Button categorybutton;
    private FloatingActionButton addbutton;
    private Button viewmorebutton;
    private ActivityRecipesBinding binding;
    private ArrayList<Recipe> listViewRecipe;

    private String android_id = "TEST_ID";
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference RecipesCollection = firestore.collection("users")
            .document(android_id).collection("Recipes");

    private StorageReference userFilesRef = FirebaseStorage.getInstance().getReference().child(android_id);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

/**
 * The following lines of code is used to check if the listView displays the contents of the Recipe class with appropriate image.
 */
        RecipeIngredient cinnamon = new RecipeIngredient("The spice is used to enhance flavours","indian spices","5","2");
        ArrayList<RecipeIngredient> recipeIngredients = new ArrayList<RecipeIngredient>();
        recipeIngredients.add(cinnamon);
        binding = ActivityRecipesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listViewRecipe = new ArrayList<Recipe>();
        RecipeAdapter listadapter = new RecipeAdapter(RecipesActivity.this,listViewRecipe);
        binding.foodList.setAdapter(listadapter);
        binding.foodList.setClickable(true);

        binding.foodList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RecipesActivity.this, ViewRecipeActivity.class);
                intent.putExtra("title", listViewRecipe.get(position).getTitle());
                intent.putExtra("hours",listViewRecipe.get(position).getHours());
                intent.putExtra("minutes",listViewRecipe.get(position).getMinutes());
                intent.putExtra("servingValue",listViewRecipe.get(position).getServingValue());
                intent.putExtra("category",listViewRecipe.get(position).getCategory());
                intent.putExtra("amount",listViewRecipe.get(position).getAmount());
                intent.putExtra("imageFileName", listViewRecipe.get(position).getImageFileName());
                intent.putExtra("comments",listViewRecipe.get(position).getComments());
                intent.putExtra("id", listViewRecipe.get(position).getId());
                /*
                   leaving the line below as a commented line since we are not dealing with viewing list of ingredients.
                       */
                //intent.putExtra("ingredients",listViewRecipe.get(position).getIngredients());


                startActivity(intent);
                return true;
            }

        });
        addbutton = findViewById(R.id.addRecipe);
        addbutton.setOnClickListener((v) -> {
            Intent intent = new Intent(RecipesActivity.this, AddRecipeActivity.class);
            startActivity(intent);
        });

        // when a recipe is added, reload the data
        RecipesCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                listViewRecipe.clear();
                for (QueryDocumentSnapshot doc : value) {
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
                    String amount = (String)data.get("amount");
                    String category = (String)data.get("category");
                    String comments = (String)data.get("comments");
                    String hours = (String)data.get("hours");
                    String minutes = (String)data.get("minutes");
                    String imageFileName = (String)data.get("imageFileName");
                    String servingValue = (String)data.get("servingValue");
                    String title = (String)data.get("title");

                    Recipe r = new Recipe(
                            title, hours, minutes, servingValue, category, amount,
                            imageFileName, comments, ings
                    );
                    r.setId(doc.getId());
                    listViewRecipe.add(r);
                }
                listadapter.notifyDataSetChanged();
            }
        });
    }
}