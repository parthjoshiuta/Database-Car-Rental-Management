import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;
public class Customer {//Dhanesh:import mysql-connector-java-5.1.32.jar 
	private static String JDBC_URL = "jdbc:mysql://localhost:3306/CarRental?useSSL=false"; 
	private static String USER = "root"; 
	private static String PASSWORD = "root"; 

	public static void main(String[] args) throws IOException, ClassNotFoundException,NumberFormatException { 
	Connection myConnection = null; //Dhanesh:NOT CONNECTED
	Statement statement = null; 
	PreparedStatement preparedStatement = null; 	

	try{ 
	//Dhanesh:CONNECTING TO THE DATABASE
	Class.forName("com.mysql.jdbc.Driver");
	 myConnection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD); //Dhanesh:GIVING CREDENTIALS


	System.out.println("Connected to database"); //Dhanesh:CONNECTED 

	//CREATING A PREPARED STATEMENT 
	String insertTableSQL = "INSERT INTO customer" 
	+ "(CUSTOMER_ID, CUST_TYPE) VALUES" 
	+ "(?,?)"; 


	preparedStatement = myConnection.prepareStatement(insertTableSQL); 

	//Dhanesh:RETRIEVING INFORMATION FROM CSV FILE 

	//Dhanesh:opening a file input stream 
	BufferedReader reader = new BufferedReader(new FileReader("/Users/parth/eclipse-workspace/DB-Project2/src/Customer.csv")); 

	String line = null; //Dhanesh:line read from csv 
	Scanner scanner = null; //Dhanesh:scanned line 

	//Dhanesh:READING FILE LINE BY LINE AND UPLOADING INFORMATION TO DATABASE 
	while((line = reader.readLine()) != null){ 
	scanner = new Scanner(line); 
	scanner.useDelimiter(","); 
	while(scanner.hasNext()){ 
	preparedStatement.setInt(1,Integer.parseInt(scanner.next())); 
	preparedStatement.setString(2,scanner.next());
	}	
	preparedStatement.executeUpdate(); 
	} 

	preparedStatement.close(); 
	System.out.println("Data imported"); 

	reader.close(); //Dhanesh:closing CSV reader 

	} catch (SQLException e){ 
	e.printStackTrace(); 
	}finally{//Dhanesh:CLOSING CONNECTION 
	try{ 
	if(statement!=null) 
	myConnection.close(); 
	}catch(SQLException se){ 
	}//Dhanesh:do nothing 
	try{ 
	if(myConnection!=null) 
	myConnection.close(); 
	}catch(SQLException se){ 
	se.printStackTrace(); 
	} 
	System.out.println("Connection closed"); 
	} 
	} 

}
