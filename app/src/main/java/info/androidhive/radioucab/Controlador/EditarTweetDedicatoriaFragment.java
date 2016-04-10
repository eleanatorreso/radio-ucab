package info.androidhive.radioucab.Controlador;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import info.androidhive.radioucab.Logica.ManejoActivity;
import info.androidhive.radioucab.Logica.ManejoEnvioTweet;
import info.androidhive.radioucab.Logica.ManejoString;
import info.androidhive.radioucab.Logica.ManejoToast;
import info.androidhive.radioucab.Model.Comentario;
import info.androidhive.radioucab.R;

public class EditarTweetDedicatoriaFragment extends Fragment {

    private final ManejoString manejoString = new ManejoString();
    private final ManejoActivity manejoActivity = ManejoActivity.getInstancia();
    private Button botonEnviar;
    private EditText editTextCancion;
    private EditText editTextArtista;
    private EditText editTextUsuario;
    private final ManejoToast manejoToast = ManejoToast.getInstancia();
    private ManejoEnvioTweet manejoTwitter;

    public EditarTweetDedicatoriaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_editar_tweet_dedicatoria, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //cambio el color del toolbar superior
        manejoActivity.mostrarCloseToolbar();
        manejoActivity.editarActivity(6, false, null, "Editar tweet dedicatoria de canción",false);
        super.onCreate(savedInstanceState);
        editTextCancion = (EditText) getActivity().findViewById(R.id.editText_cancion);
        editTextArtista = (EditText) getActivity().findViewById(R.id.editText_artista);
        editTextUsuario = (EditText) getActivity().findViewById(R.id.editText_usuario_destinatario);
        editTextUsuario.setText("@");
        botonEnviar = (Button) getActivity().findViewById(R.id.boton_enviar_solicitud);
        botonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publicarTweet();
            }
        });
    }

    public void publicarTweet() {
        if (manejoString.verificarEspacioNull(editTextCancion.getText().toString()) == true &&
                manejoString.verificarEspacioNull(editTextArtista.getText().toString()) == true &&
                manejoString.verificarEspacioNull(editTextUsuario.getText().toString()) == true) {
            final String comentario = "Quiero dedicar " + editTextCancion.getText().toString() + " - " + editTextArtista.getText().toString()
                    + " a " + editTextUsuario.getText().toString();
            Comentario tweet = new Comentario(comentario, 2, editTextArtista.getText().toString(), editTextCancion.getText().toString(),
                    editTextUsuario.getText().toString());
            manejoTwitter = new ManejoEnvioTweet(getActivity(), tweet);
            manejoTwitter.verificarTweet();
        } else {
            manejoToast.crearToast(getActivity(), getActivity().getString(R.string.toast_error_campos_obligatorios_comentario));
        }
    }
}
