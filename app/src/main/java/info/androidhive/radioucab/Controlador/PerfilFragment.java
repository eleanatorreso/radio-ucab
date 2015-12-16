package info.androidhive.radioucab.Controlador;

import info.androidhive.radioucab.Logica.ManejoActivity;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PerfilFragment extends Fragment {

    private View rootView;
    private ImageView imagen_perfil;
    private TextView usuario_nombre;
    private TextView usuario_twitter;
    private TextView usuario_correo;
    private Button boton_editar_perfil;
    private final PerfilLogica perfilLogica = new PerfilLogica();
    private Usuario usuario_actual;
    private final ManejoActivity manejoActivity = ManejoActivity.getInstancia();

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
                + usuario_actual.getFormatoImagen());
        imagen_perfil.setImageBitmap(perfilLogica.convertirImagenCirculo(bitmap, 0));
        usuario_nombre = (TextView) getActivity().findViewById(R.id.texto_usuario_nombre);
        usuario_nombre.setText(usuario_actual.getNombre() + " " + usuario_actual.getApellido());
        usuario_twitter = (TextView) getActivity().findViewById(R.id.texto_usuario_twitter);
      //usuario_twitter.setText(getActivity().getString(R.string.campo_usuario_twitter) + ": @" + usuario_actual.getUsuario_twitter());
        usuario_twitter.setText("@" + usuario_actual.getUsuario_twitter());
        usuario_correo = (TextView) getActivity().findViewById(R.id.texto_usuario_correo);
      //  usuario_correo.setText(getActivity().getString(R.string.campo_usuario_correo) + ": " + usuario_actual.getCorreo());
        usuario_correo.setText(usuario_actual.getCorreo());
        boton_editar_perfil = (Button) getActivity().findViewById(R.id.boton_editar_perfil);
        boton_editar_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manejoActivity.cambiarFragment("Editar");
            }
        });
    }

    public void actualizarPerfil () {
        perfilLogica.setContexto(this.getActivity());
        if (!Usuario.listAll(Usuario.class).isEmpty()) {
            usuario_actual = Usuario.listAll(Usuario.class).get(0);
            cargarDatosUsuario();
            manejoActivity.cambiarToolbar();
        }
        else {
            manejoActivity.cambiarFragment("Inicio");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (rootView != null) {
            //cambio el color del toolbar superior
            manejoActivity.editarActivity(6, false);
            super.onCreate(savedInstanceState);
            actualizarPerfil();
        }
    }

}

