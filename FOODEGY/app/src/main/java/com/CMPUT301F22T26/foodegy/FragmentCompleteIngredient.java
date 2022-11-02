package com.CMPUT301F22T26.foodegy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class FragmentCompleteIngredient extends DialogFragment {
    private EditText itemNameText;
    private EditText descriptionText;
    private DatePicker bestBeforeDateText;
    private Spinner locationText;
    private EditText amountText;
    private EditText unitCostText;
    private Spinner categoryText;
    ArrayList<ShoppingListItem> dataList = MainActivity.shoppingListData;


    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener{
        void onOkPressed(ShoppingListItem newFood, ShoppingListItem newItem);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement the interface method(s)");
        }
    }

//    public static FragmentCompleteIngredient newInstance(ShoppingListItem shoppingListItem) {
//
//        Bundle args = new Bundle();
//        args.putSerializable("editable", shoppingListItem);
//        FragmentCompleteIngredient fragment = new FragmentCompleteIngredient();
//        fragment.setArguments(args);
//        return fragment;
//    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_complete_ingredient, null);
//        itemNameText = view.findViewById(R.id.item_name);
        descriptionText = view.findViewById(R.id.editTextIngredientDescription);
        bestBeforeDateText = view.findViewById(R.id.addIngredientDatePicker);
        locationText = view.findViewById(R.id.addIngredientLocationSpinner);
        amountText = view.findViewById(R.id.editTextIngredientAmount);
        unitCostText = view.findViewById(R.id.editTextIngredientUnitCost);
        categoryText = view.findViewById(R.id.addIngredientLocationSpinner);

        Bundle mArgs =getArguments();
        int pos = mArgs.getInt("pos");
        ShoppingListItem editItem = dataList.get(pos);

        descriptionText.setText(editItem.getDescription());
        amountText.setText(editItem.getAmount());
        unitCostText.setText(editItem.getUnitCost());

        //setting category text
        ArrayAdapter<CharSequence> adapter_cat = ArrayAdapter.createFromResource(getContext(), R.array.categories_array, android.R.layout.simple_spinner_dropdown_item);
        adapter_cat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryText.setAdapter(adapter_cat);
        String curr_cat = editItem.getCategory();
        ArrayAdapter cat_adapter = (ArrayAdapter) categoryText.getAdapter();
        int catSpinnerPos = cat_adapter.getPosition(curr_cat);
        categoryText.setSelection(catSpinnerPos);

        //setting location text
        ArrayAdapter<CharSequence> adapter_loc = ArrayAdapter.createFromResource(getContext(), R.array.locations_array, android.R.layout.simple_spinner_dropdown_item);
        adapter_loc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationText.setAdapter(adapter_loc);
        String curr_loc = editItem.getLocation();
        ArrayAdapter loc_adapter = (ArrayAdapter) locationText.getAdapter();
        int locSpinnerPos = loc_adapter.getPosition(curr_cat);
        locationText.setSelection(locSpinnerPos);


            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            return builder
                    .setView(view)
                    .setTitle("Edit Food")
                    .setNegativeButton("Cancel", null)

                    .setPositiveButton("Ok", (dialogInterface, i) -> {
                        String itemDescription =  descriptionText.getText().toString();
                        int date = bestBeforeDateText.getDayOfMonth();
                        int month = bestBeforeDateText.getMonth();
                        int year = bestBeforeDateText.getYear();
                        String bestBeforeDate = String.valueOf(date)+"-"+String.valueOf(month)+"-"+String.valueOf(year);
                        String location = locationText.getSelectedItem().toString();
                                //getText().toString();
                        String amount = amountText.getText().toString();
                        String unitCost = unitCostText.getText().toString();
                        String category = categoryText.getSelectedItem().toString();
                        String itemName = editItem.getItemName();

                        //new food item to be added in storage ingredient
                        ShoppingListItem newItem = new ShoppingListItem(itemName, itemDescription, bestBeforeDate, location, amount, unitCost, category);

                        //removing the edit item from shopping list
                        listener.onOkPressed(editItem, newItem);


                    }).create();
//        }
//        else{
//            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//            return builder
//                    .setView(view)
//                    .setTitle("Edit Food")
//                    .setNegativeButton("Cancel", null)
//
//                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                        }
//                    }).create();
//        }
    }
}
