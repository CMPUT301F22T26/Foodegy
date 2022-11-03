package com.CMPUT301F22T26.foodegy;

import static org.junit.Assert.assertTrue;

import android.app.Fragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Test class for features of the Ingredients activity
 */
public class IngredientsActivityTest {
    private Solo solo;
    private FloatingActionButton addButton;
    private IngredientsActivity activity;
    private StorageIngredient mockIngredient;

    @Rule
    public ActivityTestRule<IngredientsActivity> rule =
            new ActivityTestRule<>(IngredientsActivity.class, true, true);

    /**
     * Run before all tests & creates the Solo instance
     * @throws Exception
     */
    @Before
    public void setup() throws Exception {
        activity = rule.getActivity();
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), activity);
        addButton = (FloatingActionButton) solo.getView(R.id.floatingActionButton);

        // create a mock ingredient that we can use to test viewing
        mockIngredient = new StorageIngredient("Mock Ingredient", "31-12-2020", "Freezer", 18, 1, "Dairy");
        activity.addIngredientToDatabase(mockIngredient);
    }

    /**
     * Test adding an ingredient
     */
    @Test
    public void testAddIngredient() throws Exception {
        // press the add button to make the dialog fragment pop up
        solo.assertCurrentActivity("Must be IngredientsActivity",IngredientsActivity.class);
        solo.clickOnView(addButton);
        EditText editDescription = (EditText)solo.getView(R.id.editTextIngredientDescription);
        EditText editQuantity = (EditText)solo.getView(R.id.editTextIngredientAmount);
        EditText editUnit = (EditText)solo.getView(R.id.editTextIngredientUnitCost);
        DatePicker datePicker = (DatePicker)solo.getView(R.id.addIngredientDatePicker);

        // enter dummy data
        solo.enterText(editDescription, "TestIngredient!!");
        solo.pressSpinnerItem(0,1);
        solo.pressSpinnerItem(1,0);
        solo.enterText(editQuantity, "15");
        solo.enterText(editUnit, "4");
        solo.setDatePicker(datePicker, 2023, 4, 19);

        solo.clickOnButton("Submit");

        // give it a second to update properly
        solo.waitForText("TestIngredient!!", 1, 2000);

        // try to find it
        ArrayList<StorageIngredient> ingredients = activity.getIngredientData();
        boolean found = false;
        String id = "";
        for (StorageIngredient ingredient : ingredients) {
            if (ingredient.getDescription().equals("TestIngredient!!")) {
                found = true;
                id = ingredient.getId();  // save the id of the created food to delete it after
                break;
            }
        }
        assertTrue("Did not find ingredient in the list", found);

        // delete the created ingredient afterwards
        activity.deleteIngredientFromDatabase(id);
    }


}
