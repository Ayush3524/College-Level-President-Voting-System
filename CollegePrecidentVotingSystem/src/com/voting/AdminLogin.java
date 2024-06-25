package com.voting;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AdminLogin {
    public static void adminLogin(Connection con, Scanner sc){

        try {
            System.out.println("Enter username: ");
            String username1 = sc.next();
            System.out.println("Enter password: ");
            String password1 = sc.next();

            if (DatabaseConnection.username.equals(username1) && password1.equals(DatabaseConnection.password)){

                System.out.println();
                System.out.println("*****************Admin Login Success********************");
                while (true){
                    System.out.println();
                    System.out.println("1. Add Nominee");
                    System.out.println("2. Update Nominee");
                    System.out.println("3. Remove Nominee");
                    System.out.println("4. View Voting");
                    System.out.println("5. View Winner Nominee");
                    System.out.println("6. Logout");
                    System.out.print("Enter a Choice: ");
                    int choice = sc.nextInt();
                    switch (choice){
                        case 1:
                            addNominee(con,sc);
                            break;
                        case 2:
                            updateNominee(con,sc);
                            break;
                        case 3:
                            removeNominee(con,sc);
                            break;
                        case 4:
                            StudentLogin.viewNominees(con);
                            break;
                        case 5:
                            viewWinner(con);
                            break;
                        case 6:
                            AdditionalMethods.logout();
                            return;

                    }
                }
            }else {
                System.out.println();
                System.out.println("Invalid Username or password.");
            }
        }catch (InterruptedException e){
            throw  new RuntimeException(e);
        }
    }

    public static void addNominee(Connection con, Scanner sc){

        System.out.println("Enter Nominee Name: ");
        String nomineeName = sc.next();
        sc.nextLine();
        System.out.println("Enter Nominee Symbol: ");
        String nomineeSymbol = sc.next();

        try {
            String sql = "INSERT INTO nominee(name, symbol) VALUES(?, ?)";

            try (PreparedStatement preparedStatement = con.prepareStatement(sql)){

                preparedStatement.setString(1,nomineeName);
                preparedStatement.setString(2,nomineeSymbol);

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows>0){
                    System.out.println();
                    System.out.println("Nominee Added Successfully!!");
                }else {
                    System.out.println();
                    System.out.println("Nominee Adding Failed.");
                }

            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void updateNominee(Connection con, Scanner sc){

        System.out.println("Enter Nominee ID to update: ");
        int nomineeId = sc.nextInt();

        if (!AdditionalMethods.nomineeExists(con,nomineeId)){
            System.out.println();
            System.out.println("No Nominee Found for given Id");
        }

        System.out.println("Enter new Nominee Name: ");
        String newNomineeName = sc.next();
        sc.nextLine();
        System.out.println("Enter Nominee Symbol: ");
        String newSymbol = sc.next();
        sc.nextLine();

        try {
            String sql = "UPDATE nominee SET name = '" + newNomineeName + "', " +
                    "symbol = '" + newSymbol + "' "+
                    "WHERE id = "+nomineeId;

            try (PreparedStatement preparedStatement = con.prepareStatement(sql)){

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows>0){
                    System.out.println();
                    System.out.println("Nominee Updated Successfully!!!");
                }else {
                    System.out.println();
                    System.out.println("Updation Failed.");
                }

            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void removeNominee(Connection con, Scanner sc){

        System.out.println("Enter Nominee Id to Remove: ");
        int nomineeId = sc.nextInt();

        if (!AdditionalMethods.nomineeExists(con,nomineeId)){
            System.out.println();
            System.out.println("No Nominee Details Found for given ID.");
        }

        try {
            String sql = "DELETE FROM nominee WHERE id = "+ nomineeId;

            try (PreparedStatement preparedStatement = con.prepareStatement(sql)){

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows>0){
                    System.out.println();
                    System.out.println("Nominee Removed Successfully!!!");
                }else {
                    System.out.println();
                    System.out.println("Removal Failed.");
                }

            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public static void viewWinner(Connection con){
        System.out.println();

        String sql = "SELECT MAX(votes) from nominee";

        try(PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet rs= preparedStatement.executeQuery()){

            while (rs.next()){
                int votes = rs.getInt("MAX(votes)");

                String sql1 = "select * from nominee where votes= "+votes;
                try ( PreparedStatement ps=con.prepareStatement(sql1);
                      ResultSet rs1 = ps.executeQuery()){

                    System.out.println("Winner President Nominee is: ");
                    System.out.println();
                    System.out.println("+----------------+-------------------------------+--------------------+-------------+");
                    System.out.println("| Nominee ID     |   Nominee Name                | Nominee Symbol     |  Votes      |");
                    System.out.println("+----------------+-------------------------------+--------------------+-------------+");

                    while (rs1.next()){

                        int nomineeId = rs1.getInt("id");
                        String nomineeName = rs1.getString("name");
                        String nomineeSymbol = rs1.getString("symbol");
                        int votes2 = rs1.getInt("votes");


                        System.out.printf("| %-14d | %-29s | %-18s | %-11d |\n",
                                nomineeId,nomineeName,nomineeSymbol,votes2);

                        System.out.println("+----------------+-------------------------------+--------------------+-------------+");
                    }

                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
