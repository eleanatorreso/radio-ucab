package info.androidhive.radioucab.Controlador;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import info.androidhive.radioucab.Logica.UsuarioLogica;
import info.androidhive.radioucab.Model.Usuario;
import info.androidhive.radioucab.R;

public class CreacionUsuario extends Fragment {

    public Usuario usuario;
    private View rootView;
    private EditText nombreUsuario;
    private EditText apellidoUsuario;
    private EditText correoUsuario;
    private CheckBox terminosCondiciones;
    public CreacionUsuario() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            rootView = inflater.inflate(R.layout.fragment_creacion_usuario, container, false);
            return rootView;
        } catch (Exception e) {
            Log.e("CreacionU: onCreateView", e.getMessage());
        }
        return null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (rootView != null) {
            super.onCreate(savedInstanceState);
            if (usuario != null) {
                nombreUsuario = (EditText) getActivity().findViewById(R.id.editText_nombre_usuario);
                nombreUsuario.setText(usuario.getNombre());
                apellidoUsuario = (EditText) getActivity().findViewById(R.id.editText_apellido_usuario);
                correoUsuario = (EditText) getActivity().findViewById(R.id.editText_correo_usuario);
                correoUsuario.setText(usuario.getCorreo());
                terminosCondiciones = (CheckBox) getActivity().findViewById(R.id.checkbox_terminosCondiciones);
                Button crearUsuario = (Button) getActivity().findViewById(R.id.boton_crearUsuario);
            }
        }
    }

    public void crearUsuario () {
        if (terminosCondiciones.isChecked()) {
            UsuarioLogica usuarioNuevo = new UsuarioLogica();
            usuarioNuevo.usuario = usuario;
            usuarioNuevo.almacenarUsuario();
        }
    }

}
