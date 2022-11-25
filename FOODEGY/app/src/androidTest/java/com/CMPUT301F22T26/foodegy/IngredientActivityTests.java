package com.CMPUT301F22T26.foodegy;

import static androidx.test.espresso.Espresso.onView;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

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
        activity.deleteIngredientFromDatabase(mockIngredientView.getId());
        activity.deleteIngredientFromDatabase(mockIngredientDelete.getId());
        activity.deleteIngredientFromDatabase(mockIngredientEdit.getId());
    }


//    @Test
//    public void testMainStart(){
//        onView(withId(R.id.StartPage)).check(matches(isDisplayed()));
//    }

    @Test
    public void testIngredientActivityStart(){
//        onView(withId(R.id.buttonToIngredientActivity)).perform(click());
        onView(withId(R.id.ingredients_activity)).check(matches(isDisplayed()));
    }
//
//    @Test
//    public void testRecipeActivityStart(){
//        onView(withId(R.id.buttonToRecipesActivity)).perform(click());
//        onView(withId(R.id.recipes_view_activity)).check(matches(isDisplayed()));
//    }
//
//    @Test
//    public void testMealPlanActivityStart(){
//        onView(withId(R.id.buttonToMealPlanActivity)).perform(click());
//        onView(withId(R.id.meal_plan_activity)).check(matches(isDisplayed()));
//    }
//
//    @Test
//    public void testShoppingListActivityStart(){
//        onView(withId(R.id.buttonToShoppingListActivity)).perform(click());
//        onView(withId(R.id.shopping_list_activity)).check(matches(isDisplayed()));
//    }


}


