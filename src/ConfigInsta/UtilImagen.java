/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ConfigInsta;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

/**
 *
 * @author vasqu
 */
public class UtilImagen {
    private UtilImagen() {
    }

    public static Image escalarAlta(Image original, int ancho, int alto) {
        BufferedImage resultado = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resultado.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(original, 0, 0, ancho, alto, null);
        g2.dispose();
        return resultado;
    }

    public static ImageIcon cargarIconoEscalado(String ruta, int ancho, int alto) {
        Image original = new ImageIcon(UtilImagen.class.getResource(ruta)).getImage();
        return new ImageIcon(escalarAlta(original, ancho, alto));
    }
    
}
