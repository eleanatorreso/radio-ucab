package info.androidhive.radioucab.Logica;

import java.util.TimeZone;

public class ZonaHoraria {
    private static ZonaHoraria ourInstance = new ZonaHoraria();

    public static ZonaHoraria getInstance() {
        return ourInstance;
    }
    private TimeZone zonaHorariaActual;

    private ZonaHoraria() {
    }


}
