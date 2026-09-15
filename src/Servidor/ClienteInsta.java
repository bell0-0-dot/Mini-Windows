/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author vasqu
 */
public class ClienteInsta {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Thread hiloEscucha;
    private boolean escuchando;

    public ClienteInsta() throws Exception {

        socket = new Socket("localhost", 12345);

        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();

        in = new ObjectInputStream(socket.getInputStream());

        System.out.println("Cliente conectado al servidor.");
    }

    public synchronized RespuestaRed enviarPeticion(PeticionRed peticion)
            throws Exception {

        System.out.println(
            "Enviando petición: " + peticion.getComando()
        );

        out.writeObject(peticion);
        out.flush();

        RespuestaRed respuesta =
                (RespuestaRed) in.readObject();

        System.out.println("Respuesta recibida.");

        return respuesta;
    }

    public void cerrarConexion() {

        try {
            if (in != null) {
                in.close();
            }

            if (out != null) {
                out.close();
            }

            if (socket != null) {
                socket.close();
            }

        } catch (Exception e) {
            System.out.println(
                "Error cerrando conexión: " + e.getMessage()
            );
        }
    }
    
    
}
