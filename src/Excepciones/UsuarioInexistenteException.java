
package Excepciones;

/**
 *
 * @author vasqu
 */
public class UsuarioInexistenteException extends Exception{

    public UsuarioInexistenteException(String User) {
        super("El usuario: "+User+" no existe.");
    }
    
}
