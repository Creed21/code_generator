/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemOperation;

import model.GenericObject;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Aca
 */
public final class SystemOperation_Delete extends SystemOperation {
    
    @Override
    public boolean perform(List<GenericObject> out_result) {
        GenericObject input_object = transferObject.getInputObject();
        Integer deleted_rows = null;
        boolean signal = dbBroker.delete(input_object, deleted_rows);
        if(!out_result.isEmpty())
            transferObject.setMessage("Sistem je obrisao "+deleted_rows+" redova");
        else
            transferObject.setMessage("Sistem nije uspeo da obrise "+input_object.getClassName());
        transferObject.setResult(new LinkedList<GenericObject>()); // posto se brise nema povratne liste rezultata, vec samo broj obrisanih redova
        transferObject.setSignal(signal);
        System.out.println("signal: "+transferObject.getSignal());
        System.out.println("message: "+transferObject.getMessage());
        System.out.println("result");
        System.out.println(out_result);
        return signal;
    }
}
