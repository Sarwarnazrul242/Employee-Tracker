
package test;

import java.sql.*;
import java.util.Scanner;

public class Assignment {
    
    static Connection myConn = null;
    static Statement myStmt = null;
    static ResultSet myRs = null;    
    static PreparedStatement pstmt = null;
    
    public static void main(String[] args) {
        
        Scanner in = new Scanner(System.in);  // Create a Scanner object
        String sql="";
        
        try {
            // 1. Get a connection to database
            myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/employees", "root", "sarwar");
            
            // 2. Create a statement
            myStmt = myConn.createStatement();
            
            // 3. Create table if it doesn't exist
            sql = "CREATE TABLE IF NOT EXISTS Employee " +
                  "(EID INT NOT NULL, " +
                  " fName VARCHAR(255), " +
                  " lName VARCHAR(255), " +
                  " DateOfJoining DATE, " + 
                  " PRIMARY KEY ( EID ))";
            myStmt.executeUpdate(sql);
            
            // 4. Display main menu
            boolean exit = false;
            while (!exit) {
                System.out.println("\nMain Menu");
                System.out.println("(1) Add an employee");
                System.out.println("(2) Drop an employee");
                System.out.println("(3) Update the hiring date");
                System.out.println("(4) Count employees");
                System.out.println("(5) Display all employees");
                System.out.println("(6) Exit");
                System.out.print("Enter your choice: ");
                int choice = in.nextInt();
                
                switch (choice) {
                    case 1:  // Add an employee
                        addEmployee();
                        break;
                    case 2:  // Drop an employee
                        dropEmployee();
                        break;
                    case 3:  // Update the hiring date
                        updateHiringDate();
                        break;
                    case 4:  // Count employees
                        countEmployees();
                        break;
                    case 5:  // Display all employees
                        displayEmployees();
                        break;
                    case 6:  // Exit
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 6.");
                        break;
                }
            }
        }
        catch (Exception exc) {
            exc.printStackTrace();
        }
    }
    
    public static void addEmployee() {
        Scanner in = new Scanner(System.in);
        try {
            // Get employee details from user
            System.out.print("EID = ");
            int eid = in.nextInt();
            System.out.print("First Name = ");
            String fname = in.next();
            System.out.print("Last Name = ");
            String lname = in.next();
            System.out.print("Date of employment (yyyy-mm-dd) = ");
            String doj = in.next();
            
            // Check if employee already exists
            String sql = "SELECT * FROM Employee WHERE EID = ?";
            pstmt = myConn.prepareStatement(sql);
            pstmt.setInt(1, eid);
            myRs = pstmt.executeQuery();
            
            if (myRs.next()) {
                // Employee already exists
                System.out.println("EID should be unique. Employee not added.");
            } else {
                // Add employee to database
                sql = "INSERT INTO Employee " +
                      "(EID, fName, lName, DateOfJoining) " +
                      "VALUES (?, ?, ?, ?)";
                pstmt = myConn.prepareStatement(sql);
                pstmt.setInt(1, eid);
                pstmt.setString(2, fname);
                pstmt.setString(3, lname);
                pstmt.setString(4, doj);
                int rowsAffected = pstmt.executeUpdate();

                            // Check if insertion was successful
                            if (rowsAffected > 0) {
                                System.out.println("Employee added successfully.");
                            } else {
                                System.out.println("Employee not added. Please try again.");
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
    public static void dropEmployee() {
        Scanner in = new Scanner(System.in);
        try {
            // Get employee ID from user
            System.out.print("EID = ");
            int eid = in.nextInt();

            // Check if employee exists
            String sql = "SELECT * FROM Employee WHERE EID = ?";
            pstmt = myConn.prepareStatement(sql);
            pstmt.setInt(1, eid);
            myRs = pstmt.executeQuery();

            if (!myRs.next()) {
                // Employee doesn't exist
                System.out.println("Employee with EID " + eid + " does not exist.");
            } else {
                // Delete employee from database
                sql = "DELETE FROM Employee WHERE EID = ?";
                pstmt = myConn.prepareStatement(sql);
                pstmt.setInt(1, eid);
                int rowsDeleted = pstmt.executeUpdate();

                if (rowsDeleted > 0) {
                    System.out.println("Employee with EID " + eid + " deleted successfully.");
                } else {
                    System.out.println("Error deleting employee with EID " + eid + ".");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void updateHiringDate() {
        Scanner in = new Scanner(System.in);
        try {
            // Get employee EID and new hiring date from user
            System.out.print("EID = ");
            int eid = in.nextInt();
            System.out.print("New hiring date (yyyy-mm-dd) = ");
            String newDateOfJoining = in.next();

            // Check if employee exists
            String sql = "SELECT * FROM Employee WHERE EID = ?";
            pstmt = myConn.prepareStatement(sql);
            pstmt.setInt(1, eid);
            myRs = pstmt.executeQuery();

            if (!myRs.next()) {
                // Employee does not exist
                System.out.println("Employee with EID " + eid + " does not exist.");
            } else {
                // Update employee's hiring date in the database
                sql = "UPDATE Employee SET DateOfJoining = ? WHERE EID = ?";
                pstmt = myConn.prepareStatement(sql);
                pstmt.setString(1, newDateOfJoining);
                pstmt.setInt(2, eid);
                int rowsUpdated = pstmt.executeUpdate();

                if (rowsUpdated == 1) {
                    System.out.println("Hiring date for employee with EID " + eid + " updated successfully.");
                } else {
                    System.out.println("Failed to update hiring date for employee with EID " + eid + ".");
                }
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
    public static void countEmployees() {
        try {
            // Execute SQL Query
            String sql = "SELECT COUNT(*) FROM Employee";
            myStmt = myConn.createStatement();
            myRs = myStmt.executeQuery(sql);
            
            // Display results
            if (myRs.next()) {
                int count = myRs.getInt(1);
                System.out.println("Total number of employees: " + count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void displayEmployees() {
        try {
            // Execute SQL query to retrieve all employees
            String sql = "SELECT * FROM Employee";
            myStmt = myConn.createStatement();
            myRs = myStmt.executeQuery(sql);

            // Display employee details
            System.out.println("Employee Details:");
            while (myRs.next()) {
                int eid = myRs.getInt("EID");
                String fname = myRs.getString("fName");
                String lname = myRs.getString("lName");
                String doj = myRs.getString("DateOfJoining");

                System.out.printf("%d\t%s %s\t%s\n", eid, fname, lname, doj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}












