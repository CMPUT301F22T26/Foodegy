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
 * This class is responsible for rendering the list of RecipeIngredients of a particular Recipe
 * (used inside of AddRecipeActivity)
 */
public class RecipeIngredientListAdapter extends ArrayAdapter<RecipeIngredient> {
    private Context context;
    private ArrayList<RecipeIngredient> dataList;

    /**
     * Initialize a RecipeIngredientListAdapter for rendering the Recipe's ingredients
     * @param context the context (AddRecipeActivity) from which this ListAdapter has been called
     * @param dataList the list of RecipeIngredients to be rendered
     */
    public RecipeIngredientListAdapter(Context context, ArrayList<RecipeIngredient> dataList){
        super(context, 0, dataList);
        this.context = context;
        this.dataList = dataList;
    }

    /**
     * Get the view of a particular RecipeIngredient
     * @param position the position, as an integer, of this Recipe Ingredient in the data list
     * @param convertView the old view, for reuse, if possible
     * @param parent the parent element to which this view will belong
     * @return the Recipe Ingredient's view (that we can render on screen)
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.recipe_ingredient_list_content, parent, false);
        }
        TextView descriptionText = view.findViewById(R.id.ingredient_description);
        TextView categoryText = view.findViewById(R.id.ingredient_category);
        TextView unitText = view.findViewById(R.id.ingredient_unit);

        RecipeIngredient currentIngredient = dataList.get(position);

        descriptionText.setText(currentIngredient.getDescription());
        categoryText.setText("Category: " + currentIngredient.getCategory());
        unitText.setText("Unit: " + currentIngredient.getUnit());


        return view;
    }
}
