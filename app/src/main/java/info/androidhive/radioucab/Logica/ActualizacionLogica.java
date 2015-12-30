package info.androidhive.radioucab.Logica;

import android.text.format.DateUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import info.androidhive.radioucab.Model.Actualizacion;

public class ActualizacionLogica {

    public Date ultimaActWS;

    //1 evento, 2 noticia, 3 programa, 4 parrilla
    public Actualizacion crearActualizacion(int tipo) {
        Actualizacion nuevaActualizacion = new Actualizacion();
        try {
            switch (tipo) {
                case 1:
                    nuevaActualizacion = new Actualizacion(ultimaActWS, null, null, null);
                    break;
                case 2:
                    nuevaActualizacion = new Actualizacion(null, ultimaActWS, null, null);
                    break;
                case 3:
                    nuevaActualizacion = new Actualizacion(null, null, ultimaActWS, null);
                    break;
                case 4:
                    nuevaActualizacion = new Actualizacion(null, null, null, ultimaActWS);
                    break;
            }
        } catch (Exception e) {
            Log.i("Actualizacion", e.getMessage());
        }
        return nuevaActualizacion;
    }

    //1 evento, 2 noticia, 3 programa
    public Actualizacion modificarActualizacion(int tipo, Date evento, Date noticia, Date programa, Date parrilla) {
        Actualizacion nuevaActualizacion = new Actualizacion();
        switch (tipo) {
            case 1:
                nuevaActualizacion.setActEvento(ultimaActWS);
                nuevaActualizacion.setActNoticia(noticia);
                nuevaActualizacion.setActPrograma(programa);
                nuevaActualizacion.setActParrilla(parrilla);
                break;
            case 2:
                nuevaActualizacion.setActEvento(evento);
                nuevaActualizacion.setActNoticia(ultimaActWS);
                nuevaActualizacion.setActPrograma(programa);
                nuevaActualizacion.setActParrilla(parrilla);
                break;
            case 3:
                nuevaActualizacion.setActEvento(evento);
                nuevaActualizacion.setActNoticia(noticia);
                nuevaActualizacion.setActPrograma(ultimaActWS);
                nuevaActualizacion.setActParrilla(parrilla);
                break;
        }
        return nuevaActualizacion;
    }

    public void almacenarUltimaActualizacion(int tipo, Date ultimaActWS) {
        this.ultimaActWS = ultimaActWS;
        try {
            Actualizacion ultimaActualizacion = new Actualizacion();
            Actualizacion nuevaActualizacion = new Actualizacion();
            List<Actualizacion> listaActualizaciones = Actualizacion.listAll(Actualizacion.class);
            if (listaActualizaciones != null && listaActualizaciones.size() > 0) {
                ultimaActualizacion = listaActualizaciones.get(0);
                Date noticia = ultimaActualizacion.getActNoticia();
                Date evento = ultimaActualizacion.getActEvento();
                Date programa = ultimaActualizacion.getActPrograma();
                Date parrilla = ultimaActualizacion.getActParrilla();
                nuevaActualizacion = modificarActualizacion(tipo, evento, noticia, programa, parrilla);
                Actualizacion.deleteAll(Actualizacion.class);
            } else {
                nuevaActualizacion = crearActualizacion(tipo);
            }
            nuevaActualizacion.save();
        } catch (Exception e) {
            Log.e("Evento: ult.act", e.getMessage());
        }
    }

}
