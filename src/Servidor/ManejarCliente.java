/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

import ConfigInsta.ServicioArchivoInsta;
import Insta.Comentario;
import Insta.Genero;
import Insta.Mensaje;
import Insta.Notificacion;
import Insta.Publicacion;
import Insta.Reaccion;
import Insta.UsuarioInsta;
import base.ListaEnlazada;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author vasqu
 */
public class ManejarCliente implements Runnable{
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private String usuarioConectado = null;

    public ManejarCliente(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {

            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream() );

            System.out.println("ManejarCliente iniciado." );

            while (!socket.isClosed()) {

                System.out.println( "Esperando petición...");
                PeticionRed peticion =(PeticionRed) in.readObject();
                System.out.println( "Petición recibida: " + peticion.getComando()
                );
                RespuestaRed respuesta = procesarPeticion( peticion);
                System.out.println("Intentando enviar respuesta...");
                
                if (respuesta != null) {
                    out.writeObject(respuesta);
                    out.flush();
                }
                System.out.println( "Respuesta enviada.");
            }

        } catch (java.net.SocketException | java.io.EOFException e) {
        
        System.out.println("Cliente desconectado de forma habitual (" + (usuarioConectado != null ? usuarioConectado : "Anónimo") + ").");
        } catch (Exception e) {
           
            System.err.println("Error no esperado en ManejarCliente:");
            e.printStackTrace();
        } finally {
            if (usuarioConectado != null) {
                ServidorInsta.removerCliente(usuarioConectado);
            }
            try { 
                socket.close(); 
            } catch (Exception e) {}
            System.out.println("Conexión finalizada.");
        }
    }

    private RespuestaRed procesarPeticion(
            PeticionRed peticion) {

        String comando =
                peticion.getComando();

        try {

            switch (comando) {

                case "LOGIN": {

                    String username =(String) peticion.getParametros()[0];
                    String password =(String) peticion.getParametros()[1];
                    synchronized (ServicioArchivoInsta.class) {UsuarioInsta usuario =ServicioArchivoInsta .validarLogin(username,password);

                        usuarioConectado =usuario.getUser();

                        ServidorInsta.agregarCliente(usuarioConectado,out);

                        return new RespuestaRed(
                            true,
                            usuario,
                            null
                        );
                    }
                }

                case "OBTENER_TIMELINE": {

                    String username =(String) peticion.getParametros()[0];

                    synchronized (
                            ServicioArchivoInsta.class) {

                        ListaEnlazada timeline = ServicioArchivoInsta  .obtenerTimeline( username);

                        return new RespuestaRed(true,timeline,  null);
                    }
                }

                case "REGISTRAR_CONEXION": {

                    String username = (String) peticion.getParametros()[0];
                    usuarioConectado = username;
                    ServidorInsta.agregarCliente(username, out);
                    return new RespuestaRed(true, null, "Conectado");
                }
                
                case "NUEVA_PUBLICACION": {
                    Publicacion pub = (Publicacion) peticion.getParametros()[0];
                    synchronized (ServicioArchivoInsta.class) {
                        ServicioArchivoInsta.guardarPublicacion(pub);
                    }
                   
                    procesarMenciones(pub.getContenido(), pub.getAutor());
                    return new RespuestaRed(true, pub, null);
                }
                case "TOGGLE_LIKE": {
                    String autorPub = (String) peticion.getParametros()[0];
                    Publicacion publicacionObj = (Publicacion) peticion.getParametros()[1];
                    Reaccion reaccion = (Reaccion) peticion.getParametros()[2];

                    synchronized (ServicioArchivoInsta.class) {
                        ServicioArchivoInsta.toggleLike(autorPub, publicacionObj, reaccion);
                    }

                   
                    if (!autorPub.equalsIgnoreCase(reaccion.getAutor())) {
                        Notificacion notif = new Notificacion(
                            reaccion.getAutor(),
                            autorPub,
                            Notificacion.TipoNotificacion.LIKE,
                            reaccion.getAutor() + " le dio me gusta a tu publicación."
                        );
                        synchronized (ServicioArchivoInsta.class) {
                            ServicioArchivoInsta.guardarNotificacion(notif);
                        }
                        ServidorInsta.notificarUsuario(autorPub, notif);
                    }
                    return new RespuestaRed(true, "Like procesado", null);
                }
                
                
                case "AGREGAR_COMENTARIO": {
                    String autorPub = (String) peticion.getParametros()[0];
                    Publicacion publicacionObj = (Publicacion) peticion.getParametros()[1];
                    Comentario comentario = (Comentario) peticion.getParametros()[2];

                    synchronized (ServicioArchivoInsta.class) {
                        ServicioArchivoInsta.agregarComentario(autorPub, publicacionObj, comentario);
                    }

                   
                    if (!autorPub.equalsIgnoreCase(comentario.getAutor())) {
                        Notificacion notif = new Notificacion( comentario.getAutor(),
                            autorPub,
                            Notificacion.TipoNotificacion.COMENTARIO,
                            comentario.getAutor() + " comentó: " + comentario.getTexto()
                        );
                        synchronized (ServicioArchivoInsta.class) {
                            ServicioArchivoInsta.guardarNotificacion(notif);
                        }
                        ServidorInsta.notificarUsuario(autorPub, notif);
                    }
                    procesarMenciones(comentario.getTexto(), comentario.getAutor());
                    return new RespuestaRed(true, comentario, null);
                }
                case "SEGUIR_USUARIO": {
                    String miUsuario = (String) peticion.getParametros()[0];
                    String usuarioASeguir = (String) peticion.getParametros()[1];

                    synchronized (ServicioArchivoInsta.class) {
                        ServicioArchivoInsta.seguirUsuario(miUsuario, usuarioASeguir);

                        Notificacion notif = new Notificacion(
                            miUsuario,
                            usuarioASeguir,
                            Notificacion.TipoNotificacion.SEGUIDOR,
                            miUsuario + " comenzó a seguirte."
                        );
                        ServicioArchivoInsta.guardarNotificacion(notif);
                        ServidorInsta.notificarUsuario(usuarioASeguir, notif);
                    }
                    return new RespuestaRed(true, "Usuario seguido", null);
                }
                
                case "DEJAR_SEGUIR_USUARIO": {
                    String miUsuario = (String) peticion.getParametros()[0];
                    String usuarioADejarDeSeguir = (String) peticion.getParametros()[1];

                    synchronized (ServicioArchivoInsta.class) {
                        ServicioArchivoInsta.dejarDeSeguirUsuario(miUsuario, usuarioADejarDeSeguir);
                    }
                    return new RespuestaRed(true, "Usuario dejado de seguir", null);
                }

                case "ENVIAR_MENSAJE": {

                     Mensaje mensaje =(Mensaje) peticion.getParametros()[0];
                    synchronized (ServicioArchivoInsta.class) {ServicioArchivoInsta.guardarMensaje(mensaje);
                    }

                    ObjectOutputStream outDestinatario =ServidorInsta.obtenerStreamCliente(  mensaje.getDestinatario()
                            );
                    if (outDestinatario != null) {RespuestaRed mensajeEnTiempoReal =
                        new RespuestaRed(true,mensaje,"NUEVO_MENSAJE");

                        synchronized (outDestinatario) {
                            outDestinatario.writeObject(
                                    mensajeEnTiempoReal
                            );
                            outDestinatario.flush();
                        }
                    }

                   
                    return new RespuestaRed(true,mensaje,null);
                }
                case "OBTENER_MENSAJES": {

                    String usuario1 =(String) peticion.getParametros()[0];
                    String usuario2 = (String) peticion.getParametros()[1];
                    synchronized ( ServicioArchivoInsta.class) {
                        ListaEnlazada<Mensaje> chat =ServicioArchivoInsta.obtenerChatEntre( usuario1,usuario2
                                );
                        return new RespuestaRed(true,chat, null);
                    }
                }
                case "ACTUALIZAR_USUARIO": {
                    UsuarioInsta usuarioEditado = (UsuarioInsta) peticion.getParametros()[0];
                    synchronized (ServicioArchivoInsta.class) {
                        ServicioArchivoInsta.actualizarUsuario(usuarioEditado);
                    }
                    
                  
                    if (usuarioConectado != null && !usuarioConectado.equals(usuarioEditado.getUser())) {
                        ServidorInsta.removerCliente(usuarioConectado);
                        usuarioConectado = usuarioEditado.getUser();
                        ServidorInsta.agregarCliente(usuarioConectado, out);
                    }
                    return new RespuestaRed(true, usuarioEditado, "Usuario actualizado correctamente");
                }

                case "DESACTIVAR_CUENTA": {
                    UsuarioInsta usuarioADesactivar = (UsuarioInsta) peticion.getParametros()[0];
                    synchronized (ServicioArchivoInsta.class) {
                        ServicioArchivoInsta.actualizarUsuario(usuarioADesactivar);
                    }
                    if (usuarioConectado != null) {
                        ServidorInsta.removerCliente(usuarioConectado);
                        usuarioConectado = null;
                    }
                    return new RespuestaRed(true, null, "Cuenta desactivada correctamente");
                }
                case "BUSCAR_USUARIOS": {
                    String texto = (String) peticion.getParametros()[0];
                    synchronized (ServicioArchivoInsta.class) {
                        ListaEnlazada<UsuarioInsta> resultado = ServicioArchivoInsta.buscarUsuariosParcial(texto);
                        return new RespuestaRed(true, resultado, null);
                    }
                }
                case "OBTENER_NOTIFICACIONES": {
                    String username = (String) peticion.getParametros()[0];
                    synchronized (ServicioArchivoInsta.class) {
                        ListaEnlazada<Notificacion> notifs = ServicioArchivoInsta.obtenerNotificaciones(username);
                        return new RespuestaRed(true, notifs, null);
                    }
                }
                
                
                default:

                    return new RespuestaRed(false,null,"Comando desconocido" );
                    
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new RespuestaRed(false, e,e.getMessage()
            );
        }
    }
    private void procesarMenciones(String texto, String emisor) {
        if (texto == null || !texto.contains("@")) return;

        Pattern pattern = Pattern.compile("@(\\w+)");
        Matcher matcher = pattern.matcher(texto);

        while (matcher.find()) {
            String usuarioMencionado = matcher.group(1);
            if (!usuarioMencionado.equalsIgnoreCase(emisor)) {
                try {
                    Notificacion notif = new Notificacion( emisor, usuarioMencionado, Notificacion.TipoNotificacion.MENCION, emisor + " te mencionó en una publicación o comentario."
                    );
                    synchronized (ServicioArchivoInsta.class) {
                        ServicioArchivoInsta.guardarNotificacion(notif);
                    }
                    ServidorInsta.notificarUsuario(usuarioMencionado, notif);
                } catch (Exception e) {
                    System.err.println("Error al procesar mención para " + usuarioMencionado);
                }
            }
        }
    }
    
    
    
}        




