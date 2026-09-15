/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

import ConfigInsta.ServicioArchivoInsta;
import Insta.Genero;
import Insta.Mensaje;
import Insta.UsuarioInsta;
import base.ListaEnlazada;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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

            out = new ObjectOutputStream(
                    socket.getOutputStream()
            );

            out.flush();

            in = new ObjectInputStream(
                    socket.getInputStream()
            );

            System.out.println(
                "ManejarCliente iniciado."
            );

            while (!socket.isClosed()) {

                System.out.println(
                    "Esperando petición..."
                );

                PeticionRed peticion =
                        (PeticionRed) in.readObject();

                System.out.println(
                    "Petición recibida: "
                    + peticion.getComando()
                );

                RespuestaRed respuesta =
                        procesarPeticion(
                            peticion
                        );

                System.out.println(
                    "Intentando enviar respuesta..."
                );

                out.writeObject(respuesta);
                out.flush();

                System.out.println(
                    "Respuesta enviada."
                );
            }

        } catch (Exception e) {

            System.out.println(
                "ERROR EN EL SERVIDOR:"
            );

            e.printStackTrace();

        } finally {

            if (usuarioConectado != null) {

                ServidorInsta.removerCliente(
                    usuarioConectado
                );
            }

            try {
                socket.close();
            } catch (Exception e) {
            }

            System.out.println(
                "Cliente desconectado."
            );
        }
    }

    private RespuestaRed procesarPeticion(
            PeticionRed peticion) {

        String comando =
                peticion.getComando();

        try {

            switch (comando) {

                case "LOGIN": {

                    String username =
                            (String) peticion
                            .getParametros()[0];

                    String password =
                            (String) peticion
                            .getParametros()[1];

                    synchronized (
                            ServicioArchivoInsta.class) {

                        UsuarioInsta usuario =
                                ServicioArchivoInsta
                                .validarLogin(
                                    username,
                                    password
                                );

                        usuarioConectado =
                                usuario.getUser();

                        ServidorInsta.agregarCliente(
                            usuarioConectado,
                            out
                        );

                        return new RespuestaRed(
                            true,
                            usuario,
                            null
                        );
                    }
                }

                case "OBTENER_TIMELINE": {

                    String username =
                            (String) peticion
                            .getParametros()[0];

                    synchronized (
                            ServicioArchivoInsta.class) {

                        ListaEnlazada timeline =
                                ServicioArchivoInsta
                                .obtenerTimeline(
                                    username
                                );

                        return new RespuestaRed(
                            true,
                            timeline,
                            null
                        );
                    }
                }

                case "REGISTRAR_CONEXION": {

                    String username =
                            (String) peticion
                            .getParametros()[0];

                    usuarioConectado = username;

                    ServidorInsta.agregarCliente(
                        username,
                        out
                    );

                    return new RespuestaRed(
                        true,
                        null,
                        "Conectado"
                    );
                }

                case "ENVIAR_MENSAJE": {

                     Mensaje mensaje =
                            (Mensaje) peticion.getParametros()[0];

                    synchronized (ServicioArchivoInsta.class) {

                       
                        ServicioArchivoInsta.guardarMensaje(mensaje);
                    }

                   
                    ObjectOutputStream outDestinatario =
                            ServidorInsta.obtenerStreamCliente(
                                    mensaje.getDestinatario()
                            );

                  
                    if (outDestinatario != null) {

                        RespuestaRed mensajeEnTiempoReal =
                                new RespuestaRed(
                                    true,
                                    mensaje,
                                    "NUEVO_MENSAJE"
                                
                            );

                        synchronized (outDestinatario) {
                            outDestinatario.writeObject(
                                    mensajeEnTiempoReal
                            );
                            outDestinatario.flush();
                        }
                    }

                   
                    return new RespuestaRed(
                            true,
                            mensaje,
                            null
                    );
                }
                case "OBTENER_MENSAJES": {

                    String usuario1 =
                            (String) peticion
                            .getParametros()[0];

                    String usuario2 =
                            (String) peticion
                            .getParametros()[1];

                    synchronized (
                            ServicioArchivoInsta.class) {

                        ListaEnlazada<Mensaje> chat =
                                ServicioArchivoInsta
                                .obtenerChatEntre(
                                    usuario1,
                                    usuario2
                                );

                        return new RespuestaRed(
                            true,
                            chat,
                            null
                        );
                    }
                }

                default:

                    return new RespuestaRed(
                        false,
                        null,
                        "Comando desconocido"
                    );
            }

        } catch (Exception e) {

            e.printStackTrace();

            return new RespuestaRed(
                false,
                e,
                e.getMessage()
            );
        }
    }
}        




