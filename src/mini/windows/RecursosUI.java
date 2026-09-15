/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mini.windows;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.net.URL;

/**
 *
 * @author gabri
 */
public class RecursosUI {
 
    private static final String CARPETA_RECURSOS = "/mini/windows/res/";
 
    public static ImageIcon cargarIcono(String nombreArchivo, int ancho, int alto) {
        URL recurso = RecursosUI.class.getResource(CARPETA_RECURSOS + nombreArchivo);
        if (recurso == null) {
            return null;
        }
        ImageIcon original = new ImageIcon(recurso);
        Image escalada = original.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(escalada);
    }
 
    public static Image cargarImagen(String nombreArchivo) {
        URL recurso = RecursosUI.class.getResource(CARPETA_RECURSOS + nombreArchivo);
        if (recurso == null) {
            return null;
        }
        return new ImageIcon(recurso).getImage();
    }
}
 