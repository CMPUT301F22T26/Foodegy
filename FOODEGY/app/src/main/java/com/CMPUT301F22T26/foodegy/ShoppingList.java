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

public class ShoppingList extends ArrayAdapter<ShoppingListItem> {

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
        TextView itemName = view.findViewById(R.id.item_name);
        TextView itemDescription = view.findViewById(R.id.item_description);
        TextView itemAmount = view.findViewById(R.id.item_amount);
        TextView itemUnitCost = view.findViewById(R.id.item_unit_cost);
        TextView itemCategory = view.findViewById(R.id.item_category);

        //filling up the object in view
        itemName.setText(currentItem.getItemName());
        itemDescription.setText(currentItem.getDescription());
        itemAmount.setText(currentItem.getAmount());
        itemUnitCost.setText(currentItem.getUnitCost());
        itemCategory.setText(currentItem.getCategory());

        return view;

    }
}
