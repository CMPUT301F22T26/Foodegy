package com.CMPUT301F22T26.foodegy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends ArrayAdapter<Recipe>{
    private Context context;
    private ArrayList<Recipe> recipeArrayList;
    public RecipeAdapter(Context context, ArrayList<Recipe> recipeArrayList){
        super(context,R.layout.recipe_activity_listview,recipeArrayList);
        this.context = context;
        this.recipeArrayList = recipeArrayList;

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       /* return super.getView(position, convertView, parent);*/
        View listview = convertView;
        if(listview ==  null){
            listview = LayoutInflater.from(context).inflate(R.layout.recipe_activity_listview, parent, false);
        }
        Recipe currentRecipe = recipeArrayList.get(position);

        TextView title = listview.findViewById(R.id.recipeHighlight);
        TextView price = listview.findViewById(R.id.recipePrice);
        TextView unit = listview.findViewById(R.id.recipeUnit);
        TextView comment = listview.findViewById(R.id.recipeDescription);
        ImageView foodpic = listview.findViewById(R.id.foodimageView);

        title.setText(currentRecipe.getTitle());
        price.setText("Price: "+currentRecipe.getAmount());
        unit.setText("Unit: "+ currentRecipe.getServingValue());
        comment.setText(currentRecipe.getComments());
       // foodpic.setImageURI(currentRecipe.getRecipeImage());
        foodpic.setImageResource(currentRecipe.getImageId());


        return listview;

    }
}
