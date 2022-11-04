package com.CMPUT301F22T26.foodegy;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Class responsible for rendering the MealPlanItems within the app
 */
public class MealPlanItemsList extends ArrayAdapter<MealPlanItem> {
    private ArrayList<MealPlanItem> itemsList;
    private Context context;

    /**
     * Initialize a MealPlanItemsList
     * @param context the context where the MealPlanItemsList was initialized
     * @param itemsList list of Items we wish to display in the app
     */
    public MealPlanItemsList(Context context, ArrayList<MealPlanItem> itemsList){
        super(context, 0, itemsList);
        this.itemsList = itemsList;
        this.context = context;
    }

    /**
     * Converts unix time to string we can display to the user
     * @param timestamp unix time that we wish to display
     * @return the string representation of time ("DD-MM-YYYY")
     */
    private String timestampToString(String timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

        if (month.length() == 1) month = "0" + month;
        if (day.length() == 1) day = "0" + day;
        return day+"-"+month+"-"+year;


    }

    /**
     * Returns the view that will be displayed
     * @param position the item in ArrayList that we wish to render
     * @param convertView old view for reuse, if possible
     * @param parent parent element containing this view
     * @return the View that will be rendered to user screen
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.meal_plan_item_view, parent, false);
        }
        //finding views to fill up the FoodItem object
        TextView itemName = view.findViewById(R.id.item_name);
        TextView servings = view.findViewById(R.id.item_servings);
        TextView date = view.findViewById(R.id.mealStartDate);
        TextView endDateView = view.findViewById(R.id.mealEndDate);


        //get FoodItem object
        MealPlanItem mealPlanItem = itemsList.get(position);

        //filling the FoodItem object in view

        endDateView.setText(timestampToString(mealPlanItem.getEndDate()));
        itemName.setText(mealPlanItem.getName());
        servings.setText(String.valueOf(mealPlanItem.getServings()));
        date.setText(timestampToString(mealPlanItem.getStartDate()));
        return view;
    }
}
