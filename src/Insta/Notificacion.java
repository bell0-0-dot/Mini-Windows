/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Insta;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author vasqu
 */
public class Notificacion implements Serializable {
    private static final long serialVersionUID = 1L;
    private String emisor;      
    private String receptor;    
    private TipoNotificacion tipo;
    private String mensaje;      
    private LocalDateTime fecha;

    public Notificacion(String emisor, String receptor, TipoNotificacion tipo, String mensaje) {
        
        this.emisor = emisor;
        this.receptor = receptor;
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.fecha = LocalDateTime.now();
    }
    
     public enum TipoNotificacion {
        LIKE, COMENTARIO, SEGUIDOR, MENCION
    }

 
    public String getEmisor() { 
        return emisor; }
    public String getReceptor() {
        return receptor; }
    public TipoNotificacion getTipo() { 
        return tipo; }
    public String getMensaje() { 
        return mensaje; }
    public LocalDateTime getFecha() {
        return fecha; }

    
}
