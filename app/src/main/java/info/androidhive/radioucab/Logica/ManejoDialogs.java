package info.androidhive.radioucab.Logica;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import info.androidhive.radioucab.Controlador.PerfilFragment;
import info.androidhive.radioucab.R;

public class ManejoDialogs extends DialogFragment {

    private String mensaje;
    private String titulo;
    private String botonPositivo;
    private String botonNegativo;
    private Context contextoActual;
    public boolean respuesta;
    public View view;
    private int tipo;

    public ManejoDialogs() {
    }

    public ManejoDialogs(String titulo, String mensaje, String botonPositivo, Context contextoActual) {
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.botonPositivo = botonPositivo;
        this.contextoActual = contextoActual;
    }

    public ManejoDialogs(String titulo, String mensaje, String botonPositivo, String botonNegativo, Context contextoActual, int tipo) {
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.botonPositivo = botonPositivo;
        this.botonNegativo = botonNegativo;
        this.contextoActual = contextoActual;
        this.tipo = tipo;
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
            Dialog alerta = builder.create();
            alerta.getWindow().setType(
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            alerta.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clasificarTipo () {
        switch (tipo) {
            case 1: {
                if (respuesta == true) {
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.addToBackStack("atras");
                    PerfilFragment perfilFragment = new PerfilFragment();
                    ft.replace(((ViewGroup) view.getParent()).getId(), perfilFragment);
                    ft.commit();
                }
            }
        }
    }

    public void crearDialogoSiYNo() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(contextoActual);
            builder.setTitle(titulo);
            builder.setMessage(mensaje);
            builder.setPositiveButton(botonPositivo,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            respuesta = true;
                            clasificarTipo();
                        }
                    });
            builder.setNegativeButton(botonNegativo,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            respuesta = false;
                        }
                    });
            Dialog alerta = builder.create();
            alerta.getWindow().setType(
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            alerta.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}