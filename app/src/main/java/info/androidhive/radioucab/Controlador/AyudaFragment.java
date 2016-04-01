package info.androidhive.radioucab.Controlador;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import info.androidhive.radioucab.Logica.ManejoActivity;
import info.androidhive.radioucab.R;

public class AyudaFragment extends Fragment {

    private ManejoActivity manejoActivity = ManejoActivity.getInstancia();

    public AyudaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ayuda, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manejoActivity.mostrarBackToolbar();
    }


}
