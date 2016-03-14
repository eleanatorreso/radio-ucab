package info.androidhive.radioucab.Controlador;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import info.androidhive.radioucab.Logica.ManejoActivity;
import info.androidhive.radioucab.R;

public class MisConcursosFragment extends Fragment {

    private ManejoActivity manejoActivity = ManejoActivity.getInstancia();

    public MisConcursosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        manejoActivity.mostrarBackToolbar();
        return inflater.inflate(R.layout.fragment_mis_concursos, container, false);
    }


}
