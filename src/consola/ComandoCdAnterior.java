package consola;

public class ComandoCdAnterior implements Comando {
    private final SistemaArchivosConsola sistema;

    public ComandoCdAnterior(SistemaArchivosConsola sistema) {
        this.sistema = sistema;
    }

    @Override
    public String ejecutar(String[] args) {
        if (args.length != 0) {
            return "Uso correcto: cd..";
        }

        if (sistema.cambiarAnterior()) {
            return "";
        }

        return "Ya se encuentra en la carpeta raíz.";
    }
}