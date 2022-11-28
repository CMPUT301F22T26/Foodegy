package com.CMPUT301F22T26.foodegy;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.provider.MediaStore;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Test stories & functionality related to MealPlans activity
 * Including:
 * -> Adding a meal plan
 * -> Making sure Meal Plans are visible
 * ->
 */
public class MealPlanActivityTest {
    private Solo solo;
    private FloatingActionButton addButton;
    private MealPlanItem mockIngredientMealPlanItemVisible;
    private MealPlanItem mockRecipeMealPlanItemVisible;
    private MealPlanItem mockMealPlanItemNonVisible;

    @Rule
    public ActivityTestRule<MealPlanActivity> rule =
            new ActivityTestRule<>(MealPlanActivity.class, true, true);

    /**
     * Runs before all the tests. Creates an instance of Solo
     * @throws Exception
     */
    @Before
    public void setup() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        addButton = (FloatingActionButton) solo.getView(R.id.addMealPlanButton);
    }

    /**
     * Run after all the tests are complete
     * @throws Exception
     */
    @After
    public void takedown() throws Exception {
        // no cleanup needed
    }


    @Test
    public void checkFragmentPopsUp(){
        solo.assertCurrentActivity("Must be Meal Plan Activity", MealPlanActivity.class);
        solo.clickOnView(addButton);

        solo.waitForText("Ingredient", 1, 2000);

        // checks an id from add_meal_plan_fragment
        onView(withId(R.id.meal_fragment_date)).check(matches((isDisplayed())));

    }


    /**
     * Test adding a meal plan & viewing it
     * -> Testing with specific data that was already there from previous tests
     * on Recipes and Ingredients
     */
    @Test
    public void testAddMealPlan() throws Exception {
        solo.assertCurrentActivity("Must be MealPlanActivity",MealPlanActivity.class);
        solo.clickOnView(addButton);

        // select cream cheese
//        solo.pressSpinnerItem(0, 3);
//        solo.enterText((EditText)solo.getView(R.id.addMealServingsCount), "9");
//        solo.setDatePicker((DatePicker)solo.getView(R.id.addMealPlanDatePicker), 2024, 8, 10);
//        solo.clickOnButton("Submit");

        // give it a second to update properly
        solo.waitForText("eggz", 1, 2000);

        // select "eggz" on index 2
        solo.clickOnView(addButton);
        // select apple
        solo.pressSpinnerItem(0, 2);
        solo.setDatePicker((DatePicker)solo.getView(R.id.addMealPlanDatePicker), 2028, 10, 30);
        solo.enterText((EditText)solo.getView(R.id.addMealServingsCount), "4");
        solo.clickOnButton("Submit");

        // give it a second to update properly
        solo.waitForText("eggz", 1, 2000);

        // Invalid entry
//        solo.clickOnView(addButton);
//        solo.clickOnView((RadioButton) solo.getView(R.id.recipeRadioButton));
//        // give it a few seconds to update
        solo.sleep(2000);
//        solo.pressSpinnerItem(0, 1);
//        solo.enterText((EditText)solo.getView(R.id.addMealServingsCount), "1");
//        solo.setDatePicker((DatePicker)solo.getView(R.id.addMealPlanDatePicker), 2021, 10, 0);
//        solo.clickOnButton("Submit");
//        solo.waitForText("new rexipeeee", 1, 2000);

        // try to find it
        ArrayList<MealPlanItem> mealPlans = rule.getActivity().getMealPlanData();
        boolean found = false;
        // testing if one of the valid entries is in the list and the invalid entry is not in the list
        for (MealPlanItem mealPlan : mealPlans) {
            System.out.println(mealPlan.getName());
            assertNotEquals(mealPlan.getName(), "new rexipeeee");
            if (mealPlan.getName().equals("eggz")) {
                found = true;
            }
        }
        assertTrue("Did not find ingredient in the list", found);


    }

}

//    @Test
//    public void testAddMealPlan() throws Exception {
//        solo.assertCurrentActivity("Must be MealPlanActivity",MealPlanActivity.class);
//        solo.clickOnView(addButton);
//
//        // select cream cheese
//        solo.pressSpinnerItem(0, 3);
//        solo.enterText((EditText)solo.getView(R.id.addMealServingsCount), "9");
//        solo.setDatePicker((DatePicker)solo.getView(R.id.addMealPlanDatePicker), 2024, 8, 10);
//        solo.clickOnButton("Submit");
//
//        // give it a second to update properly
//        solo.waitForText("Cream cheese", 1, 2000);
//
//        solo.clickOnView(addButton);
//        // select apple
//        solo.pressSpinnerItem(0, 2);
//        solo.setDatePicker((DatePicker)solo.getView(R.id.addMealPlanDatePicker), 2028, 10, 30);
//        solo.enterText((EditText)solo.getView(R.id.addMealServingsCount), "4");
//        solo.clickOnButton("Submit");
//
//        solo.waitForText("Apple", 1, 2000);
//
//        // Invalid entry
//        solo.clickOnView(addButton);
//        solo.clickOnView((RadioButton) solo.getView(R.id.recipeRadioButton));
//        // give it a few seconds to update
//        solo.sleep(2000);
//        solo.pressSpinnerItem(0, 1);
//        solo.enterText((EditText)solo.getView(R.id.addMealServingsCount), "1");
//        solo.setDatePicker((DatePicker)solo.getView(R.id.addMealPlanDatePicker), 2021, 10, 0);
//        solo.clickOnButton("Submit");
//        solo.waitForText("new rexipeeee", 1, 2000);
//
//        // try to find it
//        ArrayList<MealPlanItem> mealPlans = rule.getActivity().getMealPlanData();
//        boolean found = false;
//        // testing if one of the valid entries is in the list
//        // and the invalid entry is not in the list
//        for (MealPlanItem mealPlan : mealPlans) {
//            System.out.println(mealPlan.getName());
//            assertNotEquals(mealPlan.getName(), "new rexipeeee");
//            if (mealPlan.getName().equals("Cream cheese")) {
//                found = true;
//            }
//        }
//        assertTrue("Did not find ingredient in the list", found);
//
//
//    }
