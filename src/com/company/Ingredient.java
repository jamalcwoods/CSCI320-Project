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
            String query = "SELECT * FROM ingredient WHERE ingname='" + name + "'";
            Statement stmt = con.createStatement();
            ResultSet rsin = stmt.executeQuery(query);
            if(!rsin.next()) {
                String needFridge = "";
                if (!location.toUpperCase().equals("PANTRY")) needFridge = "yes";
                else needFridge = "no";

                query = "insert into ingredient (ingname, expdate, needfridge, unit) values ('" + name + "','" + expiration + "','" + needFridge + "','" + unit + "')";
                stmt = con.createStatement();
                int rs = stmt.executeUpdate(query);

                //add the storing relation to the appropriate table
                //store in pantry/fridge 0 by default

                query = "insert into " + location + "stores (" + location + "id, ingname, " + location + "quant) values ('6','" + name + "','" + quantity + "')";
                stmt = con.createStatement();
                rs = stmt.executeUpdate(query);
            } else {
                if (location.equals("fridge")) {//get the total quantity in all pantries
                    query = "update fridgestores set fridgequant = fridgequant + " + quantity + " where ingname='" + name + "'";
                    stmt = con.createStatement();
                    int rsc = stmt.executeUpdate(query);
                } else {
                    query = "update pantrystores set pantryquant = pantryquant + " + quantity + " where ingname='" + name + "'";
                    stmt = con.createStatement();
                    int rsc = stmt.executeUpdate(query);
                }
            }
        } catch (Exception e){
            System.out.println(e);
        }
    }
}
