package mini.windows;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class GestorArchivosFS {

    private static final int TAMANO_BUFFER = 4096;

    public static boolean crearCarpeta(File carpetaPadre, String nombre) {
        File nueva = new File(carpetaPadre, nombre);
        return nueva.mkdirs();
    }

    public static boolean renombrar(File original, String nuevoNombre) {
        File destino = new File(original.getParentFile(), nuevoNombre);
        if (destino.exists()) {
            return false;
        }
        return original.renameTo(destino);
    }

    public static boolean eliminar(File archivo) {
        if (archivo.isDirectory()) {
            File[] hijos = archivo.listFiles();
            if (hijos != null) {
                for (File hijo : hijos) {
                    eliminar(hijo);
                }
            }
        }
        return archivo.delete();
    }

    public static boolean crearArchivo(File carpetaPadre, String nombre) throws IOException {
        File nuevo = new File(carpetaPadre, nombre);
        return nuevo.createNewFile();
    }

    public static void copiar(File origen, File carpetaDestino) throws IOException {
        if (esMismoOSubcarpeta(origen, carpetaDestino)) {
            throw new IOException("No puedes pegar una carpeta dentro de sí misma.");
        }

        File destino = new File(carpetaDestino, origen.getName());

        if (origen.isDirectory()) {
            if (!destino.exists()) {
                destino.mkdirs();
            }
            File[] hijos = origen.listFiles();
            if (hijos != null) {
                for (File hijo : hijos) {
                    copiar(hijo, destino);
                }
            }
        } else {
            copiarArchivo(origen, destino);
        }
    }

    private static boolean esMismoOSubcarpeta(File origen, File candidato) throws IOException {
        File origenAbsoluto = origen.getCanonicalFile();
        File actual = candidato.getCanonicalFile();

        while (actual != null) {
            if (actual.equals(origenAbsoluto)) {
                return true;
            }
            actual = actual.getParentFile();
        }
        return false;
    }

    private static void copiarArchivo(File origen, File destino) throws IOException {
        try (FileInputStream entrada = new FileInputStream(origen);
             FileOutputStream salida = new FileOutputStream(destino)) {

            byte[] buffer = new byte[TAMANO_BUFFER];
            int bytesLeidos;

            while ((bytesLeidos = entrada.read(buffer)) != -1) {
                salida.write(buffer, 0, bytesLeidos);
            }
        }
    }
}