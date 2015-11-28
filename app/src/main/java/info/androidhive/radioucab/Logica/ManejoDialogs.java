package info.androidhive.radioucab.Logica;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.WindowManager;

import info.androidhive.radioucab.R;

public class ManejoDialogs extends DialogFragment {

    private String mensaje;
    private String titulo;
    private String botonPositivo;
    private String botonNegativo;
    public Context contextoActual;

    public ManejoDialogs() {}

    public ManejoDialogs(String titulo, String mensaje, String botonPositivo, Context contextoActual) {
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.botonPositivo = botonPositivo;
        this.contextoActual = contextoActual;
    }

    public void crearDialogo() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(contextoActual);
            builder.setTitle(titulo);
            builder.setMessage(mensaje);
            builder.setCancelable(true);
            builder.setPositiveButton(botonPositivo,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            Dialog alert = builder.create();
            alert.getWindow().setType(
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}