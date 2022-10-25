package com.CMPUT301F22T26.foodegy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

public class AddIngredientFragment extends androidx.fragment.app.DialogFragment {
    private EditText ingredientDescription;
    private EditText ingredientUnitCost;
    private EditText ingredientAmount;
    private Spinner location;
    private Spinner category;
    private Spinner day;
    private Spinner month;
    private Spinner year;

    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onOkPressed(StorageIngredient newIngredient);

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

        // note that the spinners are populated in the add_ingredient_dialog_fragment.xml already
        //  from the arrays declared in strings.xml (located in res/values/strings.xml)
        // CONSIDER: may fit nicer on the screen to use a date picker dialog!
        day = view.findViewById(R.id.addIngredientDaySpinner);
        month = view.findViewById(R.id.addIngredientMonthSpinner);
        year = view.findViewById(R.id.addIngredientYearSpinner);
        location = view.findViewById(R.id.addIngredientLocationSpinner);
        category = view.findViewById(R.id.addIngredientCategorySpinner);



        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getContext());

        return builder
            .setView(view)
            .setTitle("Add ingredient")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
            .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String description = ingredientDescription.getText().toString();
                    String d = day.getSelectedItem().toString();
                    String m = month.getSelectedItem().toString();
                    String y = year.getSelectedItem().toString();
                    listener.onOkPressed(new StorageIngredient(
                            description,
                            y+"-"+m+"-"+d,
                            location.getSelectedItem().toString(),
                            Integer.parseInt(ingredientAmount.getText().toString()),
                            Integer.parseInt(ingredientUnitCost.getText().toString()),
                            category.getSelectedItem().toString()));

                };
                // removed both of the original buttons with these default ones
                // because these were quicker to implement
            }).create();


    }

}
