package info.androidhive.radioucab.Controlador;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import info.androidhive.radioucab.Logica.LocutorProgramaLogica;
import info.androidhive.radioucab.Logica.ManejoActivity;
import info.androidhive.radioucab.Logica.ManejoDialogs;
import info.androidhive.radioucab.Logica.ManejoEnvioTweet;
import info.androidhive.radioucab.Logica.ManejoString;
import info.androidhive.radioucab.Logica.ManejoToast;
import info.androidhive.radioucab.Logica.ParrillaLogica;
import info.androidhive.radioucab.Logica.RespuestaProgramaAsyncTask;
import info.androidhive.radioucab.Model.Comentario;
import info.androidhive.radioucab.Model.LocutorParrilla;
import info.androidhive.radioucab.Model.Parrilla;
import info.androidhive.radioucab.R;

public class EditarTweetProgramaFragment extends Fragment implements RespuestaProgramaAsyncTask {

    private final ManejoString manejoString = new ManejoString();
    private final ManejoActivity manejoActivity = ManejoActivity.getInstancia();
    private Button botonEnviar;
    private EditText editTextComentario;
    private String array_spinner[];
    private Parrilla programa;
    private final ParrillaLogica parrillaLogica = new ParrillaLogica();
    private final ManejoToast manejoToast = ManejoToast.getInstancia();
    private final LocutorProgramaLogica logicaLocutor = new LocutorProgramaLogica();
    private ProgressBar progressBar;
    private Spinner spinner;
    private TextView textoEnviarA;
    private TextView encabezadoNombrePrograma;
    private ManejoDialogs manejoDialogs;
    private List <LocutorParrilla> locutores;

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
        super.onCreate(savedInstanceState);
        manejoActivity.mostrarCloseToolbar();
        manejoActivity.editarActivity(6, false, null, "Editar tweet programa",false);
        botonEnviar = (Button) getActivity().findViewById(R.id.boton_enviar_solicitud);
        botonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publicarTweet();
            }
        });
        editTextComentario = (EditText) getActivity().findViewById(R.id.editText_tweet_programa);
        progressBar = (ProgressBar)getActivity().findViewById(R.id.progressBar_editar_programa);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.azul_radio_ucab), android.graphics.PorterDuff.Mode.MULTIPLY);
        spinner = (Spinner) getActivity().findViewById(R.id.spinner_destinatarios);
        textoEnviarA = (TextView) getActivity().findViewById(R.id.encabezado_enviar_a);
        encabezadoNombrePrograma = (TextView) getActivity().findViewById(R.id.texto_nombre_programa);
        mostrarElementos(true);
        parrillaLogica.delegate = this;
        parrillaLogica.getProgramaActual(getActivity(), 1);
    }

    public void mostrarElementos(boolean cargando){
        if (cargando){
            spinner.setVisibility(View.GONE);
            editTextComentario.setVisibility(View.GONE);
            textoEnviarA.setVisibility(View.GONE);
            encabezadoNombrePrograma.setText(getActivity().getString(R.string.campo_cargando));
            botonEnviar.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
        else {
            encabezadoNombrePrograma.setText(programa.getNombrePrograma());
            spinner.setVisibility(View.VISIBLE);
            editTextComentario.setVisibility(View.VISIBLE);
            textoEnviarA.setVisibility(View.VISIBLE);
            encabezadoNombrePrograma.setVisibility(View.VISIBLE);
            botonEnviar.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    public void cambiarSpinner(){
        mostrarElementos(false);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item, array_spinner);
        spinner.setAdapter(adapter);
    }

    public void publicarTweet () {
        if (manejoString.verificarEspacioNull(editTextComentario.getText().toString())) {
            final Comentario tweet = new Comentario(editTextComentario.getText().toString(), 7);
            tweet.setIdPrograma(programa.getIdPrograma());
            if (spinner.getSelectedItemId() != 0){
                LocutorParrilla locutor = locutores.get(Integer.parseInt(spinner.getSelectedItemId() + "") - 1);
                if (locutor != null) {
                    tweet.setIdLocutor(locutor.getMyId());
                    tweet.setTwitterLocutor(locutor.getUsuarioTwitter());
                }
            }
            final ManejoEnvioTweet manejoTwitter = new ManejoEnvioTweet(getActivity(), tweet);
            manejoTwitter.verificarTweet();
        }
        else {
            manejoToast.crearToast(getActivity(), getActivity().getString(R.string.toast_error_campos_obligatorios_comentario));
        }
    }

    @Override
    public void procesoExitoso(Parrilla programa) {
        if (programa != null){
            this.programa = programa;
            locutores = programa.getLocutores();
            if (locutores != null && locutores.size() > 0) {
                array_spinner = new String[locutores.size() + 1];
                array_spinner[0] = programa.getNombrePrograma();
                for (int i = 0; i < locutores.size(); i++) {
                    array_spinner[i + 1] = locutores.get(i).getNombreLocutor();
                }
            }
            else {
                array_spinner = new String[1];
                array_spinner[0] = programa.getNombrePrograma();
            }
            cambiarSpinner();
        }
        else {
            manejoDialogs = new ManejoDialogs(getResources().getString(R.string.dialogo_asunto_error),
                    getResources().getString(R.string.campo_error_sin_programa_actual), getResources().getString(R.string.dialogo_mensaje_Ok), getActivity());
            manejoDialogs.crearDialogo(true, false);
        }
    }

    @Override
    public void procesoNoExitoso() {
        manejoActivity.mostrarNombreInformacion(getActivity().getString(R.string.campo_error_sin_programa_actual));
        encabezadoNombrePrograma.setText(getActivity().getString(R.string.campo_error_sin_programa_actual));
        progressBar.setVisibility(View.GONE);
    }
}
