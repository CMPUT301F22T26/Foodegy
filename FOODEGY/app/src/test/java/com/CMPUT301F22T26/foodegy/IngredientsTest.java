package com.CMPUT301F22T26.foodegy;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import io.grpc.Context;

public class IngredientsTest {

    /**
     * Create a random storage ingredient for testing purposes
     * @return the mockStorageIngredient
     */
    private StorageIngredient mockStorageIngredient(){
        return new StorageIngredient("Olives", "11-05-2022", "Fridge", 4, 5, "Vegetable");
    }

    /**
     * Create an IngredientList for storing information about the ingredients
     * @return the Mock Ingredient List
     */
    private IngredientList mockIngredientList(){
        ArrayList<StorageIngredient> ingredientData = new ArrayList<StorageIngredient>;
        ingredientData.add(mockStorageIngredient());
        ingredientData.add(new StorageIngredient("pickles", "12-01-2085", "Fridge", 2, 6, "Vegetable"));
        IngredientList ingredientList = new IngredientList(mockIngredientList().getContext(), ingredientData);

    }

    /**
     * Check if getters and setters work with regular data, improper inputs, etc.
     */
    @Test
    void testGetandSet(){

    }
}
