package com.CMPUT301F22T26.foodegy;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RecipesActivity extends AppCompatActivity {
    Button sortbutton;
    Button categorybutton;
    FloatingActionButton addbutton;
    Button viewmorebutton;
    ActivityRecipesBinding binding;
    ArrayList<Recipe> listViewRecipe;

    private String android_id = "TEST_ID";
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference RecipesCollection = firestore.collection("users")
            .document(android_id).collection("Recipes");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);



        RecipesCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                // populate list view (which should maybe be recycler view !)
                // - need RecipeAdapter class
            }
        });
/**
 * The following lines of code is used to check if the listView displays the contents of the Recipe class with appropriate image.
 */
        RecipeIngredient cinnamon = new RecipeIngredient("The spice is used to enhance flavours","indian spices","5","2");
        ArrayList<RecipeIngredient> recipeIngredients = new ArrayList<RecipeIngredient>();
        recipeIngredients.add(cinnamon);
        binding = ActivityRecipesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int imageid1 = R.drawable.butterchicken;
        int imageid2 = R.drawable.poutine;
        Recipe butterchicken = new Recipe("Butter Chicken","2","15","3","Indian","15",imageid1,"A tasty dish from North India",recipeIngredients);
        Recipe poutine = new Recipe("Poutine","1","4","2","Canadian","20",imageid2,"A tasty dish from Quebec",recipeIngredients);
        listViewRecipe = new ArrayList<Recipe>();
        listViewRecipe.add(butterchicken);
        listViewRecipe.add(poutine);
        RecipeAdapter listadaper = new RecipeAdapter(RecipesActivity.this,listViewRecipe);
        binding.foodList.setAdapter(listadaper);
        binding.foodList.setClickable(true);

        binding.foodList.setOnItemLongClickListener
                (new AdapterView.OnItemLongClickListener()
                 {
                     @Override
                     public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                         Intent intent = new Intent(RecipesActivity.this, ViewRecipeActivity.class);
                         intent.putExtra("title", listViewRecipe.get(position).getTitle());
                         intent.putExtra("hours",listViewRecipe.get(position).getHours());
                         intent.putExtra("minutes",listViewRecipe.get(position).getMinutes());
                         intent.putExtra("servingValue",listViewRecipe.get(position).getServingValue());
                         intent.putExtra("category",listViewRecipe.get(position).getCategory());
                         intent.putExtra("amount",listViewRecipe.get(position).getAmount());
                         intent.putExtra("imageidint",listViewRecipe.get(position).getImageId());
                        /*
                        leaving the line below as a commented line since the image URI needs to be taken from Firebase
                         */
                         // intent.putExtra("imageiduri",listViewRecipe.get(position).getRecipeImage());
                         intent.putExtra("comments",listViewRecipe.get(position).getComments());
                        /*
                        leaving the line below as a commented line since we are not dealing with viewing list of ingredients.
                         */
                         //intent.putExtra("ingredients",listViewRecipe.get(position).getIngredients());


                         startActivity(intent);
                         return true;
                     }
                 }


                );

        addbutton = findViewById(R.id.addRecipe);
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipesActivity.this, AddRecipeActivity.class);
                startActivity(intent);
            }
        });

    }

}