package com.CMPUT301F22T26.foodegy;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import android.widget.Button;
import android.widget.CheckBox;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

public class ShoppingListActivityTest {
    private Solo solo;
    private ShoppingListActivity shoppingListActivity;
    private ShoppingListItem dummyItem;
    private DatabaseManager dbm;
    private CheckBox isBought;
    private Button addToStorage;


    @Rule
    public ActivityTestRule<ShoppingListActivity> rule =
            new ActivityTestRule<>(ShoppingListActivity.class, true, true);


    /**
     * Run before all tests & creates the Solo instance
     * @throws Exception
     */
    @Before
    public void setup() throws Exception {
        shoppingListActivity = rule.getActivity();
        dbm = DatabaseManager.getInstance();
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), shoppingListActivity);


        // create a dummyitems to add in the shopping list
        dummyItem = new ShoppingListItem("Dummy", 12, "Count", "Egg");
        shoppingListActivity.addItemToShoppingList(dummyItem);
    }

    /**
     * Run after all the tests are complete
     * @throws Exception
     */
    @After
    public void takedown() throws Exception {
        // remove the 3 dummy variables after testing
        shoppingListActivity.deleteItemFromShoppingList(dummyItem);

    }

//    @Test
//    public void checkActivityOpen(){
//        //check if shopping list activity is open
//        onView(withId(R.layout.shopping_list_activity)).check(matches(isDisplayed()));
//    }

    @Test
    public void checkIfCheckBoxIsChecked(){
        solo.assertCurrentActivity("Must be Shopping List Activity", ShoppingListActivity.class);

        //wait
        solo.waitForText("List", 1, 2000);
        isBought = (CheckBox) solo.getView(R.id.Bought);
        solo.clickOnView(isBought);

        onView(withId(R.id.remove_ingredient)).check(matches(isDisplayed()));

    }

    @Test
    public void checkAddIngredientFragment(){
        solo.assertCurrentActivity("Must be Shopping List Activity", ShoppingListActivity.class);

        //wait
        solo.waitForText("List", 1, 2000);
        isBought = (CheckBox) solo.getView(R.id.Bought);
        solo.clickOnView(isBought);

        solo.waitForText("Title", 1, 2000);
        addToStorage = (Button) solo.getView(R.id.remove_ingredient);
        solo.clickOnView(addToStorage);

        solo.waitForText("Title", 1, 2000);

        //check an id from add_ingredient_dialog_fragment
        onView(withId(R.id.editTextIngredientDescription)).check(matches(isDisplayed()));
    }

    @Test
    public void checkItemRemoved(){
        solo.assertCurrentActivity("Must be Shopping List Activity", ShoppingListActivity.class);

        //give wait
        solo.waitForText("List", 1, 2000);
        isBought = (CheckBox) solo.getView(R.id.Bought);
        solo.clickOnView(isBought);

        solo.waitForText("Title", 1, 2000);
        addToStorage = (Button) solo.getView(R.id.remove_ingredient);
        solo.clickOnView(addToStorage);

        solo.waitForText("Title", 1, 2000);

        solo.clickOnButton("Submit");

        ArrayList<ShoppingListItem> listItems = shoppingListActivity.getShoppingListData();
        boolean removed = true;
        for (ShoppingListItem listItem: listItems ){
            if(listItem.getItemName().equals("Dummy")){
                removed = false;
            }
        }


        solo.waitForText("Title", 1, 2000);

        assertTrue("Could not remove the item from shopping list!", removed);

    }



//    solo.assertCurrentActivity("Must be MealPlanActivity",MealPlanActivity.class);
//        solo.clickOnView(addButton);
//
//    // give it a second to update properly
//        solo.waitForText("eggz", 1, 2000);
//
//    // select "eggz" on index 2
//        solo.clickOnView(addButton);
//    // select apple
//        solo.pressSpinnerItem(0, 2);
//        solo.setDatePicker((DatePicker)solo.getView(R.id.addMealPlanDatePicker), 2028, 10, 30);
//        solo.enterText((EditText)solo.getView(R.id.addMealServingsCount), "4");
//        solo.clickOnButton("Submit");
//
//    // give it a second to update properly
//        solo.waitForText("eggz", 1, 2000);
//
//        solo.sleep(2000);
//
//    // try to find it
//    ArrayList<MealPlanItem> mealPlans = rule.getActivity().getMealPlanData();
//    boolean found = false;
//    // testing if one of the valid entries is in the list and the invalid entry is not in the list
//        for (MealPlanItem mealPlan : mealPlans) {
//        System.out.println(mealPlan.getName());
//        assertNotEquals(mealPlan.getName(), "new rexipeeee");
//        if (mealPlan.getName().equals("eggz")) {
//            found = true;
//        }
//    }
//    assertTrue("Did not find ingredient in the list", found);


}
