package info.androidhive.radioucab.Logica;


import android.app.Activity;
import android.widget.Toast;

public class ManejoToast {
    private static ManejoToast instancia;
    private Toast toast;

    public static ManejoToast getInstancia() {
        if (instancia == null) {
            instancia = new ManejoToast();
        }
        return instancia;
    }

    public void crearToast(Activity activityActual, String mensaje) {
        toast = Toast.makeText(activityActual, mensaje, Toast.LENGTH_LONG);
        toast.show();

    }

}
