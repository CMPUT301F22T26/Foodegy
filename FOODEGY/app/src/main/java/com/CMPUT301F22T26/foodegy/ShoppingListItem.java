package com.CMPUT301F22T26.foodegy;

public class ShoppingListItem {
    /**
    This is a class for an ingredient that is to be bought, it is made when
    an ingredient is needed in meal plan (directly) or in a recipe which is
    in a meal plan(indirectly).
    This shoppingListItem has all the attribute as of ingredient but will only
    show the required on the list
     **/
    private String itemName;
    private Integer amount;
    private Integer unitCost;
    private String category;

    ShoppingListItem(String itemName, Integer amount, Integer unitCost, String category){
        this.itemName = itemName;
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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(Integer unitCost) {
        this.unitCost = unitCost;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
