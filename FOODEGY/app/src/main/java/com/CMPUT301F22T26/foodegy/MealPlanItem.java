package com.CMPUT301F22T26.foodegy;

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
    private String date;
    private int month;
    private int year;
    private FoodItems foodItem; //abstract class object
    private int servings;

    public MealPlanItem(String date, int month, int year, FoodItems foodItem, int servings){
        this.date = date;
        this.month = month;
        this.year = year;
        this.foodItem = foodItem;
        this.servings = servings;
    }

    public String  getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getFoodItem() {
        String name = foodItem.getName();
        return (name);
//        return foodItem;
    }

    public void setFoodItem(FoodItems foodItem) {
        this.foodItem = foodItem;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }
}
