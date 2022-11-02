package com.CMPUT301F22T26.foodegy;

import java.io.Serializable;

public class ShoppingListItem implements Serializable {
    /*
    This is a class for an ingredient that is to be bought, it is made when
    an ingredient is needed in meal plan (directly) or in a recipe which is
    in a meal plan(indirectly).
    This shoppingListItem has all the attribute as of ingredient but will only
    show the required on the list
     */
    private String itemName;
    private String description;
    private String bestBeforeDate;
    private String location;
    private String amount;
    private String unitCost;
    private String category;

    ShoppingListItem(String itemName, String description, String bestBeforeDate, String location, String amount, String unitCost, String category){
        this.itemName = itemName;
        this.description = description;
        this.bestBeforeDate = bestBeforeDate;
        this.location = location;
        this.amount = amount;
        this.unitCost = unitCost;
        this.category = category;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBestBeforeDate() {
        return bestBeforeDate;
    }

    public void setBestBeforeDate(String bestBeforeDate) {
        this.bestBeforeDate = bestBeforeDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(String unitCost) {
        this.unitCost = unitCost;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
