package com.CMPUT301F22T26.foodegy;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Singleton class to handle interacting with the database, adding/editing/deleting
 *   ingredients/recipes/meal plans
 */
public class DatabaseManager {
    final private static DatabaseManager dbm = new DatabaseManager();

    private String android_id = "TEST_ID";
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();


    // different collections for each user
    final private CollectionReference RecipesCollection = firestore.collection("users")
            .document(android_id).collection("Recipes");
    final private CollectionReference IngredientStorage = firestore.collection("users")
            .document(android_id).collection("IngredientStorage");
    final private CollectionReference MealPlans = firestore.collection("users")
            .document(android_id).collection("MealPlans");

    // firebase storage to hold images for recipes
    private StorageReference userFilesRef = FirebaseStorage.getInstance().getReference().child(android_id);


    private DatabaseManager() {
        // constructor is PRIVATE!! use getInstance() to get the manager
    }

    public static DatabaseManager getInstance() {
        return dbm;
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
        String imageFilename = recipe.getImageFileName();
        StorageReference fileRef = userFilesRef.child(imageFilename);
        // Upload the image to firebase storage

        if (selectedImage == null) {
            return;
        }

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
    }

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
}
