/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author vasqu
 */
public class ServidorInsta {
    private static final int PUERTO = 12345;

    public static void main(String[] args) {
        System.out.println("Iniciando Servidor Instagram...");
        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            System.out.println("Servidor escuchando en el puerto " + PUERTO);
            
            while (true) {
                Socket clienteSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde: " + clienteSocket.getInetAddress());
                
               
                new Thread(new ManejarCliente(clienteSocket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
}


}
