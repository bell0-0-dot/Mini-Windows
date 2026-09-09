package base;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;

public class FileChooserUtil {

    public static JFileChooser crearRestringido(File carpetaRaiz) {
        File raizAbsoluta = carpetaRaiz.getAbsoluteFile();

        FileSystemView vistaRestringida = new FileSystemView() {

            @Override
            public File createNewFolder(File directorioContenedor) throws IOException {
                File nuevaCarpeta = new File(directorioContenedor, "Nueva carpeta");
                if (!nuevaCarpeta.mkdir()) {
                    throw new IOException("No se pudo crear la carpeta.");
                }
                return nuevaCarpeta;
            }

            @Override
            public File[] getRoots() {
                return new File[]{raizAbsoluta};
            }

            @Override
            public File getHomeDirectory() {
                return raizAbsoluta;
            }

            @Override
            public File getDefaultDirectory() {
                return raizAbsoluta;
            }

            @Override
            public File getParentDirectory(File dir) {
                if (dir == null) {
                    return raizAbsoluta;
                }
                File dirAbsoluto = dir.getAbsoluteFile();

                if (dirAbsoluto.equals(raizAbsoluta)) {
                    return raizAbsoluta;
                }
                return super.getParentDirectory(dir);
            }
        };

        JFileChooser selector = new JFileChooser(raizAbsoluta, vistaRestringida);
        selector.setCurrentDirectory(raizAbsoluta);
        return selector;
    }
}
