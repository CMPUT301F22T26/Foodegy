package com.CMPUT301F22T26.foodegy;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import java.io.FileNotFoundException;
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
    private Uri imageUri;

    private Spinner categorySpinner;
    private ActivityEditRecipeBinding binding;

    // Ingredients list to be used by quick add ingredients and added to recipe
    private ArrayList<RecipeIngredient> ingredientsList;
    private ListView ingredientsListView;
    private RecipeIngredientListAdapter ingredientsAdapter;

    // database things
    private DatabaseManager dbm = DatabaseManager.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);
        binding = ActivityEditRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activityBackground = findViewById(R.id.add_recipe_background);



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
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.recipe_categories_array, R.layout.category_spinner);
        // Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(R.layout.category_dropdown_items);
        // Apply the adapter to the spinner
        categorySpinner.setAdapter(spinnerAdapter);
        // View Listeners -----------------------
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String title = bundle.getString("title");
        String hours = String.valueOf(bundle.getInt("hours",-1));
        String minutes = String.valueOf(bundle.getInt("minutes",-1));
        String servingValue = String.valueOf(bundle.getInt("servingValue",-1));
        String category = bundle.getString("category");
        String fileName = bundle.getString("imageFileName");
        String comments = bundle.getString("comments");
        String currentid = bundle.getString("id");
        String imageUriString = bundle.getString("imageUri");

        ingredientsList = bundle.getParcelableArrayList("ingredients");
        ingredientsListView = findViewById(R.id.ingredients_listview);
        ingredientsAdapter = new RecipeIngredientListAdapter(this, ingredientsList);
        ingredientsListView.setAdapter(ingredientsAdapter);
        ingredientsAdapter.notifyDataSetChanged();
        
        titleText.setText(title);
        hourText.setText(hours);
        minuteText.setText(minutes);
        servingsText.setText(servingValue);
       // categorySpinner.getSelectedItem().toString();
        String[] categories = getResources().getStringArray(R.array.recipe_categories_array);
        int i = 0;
        while (!categories[i].equals(category)){
            System.out.println(i + categories[i] + category);
            i++;
        }

        categorySpinner.setSelection(i);
        commentText.setText(comments);
        if (!"".equals(imageUriString) && imageUriString != null) {
            Uri u = Uri.parse(imageUriString);
            Glide.with(getApplicationContext()).load(u).into(activityBackground);
        }

//        if (fileName != null && !"".equals(fileName)) {
//            Context context = this;
//            userFilesRef.child(fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                @Override
//                public void onSuccess(Uri uri) {
//                    Log.d("RecipeAdapter", "Got download URL for " + uri.toString());
//                    String url = uri.toString();
//                    Glide.with(context).load(url).into(activityBackground);
//                }
//            });
//        }

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
                String fileName = "new-photo-name.jpg";
                // Create parameters for Intent with filename
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, fileName);
                values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");
                imageUri =
                        getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                values);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, 1231);
            }

        });

        // submit button to create & add the recipe
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleText.getText().toString();
                String hourString = hourText.getText().toString();
                String minuteString = minuteText.getText().toString();
                String servingsString = servingsText.getText().toString();
                String category = categorySpinner.getSelectedItem().toString();
                String comments = commentText.getText().toString();
                Context context = getApplicationContext();

                int servings, hour, minute;
                // validate servings
                try {
                    servings = Integer.parseInt(servingsString);
                }
                catch (IllegalArgumentException e) {
                    Toast.makeText(context, "Invalid value for servings", Toast.LENGTH_LONG).show();
                    return;
                }
                // validate hour
                try {
                    hour = Integer.parseInt(hourString);
                }
                catch (IllegalArgumentException e) {
                    Toast.makeText(context, "Invalid value for hour", Toast.LENGTH_LONG).show();
                    return;
                }
                // validate minute
                try {
                    minute = Integer.parseInt(minuteString);
                }
                catch (IllegalArgumentException e) {
                    Toast.makeText(context, "Invalid value for minutes", Toast.LENGTH_LONG).show();
                    return;
                }
                // upload image to firebase storage
                // if the user enters 61 minutes, we want that as 1 hr & 1 min
                hour += minute / 60;  // int division, so if minute < 60 it = 0
                minute = minute % 60;

                Recipe recipe;
                if (selectedImage != null) {
                    // selected an image, use that
                    String imageFilename = System.currentTimeMillis() + "."+getFileExtension(selectedImage);
                    recipe = new Recipe(title, hour, minute, servings, category,
                            imageFilename, comments, ingredientsList);
                    dbm.deleteRecipeFromDatabase(currentid, fileName);
                    dbm.addRecipeToDatabase(recipe, selectedImage);
                }
                else {
                    // user did not select an image, use the same one already there
                    System.out.println("NO IMAGE SLEECTEDD!!");
                    recipe = new Recipe(title, hour, minute, servings, category,
                            fileName, comments, ingredientsList);
                    dbm.deleteRecipeFromDatabase(currentid, "");
                    dbm.addRecipeToDatabase(recipe, null);
                }


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

                ShowRecipeIngredientFragment fragment = new ShowRecipeIngredientFragment(ingredientsList.get(i));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if (requestCode == 1231 && resultCode == Activity.RESULT_OK) {
        if (true) {
            try {
                ContentResolver cr = getContentResolver();
                try {
                    // Creating a Bitmap with the image Captured
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(cr, imageUri);
                    selectedImage = imageUri;
                    // Setting the bitmap as the image of the
                    activityBackground.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IllegalArgumentException e) {
                if (e.getMessage() != null)
                    Log.e("Exception", e.getMessage());
                else
                    Log.e("Exception", "Exception");
                e.printStackTrace();
            }
        }
    }


    // Adds new ingredient when ok is pressed from AddRecipeFragment
    @Override
    public void onOkPressed(RecipeIngredient newIngredient) {
        ingredientsList.add(newIngredient);
        ingredientsAdapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(ingredientsListView);
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

        AddIngredientToRecipeFragment fragment = new AddIngredientToRecipeFragment(ingredientsList.get(pos));
        fragment.setArguments(args);
        fragment.show(getSupportFragmentManager(), "EDIT_INGREDIENT");
    }

    // Deletes ingredient when delete is pressed from ShowRecipeIngredientFragment
    @Override
    public void onShowRecipeIngredientDeletePressed(int pos) {
        ingredientsList.remove(pos);
        ingredientsAdapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(ingredientsListView);
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

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}