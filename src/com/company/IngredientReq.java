package com.company;

import java.util.Date;

public class IngredientReq {
    String name;
    String unit;
    Integer quantity;

    public Ingredient ToIngredient(){
        Ingredient newI = new Ingredient();
        newI.name = name;
        newI.unit = unit;
        newI.quantity = 0;
        newI.location = "pantry";
        Date now = new Date(System.currentTimeMillis());
        newI.expiration = now.getYear() + "-" + now.getMonth() + "-" + now.getDay();
        return newI;
    }
}
