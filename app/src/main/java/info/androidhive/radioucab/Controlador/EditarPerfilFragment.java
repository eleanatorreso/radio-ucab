package info.androidhive.radioucab.Controlador;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import info.androidhive.radioucab.Logica.ManejoActivity;
import info.androidhive.radioucab.Logica.ManejoDialogs;
import info.androidhive.radioucab.Logica.ManejoString;
import info.androidhive.radioucab.Logica.UsuarioLogica;
import info.androidhive.radioucab.Model.Usuario;
import info.androidhive.radioucab.R;

public class EditarPerfilFragment extends Fragment {

    private Usuario usuario;
    private View rootView;
    private EditText nombreUsuario;
    private EditText apellidoUsuario;
    private EditText correoUsuario;
    private Button guardarModificacion;
    private Button cancelar;
    private ManejoDialogs cancelarRegistroUsuario;
    private Toast toast;
    private ManejoString manejoString = new ManejoString();
    private final ManejoActivity manejoActivity = ManejoActivity.getInstancia();

    public EditarPerfilFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            rootView = inflater.inflate(R.layout.fragment_editar_perfil, container, false);
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
            if (!Usuario.listAll(Usuario.class).isEmpty()) {
                usuario = Usuario.listAll(Usuario.class).get(0);
                manejoActivity.editarActivity(6, false);
                nombreUsuario = (EditText) getActivity().findViewById(R.id.editText_nombre_usuario);
                nombreUsuario.setText(usuario.getNombre());
                apellidoUsuario = (EditText) getActivity().findViewById(R.id.editText_apellido_usuario);
                apellidoUsuario.setText(usuario.getApellido());
                correoUsuario = (EditText) getActivity().findViewById(R.id.editText_correo_usuario);
                correoUsuario.setText(usuario.getCorreo());
                guardarModificacion = (Button) getActivity().findViewById(R.id.boton_crear_usuario);
                guardarModificacion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        modificarUsuario();
                    }
                });
                cancelar = (Button) getActivity().findViewById(R.id.boton_cancelar_registro);
                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelarRegistro();
                    }
                });
            }
        }
    }

    public void modificarUsuario() {
        if (manejoString.verificarEspacioNull(nombreUsuario.getText().toString()) == true
                && manejoString.verificarEspacioNull(apellidoUsuario.getText().toString()) == true
                && manejoString.verificarEspacioNull(correoUsuario.getText().toString()) == true) {
            UsuarioLogica usuarioNuevo = new UsuarioLogica();
            usuario.setNombre(nombreUsuario.getText().toString().trim());
            usuario.setApellido(apellidoUsuario.getText().toString().trim());
            usuario.setCorreo(correoUsuario.getText().toString().trim());
            usuarioNuevo.usuario = usuario;
            usuarioNuevo.contexto = getActivity();
            usuarioNuevo.almacenarUsuario(true, false);
        }
        else {
            toast = Toast.makeText(getActivity(), getActivity().getString(R.string.toast_error_campos_obligatorios), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void cancelarRegistro() {
        crearDialogoSiYNo(getActivity().getString(R.string.dialogo_asunto_cancelar_edicion),
                getActivity().getString(R.string.dialogo_contenido_cancelar_edicion),
                getActivity().getString(R.string.dialogo_mensaje_Si),
                getActivity().getString(R.string.dialogo_mensaje_No));
    }

    public void cambiarPerfil() {
        manejoActivity.cambiarFragment("Perfil");
    }

    public void crearDialogoSiYNo(String titulo, String mensaje, String botonPositivo, String botonNegativo) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(titulo);
            builder.setMessage(mensaje);
            builder.setPositiveButton(botonPositivo,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            cambiarPerfil();
                        }
                    });
            builder.setNegativeButton(botonNegativo,
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

}
