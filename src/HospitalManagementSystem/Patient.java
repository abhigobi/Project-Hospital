package HospitalManagementSystem;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {

    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection , Scanner scanner){

        this.connection = connection;
        this.scanner = scanner;
    }

    public void addPaitent(){
        System.out.println("Enter the Name: ");
        String name = scanner.next();
        System.out.println("Enter Patient Age: ");
        int age = scanner.nextInt();
        System.out.println("Enter Patient Gender: ");
        String gender = scanner.next();

        try{
            String query = "INSERT INTO patients(name,age,gender) VALUES(?,?,?)";
            PreparedStatement preparedstatement = connection.prepareStatement(query);

            preparedstatement.setString(1,name);
            preparedstatement.setInt(2,age);
            preparedstatement.setString(3,gender);

            int affectedRows = preparedstatement.executeUpdate();
            if(affectedRows>0){
                System.out.println("Patient Added sucessfully");
            }else{
                System.out.println("Failed to add Patient");
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void viewPatients(){
        String query = "select * from patients";

        try{

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultset = preparedStatement.executeQuery();
            System.out.println("Patients: ");
            System.out.println("+-------------+-------------------+----------+--------+");
            System.out.println("| Patient Id  | Name              | Age      | Gender  |");
            System.out.println("+-------------+-------------------+----------+--------+");
            while(resultset.next()){
                int id = resultset.getInt("id");
                String name = resultset.getString("name");
                int age = resultset.getInt("age");
                String gender = resultset.getString("gender");
                System.out.printf("|%-12s|%-20s|%-10s|%-9s\n ",id,name,age,gender);
                System.out.println("+------------+--------------------+--------+-------+");

            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public boolean getPatientById(int id){
        String query = "SELECT * FROM patients WHERE id = ? ";

        try{
            PreparedStatement preparedstatement = connection.prepareStatement(query);
            preparedstatement.setInt(1,id);

            ResultSet resultSet = preparedstatement.executeQuery();
            if(resultSet.next()){
                return true;
            }
            else{
                return false;
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
