/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mini.windows;

import base.Usuario;

/**
 *
 * @author gabri
 */
public class SesionActual {
    private static Usuario usuarioActual;
 
    private SesionActual() {
    }
 
    public static void iniciarSesion(Usuario usuario) {
        usuarioActual = usuario;
    }
 
    public static void cerrarSesion() {
        usuarioActual = null;
    }
 
    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }
 
    public static boolean haySesionActiva() {
        return usuarioActual != null;
    }
}
