import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
public class rentals {//Dhanesh:import mysql-connector-java-5.1.32.jar 
	private static String JDBC_URL = "jdbc:mysql://localhost:3306/CarRental?useSSL=false"; 
	private static String USER = "root"; 
	private static String PASSWORD = "root"; 

	public static void main(String[] args) throws IOException, ClassNotFoundException,NumberFormatException, ParseException { 
	Connection myConnection = null; //Dhanesh:NOT CONNECTED
	Statement statement = null; 
	PreparedStatement preparedStatement = null; 	

	try{ 
	//Dhanesh:CONNECTING TO THE DATABASE
	Class.forName("com.mysql.jdbc.Driver");
	 myConnection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD); //Dhanesh:GIVING CREDENTIALS


	System.out.println("Connected to database"); //Dhanesh:CONNECTED 

	//CREATING A PREPARED STATEMENT 
	String insertTableSQL = "INSERT INTO rentals" 
	+ "(RENTAL_ID,CUSTOMER_ID, VEHICLE_ID, RSTART_DATE, RENTAL_TYPE,REND_DATE,NO_OF_DAYS,NO_OF_WEEKS,AMT_DUE) VALUES" 
	+ "(?,?,?,?,?,?,?,?,?)"; 


	preparedStatement = myConnection.prepareStatement(insertTableSQL); 

	//Dhanesh:RETRIEVING INFORMATION FROM CSV FILE 

	//Dhanesh:opening a file input stream 
	BufferedReader reader = new BufferedReader(new FileReader("/Users/parth/eclipse-workspace/DB-Project2/src/Rentals.csv")); 

	String line = null; //Dhanesh:line read from csv 
	Scanner scanner = null; //Dhanesh:scanned line 

	SimpleDateFormat date = new SimpleDateFormat("MM/dd/yyyy");
	//Dhanesh:READING FILE LINE BY LINE AND UPLOADING INFORMATION TO DATABASE 
	while((line = reader.readLine()) != null){ 
	scanner = new Scanner(line); 
	scanner.useDelimiter(","); 
	while(scanner.hasNext()){ 
	
	
	preparedStatement.setInt(1,Integer.parseInt(scanner.next()));
	preparedStatement.setInt(2,Integer.parseInt(scanner.next()));
	preparedStatement.setInt(3,Integer.parseInt(scanner.next()));
	try { 
		java.util.Date d; 
		d = date.parse(scanner.next());
		preparedStatement.setDate(4, new java.sql.Date(d.getTime())); 
	}catch (Exception e) { 
		e.printStackTrace(); 
		}
	preparedStatement.setString(5,scanner.next());
	String g = scanner.next();
	if(g.equals("")){
		preparedStatement.setNull(6, Types.INTEGER);
		
		}else{
			java.util.Date d; 
			d = date.parse(scanner.next());
			preparedStatement.setDate(6, new java.sql.Date(d.getTime()));
		}
	String st=scanner.next();
	if(st.equals("")){
		preparedStatement.setNull(7, Types.INTEGER);
		
		}else{
			
			preparedStatement.setInt(7,Integer.parseInt(st));
		}
	String sq=scanner.next();
	if(sq.equals("")){
		
		preparedStatement.setNull(8, Types.INTEGER);
		
		}else{
			preparedStatement.setInt(8,Integer.parseInt(sq));
		}
	
	
	String ab;
    if(scanner.hasNext()==false){
		preparedStatement.setNull(9, Types.INTEGER);
		
		}else{
			ab=scanner.next();
			preparedStatement.setInt(9,Integer.parseInt(ab));
		}
	
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
