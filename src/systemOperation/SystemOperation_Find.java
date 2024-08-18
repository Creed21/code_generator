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
public final class SystemOperation_Find extends SystemOperation {
    
    @Override
    public boolean perform(List<GenericObject> out_result) {
        GenericObject input_object = transferObject.getInputObject();
        
        boolean signal = dbBroker.find(input_object, out_result);
        System.out.println("SO_FIND: result after calling find: " + out_result);
        if(!out_result.isEmpty())
            transferObject.setMessage("Sistem je pronasao "+out_result.size()+" rezultata");
        else
            transferObject.setMessage("Nema rezultata pretrage");
        transferObject.setResult(out_result);
        transferObject.setSignal(signal);
        System.out.println("signal: "+transferObject.getSignal());
        System.out.println("message: "+transferObject.getMessage());
//        System.out.println("result");
//        System.out.println(out_result);
        return signal;
    }
    
}
