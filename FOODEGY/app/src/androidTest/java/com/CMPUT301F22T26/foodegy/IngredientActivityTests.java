package com.CMPUT301F22T26.foodegy;

import static androidx.test.espresso.Espresso.onView;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.junit.Assert.assertTrue;

import android.content.Intent;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

public class IngredientActivityTests {
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

//    @Rule
//    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<>(MainActivity.class);


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
        mockIngredientView = new StorageIngredient("Mock Ingredient", "31-12-2020", locations[0], 18, "R", categories[0]);
        mockIngredientDelete = new StorageIngredient("Test delete ingredient", "01-11-1999", locations[0], 10, "R", categories[0]);
        mockIngredientEdit = new StorageIngredient("Test edit ingredient", "01-02-1000",locations[0], 10, "R", categories[0]);
        activity.addIngredientToDatabase(mockIngredientView);
        String ingred1 = mockIngredientView.getDescription();
        System.out.println("ingred1: "+ ingred1);
        activity.addIngredientToDatabase(mockIngredientDelete);
        activity.addIngredientToDatabase(mockIngredientEdit);
    }

    /**
     * Run after all the tests are complete
     * @throws Exception
     */
    @After
    public void takeDown() throws Exception {
        // remove the mock ingredient from the database afterwards<3
//        String ingred = mockIngredientView.getDescription();
//        System.out.println("ingred: "+ ingred);
        activity.deleteIngredientFromDatabase(mockIngredientView.getId());
        activity.deleteIngredientFromDatabase(mockIngredientDelete.getId());
        activity.deleteIngredientFromDatabase(mockIngredientEdit.getId());
    }

    @Test
    public void testIngredientActivityStart() throws Exception{
//        onView(withId(R.id.buttonToIngredientActivity)).perform(click());
        onView(withId(R.id.ingredients_activity)).check(matches(isDisplayed()));
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


}


