package com.CMPUT301F22T26.foodegy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.app.Fragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.robotium.solo.Solo;

import org.junit.After;
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
     * Run after all the tests are complete
     * @throws Exception
     */
    @After
    public void takedown() throws Exception {
        // remove the mock ingredient from the database afterwards<3
        activity.deleteIngredientFromDatabase(mockIngredient.getId());
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

    /**
     * Test viewing mockIngredient from the list
     */
    @Test
    public void viewIngredientTest() {
        // find the position of mockIngredient in the list view
        solo.waitForText("Mock Ingredient", 1, 2000);
        ArrayList<StorageIngredient> ingredients = activity.getIngredientData();
        int pos = 0;
        for (StorageIngredient ing : ingredients) {
            if (ing.getDescription().equals("Mock Ingredient")) {
                break;
            }
            pos++;
        }

        // click on the mock ingredient
        solo.clickInList(pos);
        // read the fragment that pops up
        TextView descView = (TextView) solo.getView(R.id.ingredientViewName);
        TextView amountView = (TextView) solo.getView(R.id.ingredientViewThisAmount);
        TextView bestBeforeView = (TextView) solo.getView(R.id.ingredientViewThisBestBeforeDate);
        TextView locationView = (TextView) solo.getView(R.id.ingredientViewThisLocation);
        TextView categoryView = (TextView) solo.getView(R.id.ingredientViewThisCategory);
        TextView unitView = (TextView) solo.getView(R.id.ingredientViewThisUnit);

        // make sure they all match up
        assertEquals("Description incorrect", mockIngredient.getDescription(), descView.getText().toString());
        assertEquals("Amount incorrect", mockIngredient.getAmount(), Integer.parseInt(amountView.getText().toString()));
        assertEquals("Best before incorrect", mockIngredient.getBestBeforeDate(), bestBeforeView.getText().toString());
        assertEquals("Location incorrect", mockIngredient.getLocation(), locationView.getText().toString());
        assertEquals("Category incorrect", mockIngredient.getCategory(), categoryView.getText().toString());
        assertEquals("Unit incorrect", mockIngredient.getUnitCost(), Integer.parseInt(unitView.getText().toString()));
    }
}
