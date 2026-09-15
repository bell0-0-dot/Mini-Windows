package consola;

public class ComandoMkdir implements Comando {
    private final SistemaArchivosConsola sistema;

    public ComandoMkdir(SistemaArchivosConsola sistema) {
        this.sistema = sistema;
    }

    @Override
    public String ejecutar(String[] args) {
        if (args.length != 1) {
            return "Uso correcto: mkdir <nombre>";
        }

        String nombre = args[0];

        if (nombre.trim().isEmpty()) {
            return "Error: debe especificar el nombre de la carpeta.";
        }

        if (sistema.crearDir(nombre)) {
            return "Carpeta \"" + nombre + "\" creada correctamente.";
        }

        return "Error: no se pudo crear la carpeta \"" + nombre + "\". Puede que ya exista.";
    }
}