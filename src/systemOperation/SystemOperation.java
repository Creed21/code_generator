/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemOperation;

import database.DbBroker;
import model.GenericObject;
import transferObject.TransferObject;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Aca
 */
public abstract class SystemOperation {
    protected DbBroker dbBroker;
    protected TransferObject transferObject;
    
    public TransferObject getTransferObject() { return transferObject; }
    public void setTransferObject(TransferObject transferObject) { this.transferObject = transferObject; }
    public SystemOperation() { 
        this.dbBroker = DbBroker.getInstance();
        transferObject = new TransferObject();
    }
    public SystemOperation(TransferObject transferObject) {
        this.dbBroker = DbBroker.getInstance();
        this.transferObject = transferObject;
    }
    
    protected abstract boolean perform(List<GenericObject> out_result);
    
    public boolean performGenericOperation() {
        dbBroker.openConnection();
        List<GenericObject> out_result = new ArrayList<>();
        boolean signal = perform(out_result);
//        transferObject.setResult(out_result);
        if(signal) 
            dbBroker.commitTrans();
        else 
            dbBroker.rollbackTrans();
        
        dbBroker.closeConnection();
        return signal;
    }
    
}
