# Database-Car-Rental-Management

## Introduction

In this project, we will be designing a Database for Rental Car Company. There will be
many components that we will be taking care of. Some of the components are - Owner
of the cars, Car types, Rental information, Customers Databases, etc.<br><br>
For designing the EER model, we have used MIcrosoft Visio. The database is created
using MySQL workbench in local environment in MySQL. We will be having many data
fields in the form of CSV file which we will be importing into the database using one or
more java programs. We will be connecting to the database using JDBC.<br><br>
The main purpose of the database to maintain the information of all the cars that are
available for rentals, the owners of each car, the rental prices on a weekly and a daily
basis, the information of each rental, available cars at every point of time,etc.<br><br>
During the course of designing, there were various points at which we were required to
automatically update a one or more records on creation of records in a particular table.
In order to achieve this purpose, we have used triggers. And wherever there is reporting
involved, we have created stored procedures. Eg. To find out the weekly rentals
transactions, etc.<br><br>
While designing, we have created the database taking into consideration many which
we will be discussing in the next section.<br><br>

## Assumptions
The EER model has been created on the basis of the following assumptions :<br>
1. There will be only location that the customer will be picking up the car from. (Eg.
DFW Airport)

2. For point number 5 and 6, since all the attributes are derived, instead of storing
the values in a separate entity, we can directly store it as a view or get it on
demand by computing the result set whenever required. The structure of the view
will be shown in the further sections for understanding purposes.

3. We have assumed that for model names, there will be multiple values in the Car
entity, so to make it easier for update queries to run, we have made a separate
relation to maintain the list of models.

4. We have introduced a new relation by the name Duration which will be a weak
entity as it cannot exist by itself. It has to depend on the car that will be rented.
We have made this relation in order to store the duration for which each car is
being rented.

5. We are assuming that there can be only 1 owner of 1 vehicle and 1 owner can
have multiple vehicles under his name.

6. 1 Customer can rent multiple vehicles of multiple types.

7. All cars of the same type have the same rental rates.

8. We have assumed that the lease term of all the cars will be in number of years.

9. An individual owner of a car can also be a customer.

10. Any type of owner or Customer will have a single Contact Number.

11. Every customer can rent a car either on a daily basis or on a monthly basis.
<br><br>

Following important observations need to be considered in the EER model :

1. An individual can be a customer as well as an owner of the vehicles which is the
reason why we have used the same entity for both functionalities.

2. Since every customer has to compulsorily be an individual or a company it has to
be a total participation from customer towards the customer types i.e. company
and individual.

3. Likewise, every owner has to have a car and every car needs to have an owner.
Hence the Owner - Car relationship will be total participation both ways.

4. Every car need to belong to 1 type. Hence, there is a total participation from car
to the types and since every car can only belong to 1 type, it is a disjoint
specialisation.

5. Every owner has to compulsorily be an individual or a rental company or a bank
which is the reason why it will be a total participation from owner towards the
type of owners.

6. Every car needs to be of a particular model which is why it needs to be a total
participation and since 1 model can belong to multiple cars, it is a 1:N
relationship.

7. The entities Daily and Weekly can only exist for a particular rental which is why
we have kept them as weak entities as they cannot exist by themselves and
since we are assuming that any rental has to be either daily or weekly, we have
considered it to be total participation with disjoint specialisation.

8. Rented by relationship will have 3 participating relations which are Customers,
Cars and Rentals and the associated cardinality is shown in figure 1.

9. An Owner can have multiple cars in his name hence the relationship is 1:N

## EER to Relational Mapping

The following rules have been followed in order to convert the EER model into a relational
schema :<br>
Step 1 : Mapping the strong Entities<br>

1. There are 5 strong entities in the database that will be mapped first. They are Customer,
Car, Model, Rental and Owner.<br>

Step 2 : Mapping the Weak Entities<br>

1. There are 2 week entities daily and weekly. In order to model the weak entities, we will
introduce the type attribute in the parent entity i.e. Rentals and add the attributes of
weekly and daily in Rental itself.<br>

Step 3 : Mapping the 1: N relationships<br>

1. For mapping Owner and Car relationship, we will add the primary key of the one side as
the foreign key on the N side. Hence we have added the owner ID in the Car relation as
the foreign key and add all the attributes of the relation on the N side itself.

2. For mapping Car and Model relationship, we will follow the same procedure and insert
the primary key of 1 side as the foreign key on the N side.
<br>

Step 4 : Mapping N-Ary Relationships<br>

