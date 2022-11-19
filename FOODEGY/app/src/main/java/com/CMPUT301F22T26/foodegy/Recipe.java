package com.CMPUT301F22T26.foodegy;

import android.media.Image;
import android.net.Uri;

import java.util.ArrayList;

/**
 * The recipe class is used to hold the user input data of the recipe which the user adds on when using the Recipe view and edit
 * feature of the app.
 */

public class Recipe {
    private String title;
    private String hours;
    private String minutes;
    private String servingValue;
    private String category;
    private Uri recipeImage;
    private String comments;
    private ArrayList<RecipeIngredient> ingredients;
    private String id;
    private int imageId;
    private String imageFileName;


    /**
     * temp constructor to test using a file
     * @return
     */
    public Recipe(String title, String hours, String minutes, String servingValue, String category,
                  String imageFileName, String comments, ArrayList<RecipeIngredient> ingredients) {

        this.title = title;
        this.hours = hours;
        this.minutes = minutes;
        this.servingValue = servingValue;
        this.category = category;
        this.imageFileName = imageFileName;
        this.comments = comments;
        this.ingredients = ingredients;
    }

    /**
     * Get Recipe's title
     * @return (String) title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set Recipe's title
     * @param title new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get Recipe's hour prep time
     * @return hours (String) -- time needed to make it
     */
    public String getHours() {
        return hours;
    }

    /**
     * Set Recipe's hour prep time
     * @param hours new prep time, passed as a string
     */
    public void setHours(String hours) {
        this.hours = hours;
    }

    /**
     * get the minute part of Recipe's prep time
     * @return how many minutes (modulo 60) it takes to make the Recipe
     */
    public String getMinutes() {
        return minutes;
    }

    /**
     * Set the minute part of the Recipe's prep time
     * @param minutes how many minutes (modulo 60) it takes to make the Recipe
     */
    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    /**
     * Get how many servings this Recipe produces
     * @return (String) number of servings
     */
    public String getServingValue() {
        return servingValue;
    }

    /**
     * Set how many servings this Recipe produces
     * @param servingValue (String) new number of servings
     */
    public void setServingValue(String servingValue) {
        this.servingValue = servingValue;
    }

    /**
     * Get which category this recipe belongs to
     * @return the belonging category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Set which category this recipe belongs to
     * @param category the new category of the recipe
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Get URI of Recipe's image
     * @return the Image URI
     */
    public Uri getRecipeImage() {
        return recipeImage;
    }

    /**
     * Set new URI as the Image of this Recipe
     * @param recipeImage the new Image URI
     */
    public void setRecipeImage(Uri recipeImage) {
        this.recipeImage = recipeImage;
    }

    /**
     * Get comments that user has about the Recipe
     * @return the user comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * Set new comments on a Recipe
     * @param comments the new set of comments
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * Get a Recipe's Firebase ID
     * @return the Id, in string form, of this Recipe in Firebase
     */
    public String getId() { return id; }

    /**
     * Set a Recipe's Firebase ID
     * @param id the new Firebase ID, in String form
     */
    public void setId(String id) { this.id = id;}

    /**
     * Get the name of the image file that this Recipe contains
     * @return the file name, as a string
     */
    public String getImageFileName() {return imageFileName;}

    /**
     * Set this recipe's image file name
     * @param imageFileName the new image file name we wish this recipe to have
     */
    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    /**
     * get the id of this recipe's image
     * @return the id, as a string
     */
    public int getImageId() {
        return imageId;
    }

    /**
     * get what RecipeIngredients make up this Recipe
     * @return the arrayList of RecipeIngredients
     */
    public ArrayList<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    /**
     * set a new List of RecipeIngredients for this Recipe
     * @param ingredients the new bunch of ingredients
     */
    public void setIngredients(ArrayList<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }
}
