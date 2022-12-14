package com.CMPUT301F22T26.foodegy;

import android.content.Context;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Singleton class to handle interacting with the database, adding/editing/deleting
 *   ingredients/recipes/meal plans
 */
public class DatabaseManager {
    private static DatabaseManager dbm;
    private static String android_id;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    // different collections for each user
    private CollectionReference RecipesCollection;
    private CollectionReference IngredientStorage;
    private CollectionReference MealPlans;

    // firebase storage to hold images for recipes
    private StorageReference userFilesRef = FirebaseStorage.getInstance().getReference().child(android_id);


    private DatabaseManager() {
        // constructor is PRIVATE!! use getInstance() to get the manager
        RecipesCollection = firestore.collection("users")
                .document(android_id).collection("Recipes");
        IngredientStorage = firestore.collection("users")
                .document(android_id).collection("IngredientStorage");
        MealPlans = firestore.collection("users")
                .document(android_id).collection("MealPlans");
    }
    public static void setAndroid_id(String id) {
        android_id = id;
        Log.d("DatabaseManager", "Device id is: "+android_id);
    }
    public static DatabaseManager getInstance() {
        if (android_id != null) {
            if (dbm == null) {
                dbm = new DatabaseManager();
            }
            return dbm;
        }
        else {
            throw new NullPointerException("android_id cannot be null!");
        }
    }
    // getters for collection references
    public CollectionReference getRecipesCollection() {
        return RecipesCollection;
    }
    public CollectionReference getIngredientStorageCollection() {
        return IngredientStorage;
    }
    public CollectionReference getMealPlansCollection() {
        return MealPlans;
    }
    public StorageReference getUserFilesRef() {
        return userFilesRef;
    }

    // INGREDIENT THINGS
    /**
     * Adds an ingredient to the Ingredient collection
     * @param newIngredient
     *  The ingredient to be added
     */
    public void addIngredientToDatabase(StorageIngredient newIngredient) {
        IngredientStorage
                .add(newIngredient)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // get the auto-generated ID for the item
                        Log.d("DatabaseManager",
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
     * Edits a StorageIngredient in the database
     * @param ingredient
     *  The new ingredient with edited attributes
     */
    public void editIngredientInDatabase(StorageIngredient ingredient) {
        //ETHAN: MAYBE the ingredient already has an ID in it??? and you dont need to put it in separately
        String id = ingredient.getId();
        IngredientStorage
                .document(id)
                .set(ingredient)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("DatabaseManager",
                                "Sucessfully edited "+id);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("DatabaseManager",
                                "Failed to edit "+id);
                    }
                })
        ;
    }

    /**
     * Deletes an ingredient form a user's IngredientStorage
     * @param id
     *  id of ingredient to be deleted
     */
    public void deleteIngredientFromDatabase(String id) {
        // Note: we do not need to remove the ingredient from the ingredient list or adapter, that
        //   is handled with the IngredientStorage.addSnapshotListener
        IngredientStorage
                .document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("DatabaseManager", "Succesfully deleted "+id);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("DatabaseManager", "Failed to delete "+id);
                    }
                });
    }

    /**
     * Adds a recipe to the Firebase
     * @param recipe
     *  The recipe to add
     */
    public void addRecipeToDatabase(Recipe recipe, Uri selectedImage) {
        // Add recipe to firestore
        RecipesCollection.add(recipe)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("DatabaseManager", "Successfully added recipe "+documentReference.getId());
                        recipe.setId(documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("DatabaseManager", "Could not add recipe, "+e);
                    }
                });

        // add the image (if it exists) to firebase storage
        if (selectedImage == null) {
            return;
        }
        String imageFilename = recipe.getImageFileName();
        StorageReference fileRef = userFilesRef.child(imageFilename);
        // Upload the image to firebase storage
        fileRef.putFile(selectedImage)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("DatabaseManager", "Successfully uploaded image " + imageFilename);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("DatabaseManager", "Failed to upload image, " + e);
                    }
                });
    }

    /**
     * Deletes a Recipe object from the firebase
     * @param id id of Recipe to delete
     * @param imageFileName the name of the image that goes with each recipe
     */
    public void deleteRecipeFromDatabase(String id, String imageFileName) {
        // delete the recipe! from the firestore!
        RecipesCollection.document(id).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("DatabaseManager", "Successfully deleted recipe "+id);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("DatabaseManager", "Failed to delete recipe "+id);
                    }
                });

        // delete the image from the firebase storage
        if (imageFileName != null && !"".equals(imageFileName)) {
            userFilesRef.child(imageFileName).delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("DatabaseManager", "Successfully deleted image " + imageFileName);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("DatabaseManager", "Failed to delete image " + imageFileName);
                        }
                    });
        }
    }

    // MEAL PLAN METHODS

    /**
     * Add meal plan item to the MealPlans collection
     * @param mealPlanItem the meal plan item to be added to the collection
     */
    public void addMealPlanToDatabase(MealPlanItem mealPlanItem){
        MealPlans
                .add(mealPlanItem)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("MealPlanActivity", "Added meal plan item" +mealPlanItem.getName());
                    }})
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("DatabaseManager", "Failed to add Meal Plan to database"+e);
                        }
                    });
        }
    


    /**
     * Edit an existing storage ingredient in firebase
     * @param id the firebase id of storage ingredient that's being modified
     * @param ingredients array of existing ingredients
     */

    public void editRecipeIngredient(String id, ArrayList<RecipeIngredient> ingredients) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("ingredients", ingredients);
        RecipesCollection.document(id)
                .update("ingredients",ingredients)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("DatabaseManager", "Successfully edited recipe ingredients for "+id);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("DatabaseManager", "Failed to update recipe ingredients for "+id+": "+e);
                    }
                });
    }
}
