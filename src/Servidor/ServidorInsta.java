/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

import base.ListaEnlazada;
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

        try (ServerSocket serverSocket =
                     new ServerSocket(PUERTO)) {

            System.out.println(
                "Servidor escuchando en el puerto "
                + PUERTO
            );

            while (true) {

                Socket clienteSocket =
                        serverSocket.accept();

                System.out.println(
                    "Cliente conectado desde: "
                    + clienteSocket.getInetAddress()
                );

                Thread hilo =
                        new Thread(
                            new ManejarCliente(clienteSocket)
                        );

                hilo.start();
            }

        } catch (Exception e) {

            System.out.println(
                "ERROR EN EL SERVIDOR:"
            );

            e.printStackTrace();
        }
    }

    public static synchronized void agregarCliente(
            String usuario,
            ObjectOutputStream out) {

        removerCliente(usuario);

        clientesConectados.insertarFinal(
            new ConexionCliente(
                usuario.toLowerCase(),
                out
            )
        );
    }

    public static synchronized ObjectOutputStream
            obtenerStreamCliente(String usuario) {

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

    public static synchronized void removerCliente(
            String usuario) {

        if (usuario == null) {
            return;
        }

        for (int i = 0;
             i < clientesConectados.length();
             i++) {

            ConexionCliente conexion =
                    clientesConectados.obtenerEn(i);

            if (conexion.getUsuario()
                    .equalsIgnoreCase(usuario)) {

                clientesConectados.eliminar(conexion);
                break;
            }
        }
    }

}
