
package ConfigInsta;
import Excepciones.ArchivoCorruptoException;
import base.ListaEnlazada;
import base.ArchivoUtil;
import Insta.UsuarioInsta;
import base.Nodo;


/**
 *
 * @author vasqu
 */
public class ServicioArchivoInsta {
    
    public static ListaEnlazada<UsuarioInsta> cargarLista() throws ArchivoCorruptoException{
        return ArchivoUtil.leerLista(Rutas.ARCHIVO_USERS);
    }
    
    public static UsuarioInsta buscarUsuario(String username) throws ArchivoCorruptoException{
        ListaEnlazada usuarios=cargarLista();
        if(usuarios==null){
            return null;
        }
        Nodo<UsuarioInsta> actual=usuarios.getCabeza();
        boolean encontrado=false;
        while(actual!=null){
            actual=actual.getSiguiente();
            if(actual.getDato().getUser().equals(username)){
                encontrado=true;
                break;
            }
        }
       
        return actual.getDato();
    }
    public static boolean validarLogin(String username, String password) throws ArchivoCorruptoException {
        UsuarioInsta encontrado= buscarUsuario(username);
        if(encontrado!=null){
             if(encontrado.verificarPassword(password)){
                 if(encontrado.isActivo()){
                     return true;
                 }
            }
        }
        return false;
    }
    
    
}
