package consola;

import java.util.HashMap;

public class InterpreteComandos {
    private final HashMap<String, Comando> comandos;
    private final SistemaArchivosConsola sistema;

    public InterpreteComandos(SistemaArchivosConsola sistema) {
        this.sistema = sistema;
        comandos = new HashMap<>();
        comandos.put("mkdir", new ComandoMkdir(sistema));
        comandos.put("rm", new ComandoRm(sistema));
        comandos.put("cd", new ComandoCd(sistema));
        comandos.put("cd..", new ComandoCdAnterior(sistema));
        comandos.put("dir", new ComandoDir(sistema));
        comandos.put("date", new ComandoDate());
        comandos.put("time", new ComandoTime());
    }

    public String ejecutar(String entrada) {
        if (entrada == null || entrada.trim().isEmpty()) {
            return "";
        }

        String[] partes = entrada.trim().split("\\s+");
        String nombreComando = partes[0].toLowerCase();

        Comando comando = comandos.get(nombreComando);
        if (comando == null) {
            return "'" + partes[0] + "' no se reconoce como un comando interno.";
        }

        String[] argumentos = new String[partes.length - 1];
        System.arraycopy(partes, 1, argumentos, 0, partes.length - 1);

        return comando.ejecutar(argumentos);
    }

    public String getRutaActual() {
        return sistema.getRutaActual();
    }
}