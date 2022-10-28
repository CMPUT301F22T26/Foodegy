package com.CMPUT301F22T26.foodegy;

import android.media.Image;
import android.net.Uri;

import java.util.ArrayList;

/**
 * The recipe class is used to hold the user input data of the recipe which the user adds on when using the Recipe view and edit
 * feature of the app.
 */
public class Recipe {

    public Recipe(String title, String hours, String minutes, String servingValue, String category,
                  String amount, Uri recipeImage, String comments, ArrayList<RecipeIngredient> ingredients) {
        this.title = title;
        this.hours = hours;
        this.minutes = minutes;
        this.servingValue = servingValue;
        this.category = category;
        this.amount = amount;
        this.recipeImage = recipeImage;
        this.comments = comments;
        this.ingredients = ingredients;
    }

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

    public Uri getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(Uri recipeImage) {
        this.recipeImage = recipeImage;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id;}


    private String title;
    private String hours;
    private String minutes;
    private String servingValue;
    private String category;
    private String amount;
    private Uri recipeImage;
    private String comments;
    private ArrayList<RecipeIngredient> ingredients;
    private String id;

    public ArrayList<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }
}