1. There is only 1 N-Ary relationship in the database i.e. Rented by. 3 relations participate
in the relationship, hence it can be called as a ternary relationship. In order to map the
ternary relationship we will create a separate relation Rented by which will contain the
primary keys of all the participating entities i.e. Vehicle_id from Car, Customer_ID from
Customers and Rental_ID from Rentals. And the primary key for this relation would be
the combination of all these primary keys.
<br>

Step 5 : Mapping specialisations<br>

1. Every car can be of multiple types. Eg. SUV, compact, etc. Here we have made a
disjoint specialisation. In order to map this, we have added a Car type attribute in the Car
relation itself and have also added the the attributes of the subclasses in the superclass
and since the attribute names are the same in the subclasses, there will not be duplicate
superclass , only 2 attributes will be enough to do the job.
<br>

Step 6 : Mapping Union Types<br>

1. Customers is a union of Individuals and Companies. In order to map into a relational
schema, we will put the primary key of customers in each of the subclasses and also we
will add another field for identifying the type of customer in the customer table.

2. Owners is a union of Rental Company, Individuals and Banks. In order to map into a
relational schema, we will follow the same procedure as point 1 ie. we will insert the
primary key of Owner as a foreign key in each of the subclasses and we will also add an
Owner type attribute in the Owner table in order to identify the type of owner.
<br>

## Creation of Database

The create table statements for each of the tables can be shown as follows :

CREATE TABLE OWNER (<br>
OWNER_ID INT(8) NOT NULL PRIMARY KEY,<br>
OWNER_TYPE VARCHAR(30) NOT NULL<br>
);<br><br>
CREATE TABLE MODEL (<br>
MODELNAME VARCHAR(30) NOT NULL,<br>
MODELYEAR INT(4) NOT NULL,<br>
PRIMARY KEY(MODELNAME, MODELYEAR)<br>
);<br><br>
CREATE TABLE BANK (<br>
BANKNAME VARCHAR(30) NOT NULL PRIMARY KEY,<br>
OID INT(8) NOT NULL,<br>
B_ADDRESS VARCHAR(300),<br>
CONSTRAINT FKEY_OID<br>
FOREIGN KEY (OID) REFERENCES OWNER(OWNER_ID));<br><br>
CREATE TABLE RENTAL_COMPANY (<br>
CNAME VARCHAR(30) NOT NULL PRIMARY KEY,<br>
OID INT(8) NOT NULL,<br>
RC_ADDRESS VARCHAR(300),<br>
CONSTRAINT FKEY_RC<br>
FOREIGN KEY (OID) REFERENCES OWNER(OWNER_ID));<br><br>
CREATE TABLE CUSTOMER (<br>
CUSTOMER_ID INT(8) NOT NULL PRIMARY KEY,<br>
CUST_TYPE VARCHAR(30) NOT NULL);<br><br>
CREATE TABLE INDIVIDUAL (<br>
SSN CHAR(9) NOT NULL PRIMARY KEY,<br>
CUSTOMER_ID INT(8) NOT NULL,<br>
NAME VARCHAR(30) NOT NULL,<br>
DOB DATE,<br>
I_PH_NO INT(10),<br>
OID INT(8) NOT NULL,<br>
CONSTRAINT FKEY_CID<br>
FOREIGN KEY (CUSTOMER_ID) REFERENCES CUSTOMER(CUSTOMER_ID),<br>
CONSTRAINT FKEY_IOID<br>
FOREIGN KEY (OID) REFERENCES OWNER(OWNER_ID)<br>
);<br><br>
CREATE TABLE COMPANY (<br>
CNAME VARCHAR(30) NOT NULL PRIMARY KEY,<br>
CUSTOMER_ID INT(8) NOT NULL,<br>
C_ADDRESS VARCHAR(300),<br>
CONSTRAINT FKEY_CCID<br>
FOREIGN KEY (CUSTOMER_ID) REFERENCES CUSTOMER(CUSTOMER_ID)<br>
);<br><br>
CREATE TABLE CARS (<br>
VEHICLE_ID INT(8) NOT NULL PRIMARY KEY,<br>
COLOR VARCHAR(15),<br>
PURCHASE_DATE DATE NOT NULL,<br>
VOWNER_ID INT(8) NOT NULL,<br>
LEASE_START_DATE DATE,<br>
LEASE_TERM INT(4),<br>
END_DATE DATE,<br>
VMODEL_NAME VARCHAR(30) NOT NULL,<br>
VMODEL_YEAR INT(4) NOT NULL,<br>
VTYPE VARCHAR(30) NOT NULL,<br>
WRATE INT(10) NOT NULL,<br>
DRATE INT(10) NOT NULL,<br>
CONSTRAINT FKEY_COID<br>
FOREIGN KEY (VOWNER_ID) REFERENCES OWNER(OWNER_ID),<br>
CONSTRAINT FKEY_MNAME<br>
FOREIGN KEY (VMODEL_NAME, VMODEL_YEAR) REFERENCES MODEL(MODELNAME,MODELYEAR)<br>
);<br><br>
CREATE TABLE RENTALS (<br>
RENTAL_ID INT(10) NOT NULL PRIMARY KEY,<br>
RSTART_DATE DATE NOT NULL,<br>
RENTAL_TYPE VARCHAR(30) NOT NULL,<br>
REND_DATE DATE NOT NULL,<br>
NO_OF_DAYS INT(2) NOT NULL,<br>
NO_OF_WEEKS INT(2) NOT NULL,<br>
AMT_DUE INT(10));<br><br>
CREATE TABLE RENTEDBY(<br>
CUSTOMER_ID INT(8) NOT NULL,<br>
RENTAL_ID INT(10) NOT NULL,<br>
VEHICLE_ID INT(8) NOT NULL,<br>
PRIMARY KEY(CUSTOMER_ID, RENTAL_ID, VEHICLE_ID),<br>
CONSTRAINT FKEY_C<br>
FOREIGN KEY (CUSTOMER_ID) REFERENCES CUSTOMER(CUSTOMER_ID),<br>
CONSTRAINT FKEY_R<br>
FOREIGN KEY (RENTAL_ID) REFERENCES RENTALS(RENTAL_ID),<br>
CONSTRAINT FKEY_V<br>
FOREIGN KEY (VEHICLE_ID) REFERENCES CARS(VEHICLE_ID)<br>
);<br><br>

