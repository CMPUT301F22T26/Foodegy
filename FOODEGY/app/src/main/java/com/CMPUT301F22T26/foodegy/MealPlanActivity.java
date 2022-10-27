package com.CMPUT301F22T26.foodegy;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MealPlanActivity extends AppCompatActivity {
    //list view related variables
    ListView mealPlanItemsView;
    ArrayAdapter<MealPlanItem> mealPlanItemsAdapter;
    ArrayList<MealPlanItem> mealPlanData;

    //calendar related variables
    CalendarView calendarView;
    TextView headerText;

    //giving some initial values
    List<String> dates = Arrays.asList("29", "23", "30", "30", "23", "23", "30", "30");
//    int[] months = new int[]{2, 3, 1};
    List<String> months = Arrays.asList("10", "10", "10", "10", "10", "10", "10", "10");
    List<String> years = Arrays.asList("2022", "2022", "2022", "2022", "2022", "2022", "2022", "2022");
        //initializing some FoodItems objects
        FoodItems foodItem1 = new TempIngredients("Apple");
        FoodItems foodItem2 = new TempIngredients("Avocado");
        FoodItems foodItem3 = new TempIngredients("Mango");
        FoodItems foodItem4 = new TempIngredients("Bread");
        FoodItems foodItem5 = new TempIngredients("Ham");
        FoodItems foodItem6 = new TempIngredients("Bagel");
        FoodItems foodItem7 = new TempIngredients("Butter");
        FoodItems foodItem8 = new TempIngredients("Mayo");
    List<FoodItems> foodItems = Arrays.asList(foodItem1, foodItem2, foodItem3, foodItem4, foodItem5, foodItem6, foodItem7, foodItem8);
    List<String> servings = Arrays.asList("3", "4", "2", "2", "1", "3", "9", "4");


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_plan_activity);

        //setting up list view
        mealPlanData = new ArrayList<MealPlanItem>();
        mealPlanItemsView = findViewById(R.id.meal_plan_items);

            //populate the list
//            for (int j = 0; j < 3; j++){
//                mealPlanData.add(new MealPlanItem(dates.get(j), months.get(j), years.get(j), foodItems.get(j), servings.get(j)));
//            }
        mealPlanItemsAdapter = new MealPlanItemsList(this, mealPlanData);
        mealPlanItemsView.setAdapter(mealPlanItemsAdapter);

        //creating the calendar
        calendarView = findViewById(R.id.calendar_view);
        headerText = findViewById(R.id.meal_plan_header);
        headerText.setText("Heyo!");


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                String date = (i1 + 1) + "/" + i2 + "/" + i;
                headerText.setText(date);
                for (int k = 0; k < mealPlanData.size(); k ++){
                    mealPlanData.remove(k);
                }
                for (int j = 0; j < months.size(); j++){
                    String currDate = months.get(j) + "/" + dates.get(j) + "/" + years.get(j);
                    if (currDate.equals(date)) {
                        mealPlanData.add(new MealPlanItem(dates.get(j), months.get(j), years.get(j), foodItems.get(j), servings.get(j)));
                    }
                }
                mealPlanItemsAdapter.notifyDataSetChanged();
            }
        });

    }
}
