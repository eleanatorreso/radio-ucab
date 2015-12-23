package info.androidhive.radioucab.Controlador;


import android.os.Bundle;
import android.app.Fragment;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class EditarTweetComentarioFragment extends Fragment {

    private final ManejoString manejoString = new ManejoString();
    private final ManejoActivity manejoActivity = ManejoActivity.getInstancia();
    private RadioGroup grupo_radio_button;
    private RadioButton radio_comentario;
    private RadioButton radio_queja;
    private RadioButton radio_sugerencia;
    private Button boton_siguiente;
    private EditText editTextComentario;
    private Toast toast;

    public EditarTweetComentarioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_editar_tweet_comentario, container, false);
    }

    private int getFinalidadTweet() {
        int radioSeleccionado = grupo_radio_button.getCheckedRadioButtonId();
        if (radioSeleccionado == radio_comentario.getId())
            return 3;
        else if (radioSeleccionado == radio_sugerencia.getId())
            return 4;
        else
            return 5;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //cambio el color del toolbar superior
        manejoActivity.editarActivity(6, false, null);
        super.onCreate(savedInstanceState);
        editTextComentario = (EditText) getActivity().findViewById(R.id.editText_tweet_comentario);
        grupo_radio_button = (RadioGroup) getActivity().findViewById(R.id.grupo_radio_button_finalidad);
        grupo_radio_button.check(R.id.radiobutton_comentario);
        radio_comentario = (RadioButton) getActivity().findViewById(R.id.radiobutton_comentario);
        radio_queja = (RadioButton) getActivity().findViewById(R.id.radiobutton_queja);
        radio_sugerencia = (RadioButton) getActivity().findViewById(R.id.radiobutton_sugerencia);
        boton_siguiente = (Button) getActivity().findViewById(R.id.boton_siguiente_comentario);
        boton_siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publicarTweet();
            }
        });
    }

    public void publicarTweet () {
        if (manejoString.verificarEspacioNull(editTextComentario.getText().toString()) == true) {
            Comentario tweet = new Comentario(editTextComentario.getText().toString(), getFinalidadTweet());
            final ManejoEnvioTweet manejoTwitter = new ManejoEnvioTweet(getActivity(), tweet);
            manejoTwitter.verificarTweet();
        }
        else {
            toast = Toast.makeText(getActivity(), "Debe escribir su comentario", Toast.LENGTH_LONG);
            toast.show();
        }
    }

}
