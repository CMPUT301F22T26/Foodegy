package com.CMPUT301F22T26.foodegy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
                    //listener.onOkPressed(new StorageIngredient("h", "hh", "hhh", 3, 1, "dd"));
                    // not complete
                };
                // removed both of the original buttons with these default ones
                // because these were quicker to implement
            }).create();


    }

}
