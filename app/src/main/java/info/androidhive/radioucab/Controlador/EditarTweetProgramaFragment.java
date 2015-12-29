package info.androidhive.radioucab.Controlador;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import info.androidhive.radioucab.Logica.ManejoActivity;
import info.androidhive.radioucab.Logica.ManejoEnvioTweet;
import info.androidhive.radioucab.Logica.ManejoString;
import info.androidhive.radioucab.Model.Comentario;
import info.androidhive.radioucab.R;

public class EditarTweetProgramaFragment extends Fragment {

    private final ManejoString manejoString = new ManejoString();
    private final ManejoActivity manejoActivity = ManejoActivity.getInstancia();
    private Button botonEnviar;
    private Toast toast;
    private EditText editTextComentario;
    private String array_spinner[];

    public EditarTweetProgramaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_editar_tweet_programa, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //cambio el color del toolbar superior
        array_spinner=new String[5];
        array_spinner[0]="option 1";
        array_spinner[1]="option 2";
        array_spinner[2]="option 3";
        array_spinner[3]="option 4";
        array_spinner[4]="option 5";
        manejoActivity.editarActivity(6, false, null);
        super.onCreate(savedInstanceState);
        botonEnviar = (Button) getActivity().findViewById(R.id.boton_enviar_solicitud);
        botonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publicarTweet();
            }
        });
        editTextComentario = (EditText) getActivity().findViewById(R.id.editText_tweet_programa);
        Spinner s = (Spinner) getActivity().findViewById(R.id.spinner_destinatarios);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item, array_spinner);
        s.setAdapter(adapter);
    }

    public void publicarTweet () {
        if (manejoString.verificarEspacioNull(editTextComentario.getText().toString())) {
            Comentario tweet = new Comentario(editTextComentario.getText().toString(), 2);
            final ManejoEnvioTweet manejoTwitter = new ManejoEnvioTweet(getActivity(), tweet);
            manejoTwitter.verificarTweet();
        }
        else {
            toast = Toast.makeText(getActivity(), getActivity().getString(R.string.toast_error_campos_obligatorios_comentario), Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
