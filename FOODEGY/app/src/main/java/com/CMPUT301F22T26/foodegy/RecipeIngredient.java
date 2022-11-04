package com.CMPUT301F22T26.foodegy;


/**
 * RecipeIngredient is used when the "quick add ingredient" from AddRecipeActivity is used
 */

public class RecipeIngredient {

    private String description;
    private String category;
    private String amount;
    private String unit;

    /**
     * Initialize a RecipeIngredient!
     * @param description the name, in one or two words
     * @param category the category of this ingredient
     * @param amount how much of this ingredient is required
     * @param unit the price of this ingredient
     */
    public RecipeIngredient(String description, String category, String amount, String unit) {
        this.description = description;
        this.category = category;
        this.amount = amount;
        this.unit = unit;
    }

    /**
     * Get the RecipeIngredient's brief description
     * @return the brief description, as a string
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set this RecipeIngredient's description
     * @param description the new description we wish to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get this RecipeIngredient's category
     * @return the category, as a string
     */
    public String getCategory() {
        return category;
    }

    /**
     * Set this RecipeIngredient's category
     * @param category the new category we wish this RecipeIngredient to have
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Get the amount of this RecipeIngredient
     * @return the amount value, as a string
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Set a new amount of this RecipeIngredient
     * @param amount the new amount value, as a string
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }

    /**
     * Get the unit cost of this RecipeIngredient, as a string
     * @return the unit cost
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Set a new unit cost for this RecipeIngredient, as a string
     * @param unit the new unit cost
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }
}
