package info.androidhive.radioucab.Controlador;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.radioucab.Logica.ManejoActivity;
import info.androidhive.radioucab.Logica.ManejoConcurso;
import info.androidhive.radioucab.Logica.ManejoProgressDialog;
import info.androidhive.radioucab.Logica.RespuestaLogica;
import info.androidhive.radioucab.Model.Concurso;
import info.androidhive.radioucab.R;

public class ConcursoFragment extends Fragment implements RespuestaLogica {

    private ManejoConcurso manejoConcurso;
    private ManejoProgressDialog manejoProgressDialog = ManejoProgressDialog.getInstancia();
    private RadioGroup radioGroupConcursos;
    private LinearLayout layoutRadioButton;
    private TextView encabezado;
    private ImageView iconoSinConcurso;
    private TextView textoSinConcurso;
    private TextView textoConConcurso;
    private Button botonSiguiente;
    private List<Integer> listaIdRadioButton = new ArrayList<Integer>();
    private ManejoActivity manejoActivity = ManejoActivity.getInstancia();
    private List<Concurso> concursosActuales;

    public ConcursoFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        manejoActivity.registrarPantallaAnalytics("Concursos");
        return inflater.inflate(R.layout.fragment_concurso, container, false);
    }

    public void comprobarConcurso(){
        manejoProgressDialog.crearProgressDialog(getActivity());
        manejoConcurso = new ManejoConcurso(getActivity(), this);
        manejoConcurso.comprobarConcursosActuales();
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        radioGroupConcursos = (RadioGroup)getActivity().findViewById(R.id.grupo_radio_button_concursos);
        layoutRadioButton = (LinearLayout)getActivity().findViewById(R.id.layout_radio_buttons_concursos);
        encabezado = (TextView)getActivity().findViewById(R.id.texto_encabezado_concurso);
        iconoSinConcurso = (ImageView)getActivity().findViewById(R.id.imagen_sin_concursos);
        textoSinConcurso = (TextView)getActivity().findViewById(R.id.texto_sin_concursos);
        textoConConcurso = (TextView)getActivity().findViewById(R.id.texto_con_concursos);
        botonSiguiente = (Button) getActivity().findViewById(R.id.boton_siguiente);
        botonSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                participarConcurso();
            }
        });
        comprobarConcurso();
    }

    private void cargarConcursosActuales(List<Concurso> concursosActuales){
        RadioButton radioButton;
        int primero = 1;
        listaIdRadioButton = new ArrayList<Integer>();
        radioGroupConcursos.setVisibility(View.VISIBLE);
        for (Concurso concurso : concursosActuales) {
            radioButton = new RadioButton(getActivity());
            radioButton.setText(concurso.getNombre());
            radioGroupConcursos.addView(radioButton);
            if (primero == 1){
                primero = 0;
                radioGroupConcursos.check(radioButton.getId());
            }
            listaIdRadioButton.add(radioButton.getId());
        }
        encabezado.setText(getActivity().getResources().getString(R.string.campo_encabezado_concurso));
        encabezado.setVisibility(View.VISIBLE);
        textoConConcurso.setVisibility(View.VISIBLE);
        botonSiguiente.setVisibility(View.VISIBLE);
    }

    private void procesarConcursos(){
        concursosActuales = Concurso.listAll(Concurso.class);
        if (concursosActuales != null && concursosActuales.size() > 0) {
            cargarConcursosActuales(concursosActuales);
        }
        else {
            encabezado.setText(getActivity().getResources().getString(R.string.campo_encabezado_sin_concursos));
            encabezado.setVisibility(View.VISIBLE);
            iconoSinConcurso.setVisibility(View.VISIBLE);
            textoSinConcurso.setVisibility(View.VISIBLE);
        }
        manejoProgressDialog.cancelarProgressDialog();
    }

    public Concurso getConcursoSeleccionado(){
        int idSeleccionado = radioGroupConcursos.getCheckedRadioButtonId();
        for(int i = 0; i < listaIdRadioButton.size(); i++){
            if (listaIdRadioButton.get(i) == idSeleccionado)
                return concursosActuales.get(i);
        }
        return null;
    }

    public void participarConcurso(){
        EditarTweetPremiacionFragment fragment = (EditarTweetPremiacionFragment)manejoActivity.cambiarFragment("TweetConcurso", true);
        Concurso concursoActual = getConcursoSeleccionado();
        //fragment.concurso = concursoActual;
    }

    @Override
    public void procesoExitoso() {
        procesarConcursos();
    }

    @Override
    public void procesoNoExitoso() {
        manejoProgressDialog.cancelarProgressDialog();
        Toast toast = Toast.makeText(getActivity(), getActivity().getString(R.string.toast_error_general), Toast.LENGTH_LONG);
        toast.show();
    }
}
