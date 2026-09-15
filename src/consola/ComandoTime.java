package consola;

import java.util.Calendar;

public class ComandoTime implements Comando {

    @Override
    public String ejecutar(String[] args) {
        if (args.length != 0) {
            return "Uso correcto: time";
        }

        Calendar actual = Calendar.getInstance();
        return String.format("Hora actual: %02d:%02d:%02d",
                actual.get(Calendar.HOUR_OF_DAY),
                actual.get(Calendar.MINUTE),
                actual.get(Calendar.SECOND));
    }
}