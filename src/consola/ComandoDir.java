package consola;

import java.io.File;

public class ComandoDir implements Comando {
    private final SistemaArchivosConsola sistema;

    public ComandoDir(SistemaArchivosConsola sistema) {
        this.sistema = sistema;
    }

    @Override
    public String ejecutar(String[] args) {
        File actual = sistema.getActual();
        File[] lista = actual.listFiles();

        if (lista == null || lista.length == 0) {
            return "La carpeta está vacía.";
        }

        StringBuilder resultado = new StringBuilder();
        resultado.append("Directorio de \"").append(sistema.getRutaActual()).append("\":\n\n");

        int contDir = 0, contArchivo = 0;

        for (File f : lista) {
            if (f.isDirectory()) {
                resultado.append("<DIR>     ").append(f.getName()).append("\n");
                contDir++;
            } else {
                resultado.append("          ").append(f.getName()).append("\n");
                contArchivo++;
            }
        }

        resultado.append("\n     ").append(contArchivo).append(" archivo(s)");
        resultado.append("\n     ").append(contDir).append(" carpeta(s)");

        return resultado.toString();
    }
}