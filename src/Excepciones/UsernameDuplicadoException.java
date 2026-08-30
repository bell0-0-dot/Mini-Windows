
package Excepciones;

/**
 *
 * @author vasqu
 */
public class UsernameDuplicadoException extends Exception {

    public UsernameDuplicadoException(String Username) {
        super("El username: "+Username+" ya se encuentra en uso");
    }
    
    
}
