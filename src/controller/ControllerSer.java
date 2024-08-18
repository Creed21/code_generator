/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import systemOperation.*;
import transferObject.TransferObject;

/**
 *
 * @author Aca
 */
public class ControllerSer {
    private static ControllerSer instance;
    
    private ControllerSer() {  }
    
    public static ControllerSer getInstance() {
        if(instance == null)
            instance = new ControllerSer();
        return instance;
    }

    public TransferObject find(TransferObject request) {
        SystemOperation so_find = new SystemOperation_Find();
        so_find.setTransferObject(request);
        so_find.performGenericOperation();
        return so_find.getTransferObject();
    }

    public TransferObject insert(TransferObject request) {
        SystemOperation so_insert = new SystemOperation_Insert();
        so_insert.setTransferObject(request);
        so_insert.performGenericOperation();
        return so_insert.getTransferObject();
    }
    
    public TransferObject update(TransferObject request) {
        SystemOperation so_update = new SystemOperation_Update();
        so_update.setTransferObject(request);
        so_update.performGenericOperation();
        return so_update.getTransferObject();
    }
    
    public TransferObject delete(TransferObject request) {
        SystemOperation so_delete = new SystemOperation_Delete();
        so_delete.setTransferObject(request);
        so_delete.performGenericOperation();
        return so_delete.getTransferObject();
    }

    public TransferObject insertBacth(TransferObject request) {
        SystemOperation_InsertBatch so_insertBatch = new SystemOperation_InsertBatch();
        so_insertBatch.setTransferObject(request);
        so_insertBatch.perform();
        return so_insertBatch.getTransferObject();
    }
    
    public TransferObject updateBatch(TransferObject request) {
        SystemOperation_UpdateBatch so_updateBatch = new SystemOperation_UpdateBatch();
        so_updateBatch.setTransferObject(request);
        so_updateBatch.perform();
        return so_updateBatch.getTransferObject();
    }
    
    public TransferObject deleteBatch(TransferObject request) {
        SystemOperationBatch so_deleteBatch = new SystemOperation_DeleteBatch();
        so_deleteBatch.setTransferObject(request);
        so_deleteBatch.perform();
        return so_deleteBatch.getTransferObject();
    }
    
}
