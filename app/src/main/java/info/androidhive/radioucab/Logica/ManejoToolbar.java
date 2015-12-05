package info.androidhive.radioucab.Logica;

import android.app.Activity;
import android.support.v7.widget.Toolbar;

import info.androidhive.radioucab.R;

public class ManejoToolbar {

    private static ManejoToolbar instancia;
    private static Activity activityPrincipal;
    private Toolbar toolbar;

    public static ManejoToolbar getInstancia () {
        if (instancia == null) {
            instancia = new ManejoToolbar();
        }
        return instancia;
    }

    public Activity getActivityPrincipal() {
        return activityPrincipal;
    }

    public void setActivityPrincipal(Activity activityPrincipal) {
        this.activityPrincipal = activityPrincipal;
        toolbar = (Toolbar) activityPrincipal.findViewById(R.id.toolbar);
    }

    public void cambiarDeColor (int seccion) {
        int color = 0;
        switch (seccion) {
            //Home
            case 1:
                color = activityPrincipal.getResources().getColor(R.color.azul_radio_ucab);
                break;
            //que esta pasando hoy
            case 2:
                color = activityPrincipal.getResources().getColor(R.color.azul_radio_ucab);
                break;
            //noticias
            case 3:
                color = activityPrincipal.getResources().getColor(R.color.amarillo_ucab);
                break;
            //eventos
            case 4:
                color = activityPrincipal.getResources().getColor(R.color.azul_radio_ucab);
                break;
            //programas
            case 5:
                color = activityPrincipal.getResources().getColor(R.color.verde_ucab);
                break;
            //perfil
            case 6:
                color = activityPrincipal.getResources().getColor(R.color.azul_ucab);
                break;
            //configuracion
            case 7:
                color = activityPrincipal.getResources().getColor(R.color.gris_claro);
                break;
        }
        toolbar.setBackgroundColor(color);
    }

}
