package com.CMPUT301F22T26.foodegy;

public abstract class FoodItems {
    private String name;
//    name = "food";

    public FoodItems(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

}
