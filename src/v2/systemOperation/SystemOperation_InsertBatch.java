/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2.systemOperation;

import v2.model.GenericObject;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aca
 */
public class SystemOperation_InsertBatch extends SystemOperationBatch {

    @Override
    protected boolean performBatch() {
        List<GenericObject> inputBatch = transferObject.getInputBacth();
        if (inputBatch == null || inputBatch.isEmpty()) {
            transferObject.setMessage("Sistem nije uspeo da izvrsi operaciju dodavanja!");
            return false;
        }
        BatchRows affectedRows = new BatchRows();
        boolean signal = false;
        try {
            signal = dbBroker.insertBatch(inputBatch, affectedRows);
            if(signal)
                transferObject.setMessage("Sistem je dodao "+affectedRows.getAffecteRows()+" redova");
            else
                transferObject.setMessage("Sistem nije uspeo da doda "+inputBatch.get(0).getClassName());
        } catch (SQLException ex) {
            transferObject.setMessage("Sistem nije uspeo da doda "+inputBatch.get(0).getClassName()+"\n"+ex.getMessage());
            Logger.getLogger(SystemOperation_InsertBatch.class.getName()).log(Level.SEVERE, null, ex);
        }
        transferObject.setSignal(signal);
        System.out.println("signal: "+signal);
        System.out.println("message: "+transferObject.getMessage());
        return transferObject.getSignal();
    }
    
}
