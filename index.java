import java.util.* ;
import java.util.concurrent.TimeUnit;

import com.mysql.jdbc.TimeUtil;

import java.sql.*;
import java.text.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class index {
	private static String JDBC_URL = "jdbc:mysql://localhost:3306/CarRental"; 
	private static String USER = "root"; 
	private static String PASSWORD = "root"; 
	private static Connection myConnection = null; 
	public static void main(String args[]) throws ClassNotFoundException
	{
		int input; 
		Scanner sc = new Scanner(System.in);
		Connection myConnection = null; 
		PreparedStatement preparedStatement = null;
		Statement statement = null; 	
		String b= null;
		SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
		try{ 
		//CONNECTING TO THE DATABASE
		Class.forName("com.mysql.jdbc.Driver");
		myConnection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD); 
		System.out.println("Connected to database"); //CONNECTED 
		// Welcome Screen : Parth Joshi 29/11/17
		System.out.println("========WELCOME TO ABC CAR RENTAL COMPANY========");
		do {	
		//Take user input : Parth Joshi 29//11/17 
		System.out.println("What would you like to do ?");
		System.out.println("1. Add a New Customer");
		System.out.println("2. Add a New Car");
		System.out.println("3. Rent a New Car");
		System.out.println("4. Return a car");
		System.out.println("5. View Active Rentals");
		System.out.println("6. View Scheduled Rentals");
		System.out.println("7. View Available Cars");
		System.out.println("8. Update Rental Rates");
		System.out.println("9. View Owner Earnings");
		System.out.println("10. View Company Earnings");
		System.out.println("11. View All Customers");
		System.out.println("12. View Our Fleet");
		System.out.println("13. Exit");
		System.out.println("Please Choose 1 option to Continue");
		input = sc.nextInt();
		if(input > 13)
		{
			System.out.println("Invalid Input. Please Try Again. Enter a value between 1 and 13");
			continue ;
		}
		switch(input)
		{
		// Adding a New Customer - Parth Joshi 01/12/2017	
		case 1 :
				String CustType, SSN = null, Name = null, CName = null, Caddress = null, Bday, MAX_CID, MCID = null, N_Cust, N_Comp, N_Ind;
				java.util.Date DOB = null;
				long phno = 0;
				int flag = 0, CID; 
				do
				{
				// Taking Customer Type for Input
				System.out.println("What Type of Customer ? INDIVIDUAL OR COMPANY");
				CustType = sc.next();
				//Taking input for Individual Parth Joshi 02/12/2017
				if (CustType.equals("INDIVIDUAL"))
				{
					System.out.println("Enter Your Full Name");
					Name = sc.nextLine();
					int f = 0;
					do
					{
					System.out.println("Enter your SSN Eg. ABC123EXT");
					SSN = sc.nextLine();
					if (SSN.length() == 9) // check for a valid SSN 
					{
						f = 1;
					}
					else
					{
						System.out.println("Invalid SSN. Please Enter Again");
					}
					}while(f != 1);
					System.out.println("Enter your Birthdate in format(DD/MM/YYYY)");
					Bday = sc.nextLine();
					try {
						DOB = myFormat.parse(Bday);
					}catch(Exception e)
					{
						e.printStackTrace();
					}
					System.out.println("Enter your Phone Number");
					phno = sc.nextLong();
					flag = 1;
				}
				//Taking input for Company Parth Joshi - 02/12/2017
				else if(CustType.equals("COMPANY"))
				{
					System.out.println("Enter Company Name");
					CName = sc.nextLine();
					System.out.println(CName);
					System.out.println("Enter Company Address");
					Caddress = sc.nextLine();
					flag = 1;
				}
				else 
					System.out.println("Invalid Input. Please Enter Again");
				}while(flag != 1);
				PreparedStatement ps = null, qs = null, ss = null;
				//FInd max customer ID from Customer Table Parth Joshi 02/12/2017
				MAX_CID = "SELECT MAX(CUSTOMER_ID) FROM CUSTOMER";
				try {
					ps = myConnection.prepareStatement(MAX_CID);
					ResultSet rs = ps.executeQuery(MAX_CID);
					while(rs.next())
					{
						MCID = rs.getString(1);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				ps.close();
				CID = Integer.parseInt(MCID);
				N_Cust = "INSERT INTO customer" + 
						 "(CUSTOMER_ID, CUST_TYPE) VALUES" + 
						 "(?,?)";
				N_Comp = "INSERT INTO company" + 
						 "(CNAME, CUSTOMER_ID, C_ADDRESS) VALUES" + 
						 "(?,?,?)"; 
				N_Ind = "INSERT INTO individual" + 
						"(SSN, CUSTOMER_ID,NAME,DOB,I_PH_NO,OID) VALUES"  + 
						"(?,?,?,?,?,?)";
				if(CustType.equals("INDIVIDUAL"))
				{	
					ps = myConnection.prepareStatement(N_Cust);
					ps.setInt(1, CID+1);
					ps.setString(2, CustType);
					ps.executeUpdate();
					qs = myConnection.prepareStatement(N_Ind);
					qs.setString(1, SSN);
					qs.setInt(2, CID+1);
					qs.setString(3, Name);
					qs.setDate(4, new java.sql.Date(DOB.getTime()));
					qs.setLong(5, phno);
					qs.setNull(6, Types.INTEGER);
					qs.executeUpdate();
					qs.close();
					
				}
				else 
				{
					ps = myConnection.prepareStatement(N_Cust);
					ps.setInt(1, CID+1);
					ps.setString(2, CustType);
					ps.executeUpdate();
					ss = myConnection.prepareStatement(N_Comp);


					ss.setInt(2, CID+1);
					ss.setString(3, Caddress);
					ss.executeUpdate();
					ss.close();
					
				}
				System.out.println("Customer Created Successfully");
				
				
				continue;
			case 2 :
				SimpleDateFormat date = new SimpleDateFormat("MM/dd/yyyy");
		        int val2=0;
			    Boolean j=false;
			    Statement stmt3 = myConnection.createStatement(
		             ResultSet.TYPE_SCROLL_INSENSITIVE,
		             ResultSet.CONCUR_UPDATABLE);
			         
			 		 ResultSet VEHICLE_ID = stmt3.executeQuery("SELECT MAX(VEHICLE_ID) from Cars");
			 		 while(VEHICLE_ID.next())
			 		 {
			 			 val2= (VEHICLE_ID.getInt(1));
			 		 }
			 		
			Statement stmt1 = myConnection.createStatement(
		             ResultSet.TYPE_SCROLL_INSENSITIVE,
		             ResultSet.CONCUR_UPDATABLE);
			 		 ResultSet OWNER_ID = stmt1.executeQuery("SELECT OWNER_ID FROM owner");
			 		List<Integer>results = new ArrayList<Integer>();
			 		
			 		while(OWNER_ID.next())
			 		{
			 			results.add(OWNER_ID.getInt(1));
			 		}
			 		
			   
			String COLOR,VMODEL_NAME = "",MODEL_TYPE;
			int LEASE_TERM,VMODEL_YEAR,WRATE,DRATE,n,VOWNER_ID;
			
			String PURCHASE_DATE,LEASE_START_DATE,ooid;
			System.out.println("How Many Cars you want to add:");
			n=sc.nextInt();
			for(int i=0;i<n;i++)
			{
				int h=1;
				System.out.println("Enter the Owner Type");
				ooid=sc.next();
				System.out.println("Enter the Vehicle Type:");
				
				MODEL_TYPE=sc.next();
				
			System.out.println("Enter the car owner's OWNER_ID");
			VOWNER_ID=sc.nextInt();
			for(int m=0;m<results.size();m++)
			{
			if(VOWNER_ID==results.get(m))
			{
			System.out.println("Found");
			j=true;
			break;
			}else{
				System.out.println("Not Found");
			}
			}
			
			System.out.println("Enter the Model Name:");
			sc.nextLine();
			VMODEL_NAME=sc.nextLine();
			System.out.println("ENTER the model Year:");
			VMODEL_YEAR=sc.nextInt();
			
			Statement stmt4=myConnection.createStatement(
		            ResultSet.TYPE_SCROLL_INSENSITIVE,
		            ResultSet.CONCUR_UPDATABLE);
			 		 ResultSet IsModelNamePresent = stmt4.executeQuery("SELECT MODELNAME,MODELYEAR from Model where MODELNAME='"+VMODEL_NAME+"' and MODELYEAR="+VMODEL_YEAR);
			
			Boolean abc=IsModelNamePresent.next();
			while(h!=0)
			{
			if(j==false)	
				{
					String xyz = "INSERT INTO owner"+ "(OWNER_ID, OWNER_TYPE) VALUES" 
							+ "(?,?)";
					preparedStatement = myConnection.prepareStatement(xyz);
					preparedStatement.setInt(1,VOWNER_ID); 
					preparedStatement.setString(2,ooid);
					preparedStatement.executeUpdate();
					System.out.println("Owner_ID added to Table Owner.");
					j=true;
				}
			else if(abc==false)
					{
						String xyz1="INSERT INTO MODEL"+"(MODELNAME, MODELYEAR) VALUES"+"(?,?)";
						preparedStatement = myConnection.prepareStatement(xyz1);
						preparedStatement.setString(1,VMODEL_NAME);
						preparedStatement.setInt(2,VMODEL_YEAR); 
						preparedStatement.executeUpdate();
						abc=true;
						System.out.println("MODELNAME and MODEL YEAR IS ADDED TO MODEL. YOU CAN NOW CONTINUE.");
					}
			else{
					
				System.out.println("What is the color of the car:");
				COLOR=sc.next();
				System.out.println("Enter the Purchase Date of the car");
		        PURCHASE_DATE = sc.next();
				System.out.println("Enter the Lease Start Date of the car");
				LEASE_START_DATE=sc.next();
				System.out.println("Enter the Lease term");
				LEASE_TERM=sc.nextInt();
				
				
				System.out.println("Enter the Weekly Rate:");
				WRATE=sc.nextInt();
				System.out.println("Enter the Daily Rate:");
				DRATE=sc.nextInt();	
				
			String insertTableSQL = "INSERT INTO cars" 
						+ "(VEHICLE_ID, COLOR,VOWNER_ID, PURCHASE_DATE, LEASE_START_DATE, LEASE_TERM, END_DATE, VMODEL_NAME, VMODEL_YEAR, VTYPE, WRATE, DRATE) VALUES"
						+ "(?,?,?,?,?,?,?,?,?,?,?,?)";
			preparedStatement = myConnection.prepareStatement(insertTableSQL);
			
			preparedStatement.setInt(1,val2+n); 
			preparedStatement.setString(2,COLOR);
			preparedStatement.setInt(3,VOWNER_ID);
			try { 
				java.util.Date d; 
				d = date.parse(PURCHASE_DATE);
				preparedStatement.setDate(4, new java.sql.Date(d.getTime()));
			}catch (Exception e) { 
				e.printStackTrace(); 
				}
			try { 
				java.util.Date d; 
				d = date.parse(LEASE_START_DATE);
				preparedStatement.setDate(5, new java.sql.Date(d.getTime())); 
			} catch (Exception e) { 
				e.printStackTrace(); 
				}
			preparedStatement.setInt(6,LEASE_TERM);
			preparedStatement.setString(7,null);
			
			preparedStatement.setString(8,VMODEL_NAME);
			preparedStatement.setInt(9,VMODEL_YEAR);
			preparedStatement.setString(10,MODEL_TYPE);
			preparedStatement.setInt(11,WRATE);
			preparedStatement.setInt(12,DRATE);
			
			preparedStatement.executeUpdate();
			System.out.println("DATA IMPORTED");
			h=0;
			}
			

			}

			}	
			preparedStatement.close();
				continue;
			case 3 :

				int answer,weeks, n1=1, answer1,answer2,val,Owner_ID,m=1,o=1,p=1,q4=1,Customer_ID=0,car_rent,DW_Ans,Max_RentalID=0,No_of_Days = 0,No_of_Weeks = 0,la=1,lb=1,dha=0,hdsb=1;
				long P_No;
				Boolean abc,dbc;
				Calendar cal = Calendar.getInstance();
				String I_Name, B_Date, ssn, COMPANY_NAME,COMPANY_ADDRESS, r;

				System.out.println("Connected to database");
			    SimpleDateFormat datef = new SimpleDateFormat("MM/dd/yyyy");
			    stmt3 = myConnection.createStatement(
			             ResultSet.TYPE_SCROLL_INSENSITIVE,
			             ResultSet.CONCUR_UPDATABLE);
				         
				 		 ResultSet CSS_ID = stmt3.executeQuery("SELECT MAX(CUSTOMER_ID) from CUSTOMER");
				 		 while(CSS_ID.next())
				 		 {
				 			 Customer_ID= (CSS_ID.getInt(1));
				 		 }
			       	System.out.println("Do you require it for personal use for your Company.\n 1.Personal Use   2. Company");
			    	answer1=sc.nextInt();
			    	if(answer1==1)
			    	{   
			    		System.out.println("Do you have a Customer ID.\n 1.YES   2.NO");
			    	    answer=sc.nextInt();
			    	    
			    	    if(answer==1)
			    	    {
			    	    	System.out.println("Enter your Customer ID.");
			    	    	val=sc.nextInt();
			    	    	while(n1!=0)
			    	    	{
			    	    		
			    	    		 stmt1 = myConnection.createStatement(
			    	   	             ResultSet.TYPE_SCROLL_INSENSITIVE,
			    	   	             ResultSet.CONCUR_UPDATABLE);
			    	   		 		 ResultSet CCID = stmt1.executeQuery("SELECT CUSTOMER_ID FROM CUSTOMER where CUSTOMER_ID="+val);
			    	   		         abc=CCID.next();
			    	   		         if(abc==false)
			    	   		         {
			    	   		        	 System.out.println("Wrong Customer_ID. Enter Your Customer_ID again");
			    	   		        	 val=sc.nextInt();
			    	   		         }
			    	   		         else{
			    	   		        	 n1=0;
			    	   		         }
			    	    	}
			    		
			    		
			    	    }
			    	    else{
			    	    	
			    	    val=Customer_ID+1;
			    		
			    		System.out.println("Enter Your SSN:");
			    		ssn=sc.next();
			    		while(o!=0)
				    	{
				    		stmt1 = myConnection.createStatement(
				   	             ResultSet.TYPE_SCROLL_INSENSITIVE,
				   	             ResultSet.CONCUR_UPDATABLE);
				   		 		 ResultSet Individual_ssn = stmt1.executeQuery("SELECT SSN FROM INDIVIDUAL where SSN='"+ssn+"'");
				   		         dbc=Individual_ssn.next();
				   		         if(dbc==true)
				   		         {
				   		        	 System.out.println("SSN already Exist. Enter different SSN");
				   		        	 ssn=sc.next();
				   		         }
				   		         else{
				   		        	 o=0;
				   		         }
				    	}
			    		
				    	
			    	
			    		System.out.println("Enter Your Name:");
			    		sc.nextLine();
			    		I_Name=sc.nextLine();
			    		System.out.println("Enter Your DOB. It should be of the form MM/dd/yyyy");
			    		B_Date=sc.next();
			    		System.out.println("Enter Your Phone Number. It should be of 10 Digits");
			    		P_No=sc.nextLong();
			    		String insertTableSQL1 = "INSERT INTO customer" 
			    				+ "(CUSTOMER_ID, CUST_TYPE) VALUES" 
			    				+ "(?,?)"; 
			    		preparedStatement = myConnection.prepareStatement(insertTableSQL1);
			    		preparedStatement.setInt(1,val); 
			    		preparedStatement.setString(2,"INDIVIDUAL");
			    		preparedStatement.executeUpdate();
			    		
			    		String insertTableSQL = "INSERT INTO individual" 
			    				+ "(SSN, CUSTOMER_ID,NAME,DOB,I_PH_NO,OID) VALUES" 
			    				+ "(?,?,?,?,?,?)"; 
			    		preparedStatement = myConnection.prepareStatement(insertTableSQL);
			    		
			    		preparedStatement.setString(1,ssn);
			    		preparedStatement.setInt(2,val); 
			    		preparedStatement.setString(3,I_Name);
			    		try { 
			    			java.util.Date d4; 
			    			d4 = datef.parse((B_Date));
			    			preparedStatement.setDate(4, new java.sql.Date(d4.getTime()));
			    		}catch (Exception e) { 
			    			e.printStackTrace(); 
			    			}	
			    		
			    		preparedStatement.setLong(5,P_No);
			    		preparedStatement.setNull(6,Types.INTEGER);
			    		preparedStatement.executeUpdate();	
			    		
			    		System.out.println("You are added as our Customer. Your CUSTOMER ID is"+(val));

			    		}
			    	}

			    	else{
			    		System.out.println("Enter Your Company Name");
			    		sc.nextLine();
			    		COMPANY_NAME=sc.nextLine();
			    		while(m!=0)
				    	{
				    		Statement stmt4 = myConnection.createStatement(
				   	             ResultSet.TYPE_SCROLL_INSENSITIVE,
				   	             ResultSet.CONCUR_UPDATABLE);
				   		 		 ResultSet C_NAME = stmt4.executeQuery("SELECT CNAME FROM COMPANY where CNAME='"+COMPANY_NAME+"';");
				   		         dbc=C_NAME.next();
				   		         if(dbc==true)
				   		         {
				   		        	 System.out.println("Company Name already Exist. Enter New Company Name");
				   		        	 COMPANY_NAME=sc.next();
				   		         }
				   		         else{
				   		        	 m=0;
				   		         }
				   		         
				    	}
			    		 System.out.println("Do you have a Customer ID.\n 1.YES   2.NO");
			    		    answer=sc.nextInt();
			    		    if(answer==1)
			    		    {
			    		    	System.out.println("Enter your Customer ID.");
			    		    	val=sc.nextInt();
			    		    	while(q4!=0)
			    		    	{
			    		    		stmt1 = myConnection.createStatement(
			    		   	             ResultSet.TYPE_SCROLL_INSENSITIVE,
			    		   	             ResultSet.CONCUR_UPDATABLE);
			    		   		 		 ResultSet CCID = stmt1.executeQuery("SELECT CUSTOMER_ID FROM CUSTOMER where CUSTOMER_ID="+val);
			    		   		         abc=CCID.next();
			    		   		         if(abc==false)
			    		   		         {
			    		   		        	 System.out.println("Wrong Customer_ID. Enter Your Customer_ID again");
			    		   		        	 val=sc.nextInt();
			    		   		         }
			    		   		         else{
			    		   		        	 q4=0;
			    		   		         }
			    		    	}
			    		    }
			    		    
			    		    	else
			    		    {
			    		    	val=Customer_ID+1;
			    				 		 
			    		    }
			    		    System.out.println("Enter Company Address:");
			    		    sc.nextLine();
			    		    COMPANY_ADDRESS=sc.nextLine();
			    		    
			    		    String insertTableSQL3 = "INSERT INTO customer" 
				    				+ "(CUSTOMER_ID, CUST_TYPE) VALUES" 
				    				+ "(?,?)"; 
				    		preparedStatement = myConnection.prepareStatement(insertTableSQL3);
				    		preparedStatement.setInt(1,val); 
				    		preparedStatement.setString(2,"COMPANY");
				    		preparedStatement.executeUpdate();
				    		
			    		    String insertTableSQL2 = "INSERT INTO company" 
			    		    		+ "(CNAME, CUSTOMER_ID, C_ADDRESS) VALUES" 
			    		    		+ "(?,?,?)";
			    		    preparedStatement = myConnection.prepareStatement(insertTableSQL2);
			    		    preparedStatement.setString(1,COMPANY_NAME);
			    			preparedStatement.setInt(2,val); 
			    			preparedStatement.setString(3,COMPANY_ADDRESS);
			    			
			    			preparedStatement.executeUpdate();
			    			
			    			System.out.println("You are added as our Customer. Your CUSTOMER ID is"+(val));
			    	}
			    	
			    	java.util.Date c4 = null;
			    	String dc;
					CallableStatement cst = myConnection.prepareCall("{CALL avCars(?, ?)}");
					System.out.println("Rental Start Date (MM/dd/YYYY)");
					sc.nextLine();
					r = sc.nextLine();
					LocalDate today=LocalDate.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
					String dateString = today.format(formatter);
					java.util.Date date3;
					while(la!=0)
					{
					java.util.Date date1 = null;
					try {
						date1 = datef.parse(r);
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					java.util.Date date2 = null;
					try {
						date2 = datef.parse(dateString);
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					
					if(date1.compareTo(date2)<0)
					{
						System.out.println("Wrong Date Entered. Date Should be today's date or future date.Enter the Start Date again ");
						r = sc.nextLine();
						
					}
					
					else
					{
						la=0;
						 System.out.println("How will you like to rent your car.\n 1.Daily   2.Weekly");
						 DW_Ans=sc.nextInt();
						    if(DW_Ans==1)
						    {
						    	System.out.println("Enter The number of days you want to rent the car");
						    	No_of_Days=sc.nextInt();
						    	
						    	System.out.println(r);
								try {
									c4 = date1;
									cst.setDate(1, new java.sql.Date(c4.getTime()));
								}catch(Exception e)
								{
									e.printStackTrace();
								}
								try {
									dc=(datef.format(c4.getTime() + TimeUnit.DAYS.toMillis( No_of_Days )));
									date3=datef.parse(dc);
									cst.setDate(2, new java.sql.Date(date3.getTime()));
								}catch(Exception e)
								{
									e.printStackTrace();
								}
								System.out.println("======Available Cars======");
								cst.execute();
								ResultSet w = cst.getResultSet();				
					     		DBTablePrinter.printResultSet(w);
								cst.execute();
						    }
								
						    else
						    {
						    	System.out.println("Enter The number of Weeks you want to rent the car");
						    	No_of_Weeks=sc.nextInt();
						    	weeks=No_of_Weeks*7;
						    	System.out.println(r);
								try {
									c4 = date1;
									cst.setDate(1, new java.sql.Date(c4.getTime()));
								}catch(Exception e)
								{
									e.printStackTrace();
								}
								
								try {
									
									/*Calendar cal1= Calendar.getInstance();
									cal1.setTime(date1);
									cal1.add(Calendar.DAY_OF_MONTH,weeks);
									String pe=date.format(cal1.getTime());
									dc=date.parse(pe);*/
									dc=(datef.format(c4.getTime() + TimeUnit.DAYS.toMillis( weeks )));
									date3=datef.parse(dc);
									cst.setDate(2, new java.sql.Date(date3.getTime()));
									
								}catch(Exception e)
								{
									e.printStackTrace();
								}
								System.out.println("======Available Cars======");
								cst.execute();
								ResultSet w = cst.getResultSet();				
					     		DBTablePrinter.printResultSet(w);
								
								
					}  	
				cst.execute();
				ResultSet z = cst.getResultSet();
			    System.out.println("Enter the Vehicle ID which you want to select");
			    car_rent=sc.nextInt();
			    
			    Statement stmt6 = myConnection.createStatement(
			             ResultSet.TYPE_SCROLL_INSENSITIVE,
			             ResultSet.CONCUR_UPDATABLE);
				         
				 		 ResultSet CSS_ID2 = stmt6.executeQuery("SELECT MAX(RENTAL_ID) from Rentals");
				 		 while(CSS_ID2.next())
				 		 {
				 			 Max_RentalID= (CSS_ID2.getInt(1));
				 		 }
			    List<Integer>results1 = new ArrayList<Integer>();
			    List<String>results2 = new ArrayList<String>();
		 		List<String>results3 = new ArrayList<String>();
		 		List<String>results4 = new ArrayList<String>();
		 		while(z.next())
		 		{
		 			results1.add(z.getInt(1));
		 			results2.add(z.getString(2));
		 			results3.add(z.getString(3));
		 			results4.add(z.getString(4));
		 				
		 		}
			    
			   for(int ji=0;ji<results1.size();ji++) 
			   {
				   if(results1.get(ji)==car_rent)
			   
			    	{
			    		dha=1;
			    		hdsb=0;
			    	}

			   }

			    	
			    
			  while(dha==1)
			  {
			    if(DW_Ans==1)
			    {
			    	String insertTableSQL7 = "INSERT INTO rentals" 
			    			+ "(RENTAL_ID,CUSTOMER_ID, VEHICLE_ID, RSTART_DATE, RENTAL_TYPE,REND_DATE,NO_OF_DAYS,NO_OF_WEEKS,AMT_DUE) VALUES" 
			    			+ "(?,?,?,?,?,?,?,?,?)";
			    	preparedStatement = myConnection.prepareStatement(insertTableSQL7); 
			    	
			    	preparedStatement.setInt(1,Max_RentalID+1);
			    	preparedStatement.setInt(2,val);
			    	
			    	preparedStatement.setInt(3,results1.get(car_rent));
			    	try { 
			    		java.util.Date x; 
			    		x = datef.parse(r);
			    		preparedStatement.setDate(4, new java.sql.Date(x.getTime())); 
			    	}catch (Exception e) { 
			    		e.printStackTrace(); 
			    		}
			    	preparedStatement.setString(5,"DAILY");
			        preparedStatement.setString(6,null);
			        preparedStatement.setInt(7,No_of_Days);	    		
			        preparedStatement.setNull(8,0);
			    	preparedStatement.setNull(9, Types.INTEGER);
			    	preparedStatement.executeUpdate(); 
			    	dha=0;
			    }
			    else
			    {
			    	String insertTableSQL8 = "INSERT INTO rentals" 
			    			+ "(RENTAL_ID,CUSTOMER_ID, VEHICLE_ID, RSTART_DATE, RENTAL_TYPE,REND_DATE,NO_OF_DAYS,NO_OF_WEEKS,AMT_DUE) VALUES" 
			    			+ "(?,?,?,?,?,?,?,?,?)";
			    	preparedStatement = myConnection.prepareStatement(insertTableSQL8);
			    	
			    	preparedStatement.setInt(1,Max_RentalID+1);
			    	preparedStatement.setInt(2,val);
			    	preparedStatement.setInt(3,car_rent);
			    	try { 
			    		java.util.Date x; 
			    		x = datef.parse(r);
			    		preparedStatement.setDate(4, new java.sql.Date(x.getTime())); 
			    		System.out.println(x);
			    	}catch (Exception e) { 
			    		e.printStackTrace(); 
			    		}
			    	preparedStatement.setString(5,"WEEKLY");
			        preparedStatement.setString(6,null);
			        preparedStatement.setInt(7,0);	    		
			        preparedStatement.setInt(8,No_of_Weeks);
			    	preparedStatement.setNull(9, Types.INTEGER);
			    	preparedStatement.executeUpdate();   
			    	dha=0;
			    }
			  
			  }
			    
			    
			    }
					}
					preparedStatement.close();

				continue;
			// Returning a Car Parth Joshi 02/12/2017
			case 4 :
				int reID ;
				String ans, dat; 
				System.out.println("Please Enter your Rental ID");
				reID = sc.nextInt();
				java.util.Date u = null ;
				CallableStatement pqr1 = myConnection.prepareCall("{CALL exist(?,?)}");
				PreparedStatement xyz = myConnection.prepareStatement("select * from rentals where rental_id = ?");
				pqr1.setInt(1, reID);
				pqr1.registerOutParameter(2, java.sql.Types.BOOLEAN);
				pqr1.executeUpdate();
				xyz.setInt(1, reID);
				
				boolean m1 = pqr1.getBoolean(2); 
				//If Rental ID exists, loop will be executed
				//Parth Joshi 02/12/2017
				if (m1 == true)
				{
					ResultSet n2 = xyz.executeQuery();
					System.out.println("Your Rental Details are as follows");
					System.out.println("Your Rental ID is " + reID);
					while(n2.next())
					{
					java.sql.Date ddf = n2.getDate(4);
					System.out.println("You Rented this car on " + ddf);
					}
					do {
						System.out.println("Would you like to Return your car today (Y/N)?");
						ans = sc.nextLine();
					if(ans.equals("Y") || ans.equals("y"))
					{
						CallableStatement q1 = myConnection.prepareCall("{CALL retCar(?,?,?)}");
						q1.setInt(1, reID);
						q1.setDate(2, new java.sql.Date(System.currentTimeMillis()));
						q1.registerOutParameter(3, java.sql.Types.INTEGER);
						q1.executeQuery();
						System.out.println("Your car has been returned successfully");
						System.out.println("Your updated payable amount is " + q1.getInt(3));
						break;
					}
					else if (ans.equals("N") || ans.equals("n"))
					{
						System.out.println("When would you like to Return your Car ?()");
						dat = sc.nextLine();
						try {
							 u = myFormat.parse(dat);
						}catch(Exception e)
						{
							e.printStackTrace();
						}
						CallableStatement q2 = myConnection.prepareCall("{CALL retCar(?,?,?)}");
						q2.setInt(1, reID);
						q2.setDate(2, new java.sql.Date(u.getTime()));
						q2.registerOutParameter(3, java.sql.Types.INTEGER);
						q2.executeQuery();
						System.out.println("Your car has been returned successfully");
						System.out.println("Your updated payable amount is " + q2.getInt(3));
						break;
					}
					else if (ans.equals("X") || ans.equals("x"))
					{
					break ; 	
					}
					else 
					{
						System.out.println("Invalid Input. Please Enter Again or press X to exit");	
					}
					}while(1 == 1);// Infinite loop as we are breaking the loop explicitly for all conditions 
				}
				else 
					System.out.println("Invalid Rental ID. Please try Again");
				continue;
			// Displaying active rentals Parth Joshi 02-12-2017
			case 5 :
				PreparedStatement p1 = null ; 
				p1 = myConnection.prepareStatement("select * from Rentals " +
						"where" + 
						"? BETWEEN RSTART_DATE AND REND_DATE;");
				System.out.println("======Currently Active Rentals======");
				p1.setDate(1, new java.sql.Date(System.currentTimeMillis()));
				ResultSet r1 = p1.executeQuery();				
				DBTablePrinter.printResultSet(r1);
				p1.close();
				continue;
			//Displaying scheduled Rentals Parth Joshi 02/12/2017
			case 6 :
				PreparedStatement z = null ; 
				z = myConnection.prepareStatement("select * from Rentals " +
						"where" + 
						"? <= RSTART_DATE;");
				System.out.println("======Scheduled Rentals======");
				z.setDate(1, new java.sql.Date(System.currentTimeMillis()));
				ResultSet y = z.executeQuery();				
				DBTablePrinter.printResultSet(y);
				z.close();
				continue;
			//Displaying Available cars based on Date Range Parth Joshi 02/12/2017
			case 7 :
				java.util.Date c6 = null, d4=null;
				CallableStatement cstm = myConnection.prepareCall("{CALL avCars(?, ?)}"); // Procedure Call 
				System.out.println("Rental Start Date (DD/MM/YYYY)");
				sc.nextLine();
				b = sc.nextLine();
				try {
					c6 = myFormat.parse(b);
				
					cstm.setDate(1, new java.sql.Date(c6.getTime()));
				}catch(Exception e)
				{
					e.printStackTrace();
				}
				System.out.println("Rental End Date (DD/MM/YYYY)");
				String f = sc.nextLine();
				try {
					d4 = myFormat.parse(f);
					cstm.setDate(2, new java.sql.Date(d4.getTime()));
				}catch(Exception e)
				{
					e.printStackTrace();
				}
				System.out.println("======Available Cars======");
				cstm.execute();
				ResultSet w = cstm.getResultSet();				
				DBTablePrinter.printResultSet(w);
				continue;
			case 8 :
				int nc=1,New_WRATE,New_DRATE;
				Boolean abcc=false;
				String VModel_Type;
				 statement = null; 
			
			
			   
				System.out.println("Enter Vehicle Type:");
				VModel_Type=sc.next();
				
				while(nc!=0)
				{
				stmt1 = myConnection.createStatement(
			             ResultSet.TYPE_SCROLL_INSENSITIVE,
			             ResultSet.CONCUR_UPDATABLE);
				 		 ResultSet OWNER_ID1 = stmt1.executeQuery("SELECT VTYPE FROM CARS where VTYPE='"+VModel_Type+"';");
				         abc=OWNER_ID1.next();
				         if(abcc==false)
				         {
				        	 System.out.println("Vehicle Type does not exist. Enter Type again:");
				        	 
				        	 VModel_Type=sc.next();
				         }
				         else{
				        	 nc=0;
				         }
				} 		 
				
				System.out.println("Please enter the New Weekly Rate:");
				New_WRATE=sc.nextInt();
				System.out.println("Please enter the New Daily Rate:");
				New_DRATE=sc.nextInt();
				
				String pqr3="UPDATE CARS SET WRATE= ?,DRATE= ? WHERE VTYPE= ?";
				preparedStatement = myConnection.prepareStatement(pqr3);
				preparedStatement.setInt(1,New_WRATE); 
				preparedStatement.setInt(2,New_DRATE);
				preparedStatement.setString(3,VModel_Type);
				preparedStatement.executeUpdate();
				
				preparedStatement.close();
				continue;
			case 9 :
				CallableStatement cstd = myConnection.prepareCall("{CALL avCars2()}");
				cstd.execute();
				ResultSet wp = cstd.getResultSet();
				String name;
				int dhap=0,da=0;
				 List<String>results1 = new ArrayList<String>();
				 List<Integer>results2 = new ArrayList<Integer>();
				 
				 while(wp.next())
					{
						results1.add(wp.getString(1));
						results2.add(wp.getInt(2));
					}
				 
				 System.out.println("Enter You Name :");
				 sc.nextLine();
				 name=sc.nextLine();
				 for(int ji=0;ji<results1.size();ji++) 
				   {
					   if(results1.get(ji).equals(name))
				    	{
				    		dhap=1;
				    		da=ji;
				    	}

				   }
				 if(dhap==1)
				 {
					 System.out.println("Your earning is "+results2.get(da));
				 }
				continue;

			case 10 :

				String StartDate, EndDate;
				int amount = 0;
				java.util.Date Start_Date = null;
				java.util.Date End_Date = null;
				SimpleDateFormat datez = new SimpleDateFormat("yyyy-MM-dd");
				System.out.println("Enter the Start Date:");
				StartDate=sc.next();
			try {
				Start_Date= datez.parse(StartDate);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				System.out.println("Enter the End Date:");
				EndDate=sc.next();
			try {
				End_Date=datez.parse(EndDate);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				stmt1 = myConnection.createStatement(
			            ResultSet.TYPE_SCROLL_INSENSITIVE,
			            ResultSet.CONCUR_UPDATABLE);
				 		CallableStatement cstz = myConnection.prepareCall("{CALL avCars5(?, ?)}");
				 		java.util.Date cz=null,dz=null;
				 		try {
							cz = Start_Date;
							cstz.setDate(1, new java.sql.Date(cz.getTime()));
						}catch(Exception e)
						{
							e.printStackTrace();
						}
						try {
						    dz = End_Date;
							System.out.println(dz);
						    cstz.setDate(2, new java.sql.Date(dz.getTime()));
						}catch(Exception e)
						{
							e.printStackTrace();
						}
				 		
						cstz.execute();
						ResultSet wz = cstz.getResultSet();				
			     		DBTablePrinter.printResultSet(wz);	

				continue;				
				//Displaying the list of all Customers Parth Joshi 02/12/2017 
			case 11 :
				System.out.println("======Our Customers======");
				PreparedStatement gst = myConnection.prepareStatement("Select Name from Individual union Select Cname from Company; "); 
				ResultSet rst = gst.executeQuery();
				DBTablePrinter.printResultSet(rst);
				gst.close();
				continue;
			//Displaying all the cars thatare present irrespective of availability
			//Parth Joshi 02/12/2017
			case 12 :
					System.out.println("======Our Fleet======");
					gst = myConnection.prepareStatement("Select ModelName,ModelYear from Model; "); 
					rst = gst.executeQuery();
					DBTablePrinter.printResultSet(rst);
					gst.close();
					continue;
			//Thank You Message 
			case 13 :
				ThankYou();
				continue;	
		}
		}while(input != 13);	
		
		} catch (SQLException e){ 
			e.printStackTrace(); 
			}finally{//CLOSING CONNECTION 
			try{ 
			if(statement!=null) 
			myConnection.close(); 
			}catch(SQLException se){ 
			}// do nothing 
			try{ 
			if(myConnection!=null) 
			myConnection.close(); 
			}catch(SQLException se){ 
			se.printStackTrace(); 
			} 
			System.out.println("Connection closed"); 
			}
	}
	// Function to display Exit Message
	static void ThankYou() {
		System.out.println("Thank You for Visiting ABC Car Rental Agency. Have a nice day");	
	}
	}