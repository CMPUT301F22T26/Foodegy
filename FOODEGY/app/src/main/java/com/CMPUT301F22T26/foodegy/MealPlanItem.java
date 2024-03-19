package com.CMPUT301F22T26.foodegy;

import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
/***
 * this is a class which defines a single item constituting a meal plan, a string type is also
 * defined to show that the item is either an ingredient or a recipe.
 * These all items will be stored in a single list and they will be shown according to the
 * date selected from calendar.
 * Here both a 'StorageIngredient' and 'Recipe' object is defined in each item. While instantiation,
 * if item is StorageIngredient, 'StorageIngredient' object is initialised and 'Recipe' object is
 * initialised to a null value (vice versa for 'Recipe' item).
 */
public class MealPlanItem {

    private String startDate;
    private String endDate;
    private Long servings;
    private String id;
    private String name;
    private ArrayList<ShoppingListItem> ingredients;

    /**
     * Initialize an instance of a MealPlanItem
     * @param startDate (String) unix time, when the MealPlan is supposed to begin
     * @param endDate (String) unix time, when the user plans to finish eating their MealPlanItem
     * @param name (String) Meal Plan Item name
     * @param servings (Long) Number of servings the user is planning to make
     * @param ingredients Ingredients and their respective amounts, making up this
     *                    MealPlanItem
     */
    public MealPlanItem( String startDate, String endDate, String name, Long servings, ArrayList<ShoppingListItem> ingredients){
        this.startDate = startDate;
        this.endDate = endDate;
        this.name = name;
        this.servings = servings;
        this.ingredients = ingredients;
    }

    /**
     * Get MealPlanItem start time (unix, String)
     * @return the time when user plans to start eating
     */
    public String  getStartDate() {

        return startDate;
    }

    /**
     * Set a new MealPlanItem start time (unix, String)
     * @param date the new time this MealPlanItem will be made
     */
    public void setStartDate(String date) {

        this.startDate = date;
    }

    /**
     * Set a new MealPlanItem end time (unix, String)
     * @param date the new time this MealPlanItem will be done
     */
    public void setEndDate(String date) {

        this.endDate = date;
    }

    /**
     * Get MealPlanItem end time (unix, String)
     * @return the time when user plans to finish eating
     */
    public String  getEndDate() {


        return endDate;
    }

    /**
     * Get MealPlanItem's number of servings (Long)
     * @return how many servings will be made
     */
    public Long getServings() {

        return servings;
    }

    /**
     * Set MealPlanItem's number of servings
     * @param servings (Long) how many servings will be made
     */
    public void setServings(Long servings) {

        this.servings = servings;
    }

    /**
     * get MealPlanItem's id in the FireBase
     * @return the Id(String) of the MealPlanItem
     */
    public String getId(){ return this.id; }

    /**
     * set the MealPlanItem's id in the FireBase
     * @param newId (String) the new id of the MealPlanItem
     */
    public void setId(String newId) {this.id = newId; }

    /**
     * get the MealPlanItem's name
     * @return the name (String)
     */
    public String getName(){
        return name;
    }

    /**
     * set the MealPlanItem's name
     * @param name (String) name of item
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * get the ingredients, mapped to their respective amounts, of the MealPlanItem
     * @return Map: ingredients and their amounts
     */
    public ArrayList<ShoppingListItem> getIngredients(){return ingredients; }

    /**
     * set a new bunch of ingredients of this MealPlanItem
     * @param newIngredients: the new ingredients, and their amounts
     */
    public void setIngredients(ArrayList<ShoppingListItem> newIngredients){this.ingredients = newIngredients; }

}

