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
      //  chefmakesrecipe,
       // fridgestores,
       // ingriedient,
        pantry(new String[]{"pantryid"});
      //  pantrystores,
      //  recipe,
       // recipeauthor,
       // reciperequires,
       //refigerator,
        //step,
        //stepuses;

        public String[] attributes;

        private Tables(String[] attributes){
            this.attributes = attributes;
        }

    }

    public static void main(String[] args) throws IOException {
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
                String query = "";
                Tables table = null;
                String s = "";
                System.out.println("1.chef, \n2.chefmakesrecipe, \n3.fridgestores, \n4.ingriedient, \n5.pantry, " +
                        "\n6.pantrystores, \n7.recipe, " + "\n8.recipeauthor, \n9.reciperequires, \n10.refigerator, " +
                        "\n11.step, \n12.stepuses");
                while (table==null) { //gets which table and makes sure it exists
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
                String tablesAttributes = "";
                String inputAttributes = "";
                String[] attributes = new String[table.attributes.length];
                for (int i = 0; i< table.attributes.length; i++){
                    System.out.println("length is: " + table.attributes.length);
                    System.out.println("Enter data for " + table.attributes[i] + ":");
                    attributes[i] = in.nextLine();
                    tablesAttributes += table.attributes[i] + ", ";
                    inputAttributes += "'" + attributes[i] + "', ";
                    System.out.println("entered: " + attributes[i]);
                }
                query += tablesAttributes.substring(0, tablesAttributes.length() - 2) + ") values (" + inputAttributes.substring(0, inputAttributes.length() - 2) + ")";
                System.out.println(query);

                //while (!s.equals("quit") && !s.equals("exit") && !s.equals("q") && !s.equals("back") && !s.equals("cancel")) {
                   // s = in.nextLine();
                //}

                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                //ResultSet rs = stmt.executeQuery("insert into Chef (chefemail, chefname) values ('bcook@gmail.com', 'bob')");
                //ResultSet rs = stmt.executeQuery("delete from Chef where chefemail='bcook@gmail.com'");

                //while(rs.next()){
                //Date now = new Date(System.currentTimeMillis());
                // java.sql.Date sqlDate = new java.sql.Date(now.getTime());
                // System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
                //}
            }
        } catch (Exception e){
            System.out.println(e);
        }
    }
}
