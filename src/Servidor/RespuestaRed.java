/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

import java.io.Serializable;

/**
 *
 * @author vasqu
 */
public class RespuestaRed implements Serializable{
    private static final long serialVersionUID = 1L;

    private boolean exito;
    private Object contenido;
    private String mensajeError;

    public RespuestaRed(boolean exito, Object contenido, String mensajeError) {
        this.exito = exito;
        this.contenido = contenido;
        this.mensajeError = mensajeError;
    }

    public boolean isExito() { 
        return exito; }
    public Object getContenido() { 
        return contenido; }
    public String getMensajeError() { 
        return mensajeError; }
}
