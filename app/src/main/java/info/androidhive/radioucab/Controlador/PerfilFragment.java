package info.androidhive.radioucab.Controlador;

import info.androidhive.radioucab.Logica.PerfilLogica;
import info.androidhive.radioucab.R;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.twitter.sdk.android.Twitter;

public class PerfilFragment extends Fragment {

    private View rootView;
    private ImageView imagen_perfil;
    private final PerfilLogica perfilLogica =  new PerfilLogica();

    public PerfilFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            rootView = inflater.inflate(R.layout.fragment_perfil, container, false);
            return rootView;
        } catch (Exception e) {
            Log.e("Perfil: onCreateView", e.getMessage());
        }
        return null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (rootView != null) {
            super.onCreate(savedInstanceState);
            if (Twitter.getInstance().core.getSessionManager().getActiveSession() != null) {
                imagen_perfil = (ImageView) getActivity().findViewById(R.id.imagen_perfil);
                Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/.RadioUCAB/picBig.jpeg");
                imagen_perfil.setImageBitmap(perfilLogica.convertirImagenCirculo(bitmap));
            }
            else {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.addToBackStack("atras");
                InicioSesionTwitter inicioSesionTwitterFragment = new InicioSesionTwitter();
                ft.replace(((ViewGroup) getView().getParent()).getId(), inicioSesionTwitterFragment);
                ft.commit();
            }
        }
    }

}

