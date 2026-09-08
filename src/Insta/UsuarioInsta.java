
package Insta;
import base.Usuario;

/**
 *
 * @author vasqu
 */
public class UsuarioInsta extends Usuario{
    private String nombre;
    private Genero genero;
    private int edad;
    private String archivoFoto;
    

    public UsuarioInsta(String nombre, Genero genero, int edad, String User, String password, String nombreFoto) {
        super(User, password);   
        this.nombre=nombre;
        this.genero=genero;
        this.edad=edad;
        this.archivoFoto=nombreFoto;
    }
    
    
}
