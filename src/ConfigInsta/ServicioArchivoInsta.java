
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
import java.io.File;
import Insta.Publicacion;




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
        crearEstructuraUsuario(username);
        return nuevo;
    }
    public static void crearEstructuraUsuario(String username) throws IOException{
       File carpetaUsuario=new File(Rutas.rutaCarpetaUsuario(username));
       if((!carpetaUsuario.exists()&&!carpetaUsuario.mkdirs())){
           throw new IOException("No se pudo crear la carpeta del usuario "+ username);
       }
       
       File carpetaImagenes=new File(Rutas.rutaImagenes(username));
       if(!carpetaImagenes.exists()&&!carpetaImagenes.mkdirs()){
           throw new IOException("Error al crear la carpeta del usuario: "+username);
       }
       
       File carpetaFolders=new File(Rutas.rutaFolderPersonales(username));
       if(!carpetaFolders.exists()&&!carpetaFolders.mkdirs()){
           throw new IOException("Error al crear la carpeta deel usuario: "+username);
       }
       
       File carpetaStickers=new File(Rutas.rutaStickersPersonales(username));
       if(!carpetaStickers.exists()&&!carpetaStickers.mkdirs()){
           throw new IOException("Error al crear la carpeta del usuario "+username);
       }
       
       
       
           }
    
    //metodos para el timeline
    public static ListaEnlazada<String>obtenerSeguidos(String username)throws ArchivoCorruptoException{
       return ArchivoUtil.leerLista(Rutas.rutaFollowing(username));
    }
    
    
    public static  ListaEnlazada<Publicacion> ordenarPorFechaDesc(ListaEnlazada<Publicacion> combinado) {
    int n = combinado.length();
    Publicacion[] arreglo = new Publicacion[n];

    for (int i = 0; i < n; i++) {
        arreglo[i] = combinado.obtenerEn(i);
    }

    
    for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n - 1 - i; j++) {
            if (arreglo[j].getFecha().compareTo(arreglo[j + 1].getFecha()) < 0) {
                Publicacion temp = arreglo[j];
                arreglo[j] = arreglo[j + 1];
                arreglo[j + 1] = temp;
            }
        }
    }

   
    ListaEnlazada<Publicacion> ordenada = new ListaEnlazada<>();
    for (int i = 0; i < n; i++) {
        ordenada.insertarFinal(arreglo[i]);
    }
    return ordenada;
}
    
    public static ListaEnlazada<Publicacion> obtenerTimeline(String username) throws ArchivoCorruptoException{
        ListaEnlazada<Publicacion> combinado = new ListaEnlazada<>();

    ListaEnlazada<Publicacion> propias = ArchivoUtil.leerLista(Rutas.rutaInsta(username));
    for (int i = 0; i < propias.length(); i++) {
        combinado.insertarFinal(propias.obtenerEn(i));
    }

    ListaEnlazada<String> seguidos = obtenerSeguidos(username);
    for (int i = 0; i < seguidos.length(); i++) {
        String usernameSeguido = seguidos.obtenerEn(i);
        ListaEnlazada<Publicacion> publicacionesDeEse =
                ArchivoUtil.leerLista(Rutas.rutaInsta(usernameSeguido));
        for (int j = 0; j < publicacionesDeEse.length(); j++) {
            combinado.insertarFinal(publicacionesDeEse.obtenerEn(j));
        }
    }

    return ordenarPorFechaDesc(combinado);
    }
    
    //panelPerfil ---metodos 
    
    public static ListaEnlazada<String> obtenerSeguidores(String username) throws ArchivoCorruptoException {
        return ArchivoUtil.leerLista(Rutas.rutaFollowers(username));
    }
    //panelBuscar metodos necesarios:
    
    public static ListaEnlazada<Publicacion> buscarPorHashtag(String hashtag) throws ArchivoCorruptoException {
        ListaEnlazada<Publicacion> resultado = new ListaEnlazada<>();
        ListaEnlazada<UsuarioInsta> usuarios = cargarLista();

        for (int i = 0; i < usuarios.length(); i++) {
            String username = usuarios.obtenerEn(i).getUser();
            ListaEnlazada<Publicacion> publicaciones = ArchivoUtil.leerLista(Rutas.rutaInsta(username));

            for (int j = 0; j < publicaciones.length(); j++) {
                Publicacion p = publicaciones.obtenerEn(j);
                if (p.getHashtags().contiene(hashtag)) {
                    resultado.insertarFinal(p);
                }
            }
        }
        return ordenarPorFechaDesc(resultado);
    }
    
    public static ListaEnlazada<UsuarioInsta> buscarUsuariosParcial(String texto) throws ArchivoCorruptoException {
        ListaEnlazada<UsuarioInsta> resultado = new ListaEnlazada<>();
        ListaEnlazada<UsuarioInsta> todos = cargarLista();

        for (int i = 0; i < todos.length(); i++) {
            UsuarioInsta u = todos.obtenerEn(i);
            if (u.getUser().toLowerCase().contains(texto.toLowerCase())) {
                resultado.insertarFinal(u);
            }
        }
        return resultado;
    }
    
    public static ListaEnlazada<UsuarioInsta> obtenerUsuariosSugeridos(int cantidad) throws ArchivoCorruptoException {
        ListaEnlazada<UsuarioInsta> todos = cargarLista();
        ListaEnlazada<UsuarioInsta> sugeridos = new ListaEnlazada<>();

        for (int i = 0; i < Math.min(cantidad, todos.length()); i++) {
            sugeridos.insertarFinal(todos.obtenerEn(i));
        }
        return sugeridos;
    }
    
    
}
    
