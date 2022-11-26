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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Fragment that handles adding a RecipeIngredient to a recipe. Called when a user presses the
 * button that says "add an ingredient" in the AddRecipeActivity.
 * The user is able to fill in brief information about the ingredient.
 */

public class AddIngredientToRecipeFragment extends DialogFragment {
    private ArrayList<RecipeIngredient> dataList = AddRecipeActivity.ingredientsList;
    private TextView titleText;
    private EditText descriptionText;
    private Spinner categorySpinner;
    private EditText amountText;
    private EditText unitText;

    private AddIngredientToRecipeFragment.OnFragmentInteractionListener listener;

    /**
     * Listener for when the user is finished entering information and we can pass it back to
     * the AddRecipeActivity.
     */
    public interface OnFragmentInteractionListener {
        void onOkPressed(RecipeIngredient newIngredient);
        void onEditOkPressed(RecipeIngredient newIngredient, int i);
    }

    @Override
    public void onAttach(Context context) {
        // check whether the appropriate OnFragmentInteractionListener has been implemented
        super.onAttach(context);
        if (context instanceof AddIngredientToRecipeFragment.OnFragmentInteractionListener) {
            listener = (AddIngredientToRecipeFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement the interface method(s)");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_ingredient_to_recipe, null);

        titleText = view.findViewById(R.id.add_ingredient_title);
        descriptionText = view.findViewById(R.id.quick_add_ingredient_description);
        amountText = view.findViewById(R.id.quick_add_ingredient_amount);
        unitText = view.findViewById(R.id.quick_add_ingredient_unit);

        // category spinner
        categorySpinner = view.findViewById(R.id.quick_add_ingredient_category);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.quick_add_ingredient_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        categorySpinner.setAdapter(adapter);

        Bundle mArgs = getArguments();
        String eval = mArgs.getString("eval");

        if (eval == "Edit") {
            int pos = mArgs.getInt("pos");
            dataList = ViewRecipeActivity.recipeIngredients;
            RecipeIngredient currentIngredient = dataList.get(pos);
            descriptionText.setText(currentIngredient.getDescription());
            amountText.setText(currentIngredient.getAmount());
            unitText.setText(currentIngredient.getUnit());

            // Set spinner to current ingredient's category
            String currentLocation = currentIngredient.getCategory();
            ArrayAdapter myAdap = (ArrayAdapter) categorySpinner.getAdapter();
            int spinnerPosition = myAdap.getPosition(currentLocation);
            categorySpinner.setSelection(spinnerPosition);

            titleText.setText("Quick Edit Ingredient");
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            return builder
                    .setView(view)
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String description = descriptionText.getText().toString();
                            String category = categorySpinner.getSelectedItem().toString();
                            String amount = amountText.getText().toString();
                            String unit = unitText.getText().toString();

                            // validate input
                            if (description.length() == 0 || amount.length() == 0 || unit.length() == 0) {
                                Toast.makeText(getContext(), "Field(s) cannot be empty", Toast.LENGTH_LONG).show();
                                return;
                            }
                            RecipeIngredient newIngredient = new RecipeIngredient(description, category, amount, unit);
                            listener.onEditOkPressed(newIngredient, pos);

                        }
                    }).create();
        } else {
            titleText.setText("Quick Add Ingredient");
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            return builder
                    .setView(view)
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String description = descriptionText.getText().toString();
                            String category = categorySpinner.getSelectedItem().toString();
                            String amount = amountText.getText().toString();
                            String unit = unitText.getText().toString();

                            // validate input
                            if (description.length() == 0 || amount.length() == 0 || unit.length() == 0) {
                                Toast.makeText(getContext(), "Field(s) cannot be empty", Toast.LENGTH_LONG).show();
                                return;
                            }
                            RecipeIngredient newIngredient = new RecipeIngredient(description, category, amount, unit);
                            listener.onOkPressed(newIngredient);

                        }
                    }).create();
        }
    }
}
