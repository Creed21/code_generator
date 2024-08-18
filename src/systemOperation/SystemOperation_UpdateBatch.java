/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemOperation;

import model.GenericObject;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aca
 */
public class SystemOperation_UpdateBatch extends SystemOperationBatch {

    @Override
    protected boolean performBatch() throws SQLException {
        List<GenericObject> inputBatch = transferObject.getInputBacth();
        if (inputBatch == null || inputBatch.isEmpty()) {
            transferObject.setMessage("Sistem nije uspeo da izvrsi operaciju izmene!");
            return false;
        }
        
        BatchRows affectedRows = new BatchRows();
        boolean signal = false;
        try {
            signal = dbBroker.updateBatch(inputBatch, affectedRows);
            if(signal)
                transferObject.setMessage("Sistem je izmenio "+affectedRows.getAffecteRows()+" redova");
            else
                transferObject.setMessage("Sistem nije sačuvao izmene");
        } catch (SQLException ex) {
            transferObject.setMessage("Sistem nije sačuvao izmene"+"\n"+ex.getMessage());
            Logger.getLogger(SystemOperation_InsertBatch.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("signal: "+transferObject.getSignal());
        System.out.println("message: "+transferObject.getMessage());
        return transferObject.getSignal();
    }
    
}
