package info.androidhive.radioucab.Logica;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ManejoFecha {

    private final SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
    private final SimpleDateFormat formato2 = new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");

    public ManejoFecha() {

    }

    public Date getDateNow() {
        Calendar calendario = Calendar.getInstance();
        String tiempoActual = formato.format(calendario.getTime());
        try {
            return (formato.parse(tiempoActual));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Date convertirString(String fecha) {
        try {
            return formato.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Date convertirStringSimple(String fecha) {
        try {
            return formato2.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Date convertirStringHora(String fecha) {
        try {
            return formatoHora.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getDiaSemana (Date fecha){
        final SimpleDateFormat sdf_ = new SimpleDateFormat("EEEE");
        final String diaSemana = sdf_.format(fecha);
        return diaSemana.substring(0, 1).toUpperCase() + diaSemana.substring(1);
    }

    public String getHoraActual ()  {
        return formatoHora.format(new Date());
    }

    public String getUltimoMinutoDia () {
        Calendar c = Calendar.getInstance();
        c.set(1970,01,01,23,59,59);
        formatoHora.applyPattern("HH:mm:ss");
        return formatoHora.format(c.getTime());
    }

    public String getHoraTope() {
        Calendar c = Calendar.getInstance();
        c.set(1970,01,01,00,00);
        return formatoHora.format(c.getTime());
    }

}
