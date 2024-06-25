package com.voting;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdditionalMethods {

    public static boolean studentExists(Connection con, int rollNumber){

        String sql = "SELECT roll_no FROM student";

        try (PreparedStatement preparedStatement = con.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()){

            return resultSet.next();

        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean nomineeExists(Connection con, int nomineeId){

        try {
            String sql = "SELECT id FROM nominee";

            try (PreparedStatement preparedStatement = con.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()){

                return resultSet.next();

            }
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public static void exit(Boolean flag) throws InterruptedException{
        System.out.print("Exiting System");
        int i=5;
        while (i>0){
            System.out.print(".");
            Thread.sleep(450);
            i--;
        }
        System.out.println();
        System.out.println("ThankYou For Using College President Voting System.");
        flag = false;
    }

    public static void logout() throws InterruptedException {
        System.out.print("Logging Out");
        int i=5;
        while (i>0){
            System.out.print(".");
            Thread.sleep(350);
            i--;
        }
        System.out.println();
    }
}
