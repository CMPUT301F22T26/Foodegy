package com.CMPUT301F22T26.foodegy;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


import java.util.ArrayList;

/**
 * The ShoppingList class is responsible for generating a view of a shopping list item, based on
 * an array of shopping list items
 */
public class ShoppingList extends ArrayAdapter<ShoppingListItem>  {

    private ArrayList<ShoppingListItem> itemsList;
    private Context context;
    private ArrayList<Integer> checkedItems;

    /**
     * Initialize a ShoppingList!
     * @param context the context (usually ShoppingListActivity) from which this ShoppingList
     *                is being initialized from
     * @param itemsList the list of shopping list items that will need to be rendered
     */
    ShoppingList(Context context, ArrayList<ShoppingListItem> itemsList){
        super(context, 0, itemsList);
        this.context = context;
        this.itemsList = itemsList;
        checkedItems = new ArrayList<Integer>();
    }

    /**
     * Get the view of a particular shopping list item
     * @param position the position, as an integer, of this item in the data list
     * @param convertView the old view, for reuse, if possible
     * @param parent the parent element to which this view will belong
     * @return the view of the item
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.shopping_list_item_view, parent, false);
        }

        //get ShoppingListItem object
        ShoppingListItem currentItem = itemsList.get(position);

        //finding views to fill up the FoodItem object
        TextView itemNameView = view.findViewById(R.id.item_name);
        TextView itemAmountView = view.findViewById(R.id.item_amount);
        TextView itemUnitCostView = view.findViewById(R.id.item_unit_cost);
        TextView itemCategoryView = view.findViewById(R.id.item_category);

        CheckBox bought = view.findViewById(R.id.Bought);
        if (!checkedItems.contains(position)){
            bought.setChecked(false);
        }
        Button buyIngredient = view.findViewById(R.id.remove_ingredient);





        bought.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    buyIngredient.setVisibility(View.VISIBLE);
                    buyIngredient.setClickable(true);
                    checkedItems.add(position);
                    System.out.println(checkedItems);

                    buyIngredient.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Bundle args = new Bundle();
                            args.putInt("pos", position);

                            AddIngredientFragment editFragment = new AddIngredientFragment();
                            ShoppingListActivity myActivity = (ShoppingListActivity) context;
                            myActivity.inflateFragment(currentItem);
                        }


                    });


                } else {
                    buyIngredient.setVisibility(View.INVISIBLE);
                    buyIngredient.setClickable(false);
                    try {
                        checkedItems.remove(checkedItems.indexOf(position));
                    } catch (Exception e) {

                    }

                }
                if (checkedItems.contains(position)){
                    bought.setChecked(true);
                }
            }
        });

        buyIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //filling up the object in view
        itemNameView.setText(currentItem.getItemName());
        itemAmountView.setText(String.valueOf(currentItem.getAmount()));
        itemUnitCostView.setText(currentItem.getMeasurementUnit());
        itemCategoryView.setText(currentItem.getCategory());

        String category = currentItem.getCategory();
        View indicator = view.findViewById(R.id.shopping_cat_indicator);
        if (category.equals("Vegetable") | category.equals("Fruit")) {
            indicator.setBackgroundColor(view.getResources().getColor(R.color.vegetable));
        } else if (category.equals("Dairy") | category.equals("Condiment") | category.equals("Drink")) {
            indicator.setBackgroundColor(view.getResources().getColor(R.color.dairy));
        } else if (category.equals("Grain") | category.equals("Carb")) {
            indicator.setBackgroundColor(view.getResources().getColor(R.color.grain));
        } else if (category.equals("Meat") | category.equals("Protein")) {
            indicator.setBackgroundColor(view.getResources().getColor(R.color.meat));
        }

        return view;

    }

}
