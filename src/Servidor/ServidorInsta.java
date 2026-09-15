/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

import Insta.Notificacion;
import base.ListaEnlazada;
import base.Nodo;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author vasqu
 */
public class ServidorInsta {
    private static final int PUERTO = 12345;

    private static ListaEnlazada<ConexionCliente>
            clientesConectados = new ListaEnlazada<>();

    public static void main(String[] args) {

        System.out.println(
            "Iniciando Servidor Instagram..."
        );

        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {

            System.out.println("Servidor escuchando en el puerto "
                + PUERTO
            );

            while (true) {

                Socket clienteSocket = serverSocket.accept();

                System.out.println( "Cliente conectado desde: "+ clienteSocket.getInetAddress()
                );

                Thread hilo = new Thread(new ManejarCliente(clienteSocket) );
                hilo.start();
            }

        } catch (Exception e) {

            System.out.println(
                "ERROR EN EL SERVIDOR:"
            );

            e.printStackTrace();
        }
    }

    public static synchronized void agregarCliente( String usuario,ObjectOutputStream out) {
        removerCliente(usuario);

        clientesConectados.insertarFinal(new ConexionCliente(usuario.toLowerCase(),out)
        );
        System.out.println("Cliente registrado en servidor: " + usuario);
    }
 
    public static synchronized void removerCliente( String usuario) {
        Nodo<ConexionCliente> actual = clientesConectados.getCabeza();
        while (actual != null) {
            if (actual.getDato().getUsuario().equalsIgnoreCase(usuario)) {
                clientesConectados.eliminar(actual.getDato());
                System.out.println("Cliente removido del servidor: " + usuario);
                break;
            }
            actual = actual.getSiguiente();
        }
    }
    
    public static synchronized ObjectOutputStream obtenerStreamCliente(String usuario) {
        for (int i = 0;
             i < clientesConectados.length();
             i++) {

            ConexionCliente conexion =
                    clientesConectados.obtenerEn(i);

            if (conexion.getUsuario()
                    .equalsIgnoreCase(usuario)) {

                return conexion.getOut();
            }
        }

        return null;
    }
    
    public static synchronized void notificarUsuario(String destino, Notificacion notif) {
        ObjectOutputStream outDestino = obtenerStreamCliente(destino);
        if (outDestino != null) {
            try {
                RespuestaRed evento = new RespuestaRed(true, notif, "NUEVA_NOTIFICACION");
                synchronized (outDestino) {
                    outDestino.writeObject(evento);
                    outDestino.flush();
                }
            } catch (Exception e) {
                System.err.println("Error al notificar a " + destino + ": " + e.getMessage());
            }
        }

    }
}
