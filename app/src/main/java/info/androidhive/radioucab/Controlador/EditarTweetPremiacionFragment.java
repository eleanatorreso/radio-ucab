package info.androidhive.radioucab.Controlador;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.radioucab.Logica.ManejoActivity;
import info.androidhive.radioucab.Logica.ManejoDialogs;
import info.androidhive.radioucab.Logica.ManejoEnvioTweet;
import info.androidhive.radioucab.Logica.ManejoString;
import info.androidhive.radioucab.Logica.ManejoToast;
import info.androidhive.radioucab.Model.Comentario;
import info.androidhive.radioucab.Model.Concurso;
import info.androidhive.radioucab.Model.Premio;
import info.androidhive.radioucab.R;

public class EditarTweetPremiacionFragment extends Fragment {

    private final ManejoString manejoString = new ManejoString();
    private final ManejoActivity manejoActivity = ManejoActivity.getInstancia();
    private Button botonEnviar;
    private TextView nombreConcurso;
    private TextView descripcionConcurso;
    private EditText editTextComentario;
    private CheckBox terminosCondiciones;
    private ManejoDialogs dialogoTerminosCondiciones;
    private final ManejoToast manejoToast = ManejoToast.getInstancia();
    private ManejoEnvioTweet manejoTwitter;
    public Concurso concurso;
    private List<String> posicion;
    private List<String> premio;
    private ListView listaPosicion;
    private ListView listaPremios;
    private View rootView;
    private Button botonVerPremios;

    public EditarTweetPremiacionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try {
            rootView = inflater.inflate(R.layout.fragment_editar_tweet_premiacion, container, false);
            return rootView;
        } catch (Exception e) {
            Log.e("Parrilla: onCreateView", e.getMessage());
        }
        return null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //cambio el color del toolbar superior
        manejoActivity.editarActivity(6, false, null, "Editar tweet premiación", false);
        manejoActivity.mostrarCloseToolbar();
        super.onCreate(savedInstanceState);
        botonEnviar = (Button) getActivity().findViewById(R.id.boton_participar_premiacion);
        botonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publicarTweet();
            }
        });
        botonVerPremios = (Button) getActivity().findViewById(R.id.boton_ver_premios);
        botonVerPremios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDialogoPremios();
            }
        });
        nombreConcurso = (TextView)getActivity().findViewById(R.id.texto_nombre_premiacion);
        nombreConcurso.setText(concurso.getNombre());
        descripcionConcurso = (TextView)getActivity().findViewById(R.id.texto_nombre_premiacion);
        descripcionConcurso.setText(concurso.getDescripcion());
        editTextComentario = (EditText) getActivity().findViewById(R.id.editText_tweet_premiacion);
        terminosCondiciones = (CheckBox) getActivity().findViewById(R.id.checkbox_terminos_condiciones_concurso);
        terminosCondiciones.setText(Html.fromHtml("<u>" + getActivity().getString(R.string.campo_terminos_condiciones_concurso)
                + "</u>"));
        dialogoTerminosCondiciones = new ManejoDialogs(getActivity().getString(R.string.dialogo_asunto_terminos_condiciones_concurso),
                concurso.getTerminos_condiciones(),
                getActivity().getString(R.string.dialogo_mensaje_cerrar), getActivity());
        terminosCondiciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogoTerminosCondiciones.crearDialogo(false, true);
            }
        });
    }

    public void abrirDialogoPremios() {
        final AlertDialog.Builder dialogo = new AlertDialog.Builder(getActivity());
        final AlertDialog alerta_dialogo;
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialogo_premios, null);
        dialogo.setView(dialogView);
        dialogo.setCancelable(true);
        alerta_dialogo = dialogo.create(); //returns an AlertDialog from a Builder.
        alerta_dialogo.show();
        final Button boton_cerrar = (Button) dialogView.findViewById(R.id.boton_cerrar_premios);
        boton_cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerta_dialogo.dismiss();
            }
        });
        listaPosicion = (ListView) dialogView.findViewById(R.id.lista_posicion);
        listaPremios = (ListView) dialogView.findViewById(R.id.lista_premios);
        cargarPremios();
    }

    public void cargarPremios() {
        posicion = new ArrayList<String>();
        premio = new ArrayList<String>();
        List<Premio> premios = concurso.getPremios();
        for (Premio item : premios) {
            posicion.add(item.getPosicion() + "");
            premio.add(item.getNombre());
        }
        ArrayAdapter<String> adapterPosicion = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, posicion);
        listaPosicion.setAdapter(adapterPosicion);
        ArrayAdapter<String> adapterPremio = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, premio);
        listaPremios.setAdapter(adapterPremio);
    }

    public void publicarTweet() {
        if (manejoString.verificarEspacioNull(editTextComentario.getText().toString()) &&
                terminosCondiciones.isChecked()) {
            final Comentario tweet = new Comentario(editTextComentario.getText().toString(), 1);
            tweet.setIdConcurso(concurso.getMyId());
            manejoTwitter = new ManejoEnvioTweet(getActivity(), tweet);
            manejoTwitter.verificarTweet();
        } else {
            manejoToast.crearToast(getActivity(), getActivity().getString(R.string.toast_error_campos_obligatorios_comentario));
        }
    }
}
