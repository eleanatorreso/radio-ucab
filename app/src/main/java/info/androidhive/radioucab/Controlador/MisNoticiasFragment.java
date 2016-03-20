package info.androidhive.radioucab.Controlador;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import info.androidhive.radioucab.Logica.ManejoActivity;
import info.androidhive.radioucab.R;

public class MisNoticiasFragment extends Fragment {

    private ManejoActivity manejoActivity = ManejoActivity.getInstancia();
    public MisNoticiasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        manejoActivity.mostrarBackToolbar();
        manejoActivity.registrarPantallaAnalytics("Mis noticias");
        return inflater.inflate(R.layout.fragment_mis_noticias, container, false);
    }

}
