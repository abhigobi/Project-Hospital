package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {

    private static final String url = "jdbc:mysql://127.0.0.1:3300/hospital";
    private static final String username = "root";
    private static final String password = "123456";

    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);
        try{
            Connection connection = DriverManager.getConnection(url,username,password);
            Patient patient = new Patient(connection,scanner);
            Doctors doctor = new Doctors(connection);
            while(true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patients");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.println("Enter your choice");
                int choice = scanner.nextInt();

                switch(choice){
                    case 1:
                        patient.addPaitent();

                    case 2:
                        patient.viewPatients();

                    case 3:
                        doctor.viewDoctors();

                    case 4:

                    case 5:
                        return;

                    default:
                        System.out.println("Enter the valid choice");

                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public static void bookAppointment( Patient patient,Doctors doctor,Connection connection,Scanner scanner){
        System.out.println("Enter the patient ID: ");
        int patientId = scanner.nextInt();
        System.out.println("Enter the Doctor ID: ");
        int doctorId = scanner.nextInt();
        System.out.println("Enter the Appointment Date (YYYY-MM-DD) ");
        String appointmentDate = scanner.next();

        if(patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)){
            if(checkDoctor(doctorId,appointmentDate,connection)){
                String appointQuery = "INSERT INTO appointments(patient_id , doctor_id, appointment_date) VALUES(?,?,?);";
                   try{
                       PreparedStatement preparedStatement = connection.prepareStatement(appointQuery);
                       preparedStatement.setInt(1,patientId);
                       preparedStatement.setInt(2,doctorId);
                       preparedStatement.setString(3,appointmentDate);
                       int rowsAffected = preparedStatement.executeUpdate();
                       if(rowsAffected>0){
                           System.out.println("Appointment booked");
                       }
                       else{
                           System.out.println("Failed to book appointment");
                       }
                   }catch(SQLException e){
                       e.printStackTrace();
                   }
            }
            else{
                System.out.println("Doctor Not available on this date!!");
            }
        }else{
            System.out.println("Either doctor OR doesn't exit");
        }
    }
    static public boolean checkDoctor(int doctorId , String appointmentDate , Connection connection){

        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                if(count==0){
                    return true;
                }else{
                    return false;
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
