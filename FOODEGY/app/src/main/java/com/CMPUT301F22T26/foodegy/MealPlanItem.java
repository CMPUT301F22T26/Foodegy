package com.CMPUT301F22T26.foodegy;

import android.widget.RadioGroup;

import java.util.Calendar;
import java.util.Map;

public class MealPlanItem {
    /***
     * this is a class which defines a single item constituting a meal plan, a string type is also
     * defined to show that the item is either an ingredient or a recipe.
     * These all items will be stored in a single list and they will be shown according to the
     * date selected from calendar.
     * Here both a 'StorageIngredient' and 'Recipe' object is defined in each item. While instantiation,
     * if item is StorageIngredient, 'StorageIngredient' object is initialised and 'Recipe' object is
     * initialised to a null value (vice versa for 'Recipe' item).
     */
    private String startDate;
    private String endDate;
    private Long servings;
    private String id;
    private String name;
    private Map ingredients;

    public MealPlanItem( String startDate, String endDate, String name, Long servings, Map ingredients){
        this.startDate = startDate;
        this.endDate = endDate;
        this.name = name;
        this.servings = servings;
        this.ingredients = ingredients;
    }

    public String  getStartDate() {

        return startDate;
    }

    public void setStartDate(String date) {

        this.startDate = date;
    }

    public void setEndDate(String date) {

        this.endDate = date;
    }

    public String  getEndDate() {


        return endDate;
    }

    public Long getServings() {

        return servings;
    }

    public void setServings(Long servings) {

        this.servings = servings;
    }

    public String getId(){ return this.id; }
    public void setId(String newId) {this.id = newId; }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Map getIngredients(){return ingredients; }
    public void setIngredients(Map newIngredients){this.ingredients = newIngredients; }

}

