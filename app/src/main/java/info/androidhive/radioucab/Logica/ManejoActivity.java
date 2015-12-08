package info.androidhive.radioucab.Logica;

import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import info.androidhive.radioucab.R;

public class ManejoActivity {

    private static ManejoActivity instancia;
    private static Activity activityPrincipal;
    private Toolbar toolbar;

    public static ManejoActivity getInstancia () {
        if (instancia == null) {
            instancia = new ManejoActivity();
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

    public void cambiarIconoMenu() {
        ImageView botonMenu = (ImageView) getActivityPrincipal().findViewById(R.id.icono_menu);
        botonMenu.setImageDrawable(getActivityPrincipal().getResources().getDrawable(R.drawable.ic_menu_white_24dp));
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
