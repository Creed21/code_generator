# code_generator

# Introduction
This document provides an overview of the different versions of the Code Generator, detailing the features, enhancements, and improvements made in each version. The Code Generator is a tool designed to automate the generation of boilerplate code for applications, especially in environments such as Spring Boot. It helps developers by dynamically creating classes like services, controllers, repositories, DTOs, etc., based on templates and user inputs.

# Version 1.0 - Initial Release
# Release Date: MM/DD/YYYY
#Step 1: Establishing a Connection to PostgreSQL
The first step in the code generation process is to establish a connection to the PostgreSQL database. The generator prompts the user to input the necessary connection details, including:

Database URL (e.g., jdbc:postgresql://localhost:5432/mydb)
Username
Password
The tool uses these credentials to connect to the database, utilizing JDBC (Java Database Connectivity) to establish a session with PostgreSQL.

#Step 2: Selecting the Schema and Tables
Once connected, the generator retrieves and lists the available database schemas. The user is then prompted to select the schema they want to work with. The generator uses this schema as the scope for generating code.

After selecting a schema, the generator lists the tables available within that schema. The user can select one or more tables from the console interface. These tables will be used as the basis for generating the Java code.

#Step 3: Mapping Table Fields to Java Types
For each selected table, the tool inspects the table's columns and their data types. It maps the database types (e.g., VARCHAR, INTEGER, TIMESTAMP) to the appropriate Java types (e.g., String, Integer, java.sql.Timestamp). This mapping is essential for generating valid Java classes that correctly represent the database schema.

#Step 4: Create classes
Create classes on file system
