package com.voting;


public class DatabaseConnection {

    public static final String url = "jdbc:mysql://localhost:3306/voting_system";
    public static final String username = "root";
    public static final String password = "ayush@24";

    public static void dbConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

    }

}
