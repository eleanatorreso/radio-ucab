package info.androidhive.radioucab;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import info.androidhive.radioucab.Logica.ManejoActivity;
import info.androidhive.radioucab.Logica.ManejoDialogs;
import info.androidhive.radioucab.Logica.ManejoEnvioTweet;
import info.androidhive.radioucab.Logica.ManejoString;
import info.androidhive.radioucab.Model.Comentario;

public class EditarTweetPremiacionFragment extends Fragment {

    private final ManejoString manejoString = new ManejoString();
    private final ManejoActivity manejoActivity = ManejoActivity.getInstancia();
    private Button botonEnviar;
    private Toast toast;
    private EditText editTextComentario;
    private CheckBox terminosCondiciones;
    private ManejoDialogs dialogoTerminosCondiciones;

    public EditarTweetPremiacionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_editar_tweet_premiacion, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //cambio el color del toolbar superior
        manejoActivity.editarActivity(6, false, null);
        super.onCreate(savedInstanceState);
        botonEnviar = (Button) getActivity().findViewById(R.id.boton_participar_premiacion);
        botonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publicarTweet();
            }
        });
        editTextComentario = (EditText) getActivity().findViewById(R.id.editText_tweet_premiacion);
        terminosCondiciones = (CheckBox) getActivity().findViewById(R.id.checkbox_terminos_condiciones_concurso);
        terminosCondiciones.setText(Html.fromHtml("<u>" + getActivity().getString(R.string.campo_terminos_condiciones_concurso)
                + "</u>"));
        dialogoTerminosCondiciones = new ManejoDialogs(getActivity().getString(R.string.dialogo_asunto_terminos_condiciones_concurso),
                "terminos traidos de la bd",
                getActivity().getString(R.string.dialogo_mensaje_cerrar),getActivity());
        terminosCondiciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogoTerminosCondiciones.crearDialogo();
            }
        });
    }

    public void publicarTweet () {
        if (manejoString.verificarEspacioNull(editTextComentario.getText().toString()) &&
                terminosCondiciones.isChecked()) {
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