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
            Integer r;
            String query = "";
            Integer hours = (int)(Math.ceil(duration/60));
            Integer minutes = duration % 60;
            String minutesString;
            String hoursString;
            if(minutes < 10){
                minutesString = "0" + minutes;
            } else {
                minutesString = minutes.toString();
            }
            if(hours < 10){
                hoursString = "0" + hours;
            } else {
                hoursString = hours.toString();
            }


            query = "insert into recipe (recname,backstory,maketime,timesmade) values ('"+ name + "','" + backstory + "','" + hoursString +":" + minutesString + ":00',0)";
            stmt = con.createStatement();
            r = stmt.executeUpdate(query);

            query = "insert into recipeauthor (recname,author) values ('"+ name + "','" + author + "')";
            stmt = con.createStatement();
            r = stmt.executeUpdate(query);
            for (int i = 0; i < steps.length; i++) {
                query = "insert into step (recname,stepnumber,directions) values ('"+ name + "'," + (i+1) + ",'" + steps[i].directions + "')";
                stmt = con.createStatement();
                r = stmt.executeUpdate(query);
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
                        r = stmt.executeUpdate(query);
                    }
                }
            }
        } catch (Exception e){
            System.out.println(e);
        }
    }
}
