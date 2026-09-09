package mini.windows;

import java.awt.Image;
import java.awt.image.BufferedImage;

public class EscaladorImagenes {

    public static Image escalarConservandoProporcion(BufferedImage original, int anchoMax, int altoMax) {
        int anchoOriginal = original.getWidth();
        int altoOriginal = original.getHeight();

        double escala = Math.min((double) anchoMax / anchoOriginal, (double) altoMax / altoOriginal);
        if (escala > 1) {
            escala = 1;
        }

        int nuevoAncho = Math.max((int) (anchoOriginal * escala), 1);
        int nuevoAlto = Math.max((int) (altoOriginal * escala), 1);

        return original.getScaledInstance(nuevoAncho, nuevoAlto, Image.SCALE_SMOOTH);
    }
}
