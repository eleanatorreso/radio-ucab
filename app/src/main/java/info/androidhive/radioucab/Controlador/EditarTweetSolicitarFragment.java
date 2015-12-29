package info.androidhive.radioucab.Controlador;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import info.androidhive.radioucab.Logica.ManejoActivity;
import info.androidhive.radioucab.Logica.ManejoEnvioTweet;
import info.androidhive.radioucab.Logica.ManejoString;
import info.androidhive.radioucab.Model.Comentario;
import info.androidhive.radioucab.R;

public class EditarTweetSolicitarFragment extends Fragment {

    private final ManejoString manejoString = new ManejoString();
    private final ManejoActivity manejoActivity = ManejoActivity.getInstancia();
    private Button botonEnviar;
    private EditText editTextCancion;
    private EditText editTextArtista;
    private Toast toast;

    public EditarTweetSolicitarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_editar_tweet_solicitar, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //cambio el color del toolbar superior
        manejoActivity.editarActivity(6, false, null);
        super.onCreate(savedInstanceState);
        editTextCancion = (EditText) getActivity().findViewById(R.id.editText_cancion);
        editTextArtista = (EditText) getActivity().findViewById(R.id.editText_artista);
        botonEnviar = (Button) getActivity().findViewById(R.id.boton_enviar_solicitud);
        botonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publicarTweet();
            }
        });
    }

    public void publicarTweet () {
        if (manejoString.verificarEspacioNull(editTextCancion.getText().toString()) == true &&
                manejoString.verificarEspacioNull(editTextArtista.getText().toString()) == true) {
            Comentario tweet = new Comentario(editTextCancion.getText().toString(), 1);
            final ManejoEnvioTweet manejoTwitter = new ManejoEnvioTweet(getActivity(), tweet);
            manejoTwitter.verificarTweet();
        }
        else {
            toast = Toast.makeText(getActivity(), getActivity().getString(R.string.toast_error_campos_obligatorios_solicitud), Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
