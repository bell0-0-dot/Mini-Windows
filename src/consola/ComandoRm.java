package consola;

import java.io.File;

public class ComandoRm implements Comando {
    private final SistemaArchivosConsola sistema;

    public ComandoRm(SistemaArchivosConsola sistema) {
        this.sistema = sistema;
    }

    @Override
    public String ejecutar(String[] args) {
        if (args.length != 1) {
            return "Uso correcto: rm <nombre>";
        }

        String nombre = args[0];
        File archivo = sistema.buscar(nombre);

        if (!archivo.exists()) {
            return "Error: \"" + nombre + "\" no existe.";
        }

        if (sistema.eliminar(archivo)) {
            return "\"" + nombre + "\" eliminado correctamente.";
        }

        return "Error: no se pudo eliminar \"" + nombre + "\".";
    }
}