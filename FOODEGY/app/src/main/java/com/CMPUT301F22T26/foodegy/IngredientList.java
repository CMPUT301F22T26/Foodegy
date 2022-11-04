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

    public IngredientList(Context context, ArrayList<StorageIngredient> ingredientList){
        super(context, 0, ingredientList);
        this.ingredientList = ingredientList;
        this.context = context;

    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;
        if (view==null){
            view = LayoutInflater.from(context).inflate(R.layout.ingredient_item, parent, false);

        }
        // get the views to fill in
        StorageIngredient ingredient = ingredientList.get(position);
        TextView ingredientItemDescription = view.findViewById(R.id.IngredientItemDescription);
        TextView ingredientDate = view.findViewById(R.id.ingredientDate);
        TextView ingredientLocation = view.findViewById(R.id.ingredientLocation);
        TextView ingredientQuantity = view.findViewById(R.id.ingredientQuantity);
        TextView ingredientUnitCost = view.findViewById(R.id.ingredientUnitCost);
        TextView ingredientCategory = view.findViewById(R.id.ingredientCategory);

        // fill in the views based on ingredient data
        ingredientItemDescription.setText(ingredient.getDescription());
        ingredientDate.setText(ingredient.getBestBeforeDate());
        ingredientLocation.setText(ingredient.getLocation());
        ingredientQuantity.setText(String.valueOf(ingredient.getAmount()));
        ingredientCategory.setText(ingredient.getCategory());
        ingredientUnitCost.setText(String.valueOf(ingredient.getUnitCost()));

        return view;


    }



}
