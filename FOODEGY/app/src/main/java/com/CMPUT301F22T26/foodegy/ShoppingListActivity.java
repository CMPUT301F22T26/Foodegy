package com.CMPUT301F22T26.foodegy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.CMPUT301F22T26.foodegy.databinding.ActivityRecipesBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * This activity will allow the user to navigate and manage their shopping cart, as well as mark
 * items as purchased and add them to storage
 */
public class ShoppingListActivity extends AppCompatActivity implements AddIngredientFragment.OnFragmentInteractionListener {
    //list view related variables
    private ListView shoppingListView;
    private ArrayAdapter<ShoppingListItem> shoppingListItemArrayAdapter;
    private ArrayList<ShoppingListItem> shoppingListData;

    private NavigationBarView bottomNavBar;

    ArrayList<StorageIngredient> storageIngredientData;
    ArrayList<MealPlanItem> mealPlanData;

    // initialize the firebase
    final private String android_id = "TEST_ID";
    final private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    final private CollectionReference IngredientStorage = firestore.collection("users")
            .document(android_id).collection("IngredientStorage");
    final private CollectionReference MealPlans = firestore.collection("users")
            .document(android_id).collection("MealPlans");

    //giving some inital values
    // since for now, the ShoppingList is not hooked up to the MealPlan list
    List<String> names = Arrays.asList("Apple", "Bread", "Cream cheese");
    List<Integer> amounts = Arrays.asList(3, 3, 2);
    List<String> units = Arrays.asList("apples", "loaves", "oz");


    List<String> cates = Arrays.asList("Vegetable", "Grain", "Dairy");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_list_activity);

        shoppingListData = new ArrayList<ShoppingListItem>();
        shoppingListView = findViewById(R.id.shopping_list);

        shoppingListItemArrayAdapter = new ShoppingList(this, shoppingListData);
        shoppingListView.setAdapter(shoppingListItemArrayAdapter);

        // populate shoppingListData

        // first, query ingredients to find which ingredients user currently has in storage
        storageIngredientData = new ArrayList<StorageIngredient>();
        mealPlanData = new ArrayList<MealPlanItem>();
        IngredientStorage.get().addOnCompleteListener(
                // query ingredient storage for all of the documents it currently contains
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot doc : task.getResult()){
                                try {
                                    String id = doc.getId();
                                    Map<String, Object> data = doc.getData();
                                    String description = (String)data.get("description");
                                    String bestBefore = (String)data.get("bestBeforeDate");
                                    String location = (String)data.get("location");
                                    int amount = doc.getLong("amount").intValue();
                                    String measurementUnit = (String)doc.get("measurementUnit");
                                    //int unitCost = doc.getLong("unitCost").intValue();
                                    String category = (String)data.get("category");
                                    StorageIngredient newIngredient = new StorageIngredient(
                                            description,
                                            bestBefore,
                                            location,
                                            amount,
                                            measurementUnit,
                                            category
                                    );
                                    newIngredient.setId(id);
                                    storageIngredientData.add(newIngredient);
                                } catch (Exception e) {
                                    Log.d("Query", "Error reading document", e);
                                }
                            }

                            // run second query here
                            String timeStampDate = String.valueOf(System.currentTimeMillis());

                            MealPlans.whereLessThanOrEqualTo("startDate", timeStampDate).orderBy("startDate").get().addOnCompleteListener(
                                    new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()){
                                                for (DocumentSnapshot doc : task.getResult()){
                                                    try {
                                                        // now must manually filter by endDate
                                                        Map data = doc.getData();
                                                        String endDate = (String) data.get("endDate");
                                                        if (Long.parseLong(endDate) >= Long.parseLong(timeStampDate)) {
                                                            String id = (String) data.get("id");
                                                            String startDate = (String) data.get("startDate");
                                                            String name = (String) data.get("name");
                                                            Long servings = (Long) data.get("servings");
                                                            Map ingredients = (Map) data.get("ingredients");

                                                            mealPlanData.add(new MealPlanItem(startDate, endDate, name, servings, ingredients));
                                                        }

                                                    } catch (Exception e) {
                                                        Log.d("Query", "Error reading document", e);
                                                    }
                                                }
                                                System.out.println("FINISHED FETCHING DOCUMENTS" + storageIngredientData + "====" + mealPlanData);
                                                compareIngredientAndMealPlanContents(storageIngredientData, mealPlanData);
                                            } else {
                                                Log.d("Query", "Error getting documents: ", task.getException());
                                            }
                                        }
                                    }


                            );



                            System.out.println("GRABBED INGREDIENT DATA INSIDE QUERY" + storageIngredientData);
                        } else {
                            Log.d("Query", "Error getting documents: ", task.getException());
                        }
                    }

                }



        );

