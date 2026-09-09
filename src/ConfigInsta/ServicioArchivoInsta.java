
package ConfigInsta;
import Excepciones.ArchivoCorruptoException;
import Excepciones.CuentaDesactivadaException;
import Excepciones.PasswordIncorrectoException;
import Excepciones.UsernameDuplicadoException;
import Excepciones.UsuarioInexistenteException;
import base.ListaEnlazada;
import base.ArchivoUtil;
import Insta.UsuarioInsta;
import base.Nodo;
import java.io.IOException;
import Insta.Genero;



/**
 *
 * @author vasqu
 */
public class ServicioArchivoInsta {
    
    public static ListaEnlazada<UsuarioInsta> cargarLista() throws ArchivoCorruptoException{
        return ArchivoUtil.leerLista(Rutas.ARCHIVO_USERS);
    }
    
    public static UsuarioInsta buscarUsuario(String username) throws ArchivoCorruptoException{
        ListaEnlazada <UsuarioInsta>usuarios=cargarLista();
        
        Nodo<UsuarioInsta> actual=usuarios.getCabeza();
        while(actual!=null){
            
            if(actual.getDato().getUser().equals(username)){
                return actual.getDato();
                
            }
            actual=actual.getSiguiente();
        }
        return null;
    }
    public static UsuarioInsta validarLogin(String username, String password) throws ArchivoCorruptoException,  UsuarioInexistenteException, PasswordIncorrectoException,   CuentaDesactivadaException, IOException {
        UsuarioInsta encontrado=buscarUsuario(username);
        if(encontrado==null){
            throw new UsuarioInexistenteException(username);
        }
        if(!encontrado.verificarPassword(password)){
            throw new PasswordIncorrectoException();
        }
        if(!encontrado.isActivo()){
            throw new CuentaDesactivadaException(username); 
        }
        return encontrado;
}
    public static void guardarLista(ListaEnlazada<UsuarioInsta> lista) throws IOException{
        ArchivoUtil.guardarLista(Rutas.ARCHIVO_USERS, lista);
    }
    
    public static UsuarioInsta registrarUsuario(String nombre,Genero genero, int edad,
            String username, String password, String nombreFoto)throws ArchivoCorruptoException,IOException, UsernameDuplicadoException{
        
        UsuarioInsta nuevo;
        if(buscarUsuario(username)!=null){
            throw new UsernameDuplicadoException(username);
        }
        nuevo=new UsuarioInsta(nombre, genero,edad, username, password, nombreFoto);
        ArchivoUtil.agregarRegistro(Rutas.ARCHIVO_USERS, nuevo);
        return nuevo;
    }
}
    
