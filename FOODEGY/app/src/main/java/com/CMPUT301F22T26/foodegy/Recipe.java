package com.CMPUT301F22T26.foodegy;

import android.media.Image;

/**
 * The recipe class is used to hold the user input data of the recipe which the user adds on when using the Recipe view and edit
 * feature of the app.
 */
public class Recipe {

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public String getServingValue() {
        return servingValue;
    }

    public void setServingValue(String servingValue) {
        this.servingValue = servingValue;
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

    public Image getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(Image recipeImage) {
        this.recipeImage = recipeImage;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }


    private String title;
    private String hours;
    private String minutes;
    private String servingValue;
    private String category;
    private String amount;
    private Image recipeImage;
    private String comments;
/*    private ArrayList<Ingredients> ingredients;

    public ArrayList<Ingredients> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredients> ingredients) {
        this.ingredients = ingredients;
    }*/
}
