package info.androidhive.radioucab.Controlador;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import info.androidhive.radioucab.Logica.ManejoToolbar;
import info.androidhive.radioucab.R;

public class ConfiguracionFragment extends Fragment {

    private ManejoToolbar toolbar = ManejoToolbar.getInstancia();
	public ConfiguracionFragment(){}
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_configuracion, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.cambiarDeColor(7);
    }

}
