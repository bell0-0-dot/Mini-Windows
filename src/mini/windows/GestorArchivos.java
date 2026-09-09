/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mini.windows;

import Excepciones.ArchivoCorruptoException;
import base.ArchivoUtil;
import base.ListaEnlazada;
import base.Usuario;
import Excepciones.UsernameDuplicadoException;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author gabri
 */
public class GestorArchivos {
    
    
    public static final String RAIZ = "Z" + File.separator;
    public static final String RUTA_USUARIOS = RAIZ + "usuarios.sop";
    
    public static void inicializarSistema() throws IOException, ArchivoCorruptoException {
 
        File carpetaRaiz = new File(RAIZ);
        if (!carpetaRaiz.exists()) {
            carpetaRaiz.mkdirs();
        }
 
        ListaEnlazada<Usuario> usuarios = ArchivoUtil.leerLista(RUTA_USUARIOS);
 
        if (usuarios.estaVacia()) {
            Usuario admin = new Usuario("admin", "admin");
            admin.setAdmin(true);
            usuarios.insertarFinal(admin);
            ArchivoUtil.guardarLista(RUTA_USUARIOS, usuarios);
            crearCarpetasDeUsuario("admin");
        }
    }
 
    public static void crearUsuario(String username, String password, boolean esAdmin)
            throws UsernameDuplicadoException, IOException, ArchivoCorruptoException {
 
        ListaEnlazada<Usuario> usuarios = ArchivoUtil.leerLista(RUTA_USUARIOS);
 
        if (existeUsername(usuarios, username)) {
            throw new UsernameDuplicadoException(username);
        }
 
        Usuario nuevo = new Usuario(username, password);
        nuevo.setAdmin(esAdmin);
        usuarios.insertarFinal(nuevo);
        ArchivoUtil.guardarLista(RUTA_USUARIOS, usuarios);
 
        crearCarpetasDeUsuario(username);
    }
 
    public static Usuario buscarUsuario(String username) throws ArchivoCorruptoException {
        ListaEnlazada<Usuario> usuarios = ArchivoUtil.leerLista(RUTA_USUARIOS);
        for (int i = 0; i < usuarios.length(); i++) {
            Usuario u = usuarios.obtenerEn(i);
            if (u.getUser().equalsIgnoreCase(username)) {
                return u;
            }
        }
        return null;
    }
 
    public static String rutaCarpetaUsuario(String username) {
        return RAIZ + username + File.separator;
    }
 
    private static boolean existeUsername(ListaEnlazada<Usuario> usuarios, String username) {
        for (int i = 0; i < usuarios.length(); i++) {
            if (usuarios.obtenerEn(i).getUser().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }
 
    private static void crearCarpetasDeUsuario(String username) {
        String base = rutaCarpetaUsuario(username);
        new File(base + "Mis Documentos").mkdirs();
        new File(base + "Musica").mkdirs();
        new File(base + "Mis Imagenes").mkdirs();
    }
    
}
