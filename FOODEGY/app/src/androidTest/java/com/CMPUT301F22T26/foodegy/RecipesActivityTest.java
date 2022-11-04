package com.CMPUT301F22T26.foodegy;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

public class RecipesActivityTest {
    private Solo solo;
    private RecipesActivity activity;

    @Rule
    public ActivityTestRule<RecipesActivity> rule =
            new ActivityTestRule<>(RecipesActivity.class, true, true);
    public Recipe recipe1;
    public Recipe recipe2;

    @Before
    public void setup() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        //addButton = (FloatingActionButton) solo.getView(R.id.addRecipe);

        ArrayList<RecipeIngredient> ingredients1 = new ArrayList<RecipeIngredient>();
        ingredients1.add(new RecipeIngredient("ramen", "Grain", "1", "1"));
        ingredients1.add(new RecipeIngredient("chili paste", "Condiment", "1", "5"));

        recipe1 = new Recipe("Noodles", "0", "15", "2", "Lunch",
                "100g", "testimg1.jpg", "Quick & easy noodle recipe", ingredients1);

        ArrayList<RecipeIngredient> ingredients2 = new ArrayList<RecipeIngredient>();
        ingredients2.add(new RecipeIngredient("white bread", "Grain", "3", "4"));
        ingredients2.add(new RecipeIngredient("Large white egg", "Egg", "1", "2"));

        recipe2 = new Recipe("French toast", "0", "20", "3", "Breakfast",
                "2", "frenchToast.jpg", "Sweet French toast for breakfast", ingredients2);


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
        solo.enterText((EditText)solo.getView(R.id.amount_text), "100");
        solo.enterText((EditText)solo.getView(R.id.comment_text), "Quick & easy noodle recipe");

        // will have to manually select image since robotium doesn't have accesss to
        // activities outside of the application
        solo.clickOnButton("Select from gallery");
        solo.sleep(500);
        solo.clickOnButton("Add an ingredient");

        // wait for fragment to pop up
        solo.waitForFragmentById(R.layout.add_ingredient_to_recipe);
        solo.enterText((EditText)solo.getView(R.id.quick_add_ingredient_description), "ramen");
        solo.pressSpinnerItem(0, 3); // select ramen as Grain
        solo.enterText((EditText)solo.getView(R.id.quick_add_ingredient_amount), "1");
        solo.enterText((EditText)solo.getView(R.id.quick_add_ingredient_unit), "1");

        solo.clickOnText("Ok");
        solo.sleep(500);
        solo.clickOnButton("Submit");
    }
}
