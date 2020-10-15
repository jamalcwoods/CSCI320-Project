package com.company;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here

        /*
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String test = reader.readLine();

        do{
            System.out.println(test);
            test = reader.readLine();
        } while(test != "exit");
        */

        Connection con = DriverManager.getConnection();
    }
}
