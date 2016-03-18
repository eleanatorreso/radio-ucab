package info.androidhive.radioucab.Logica;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

public class ManejoProgressDialog {

    private static ManejoProgressDialog instancia;
    private static ProgressDialog progressDialog;

    public static ManejoProgressDialog getInstancia() {
        if (instancia == null) {
            instancia = new ManejoProgressDialog();
        }
        return instancia;
    }

    public void crearProgressDialog(Context contexto){
        progressDialog = new ProgressDialog(contexto);
        progressDialog.setMessage("Comprobando concursos...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    public void cancelarProgressDialog(){
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
    public void iniciarProgressDialog(String mensaje, Activity activity){
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
        progressDialog = ProgressDialog.show(activity,"",mensaje,true, true);
    }

}
