package info.androidhive.radioucab.Logica;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import info.androidhive.radioucab.Model.Actualizacion;

public class ActualizacionLogica {

    public Date ultimaActWS;

    //1 evento, 2 noticia, 3 programa
    public Actualizacion crearActualizacion(int tipo) {
        Actualizacion nuevaActualizacion = new Actualizacion();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            Date fake = format.parse("01/01/1900 12:00:00");
            switch (tipo) {
                case 1:
                    nuevaActualizacion = new Actualizacion(ultimaActWS, fake, fake);
                    break;
                case 2:
                    nuevaActualizacion = new Actualizacion(fake, ultimaActWS, fake);
                    break;
                case 3:
                    nuevaActualizacion = new Actualizacion(fake, fake, ultimaActWS);
                    break;
            }
        } catch (ParseException e) {
            Log.i("Actualizacion", e.getMessage());
        }
        return nuevaActualizacion;
    }

    //1 evento, 2 noticia, 3 programa
    public Actualizacion modificarActualizacion(int tipo, Date evento, Date noticia, Date programa) {
        Actualizacion nuevaActualizacion = new Actualizacion();
        switch (tipo) {
            case 1:
                nuevaActualizacion.setActEvento(ultimaActWS);
                nuevaActualizacion.setActNoticia(noticia);
                nuevaActualizacion.setActPrograma(programa);
                break;
            case 2:
                nuevaActualizacion.setActEvento(evento);
                nuevaActualizacion.setActNoticia(ultimaActWS);
                nuevaActualizacion.setActPrograma(programa);
                break;
            case 3:
                nuevaActualizacion.setActEvento(evento);
                nuevaActualizacion.setActNoticia(noticia);
                nuevaActualizacion.setActPrograma(ultimaActWS);
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
                nuevaActualizacion = modificarActualizacion(tipo, evento, noticia, programa);
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
