package com.CMPUT301F22T26.foodegy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Fragment that handles adding a new MealPlan by the user. Called when a user presses the
 * Floating Action Button in the MealPlanActivity.
 */
public class AddMealPlanFragment extends androidx.fragment.app.DialogFragment {
    private RadioGroup mealPlanType;
    private Spinner foodSelection;
    private EditText servings;
    private DatePicker finishEating;
    private TextView currentDate;
    private ArrayAdapter<String> adapter; // displays values needed for the spinner

    private AddMealPlanFragment.OnFragmentInteractionListener listener;

    // user id
    final private DatabaseManager dbm = DatabaseManager.getInstance();
    final private CollectionReference IngredientStorage = dbm.getIngredientStorageCollection();
    final private CollectionReference RecipeStorage = dbm.getRecipesCollection();

    /**
     * Interface that will allow the fragment to pass information back to the activity
     * (and subsequently the Firebase)
     */
    public interface OnFragmentInteractionListener {
        void onSubmitPressed(MealPlanItem mealPlanItem);

    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof AddMealPlanFragment.OnFragmentInteractionListener){
            listener = (AddMealPlanFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * After user changes their mind where they want to select their meal plan from,
     *         (ingredient or recipe),
     *          query the database to find available ingredients or recipes
     * @param ref reference to collection we want to query from
     * @param field String with the value that is to be displayed in the listView
     *              (title, or description)
     */
    public void updateMealType(CollectionReference ref, String field){
        // dynamically grab list of ingredients
        ArrayList<String> ingredientListName = new ArrayList<String>();
        // query firebase
        ref.get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot doc : task.getResult()){
                                try{
                                    String itemName = (String) doc.getData().get(field);

                                    ingredientListName.add(itemName);
                                } catch (Exception e){

                                }

                            }
                            // after we grabbed all the ingredient names,
                            // can go ahead and populate the spinner.
                            adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, ingredientListName);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            foodSelection.setAdapter(adapter);


                        } else {
                            // could not access collection
                            Log.d("QueryResult", ":( error getting documents: ", task.getException());
                        }
                    }});

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_meal_plan_fragment, null);
        mealPlanType = view.findViewById(R.id.radioGroup);
        currentDate = view.findViewById(R.id.meal_fragment_date);
        foodSelection = view.findViewById(R.id.addMealItemName);
        servings = view.findViewById(R.id.addMealServingsCount);
        finishEating = view.findViewById(R.id.addMealPlanDatePicker);

        updateMealType(IngredientStorage, "description");
        Bundle givenDate = getArguments(); // grab the start date
        // (in milliseconds)
        assert givenDate != null;
        String timeStampDate = givenDate.getString("Date");
        assert timeStampDate != null;
        // we store using millisecond times
        // for display, must convert millisecond to year-month-day

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.valueOf(timeStampDate));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

        if (month.length() == 1) month = "0" + month;
        if (day.length() == 1) day = "0" + day;

        String date = day+"-"+month+"-"+year;

        currentDate = view.findViewById(R.id.meal_fragment_date);
        currentDate.setText(date);

        mealPlanType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i){
                    case R.id.ingredientRadioButton:
                        updateMealType(IngredientStorage, "description");

                        break;
                    case R.id.recipeRadioButton:
                        updateMealType(RecipeStorage, "title");
                        break;

                }
            }
        });
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getContext());


        return builder
                .setView(view)
                .setTitle("Add Meal Plan Item")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        servings = view.findViewById(R.id.addMealServingsCount);
                        String servingInputValue = servings.getText().toString();


//                        // validate input!
                        if (servingInputValue.length() == 0) {
                            Toast.makeText(getActivity(), "Servings cannot be empty", Toast.LENGTH_LONG).show();
                            return;
                        }

                        int numberOfServings = Integer.parseInt(servingInputValue);
                        foodSelection = view.findViewById(R.id.addMealItemName);
                        String foodSelectionName = foodSelection.getSelectedItem().toString();


                        // get date until which this recipe will be eaten
                        // also convert to string
                        finishEating = view.findViewById(R.id.addMealPlanDatePicker);
                        int year = finishEating.getYear();
                        int month = finishEating.getMonth() + 1;
                        int day = finishEating.getDayOfMonth();

                        // grab this date in epoch time
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, day);
                        Long endTime = calendar.getTimeInMillis();

                        if (endTime < Long.parseLong(timeStampDate)) {
                            Toast.makeText(getActivity(), "Invalid end date", Toast.LENGTH_LONG).show();
                            return;
                        }




                        // get ingredients
                        int selectedOptionId = mealPlanType.getCheckedRadioButtonId();
                        RadioButton selectedButton = mealPlanType.findViewById(selectedOptionId);
                        String selectedOption = selectedButton.getText().toString();
                        assert (selectedOption.equals("Ingredient") || selectedOption.equals("Recipe"));


                        ArrayList<ShoppingListItem> ingredients = new ArrayList<>();

                        System.out.println("SELECTED OPTION ISSSS" + selectedOption);
                        if (selectedOption.equals("Ingredient")){
                            IngredientStorage.whereEqualTo("description", foodSelectionName).get().addOnCompleteListener(
                                    new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                try {
                                                    // grab first result for simplicity
                                                    Map<String, Object> doc = task.getResult().getDocuments().get(0).getData();
                                                    // save this ingredient as a ShoppingListItem
                                                    ingredients.add(new ShoppingListItem((String) doc.get("description"), numberOfServings, (String) doc.get("measurementUnit"), (String) doc.get("category")));
                                                } catch (Exception e) {

                                                    System.out.println("EXCEPTION OCCURRED! " + e);
                                                }


                                            }

                                            listener.onSubmitPressed(
                                                    new MealPlanItem(
                                                            timeStampDate, String.valueOf(endTime), foodSelectionName, Long.valueOf(numberOfServings), ingredients
                                                    ));

                                        };
                                    });


                        } else {
                            // query firebase for recipe based on name; (first occurrence)
                            // grab its list of ingredients
                            // & set the current list of ingredients to that list
                            RecipeStorage.whereEqualTo("title", foodSelectionName).get().addOnCompleteListener(
                                    new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                try {
                                                    // grab first result for simplicity
                                                    Map<String, Object> doc = task.getResult().getDocuments().get(0).getData();
                                                    ArrayList<Map<String, Object>> ingredientsArray = (ArrayList<Map<String, Object>>) doc.get("ingredients");
                                                    for (int k = 0; k < ingredientsArray.size(); k++){
                                                        // go through each ingredient that the recipe has
                                                        // access map fields using attributes of recipeIngredient
                                                        Map<String, Object> currentIngredient = ingredientsArray.get(k); // get current value
                                                        String itemName = (String) currentIngredient.get("description");
                                                        Integer amount = Integer.valueOf((String) currentIngredient.get("amount"));
                                                        String unit = (String) currentIngredient.get("unit");
                                                        String category = (String) currentIngredient.get("category");
                                                        ingredients.add(new ShoppingListItem(itemName, amount, unit, category));
                                                    }

                                                } catch (Exception e) {

                                                }
                                            }


                                            listener.onSubmitPressed(
                                                    new MealPlanItem(
                                                            timeStampDate, String.valueOf(endTime), foodSelectionName, Long.valueOf(numberOfServings), ingredients
                                                    ));
                                        };
                                    });



                        }


                        System.out.println("INGREDIENTS!" + ingredients);
                    }
                }).create();



    }

}