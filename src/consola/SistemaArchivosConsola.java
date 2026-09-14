package consola;

import java.io.File;

/**
 * @author gabri
 */
public class SistemaArchivosConsola {
    private final File raiz;
    private File actual;

    public SistemaArchivosConsola(String rutaRaizUsuario) {
        this.raiz = new File(rutaRaizUsuario);

        if (!raiz.exists()) {
            raiz.mkdirs();
        }

        this.actual = raiz;
    }

    public File getActual() {
        return actual;
    }

    public String getRutaActual() {
        return actual.getAbsolutePath();
    }

    public File buscar(String nombre) {
        return new File(actual, nombre);
    }

    public boolean crearDir(String nombre) {
        File nuevoDir = new File(actual, nombre);
        if (nuevoDir.exists()) {
            return false;
        }
        return nuevoDir.mkdir();
    }

    private boolean estaDentroDeRaiz(File archivo) {
        try {
            String rutaRaiz = raiz.getCanonicalPath();
            String rutaArchivo = archivo.getCanonicalPath();
            return rutaArchivo.startsWith(rutaRaiz);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean cambiarAnterior() {
        if (actual.equals(raiz)) {
            return false;
        }

        File padre = actual.getParentFile();

        if (padre != null && estaDentroDeRaiz(padre)) {
            actual = padre;
            return true;
        }

        return false;
    }

    public boolean cambiarDir(String nombre) {
        File nuevoDir = new File(actual, nombre);

        if (!nuevoDir.exists() || !nuevoDir.isDirectory()) {
            return false;
        }

        if (!estaDentroDeRaiz(nuevoDir)) {
            return false;
        }

        actual = nuevoDir;
        return true;
    }

    public boolean eliminar(File archivo) {
        if (!archivo.exists()) {
            return false;
        }

        if (archivo.isDirectory()) {
            File[] contenido = archivo.listFiles();
            if (contenido != null) {
                for (File elemento : contenido) {
                    eliminar(elemento);
                }
            }
        }

        return archivo.delete();
    }
}