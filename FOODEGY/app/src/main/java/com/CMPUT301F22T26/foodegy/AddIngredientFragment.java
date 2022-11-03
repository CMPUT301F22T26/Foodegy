package com.CMPUT301F22T26.foodegy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AddIngredientFragment extends androidx.fragment.app.DialogFragment {
    private EditText ingredientDescription;
    private EditText ingredientUnitCost;
    private EditText ingredientAmount;
    private Spinner location;
    private Spinner category;
    private DatePicker bestBeforeDate;

    private OnFragmentInteractionListener listener;

    private StorageIngredient ingredient;
    public AddIngredientFragment() {
        super();
    }
    public AddIngredientFragment(StorageIngredient ingredient) {
        super();
        this.ingredient = ingredient;
    }

    public interface OnFragmentInteractionListener {
        void onAddPressed(StorageIngredient newIngredient);
        void onEditPressed(StorageIngredient ingredient);

    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
            + " must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_ingredient_dialog_fragment, null);
        ingredientDescription = view.findViewById(R.id.editTextIngredientDescription);
        ingredientAmount = view.findViewById(R.id.editTextIngredientAmount);
        ingredientUnitCost = view.findViewById(R.id.editTextIngredientUnitCost);
        bestBeforeDate = view.findViewById(R.id.addIngredientDatePicker);
        // note that the spinners are populated in the add_ingredient_dialog_fragment.xml already
        //  from the arrays declared in strings.xml (located in res/values/strings.xml)
        location = view.findViewById(R.id.addIngredientLocationSpinner);
        category = view.findViewById(R.id.addIngredientCategorySpinner);


        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getContext());

        String label;
        if (ingredient == null) {
            // we are adding a new food
            label = "Add ingredient";
        }
        else {
            // editing a food
            label = "Edit ingredient";

            // want fill in all the options from the data in the food
            ingredientDescription.setText(ingredient.getDescription());
            ingredientAmount.setText(Integer.toString(ingredient.getAmount()));
            ingredientUnitCost.setText(Integer.toString(ingredient.getUnitCost()));

            // get string array from resources
            String[] categories = getResources().getStringArray(R.array.categories_array);
            int i=0;
            while (!categories[i].equals(ingredient.getCategory()))
                i++;
            category.setSelection(i);

            String[] locations = getResources().getStringArray(R.array.locations_array);
            i=0;
            while (!locations[i].equals(ingredient.getLocation()))
                i++;
            location.setSelection(i);
            // need to convert the date to integers to pass to the DatePicker
            String[] date = ingredient.getBestBeforeDate().split("-");
            bestBeforeDate.updateDate(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]));
        }
        return builder
            .setView(view)
            .setTitle(label)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
            .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String description = ingredientDescription.getText().toString();
                    String amountStr = ingredientAmount.getText().toString();
                    String unitCostStr = ingredientUnitCost.getText().toString();
                    // get the date & convert it to a string
                    String year = Integer.toString(bestBeforeDate.getYear());
                    String month = Integer.toString(bestBeforeDate.getMonth());
                    String day = Integer.toString(bestBeforeDate.getDayOfMonth());
                    // add leading 0s if necessary
                    if (month.length() == 1) month = "0" + month;
                    if (day.length() == 1) day = "0" + day;


                    // validate input!
                    if (description.length() == 0) {
                        Toast.makeText(getActivity(), "Description cannot be empty", Toast.LENGTH_LONG).show();
                        return;
                    }
                    int amount;  // validate amount
                    if (amountStr.length() == 0) {
                        Toast.makeText(getActivity(), "Amount cannot be empty", Toast.LENGTH_LONG).show();
                        return;
                    }
                    else {
                        amount = Integer.parseInt(amountStr);
                    }
                    int unitCost; // validate unitCost
                    if (unitCostStr.length() == 0) {
                        Toast.makeText(getActivity(), "Unit Cost cannot be empty", Toast.LENGTH_LONG).show();
                        return;
                    }
                    else {
                        unitCost = Integer.parseInt(unitCostStr);
                    }

                    if (ingredient == null) {
                        // making a new ingredient
                        // get the date & convert to a string

                        listener.onAddPressed(new StorageIngredient(
                                description,
                                day+"-"+month+"-"+year,
                                location.getSelectedItem().toString(),
                                amount,
                                unitCost,
                                category.getSelectedItem().toString()
                        ));
                    }
                    else {
                        ingredient.setDescription(description);
                        ingredient.setBestBeforeDate(day+"-"+month+"-"+year);
                        ingredient.setAmount(amount);
                        ingredient.setUnitCost(unitCost);
                        ingredient.setCategory(category.getSelectedItem().toString());
                        ((IngredientsActivity) getActivity()).editIngredientInDatabase(ingredient.getId(), ingredient);
                    }

                };
            }).create();


    }

}