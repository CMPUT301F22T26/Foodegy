package com.CMPUT301F22T26.foodegy;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;

public class RecipesActivityTest {
    private Solo solo;
    private RecipesActivity activity;

    @Rule
    public ActivityTestRule<RecipesActivity> rule =
            new ActivityTestRule<>(RecipesActivity.class, true, true);

    @Before
    public void setup() throws Exception {
        activity = rule.getActivity();
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), activity);
    }
}
