package info.androidhive.radioucab.Logica;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import info.androidhive.radioucab.Model.Actualizacion;
import info.androidhive.radioucab.Model.HorarioPrograma;
import info.androidhive.radioucab.Model.Programa;

public class ParillaLogica {

    private static ParillaLogica instancia;
    private static List<Programa> parrillaDelDia;
    private static Programa programaActual;

    public static ParillaLogica getInstancia() {
        if (instancia == null) {
            instancia = new ParillaLogica();
        }
        return instancia;
    }

    public List<Programa> getParrillaDelDia() {
        return parrillaDelDia;
    }

    public void setParrillaDelDia(List<Programa> parrillaDelDia) {
        this.parrillaDelDia = parrillaDelDia;
    }


    public boolean comprobarActualizacionParrilla() {
        List<Actualizacion> listaActualizaciones = Actualizacion.listAll(Actualizacion.class);
        if (listaActualizaciones != null && !listaActualizaciones.isEmpty()) {
            Actualizacion actualizacion = listaActualizaciones.get(0);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            if (sdf.format(actualizacion.getActParrilla()).equals(sdf.format(new Date()))) {
                return true;
            }
        }
        return false;
    }

    public void getProgramaActual() {
        Calendar hoy = Calendar.getInstance();
        int dia_actual = hoy.get(Calendar.DAY_OF_WEEK);
        for (Programa programa : parrillaDelDia) {
           // List<HorarioPrograma> horario =
            //Time hora_inicio = programa
        }
    }
}
