package com.CMPUT301F22T26.foodegy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.CMPUT301F22T26.foodegy.databinding.ActivityViewRecipeBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class allows the user to view the recipe, and all of the data/details that the recipe has
 */
public class ViewRecipeActivity extends AppCompatActivity implements ShowRecipeIngredientFragment.OnFragmentInteractionListener, AddIngredientToRecipeFragment.OnFragmentInteractionListener {
    private ActivityViewRecipeBinding binding;
    private FloatingActionButton cancel;
    private Button edit;
    private Button delete;
    private String title;
    private int hours;
    private int minutes;
    private int servingValue;
    private String category;
    private String imageFileName;
    private String comments;
    private String id;
    private ArrayList<RecipeIngredient> recipeIngredients;
    private RecipeIngredientListAdapter recipeIngredientListAdapter;
    private ListView ingredientsListView;


    final private DatabaseManager dbm = DatabaseManager.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        binding = ActivityViewRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        final Intent intent = this.getIntent();

        if(intent==null) {
            return;
        }
        title = intent.getStringExtra("title");
        hours = intent.getIntExtra("hours", -1);
        minutes = intent.getIntExtra("minutes", -1);
        servingValue = intent.getIntExtra("servingValue", -1);
        category = intent.getStringExtra("category");
        String imageUriString = intent.getStringExtra("imageUri");
        imageFileName = intent.getStringExtra("imageFileName");
        comments = intent.getStringExtra("comments");
        id = intent.getStringExtra("id");

        recipeIngredients = intent.getParcelableArrayListExtra("ingredients");



        binding.titleText.setText(title);
        String minutesString = minutes<10 ? "0"+minutes : ""+minutes;
        binding.timeText.setText(hours +" : " +minutesString);
        binding.servingsText.setText(String.valueOf(servingValue));
        binding.categoryText.setText(category);
        ingredientsListView = findViewById(R.id.ingredientsList);
        //recipeIngredients = new ArrayList<>();
        recipeIngredientListAdapter = new RecipeIngredientListAdapter(this, recipeIngredients);
        ingredientsListView.setAdapter(recipeIngredientListAdapter);


        // replaces the below code
        if (imageUriString != null && !"".equals(imageUriString)) {
            Uri u = Uri.parse(imageUriString);
            System.out.println(u.toString());
            Glide.with(this).load(u).into(binding.foodImage);
        }

        binding.commentText.setText(comments);
        cancel = findViewById(R.id.cancelButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        delete = findViewById(R.id.deleteButton);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // delete the recipe! from the firestore!
                dbm.deleteRecipeFromDatabase(id, imageFileName);
                finish();
            }
        });

        edit = findViewById(R.id.editButton);
        System.out.println(title);
        edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent2 = new Intent(ViewRecipeActivity.this, EditRecipeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("title",title);
                    bundle.putInt("hours",hours);
                    bundle.putInt("minutes",minutes);
                    bundle.putInt("servingValue",servingValue);
                    bundle.putString("category",category);
                    bundle.putString("imageFileName",imageFileName);
                    bundle.putString("comments",comments);
                    bundle.putString("id",id);
                    bundle.putString("imageUri", imageUriString);
                    bundle.putParcelableArrayList("ingredients",recipeIngredients);
                    //bundle.putString("imageUri", )
                    intent2.putExtras(bundle);


                    /*intent.putExtra("title",title);
                    intent.putExtra("hours",hours);
                    intent.putExtra("minutes",minutes);
                    intent.putExtra("servingValue",servingValue);
                    intent.putExtra("category",category);
                    intent.putExtra("imageFileName",imageFileName );
                    intent.putExtra("comments",comments);
                    intent.putExtra("id",id);*/
                    startActivityForResult(intent2, 10);
                }
            }
        );
        ingredientsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Pass item index to fragment
                Bundle args = new Bundle();
                args.putInt("pos", i);

                ShowRecipeIngredientFragment fragment = new ShowRecipeIngredientFragment(recipeIngredients.get(i));
                fragment.setArguments(args);
                fragment.show(getSupportFragmentManager(), "SHOW_INGREDIENT");
                return true;
            }
        });

    }
    @Override
    public void onShowRecipeIngredientOkPressed(int pos) {
        Bundle args = new Bundle();
        args.putInt("pos", pos);
        args.putString("eval", "Edit");

        AddIngredientToRecipeFragment fragment = new AddIngredientToRecipeFragment(recipeIngredients.get(pos));
        fragment.setArguments(args);
        fragment.show(getSupportFragmentManager(), "EDIT_INGREDIENT");
    }

    @Override
    public void onShowRecipeIngredientDeletePressed(int pos) {
        recipeIngredients.remove(pos);
        recipeIngredientListAdapter.notifyDataSetChanged();

    }

    @Override
    public void onOkPressed(RecipeIngredient newIngredient) {
        recipeIngredients.add(newIngredient);
        recipeIngredientListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEditOkPressed(RecipeIngredient newIngredient, int i) {
        recipeIngredients.set(i, newIngredient);
        dbm.editRecipeIngredient(id, recipeIngredients);
        recipeIngredientListAdapter.notifyDataSetChanged();
    }

    /**
     * When done editing, close this activity as well
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }
}