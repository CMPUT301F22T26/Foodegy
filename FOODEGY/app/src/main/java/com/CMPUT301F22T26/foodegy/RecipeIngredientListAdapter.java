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

public class RecipeIngredientListAdapter extends ArrayAdapter<RecipeIngredient> {
    private Context context;
    private ArrayList<RecipeIngredient> dataList;

    public RecipeIngredientListAdapter(Context context, ArrayList<RecipeIngredient> dataList){
        super(context, 0, dataList);
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.recipe_ingredient_list_content, parent, false);
        }
        TextView descriptionText = view.findViewById(R.id.ingredient_description);
        TextView categoryText = view.findViewById(R.id.ingredient_category);
        TextView amountText = view.findViewById(R.id.ingredient_amount);
        TextView unitText = view.findViewById(R.id.ingredient_unit);

        RecipeIngredient currentIngredient = dataList.get(position);

        descriptionText.setText(currentIngredient.getDescription());
        categoryText.setText("Category: " + currentIngredient.getCategory());
        amountText.setText("- " + currentIngredient.getAmount());
        unitText.setText("- " + currentIngredient.getUnit());


        return view;
    }
}
