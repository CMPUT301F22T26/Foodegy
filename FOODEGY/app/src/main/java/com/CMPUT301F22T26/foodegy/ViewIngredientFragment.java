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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentResultOwner;
import androidx.lifecycle.LifecycleOwner;


public class ViewIngredientFragment  extends DialogFragment implements FragmentResultOwner {

    private AddIngredientFragment.OnFragmentInteractionListener listener;

    private StorageIngredient ingredient;

    public ViewIngredientFragment(StorageIngredient ingredient) {
        super();
        this.ingredient = ingredient;
    }
    private TextView ingredientDescription;
    private TextView ingredientAmount;
    private TextView bestBeforeDate;
    private TextView location;
    private TextView category;
    private TextView unit;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof AddIngredientFragment.OnFragmentInteractionListener){
            listener = (AddIngredientFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.ingredient_item_view, null);
        ingredientDescription = view.findViewById(R.id.ingredientViewName);
        ingredientAmount = view.findViewById(R.id.ingredientViewThisAmount);
        bestBeforeDate = view.findViewById(R.id.ingredientViewThisBestBeforeDate);
        location = view.findViewById(R.id.ingredientViewThisLocation);
        category = view.findViewById(R.id.ingredientViewThisCategory);
        unit = view.findViewById(R.id.ingredientViewThisUnit);

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getContext());

        String label;
        if (ingredient == null) {
            // we are adding a new food
            label = "Add ingredient";
        }
        else {
            // editing a food
            label = "View ingredient";

            // want fill in all the options from the data in the food
            ingredientDescription.setText(ingredient.getDescription());
            ingredientAmount.setText(Integer.toString(ingredient.getAmount()));
            bestBeforeDate.setText(ingredient.getBestBeforeDate());
            location.setText(ingredient.getLocation());
            category.setText(ingredient.getCategory());
            unit.setText(Integer.toString(ingredient.getUnitCost()));

        }
        return builder
                .setView(view)
                .setTitle(label)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((IngredientsActivity) getActivity()).deleteIngredient(ingredient);


                    }
                })
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((IngredientsActivity) getActivity()).onEditPressed(ingredient);

                    };
                }).create();


    }


    @Override
    public void setFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

    }

    @Override
    public void clearFragmentResult(@NonNull String requestKey) {

    }

    @Override
    public void setFragmentResultListener(@NonNull String requestKey, @NonNull LifecycleOwner lifecycleOwner, @NonNull FragmentResultListener listener) {

    }

    @Override
    public void clearFragmentResultListener(@NonNull String requestKey) {

    }
}