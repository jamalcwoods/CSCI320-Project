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
        stepuses(new String[]{"stepnumber", "ingname", "quantuse"});

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
                    System.out.println("Would you like to 1)add a new row to a table or 2)read from a table?: ");
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
                        query = "inserted into " + table + " (";
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
                    } else if (option.equals("2")) { // 2 changed to "2"
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
                        /*
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
                        System.out.println("Query: " + query);
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        //need to figure out how to print the data in the result set
                        while (rs.next()) {
                            for (int i = 1; i <= table.attributes.length; i++) {
                                System.out.print(rs.getString(i) + "\t");
                            }
                            System.out.println();
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
