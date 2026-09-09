package mini.windows;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public class CargadorImagenTarea implements Runnable {

    private final File archivoImagen;
    private final int anchoMaximo;
    private final int altoMaximo;
    private final Consumer<ImageIcon> alTerminar;

    public CargadorImagenTarea(File archivoImagen, int anchoMaximo, int altoMaximo,Consumer<ImageIcon> alTerminar) {
        this.archivoImagen = archivoImagen;
        this.anchoMaximo = anchoMaximo;
        this.altoMaximo = altoMaximo;
        this.alTerminar = alTerminar;
    }

    @Override
    public void run() {
        ImageIcon resultado = null;

        try {
            BufferedImage original = ImageIO.read(archivoImagen);
            if (original != null) {
                Image escalada = EscaladorImagenes.escalarConservandoProporcion(original, anchoMaximo, altoMaximo);
                resultado = new ImageIcon(escalada);
            }
        } catch (IOException e) {
            System.err.println("No se pudo leer la imagen " + archivoImagen.getName() + ": " + e.getMessage());
        }

        final ImageIcon iconoFinal = resultado;
        SwingUtilities.invokeLater(() -> alTerminar.accept(iconoFinal));
    }
}
