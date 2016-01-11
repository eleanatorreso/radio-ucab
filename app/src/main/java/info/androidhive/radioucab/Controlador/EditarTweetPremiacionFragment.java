package info.androidhive.radioucab.Controlador;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import info.androidhive.radioucab.Logica.ManejoActivity;
import info.androidhive.radioucab.Logica.ManejoDialogs;
import info.androidhive.radioucab.Logica.ManejoEnvioTweet;
import info.androidhive.radioucab.Logica.ManejoString;
import info.androidhive.radioucab.Model.Comentario;
import info.androidhive.radioucab.Model.Concurso;
import info.androidhive.radioucab.R;

public class EditarTweetPremiacionFragment extends Fragment {

    private final ManejoString manejoString = new ManejoString();
    private final ManejoActivity manejoActivity = ManejoActivity.getInstancia();
    private Button botonEnviar;
    private TextView nombrePremiacion;
    private TextView descripcionPremiacion;
    private TextView editTextTweet;
    private Toast toast;
    public Concurso concurso;
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
        nombrePremiacion = (TextView) getActivity().findViewById(R.id.texto_nombre_premiacion);
        nombrePremiacion.setText(concurso.getNombre());
        descripcionPremiacion = (TextView) getActivity().findViewById(R.id.texto_descripcion_premiacion);
        descripcionPremiacion.setText(concurso.getDescripcion());
        editTextTweet = (TextView) getActivity().findViewById(R.id.editText_tweet_premiacion);
        terminosCondiciones = (CheckBox) getActivity().findViewById(R.id.checkbox_terminos_condiciones_concurso);
        terminosCondiciones.setText(Html.fromHtml("<u>" + getActivity().getString(R.string.campo_terminos_condiciones)
                + "</u>"));
        dialogoTerminosCondiciones = new ManejoDialogs(getActivity().getString(R.string.dialogo_asunto_terminos_condiciones),
                concurso.getTerminos_condiciones(),
                getActivity().getString(R.string.dialogo_mensaje_cerrar), getActivity());
        terminosCondiciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogoTerminosCondiciones.crearDialogo();
            }
        });
        botonEnviar = (Button) getActivity().findViewById(R.id.boton_participar_premiacion);
        botonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publicarTweet();
            }
        });
    }

    public void publicarTweet () {
        if (terminosCondiciones.isChecked() && manejoString.verificarEspacioNull(editTextTweet.getText().toString()) == true) {
            Comentario tweet = new Comentario(editTextTweet.getText().toString(), 1);
            tweet.setIdConcurso(concurso.getMyId());
            final ManejoEnvioTweet manejoTwitter = new ManejoEnvioTweet(getActivity(), tweet);
            manejoTwitter.verificarTweet();
        }
        else if (!terminosCondiciones.isChecked()){
            toast = Toast.makeText(getActivity(), getActivity().getString(R.string.toast_error_terminos_condiciones_concurso), Toast.LENGTH_LONG);
            toast.show();
        }
        else {
            toast = Toast.makeText(getActivity(), getActivity().getString(R.string.toast_error_campos_obligatorios_comentario), Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
