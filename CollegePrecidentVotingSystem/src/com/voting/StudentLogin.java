package com.voting;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class StudentLogin {

    public static boolean flag = true;
    public static void studentLogin(Connection con, Scanner sc){

        System.out.println("Enter Roll Number: ");
        int rollNumber = sc.nextInt();
        System.out.println("Enter Password: ");
        String password = sc.next();
        sc.nextLine();

        if (!AdditionalMethods.studentExists(con,rollNumber)){
            System.out.println();
            System.out.println("No Student Details Found for given roll number.");
        }

        try {
            String sql = "SELECT roll_no, password FROM student WHERE roll_no = " + rollNumber;

            try (PreparedStatement preparedStatement = con.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()){

                while (resultSet.next()){
                    int roll_no = resultSet.getInt("roll_no");
                    String password1 = resultSet.getString("password");
                    if (rollNumber==roll_no && password.equals(password1)){
                        System.out.println();
                        System.out.println("***************||Student Login Success||****************");
                        while (flag){
                            System.out.println();
                            System.out.println("1. View Nominees");
                            System.out.println("2. Add Vote");
                            System.out.println("3. Remove Vote");
                            System.out.println("4. Logout");
                            System.out.print("Enter a Choice: ");
                            int choice = sc.nextInt();
                            switch (choice){
                                case 1:
                                    viewNominees(con);
                                    break;
                                case 2:
                                    vote(con,sc,rollNumber);
                                    break;
                                case 3:
                                    removeVote(con, sc, rollNumber);
                                    break;
                                case 4:
                                    preparedStatement.close();
                                    resultSet.close();
                                    AdditionalMethods.logout();
                                    return;
                                default:
                                    System.out.println();
                                    System.out.println("Invalid Choice. Try again.");
                                    break;
                            }
                        }
                    }else {
                        System.out.println("Invalid username or password.");
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }

    }

    public static void viewNominees(Connection con){


        try {
            String sql = "SELECT id,name,symbol,votes FROM nominee";
            try (PreparedStatement preparedStatement = con.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()){

                System.out.println();
                System.out.println("+----------------+-------------------------------+--------------------+-------------+");
                System.out.println("| Nominee ID     |   Nominee Name                | Nominee Symbol     |  Votes      |");
                System.out.println("+----------------+-------------------------------+--------------------+-------------+");

                while (resultSet.next()){
                    int nomineeId = resultSet.getInt("id");
                    String nomineeName = resultSet.getString("name");
                    String nomineeSymbol = resultSet.getString("symbol");
                    int votes = resultSet.getInt("votes");


                    System.out.printf("| %-14d | %-29s | %-18s | %-11d |\n",
                            nomineeId,nomineeName,nomineeSymbol,votes);

                    System.out.println("+----------------+-------------------------------+--------------------+-------------+");
                }

            }

        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public static void vote(Connection con,Scanner sc,int rollNumber){

        System.out.println("Enter Nominee Id: ");
        int nomineeId = sc.nextInt();

        if (!AdditionalMethods.nomineeExists(con,nomineeId)){
            System.out.println();
            System.out.println("No Nominee Found For This Id.");
        }

        try {
            String verify = "SELECT status FROM student WHERE roll_no = "+rollNumber;


            try (PreparedStatement preparedStatement = con.prepareStatement(verify);
                 ResultSet resultSet = preparedStatement.executeQuery()){

                while (resultSet.next()){
                    int status = resultSet.getInt("status");
                    if (status==0){

                        String retrieve = "SELECT votes FROM nominee WHERE id= "+ nomineeId;

                        try (PreparedStatement retrieveVotes = con.prepareStatement(retrieve);
                             ResultSet retrieveResult = retrieveVotes.executeQuery()){

                            while (retrieveResult.next()){
                                int result = retrieveResult.getInt("votes");
                                String sql = "UPDATE nominee SET votes = ? WHERE id = "+ nomineeId;
                                try (PreparedStatement preparedStatement1 = con.prepareStatement(sql)){
                                    preparedStatement1.setInt(1,result + 1);
                                    int affectedRows = preparedStatement1.executeUpdate();
                                    if (affectedRows>0){
                                        System.out.println();
                                        System.out.println("Voted Successfully!!");
                                        String vote = "UPDATE student SET status = ? WHERE roll_no = "+rollNumber;
                                        try (PreparedStatement preparedStatement2 = con.prepareStatement(vote)){
                                            preparedStatement2.setBoolean(1,true);
                                            preparedStatement2.executeUpdate();
                                        }
                                    }else {
                                        System.out.println();
                                        System.out.println("Voting Failed.");
                                    }
                                }
                            }
                        }

                    }else {
                        System.out.println();
                        System.out.println("You are already Voted..");
                    }
                }

            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public static void removeVote(Connection con, Scanner sc,int rollNumber){

        System.out.println("Enter Nominee Id: ");
        int nomineeId = sc.nextInt();

        if (!AdditionalMethods.nomineeExists(con,nomineeId)){
            System.out.println();
            System.out.println("No Nominee Found For This Id.");
        }

        try {
            String verify = "SELECT status FROM student WHERE roll_no = "+rollNumber;

            try (PreparedStatement preparedStatement = con.prepareStatement(verify);
                 ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    int status = resultSet.getInt("status");

                    if (status==1){

                        String retrieve = "SELECT votes FROM nominee WHERE id= "+ nomineeId;

                        try (PreparedStatement retrieveVotes = con.prepareStatement(retrieve);
                             ResultSet retrieveResult = retrieveVotes.executeQuery()){

                            while (retrieveResult.next()){
                                int result = retrieveResult.getInt("votes");
                                String sql = "UPDATE nominee SET votes = ? WHERE id = "+ nomineeId;

                                if(result>0){
                                    try (PreparedStatement stmt = con.prepareStatement(sql)){

                                        stmt.setInt(1,result - 1);
                                        int affectedRows = stmt.executeUpdate();

                                        if (affectedRows>0){
                                            System.out.println();
                                            System.out.println("Remove Vote Successfully!!");
                                            String vote = "UPDATE student SET status = ? WHERE roll_no = "+rollNumber;
                                            try (PreparedStatement pre = con.prepareStatement(vote)){
                                                pre.setBoolean(1,false);
                                                pre.executeUpdate();
                                            }

                                        }

                                    }
                                }else {
                                    System.out.println();
                                    System.out.println("Nominee Doesn't Have any vote.");
                                }

                            }
                        }

                    }else {
                        System.out.println();
                        System.out.println("You are Not voted to any Nominee. Please vote first.");
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
