# code_generator

# Introduction
This document provides an overview of the different versions of the Code Generator, detailing the features, enhancements, and improvements made in each version. The Code Generator is a tool designed to automate the generation of boilerplate code for applications, especially in environments such as Spring Boot. It helps developers by dynamically creating classes like services, controllers, repositories, DTOs, etc., based on templates and user inputs.

# Version 1.0 - JAVA POJO CLASSES
# Release Date: 08/29/2024
# Step 1: Establishing a Connection to PostgreSQL
The first step in the code generation process is to establish a connection to the PostgreSQL database. The generator prompts the user to input the necessary connection details, including:

Database URL (e.g., jdbc:postgresql://localhost:5432/mydb)
Username
Password
The tool uses these credentials to connect to the database, utilizing JDBC (Java Database Connectivity) to establish a session with PostgreSQL.

# Step 2: Selecting the Schema and Tables
Once connected, the generator retrieves and lists the available database schemas. The user is then prompted to select the schema they want to work with. The generator uses this schema as the scope for generating code.

After selecting a schema, the generator lists the tables available within that schema. The user can select one or more tables from the console interface. These tables will be used as the basis for generating the Java code.

# Step 3: Mapping Table Fields to Java Types
For each selected table, the tool inspects the table's columns and their data types. It maps the database types (e.g., VARCHAR, INTEGER, TIMESTAMP) to the appropriate Java types (e.g., String, Integer, java.sql.Timestamp). This mapping is essential for generating valid Java classes that correctly represent the database schema.

# Step 4: Code Generation
Once the user selects the desired option (App or ORM-only), the code generator uses predefined templates to generate the appropriate code:

App Option: Generates both server and client code along with the mini ORM classes, compiling the application into a working executable.
ORM-Only Option: Generates only the Java classes with ORM annotations, which the user can manually integrate into their project.

# Step 5: Review and Adjust
As in Version 1, the generated files are saved to a specified directory, and the user can review the generated code. For the app option, the compiled code is immediately runnable, allowing the user to start the server and interact with the generated application.


# Summary of the Code Generation Process - Version 2
PostgreSQL Connection: The user connects to a PostgreSQL database to retrieve the schema and tables.
Schema and Table Selection: The user selects which tables to use for code generation.
User Choice - App or ORM Only:
App Option: Generates a complete server-client application that communicates over sockets and uses a mini ORM for database operations.
ORM-Only Option: Generates only the ORM model classes annotated for use with the mini ORM.
Code Generation: The tool applies the appropriate template and generates the necessary code, either compiling it into an executable (App option) or providing ORM classes (ORM-only option).
Review and Adjust: The user can review the generated code and make any necessary adjustments.

# Advantages of Version 2
Enhanced Functionality: This version goes beyond basic code generation and provides a fully functioning application, which can be a significant productivity boost for developers working with networking and databases.
Mini ORM: The lightweight ORM simplifies database interactions without requiring the overhead of more complex ORM frameworks.
Flexibility: The ability to choose between generating a full application or just ORM classes gives developers flexibility depending on their project needs.

<br>

# Version 2.0 - JAVA POJO CLASSES / JAVA BACKEND APP WITH MINI ORM
# Release Date: 08/30/2024
In Version 2 of the Code Generator, the process is enhanced with two primary options for users: generating a complete application with networking and ORM capabilities, or generating only the ORM (Object-Relational Mapping) model classes. This version builds upon the foundation laid in Version 1, adding new functionalities such as communication over sockets and a lightweight mini ORM for database interaction.

Here’s an overview of the steps involved in the code generation process in Version 2:

# Step 1: Establishing a Connection to PostgreSQL
The process begins the same as in Version 1. The user is prompted to input the necessary PostgreSQL connection details, including:

Database URL
Username
Password
Once the connection is established, the generator retrieves the available schemas and tables from the database.

# Step 2: Schema and Table Selection
After the database connection is made, the user is presented with a list of available schemas. They select the schema they want to use, and then choose one or more tables from that schema to generate code for.

# Step 3: User Choice - App or ORM Only
After selecting the schema and tables, the user is given a choice between two options:

# Option 1: Generate Full Application (App Option)
In this option, the tool generates a fully functional application that:

Communicates using Java objects over sockets: The generated application includes a server that listens on a socket and can send/receive serialized Java objects as messages.
Contains a mini ORM: The mini ORM is designed to interact with the PostgreSQL database. It automatically maps database tables to Java objects, providing basic CRUD (Create, Read, Update, Delete) operations.
The generated application includes:

# Server-Side Code:

The server listens on a predefined port and handles client connections using sockets.
Java objects (generated from the database schema) are serialized and sent over the network. These objects represent the data that the client can manipulate.
The server uses the mini ORM to persist and retrieve data from the PostgreSQL database.
Client-Side Code:

The client application is able to connect to the server via sockets.
The client can send requests to the server for data, as well as submit data for persistence.
Mini ORM:

A lightweight ORM is generated alongside the application code. This mini ORM maps database tables to Java objects using annotations.
The ORM handles basic SQL operations behind the scenes, allowing the developer to interact with the database using standard Java objects.
Code Compilation:

After generating the Java source code, the tool automatically compiles the code into .class files.
The compiled code is ready to be executed immediately after generation.

# Option 2: Generate ORM-Only Model Classes
In this option, the tool generates only the ORM model classes without creating a complete application. This is useful if the user wants to integrate these ORM classes into an existing project or if they only need the data models.

The generated ORM classes are:

Annotated for Mini ORM: Each class is annotated to map it to the corresponding database table, similar to popular frameworks like JPA but in a simpler, custom format for the mini ORM.
Fields and Methods: The generated classes include fields for each table column, getters and setters for each field, and basic constructor methods. Main class with ORM code is #GenericObject class#
Annotations: The tool applies annotations that describe how the fields map to database columns, such as @PrimaryKeyAnnotation, @ConstructorAnnotation, and others specific to the mini ORM.

<br>

<br>

# Version 3.0 - TEMPLATE CODE GENERATOR
# Release Date: 09/29/2024

