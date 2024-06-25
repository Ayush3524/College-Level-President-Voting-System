import com.voting.*;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Scanner;


public class CollegePresidentVotingSystem {

        public static void main(String[] args) throws ClassNotFoundException,SQLException {

                try {

                        Connection con = DriverManager.getConnection(DatabaseConnection.url,DatabaseConnection.username,DatabaseConnection.password);

                        while (true){
                                System.out.println();
                                System.out.println("****************||COLLEGE PRESIDENT VOTING SYSTEM||***********************");
                                System.out.println();
                                System.out.println("1. Register Student");
                                System.out.println("2. Student Login");
                                System.out.println("3. Admin Login");
                                System.out.println("4. Exit");
                                System.out.print("Enter a Choice: ");
                                Scanner sc= new Scanner(System.in);
                                int choice = sc.nextInt();
                                switch (choice){
                                        case 1:
                                                RegisterStudent.registerStudent(con,sc);
                                                break;
                                        case 2:
                                                StudentLogin.studentLogin(con,sc);
                                                break;
                                        case 3:
                                                AdminLogin.adminLogin(con,sc);
                                                break;
                                        case 4:
                                                AdditionalMethods.exit(StudentLogin.flag);
                                                sc.close();
                                                con.close();
                                                return;
                                        default:
                                                System.out.println();
                                          System.out.println("Invalid Choice. Try again.");
                                }

                        }
                }catch (SQLException e){
                        System.out.println(e.getMessage());
                }catch (InterruptedException e){
                        throw new RuntimeException(e);
                }
        }


}
