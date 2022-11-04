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

public class ShoppingList extends ArrayAdapter<ShoppingListItem>  {

    private ArrayList<ShoppingListItem> itemsList;
    private Context context;

    ShoppingList(Context context, ArrayList<ShoppingListItem> itemsList){
        super(context, 0, itemsList);
        this.context = context;
        this.itemsList = itemsList;
    }

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
        Button buyIngredient = view.findViewById(R.id.remove_ingredient);

        bought.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    buyIngredient.setVisibility(View.VISIBLE);
                    buyIngredient.setClickable(true);

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
        itemUnitCostView.setText(String.valueOf(currentItem.getUnitCost()));
        itemCategoryView.setText(currentItem.getCategory());
        return view;

    }

}
