package com.CMPUT301F22T26.foodegy;


import static androidx.test.espresso.Espresso.onView;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;

import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

public class TestTests {

    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void testMainStart(){
        onView(withId(R.id.StartPage)).check(matches(isDisplayed()));
    }

    @Test
    public void testIngredientActivityStart(){
        onView(withId(R.id.buttonToIngredientActivity)).perform(click());
        onView(withId(R.id.ingredients_activity)).check(matches(isDisplayed()));
    }

    @Test
    public void testRecipeActivityStart(){
        onView(withId(R.id.buttonToRecipesActivity)).perform(click());
        onView(withId(R.id.recipes_view_activity)).check(matches(isDisplayed()));
    }

    @Test
    public void testMealPlanActivityStart(){
        onView(withId(R.id.buttonToMealPlanActivity)).perform(click());
        onView(withId(R.id.meal_plan_activity)).check(matches(isDisplayed()));
    }

    @Test
    public void testShoppingListActivityStart(){
        onView(withId(R.id.buttonToShoppingListActivity)).perform(click());
        onView(withId(R.id.shopping_list_activity)).check(matches(isDisplayed()));
    }



}
