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
    private String measurementUnit;
    private String category;

    /**
     * Initialize a shopping list item
     * @param itemName the brief name or description
     * @param amount the amount of the item, as an integer
     * @param unit is the unit in which item is measured, as a string
     * @param category the category of the item, as a String
     */
    ShoppingListItem(String itemName, Integer amount,String unit, String category){
        this.itemName = itemName;
        this.amount = amount;
        this.measurementUnit = unit;
        this.category = category;
    }

    /**
     * Get the name of the shopping list item
     * @return the name, as a string
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * Set a new name to the Shopping list item
     * @param itemName the new name we want to give our item
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * Get how much of an item is required
     * @return the amount, as an Integer
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * Set a new amount to this shopping list item
     * @param amount the amount, as an Integer
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
     * Get the units in which the item is measured
     * @return the unit, as a String
     */
    public String getMeasurementUnit() {
        return measurementUnit;
    }

    /**
     * Set the unit cost of this item
     * @param measurementUnit the new measurement unit
     */
    public void setMeasurementUnit(String measurementUnit) {
        this.measurementUnit = measurementUnit;
    }

    /**
     * Get this shopping list item's category
     * @return the category, as a string
     */
    public String getCategory() {
        return category;
    }

    /**
     * Set the category of the shopping list item
     * @param category the new category, as a string
     */
    public void setCategory(String category) {
        this.category = category;
    }
}
