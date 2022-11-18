package com.CMPUT301F22T26.foodegy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.widget.DatePicker;
import android.widget.EditText;
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
    private StorageIngredient mockIngredientView;
    private StorageIngredient mockIngredientDelete;
    private StorageIngredient mockIngredientEdit;
    private String[] locations;
    private String[] categories;
    private DatabaseManager dbm;
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
        dbm = DatabaseManager.getInstance();
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), activity);
        addButton = (FloatingActionButton) solo.getView(R.id.floatingActionButton);

        locations = activity.getResources().getStringArray(R.array.locations_array);
        categories = activity.getResources().getStringArray(R.array.categories_array);

        // create a mock ingredient that we can use to test viewing
        mockIngredientView = new StorageIngredient("Mock Ingredient", "31-12-2020", locations[0], 18, 1, categories[0]);
        mockIngredientDelete = new StorageIngredient("Test delete ingredient", "01-11-1999", locations[0], 10, 1, categories[0]);
        mockIngredientEdit = new StorageIngredient("Test edit ingredient", "01-02-1000",locations[0], 10, 1, categories[0]);
        activity.addIngredientToDatabase(mockIngredientView);
        activity.addIngredientToDatabase(mockIngredientDelete);
        activity.addIngredientToDatabase(mockIngredientEdit);
    }

    /**
     * Run after all the tests are complete
     * @throws Exception
     */
    @After
    public void takedown() throws Exception {
        // remove the mock ingredient from the database afterwards<3
        activity.deleteIngredientFromDatabase(mockIngredientView.getId());
        activity.deleteIngredientFromDatabase(mockIngredientDelete.getId());
        activity.deleteIngredientFromDatabase(mockIngredientEdit.getId());
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
        solo.enterText(editDescription, "aTestIngredient!!");
        solo.pressSpinnerItem(0,1);
        solo.pressSpinnerItem(1,0);
        solo.enterText(editQuantity, "15");
        solo.enterText(editUnit, "4");
        solo.setDatePicker(datePicker, 2023, 4, 19);

        solo.clickOnButton("Submit");

        // give it a second to update properly
        solo.waitForText("aTestIngredient!!", 1, 2000);

        // try to find it
        ArrayList<StorageIngredient> ingredients = activity.getIngredientData();
        boolean found = false;
        String id = "";
        for (StorageIngredient ingredient : ingredients) {
            if (ingredient.getDescription().equals("aTestIngredient!!")) {
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
        int pos = 1;  // clickInList is indexed by 1 for some reason
        for (StorageIngredient ing : ingredients) {
            if ("Mock Ingredient".equals(ing.getDescription())) {
                break;
            }
            pos++;
        }

        // click on the mock ingredient
        if (pos>3)
            solo.scrollDown();
        solo.clickInList(pos);
        // read the fragment that pops up
        TextView descView = (TextView) solo.getView(R.id.ingredientViewName);
        TextView amountView = (TextView) solo.getView(R.id.ingredientViewThisAmount);
        TextView bestBeforeView = (TextView) solo.getView(R.id.ingredientViewThisBestBeforeDate);
        TextView locationView = (TextView) solo.getView(R.id.ingredientViewThisLocation);
        TextView categoryView = (TextView) solo.getView(R.id.ingredientViewThisCategory);
        TextView unitView = (TextView) solo.getView(R.id.ingredientViewThisUnit);

        // make sure they all match up
        assertEquals("Description incorrect", mockIngredientView.getDescription(), descView.getText().toString());
        assertEquals("Amount incorrect", mockIngredientView.getAmount(), Integer.parseInt(amountView.getText().toString()));
        assertEquals("Best before incorrect", mockIngredientView.getBestBeforeDate(), bestBeforeView.getText().toString());
        assertEquals("Location incorrect", mockIngredientView.getLocation(), locationView.getText().toString());
        assertEquals("Category incorrect", mockIngredientView.getCategory(), categoryView.getText().toString());
        assertEquals("Unit incorrect", mockIngredientView.getUnitCost(), Integer.parseInt(unitView.getText().toString()));
    }

    /**
     * Test deleting an ingredient
     */
    @Test
    public void deleteIngredientTest() {
        // wait for ingredient to be added
        solo.waitForText("Test delete ingredient", 1, 2000);

        // find the position of it in the list
        ArrayList<StorageIngredient> ingredients = activity.getIngredientData();
        int pos = 1;  // for some reason clickInList is indexed by 1???????? ;(
        for (StorageIngredient ing : ingredients) {
            if ("Test delete ingredient".equals(ing.getDescription())) {
                break;
            }
            pos++;
        }
        if (pos>3)
            solo.scrollDown();
        solo.clickInList(pos);
        solo.sleep(500);
        // delete >:D
        solo.clickOnButton("Delete");

        // give it 1 second to update
        solo.sleep(1000);
        // try to find the ingredient in the datalist again! (shouldnt be able to, because its gone)
        ingredients = activity.getIngredientData();
        boolean found = false;
        for (StorageIngredient ing : ingredients) {
            if ("Test delete ingredient".equals(ing.getDescription())) {
                found = true;
                break;
            }
        }
        assertFalse("Ingredient was not deleted", found);
    }

    /**
     * Test editing an ingredient
     */
    @Test
    public void editIngredientTest() {
        // wait for it to be added
        solo.waitForText("Test edit ingredient", 1, 2000);
        // find the ingredient in the list
        ArrayList<StorageIngredient> ingredients = activity.getIngredientData();
        int pos = 1;  // clickInList is indexed by 1 for some reason
        for (StorageIngredient ing : ingredients) {
            if ("Test edit ingredient".equals(ing.getDescription())) {
                break;
            }
            pos++;
        }

        // click on the mock ingredient
        if (pos>=3)
            solo.scrollDown();
        solo.clickInList(pos);
        solo.sleep(500);
        solo.clickOnButton("Edit");

        EditText editDescription = (EditText)solo.getView(R.id.editTextIngredientDescription);
        EditText editAmount = (EditText)solo.getView(R.id.editTextIngredientAmount);
        EditText editUnit = (EditText)solo.getView(R.id.editTextIngredientUnitCost);
        DatePicker datePicker = (DatePicker)solo.getView(R.id.addIngredientDatePicker);

        // clear data
        solo.clearEditText(editDescription);
        solo.clearEditText(editAmount);
        solo.clearEditText(editUnit);

        // enter dummy data
        solo.enterText(editDescription, "Edited the ingredient!");
        solo.pressSpinnerItem(0,1);  // location
        solo.pressSpinnerItem(1,1);  // category
        solo.enterText(editAmount, "333");
        solo.enterText(editUnit, "183");
        solo.setDatePicker(datePicker, 2020, 4, 2);
        solo.clickOnButton("Submit");

        solo.waitForText("Edited the ingredient!", 1, 2000);
        // find the ingredient again
        ingredients = activity.getIngredientData();
        StorageIngredient newIngredient = null;
        for (int i=0; i<ingredients.size(); i++) {
            StorageIngredient ing = ingredients.get(i);
            if ("Edited the ingredient!".equals(ing.getDescription())) {
                newIngredient = ing;
            }
        }

        // give it a second for it to update
        solo.waitForText("Edited the ingredient!", 1, 2000);
        assertEquals("Description not edited", "Edited the ingredient!", newIngredient.getDescription());
        assertEquals("Location not edited", locations[1], newIngredient.getLocation());
        assertEquals("Category not edited", categories[1], newIngredient.getCategory());
        assertEquals("Amount not edited", 333, newIngredient.getAmount());
        assertEquals("Unit not edited", 183, newIngredient.getUnitCost());
        assertEquals("Best Before Date not edited", "02-04-2020", newIngredient.getBestBeforeDate());
    }
}
