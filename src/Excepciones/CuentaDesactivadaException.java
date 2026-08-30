
package Excepciones;

/**
 *
 * @author vasqu
 */
public class CuentaDesactivadaException extends Exception{

    public CuentaDesactivadaException(String Username) {
        super("La cuenta: "+Username+ " se encuentra desactivada");
        
    }
    
}
