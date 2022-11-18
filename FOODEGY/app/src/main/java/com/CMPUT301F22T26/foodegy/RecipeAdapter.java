package com.CMPUT301F22T26.foodegy;

import android.content.Context;

import android.net.Uri;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * This class is responsible for making Recipes render-able on screen
 */
public class RecipeAdapter extends ArrayAdapter<Recipe>{
    private Context context;
    private ArrayList<Recipe> recipeArrayList;
    final private DatabaseManager dbm = DatabaseManager.getInstance();

    private ImageView foodpic;
    /**
     * Initialize a RecipeAdapter!
     * @param context the context from which the Adapter has been initialized
     * @param recipeArrayList the ArrayList of Recipes we wish to display
     */
    public RecipeAdapter(Context context, ArrayList<Recipe> recipeArrayList){
        super(context,R.layout.recipe_activity_listview,recipeArrayList);
        this.context = context;
        this.recipeArrayList = recipeArrayList;

    }

    /**
     * Returns a View of a particular Recipe
     * @param position the position in the ArrayList of which Recipe we wish to see
     * @param convertView the old view, for reusing if possible
     * @param parent the parent element where this View will belong
     * @return the View for Android to display
     */
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
        foodpic = listview.findViewById(R.id.foodimageView);

        title.setText(currentRecipe.getTitle());
        price.setText("Price: "+currentRecipe.getAmount());
        unit.setText("Unit: "+ currentRecipe.getServingValue());
        comment.setText(currentRecipe.getComments());

        Picasso.get().load(currentRecipe.getRecipeImage()).into(foodpic);
        //Glide.with(getContext()).load(currentRecipe.getRecipeImage()).into(foodpic);
        // load in the image
//        String imageFileName = currentRecipe.getImageFileName();
//        if (imageFileName != null && !"".equals(imageFileName)) {
//            Context context = getContext();
//            dbm.getUserFilesRef().child(imageFileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                @Override
//                public void onSuccess(Uri uri) {
//                    Log.d("RecipeAdapter", "Got download URL for " + uri.toString());
//                    String url = uri.toString();
//                    Glide.with(context).load(url).into(foodpic);
//                }
//            });
//        }

        return listview;
    }
}
