/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemOperation;

import model.GenericObject;

import java.util.List;

/**
 *
 * @author Aca
 */
public final class SystemOperation_Update extends SystemOperation {

    @Override
    public boolean perform(List<GenericObject> out_result) {
        GenericObject input_object = transferObject.getInputObject();
        
        boolean signal = dbBroker.update(input_object, out_result);
        if(!out_result.isEmpty())
            transferObject.setMessage("Sistem je sačuvao izmene");
        else
            transferObject.setMessage("Sistem nije sačuvao izmene");
        transferObject.setResult(out_result);
        transferObject.setSignal(signal);
        System.out.println("signal: "+transferObject.getSignal());
        System.out.println("message: "+transferObject.getMessage());
        System.out.println("result");
        System.out.println(out_result);
        return signal;
    }
    
}
