/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2.database;

import v2.model.*;
import v2.systemOperation.BatchRows;

import java.sql.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Aca
 */
public class DbBroker {
    private static DbBroker instance;
    private Connection connection;
    private ResultSet resultSet;
    private Statement statement;
    
    private DbBroker() {}
    public static DbBroker getInstance() {
        if(instance == null)
            instance = new DbBroker();
        return instance;
    }

    public boolean openConnection() {
        try {    
            String url = "jdbc:mysql://localhost:3306/auto_skola_3";
            String user = "root";
            String pass = "";

            connection = DriverManager.getConnection(url, user, pass);
            connection.setAutoCommit(false);
            
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DbBroker.class.getName()).log(Level.SEVERE, "Cannot open the connection", ex);
        }
        return false;
    }
    
    public boolean closeConnection() {
        try {
            if(connection != null) connection.close();
            if(resultSet != null) resultSet.close();
            if(statement != null) statement.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DbBroker.class.getName()).log(Level.SEVERE, "Could not close the transaction", ex);
        }
        return false;
    }

    public synchronized boolean commitTrans() {
        try {
            connection.commit();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DbBroker.class.getName()).log(Level.SEVERE, "Could not commit the transaction", ex);
        }
        return false;
    }

    public synchronized boolean rollbackTrans() {
        try {
            connection.rollback();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DbBroker.class.getName()).log(Level.SEVERE, "Could not rollback the transaction", ex);
        }
        return false;
    }

    public synchronized boolean find(GenericObject input, List<GenericObject> result) {
        boolean signal = false;
        if(input == null)
            return false;
            
        String query = input.makeSelectRefl();
        System.out.println("\tSklopljen query: \n"+query);
        
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                GenericObject dbRecord = input.getRecordRefl(resultSet);
                result.add(dbRecord);
                signal = true;
            }
        } catch (SQLException ex) {Logger.getLogger(DbBroker.class.getName()).log(Level.SEVERE, null, ex);}
        
        return signal;
    }

    public synchronized boolean insert(GenericObject input, List<GenericObject> result) {
        boolean signal = false;
        if(input == null)
            return false;
            
        String query = input.makeInsertRefl();
        System.out.println("\tSklopljen query: \t"+query);
        
        try {
            statement = connection.createStatement();
            int rows_affected = statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            if(rows_affected > 0) {
                signal = true;
                System.out.println("execute insert rows_affected: "+rows_affected);
            }
            else {
                System.out.println("execute insert rows_affected: "+rows_affected);
                return false;
            }

        } catch (SQLException ex) {
            Logger.getLogger(DbBroker.class.getName()).log(Level.SEVERE, null, ex);
            signal = false;
        }
        
        return signal;
    }

    public synchronized boolean update(GenericObject input, List<GenericObject> result) {
        boolean signal = false;
        if(input == null)
            return false;
            
        String query = input.makeUpdateRefl();
        System.out.println("\tSklopljen query: \t"+query);
        
        try {
            statement = connection.createStatement();
            
            if(statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS) > 0) {
                resultSet = statement.getGeneratedKeys();
                signal = true;
            }
            else {
                return false;
            }
            
            while(resultSet.next()) {
                String ret_query = input.makeSelectReflPrimKey();
                System.out.println("\t\tSklopljen query posle inserta: \n"+query);
                try (Statement ret_statement = connection.createStatement();
                    ResultSet ret_resultSet = ret_statement.executeQuery(ret_query)) 
                {
                    result.add(input.getRecordRefl(ret_resultSet));
                } catch(SQLException ex) {}
            }
        } catch (SQLException ex) {Logger.getLogger(DbBroker.class.getName()).log(Level.SEVERE, null, ex);}
        
        return signal;
    }
    
    public synchronized boolean delete(GenericObject input, Integer deleted_rows) {
        boolean signal = false;
        if(input == null)
            return false;
            
        String query = input.makeDeleteRefl();
        System.out.println("\tSklopljen query: \t"+query);
        
        try {
            statement = connection.createStatement();
            deleted_rows = statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            return deleted_rows > 0;
        } catch (SQLException ex) {Logger.getLogger(DbBroker.class.getName()).log(Level.SEVERE, null, ex);}
        
        return signal;
    }
    
    public synchronized boolean deleteBatch(List<GenericObject> inputBatch, BatchRows affectedRows) throws SQLException {
        if(inputBatch == null || inputBatch.isEmpty())
            return false;

        statement = connection.createStatement();
        for(GenericObject input : inputBatch) {
            String query = input.makeDeleteRefl();
            System.out.println("\tSklopljen query: \n"+query);
            statement.addBatch(query);
        }
        int[] deleted_rows = statement.executeBatch();
        affectedRows.setAffecteRows(deleted_rows.length);
        for(int i : deleted_rows)
            System.out.println("deleted: "+i);
        
        return deleted_rows.length > 0;
    }

    public synchronized boolean insertBatch(List<GenericObject> inputBatch, BatchRows affectedRows) throws SQLException {
        if(inputBatch == null || inputBatch.isEmpty())
            return false;

        statement = connection.createStatement();
        for(GenericObject input : inputBatch) {
            String query = input.makeInsertRefl();
            System.out.println("\tSklopljen query: \n"+query);
            statement.addBatch(query);
        }
        int inserted_rows[] = statement.executeBatch();
        affectedRows.setAffecteRows(inserted_rows.length);
        
        for(int i : inserted_rows)
            System.out.println("inserted: "+i);
        return inserted_rows.length > 0;
    }
    
    public synchronized boolean updateBatch(List<GenericObject> inputBatch, BatchRows affectedRows) throws SQLException {
        if(inputBatch == null || inputBatch.isEmpty())
            return false;
        System.out.println("DB.updateBatch");
        statement = connection.createStatement();
        for(GenericObject input : inputBatch) {
            String query = input.makeUpdateRefl();
            System.out.println("\tSklopljen query: \n"+query);
            statement.addBatch(query);
        }
        int[] updated_rows = statement.executeBatch();
        affectedRows.setAffecteRows(updated_rows.length);
        
        for(int i : updated_rows)
            System.out.println("updated: "+i);
        
        return updated_rows.length > 0;
    }

}
