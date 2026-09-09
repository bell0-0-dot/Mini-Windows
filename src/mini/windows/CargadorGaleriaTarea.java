package mini.windows;

import base.ListaEnlazada;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.BiConsumer;

public class CargadorGaleriaTarea implements Runnable {

    private final ListaEnlazada<File> imagenes;
    private final int tamanoMiniatura;
    private final BiConsumer<Integer, ImageIcon> alCargarUna;

    public CargadorGaleriaTarea(ListaEnlazada<File> imagenes, int tamanoMiniatura,
                                 BiConsumer<Integer, ImageIcon> alCargarUna) {
        this.imagenes = imagenes;
        this.tamanoMiniatura = tamanoMiniatura;
        this.alCargarUna = alCargarUna;
    }

    @Override
    public void run() {
        for (int i = 0; i < imagenes.length(); i++) {
            File archivo = imagenes.obtenerEn(i);
            ImageIcon miniatura = cargarMiniatura(archivo);

            final int indice = i;
            SwingUtilities.invokeLater(() -> alCargarUna.accept(indice, miniatura));
        }
    }

    private ImageIcon cargarMiniatura(File archivo) {
        try {
            BufferedImage original = ImageIO.read(archivo);
            if (original == null) return null;

            Image escalada = EscaladorImagenes.escalarConservandoProporcion(original, tamanoMiniatura, tamanoMiniatura);
            return new ImageIcon(escalada);
        } catch (IOException e) {
            return null;
        }
    }
}