## JDBC : Insert into Databases from CSV

In order to insert the values into databases, making single entries into the database is a tedious
and time consuming task considering the amount of data that needed to be inserted. In order to
achieve this, we have created separate CSV files for each table that needs to be created and
have also taken into consideration the foreign key constraints in the CSV files itself.<br>
We have created separate JDBC programs to insert into each table. We created separate
programs so that if we need to insert into 1 single table again, it becomes easier for us to do so.<br>
The java codes for inserting into the databases has been submitted in the project zip along with
the CSV that were needed.<br>
We have completed the project in a local environment as we had our own version of mySQL to
work with. So the project will be running on local host itself with proper credentials.<br>

## JDBC : Transactions and Retrieval Queries

In order to process various transactions as well as in order to view different retrieval queries, we
have made a JDBC program that facilitates us to do the same. We have implementing the entire
functionality via console.<br>

There are 11 major functionalities that we have covered. They are as follows :

1. Add a New Customer

2. Add a New Car

3. Rent a New Car

4. Return a car

5. View Active Rentals

6. View Scheduled Rentals

7. View Available Cars

8. Update Rental Rates

9. View Earnings

10. View All Customers

11. View Our Fleet

We will go into the logic of each functionality in the next section. We have used a switch
statement in order to achieve the functionality. We have made the code such that, the loop will
keep running until the user does not select exit option.

### Add a New Customer

In this section, we will be adding a new customer. In order to achieve this, we have followed a
logic that briefly described as follows :<br>
Step 1 : Take an input for the User type (Individual or Company)<br>
Step 2 : Based on the Customer type, we have taken the input for various fields that the
customer needs to enter in order to make a new customer. In this process, we have also
checked for various validations wherever required. For Eg. SSN should be 9 characters long
etc.<br>
Step 3 : The next step is to insert the value into the database. In order to do that, we have first
fetched the maximum value of the ID column in the customers table so that we can assign a
value that 1 greater than the value fetched.<br>
Step 4 : With the appropriate input values, we first insert the values into the Customers table.<br>
Step 5 : based on the type of Customer, we will then insert the values into the Company table
or the Individual table whichever applicable.<br>

### Add a New Car:

