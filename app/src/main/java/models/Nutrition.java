package models;

import java.util.Map;

public class Nutrition {
    long calories, fat, cholesterol, carbohydrates, protein, sodium;

    public Nutrition(  long calories, long fat, long cholesterol, long carbohydrates, long protein, long sodium) {
        this.calories = calories;
        this.fat = fat;
        this.cholesterol = cholesterol;
        this.carbohydrates = carbohydrates;
        this.protein = protein;
        this.sodium = sodium;
    }

    public static  Nutrition fromMap(Map<String, Object> data){

        return new Nutrition(
                (long) data.get("calories"),
                (long) data.get("fat"),
                (long) data.get("cholesterol"),
                (long) data.get("carbohydrates"),
                (long) data.get("protein"),
                (long) data.get("sodium")
                );
    }

    public long getCalories() {
        return calories;
    }

    public long getFat() {
        return fat;
    }

    public long getCholesterol() {
        return cholesterol;
    }

    public long getCarbohydrates() {
        return carbohydrates;
    }

    public long getProtein() {
        return protein;
    }

    public long getSodium() {
        return sodium;
    }

    @Override
    public String toString() {
        return "Nutrition{" +
//                 "recipeID= " + recipeID + '\'' +
                ", calories=" + calories +
//                ", fat=" + fat +
//                ", cholesterol=" + cholesterol +
//                ", carbohydrates=" + carbohydrates +
//                ", protein=" + protein +
//                ", sodium=" + sodium +
                '}';
    }
}
