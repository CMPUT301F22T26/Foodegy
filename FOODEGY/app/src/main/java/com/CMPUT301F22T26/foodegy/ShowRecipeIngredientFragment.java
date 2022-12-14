package com.CMPUT301F22T26.foodegy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

/**
 * This class is responsible for displaying the details of a RecipeIngredient as a DialogFragment
 */
public class ShowRecipeIngredientFragment extends DialogFragment {
    private TextView name;
    private TextView category;
    private TextView unit;
    private RecipeIngredient recipeIngredient;
    private ShowRecipeIngredientFragment.OnFragmentInteractionListener listener;

    public ShowRecipeIngredientFragment() {
        super();
    }

    /**
     * Fragment to display details of a recipe
     * @param r
     *  The recipe to show
     */
    public ShowRecipeIngredientFragment(RecipeIngredient r) {
        super();
        recipeIngredient = r;
    }
    /**
     * Functions handling Interactions with the RecipeIngredient
     * (user may either click ok, and close the fragment, or they may choose
     * to delete the ingredient)
     */
    public interface OnFragmentInteractionListener {
        void onShowRecipeIngredientOkPressed(int pos);
        void onShowRecipeIngredientDeletePressed(int pos);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ShowRecipeIngredientFragment.OnFragmentInteractionListener) {
            listener = (ShowRecipeIngredientFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement the interface method(s)");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.show_recipe_ingredient, null);

        name = view.findViewById(R.id.show_ingredient_title);
        category = view.findViewById(R.id.show_ingredient_category);
        unit = view.findViewById(R.id.show_ingredient_unit);

        Bundle mArgs = getArguments();
        int pos = mArgs.getInt("pos");

        name.setText(recipeIngredient.getDescription());
        category.setText(recipeIngredient.getCategory());
        unit.setText(recipeIngredient.getUnit());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setNegativeButton("Cancel", null)
                .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onShowRecipeIngredientDeletePressed(pos);
                    }
                })
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    listener.onShowRecipeIngredientOkPressed(pos);

                    }
                }).create();
    }
}
