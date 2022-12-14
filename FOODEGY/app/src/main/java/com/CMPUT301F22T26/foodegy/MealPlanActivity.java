package com.CMPUT301F22T26.foodegy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//import org.checkerframework.checker.units.qual.C;

import com.CMPUT301F22T26.foodegy.databinding.ActivityRecipesBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * The activity for viewing & interacting with the user's MealPlans. Handles storing
 * entries in the Firebase and making queries.
 */

public class MealPlanActivity extends AppCompatActivity implements AddMealPlanFragment.OnFragmentInteractionListener {
    //list view related variables
    private ListView mealPlanItemsView;
    private ArrayAdapter<MealPlanItem> mealPlanItemsAdapter;
    private ArrayList<MealPlanItem> mealPlanData;
    private String timeStampDate;

    // each device has a unique id, use that as their own personal collection name
    final private DatabaseManager dbm = DatabaseManager.getInstance();
    final private CollectionReference MealPlans = dbm.getMealPlansCollection();

    //calendar related variables
    private CalendarView calendarView;
    private TextView headerText;
    private BottomNavigationView bottomNavBar;
    private LinearLayout calendar_list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_plan_activity);



        // find the items planned for today
        Calendar calendar = Calendar.getInstance();
        timeStampDate = String.valueOf(calendar.getTimeInMillis());


        MealPlans.whereLessThanOrEqualTo("startDate", timeStampDate).orderBy("startDate").get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot doc : task.getResult()){
                                try {
                                    // now must manually filter by endDate
                                    Map data = doc.getData();
                                    String endDate = (String) data.get("endDate");
                                    if (Long.parseLong(endDate) >= Long.parseLong(timeStampDate)) {
                                        String id = (String) data.get("id");
                                        String startDate = (String) data.get("startDate");
                                        String name = (String) data.get("name");
                                        Long servings = (Long) data.get("servings");
                                        ArrayList<ShoppingListItem> ingredients = (ArrayList<ShoppingListItem>) data.get("ingredients");

                                        mealPlanData.add(new MealPlanItem(startDate, endDate, name, servings, ingredients));
                                        mealPlanItemsAdapter.notifyDataSetChanged();


                                    }
                                } catch (Exception e) {
                                    Log.d("Query", "Error reading document", e);
                                }
                            }


                        } else {
                            Log.d("Query", "Error getting documents: ", task.getException());
                        }
                    }
                }


        );


        bottomNavBar = findViewById(R.id.bottom_nav);
        bottomNavBar.setSelectedItemId(R.id.meal_plan);
        bottomNavBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ingredients:
                        startActivity(new Intent(getBaseContext(), IngredientsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.meal_plan:
                        break;
                    case R.id.shopping_cart:
                        startActivity(new Intent(getBaseContext(), ShoppingListActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.recipes:
                        startActivity(new Intent(getBaseContext(), RecipesActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                }

                return false;
            }
        });


        // visibility of CalendarView
        Button btnCalVisibility = findViewById(R.id.btn_Calendar_Visibility);
        calendar_list = findViewById(R.id.calendar_list_layout);
        btnCalVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnCalVisibility.getText() == getText(R.string.hide) ){
                    slideUp(calendarView, calendar_list);
                    btnCalVisibility.setText("Show Calendar");
                }else{
                    slideDown(calendarView, calendar_list);
                    btnCalVisibility.setText(R.string.hide);
                }
            }
        });

        //setting up list view
        mealPlanData = new ArrayList<MealPlanItem>();
        mealPlanItemsView = findViewById(R.id.meal_plan_items);


        mealPlanItemsAdapter = new MealPlanItemsList(this, mealPlanData);
        mealPlanItemsView.setAdapter(mealPlanItemsAdapter);

        //creating the calendar
        calendarView = findViewById(R.id.calendar_view);
        headerText = findViewById(R.id.meal_plan_header);
        headerText.setText("Your Meal Plans");

        final FloatingActionButton addMealPlanButton = findViewById(R.id.addMealPlanButton);
        addMealPlanButton.setOnClickListener((v)->{
            // will have to pass the date to the add  meal fragment
            Bundle args = new Bundle();
            // get date as String
            if (timeStampDate == null){
                // if timeStampDate has not been initialized, initialize it
                // set to current date
                timeStampDate = String.valueOf(Calendar.getInstance().getTimeInMillis());
                Log.d("timeStampDate", timeStampDate);
            }
            args.putString("Date", timeStampDate);
            AddMealPlanFragment fragment = new AddMealPlanFragment();
            fragment.setArguments(args);

            fragment.show(getSupportFragmentManager(), "ADD_MEAL_PLAN");
        });


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int intYear, int intMonth, int intDay) {
                // put date in DD-MM-YYYY FOR DISPLAY PURPOSES
                System.out.println("ON SELECTED DAY CHANGE EXECUTE");
                String month = String.valueOf(intMonth); // do NOT have to increment month by 1 here
                String day = String.valueOf(intDay);
                if (month.length() == 1) month = "0" + month;
                if (day.length() == 1) day = "0" + day;
                String date = day+"-"+month+"-"+String.valueOf(intYear);
                headerText.setText(date);


                // for storage in firebase, need it in timestamp format
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, intYear);
                calendar.set(Calendar.MONTH, intMonth );
                calendar.set(Calendar.DAY_OF_MONTH, intDay);

                timeStampDate = String.valueOf(calendar.getTimeInMillis());

                // query database for all items that can be eaten within this range
                // process:
                // grab all, sort by endDate
                // append to list until endDate is past the currentDate
                mealPlanData.clear();
                mealPlanItemsAdapter.notifyDataSetChanged();
                // query mealPlans & order by endDate

                // must call endAt to limit

                MealPlans.whereLessThanOrEqualTo("startDate", timeStampDate).orderBy("startDate").get().addOnCompleteListener(
                        new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    for (DocumentSnapshot doc : task.getResult()){
                                        try {
                                            // now must manually filter by endDate
                                            Map data = doc.getData();
                                            String endDate = (String) data.get("endDate");
                                            if (Long.parseLong(endDate) >= Long.parseLong(timeStampDate)) {
                                                String id = (String) data.get("id");
                                                String startDate = (String) data.get("startDate");
                                                String name = (String) data.get("name");
                                                Long servings = (Long) data.get("servings");
                                                ArrayList<ShoppingListItem> ingredients = (ArrayList<ShoppingListItem>) data.get("ingredients");

                                                mealPlanData.add(new MealPlanItem(startDate, endDate, name, servings, ingredients));
                                                mealPlanItemsAdapter.notifyDataSetChanged();


                                            }
                                        } catch (Exception e) {
                                            Log.d("Query", "Error reading document", e);
                                        }
                                    }


                                } else {
                                    Log.d("Query", "Error getting documents: ", task.getException());
                                }
                            }
                        }


                );
            }
        });

    }

    @Override
    public void onSubmitPressed(MealPlanItem mealPlanItem) {
        System.out.println("MEAL PLAN ACTIVITY!!!! ADDING THROUGH DM" + mealPlanItem);
        mealPlanItemsAdapter.add(mealPlanItem);
        dbm.addMealPlanToDatabase(mealPlanItem);
    }

    /**
     * Returns meal plan data, used for testing purposes
     * @return
     *  The ArrayList of current meal plans
     */
    public ArrayList<MealPlanItem> getMealPlanData() {
        return mealPlanData;
    }

    // slide the view from below itself to the current position
    public void slideUp(View view, View view2){
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,  // fromYDelta
                -view.getHeight());                // toYDelta

        animate.setDuration(500);
        animate.setFillAfter(true);
        view2.startAnimation(animate);

    }

    // slide the view from its current position to below itself
    public void slideDown(View view, View view2){
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                -view.getHeight(),                 // fromYDelta
                0); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view2.startAnimation(animate);
    }
}
