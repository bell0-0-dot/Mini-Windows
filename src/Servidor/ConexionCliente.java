/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

import java.io.ObjectOutputStream;

/**
 *
 * @author vasqu
 */
public class ConexionCliente {
    private String usuario;
    private ObjectOutputStream out;

    public ConexionCliente(String usuario, ObjectOutputStream out) {
        this.usuario = usuario;
        this.out = out;
    }

    public String getUsuario() { 
        return usuario; }
    public ObjectOutputStream getOut() { 
        return out; }
    
}
