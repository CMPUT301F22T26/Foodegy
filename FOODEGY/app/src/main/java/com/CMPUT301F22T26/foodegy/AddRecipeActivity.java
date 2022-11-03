package com.CMPUT301F22T26.foodegy;

import static java.security.AccessController.getContext;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class AddRecipeActivity extends AppCompatActivity implements AddIngredientToRecipeFragment.OnFragmentInteractionListener, ShowRecipeIngredientFragment.OnFragmentInteractionListener {

    EditText titleText;
    EditText hourText;
    EditText minuteText;
    EditText servingsText;
    EditText amountText;
    EditText commentText;
    ImageView activityBackground;

    Button imageButton;
    private Uri selectedImage;

    Button ingredientsButton;
    Button submitButton;
    Button cancelButton;

    Spinner categorySpinner;

    // Ingredients list to be used by quick add ingredients and added to recipe
    public static ArrayList<RecipeIngredient> ingredientsList;
    ListView ingredientsListView;
    RecipeIngredientListAdapter ingredientsAdapter;

    private String android_id = "TEST_ID";
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference RecipesCollection = firestore.collection("users")
            .document(android_id).collection("Recipes");
    private StorageReference userFilesRef = FirebaseStorage.getInstance().getReference().child(android_id);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipe_activity);

        activityBackground = findViewById(R.id.add_recipe_background);

        ingredientsListView = findViewById(R.id.ingredients_listview);
        ingredientsList = new ArrayList<>();
        ingredientsAdapter = new RecipeIngredientListAdapter(this, ingredientsList);
        ingredientsListView.setAdapter(ingredientsAdapter);

        titleText = findViewById(R.id.title_text);
        hourText = findViewById(R.id.hour_text);
        minuteText = findViewById(R.id.minute_text);
        servingsText = findViewById(R.id.servings_text);
        amountText = findViewById(R.id.amount_text);
        commentText = findViewById(R.id.comment_text);

        imageButton = findViewById(R.id.image_button);


        ingredientsButton = findViewById(R.id.ingredient_button);
        submitButton = findViewById(R.id.submit_button);
        cancelButton = findViewById(R.id.cancel_button);

        // Category spinner
        categorySpinner = findViewById(R.id.category_spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.test_array, R.layout.category_spinner);
        // Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(R.layout.category_dropdown_items);
        // Apply the adapter to the spinner
        categorySpinner.setAdapter(spinnerAdapter);
        // View Listeners -----------------------

        ingredientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Pass item index to fragment
                Bundle args = new Bundle();
                args.putString("eval", "Add");

                AddIngredientToRecipeFragment fragment = new AddIngredientToRecipeFragment();
                fragment.setArguments(args);
                fragment.show(getSupportFragmentManager(), "ADD_INGREDIENT");
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleText.getText().toString();
                String hour = hourText.getText().toString();
                String minute = minuteText.getText().toString();
                String servings = servingsText.getText().toString();
                String category = categorySpinner.getSelectedItem().toString();
                String amount = amountText.getText().toString();
                String comments = commentText.getText().toString();

                // upload image to firebase storage
                String imageFilename = System.currentTimeMillis() +"."+getFileExtension(selectedImage);
                StorageReference fileRef = userFilesRef.child(imageFilename);
                fileRef.putFile(selectedImage)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.d("AddRecipeActivity", "Successfully uploaded image "+imageFilename);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("AddRecipeActivity", "Failed to upload image, "+e);
                            }
                        });

                Recipe recipe = new Recipe(title, hour, minute, servings, category, amount,
                        imageFilename, comments, ingredientsList);
                // Add recipe to firebase
                RecipesCollection.add(recipe)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("AddRecipe", "Successfully added recipe "+documentReference.getId());
                                recipe.setId(documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("AddRecipe", "Could not add recipe, "+e);
                            }
                        });
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ingredientsList.clear(); // remove all items from ingredients list

                //Intent intent = new Intent( AddRecipeActivity.this, RecipesActivity.class);
                //startActivity(intent);
                finish();
            }
        });


        ingredientsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Pass item index to fragment
                Bundle args = new Bundle();
                args.putInt("pos", i);

                ShowRecipeIngredientFragment fragment = new ShowRecipeIngredientFragment();
                fragment.setArguments(args);
                fragment.show(getSupportFragmentManager(), "SHOW_INGREDIENT");
                return true;
            }
        });
    }

    /**
     * Opens the user's gallery where they can choose an image for the recipe
     */
    private void imageChooser()
    {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        launchSomeActivity.launch(i);
    }

    ActivityResultLauncher<Intent> launchSomeActivity
            = registerForActivityResult(
            new ActivityResultContracts
                    .StartActivityForResult(),
            result -> {
                if (result.getResultCode()
                        == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    // do your operation from here....
                    if (data != null
                            && data.getData() != null) {
                        selectedImage = data.getData();
                        Bitmap selectedImageBitmap = null;
                        try {
                            selectedImageBitmap
                                    = MediaStore.Images.Media.getBitmap(
                                    this.getContentResolver(),
                                    selectedImage);


                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }

                        activityBackground.setImageBitmap(selectedImageBitmap);
                    }
                }
            });


    // Adds new ingredient when ok is pressed from AddRecipeFragment
    @Override
    public void onOkPressed(RecipeIngredient newIngredient) {
        ingredientsList.add(newIngredient);
        ingredientsAdapter.notifyDataSetChanged();
    }

    // Edits ingredient when ok is pressed from AddRecipeFragment when accessed from ShowRecipeIngredientsFragment
    @Override
    public void onEditOkPressed(RecipeIngredient newIngredient, int i) {
        ingredientsList.set(i, newIngredient);
        ingredientsAdapter.notifyDataSetChanged();
    }

    // Show details of ingredient when long pressed
    @Override
    public void onShowRecipeIngredientOkPressed(int pos) {
        Bundle args = new Bundle();
        args.putInt("pos", pos);
        args.putString("eval", "Edit");

        AddIngredientToRecipeFragment fragment = new AddIngredientToRecipeFragment();
        fragment.setArguments(args);
        fragment.show(getSupportFragmentManager(), "EDIT_INGREDIENT");
    }

    // Deletes ingredient when delete is pressed from ShowRecipeIngredientFragment
    @Override
    public void onShowRecipeIngredientDeletePressed(int pos) {
        ingredientsList.remove(pos);
        ingredientsAdapter.notifyDataSetChanged();
    }

    /**
     * returns the file extension of a file
     * @param uri
     *  the uri of the file
     * @return
     *  file extension as a string
     */
    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
}
