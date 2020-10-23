package com.company;

import java.sql.*;
import java.util.Date;

public class Ingredient {
    String name;
    String unit;
    Integer quantity;
    String location;
    String expiration;

    public void AddIngredient(Connection con){
        try {
            String needFridge = "";
            if (!location.toUpperCase().equals("PANTRY")) needFridge = "yes";
            else needFridge = "no";

            String query = "insert into ingredient (ingname, expdate, needfridge, unit) values ('" + name + "','" + expiration + "','" + needFridge + "','" + unit + "')";
            System.out.println("Adding ingredient (" + name + "," + expiration + "," + needFridge + "," + unit + ") to ingredient table");
            Statement stmt;
            stmt = con.createStatement();
            int rs = stmt.executeUpdate(query);

            //add the storing relation to the appropriate table
            //store in pantry/fridge 0 by default

            query = "insert into " + location + "stores (" + location + "id, ingname, " + location + "quant) values ('6','" +
                    name + "','" + quantity + "')";
            System.out.println("Adding relation (6," + name + "," + quantity + ") to " + location + "stores table");
            stmt = con.createStatement();
            rs = stmt.executeUpdate(query);
        } catch (Exception e){
            System.out.println(e);
        }
    }
}
