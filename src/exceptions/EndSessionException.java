/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

import java.io.IOException;

/**
 *
 * @author Aca
 */
public class EndSessionException extends IOException {

    public EndSessionException() {
    }

    public EndSessionException(String message) {
        super(message);
    }

    public EndSessionException(String message, Throwable cause) {
        super(message, cause);
    }

    public EndSessionException(Throwable cause) {
        super(cause);
    }
    
}
