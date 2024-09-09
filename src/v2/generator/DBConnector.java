/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package v2.generator;

import v2.db.DBConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aca
 */
public class DBConnector {

    private static DBConnector instance;

    private DBConfig dbConfig;

    private Connection connection;
    private ResultSet resultSet;
    private Statement statement;

    private DBConnector() throws RuntimeException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Missing postgres driver!");
        }
    }
    
    private DBConnector(DBConfig dBConfig) throws RuntimeException {
        try {
            Class.forName("org.postgresql.Driver");
            this.dbConfig = dBConfig;
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Missing postgres driver!");
        }
        this.dbConfig = new DBConfig(dbConfig.getDbUrl(), dbConfig.getUser(), dbConfig.getPassword());
    }

    private DBConnector(String dbUrl, String user, String password) throws RuntimeException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Missing postgres driver!");
        }
        this.dbConfig = new DBConfig(dbUrl, user, password);
    }

    public static DBConnector getInstance() {
        if (instance == null) {
            try {
                instance = new DBConnector();
            } catch (RuntimeException ex) {
                Logger.getLogger(DBConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return instance;
    }

    public static DBConnector getInstance(String dbUrl, String user, String password) throws RuntimeException {
        if (instance == null) {
            instance = new DBConnector(dbUrl, user, password);
        }
        return instance;
    }
    
    public static DBConnector getInstance(DBConfig dBConfig) throws RuntimeException {
        if (instance == null) {
            instance = new DBConnector(dBConfig);
        }
        return instance;
    }

    public boolean openConnection() {
        try {
            connection = DriverManager.getConnection(dbConfig.getDbUrl(), dbConfig.getUser(), dbConfig.getPassword());
            connection.setAutoCommit(false);

            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBConnector.class.getName()).log(Level.SEVERE, "Cannot open the connection.", ex);
        }
        return false;
    }

    public boolean closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBConnector.class.getName()).log(Level.SEVERE, "Could not close the transaction", ex);
        }
        return false;
    }

    public List<String> readSchemas() throws RuntimeException {
        List<String> schemas = new ArrayList<>();

        if (!openConnection()) {
            return schemas;
        }

        String query = "SELECT schema_name FROM information_schema.schemata where schema_name not in ('pg_toast', 'pg_catalog', 'information_schema')";
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                schemas.add(resultSet.getString("schema_name"));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        } finally {
            closeConnection();
        }

        return schemas;
    }

    public List<String> readTables(String schema) throws RuntimeException {
        if (schema == null || schema.isEmpty()) {
            throw new RuntimeException("Schema cannot be empty!");
        }

        List<String> tables = new ArrayList<>();

        if (!openConnection()) {
            return tables;
        }

        // Fetch the tables from pg_catalog.pg_tables
        String query = "SELECT tablename FROM pg_catalog.pg_tables WHERE schemaname = '" + schema + "'";
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                tables.add(resultSet.getString("tablename"));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        } finally {
            closeConnection();
        }
        return tables;
    }

    public Map<String, String> readFields(String schema, String table) throws RuntimeException {
        Map<String, String> fields = new LinkedHashMap<>();
        String query = "SELECT column_name, data_type FROM information_schema.columns WHERE table_schema = '"
                + schema + "' AND table_name = '" + table + "' ORDER BY ordinal_position";
        try {
            openConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String columnName = resultSet.getString("column_name");
                String dataType = resultSet.getString("data_type");
                String javaType = mapToJavaType(dataType);
                fields.put(columnName, javaType);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        } finally {
            closeConnection();
        }
        return fields;
    }
    
    public List<String> readPrimKeysForTable(String schema, String table) throws RuntimeException {
        List<String> primKeys = new ArrayList<>();
        String constraintQuery = "SELECT constraint_name FROM information_schema.table_constraints\n" +
                            "WHERE\n" +
                            "constraint_schema = '"+schema+"' \n" +
                            "AND table_name = '"+table+"' \n" +
                            "AND constraint_type = 'PRIMARY KEY'";
        String query = "SELECT column_name FROM information_schema.key_column_usage "
                + "WHERE constraint_schema = '"+schema+"'"
                +" AND constraint_name IN ("
                +constraintQuery
                +") ORDER BY ordinal_position";
        
        try {
            openConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            
            while (resultSet.next()) {
                String columnName = resultSet.getString("column_name");
                primKeys.add(columnName);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        } finally {
            closeConnection();
        }
        
        return primKeys;
    }
        
    private String mapToJavaType(String postgresType) throws RuntimeException {
        if(postgresType == null || postgresType.isEmpty())
            throw new RuntimeException("Type cannot be empty.");
        return switch (postgresType) {
            // numbers
            case "integer" -> "Integer";
            case "numeric" -> "Double";
            // text
            case "varchar", "text" -> "String";
            // dates & timestamps
            case "timestamp", "timestamp without time zone", "timestamp with time zone" -> "java.sql.Timestamp";
            case "date" -> "java.sql.Date";
            // boolean
            case "bool" -> "Boolean";
            // else
            default -> "Object";
        };
    }
        
}
