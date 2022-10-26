package com.CMPUT301F22T26.foodegy;

import android.app.appsearch.StorageInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MealPlanItemsList extends ArrayAdapter<MealPlanItem> {
    private ArrayList<MealPlanItem> itemsList;
    private Context context;

    public MealPlanItemsList(Context context, ArrayList<MealPlanItem> itemsList){
        super(context, 0, itemsList);
        this.itemsList = itemsList;
        this.context = context;
    }

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
        TextView date = view.findViewById(R.id.text3);
//        TextView month = view.findViewById(R.id.text4);
//        TextView year = view.findViewById(R.id.text5);

        //get FoodItem object
        MealPlanItem mealPlanItem = itemsList.get(position);

        //filling the FoodItem object in view
        String date_s = mealPlanItem.getDate();
        String month_s = mealPlanItem.getMonth();
        String year_s = mealPlanItem.getYear();
        itemName.setText(mealPlanItem.getFoodItem());
        servings.setText(mealPlanItem.getServings());
        String date_f = date_s + "/" + month_s + "/" + year_s;
        date.setText(date_f);
//        month.setText(month_s);
//        year.setText(year_s);

        return view;
    }
}
