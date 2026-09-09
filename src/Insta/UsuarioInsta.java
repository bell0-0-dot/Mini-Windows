
package Insta;
import base.Usuario;
import java.time.LocalDate;

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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getArchivoFoto() {
        return archivoFoto;
    }

    public void setArchivoFoto(String archivoFoto) {
        this.archivoFoto = archivoFoto;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
    
}
