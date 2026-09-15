/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

import ConfigInsta.ServicioArchivoInsta;
import Insta.Genero;
import Insta.UsuarioInsta;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author vasqu
 */
public class ManejarCliente implements Runnable{
    private Socket socket;

        public ManejarCliente(Socket socket) {
            this.socket = socket;
        }
       @Override
        public void run() {
            try (
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
            ) {
                while (!socket.isClosed()) {
                    PeticionRed peticion = (PeticionRed) in.readObject();
                    RespuestaRed respuesta = procesarPeticion(peticion);
                    out.writeObject(respuesta);
                    out.flush();
                }
            } catch (Exception e) {
                System.out.println("Cliente desconectado: " + socket.getInetAddress());
            }
        }
        
      private RespuestaRed procesarPeticion(PeticionRed peticion) {
            try {
                switch (peticion.getComando()) {
                    case "LOGIN": {
                        String user = (String) peticion.getParametros()[0];
                        String pass = (String) peticion.getParametros()[1];
                    
                        synchronized (ServicioArchivoInsta.class) {
                            UsuarioInsta u = ServicioArchivoInsta.validarLogin(user, pass);
                            return new RespuestaRed(true, u, null);
                        }
                    }
                    case "REGISTRAR": {
                        String nombre = (String) peticion.getParametros()[0];
                        Genero genero = (Genero) peticion.getParametros()[1];
                        int edad = (Integer) peticion.getParametros()[2];
                        String username = (String) peticion.getParametros()[3];
                        String password = (String) peticion.getParametros()[4];
                        String nombreFoto = (String) peticion.getParametros()[5];

                        synchronized (ServicioArchivoInsta.class) {
                            UsuarioInsta nuevo = ServicioArchivoInsta.registrarUsuario(
                                    nombre, genero, edad, username, password, nombreFoto
                            );
                            return new RespuestaRed(true, nuevo, null);
                        }
                    }
                    default:
                        return new RespuestaRed(false, null, "Comando desconocido");
                }
            } catch (Exception e) {
                return new RespuestaRed(false, e, e.getMessage());
            }
        }
    }  
        



