package info.androidhive.radioucab.Controlador;

import info.androidhive.radioucab.Logica.ManejoToolbar;
import info.androidhive.radioucab.Logica.PerfilLogica;
import info.androidhive.radioucab.Model.Usuario;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

public class PerfilFragment extends Fragment {

    private View rootView;
    private ImageView imagen_perfil;
    private TextView nombre_usuario;
    private TextView usuario_twitter;
    private final PerfilLogica perfilLogica = new PerfilLogica();
    private Usuario usuarioActual;
    private final ManejoToolbar toolbar = ManejoToolbar.getInstancia();

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

    public void cargarDatosUsuario() {
        imagen_perfil = (ImageView) getActivity().findViewById(R.id.imagen_perfil);
        Bitmap bitmap = BitmapFactory.decodeFile(getActivity().getString(R.string.ruta_archivos_radio_ucab) + "picBig."
                + usuarioActual.getFormatoImagen());
        imagen_perfil.setImageBitmap(perfilLogica.convertirImagenCirculo(bitmap,0));
        nombre_usuario = (TextView) getActivity().findViewById(R.id.texto_nombre_usuario);
        nombre_usuario.setText(usuarioActual.getNombre() + " " + usuarioActual.getApellido());
        usuario_twitter = (TextView) getActivity().findViewById(R.id.texto_usuario_twitter);
        usuario_twitter.setText("@" + usuarioActual.getUsuario_twitter());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (rootView != null) {
            //cambio el color del toolbar superior
            toolbar.cambiarDeColor(6);
            super.onCreate(savedInstanceState);
            perfilLogica.setContexto(this.getActivity());
            if (!Usuario.listAll(Usuario.class).isEmpty()) {
                usuarioActual = Usuario.listAll(Usuario.class).get(0);
                cargarDatosUsuario();
            }
            else {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.addToBackStack("atras");
                InicioSesionTwitterFragment inicioSesionTwitterFragmentFragment = new InicioSesionTwitterFragment();
                ft.replace(((ViewGroup) getView().getParent()).getId(), inicioSesionTwitterFragmentFragment);
                ft.commit();
            }
        }
    }

}

