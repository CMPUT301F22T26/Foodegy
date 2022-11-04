package com.CMPUT301F22T26.foodegy;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
     * Check if getters and setters work with regular data, improper inputs, etc.
     */
    @Test
    void testGetandSet(){
        String[] namesArray = new String[] {"Pizza", "Pepperoni and cream cheese pasta", "Corn", "h", ""};
        String[] datesArray = new String[] { "12-01-2021", "30-10-2002", "---", "", "04-06-2028"};
        String[] locationsArray = new String[] {"Freezer", "Fridge", "Cupboard", "Outside", "Garage"};
        int[] unitCostArray = new int[]{100000, 0, 1, 50, 5};
        int[] amountArray = new int[]{1, 50, 0, 5000, 5};
        String[] category = new String[] {"Vegetable", "Meat", "Dairy", "Meat", ""};

        for (int i = 0; i < 5; i++){
            StorageIngredient newIngredient = new StorageIngredient(namesArray[i], datesArray[i], locationsArray[i], amountArray[i], unitCostArray[i], category[i]);
            assertEquals(newIngredient.getAmount(), amountArray[i]);
            assertEquals(newIngredient.getUnitCost(), unitCostArray[i]);
            assertEquals(newIngredient.getCategory(), category[i]);
            assertEquals(newIngredient.getUnitCost(), unitCostArray[i]);
            assertEquals(newIngredient.getDescription(), namesArray[i]);

            // try setting now
            int new_index = (i + 1) % 5;
            newIngredient.setAmount(amountArray[new_index]);
            newIngredient.setUnitCost(unitCostArray[new_index]);
            newIngredient.setCategory(category[new_index]);
            newIngredient.setBestBeforeDate(datesArray[new_index]);
            newIngredient.setLocation(locationsArray[new_index]);
            newIngredient.setDescription(namesArray[new_index]);


            assertEquals(newIngredient.getAmount(), amountArray[new_index]);
            assertEquals(newIngredient.getUnitCost(), unitCostArray[new_index]);
            assertEquals(newIngredient.getCategory(), category[new_index]);
            assertEquals(newIngredient.getUnitCost(), unitCostArray[new_index]);
            assertEquals(newIngredient.getDescription(), namesArray[new_index]);

        }




    }
}
