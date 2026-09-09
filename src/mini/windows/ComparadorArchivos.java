package mini.windows;

import java.io.File;
import java.util.List;

public class ComparadorArchivos {

    public enum Criterio {
        NOMBRE, FECHA, TIPO, TAMANO
    }

    public static void ordenar(List<File> archivos, Criterio criterio) {
        int n = archivos.size();

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                File actual = archivos.get(j);
                File siguiente = archivos.get(j + 1);

                if (vaDespuesDe(actual, siguiente, criterio)) {
                    archivos.set(j, siguiente);
                    archivos.set(j + 1, actual);
                }
            }
        }
    }

    private static boolean vaDespuesDe(File actual, File siguiente, Criterio criterio) {
        switch (criterio) {
            case FECHA:
                return actual.lastModified() > siguiente.lastModified();
            case TAMANO:
                return actual.length() > siguiente.length();
            case TIPO:
                return extensionDe(actual).compareToIgnoreCase(extensionDe(siguiente)) > 0;
            case NOMBRE:
            default:
                return actual.getName().compareToIgnoreCase(siguiente.getName()) > 0;
        }
    }

    private static String extensionDe(File archivo) {
        String nombre = archivo.getName().toLowerCase();
        int punto = nombre.lastIndexOf('.');
        return punto == -1 ? "" : nombre.substring(punto + 1);
    }
}