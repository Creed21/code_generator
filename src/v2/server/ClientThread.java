/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2.server;

import v2.controller.ControllerSer;
import v2.exceptions.EndSessionException;
import v2.transferObject.TransferObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aca
 */
public class ClientThread extends Thread {
    private final Socket CLIENT;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private ControllerSer controller;
    private TransferObject request;
    private TransferObject response;
    
    public ClientThread(Socket client) {
        this.CLIENT = client;
        getStreams();
    }

    private void getStreams() {
        try {
            ois = new ObjectInputStream(CLIENT.getInputStream());
            oos = new ObjectOutputStream(CLIENT.getOutputStream());
            controller = ControllerSer.getInstance();
        }catch (IOException ex) {
//            System.out.println("");
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("\tClientThread.run()");
            handleClient();
        } catch (IOException | ClassNotFoundException ex) {
            if(ex.getMessage().contains("reset")) {
                System.out.println("Client disconnetcted");
                return;
            }
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            try {
                CLIENT.close();
            } catch (IOException ex1) {
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    private void handleClient() throws IOException, ClassNotFoundException {
        while(true) {
            request = (TransferObject) ois.readObject();// read request
            System.out.println("request: "+request);
            response = new TransferObject();
            if(request.getAction().contains("Batch"))  {
                System.out.print(request.getAction()+"\t");
                System.out.println(request.getInputBacth());
            } else
                System.out.println(request.getAction()+"\t"+request.getInputObject());
            
            try {
                performAction(request.getAction());       // do business logic
            } catch(Exception ex) {
                response.setMessage(ex.getMessage());
                System.out.println(ex.getMessage());
            }
            sendResponse(response);                     // send response
        }
    }

    private void performAction(String action) throws IOException {
        System.out.println("\tperformOperation.read acction :"+action);
        switch(action) {
            case "find":
                System.out.println("\t\tperformOperation.find()");
                response = controller.find(request);
                break;
            case "insert":
                System.out.println("\t\tperformOperation.insert");
                response = controller.insert(request);
                break;
            case "update":
                System.out.println("\t\tperformOperation.update");
                response = controller.update(request);
                break;
            case "delete":
                System.out.println("\t\tperformOperation.delete");
                response = controller.delete(request);
                break;
            case "insertBatch":
                System.out.println("\t\tperformOperation.insertBatch");
                response = controller.insertBacth(request);
                break;
            case "updateBatch":
                System.out.println("\t\tperformOperation.updateBatch");
                response = controller.updateBatch(request);
                break;
            case "deleteBatch":
                System.out.println("\t\tperformOperation.deleteBatch");
                response = controller.deleteBatch(request);
                break;
            case "end":
                throw new EndSessionException("Client has disconnected: " + CLIENT);
            default:
                response.setSignal(false);
                response.setMessage("Action \"" + action+"\" is not supported.");
                response.setInputObject(request.getInputObject());
                response.setResult(null);
        }
    }

    private void sendResponse(TransferObject response) throws IOException {
        System.out.println("Sending response for client:");
        System.out.println(response.getResult());
        System.out.println("**************************************");
        oos.writeObject(response);
        oos.flush();
        oos.reset();
    }

    
    
}
