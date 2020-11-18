package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class Main {

    public enum Tables{
        chef(new String[]{"chefemail", "chefname"}),
        chefmakesrecipe(new String[]{"chefemail", "recname", "datemade", "amountmod"}),
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
        String currentEmail = "";
        String currentName = "";
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
                    System.out.println("Would you like to:\n1)add a new row to a table\n2)read from a table\n3)perform an action");
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

                        if (table.name().equals("chefmakesrecipe")){
                            query = "UPDATE recipe SET timesmade = (SELECT SUM(amountmod) FROM chefmakesrecipe WHERE recname = '" + attributes[1] + "')  WHERE recname = '" + attributes[1] + "'";
                            System.out.println(query);
                            Statement stmt2 = con.createStatement();
                            int rs2 = stmt2.executeUpdate(query);

                        }
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
                        System.out.print("What is your email?:");
                        String chefEmail = in.nextLine();
                        currentEmail = chefEmail;

                        Boolean hasAccount = false;
                        query = "SELECT chefemail,chefname FROM chef";
                        Statement stmt = con.createStatement();
                        ResultSet rsC = stmt.executeQuery(query);

                        while (rsC.next()) {
                            if (rsC.getString(1).matches(chefEmail)) {
                                hasAccount = true;
                                currentName = rsC.getString(2);
                            }
                        }

                        if (!hasAccount) {
                            System.out.print("What is your name?:");
                            String chefName = in.nextLine();
                            currentName = chefName;
                            query = "insert into chef (chefemail,chefname) values ('" + chefEmail + "','" + chefName + "')";
                            stmt = con.createStatement();
                            int rsc = stmt.executeUpdate(query);
                        }
                        System.out.println("Welcome " + currentName);
                        Boolean doingActions = true;
                        while(doingActions) {
                            String action = "";
                            System.out.println("Perform one of the following actions:");
                            System.out.println("0)Exit\n1)Add an ingredient\n2)Add a recipe\n3)Make a recipe");
                            System.out.println("4)View ingredients\n5)View recipes\n6)View dishes made");
                            System.out.println("7)Recommend recipes\n8)Recommend ingredients to add\n9)View chef data");
                            action = in.nextLine();
                            ResultSet rs;
                            switch (action) {
                                case "0":
                                    doingActions = false;
                                    break;
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
                                    while (!(newIngredient.location.toUpperCase().equals("FRIDGE") || newIngredient.location.toUpperCase().equals("REFRIDGERATOR") || newIngredient.location.toUpperCase().equals("PANTRY"))) {
                                        System.out.println("Valid locations are 'fridge' and 'pantry'");
                                        System.out.print("Where is this ingredient going to be stored?");
                                        newIngredient.location = in.nextLine();
                                    }
                                    System.out.print("When does this ingredient expire? (year-m-d):");
                                    newIngredient.expiration = in.nextLine();
                                    newIngredient.AddIngredient(con);

                                    break;
                                case "2": //code for add recipe here
                                    Recipe newRecipe = new Recipe();
                                    System.out.print("What is this a recipe for?:");
                                    newRecipe.name = in.nextLine();
                                    System.out.print("Who is the creator of this recipe?:");
                                    newRecipe.author = in.nextLine();
                                    System.out.print("What is the backstory of this recipe?:");
                                    newRecipe.backstory = in.nextLine();
                                    System.out.print("How many minutes does it take to make this recipe?:");
                                    newRecipe.duration = Integer.parseInt(in.nextLine());
                                    System.out.print("How many different steps are required for this recipe?:");
                                    Integer stepCount = Integer.parseInt(in.nextLine());
                                    newRecipe.steps = new Step[stepCount];
                                    for (int i = 0; i < stepCount; i++) {
                                        Step newStep = new Step();
                                        System.out.print("What are the instructions for step #" + (i + 1) + "?:");
                                        newStep.directions = in.nextLine();
                                        System.out.print("How many different ingredients are required for step #" + (i + 1) + "?:");
                                        Integer ingredientCount = Integer.parseInt(in.nextLine());
                                        newStep.ingRequirements = new IngredientReq[ingredientCount];
                                        for (int x = 0; x < ingredientCount; x++) {
                                            IngredientReq iReq = new IngredientReq();
                                            System.out.print("What is the name of ingredient #" + (x + 1) + "?:");
                                            iReq.name = in.nextLine();
                                            System.out.print("In what format of units does the step require the ingredient #" + (x + 1) + "? (singular term):");
                                            iReq.unit = in.nextLine();
                                            System.out.print("How many of ingredient #" + (x + 1) + " are you using in step #" + (i + 1) + "?:");
                                            iReq.quantity = Integer.parseInt(in.nextLine());
                                            newStep.ingRequirements[x] = iReq;
                                        }
                                        newRecipe.steps[i] = newStep;
                                    }
                                    newRecipe.AddRecipe(con);
                                    break;
                                case "3": //code for make recipe here
                                    query = "SELECT recname FROM recipe";
                                    stmt = con.createStatement();
                                    rs = stmt.executeQuery(query);
                                    System.out.println("available recipes:");
                                    ArrayList<String> prepareRecNames = new ArrayList<>();
                                    while (rs.next()) {
                                        String thisName = rs.getString(1);
                                        System.out.println(thisName);
                                        prepareRecNames.add(thisName);
                                    }
                                    String prepareRec;
                                    do {
                                        System.out.print("Which recipe would you like to prepare");
                                        prepareRec = in.nextLine();
                                    } while (!prepareRecNames.contains(prepareRec));

                                    float amountModifier = 0f;
                                    do {
                                        System.out.print("How much of the recipe would you like to prepare?\n(0.5 for half, 2.0 for double, minimum amount is 0.5)");
                                        amountModifier = Float.parseFloat(in.nextLine());
                                    } while (amountModifier < 0.5f);
                                    Boolean canPrepare = true;

                                    query = "SELECT ingname,quantreq FROM reciperequires WHERE recname ='" + prepareRec + "'";
                                    stmt = con.createStatement();
                                    rs = stmt.executeQuery(query);
                                    while (rs.next()) {
                                        Float amountNeeded = Float.parseFloat(rs.getString(2)) * amountModifier;
                                        String ing = rs.getString(1);

                                        query = "SELECT * FROM ingredient WHERE ingname='" + ing + "'";
                                        stmt = con.createStatement();
                                        ResultSet rsin = stmt.executeQuery(query);
                                        rsin.next();
                                        Float total = 0f;
                                        String needFridge = rsin.getString(3).toUpperCase();
                                        if (needFridge.equals("F")) {
                                            //get the total quantity in all pantries
                                            query = "SELECT SUM(pantryquant) AS total FROM pantrystores WHERE ingname = '" + ing + "'";
                                            Statement stmt1 = con.createStatement();
                                            ResultSet rs1 = stmt1.executeQuery(query);
                                            rs1.next();
                                            if (rs1.getString(1) != null) {
                                                total += Float.parseFloat(rs1.getString(1));
                                            }
                                        }
                                        //get the total quantity in all fridges
                                        query = "SELECT SUM(fridgequant) AS total FROM fridgestores WHERE ingname = '" + ing + "'";
                                        Statement stmt1 = con.createStatement();
                                        ResultSet rs1 = stmt1.executeQuery(query);
                                        rs1.next();
                                        if (rs1.getString(1) != null) {
                                            total += Float.parseFloat(rs1.getString(1));
                                        }

                                        if (amountNeeded <= total) {
                                            System.out.println(ing + " requirement met (" + total + "/" + amountNeeded + ")");
                                        } else {
                                            canPrepare = false;
                                            System.out.println(ing + " requirement not met (" + total + "/" + amountNeeded + ")");
                                        }
                                    }
                                    if (canPrepare) {
                                        query = "SELECT ingname,quantreq FROM reciperequires WHERE recname ='" + prepareRec + "'";
                                        stmt = con.createStatement();
                                        rs = stmt.executeQuery(query);
                                        while (rs.next()) {
                                            Float toRemove = Float.parseFloat(rs.getString(2)) * amountModifier;
                                            String ing = rs.getString(1);

                                            query = "SELECT * FROM ingredient WHERE ingname='" + ing + "'";
                                            stmt = con.createStatement();
                                            ResultSet rsin = stmt.executeQuery(query);
                                            rsin.next();
                                            String needFridge = rsin.getString(3).toUpperCase();
                                            if (needFridge.equals("F")) {

                                                query = "SELECT SUM(pantryquant) AS total FROM pantrystores WHERE ingname = '" + ing + "'";
                                                Statement stmt1 = con.createStatement();
                                                ResultSet rs1 = stmt1.executeQuery(query);
                                                rs1.next();


                                                //get the total quantity in all pantries
                                                query = "update pantrystores set pantryquant = pantryquant - " + toRemove + " where ingname='" + ing + "'";
                                                stmt = con.createStatement();
                                                int rsc = stmt.executeUpdate(query);
                                                toRemove -= Float.parseFloat(rs1.getString(1));

                                            }
                                            if (toRemove > 0) {
                                                query = "SELECT SUM(fridgequant) AS total FROM fridgestores WHERE ingname = '" + ing + "'";
                                                Statement stmt1 = con.createStatement();
                                                ResultSet rs1 = stmt1.executeQuery(query);
                                                rs1.next();
                                                //get the total quantity in all fridges

                                                query = "update fridgestores set fridgequant = fridgequant - " + toRemove + " where ingname='" + ing + "'";
                                                stmt = con.createStatement();
                                                int rsc = stmt.executeUpdate(query);
                                            }
                                        }

                                        Calendar now = Calendar.getInstance();
                                        query = "insert into chefmakesrecipe (chefemail,recname,datemade,amountmod) values ('" + currentEmail + "','" + prepareRec + "','" + now.get(Calendar.YEAR) + "-" + now.get(Calendar.MONTH) + "-" + now.get(Calendar.DATE) + "'," + amountModifier + ")";
                                        stmt = con.createStatement();
                                        int rsc = stmt.executeUpdate(query);

                                        query = "update recipe set timesmade = timesmade + 1 where recname='" + prepareRec + "'";
                                        stmt = con.createStatement();
                                        rsc = stmt.executeUpdate(query);
                                        System.out.println(prepareRec + " created");
                                    }
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
                                        int total = 0;
                                        String needFridge = rs.getString(3).toUpperCase();
                                        if (needFridge.equals("F")) {
                                            String iName = rs.getString(1);
                                            //get the total quantity in all pantries
                                            query = "SELECT SUM(pantryquant) AS total FROM pantrystores WHERE ingname = '" + iName + "'";
                                            Statement stmt1 = con.createStatement();
                                            ResultSet rs1 = stmt1.executeQuery(query);
                                            rs1.next();
                                            if (rs1.getString(1) != null) {
                                                total += Integer.parseInt(rs1.getString(1));
                                            }
                                        }
                                        String iName = rs.getString(1);
                                        //get the total quantity in all fridges
                                        query = "SELECT SUM(fridgequant) AS total FROM fridgestores WHERE ingname = '" + iName + "'";
                                        Statement stmt1 = con.createStatement();
                                        ResultSet rs1 = stmt1.executeQuery(query);

                                        rs1.next();
                                        if (rs1.getString(1) != null) {
                                            total += Integer.parseInt(rs1.getString(1));
                                        }
                                        System.out.println(total);
                                    }
                                    break;
                                case "5": //code for view recipes here
                                    //print out all of the recipe names
                                    query = "SELECT recname FROM recipe";
                                    stmt = con.createStatement();
                                    rs = stmt.executeQuery(query);
                                    System.out.println("available recipes:");
                                    ArrayList<String> recNames = new ArrayList<>();
                                    while (rs.next()) {
                                        String thisName = rs.getString(1);
                                        System.out.println(thisName);
                                        recNames.add(thisName);
                                    }
                                    String rec;
                                    do {
                                        System.out.print("Which recipe would you like to view");
                                        rec = in.nextLine();
                                    } while (!recNames.contains(rec));

                                    //print the recipe information
                                    query = "SELECT * FROM recipe WHERE recname = '" + rec + "'";
                                    stmt = con.createStatement();
                                    rs = stmt.executeQuery(query);
                                    rs.next();
                                    for (int i = 1; i <= Tables.recipe.attributes.length; i++) {
                                        System.out.print(rs.getString(i) + "\t");
                                    }

                                    //get all of the required ingredients
                                    System.out.println("\nIngredients:");
                                    query = "SELECT ingname,quantreq FROM reciperequires WHERE recname ='" + rec + "'";
                                    stmt = con.createStatement();
                                    rs = stmt.executeQuery(query);
                                    while (rs.next()) {
                                        System.out.println(rs.getString(1) + "\t" + rs.getString(2));
                                    }

                                    //get all of the steps
                                    System.out.println("Steps:");
                                    query = "SELECT stepnumber,directions FROM step WHERE recname ='" + rec + "'";
                                    stmt = con.createStatement();
                                    rs = stmt.executeQuery(query);
                                    while (rs.next()) {
                                        System.out.println(rs.getString(1) + "\t" + rs.getString(2));
                                    }
                                    break;
                                case "6": //code for view dishes made here
                                    query = "SELECT * FROM chefmakesrecipe";
                                    stmt = con.createStatement();
                                    rs = stmt.executeQuery(query);
                                    while (rs.next()) {
                                        for (int i = 1; i <= Tables.chefmakesrecipe.attributes.length; i++) {
                                            System.out.print(rs.getString(i) + "\t");
                                        }
                                        System.out.println("");
                                    }
                                    break;
                                case "7":
                                    System.out.println("What would you like a recommendation based on?");
                                    System.out.println("0)Based on recipes completed by other chefs" +
                                            "\n1)Based on ingredients in pantry/refrigerator" +
                                            "\n2)Based on number of times recipes have been completed" +
                                            "\n3)Based on specific ingreient" +
                                            "\n4)Based on time needed to cook" +
                                            "\n5)Based on steps in recipe");
                                    String choice = in.nextLine();
                                    switch (choice) {
                                        case "0":
                                            query = "SELECT chefemail,recname FROM chefmakesrecipe";
                                            stmt = con.createStatement();
                                            rs = stmt.executeQuery(query);
                                            HashSet<String[]> data = new HashSet<String[]>();
                                            HashSet<String> userMade = new HashSet<String>();
                                            HashSet<String> similarChef = new HashSet<String>();
                                            HashSet<String> recommened = new HashSet<String>();
                                            while (rs.next()) {
                                                data.add(new String[]{rs.getString(1), rs.getString(2)});
                                                if (rs.getString(1).matches(currentEmail)) {
                                                    userMade.add(rs.getString(2));
                                                }
                                            }
                                            for (String[] temp : data) {
                                                if (userMade.contains(temp[1]) && !currentEmail.matches(temp[0])) {
                                                    similarChef.add((temp[0]));
                                                }
                                            }
                                            for (String[] temp : data) {
                                                if (similarChef.contains(temp[0]) && !recommened.contains(temp[1]) && !userMade.contains(temp[1])) {
                                                    recommened.add(temp[1]);
                                                }
                                            }
                                            for (String temp : recommened) {
                                                System.out.println(temp);
                                            }
                                            break;

                                        case "1":
                                            query = "SELECT ingname,pantryquant FROM pantrystores";
                                            stmt = con.createStatement();
                                            rs = stmt.executeQuery(query);
                                            HashMap<String, Float> ingredients = new HashMap<String, Float>();
                                            while (rs.next()) {
                                                ingredients.put(rs.getString(1), Float.parseFloat(rs.getString(2)));
                                            }

                                            query = "SELECT ingname,fridgequant FROM fridgestores";
                                            stmt = con.createStatement();
                                            rs = stmt.executeQuery(query);
                                            while (rs.next()) {
                                                if (ingredients.containsKey(rs.getString(1))) {
                                                    ingredients.replace(rs.getString(1), ingredients.get(rs.getString(1)) + Float.parseFloat(rs.getString(2)));
                                                } else {
                                                    ingredients.put(rs.getString(1), Float.parseFloat(rs.getString(2)));
                                                }
                                            }

                                            HashMap<String, HashMap<String, Float>> recipesRequires = new HashMap<String, HashMap<String, Float>>();
                                            query = "SELECT recname,ingname,quantreq FROM reciperequires";
                                            stmt = con.createStatement();
                                            rs = stmt.executeQuery(query);
                                            while (rs.next()) {
                                                String rName = rs.getString(1);
                                                String iName = rs.getString(2);
                                                Float iQuant = Float.parseFloat(rs.getString(3));
                                                if (recipesRequires.containsKey(rName)) {
                                                    recipesRequires.get(rName).put(iName, iQuant);
                                                } else {
                                                    HashMap<String, Float> temp = new HashMap<String, Float>();
                                                    temp.put(iName, iQuant);
                                                    recipesRequires.put(rName, temp);
                                                }
                                            }

                                            HashSet<String> possible = new HashSet<String>();
                                            for (HashMap.Entry<String, HashMap<String, Float>> recipe : recipesRequires.entrySet()) {
                                                boolean canMake = true;
                                                for (HashMap.Entry<String, Float> ingredientReq : recipe.getValue().entrySet()) {
                                                    if (ingredients.containsKey(ingredientReq.getKey())) {
                                                        if (ingredients.get(ingredientReq.getKey()) < ingredientReq.getValue()) {
                                                            canMake = false;
                                                            break;
                                                        }
                                                    }
                                                }
                                                if (!canMake) {
                                                    break;
                                                } else {
                                                    possible.add(recipe.getKey());
                                                }
                                            }
                                            System.out.println("Recipe's possible: " + possible.size());
                                            for (String recipe : possible) {
                                                System.out.println(recipe);
                                            }
                                            break;

                                        case "2":
                                            query = "SELECT recname,timesmade FROM recipe";
                                            stmt = con.createStatement();
                                            rs = stmt.executeQuery(query);
                                            HashMap<String, Integer> makeAmounts = new HashMap<String, Integer>();
                                            while (rs.next()) {
                                                makeAmounts.put(rs.getString(1), Integer.parseInt(rs.getString(2)));
                                            }
                                            makeAmounts = sortByValue(makeAmounts);
                                            System.out.println("Recipe recommendations based on times made");
                                            for (Map.Entry<String, Integer> en : makeAmounts.entrySet()) {
                                                System.out.println(en.getKey() + " | Times Made: " + en.getValue());
                                            }
                                            break;

                                        case "3":
                                            System.out.println("Name the ingredient you would like to specify");
                                            String ingredient = in.nextLine().toLowerCase();
                                            query = "SELECT recname,ingname FROM reciperequires";
                                            stmt = con.createStatement();
                                            rs = stmt.executeQuery(query);
                                            HashSet<String> recipes = new HashSet<String>();
                                            while (rs.next()) {
                                                if (ingredient.matches(rs.getString(2))) {
                                                    recipes.add(rs.getString(1));
                                                }
                                            }
                                            System.out.println("Recipes with " + ingredient + " in them:");
                                            for (String recipe : recipes) {
                                                System.out.println(recipe);
                                            }
                                            break;

                                        case "4":
                                            query = "SELECT recname,makeTime FROM recipe";
                                            stmt = con.createStatement();
                                            rs = stmt.executeQuery(query);
                                            HashMap<String, Integer> makeTimes = new HashMap<String, Integer>();
                                            while (rs.next()) {
                                                Date d = null;
                                                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                                                d = format.parse(rs.getString(2));
                                                makeTimes.put(rs.getString(1) + " | Time to make: " + rs.getString(2), (int) d.getTime());
                                            }
                                            makeTimes = sortByValue(makeTimes);
                                            System.out.println("Recipe recommendations based on times made");
                                            for (Map.Entry<String, Integer> en : makeTimes.entrySet()) {
                                                System.out.println(en.getKey());
                                            }
                                            break;

                                        case "5":
                                            query = "SELECT recname FROM step";
                                            stmt = con.createStatement();
                                            rs = stmt.executeQuery(query);
                                            HashMap<String, Integer> stepsCount = new HashMap<String, Integer>();
                                            while (rs.next()) {
                                                if (stepsCount.containsKey(rs.getString(1))) {
                                                    stepsCount.replace(rs.getString(1), stepsCount.get(rs.getString(1)) + 1);
                                                } else {
                                                    stepsCount.put(rs.getString(1), 1);
                                                }
                                            }
                                            stepsCount = sortByValue(stepsCount);
                                            System.out.println("Recipe recommendations based on number of steps:");
                                            for (Map.Entry<String, Integer> en : stepsCount.entrySet()) {
                                                System.out.println(en.getKey() + " | Steps: " + en.getValue());
                                            }
                                            break;
                                    }
                                    break;

                                case "8":
                                    System.out.println("What would you like a recommendation based on?");
                                    System.out.println("0)Based on recipes completed by you" +
                                            "\n1)Based on all possible recipes");
                                    String choice1 = in.nextLine();
                                    switch (choice1) {
                                        case "0":
                                            query = "SELECT ingname,pantryquant FROM pantrystores";
                                            stmt = con.createStatement();
                                            rs = stmt.executeQuery(query);
                                            HashMap<String, Float> ingredients = new HashMap<String, Float>();
                                            while (rs.next()) {
                                                ingredients.put(rs.getString(1), Float.parseFloat(rs.getString(2)));
                                            }

                                            query = "SELECT ingname,fridgequant FROM fridgestores";
                                            stmt = con.createStatement();
                                            rs = stmt.executeQuery(query);
                                            while (rs.next()) {
                                                if (ingredients.containsKey(rs.getString(1))) {
                                                    ingredients.replace(rs.getString(1), ingredients.get(rs.getString(1)) + Float.parseFloat(rs.getString(2)));
                                                } else {
                                                    ingredients.put(rs.getString(1), Float.parseFloat(rs.getString(2)));
                                                }
                                            }

                                            query = "SELECT chefemail,recname FROM chefmakesrecipe";
                                            stmt = con.createStatement();
                                            rs = stmt.executeQuery(query);
                                            HashSet<String> madeRecipes = new HashSet<String>();
                                            while (rs.next()) {
                                                if (rs.getString(1).matches(chefEmail)) {
                                                    madeRecipes.add(rs.getString(2));
                                                }
                                            }

                                            query = "SELECT recname,ingname,quantreq FROM reciperequires";
                                            stmt = con.createStatement();
                                            rs = stmt.executeQuery(query);
                                            HashSet<String> ingList = new HashSet<String>();
                                            while (rs.next()) {
                                                if (madeRecipes.contains(rs.getString(1))) {
                                                    if (ingredients.get(rs.getString(2)) < Float.parseFloat(rs.getString(3))) {
                                                        ingList.add(rs.getString(2));
                                                    }
                                                }
                                            }

                                            System.out.println("Ingredient recommendations based on recipes completed:");
                                            for (String ing : ingList) {
                                                System.out.println(ing);
                                            }
                                            break;

                                        case "1":
                                            query = "SELECT ingname,pantryquant FROM pantrystores";
                                            stmt = con.createStatement();
                                            rs = stmt.executeQuery(query);
                                            ingredients = new HashMap<String, Float>();
                                            while (rs.next()) {
                                                ingredients.put(rs.getString(1), Float.parseFloat(rs.getString(2)));
                                            }

                                            query = "SELECT ingname,fridgequant FROM fridgestores";
                                            stmt = con.createStatement();
                                            rs = stmt.executeQuery(query);
                                            while (rs.next()) {
                                                if (ingredients.containsKey(rs.getString(1))) {
                                                    ingredients.replace(rs.getString(1), ingredients.get(rs.getString(1)) + Float.parseFloat(rs.getString(2)));
                                                } else {
                                                    ingredients.put(rs.getString(1), Float.parseFloat(rs.getString(2)));
                                                }
                                            }

                                            HashMap<String, HashMap<String, Float>> recipesRequires = new HashMap<String, HashMap<String, Float>>();
                                            query = "SELECT recname,ingname,quantreq FROM reciperequires";
                                            stmt = con.createStatement();
                                            rs = stmt.executeQuery(query);
                                            while (rs.next()) {
                                                String rName = rs.getString(1);
                                                String iName = rs.getString(2);
                                                Float iQuant = Float.parseFloat(rs.getString(3));
                                                if (recipesRequires.containsKey(rName)) {
                                                    recipesRequires.get(rName).put(iName, iQuant);
                                                } else {
                                                    HashMap<String, Float> temp = new HashMap<String, Float>();
                                                    temp.put(iName, iQuant);
                                                    recipesRequires.put(rName, temp);
                                                }
                                            }

                                            HashSet<String> needed = new HashSet<String>();
                                            for (HashMap.Entry<String, HashMap<String, Float>> recipe : recipesRequires.entrySet()) {
                                                for (HashMap.Entry<String, Float> ingredientReq : recipe.getValue().entrySet()) {
                                                    if (ingredients.containsKey(ingredientReq.getKey())) {
                                                        if (ingredients.get(ingredientReq.getKey()) < ingredientReq.getValue()) {
                                                            if (!needed.contains(ingredientReq.getKey())) {
                                                                needed.add(ingredientReq.getKey());
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            System.out.println("Ingredient recommendations based on existing recipes:");
                                            for (String ing : needed) {
                                                System.out.println(ing);
                                            }
                                            break;
                                    }
                                    break;
                                case "9":
                                    System.out.println("What chef information would you like to know?");
                                    System.out.println("0)Most used ingredient" +
                                            "\n1)Recipe history");
                                    String choice2 = in.nextLine();
                                    switch (choice2) {
                                        case "0":
                                            query = "SELECT recname,amountmod,chefemail FROM chefmakesrecipe";
                                            stmt = con.createStatement();
                                            rs = stmt.executeQuery(query);
                                            HashMap<String, Float> makeamounts = new HashMap<String, Float>();
                                            while (rs.next()) {
                                                if (rs.getString(3).matches(chefEmail)) {
                                                    if (makeamounts.containsKey(rs.getString(1))) {
                                                        makeamounts.replace(rs.getString(1), makeamounts.get(rs.getString(1)) + rs.getFloat(2));
                                                    } else {
                                                        makeamounts.put(rs.getString(1), rs.getFloat(2));
                                                    }
                                                }
                                            }

                                            HashMap<String, Float> ingamounts = new HashMap<String, Float>();
                                            query = "SELECT recname,ingname,quantreq FROM reciperequires";
                                            stmt = con.createStatement();
                                            rs = stmt.executeQuery(query);
                                            while (rs.next()) {
                                                if (makeamounts.containsKey(rs.getString(1))) {
                                                    if (ingamounts.containsKey(rs.getString(2))) {
                                                        ingamounts.replace(rs.getString(2), ingamounts.get(rs.getString(2)) + (rs.getFloat(3) * makeamounts.get(rs.getString(1))));
                                                    } else {
                                                        ingamounts.put(rs.getString(2), rs.getFloat(3) * makeamounts.get(rs.getString(1)));
                                                    }
                                                }
                                            }
                                            ingamounts = sortByValueF(ingamounts);

                                            for (Map.Entry<String, Float> en : ingamounts.entrySet()) {
                                                System.out.println(en.getKey() + " | Amount Used: " + en.getValue());
                                            }
                                            break;

                                        case "1":
                                            query = "SELECT recname,amountmod,chefemail,datemade FROM chefmakesrecipe";
                                            stmt = con.createStatement();
                                            rs = stmt.executeQuery(query);
                                            while (rs.next()) {
                                                if (rs.getString(3).matches(chefEmail)) {
                                                    System.out.println("Name: " + rs.getString(1) + " | Amount Made: " + rs.getString(2) + " | Date Made: " + rs.getString(4));
                                                }
                                            }
                                            break;
                                    }
                            }
                            System.out.println("What would you like to do next?:" +
                                    "\n0)Preform another action " +
                                    "\n1)Exit To Menu");
                            s = in.nextLine();
                            doingActions = s.matches("0");
                        }
                    }
                    System.out.println("Would you like to exit the program(y/n)?");
                    s = in.nextLine();
                    if (s.equals("y")) {
                        cont = false;
                    }
                }
            }
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public static HashMap<String, Float> sortByValueF(HashMap<String, Float> hm)
    {
        List<Map.Entry<String, Float> > list =
                new LinkedList<Map.Entry<String, Float> >(hm.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Float> >() {
            public int compare(Map.Entry<String, Float> o1,
                               Map.Entry<String, Float> o2)
            {
                return -(o1.getValue()).compareTo(o2.getValue());
            }
        });

        HashMap<String, Float> temp = new LinkedHashMap<String, Float>();
        for (Map.Entry<String, Float> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm)
    {
        List<Map.Entry<String, Integer> > list =
                new LinkedList<Map.Entry<String, Integer> >(hm.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2)
            {
                return -(o1.getValue()).compareTo(o2.getValue());
            }
        });

        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
}


