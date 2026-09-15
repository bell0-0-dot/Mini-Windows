/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Insta;

import Servidor.ClienteInsta;

/**
 *
 * @author vasqu
 */
public class SesionActual {
    
    private static SesionActual Instancia;
    private UsuarioInsta userActual;
    private ClienteInsta cliente;

    private SesionActual() {
    }

    public ClienteInsta getCliente() {
        return cliente;
    }

    public void setCliente(ClienteInsta cliente) {
        this.cliente = cliente;
    }
    
    
    
    public static SesionActual getInstancia(){
        if(Instancia==null){
            Instancia=new SesionActual();
        }
        return Instancia;
    }
    
    public void iniciarSesion(UsuarioInsta usuario){
        userActual=usuario;
    }
    public void cerrarSesion(){
        userActual=null;
    }

    
    public UsuarioInsta getUserActual() {
        return userActual;
    }

   
    
    
}
