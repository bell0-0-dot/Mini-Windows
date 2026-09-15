package consola;

import java.io.File;

public class ComandoCd implements Comando {
    private final SistemaArchivosConsola sistema;

    public ComandoCd(SistemaArchivosConsola sistema) {
        this.sistema = sistema;
    }

    @Override
    public String ejecutar(String[] args) {
        if (args.length == 0) {
            return "Uso correcto: cd <carpeta>";
        }

        String nombre = args[0];
        
        for (int i = 1; i < args.length; i++) {
            nombre = nombre+" "+args[i];
        }

        if (nombre.equals("..")) {
            if (sistema.cambiarAnterior()) {
                return "";
            }
            return "Ya se encuentra en la carpeta raíz.";
        }

        File carpeta = sistema.buscar(nombre);

        if (!carpeta.exists()) {
            return "Carpeta \"" + nombre + "\" no encontrada.";
        }
        if (!carpeta.isDirectory()) {
            return "\"" + nombre + "\" no es una carpeta.";
        }
        if (!sistema.cambiarDir(nombre)) {
            return "No se pudo cambiar a la carpeta \"" + nombre + "\".";
        }

        return "";
    }
}