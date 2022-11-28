package com.CMPUT301F22T26.foodegy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.test.espresso.action.ViewActions;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

public class RecipesActivityTest {
    private Solo solo;
    private RecipesActivity activity;
    private MainActivity mainActivity;
    private DatabaseManager dbms;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);
    public Recipe recipe1;
    public Recipe recipe2;
    public Recipe mockRecipeView;

    final private String android_id = "TEST_ID";
    final private CollectionReference RecipesCollection = FirebaseFirestore.getInstance()
            .collection("users").document(android_id).collection("Recipes");
    private StorageReference userFilesRef = FirebaseStorage.getInstance().getReference().child(android_id);

    @Before
    public void setup() throws Exception {
        mainActivity = rule.getActivity();
        dbms = DatabaseManager.getInstance();
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), mainActivity);

        ArrayList<RecipeIngredient> ingredients1 = new ArrayList<RecipeIngredient>();
        ingredients1.add(new RecipeIngredient("ramen", "Grain", "1", "1"));
        ingredients1.add(new RecipeIngredient("chili paste", "Condiment", "1", "5"));

        recipe1 = new Recipe("Noodles", 0, 15, 2, "Lunch",
                "", "Quick & easy noodle recipe", ingredients1);

        ArrayList<RecipeIngredient> ingredients2 = new ArrayList<RecipeIngredient>();
        ingredients2.add(new RecipeIngredient("white bread", "Grain", "3", "4"));
        ingredients2.add(new RecipeIngredient("Large white egg", "Egg", "1", "2"));

        recipe2 = new Recipe("French toast", 0, 20, 3, "Breakfast",
                "", "Sweeeet! french toast!for breakfast!!", ingredients2);

        ArrayList<RecipeIngredient> ingredients3 = new ArrayList<RecipeIngredient>();
        ingredients3.add(new RecipeIngredient("Dosa Batter","Grain","1","1"));
        mockRecipeView = new Recipe("Dosa",0,5,3,"Breakfast","","Mock Recipe",ingredients3);
        // The AddRecipeToDatabase shows a problem since it also expects the Image URI as a parameter.

        dbms.addRecipeToDatabase(recipe1, recipe1.getRecipeImage());
        dbms.addRecipeToDatabase(recipe2,recipe2.getRecipeImage());
        dbms.addRecipeToDatabase(mockRecipeView,mockRecipeView.getRecipeImage());

        solo.clickOnButton("RECIPES");
        solo.waitForActivity("RecipesActivity",3000);
        solo.assertCurrentActivity("This must be Recipes Activity",RecipesActivity.class);

        activity = (RecipesActivity) solo.getCurrentActivity();

    }
    @After
    public void takedown() {
        dbms.deleteRecipeFromDatabase(recipe1.getId(), recipe1.getImageFileName());
        dbms.deleteRecipeFromDatabase(recipe2.getId(), recipe2.getImageFileName());
        dbms.deleteRecipeFromDatabase(mockRecipeView.getId(), mockRecipeView.getImageFileName());
    }
    /**
     * Test adding a Recipe with all valid parameters
     */
    @Test
    public void testValidAdd() throws Exception {
        // initialize RecipesActivity
        solo.assertCurrentActivity("Must be RecipesActivity",RecipesActivity.class);
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.addRecipe));

        solo.waitForActivity("AddRecipeActivity", 2000);
        solo.assertCurrentActivity("Want to be in AddRecipeActivity", AddRecipeActivity.class);
        solo.enterText((EditText)solo.getView(R.id.title_text), "Noodles");
        solo.enterText((EditText)solo.getView(R.id.hour_text), "0");
        solo.enterText((EditText)solo.getView(R.id.minute_text), "15");
        solo.enterText((EditText)solo.getView(R.id.servings_text), "2");
        solo.pressSpinnerItem(0, 1); // select Noodles as Lunch
        solo.enterText((EditText)solo.getView(R.id.comment_text), "Quick & easy noodle recipe");

        // will have to manually select image since robotium doesn't have accesss to
        // activities outside of the application
        solo.clickOnButton("Select from gallery");
        solo.sleep(500);
        solo.clickOnButton("Add an ingredient");

        // wait for fragment to pop up
        solo.waitForText("Quick Add Ingredient", 1, 2000);
        solo.enterText((EditText)solo.getView(R.id.quick_add_ingredient_description), "ramen");
        solo.pressSpinnerItem(0, 3); // select ramen as Grain
        solo.enterText((EditText)solo.getView(R.id.quick_add_ingredient_amount), "1");
        solo.enterText((EditText)solo.getView(R.id.quick_add_ingredient_unit), "1");

        solo.clickOnText("Ok");
        solo.sleep(500);
        solo.clickOnButton("Submit");

        solo.waitForText("Noodles", 1, 2000);
        // try to find the new recipe we added
        ArrayList<Recipe> recipes = activity.getListViewRecipe();
        Recipe foundRecipe = null;
        for (Recipe r : recipes) {
            if ("Noodles".equals(r.getTitle())) {
                foundRecipe = r;
                break;
            }
        }

        if (foundRecipe != null) {
            // delete from the database afterwards
            dbms.deleteRecipeFromDatabase(foundRecipe.getId(), foundRecipe.getImageFileName());
        }
        assertTrue("Recipe was not added", foundRecipe!=null);
    }

    /**
     * Method to delete recipe from database expediently
     * @param r
     *  Recipe to be deleted
     */
    private void deleteRecipeFromDatabase(Recipe r) {
        String id = r.getId();
        String imageFileName = r.getImageFileName();
        RecipesCollection.document(id).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("RecipesActivityTest", "Successfully deleted recipe "+id);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("RecipesActivityTest", "Failed to delete recipe"+id);
                    }
                });
        // delete the image from the firebase storage
        if (imageFileName != null && !"".equals(imageFileName)) {
            userFilesRef.child(imageFileName).delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("RecipesActivityTest", "Successfully deleted image " + imageFileName);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("RecipesActivityTest", "Failed to delete image " + imageFileName);
                        }
                    });
        }
    }

    @Test
    public void testViewRecipe(){
        solo.assertCurrentActivity("Your are still on RecipesActivity",RecipesActivity.class);
        solo.waitForText("Mock Recipe", 1, 2000);

        ArrayList<Recipe> recipes = activity.getListViewRecipe();
        int pos = 1;  // clickInList is indexed by 1 for some reason
        for (Recipe recipe : recipes) {
            if ("Mock Recipe".equals(recipe.getComments())) {
                break;
            }
            pos++;
        }

        // click on the mock recipe "Dosa" named mockRecipeView
        if (pos>3)
            solo.scrollDown();
        solo.clickLongInList(pos);
        // long clicking on list view should result in launching View Recipe Activity
        // asserts that the activity launches is ViewRecipeActitvity
        solo.assertCurrentActivity("You are viewing ViewRecipeActivity", ViewRecipeActivity.class);
        TextView titleText = (TextView) solo.getView(R.id.titleText);
        TextView  servingsText = (TextView) solo.getView(R.id.servingsText);
        TextView categoryText = (TextView) solo.getView(R.id.categoryText);
        TextView commentsText = (TextView) solo.getView(R.id.commentText);
        TextView timeText = (TextView) solo.getView(R.id.timeText);

      //  ListView ingredientsList = (ListView) solo.getView(R.id.ingredientList);

        // make sure they all match up
        assertEquals("Title Matches", mockRecipeView.getTitle(), titleText.getText().toString());
        assertEquals("Servings Matches", mockRecipeView.getServingValue(), Integer.parseInt(servingsText.getText().toString()));
        assertEquals("Category Matches", mockRecipeView.getCategory(), categoryText.getText().toString());
        assertEquals("Comments Matches", mockRecipeView.getComments(), commentsText.getText().toString());
    }
}
