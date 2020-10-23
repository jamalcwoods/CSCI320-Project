package com.company;

import java.sql.*;

public class Recipe {
    String name;
    String author;
    String backstory;
    Integer duration;
    Step[] steps;

    public void AddRecipe(Connection con){
        System.out.println("Creating Recipe...");
        try {
            Statement stmt = con.createStatement();
            ResultSet rs;
            String query = "";
            Integer hours = (int)(Math.ceil(duration/60));
            Integer minutes = duration % 60;


            query = "insert into recipe (recname,backstory,maketime,timesmade) values ('"+ name + "','" + backstory + "','" + hours.toString() +":" + minutes.toString() + ":00',0)";
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            for (int i = 0; i < steps.length; i++) {
                query = "insert into step (recname,stepnumber,directions) values ('"+ name + "'," + (i+1) + ",'" + steps[i].directions + "')";
                stmt = con.createStatement();
                rs = stmt.executeQuery(query);
                if(steps[i].ingRequirements != null){
                    for (int j = 0; j < steps[i].ingRequirements.length; j++) {
                        IngredientReq ir = steps[i].ingRequirements[j];
                        query = "select ingname from ingredient where ingname='" + ir.name + "'";
                        stmt = con.createStatement();
                        rs = stmt.executeQuery(query);
                        Boolean newIng = true;
                        while (rs.next()) {
                            newIng = false;
                            System.out.println(rs);
                        }
                        if (newIng) {
                            Ingredient newIngredient = ir.ToIngredient();
                            newIngredient.AddIngredient(con);
                        }
                        query = "insert into stepuses (recname,stepnumber,ingname,quantuse) values ('" + name + "'," + (i + 1) + ",'" + ir.name + "'," + ir.quantity +")";
                        stmt = con.createStatement();
                        rs = stmt.executeQuery(query);
                    }
                }
            }
        } catch (Exception e){
            System.out.println(e);
        }
    }
}
