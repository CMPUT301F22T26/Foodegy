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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import java.util.ArrayList;

public class ShoppingListActivityTest {
    private Solo solo;
    private ShoppingListActivity activity;
    private ShoppingListItem dummyItem1;
    private ShoppingListItem dummyItem2;
    private ShoppingListItem dummyItem3;
    private String itemName;
    private Integer amount;
    private String category;
    private DatabaseManager dbm;

    private ShoppingListActivity shoppingListActivity;
    private ShoppingListItem dummyItem;
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
        activity = rule.getActivity();
        dbm = DatabaseManager.getInstance();
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), activity);

        // create a dummyitems to add in the shopping list
        dummyItem1 = new ShoppingListItem("Dummy1", 12, "Count", "Egg");
        dummyItem2 = new ShoppingListItem("Dummy2", 6, "Kg", "Grain");
        dummyItem3 = new ShoppingListItem("Dummy3", 2, "Litres", "Dairy");
        activity.addItemToShoppingList(dummyItem1);
        activity.addItemToShoppingList(dummyItem2);
        activity.addItemToShoppingList(dummyItem3);
        shoppingListActivity = rule.getActivity();
        dbm = DatabaseManager.getInstance();

        shoppingListActivity.addItemToShoppingList(dummyItem);
    }

    /**
     * Run after all the tests are complete
     * @throws Exception
     */
    @After
    public void takedown() throws Exception {
        // remove the 3 dummy variables after testing
        activity.deleteItemFromShoppingList(dummyItem1);
        activity.deleteItemFromShoppingList(dummyItem2);
        activity.deleteItemFromShoppingList(dummyItem3);
    }

    @Test
    public void checkActivityOpen(){
        //check if shopping list activity is open
        onView(withId(R.id.shopping_list_activity)).check(matches(isDisplayed()));
    }

    @Test
    public void checkPopUpForCompleteIngredient(){

    }


}

//    /**
//     * Run before all tests & creates the Solo instance
//     * @throws Exception
//     */
//    @Before
//    public void setup() throws Exception {
//        activity = rule.getActivity();
//        dbm = DatabaseManager.getInstance();
//        solo = new Solo(InstrumentationRegistry.getInstrumentation(), activity);
//        addButton = (FloatingActionButton) solo.getView(R.id.floatingActionButton);
//
//        locations = activity.getResources().getStringArray(R.array.locations_array);
//        categories = activity.getResources().getStringArray(R.array.categories_array);
//
//        // create a mock ingredient that we can use to test viewing
//        mockIngredientView = new StorageIngredient("Mock Ingredient", "31-12-2020", locations[0], 18, "R", categories[0]);
//        mockIngredientDelete = new StorageIngredient("Test delete ingredient", "01-11-1999", locations[0], 10, "R", categories[0]);
//        mockIngredientEdit = new StorageIngredient("Test edit ingredient", "01-02-1000",locations[0], 10, "R", categories[0]);
//        activity.addIngredientToDatabase(mockIngredientView);
//        activity.addIngredientToDatabase(mockIngredientDelete);
//        activity.addIngredientToDatabase(mockIngredientEdit);
//    }
//
//    /**
//     * Run after all the tests are complete
//     * @throws Exception
//     */
//    @After
//    public void takedown() throws Exception {
//        // remove the mock ingredient from the database afterwards<3
//        activity.deleteIngredientFromDatabase(mockIngredientView.getId());
//        activity.deleteIngredientFromDatabase(mockIngredientDelete.getId());
//        activity.deleteIngredientFromDatabase(mockIngredientEdit.getId());
//    }


//    ate String itemName;
//    private Integer amount;
//    private String measurementUnit;
//    private String category;


//    private Solo solo;
//    private FloatingActionButton addButton;
//    private IngredientsActivity activity;
//    private StorageIngredient mockIngredientView;
//    private StorageIngredient mockIngredientDelete;
//    private StorageIngredient mockIngredientEdit;
//    private String[] locations;
//    private String[] categories;
//    private DatabaseManager dbm;
//    @Rule
//    public ActivityTestRule<IngredientsActivity> rule =
//            new ActivityTestRule<>(IngredientsActivity.class, true, true);
