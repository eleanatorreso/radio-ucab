package info.androidhive.radioucab.Controlador;

import info.androidhive.radioucab.Logica.ManejoActivity;
import info.androidhive.radioucab.Logica.PerfilLogica;
import info.androidhive.radioucab.Model.Usuario;
import info.androidhive.radioucab.R;

import android.app.Fragment;
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
    private ImageView imagen_mis_concursos;
    private ImageView imagen_mis_noticias;
    private ImageView imagen_mis_programas;
    private ImageView imagen_ayuda;
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
                + usuario_actual.getFormato_imagen());
        imagen_perfil.setImageBitmap(perfilLogica.convertirImagenCirculo(bitmap, 0));
        usuario_nombre = (TextView) getActivity().findViewById(R.id.texto_usuario_nombre);
        usuario_nombre.setText(usuario_actual.getNombre() + " " + usuario_actual.getApellido());
        usuario_twitter = (TextView) getActivity().findViewById(R.id.texto_usuario_twitter);
        usuario_twitter.setText("@" + usuario_actual.getUsuario_twitter());
        usuario_correo = (TextView) getActivity().findViewById(R.id.texto_usuario_correo);
        usuario_correo.setText(usuario_actual.getCorreo());
        boton_editar_perfil = (Button) getActivity().findViewById(R.id.boton_editar_perfil);
        boton_editar_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manejoActivity.cambiarFragment("EditarPerfil", true, true);
            }
        });
        imagen_mis_concursos = (ImageView) getActivity().findViewById(R.id.imagen_premiacion);
        imagen_mis_concursos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manejoActivity.cambiarFragment("MisConcursosFragment", true, false);
            }
        });
        imagen_mis_noticias = (ImageView) getActivity().findViewById(R.id.imagen_preferencia_noticias);
        imagen_mis_noticias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manejoActivity.cambiarFragment("MisNoticiasFragment", true, false);
            }
        });
        imagen_mis_programas = (ImageView) getActivity().findViewById(R.id.imagen_programas_favoritos);
        imagen_mis_programas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manejoActivity.cambiarFragment("MisProgramasFragment", true, false);
            }
        });
        imagen_ayuda = (ImageView) getActivity().findViewById(R.id.icono_ayuda);
        imagen_ayuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manejoActivity.cambiarFragment("AyudaFragment", true, false);
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
            manejoActivity.cambiarFragment("Inicio",false, false);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (rootView != null) {
            //cambio el color del toolbar superior
            manejoActivity.editarActivity(6, false, "Perfil", "Perfil",true);
            manejoActivity.ocultarBackToolbar();
            super.onCreate(savedInstanceState);
            actualizarPerfil();
        }
    }

}

