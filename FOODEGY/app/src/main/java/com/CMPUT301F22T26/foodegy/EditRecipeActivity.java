package com.CMPUT301F22T26.foodegy;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.CMPUT301F22T26.foodegy.databinding.ActivityEditRecipeBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Activity to handle adding a recipe
 */
public class EditRecipeActivity extends AppCompatActivity implements AddIngredientToRecipeFragment.OnFragmentInteractionListener, ShowRecipeIngredientFragment.OnFragmentInteractionListener{
    private EditText titleText;
    private EditText hourText;
    private EditText minuteText;
    private EditText servingsText;
    private EditText commentText;
    private ImageView activityBackground;
    private Button imageButton;
    private Uri selectedImage;

    private Button ingredientsButton;
    private Button submitButton;
    private Button cancelButton;

    private Spinner categorySpinner;
    ActivityEditRecipeBinding binding;

    // Ingredients list to be used by quick add ingredients and added to recipe
    public static ArrayList<RecipeIngredient> ingredientsList;
    private ListView ingredientsListView;
    private RecipeIngredientListAdapter ingredientsAdapter;

    // database things
    private String android_id = "TEST_ID";
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference RecipesCollection = firestore.collection("users")
            .document(android_id).collection("Recipes");
    private StorageReference userFilesRef = FirebaseStorage.getInstance().getReference().child(android_id);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);
        binding = ActivityEditRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activityBackground = findViewById(R.id.add_recipe_background);

        ingredientsListView = findViewById(R.id.ingredients_listview);
        ingredientsList = new ArrayList<>();
        ingredientsAdapter = new RecipeIngredientListAdapter(this, ingredientsList);
        ingredientsListView.setAdapter(ingredientsAdapter);

        titleText = findViewById(R.id.title_text);
        hourText = findViewById(R.id.hour_text);
        minuteText = findViewById(R.id.minute_text);
        servingsText = findViewById(R.id.servings_text);
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
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String title = bundle.getString("title");
        String hours = bundle.getString("hours");
        String minutes = bundle.getString("minutes");
        String servingValue = bundle.getString("servingValue");
        String category = bundle.getString("category");
        String fileName = bundle.getString("imageFileName");
        String comments = bundle.getString("comments");
        String currentid = bundle.getString("id");
        System.out.println(title);
        titleText.setText(title);
        hourText.setText(hours);
        minuteText.setText(minutes);
        servingsText.setText(servingValue);
       // categorySpinner.getSelectedItem().toString();
        String[] categories = getResources().getStringArray(R.array.test_array);
        int i = 0;
        while (!categories[i].equals(category)){
            i++;
        }

        categorySpinner.setSelection(i);
        commentText.setText(comments);
        if (fileName != null && !"".equals(fileName)) {
            Context context = this;
            userFilesRef.child(fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Log.d("RecipeAdapter", "Got download URL for " + uri.toString());
                    String url = uri.toString();
                    Glide.with(context).load(url).into(activityBackground);
                }
            });
        }





        // (quick)add an ingredient to the recipe
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

        // choose an image from gallery
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });

        // submit button to create & add the recipe
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleText.getText().toString();
                String hour = hourText.getText().toString();
                String minute = minuteText.getText().toString();
                String servings = servingsText.getText().toString();
                String category = categorySpinner.getSelectedItem().toString();
                String comments = commentText.getText().toString();
                if (servingsText.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Servings cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                // upload image to firebase storage
                String imageFilename = System.currentTimeMillis() +"."+getFileExtension(selectedImage);
                Recipe recipe = new Recipe(title, hour, minute, servings, category,
                        imageFilename, comments, ingredientsList);
                addRecipeToDatabase(recipe);
                finish();
            }
        });

        // close the activity when cancel pressed
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        // long tap on an ingredient to show more details
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

    /**
     * Adds a recipe to the Firebase
     * @param recipe
     *  The recipe to add
     */
    public void addRecipeToDatabase(Recipe recipe) {
        String imageFilename = recipe.getImageFileName();
        StorageReference fileRef = userFilesRef.child(imageFilename);
        // Upload the image to firebase storage
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

        // Add recipe to firestore
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
    }


}