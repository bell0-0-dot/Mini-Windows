
package ConfigInsta;
import ConfigInsta.Rutas;
import Excepciones.ArchivoCorruptoException;
import Excepciones.CuentaDesactivadaException;
import Excepciones.PasswordIncorrectoException;
import Excepciones.UsernameDuplicadoException;
import Excepciones.UsuarioInexistenteException;
import Insta.Comentario;
import base.ListaEnlazada;
import base.ArchivoUtil;
import Insta.UsuarioInsta;
import base.Nodo;
import java.io.IOException;
import Insta.Genero;
import Insta.Mensaje;
import Insta.Notificacion;
import java.io.File;
import Insta.Publicacion;
import Insta.Reaccion;




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
       
       File archivoInbox = new File(Rutas.rutaInbox(username));
        if (!archivoInbox.exists()) {
      
        try {
            ArchivoUtil.guardarLista(Rutas.rutaInbox(username), new ListaEnlazada<Mensaje>());
        } catch (Exception e) {
            throw new IOException("Error al crear el archivo inbox.ins para el usuario: " + username, e);
        }
        }
        
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
    
    public static void guardarPublicacion(Publicacion pub) throws ArchivoCorruptoException, IOException {
        String ruta = Rutas.rutaInsta(pub.getAutor());
        ListaEnlazada<Publicacion> publicaciones = ArchivoUtil.leerLista(ruta);
        if (publicaciones == null) {
            publicaciones = new ListaEnlazada<>();
        }
        publicaciones.insertarFinal(pub);
        ArchivoUtil.guardarLista(ruta, publicaciones);
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
    
    
      //metodos para el timeline
    public static ListaEnlazada<String>obtenerSeguidos(String username)throws ArchivoCorruptoException{
       return ArchivoUtil.leerLista(Rutas.rutaFollowing(username));
    }
    
    public static void seguirUsuario(String miUsuario, String usuarioASeguir) throws ArchivoCorruptoException, IOException {
        
        String rutaFollowing = Rutas.rutaFollowing(miUsuario);
        ListaEnlazada<String> misSeguidos = ArchivoUtil.leerLista(rutaFollowing);
        if (misSeguidos == null) misSeguidos = new ListaEnlazada<>();

        if (!misSeguidos.contiene(usuarioASeguir)) {
            misSeguidos.insertarFinal(usuarioASeguir);
            ArchivoUtil.guardarLista(rutaFollowing, misSeguidos);
        }

       
        String rutaFollowers = Rutas.rutaFollowers(usuarioASeguir);
        ListaEnlazada<String> susSeguidores = ArchivoUtil.leerLista(rutaFollowers);
        if (susSeguidores == null) susSeguidores = new ListaEnlazada<>();

        if (!susSeguidores.contiene(miUsuario)) {
            susSeguidores.insertarFinal(miUsuario);
            ArchivoUtil.guardarLista(rutaFollowers, susSeguidores);
        }
    }
    public static void dejarDeSeguirUsuario(String miUsuario, String usuarioADejar) throws ArchivoCorruptoException, IOException {
        
        String rutaFollowing = Rutas.rutaFollowing(miUsuario);
        ListaEnlazada<String> misSeguidos = ArchivoUtil.leerLista(rutaFollowing);
        if (misSeguidos != null && misSeguidos.contiene(usuarioADejar)) {
            misSeguidos.eliminar(usuarioADejar);
            ArchivoUtil.guardarLista(rutaFollowing, misSeguidos);
        }

        
        String rutaFollowers = Rutas.rutaFollowers(usuarioADejar);
        ListaEnlazada<String> susSeguidores = ArchivoUtil.leerLista(rutaFollowers);
        if (susSeguidores != null && susSeguidores.contiene(miUsuario)) {
            susSeguidores.eliminar(miUsuario);
            ArchivoUtil.guardarLista(rutaFollowers, susSeguidores);
        }
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
            UsuarioInsta u = usuarios.obtenerEn(i);
            String rutaInsta = Rutas.rutaInsta(u.getUser());

        
            ListaEnlazada<Publicacion> publicacionesUsuario = ArchivoUtil.leerLista(rutaInsta);

            if (publicacionesUsuario != null) {
            for (int j = 0; j < publicacionesUsuario.length(); j++) {
                Publicacion p = publicacionesUsuario.obtenerEn(j);

               
                if (p.getHashtags() != null) {
                    for (int k = 0; k < p.getHashtags().length(); k++) {
                        String h = p.getHashtags().obtenerEn(k).toLowerCase().replace("#", "");
                        if (h.contains(hashtag)) {
                            resultado.insertarFinal(p);
                            break; 
                        }
                    }
                }
            }
        }
    }

    return resultado;
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
    //comentarios// metodos
    //falta server para guardar comentarios en publicaciones
    //ajenas
    public static void agregarComentario(String autorPublicacion, Publicacion publicacionObjetivo, Comentario nuevoComentario) throws ArchivoCorruptoException, IOException {
        String ruta = Rutas.rutaInsta(autorPublicacion);
        ListaEnlazada<Publicacion> publicaciones = ArchivoUtil.leerLista(ruta);

        if (publicaciones != null) {
            for (int i = 0; i < publicaciones.length(); i++) {
                Publicacion p = publicaciones.obtenerEn(i);

                
                if (p.getAutor().equalsIgnoreCase(publicacionObjetivo.getAutor()) &&
                    p.getFecha().equals(publicacionObjetivo.getFecha())) {

                    p.agregarComentario(nuevoComentario);
                    break;
                }
            }
            ArchivoUtil.guardarLista(ruta, publicaciones);
        }
}
    public static void toggleLike(String autorPublicacion, Publicacion publicacionObjetivo, Reaccion reaccion) 
            throws ArchivoCorruptoException, IOException {
        String ruta = Rutas.rutaInsta(autorPublicacion);
        ListaEnlazada<Publicacion> publicaciones = ArchivoUtil.leerLista(ruta);

        if (publicaciones != null) {
            for (int i = 0; i < publicaciones.length(); i++) {
                Publicacion p = publicaciones.obtenerEn(i);

                // Comparación segura por autor y fecha exacta de creación
                if (p.getAutor().equalsIgnoreCase(publicacionObjetivo.getAutor()) &&
                    p.getFecha().equals(publicacionObjetivo.getFecha())) {

                    if (p.getReacciones().contiene(reaccion)) {
                        p.getReacciones().eliminar(reaccion);
                    } else {
                        p.agregarReaccion(reaccion);
                    }
                    break;
                }
            }
            ArchivoUtil.guardarLista(ruta, publicaciones);
        }
    }
    
    
    
    //metodo para guardar cambios de usuarios:
    public static void actualizarUsuario(UsuarioInsta usuarioModificado) throws ArchivoCorruptoException, IOException {
        ListaEnlazada<UsuarioInsta> lista = cargarLista();
        Nodo<UsuarioInsta> actual = lista.getCabeza();

        while (actual != null) {
            if (actual.getDato().getUser().equals(usuarioModificado.getUser())) {
                actual.setDato(usuarioModificado);
                break;
            }
            actual = actual.getSiguiente();
        }

        guardarLista(lista);
}
    
    //servidor mensajes
    
    public static void guardarMensaje(Mensaje mensaje) {
    try {
       
        guardarMensajeEnInbox(mensaje.getAutor(), mensaje);
        
        
        guardarMensajeEnInbox(mensaje.getDestinatario(), mensaje);
    } catch (Exception e) {
        System.err.println("Error al guardar mensaje: " + e.getMessage());
    }
}
    private static void guardarMensajeEnInbox(String username, Mensaje mensaje) throws Exception {
        String ruta = Rutas.rutaInbox(username);
        ListaEnlazada<Mensaje> listaInbox = ArchivoUtil.leerLista(ruta);
        if (listaInbox == null) {
            listaInbox = new ListaEnlazada<>();
        }
        listaInbox.insertarFinal(mensaje);
        ArchivoUtil.guardarLista(ruta, listaInbox);
}

    public static ListaEnlazada<Mensaje> obtenerChatEntre(String user1, String user2) {
        ListaEnlazada<Mensaje> chatFiltrado = new ListaEnlazada<>();
        try {
            String rutaInbox = Rutas.rutaInbox(user1);
            ListaEnlazada<Mensaje> historial = ArchivoUtil.leerLista(rutaInbox);

            if (historial != null) {
                for (int i = 0; i < historial.length(); i++) {
                    Mensaje m = historial.obtenerEn(i);
                    boolean de1a2 = m.getAutor().equalsIgnoreCase(user1) && m.getDestinatario().equalsIgnoreCase(user2);
                    boolean de2a1 = m.getAutor().equalsIgnoreCase(user2) && m.getDestinatario().equalsIgnoreCase(user1);

                    if (de1a2 || de2a1) {
                        chatFiltrado.insertarFinal(m);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al leer inbox.ins de " + user1 + ": " + e.getMessage());
        }
        return chatFiltrado;
    }
    
    public static void guardarNotificacion(Notificacion notif) throws ArchivoCorruptoException, IOException {
        String ruta = Rutas.rutaNotificaciones(notif.getReceptor());
        ListaEnlazada<Notificacion> lista = ArchivoUtil.leerLista(ruta);
        if (lista == null) {
            lista = new ListaEnlazada<>();
        }
        lista.insertarFinal(notif);
        ArchivoUtil.guardarLista(ruta, lista);
    }

    public static ListaEnlazada<Notificacion> obtenerNotificaciones(String username) throws ArchivoCorruptoException {
        ListaEnlazada<Notificacion> lista = ArchivoUtil.leerLista(Rutas.rutaNotificaciones(username));
        return lista != null ? lista : new ListaEnlazada<>();
    }
    //contenido pre-cargado
    
    public static ListaEnlazada<Publicacion> obtenerPublicacionesDeCuentas(String[] usernames) throws ArchivoCorruptoException {
        ListaEnlazada<Publicacion> resultado = new ListaEnlazada<>();

        for (String username : usernames) {
            if (buscarUsuario(username) == null){
                continue;
            }  

            ListaEnlazada<Publicacion> publicaciones = ArchivoUtil.leerLista(Rutas.rutaInsta(username));
            for (int i = 0; i < publicaciones.length(); i++) {
                resultado.insertarFinal(publicaciones.obtenerEn(i));
            }
        }

    return ordenarPorFechaDesc(resultado);
}
    
    
    
}
    

    
