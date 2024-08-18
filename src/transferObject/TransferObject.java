/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transferObject;

import model.GenericObject;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Aca
 */
public class TransferObject implements Serializable {
    private static final long serialVersionUID = 123123L;

    private String action;
    private boolean signal;
    private String message;
    private GenericObject inputObject;
    private GenericObject inputObject2;
    private List<GenericObject> inputBatch;
    private List<GenericObject> result;

    public TransferObject() {}
    public TransferObject(String action, boolean signal, GenericObject input_object, List<GenericObject> result, String message) {
        this.action = action;
        this.signal = signal;
        this.inputObject = input_object;
        this.result = result;
        this.message = message;
    }
    public String getAction() {return action;}
    public void setAction(String action) {this.action = action;}
    public boolean getSignal() {return signal;}
    public void setSignal(boolean signal) {this.signal = signal;}
    public GenericObject getInputObject() {return inputObject;}
    public void setInputObject(GenericObject input_object) {this.inputObject = input_object;}
    public GenericObject getInputObject2() {return inputObject2;}
    public void setInputObject2(GenericObject input_object2) {this.inputObject2 = input_object2;}
    public List<GenericObject> getInputBacth() {return inputBatch;}
    public void setInputBatch(List<GenericObject> input_object) {this.inputBatch = input_object;}
    public List<GenericObject> getResult() {return result;}
    public void setResult(List<GenericObject> result) {this.result = result;}
    public String getMessage() {return message;}
    public void setMessage(String message) {this.message = message;}
    @Override
    public String toString() {
        return "TransferObject{" + "action=" + action + ", signal=" + signal + ", input_object=" + inputObject + ", result=" + result + ", message=" + message + '}';
    }
    
}
