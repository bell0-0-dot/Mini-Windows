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
    private static ClienteInsta instancia;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    
    private ClienteInsta() {
        try {
            socket = new Socket("localhost", 12345);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            System.err.println("Error al conectar con el servidor: " + e.getMessage());
        }
    }
    
    public static synchronized ClienteInsta getInstancia() {
        if (instancia == null) {
            instancia = new ClienteInsta();
        }
        return instancia;
    }

    public synchronized RespuestaRed enviarPeticion(PeticionRed peticion) throws Exception {
        out.writeObject(peticion);
        out.flush();
        return (RespuestaRed) in.readObject();
    }
    
}
