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
Version 3 of the Code Generator introduces a powerful template system that provides developers with more control over how code is generated from database schemas. This version supports the use of templates and configuration files to customize the code generation process, allowing for the creation of classes in various programming languages based on user input. The entire process is driven by the schema of a PostgreSQL database and user-selected tables.

# Step 1: PostgreSQL Connection and Table Selection
As in previous versions, the first step involves connecting to a PostgreSQL database. The user is prompted for the database URL, username, and password, and once connected, the generator retrieves the available schemas and tables. The user then selects the tables they want to generate code for.

## Database: PostgreSQL only.
Schema and Table Selection: Users can select one or more tables for code generation.
Step 2: Template and Configuration Selection
After selecting the tables, the user specifies the programming language they wish to generate classes for. The tool then loads the corresponding template and template configuration files, which dictate how the code will be generated. These templates can be customized and stored in a specific directory.

## Template Class:
The template class defines the structure and format of the generated files. Each language (e.g., Java, Python, TypeScript) has its own template that uses placeholders and logic to dynamically generate code based on the database schema.
Template Path: Based on the user's selected language, the appropriate template file is chosen. For example, if the user selects Java, the corresponding Java template is loaded.

# java_model_config.json
```
{
    "placeholders": {
    	"folderName": "model",
    	"fileName": "${tableNameCapitalized}",
    	"extension": "java",
    	"newLine": "\n",
    	"indent": "\t"
	},
	"templateFilePath": "C:\\Users\\Korisnik\\Desktop\\test_generator_v3\\java_model_template.txt"
}

Template Language Choice: The user can choose between different templates such as Java, Python, TypeScript, etc. The generator will load the correct template based on the selected language.


## Template Config Class:

The template configuration class defines how the template is applied to the selected tables and fields. It contains metadata such as the package name, file paths, class names, indentation styles, and other configurations required for generating the final files.
File Path Configuration: The template configuration contains settings for where the generated files should be saved. These paths can be customized, including the root directory, subdirectories, and file extensions, based on the user's input or predefined configurations.
Example template config (Java config):

```
# java_model_template.txt
```
package ${fileName};

public class ${tableNameCapitalized} {
forEachField('${indent}private ${fieldType} ${fieldName};${newLine}')

${indent}public ${tableNameCapitalized}() {}

${indent}public ${tableNameCapitalized}(forEachFieldConcat('${fieldType} ${fieldName}') ) {
forEachField('${indent}${indent}this.${fieldName} = ${fieldName};${newLine}')
${indent}}

forEachField('${indent}public void set${fieldNameCapitalized}(${fieldType} ${fieldName}) {${newLine} ${indent}${indent}this.${fieldName} = ${fieldName};${newLine}${indent}}${newLine} ')

forEachField('${indent}public ${fieldType} get${fieldNameCapitalized}() {${newLine}${indent}${indent}return ${fieldName};${indent}${newLine}${indent}}${newLine}')
}
```

## Dynamic Path Resolution: The template configuration uses placeholders like ${packageName} and ${tableNameCapitalized} to dynamically generate the correct file paths for the output files.
File Extensions: The template config also includes details such as file extensions, which may vary depending on the language (e.g., .java for Java, .py for Python).
Step 3: Mapping Database Schema to Templates
Once the templates and configuration files are loaded, the generator maps the selected tables and their corresponding fields from the database schema to the placeholders in the templates.

## Table Mapping: Each selected table is mapped to a class based on the template configuration. For instance, a table named users might map to a Java class UserEntity.
Field Mapping: The generator maps the columns in the database to the fields in the generated classes. The data types from PostgreSQL (e.g., VARCHAR, INTEGER) are mapped to the appropriate language-specific types (e.g., String in Java, str in Python).

# Step 4: Code Generation
Once the table and field mappings are complete, the generator proceeds with code generation:

File Paths: The generator creates the output directories and file paths as specified in the template configuration. The ${packageName} or equivalent placeholder is resolved based on user input, and files are generated in the appropriate directories.

Class and File Creation: For each selected table, a corresponding class file is created. The generator populates the templates with the actual table and field data, replacing placeholders like ${className}, ${fieldType}, and ${fieldName} with the real values extracted from the database.

## Language-Specific Adjustments: Based on the template, the generator makes language-specific adjustments (e.g., formatting, imports, annotations). This ensures that the generated code adheres to the conventions of the chosen programming language.

ORM Annotations: If the language and template support it, ORM annotations are applied to the generated classes. For example, in Java, the template may include annotations such as @Entity, @Id, and @Column to enable ORM functionality.

# Step 5: Output and Review
Once the generation process is complete, the generated files are saved in the specified directory structure. The user can then review the generated code, integrate it into their project, or make further adjustments if necessary.

## Generated Files: The generated classes are placed in the configured outputDirectory. The directory structure reflects the package name or equivalent organization for other languages.
## Compilation: If the user has opted to generate code for a compiled language like Java, they can proceed to compile the code and integrate it into their project.

# Step 6: Manual Adjustments (Optional)
The generated code is designed to be functional and ready for integration, but users are free to manually adjust the generated classes, add custom logic, or modify the structure as needed.

# Summary of the Code Generation Process - Version 3
  - PostgreSQL Connection: Users connect to the database, select the schema and tables, and proceed with code generation.
  - Template and Config Selection: The user chooses the target language for code generation, and the appropriate template and configuration files are loaded.
  - Schema Mapping: The database schema is mapped to placeholders in the templates, with table names, fields, and data types being resolved dynamically.
  - Code Generation: Classes are generated based on the templates and configurations, with file paths, class names, and field types being dynamically populated from the database schema.
  - Output and Review: The generated files are saved to disk according to the configured file paths and are ready for integration or further adjustment.

# Advantages of Version 3
  - Template Flexibility: Users can customize templates to support various programming languages and adjust code generation based on their specific needs.
  - Dynamic Path and Configuration Handling: The template config allows dynamic control over file paths, class naming conventions, and other settings.
  - Multiple Languages: The ability to generate classes in different languages (e.g., Java, Python, TypeScript) makes this version more flexible and suitable for various environments.
  - Version 3 provides a highly customizable and flexible code generation process, allowing developers to dynamically generate code in different languages based on their database schema and chosen configuration.

# this version 3.0 is upgraded to be maven project
