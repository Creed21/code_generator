/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2.systemOperation;

import v2.model.GenericObject;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Aca
 */
public class SystemOperation_DeleteBatch extends SystemOperationBatch {

    @Override
    protected boolean performBatch() throws SQLException {
        List<GenericObject> inputBatch = transferObject.getInputBacth();
        if (inputBatch == null || inputBatch.isEmpty()) {
            transferObject.setMessage("Sistem nije uspeo da izvrsi operaciju brisanja!");
            return false;
        }
        
        BatchRows affectedRows = new BatchRows();
        
        if(dbBroker.deleteBatch(inputBatch, affectedRows))
            transferObject.setMessage("Sistem je obrisao "+affectedRows.getAffecteRows()+" redova");
        else
            transferObject.setMessage("Sistem nije obrisao "+inputBatch.get(0).getClassName());
        
        System.out.println("signal: "+transferObject.getSignal());
        System.out.println("message: "+transferObject.getMessage());
        return transferObject.getSignal();
    }
    
}
