package com.CMPUT301F22T26.foodegy;


/**
 * RecipeIngredient is used when the "quick add ingredient" from AddRecipeActivity is used
 */
public class RecipeIngredient{

    private String description;
    private String category;
    private String amount;
    private String unit;

    public RecipeIngredient(String description, String category, String amount, String unit) {
        this.description = description;
        this.category = category;
        this.amount = amount;
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
