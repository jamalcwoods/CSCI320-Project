package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class Main {

    public enum Tables{
        chef(new String[]{"chefemail", "chefname"}),
        chefmakesrecipe(new String[]{"chefemail", "recname", "datemade"}),
        fridgestores(new String[]{"fridgeid", "ingname", "fridgequant"}),
        ingredient(new String[]{"ingname", "expdate", "needfridge", "unit"}),
        pantry(new String[]{"pantryid"}),
        pantrystores(new String[]{"pantryid", "ingname", "pantryquant"}),
        recipe(new String[]{"recname", "backstory", "maketime", "timesmade"}),
        recipeauthor(new String[]{"recname", "author"}),
        reciperequires(new String[]{"recname", "ingname", "quantreq"}),
        refrigerator(new String[]{"fridgeid"}),
        step(new String[]{"stepnumber", "recname", "directions"}),
        stepuses(new String[]{"recname","stepnumber", "ingname", "quantuse"});

        public String[] attributes;

        Tables(String[] attributes){
            this.attributes = attributes;
        }
    }

    public static void main(String[] args) throws IOException { //TODO split up the main, make sure that forign keys exist when entering them, dont end program when something entered is wrong (need to go back)
        /*
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String test = reader.readLine();

        do{
            System.out.println(test);
            test = reader.readLine();
        } while(test != "exit");
        */
        String password = "rohng0iP7ma5ahgeisoo";
        String user = "p320_22";
        String url = "jdbc:postgresql://reddwarf.cs.rit.edu:5432/p320_22";
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(url, user, password);

            if(con != null) {
                System.out.println("OK");
                Scanner in = new Scanner(System.in);
                boolean cont = true;

                while (cont){
                    String query = "";
                    Tables table = null;
                    String s = "";
                    System.out.println("Would you like to 1)add a new row to a table 2)read from a table? or 3)perform an action: ");
                    String option = in.nextLine();
                    if (option.equals("1")) {
                        //create a new row in a table
                        String tablesAttributes = "";
                        String inputAttributes = "";
                        System.out.println("1.chef, \n2.chefmakesrecipe, \n3.fridgestores, \n4.ingredient, \n5.pantry, " +
                                "\n6.pantrystores, \n7.recipe, " + "\n8.recipeauthor, \n9.reciperequires, \n10.refrigerator, " +
                                "\n11.step, \n12.stepuses");
                        while (table == null) { //gets which table and makes sure it exists
                            System.out.println("What table would you like to add to?: ");
                            s = in.nextLine();
                            for (Tables tab : Tables.values()) {
                                if (tab.name().equals(s)) {
                                    table = tab;
                                }
                            }
                        }
                        System.out.println("Table adding to is " + table);
                        query = "insert into " + table + " (";
                        String[] attributes = new String[table.attributes.length];
                        for (int i = 0; i < table.attributes.length; i++) {
                            System.out.println("Enter data for " + table.attributes[i] + ":");
                            attributes[i] = in.nextLine();
                            tablesAttributes += table.attributes[i] + ", ";
                            inputAttributes += "'" + attributes[i] + "', ";
                            System.out.println("entered: " + attributes[i]);
                        }
                        query += tablesAttributes.substring(0, tablesAttributes.length() - 2) + ") values (" + inputAttributes.substring(0, inputAttributes.length() - 2) + ")";
                        System.out.println(query);

                        Statement stmt = con.createStatement();
                        int rs = stmt.executeUpdate(query);
                        //ResultSet rs = stmt.executeQuery("insert into Chef (chefemail, chefname) values ('bcook@gmail.com', 'bob')");
                        //ResultSet rs = stmt.executeQuery("delete from Chef where chefemail='bcook@gmail.com'");

                        //while(rs.next()){
                        //Date now = new Date(System.currentTimeMillis());
                        // java.sql.Date sqlDate = new java.sql.Date(now.getTime());
                        // System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
                        //}
                    } else if (option.equals("2")) {
                        //read a table
                        System.out.println("1.chef, \n2.chefmakesrecipe, \n3.fridgestores, \n4.ingredient, \n5.pantry, " +
                                "\n6.pantrystores, \n7.recipe, " + "\n8.recipeauthor, \n9.reciperequires, \n10.refrigerator, " +
                                "\n11.step, \n12.stepuses");
                        while (table == null) { //gets which table and makes sure it exists
                            System.out.println("From which table would you like to read the data?: ");
                            s = in.nextLine();
                            for (Tables tab : Tables.values()) {
                                if (tab.name().equals(s)) {
                                    table = tab;
                                }
                            }
                        }
                        /* -- this code is not getting all of the columns in the result set
                              even though the queries look correct
                        //start the query
                        query = "select ";
                        //add the attributes to the query
                        for (int i = 0; i < table.attributes.length; i++) {
                            query += table.attributes[i] + " ";
                        }
                        //finish the query
                        query += "from " + table;
                         */
                        query = "SELECT * FROM " + table;
                        //System.out.println("Query: " + query);
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        while (rs.next()) {
                            for (int i = 1; i <= table.attributes.length; i++) {
                                System.out.print(rs.getString(i) + "\t");
                            }
                            System.out.println();
                        }
                    } else if (option.equals("3")) {
                        String action = "";
                        System.out.println("Perform one of the following actions:");
                        System.out.println("0)Cancel, 1)Add an ingredient, 2)Add a recipe, 3)Make a recipe");
                        System.out.println("4)View ingredients, 5)View recipes, 6)View dishes made");
                        action = in.nextLine();
                        ResultSet rs;
                        Statement stmt;
                        switch (action) {
                            case "1": //create a new ingredient and put it in the pantry/fridge
                                Ingredient newIngredient = new Ingredient();
                                System.out.print("What is the name of the ingredient?:");
                                newIngredient.name = in.nextLine();
                                System.out.print("In what format of units are you adding the ingredient? (singular term):");
                                newIngredient.unit = in.nextLine();
                                System.out.print("How many of this ingredient are you adding?:");
                                newIngredient.quantity = Integer.parseInt(in.nextLine());
                                System.out.print("Where is this ingredient going to be stored?");
                                newIngredient.location = in.nextLine();
                                while (!(newIngredient.location.toUpperCase().equals("FRIDGE")||newIngredient.location.toUpperCase().equals("REFRIDGERATOR")||newIngredient.location.toUpperCase().equals("PANTRY"))) {
                                    System.out.println("Valid locations are 'fridge' and 'pantry'");
                                    System.out.print("Where is this ingredient going to be stored?");
                                    newIngredient.location = in.nextLine();
                                }
                                System.out.print("When does this ingredient expire? (year-m-d):");
                                newIngredient.expiration = in.nextLine();
                                newIngredient.AddIngredient(con);

                                break;
                            case "2": //code for add recipe here
                                System.out.print("What is this a recipe for?:");
                                String rName = in.nextLine();
                                System.out.print("Who is the creator of this recipe?:");
                                String aName = in.nextLine();
                                System.out.print("What is the backstory of this recipe?:");
                                String backstory = in.nextLine();
                                System.out.print("How many minutes does it take to make this recipe?:");
                                String makeLength = in.nextLine();
                                System.out.print("How many different steps are required for this recipe?:");
                                String stepCountString = in.nextLine();
                                Integer stepCount = Integer.parseInt(stepCountString);
                                Step[] steps;
                                for (int i = 0; i < stepCount; i++) {
                                    Step newStep = new Step();
                                    System.out.print("What are the instructions for this step?:");
                                    String stepDirections = in.nextLine();
                                }
                                break;
                            case "3": //code for make recipe here
                                break;
                            case "4": //get all of the ingredients from the ingredients table
                                query = "SELECT * FROM ingredient";
                                stmt = con.createStatement();
                                rs = stmt.executeQuery(query);
                                while (rs.next()) {
                                    for (int i = 1; i <= Tables.ingredient.attributes.length; i++) {
                                        System.out.print(rs.getString(i) + "\t");
                                    }

                                    //find the quantity of this ingredient that is on hand by searching the pantry/fridge stores tables
                                    String needFridge = rs.getString(3).toUpperCase();
                                    String location;
                                    if (needFridge.equals("YES")) {
                                        location = "fridge";
                                    } else {
                                        location = "pantry";
                                    }
                                    String iName = rs.getString(1);
                                    //get the total quantity in all pantries/fridges
                                    query = "SELECT SUM(quantity) AS total FROM " + location + "stores WHERE ingname = '" + iName + "'";
                                    Statement stmt1 = con.createStatement();
                                    ResultSet rs1 = stmt1.executeQuery(query);
                                    rs1.next();
                                    System.out.println(rs1.getString(1));
                                }
                                break;
                            case "5": //code for view recipes here
                                break;
                            case "6": //code for view dishes made here
                                break;
                        }
                    }
                    System.out.println("continue? (y/n):");
                    s = in.nextLine();
                    if(s.equals("n")){
                        cont = false;
                    }
                }
            }
        } catch (Exception e){
            System.out.println(e);
        }
    }
}
