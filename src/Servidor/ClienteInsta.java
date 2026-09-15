/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

import Insta.Mensaje;
import Insta.Notificacion;
import Insta.SesionActual;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.Consumer;

/**
 *
 * @author vasqu
 */
public class ClienteInsta {
    private static ClienteInsta instancia;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    
    private Consumer<Mensaje> listenerMensajes;
    private Consumer<Notificacion> listenerNotificaciones;
    
    private Thread hiloEscucha;
    private boolean escuchando;

    public ClienteInsta() throws Exception {

        socket = new Socket("localhost", 12345);

        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();

        in = new ObjectInputStream(socket.getInputStream());

        System.out.println("Cliente conectado al servidor.");
    }

    public static synchronized ClienteInsta getInstancia() throws Exception {
        if (instancia == null || instancia.socket == null || instancia.socket.isClosed()) {
            instancia = new ClienteInsta();
        }
        return instancia;
    }
    
    public synchronized RespuestaRed enviarPeticion(PeticionRed peticion)
            throws Exception {
        System.out.println( "Enviando petición: " + peticion.getComando() );
        out.writeObject(peticion);
        out.flush();

        RespuestaRed respuesta =(RespuestaRed) in.readObject();
        System.out.println("Respuesta recibida.");
        return respuesta;
    }
    
    public void registrarListenerMensajes(Consumer<Mensaje> listener) {
        this.listenerMensajes = listener;
    }
    
    public void registrarListenerNotificaciones(Consumer<Notificacion> listener) {
        this.listenerNotificaciones = listener;
    }
    
    public void iniciarHiloEscucha() {
        Thread hilo = new Thread(() -> {
            try {
                while (socket != null && !socket.isClosed()) {
                    Object obj = in.readObject();
                    if (obj instanceof RespuestaRed) {
                        RespuestaRed resp = (RespuestaRed) obj;
                        
                        if ("NUEVO_MENSAJE".equals(resp.getMensajeError()) && listenerMensajes != null) {
                            Mensaje msg = (Mensaje) resp.getContenido();
                            javax.swing.SwingUtilities.invokeLater(() -> listenerMensajes.accept(msg));
                        } 
                        else if ("NUEVA_NOTIFICACION".equals(resp.getMensajeError()) && listenerNotificaciones != null) {
                            Notificacion notif = (Notificacion) resp.getContenido();
                            javax.swing.SwingUtilities.invokeLater(() -> listenerNotificaciones.accept(notif));
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Hilo de escucha finalizado.");
            }
        });
        hilo.setDaemon(true);
        hilo.start();
    }
    
    
    

    public void cerrarConexion() {

        try {
        if (socket != null && !socket.isClosed() && out != null) {
            if (SesionActual.getInstancia().getUserActual() != null) {
                String usuarioActual = SesionActual.getInstancia().getUserActual().getUser();
                out.writeObject(new PeticionRed("DESCONECTAR", usuarioActual));
                out.flush();
            }
        }
        } catch (Exception e) {
           
        }

        
        try { if (out != null) out.close(); 
        } catch (Exception ignored) {}
        try { 
            if (in != null) in.close(); 
        } catch (Exception ignored) {}
        try { 
            if (socket != null && !socket.isClosed()) socket.close(); 
        } catch (Exception ignored) {}

    System.out.println("Conexión con el servidor cerrada correctamente.");
    }
    
    
}