In this section, we will adding new car. In order to achieve this, we have followed a logic that is
briefly described.<br>
Step 1: Here, we first ask the user how many new cars does he want to add .<br>
Step 2: After that, we ask the Vehicle’s type as we will be checking it with model table’s
MODELTYPE.<br>
Step 3: After that we will be asking for the Car’s owner ID and checking if that is there in the
owner table or not. If it’s not present we will be adding it to the owner table.<br>
Step 4: After that we will be asking for basic details about the car, including the lease start date
end date and purchase date.<br>
Step 5: If all the details will be correct, the car will be added to our CARS table.<br>
### Rent a New Car
In this section, we will Doing the renting part. In order to achieve this, we have followed a logic
that is briefly described.<br>
Step 1: First we will be asking if the car is for person use or for company’s use.<br>
Step 2: After that we will be asking if the person or company has a registered customer ID or
not. If the ID is present. We will be going ahead with the details. If the person is not a registered
owner, we will be adding the customer first and then we will be going ahead with the process.<br>
Step 3: After that We will asking for the date for which the customer needs the car and
according to that we will be displaying a table and asking to select one from that.<br>

### Return a car

In this functionality we will be returning a rented car. The logic that is used can be briefly
described as follows :<br>
Step 1 : Ask the user for the Rental ID and check whether the Rental ID exists in the database
or not. In order to check this, we have written a stored procedure that enables us to do the
same. If the rental ID exists, the logic goes ahead otherwise it asks the user to enter the Rental
ID again.<br>
Step 2 : Once a rental ID match is found, we display the details for that rental.<br>
Step 3 : The next step is to ask the user whether the user he wants to return the car today or on
a future date.<br>
Step 4 : Once he has made an appropriate selection, we will be calling the stored procedure
that is responsible for making the appropriate updates in the rental table as well as calculate the
final due amount and insert it into the database. We have also taken care that if the car is
returned once, it will not be updated again. We have used a flag to do the same.<br>
Step 5 : Once the update has been successfully made, the final due amount is displayed on the
console.<br>

### View Active Rentals

In this functionality we will be viewing the active rentals. The logic that is used can be briefly
described as follows :<br>
The basic functionality that is used for this functionality is that a rental is currently active if the
current date is between the start date and the end date of the rental<br>
The code snippet can be seen a follows :<br>
p = myConnection .prepareStatement( "select * from Rentals " +<br>
"where" +<br>
"? BETWEEN RSTART_DATE AND REND_DATE;" );<br>
System. out .println( "======Currently Active Rentals======" );<br>
p .setDate(1, new java.sql.Date(System.currentTimeMillis()));<br>
ResultSet r = p .executeQuery();<br>
DBTablePrinter.printResultSet( r );<br>
p .close();<br>
continue ;<br>

### View Scheduled Rentals
In this functionality we will be viewing the scheduled rentals. The logic that is used can be briefly
described as follows :<br>
The basic functionality that is used for this functionality is that a rental is a scheduled rental if
the current date is smaller than the start date of the rental.<br>
The code snippet can be seen a follows :<br>
z = myConnection .prepareStatement( "select * from Rentals " +<br>
"where" +<br>
"? <= RSTART_DATE;" );<br>
System. out .println( "======Scheduled Rentals======" );<br>
z .setDate(1, new java.sql.Date(System.currentTimeMillis()));<br>
ResultSet y = z .executeQuery();<br>
DBTablePrinter.printResultSet( y );<br>
z .close();<br>
continue ;<br>

### View Available Cars

In this functionality we will be viewing all the available cars based on the date range. The logic
that is used can be briefly described as follows :<br>
The basic functionality that is used for this functionality is that a car will be available only if the
car is rented before the next scheduled rental or after the next scheduled rental. This list will
also contain all those cars which have not been rented till now.<br>

### Update Rental Rates

Step1: Here we will be updating the rates according to the type of the vehicle.<br>

### View Earnings

Step1: Here we will calculating the earnings of the company as well as the earnings of our
customers according to the owner_Id<br>

### View All Customers

In this functionality, we will be displaying the details of all the customers which is a union of all
the entries in Individual as well as the Company table.<br>

### View Our Fleet

In this functionality, we will be displaying all the vehicles that are available in the rental
Company<br>

The code snippet can be seen as follows :<br>

System. out .println( "======Our Fleet======" );<br>
gst = myConnection .prepareStatement( "Select ModelName,ModelYear from Model; " );<br>
rst = gst .executeQuery();<br>
DBTablePrinter.printResultSet( rst );<br>
gst .close();<br>
continue ;<br>

## Conclusion

In Part 1 and Part 2, we created the EER model for the Car Rental Agency and mapped the
them into a relational model.<br>
After creating the relational model, we created the tables in the database.<br>
In the next section of the project, we will be inserting records in the database and also writing a
few update queries on the database.
