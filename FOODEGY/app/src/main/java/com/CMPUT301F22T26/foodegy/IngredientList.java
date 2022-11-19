package com.CMPUT301F22T26.foodegy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * Custom ArrayAdapter for displaying StorageIngredients. Used in IngredientsActivity
 */
public class IngredientList extends ArrayAdapter<StorageIngredient> {
    private ArrayList<StorageIngredient> ingredientList;
    private Context context;

    /**
     * Constructor for creating an IngredientList
     * @param context: context from where the IngredientList is being created
     * @param ingredientList: Array of StorageIngredients to be displayed in IngredientList
     */
    public IngredientList(Context context, ArrayList<StorageIngredient> ingredientList){
        super(context, 0, ingredientList);
        this.ingredientList = ingredientList;
        this.context = context;

    }

    /**
     * Returns the View that can be displayed by the app
     * @param position the StorageIngredient's position inside the ArrayList
     *                 (ie. StorageIngredient we want to display)
     * @param convertView the old View to reuse, if possible
     * @param parent parent ViewGroup that this View will belong to
     * @return the View that has been created
     */
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;
        if (view==null){
            view = LayoutInflater.from(context).inflate(R.layout.ingredient_item, parent, false);
        }

        StorageIngredient ingredient = ingredientList.get(position);
        TextView ingredientItemDescription = view.findViewById(R.id.IngredientItemDescription);
        TextView ingredientDate = view.findViewById(R.id.ingredientDate);
        TextView ingredientLocation = view.findViewById(R.id.ingredientLocation);
        TextView ingredientQuantity = view.findViewById(R.id.ingredientQuantity);
        TextView ingredientUnitCost = view.findViewById(R.id.ingredientUnitCost);
        TextView ingredientCategory = view.findViewById(R.id.ingredientCategory);
        View indicator = view.findViewById(R.id.cat_indicator);


        String category = ingredient.getCategory();

        ingredientItemDescription.setText(ingredient.getDescription());
        ingredientDate.setText(ingredient.getBestBeforeDate());
        ingredientLocation.setText(ingredient.getLocation());
        ingredientQuantity.setText(String.valueOf(ingredient.getAmount()));
        ingredientCategory.setText(ingredient.getCategory());
        ingredientUnitCost.setText(ingredient.getMeasurementUnit());

        System.out.println(category.getClass());
        if (category.equals("Vegetable")) {
            indicator.setBackgroundColor(view.getResources().getColor(R.color.vegetable));
        } else if (category.equals("Dairy")) {
            indicator.setBackgroundColor(view.getResources().getColor(R.color.dairy));
        } else if (category.equals("Grain")) {
            indicator.setBackgroundColor(view.getResources().getColor(R.color.grain));
        } else if (category.equals("Meat")) {
            indicator.setBackgroundColor(view.getResources().getColor(R.color.meat));
        }

        return view;


    }


}
