package consola;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ComandoDate implements Comando {

    @Override
    public String ejecutar(String[] args) {
        if (args.length != 0) {
            return "Uso correcto: date";
        }

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        return "Fecha actual: " + formato.format(new Date());
    }
}