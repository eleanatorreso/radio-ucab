package info.androidhive.radioucab.Logica;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ManejoFecha {

    private final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
    private final SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");

    public ManejoFecha() {

    }

    public Date getDateNow() {
        Calendar calendario = Calendar.getInstance();
        String tiempoActual = format.format(calendario.getTime());
        try {
            return (format.parse(tiempoActual));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Date convertirString(String fecha) {
        try {
            return format.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Date convertirStringSimple(String fecha) {
        try {
            return format2.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
