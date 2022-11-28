package com.CMPUT301F22T26.foodegy;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.Activity;
import android.content.ActivityNotFoundException;
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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;

import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Activity to handle adding a recipe
 */
public class AddRecipeActivity extends AppCompatActivity implements AddIngredientToRecipeFragment.OnFragmentInteractionListener, ShowRecipeIngredientFragment.OnFragmentInteractionListener {
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

    // Ingredients list to be used by quick add ingredients and added to recipe
    private ArrayList<RecipeIngredient> ingredientsList;
    private ListView ingredientsListView;
    private RecipeIngredientListAdapter ingredientsAdapter;

    // database things
    final private DatabaseManager dbm = DatabaseManager.getInstance();
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Uri imageUri;

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

        // take photo
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

        }

        );

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

                // perform input validation
                if (title.length() == 0) {
                    Toast.makeText(context, "Title cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                int servings, hour, minute;
                // validate servings
                try {
                    servings = Integer.parseInt(servingsString);
                }
                catch (IllegalArgumentException e) {
                    Toast.makeText(getApplicationContext(), "Invalid input for servings", Toast.LENGTH_LONG).show();
                    return;
                }
                // validate hour
                try {
                    hour = Integer.parseInt(hourString);
                }
                catch (IllegalArgumentException e) {
                    Toast.makeText(context, "Invalid input for hour", Toast.LENGTH_LONG).show();
                    return;
                }
                // validate minute
                try {
                    minute = Integer.parseInt(minuteString);
                }
                catch (IllegalArgumentException e) {
                    Toast.makeText(context, "Invalid input for minutes", Toast.LENGTH_LONG).show();
                    return;
                }

                // if the user enters 61 minutes, we want that as 1 hr & 1 min
                hour += minute / 60;  // int division, so if minute < 60 it = 0
                minute = minute % 60;
                // upload image to firebase storage
                String imageFilename = null;
                if (selectedImage != null) {
                    imageFilename = System.currentTimeMillis() + "." + getFileExtension(selectedImage);
                }

                Recipe recipe = new Recipe(title, hour, minute, servings, category,
                        imageFilename, comments, ingredientsList);
                dbm.addRecipeToDatabase(recipe, selectedImage);
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
     * When the camera app returns, get the image URI
     */
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
        setListViewHeightBasedOnChildren(ingredientsListView);
        ;
    }

    /**
     * Edits ingredient when ok is pressed from AddRecipeFragment when accessed from ShowRecipeIngredientsFragment
     */
    @Override
    public void onEditOkPressed(RecipeIngredient newIngredient, int i) {
        ingredientsList.set(i, newIngredient);
        ingredientsAdapter.notifyDataSetChanged();
    }

    /**
     * Show details of ingredient when long pressed
     * @param pos
     */
    @Override
    public void onShowRecipeIngredientOkPressed(int pos) {
        Bundle args = new Bundle();
        args.putInt("pos", pos);
        args.putString("eval", "Edit");

        AddIngredientToRecipeFragment fragment = new AddIngredientToRecipeFragment(ingredientsList.get(pos));
        fragment.setArguments(args);
        fragment.show(getSupportFragmentManager(), "EDIT_INGREDIENT");
    }

    /**
     * Deletes ingredient when delete is pressed from ShowRecipeIngredientFragment
     * @param pos
     */
    @Override
    public void onShowRecipeIngredientDeletePressed(int pos) {
        ingredientsList.remove(pos);
        ingredientsAdapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(ingredientsListView);
        ;
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
