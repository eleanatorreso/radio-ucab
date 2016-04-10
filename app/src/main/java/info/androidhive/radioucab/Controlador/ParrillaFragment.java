package info.androidhive.radioucab.Controlador;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import info.androidhive.radioucab.Conexiones.conexionGETAPIJSONArray;
import info.androidhive.radioucab.Conexiones.conexionGETAPIString;
import info.androidhive.radioucab.Logica.ActualizacionLogica;
import info.androidhive.radioucab.Logica.ManejoActivity;
import info.androidhive.radioucab.Logica.ManejoFecha;
import info.androidhive.radioucab.Logica.ManejoProgressDialog;
import info.androidhive.radioucab.Logica.ManejoToast;
import info.androidhive.radioucab.Logica.ParrillaLogica;
import info.androidhive.radioucab.Logica.RespuestaAsyncTask;
import info.androidhive.radioucab.Model.Actualizacion;
import info.androidhive.radioucab.Model.Parrilla;
import info.androidhive.radioucab.R;

public class ParrillaFragment extends Fragment implements RespuestaAsyncTask {

    private View rootView;
    private ListView listaHora;
    private ListView listaPrograma;
    private conexionGETAPIJSONArray conexion;
    private List<String> horas;
    private List<String> programas;
    static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
    private final ManejoActivity manejoActivity = ManejoActivity.getInstancia();
    private final ParrillaLogica parrillaLogica = new ParrillaLogica();
    private final ActualizacionLogica actualizacionLogica = new ActualizacionLogica();
    private ManejoProgressDialog manejoProgressDialog = ManejoProgressDialog.getInstancia();
    private final ManejoToast manejoToast = ManejoToast.getInstancia();
    private conexionGETAPIString conexionString;
    private static Date ultimaActWS;
    private static final ManejoFecha tiempoActual = new ManejoFecha();
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView textoDiaActualizacion;

    public ParrillaFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            rootView = inflater.inflate(R.layout.fragment_parrilla, container, false);
            return rootView;
        } catch (Exception e) {
            Log.e("Parrilla: onCreateView", e.getMessage());
        }
        return null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //cambio el color del toolbar superior
        manejoActivity.editarActivity(2, true, "Parrilla", "Parrilla",true);
        listaHora = (ListView) rootView.findViewById(R.id.lista_hora);
        listaPrograma = (ListView) rootView.findViewById(R.id.lista_programa);
        textoDiaActualizacion = (TextView) getActivity().findViewById(R.id.texto_dia_actualizacion_parrilla);
        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh_parrilla);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                comprobarUltimaActualizacion();
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.amarillo_ucab, R.color.azul_radio_ucab);
        comprobarActualizacionParrilla();
    }

    public void comprobarActualizacionParrilla() {
        List<Parrilla> parrillaProgramacion = Parrilla.listAll(Parrilla.class);
        cargarParrillaActualizada();
        if (!parrillaLogica.comprobarActualizacionParrilla() || parrillaProgramacion.size() == 0) {
            comprobarUltimaActualizacion();
        }
    }

    public void cargarParrillaActualizada() {
        horas = new ArrayList<String>();
        programas = new ArrayList<String>();
        List<Parrilla> parrillaProgramacion = Parrilla.listAll(Parrilla.class);
        for (Parrilla programa : parrillaProgramacion) {
            horas.add(programa.getHorario());
            programas.add(programa.getNombrePrograma());
        }
        ArrayAdapter<String> adapterHora = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, horas);
        listaHora.setAdapter(adapterHora);
        ArrayAdapter<String> adapterPrograma = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, programas);
        listaPrograma.setAdapter(adapterPrograma);
        textoDiaActualizacion.setText(actualizacionLogica.getActualizacionParrilla());
    }

    public static Date GetUTCdatetimeAsDate() {
        return StringDateToDate(GetUTCdatetimeAsString());
    }

    public static String GetUTCdatetimeAsString() {
        final SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        final String utcTime = sdf.format(new Date());
        return utcTime;
    }

    public static Date StringDateToDate(String StrDate) {
        Date dateToReturn = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);
        try {
            dateToReturn = (Date) dateFormat.parse(StrDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateToReturn;
    }

    public void comprobarUltimaActualizacion() {
        manejoProgressDialog.iniciarProgressDialog("Comprobando si hay actualizaciones disponibles...", getActivity());
        conexionString = new conexionGETAPIString();
        conexionString.contexto = getActivity();
        conexionString.delegate = this;
        conexionString.execute("Api/Programa/GetDiaActual");
    }

    public void cargarParrilla() {
        manejoProgressDialog.iniciarProgressDialog("Cargando parrilla de programación...", getActivity());
        conexion = new conexionGETAPIJSONArray();
        conexion.contexto = getActivity();
        conexion.delegate = this;
        TimeZone zonaHoraria = TimeZone.getDefault();
        conexion.execute("Api/Programa/GetContenido?GMT=" + zonaHoraria.getDisplayName(false, TimeZone.SHORT));
    }

    public void mostrarResultados(JSONArray resultados) {
        parrillaLogica.procesarResultados(ultimaActWS, resultados);
        ArrayAdapter<String> adapterHora = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, parrillaLogica.getHoras());
        listaHora.setAdapter(adapterHora);
        ArrayAdapter<String> adapterPrograma = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, parrillaLogica.getProgramas());
        listaPrograma.setAdapter(adapterPrograma);
        textoDiaActualizacion.setText("Actualizado al día: " + actualizacionLogica.getActualizacionParrilla());
        parrillaLogica.setHoras(null);
        parrillaLogica.setProgramas(null);
    }

    public void ultimaActualizacion(String resultado) {
        try {
            resultado = resultado.replace("\"", "");
            final List<Actualizacion> listaActualizaciones = Actualizacion.listAll(Actualizacion.class);
            ultimaActWS = tiempoActual.convertirStringSimple(resultado);
            if (listaActualizaciones != null && listaActualizaciones.size() > 0) {
                Actualizacion ultimaActualizacion = listaActualizaciones.get(0);
                if (ultimaActualizacion.getActParrilla().equals(ultimaActWS) == true) {
                    manejoToast.crearToast(getActivity(), "Parrilla actualizada");
                } else {
                    cargarParrilla();
                }
            } else {
                cargarParrilla();
            }
        } catch (Exception e) {
            Log.e("Parrilla: ultima act", e.getMessage());
        }
        manejoProgressDialog.cancelarProgressDialog();
    }

    @Override
    public void procesoExitoso(JSONArray resultados) {
        swipeRefreshLayout.setRefreshing(false);
        mostrarResultados(resultados);
        manejoProgressDialog.cancelarProgressDialog();
    }

    @Override
    public void procesoExitoso(JSONObject resultado) {
    }

    @Override
    public void procesoExitoso(int codigo, int tipo) {

    }

    @Override
    public void procesoExitoso(String respuesta) {
        swipeRefreshLayout.setRefreshing(false);
        ultimaActualizacion(respuesta);
        manejoProgressDialog.cancelarProgressDialog();
    }

    @Override
    public void procesoNoExitoso() {
        try {
            manejoProgressDialog.cancelarProgressDialog();
            swipeRefreshLayout.setRefreshing(false);
            manejoToast.crearToast(getActivity(), "Error al actualizar la parrilla de programación, intentelo más tarde");
        } catch (Exception e) {
            Log.e("Noticias: noexit", e.getMessage());
        }
    }
}
