package com.voting;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class RegisterStudent {
    public static void registerStudent(Connection con, Scanner sc){

        System.out.println("Enter Roll Number");
        int rollNumber = sc.nextInt();
        System.out.println("Enter Student Name: ");
        String studentName = sc.next();
        sc.nextLine();
        System.out.println("Enter password: ");
        String password = sc.next();
        sc.nextLine();

        try {
            String sql = "INSERT INTO student(roll_no, name, password) VALUES(?, ?, ?)";

            try (PreparedStatement preparedStatement = con.prepareStatement(sql)){

                preparedStatement.setInt(1,+ rollNumber);
                preparedStatement.setString(2, studentName);
                preparedStatement.setString(3,password);


                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows>0){
                    System.out.println();
                    System.out.println("Student Register Successfully!!");
                }else {
                    System.out.println();
                    System.out.println("Registration Failed.");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
