/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemOperation;

import database.DbBroker;
import transferObject.TransferObject;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aca
 */
public abstract class SystemOperationBatch {
    protected DbBroker dbBroker;
    protected TransferObject transferObject;
    
    public TransferObject getTransferObject() { return transferObject; }
    public void setTransferObject(TransferObject transferObject) { this.transferObject = transferObject; }
    public SystemOperationBatch() { 
        this.dbBroker = DbBroker.getInstance();
        transferObject = new TransferObject();
    }
    public SystemOperationBatch(TransferObject transferObject) {
        this.dbBroker = DbBroker.getInstance();
        this.transferObject = transferObject;
    }
    
    protected abstract boolean performBatch() throws SQLException;
    
    public boolean perform() {
        try {
            dbBroker.openConnection();
            performBatch();
            dbBroker.commitTrans();
            transferObject.setSignal(true);
            dbBroker.closeConnection();
            return true;
        } catch (SQLException ex) {
            dbBroker.rollbackTrans();
            dbBroker.closeConnection();
            Logger.getLogger(SystemOperationBatch.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
}
