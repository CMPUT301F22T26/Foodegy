package com.CMPUT301F22T26.foodegy;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * RecipeIngredient is used when the "quick add ingredient" from AddRecipeActivity is used
 */

public class RecipeIngredient implements Parcelable {

    private String description;
    private String category;
    private String unit;
    private String amount;

    /**
     * Initialize a RecipeIngredient!
     * @param description the name, in one or two words
     * @param category the category of this ingredient
     * @param unit the price of this ingredient
     */
    public RecipeIngredient(String description, String category, String unit, String amount) {
        this.description = description;
        this.category = category;
        this.unit = unit;
        this.amount = amount;
    }

    protected RecipeIngredient(Parcel in) {
        description = in.readString();
        category = in.readString();
        unit = in.readString();
        amount = in.readString();
    }

    public static final Creator<RecipeIngredient> CREATOR = new Creator<RecipeIngredient>() {
        @Override
        public RecipeIngredient createFromParcel(Parcel in) {
            return new RecipeIngredient(in);
        }

        @Override
        public RecipeIngredient[] newArray(int size) {
            return new RecipeIngredient[size];
        }
    };

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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(category);
        dest.writeString(unit);
    }
}
