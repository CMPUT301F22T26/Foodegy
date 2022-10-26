package com.CMPUT301F22T26.foodegy;

public class StorageIngredient {
    private String description;
    private String bestBeforeDate;
    private String location;
    private int amount;
    private int unitCost;
    private String category;
    private String id;

    StorageIngredient(String description, String bestBeforeDate, String location, int amount, int unitCost, String category){
        this.description = description;
        this.bestBeforeDate = bestBeforeDate;
        this.location = location;
        this.amount = amount;
        this.unitCost = unitCost;
        this.category = category;
        // dont need id in the constructor
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBestBeforeDate() {
        return this.bestBeforeDate;
    }

    public void setBestBeforeDate(String bestBeforeDate) {
        this.bestBeforeDate = bestBeforeDate;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getUnitCost() {
        return this.unitCost;
    }

    public void setUnitCost(int unitCost) {
        this.unitCost = unitCost;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {return this.id;}

    public void setId(String id) {this.id = id;}
}