//
//        shoppingListData.add(new ShoppingListItem(names.get(0), amounts.get(0), units.get(0), cates.get(0)));
//        shoppingListData.add(new ShoppingListItem(names.get(1), amounts.get(1), units.get(1), cates.get(1)));
//        shoppingListData.add(new ShoppingListItem(names.get(2), amounts.get(2), units.get(2), cates.get(2)));

        bottomNavBar = (NavigationBarView) findViewById(R.id.bottom_nav);
        bottomNavBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ingredients:
                        startActivity(new Intent(getBaseContext(), IngredientsActivity.class));
                        break;
                    case R.id.meal_plan:
                        startActivity(new Intent(getBaseContext(), MealPlanActivity.class));
                        break;
                    case R.id.shopping_cart:
                        break;
                    case R.id.recipes:
                        startActivity(new Intent(getBaseContext(), RecipesActivity.class));
                        break;

                }

                return false;
            }
        });

        System.out.println("GRABBED INGREDIENT DATA" + storageIngredientData);

    }

    /**
     * Responsible for calling the AddIngredientFragment
     * (this method gets called from inside a Shopping List View when the user
     * marks that particular item as bought).
     * This method will also remove the user's item from the shopping list as it is displayed on screen
     * @param bought the item that has been bought
     */
    public void inflateFragment(ShoppingListItem bought){
        shoppingListData.remove(bought);
        shoppingListItemArrayAdapter.notifyDataSetChanged();
        new AddIngredientFragment(bought).show(getSupportFragmentManager(), "ADD_SHOPPING_LIST_ITEM");
    }


    /**
     * This handles adding the new ingredient onto the Firebase
     * @param newIngredient the new ingredient that we want to store
     */
    @Override
    public void addIngredientToDatabase(StorageIngredient newIngredient) {
        // add an ingredient
        IngredientStorage
                .add(newIngredient)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // get the auto-generated ID for the item
                        Log.d("MainActivity",
                                "Added storage ingredient "+newIngredient.getDescription()+", id="+documentReference.getId());
                        newIngredient.setId(documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MainActivity",
                                "Failed to add storage ingredient "+newIngredient.getDescription());
                    }
                })
        ;

    }
    /**
     * Takes two lists, one containing StorageIngredient objects and another containing MealPlanItem objects,
     * and performs item matching based on
     * @param storageIngredientData ArrayList containing StorageIngredient items (currently in storage)
     * @param mealPlanData ArrayList containing MealPlanItem objects (things the user wishes to cook)
     */
    public void compareIngredientAndMealPlanContents(ArrayList<StorageIngredient> storageIngredientData, ArrayList<MealPlanItem>mealPlanData){

        // generate information for first set of MealPlan ingredients

        for (int i = 0; i < mealPlanData.size(); i++){
            Map<String, ArrayList> mealPlanIngredients = mealPlanData.get(i).getIngredients();
            Object[] keyList = mealPlanIngredients.keySet().toArray();
            for (int k = 0; k < keyList.length; k++){
                // iterate through all ingredients in the mealPlan
                String key = (String)keyList[k];
                ArrayList values = mealPlanIngredients.get(key);
                Integer requiredAmount = 0;
                if (values.get(0) instanceof Long) {
                    requiredAmount = Math.toIntExact((Long) values.get(0));
                } else if (values.get(0) instanceof Double) {
                    requiredAmount = Math.toIntExact(Math.round((Double) values.get(0)));
                }

                // will need to calculate
                for (int j = 0; j < storageIngredientData.size(); j++){
                    // now go through each ingredient and check for matches
                    if (key.equals(storageIngredientData.get(j).getDescription())){
                        // key was found
                        // will need to update required amount of this item
                        requiredAmount -= storageIngredientData.get(j).getAmount();
                    }
                    if (j == (storageIngredientData.size() - 1)){
                        // means we haven't matched the key in our n - 1 iterations
                        // NOT FINISHED!!!
                        if (requiredAmount > 0){
                            shoppingListData.add(new ShoppingListItem(key,  requiredAmount, (String) values.get(1), (String) values.get(2)));

                        }
                    }
                }
            }
        }
        shoppingListItemArrayAdapter.notifyDataSetChanged();
        // for each mealPlan, see what items are NOT in ingredientStorage and append those to list
        // of returned values
    }


    /**
     * There is no editing in shopping list activity so this method is simply overwritten
     * @param ingredient the ingredient the user would be trying to edit
     */
    @Override
    public void onEditPressed(StorageIngredient ingredient) {

    }
}