/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Insta;

/**
 *
 * @author vasqu
 */
public class SesionActual {
    
    private static SesionActual Instancia;
    private UsuarioInsta userActual;

    private SesionActual() {
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
